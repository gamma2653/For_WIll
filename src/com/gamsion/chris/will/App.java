package com.gamsion.chris.will;

import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class App {
	boolean recording = false;
	boolean listening = false;
	AudioFileFormat.Type filetype = AudioFileFormat.Type.AU;
	TargetDataLine line;
	WriteThread wt;
	CheckThread ct;
	Thread mainThread;

	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format;
	}

	public void startListening() {
		if (!listening) {
			listening = true;
			try {
				AudioFormat format = getAudioFormat();
				DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

				// checks if system supports the data line
				if (!AudioSystem.isLineSupported(info)) {
					throw new RuntimeException("Line not supported");
				}
				line = (TargetDataLine) AudioSystem.getLine(info);
				line.open(format);
				line.start(); // start capturing

				AudioInputStream ais = new AudioInputStream(line);
				ct = new CheckThread(ais);
				ct.start();
			} catch (LineUnavailableException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void stopListening() {
		if (listening) {
			listening = false;
			ct.run = false;
			line.stop();
			line.close();
		}
	}

	public void startSnippet() {
		if (!recording) {
			recording = true;
			wt = new WriteThread();
			wt.start();
		}
	}

	public void endSnippet() {
		if (recording) {
			recording = false;
			wt.interrupt();
		}
	}

	class WriteThread extends Thread {
		@Override
		public void run() {
			AudioInputStream ais = new AudioInputStream(line);
			OutputStream os;
			try {
				os = Utility.getOutputStream();
				line.flush();
				// start recording
				AudioSystem.write(ais, filetype, os);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class CheckThread extends Thread {
		AudioInputStream ais;
		public boolean run = true;
		public long startTime;

		public CheckThread(AudioInputStream ais) {
			this.ais = ais;
		}

		@Override
		public void run() {
			long timeSinceLastPeak = System.currentTimeMillis();
			startTime = System.currentTimeMillis();
			final int bufferByteSize = 2048;
			byte[] buf = new byte[bufferByteSize];
			float[] samples = new float[bufferByteSize / 2];

			float lastPeak = 0f;

			for (int b; (b = line.read(buf, 0, buf.length)) > -1;) {
				if (!run)
					return;
				// convert bytes to samples here
				for (int i = 0, s = 0; i < b;) {
					if (!run)
						return;
					int sample = 0;

					sample |= buf[i++] << 8;
					sample |= buf[i++] & 0xFF;

					// normalize to range of +/-1.0f
					samples[s++] = sample / 32768f;
				}

				float rms = 0f;
				float peak = 0f;
				for (float sample : samples) {

					float abs = Math.abs(sample);
					if (abs > peak) {
						peak = abs;
					}

					rms += sample * sample;
				}

				rms = (float) Math.sqrt(rms / samples.length);

				if (lastPeak > peak) {
					peak = lastPeak * 0.875f;
				}

				lastPeak = peak;
				System.out.println(lastPeak);
				if (lastPeak > 0.1) {
					timeSinceLastPeak = System.currentTimeMillis();
					if (!recording) {
						Utility.print("Starting recording!");
						App.this.startSnippet();
					}
				}
				if (recording && ((System.currentTimeMillis() - timeSinceLastPeak) > Utility.maxRecTime)) {
					Utility.print("Stopping recording!");
					App.this.endSnippet();
				}
			}

		}
	}

}
