package ru.kmikhails.accountcare.util;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

	public static void rotateImage(String path) {
		try (InputStream inputStream = PdfConverter.class.getClassLoader().getResourceAsStream(path)) {
			if (inputStream != null) {
				BufferedImage image = ImageIO.read(inputStream);

				final double rads = Math.toRadians(90);
				final double sin = Math.abs(Math.sin(rads));
				final double cos = Math.abs(Math.cos(rads));
				final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
				final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
				final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
				final AffineTransform at = new AffineTransform();
				at.translate(w / 2, h / 2);
				at.rotate(rads, 0, 0);
				at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
				final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				rotateOp.filter(image, rotatedImage);

				ImageIO.write(rotatedImage, "JPG", new File("src/main/resources/" + path));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
