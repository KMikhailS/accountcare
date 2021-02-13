package ru.kmikhails.accountcare.view;

import ru.kmikhails.accountcare.util.ImageScaler;
import ru.kmikhails.accountcare.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ImagePanel extends JPanel {
	private static JLabel label;
	private static JScrollPane scrollPane;
	private static JFrame frame;
	private static JPanel panel;

	public static void main(String[] args) {
//		SwingUtilities.invokeLater(ImagePanel::new);
		init();
	}

	public static void init() {
		frame = new JFrame();
		panel = new ImagePanel();
		frame.add(panel);
		List<String> images = new ArrayList<>();
//		images.add("cat_1.jpg");
//		images.add("cat_2.jpg");
		images.add("check_3.jpg");
		images.add("check_1.jpeg");
//		images.add("check_2.pdf");
		ListIterator<String> imageIterator = images.listIterator();
		String path = images.get(0);
		frame.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		if (images.size() > 1) {
			JButton nextButton = new JButton("Next");
			nextButton.addActionListener(e -> {
				if (imageIterator.hasNext()) {
					frame.remove(label);
					imageIterator.next();
					paint(imageIterator.next());
					frame.repaint();
				}
			});
			JButton prevButton = new JButton("Prev");
			prevButton.addActionListener(e -> {
				if (imageIterator.hasPrevious()) {
					frame.remove(label);
					imageIterator.previous();
					paint(imageIterator.previous());
					frame.repaint();
				}
			});
			buttonPanel.add(prevButton);
			buttonPanel.add(nextButton);
			buttonPanel.setPreferredSize(new Dimension(0, 30));
		}
		frame.add(buttonPanel, BorderLayout.SOUTH);
		JButton rotateButton = new JButton("Rotate");
		rotateButton.addActionListener(e -> {
			ImageUtil.rotateImage(path);
			frame.remove(label);
			paint(path);
			frame.repaint();
		});
		buttonPanel.add(rotateButton);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(750, 1000);
		paint(path);
//		frame.setVisible(true);
//		frame.setLocationRelativeTo(null);
//		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

//	@Override
//	public void paintComponent(Graphics graphics) {
//		BufferedImage image = loadImage("check_1.jpeg");
//		AffineTransform transform = AffineTransform.getTranslateInstance(100, 100);
//		transform.rotate(Math.toRadians(0), image.getHeight() / 2, image.getWidth() / 2);
//		Graphics2D graphics2D = (Graphics2D) graphics;
//		transform.scale(0.5, 0.5);
//
//		graphics2D.drawImage(image, transform, null);
////		repaint();
//	}


	static void paint(String paths) {
		Image image;
		try {
			InputStream inputStream = ImageScaler.class.getClassLoader().getResourceAsStream(paths);
//			image = ImageIO.read(inputStream);
			BufferedImage bi = ImageIO.read(inputStream);
			image = bi.getScaledInstance(1500, 1000, Image.SCALE_SMOOTH);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Ошибка при чтении скана",
					"Ошибка", JOptionPane.ERROR_MESSAGE);
			throw new IllegalArgumentException("Image does not read");
		}
		if (image != null) {
			frame.setSize(new Dimension(image.getWidth(null), image.getHeight(null)));
			label = new JLabel(new ImageIcon(image));
//			scrollPane = new JScrollPane(label);
//			frame.add(scrollPane);
			frame.add(label);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
	}

	private BufferedImage loadImage(String filepath) {
		BufferedImage bufferedImage = null;
		try {
			InputStream inputStream = ImageScaler.class.getClassLoader().getResourceAsStream(filepath);
			bufferedImage = ImageIO.read(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufferedImage;
	}
}
