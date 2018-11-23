package com.gamsion.chris.will;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class GUI extends JFrame {
	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;
	// Basic
	static Toolkit tk = Toolkit.getDefaultToolkit();
	static Dimension screensize = tk.getScreenSize();

	// Initialization materials
	JPanel contentPane;
	SpringLayout layout;
	JButton start;
	JButton end;
	JLabel status;
	App app;

	public GUI(String title, App app) {
		super(title);
		this.app = app;
		initialize();
	}

	void initialize() {
		contentPane = new JPanel();
		this.setContentPane(contentPane);
		layout = new SpringLayout();
		contentPane.setLayout(layout);
		// contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		start = new JButton("Start");
		end = new JButton("End");
		status = new JLabel("Stopped.");
		start.addActionListener((e) -> {
			status.setText("Started.");
			app.startListening();
			System.out.println("Started");
		});
		end.addActionListener((e) -> {
			status.setText("Stopped.");
			app.stopListening();
			System.out.println("Ended");
		});
		contentPane.add(start);
		contentPane.add(end);
		contentPane.add(status);
		setupLayout();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(screensize.width / 2, screensize.height / 2));
		this.pack();
		this.setLocationRelativeTo(null);
	}

	void setupLayout() {
		layout.putConstraint(SpringLayout.SOUTH, start, -30, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, start, 20, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, start, 100, SpringLayout.WEST, start);

		layout.putConstraint(SpringLayout.SOUTH, end, -30, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.EAST, end, -20, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.WEST, end, -100, SpringLayout.EAST, end);

		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, status, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
		layout.putConstraint(SpringLayout.NORTH, status, 20, SpringLayout.NORTH, contentPane);

	}
}
