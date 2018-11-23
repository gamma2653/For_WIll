package com.gamsion.chris.will;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public static long maxRecTime = 6000L;
	public static final String AUDIOFOLDER = "audio";
	static final DateFormat df = new SimpleDateFormat("MM_dd_yy");
	static final DateFormat ff = new SimpleDateFormat("HH_mm_ss");

	public static OutputStream getOutputStream() throws IOException {
		generateDateFolder();
		Path p = Paths.get(".", AUDIOFOLDER, df.format(new Date()), ff.format(new Date())+".au");
		print(p);
		p = Files.createFile(p);
		return Files.newOutputStream(p);
	}

	/**
	 * Generates a folder with the df.format(d) (of the format "MM_dd_yy") at the
	 * launch started in directory.
	 */
	public static void generateDateFolder() {

		if (Files.isDirectory(Paths.get(".", AUDIOFOLDER))) {
			if (Files.isDirectory(Paths.get(".", AUDIOFOLDER, df.format(new Date())))) {
				return;
			}
		} else {
			try {
				Files.createDirectory(Paths.get(".", AUDIOFOLDER));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			Files.createDirectory(Paths.get(".", AUDIOFOLDER, df.format(new Date())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void print(Object o) {
		System.out.println(o);
	}

	public static void main(String[] args) {
		try {
			getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
