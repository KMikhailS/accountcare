package ru.kmikhails.accountcare.view;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class SaveScanForm extends JFrame {
//	private JLabel URLLabel = new JLabel("Download URL: ");
//	private JTextField URLField = new JTextField(30);
	private JFilePicker filePicker = new JFilePicker(new Font(null, Font.PLAIN, 14), "Открыть");

	private JButton saveButton = new JButton("Сохранить");
	private JButton quitButton = new JButton("Выход");

//	private JLabel filenameLabel = new JLabel("File name: ");
//	private JTextField filenameField = new JTextField(20);

//	private JLabel fileSizeLabel = new JLabel("File size(bytes): ");
//	private JTextField fileSizeField = new JTextField(20);

//	private JLabel progressLabel = new JLabel("Progress:");
//	private JProgressBar progressBar = new JProgressBar(0, 100);

	public SaveScanForm(String accountNumber, String accountDate) {
		super("Сохранение скана");

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);

		filePicker.setMode(JFilePicker.MODE_OPEN);
//		filePicker.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		saveButton.addActionListener(e -> saveScan(filePicker.getSelectedFilePath(), accountNumber, accountDate));

//		filenameField.setEditable(false);
//		fileSizeField.setEditable(false);

//		progressBar.setPreferredSize(new Dimension(200, 30));
//		progressBar.setStringPainted(true);

		constraints.gridx = 0;
		constraints.gridy = 0;
//		add(URLLabel, constraints);

		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
//		add(URLField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 0.0;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.NONE;
		add(filePicker, constraints);

		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		add(saveButton, constraints);

		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		add(quitButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
//		add(filenameLabel, constraints);

		constraints.gridx = 1;
//		add(filenameField, constraints);

		constraints.gridy = 4;
		constraints.gridx = 0;
//		add(fileSizeLabel, constraints);

		constraints.gridx = 1;
//		add(fileSizeField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
//		add(progressLabel, constraints);

		constraints.gridx = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
//		add(progressBar, constraints);

		pack();
		setLocationRelativeTo(null);    // center on screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void saveScan(String path, String accountNumber, String accountDate) {
		File sourceFile = new File(path);
		File destinationFile = new File("/home/mkrivoshchapov/" + accountNumber + "_" + accountDate + ".jpg");
		if (destinationFile.exists()) {
			destinationFile = new File("/home/mkrivoshchapov/" + accountNumber + "_" + accountDate + "_1.jpg");
		}
		try {
			FileUtils.copyFile(sourceFile, destinationFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Ошибка сохранения скана",
					"Ошибка", JOptionPane.ERROR_MESSAGE);
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		JOptionPane.showMessageDialog(this, "Скан сохранён",
				"Сохранение", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new SaveScanForm("1234", "2020-01-01"));
	}
}
