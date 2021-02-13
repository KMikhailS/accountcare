package ru.kmikhails.accountcare.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ImageScaler extends JPanel {
	private static int i = 0;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.add(new ImageScaler(), BorderLayout.CENTER);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		BufferedImage image = loadImage("cat_1.jpg");
		JLabel label = new JLabel();
		Graphics2D graphics2D = (Graphics2D) image.getGraphics();
		AffineTransform transform = AffineTransform.getTranslateInstance(100, 100);
		graphics2D.drawImage(image, transform, label);

		label.setIcon(new ImageIcon(image));

		frame.add(label);

		JPanel panel = new JPanel();
		JButton button = new JButton("Start");
		button.addActionListener(e -> {
			i += 90;
		});
		panel.setLayout(new FlowLayout());
		panel.add(button);
		frame.add(panel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

//	public void paintComponent(Graphics graphics) {
//		BufferedImage image = loadImage("cat_1.jpg");
//		AffineTransform transform = AffineTransform.getTranslateInstance(100, 100);
//		transform.rotate(Math.toRadians(i), image.getHeight() / 2, image.getWidth() / 2);
//		Graphics2D graphics2D = (Graphics2D) graphics;
//		transform.scale(0.5, 0.5);
//
//
////		graphics2D.dispose();
//		graphics2D.drawImage(image, transform, null);
//
//		repaint();
////		revalidate();
//	}


	private static BufferedImage loadImage(String filepath) {
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
