package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import foreignContributions.CannyEdgeDetector;
import abcdCriterium.BorderIrregularity;

public class EdgeTest {

	public static void main(String[] args) {
		File file = new File("mole2.jpeg");
		EdgeTest border = new EdgeTest();
		border.EdgeDetector(file);
	}

	private void EdgeDetector(File image) {
		CannyEdgeDetector detector = new CannyEdgeDetector();
		detector.setLowThreshold(2f);
		detector.setHighThreshold(9f);
		try {
			detector.setSourceImage(ImageIO.read(image));
		} catch (IOException e) {
			System.out.println("Image not found.");
			e.printStackTrace();
		}
		detector.process();
		System.out.println("Data size: " + detector.getBorderCoordinates().size());
		BorderIrregularity border = new BorderIrregularity();
		System.out.println(border.returnIrregularityMetric(detector.getBorderCoordinates()));

		BufferedImage bImage = new BufferedImage(detector.getEdgesImage().getWidth(), detector.getEdgesImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bImage.createGraphics();
		g2d.setColor(Color.WHITE);
		for(int i=0; i<detector.getBorderCoordinates().size(); i++) {
			int x = detector.getBorderCoordinates().get(i).getX();
			int y = detector.getBorderCoordinates().get(i).getY();
			g2d.drawOval(x, y, 2, 2);
			g2d.fillOval(x, y, 2, 2);
		}
		g2d.drawOval(117, 77, 3, 3);
		g2d.fillOval(117, 77, 3, 3);
		g2d.dispose();
		BufferedImage original = detector.getSourceImage();
		BufferedImage combined = new BufferedImage(detector.getEdgesImage().getWidth(), detector.getEdgesImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = combined.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.drawImage(bImage, 0, 0, null);
			File processed = new File("withCV2.png");
			try {
				ImageIO.write(combined, "png", processed);
			} catch (IOException e) {
				System.out.println("Unable to save image.");
				e.printStackTrace();
			}

		}

	}
