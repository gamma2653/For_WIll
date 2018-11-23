package com.gamsion.chris.will;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Main {
	public static void main(String[] args) {
		final GUI gui = new GUI("For Will", new App());
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (UnsupportedLookAndFeelException ex) {
						Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
					}
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
				gui.setVisible(true);
			}
		});
	}
}
