package ru.kmikhails.accountcare.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PdfConverter {
	public static void main(String[] args) {
		try (InputStream inputStream = PdfConverter.class.getClassLoader().getResourceAsStream("check_3.pdf");
			 PDDocument document = PDDocument.load(inputStream)) {
			PDFRenderer renderer = new PDFRenderer(document);
			BufferedImage bufferedImage = renderer.renderImage(0);
			ImageIO.write(bufferedImage, "JPEG", new File("check_3.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
