package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EdgeTest {

	public static void main(String[] args) {
		File file = new File("mole.jpg");
		EdgeTest border = new EdgeTest();
		border.EdgeDetector(file);
	}

	private void EdgeDetector(File image) {
		CannyEdgeDetector detector = new CannyEdgeDetector();
		detector.setLowThreshold(1f);
		detector.setHighThreshold(8.5f);
		try {
			detector.setSourceImage(ImageIO.read(image));
		} catch (IOException e) {
			System.out.println("Image not found.");
			e.printStackTrace();
		}
		detector.process();
		System.out.println("Data size: " + detector.getBorderCoordinates().size());

		BufferedImage bImage = new BufferedImage(detector.getEdgesImage().getWidth(), detector.getEdgesImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bImage.createGraphics();
		g2d.setColor(Color.RED);
		for(int i=0; i<detector.getBorderCoordinates().size(); i++) {
			int x = detector.getBorderCoordinates().get(i).getX();
			int y = detector.getBorderCoordinates().get(i).getY();
			g2d.drawOval(x, y, 3, 3);
			g2d.fillOval(x, y, 3, 3);
		}
//		g2d.drawOval(146, 69, 10, 10);
//		g2d.fillOval(146, 69, 10, 10);
		g2d.dispose();

			File processed = new File("COORDINATES.png");
			try {
				ImageIO.write(bImage, "png", processed);
			} catch (IOException e) {
				System.out.println("Unable to save image.");
				e.printStackTrace();
			}

		}

	}
