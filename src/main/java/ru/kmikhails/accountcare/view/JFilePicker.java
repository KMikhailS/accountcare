package ru.kmikhails.accountcare.view;

import javax.swing.*;
import java.awt.*;

public class JFilePicker extends JPanel {
	private String textFieldLabel;
	private String buttonLabel;

	private JLabel label;
	private JTextField textField;
	private JButton button;

	private JFileChooser fileChooser;

	private int mode;
	public static final int MODE_OPEN = 1;
	public static final int MODE_SAVE = 2;

	public JFilePicker(String textFieldLabel, String buttonLabel) {
		this.textFieldLabel = textFieldLabel;
		this.buttonLabel = buttonLabel;

		fileChooser = new JFileChooser();

		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		label = new JLabel(textFieldLabel);

		textField = new JTextField(30);
		button = new JButton(buttonLabel);

		button.addActionListener(e -> buttonActionPerformed());

		add(label);
		add(textField);
		add(button);
	}

	private void buttonActionPerformed() {
		if (mode == MODE_OPEN) {
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} else if (mode == MODE_SAVE) {
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

//	public void addFileTypeFilter(String extension, String description) {
//		FileTypeFilter fileTypeFilter = new FileTypeFilter(extension, description);
//		fileChooser.addChoosableFileFilter(fileTypeFilter);
//	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public String getSelectedFilePath() {
		return textField.getText();
	}
}
