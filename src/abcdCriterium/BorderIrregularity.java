package abcdCriterium;

import imagePrep.ImageRotation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BorderIrregularity {
	
	public static void main(String[] args) {
		File file = new File("mole2.jpeg");
		BorderIrregularity border = new BorderIrregularity();
		border.EdgeDetector(file);
	}
	
	private void EdgeDetector(File image) {
		CannyEdgeDetector detector = new CannyEdgeDetector();
		detector.setLowThreshold(2f);
		detector.setHighThreshold(9.5f);
		try {
			detector.setSourceImage(ImageIO.read(image));
		} catch (IOException e) {
			System.out.println("Image not found.");
			e.printStackTrace();
		}
		detector.process();
		BufferedImage edges = detector.getEdgesImage();
		File processed = new File("Mole2-L2-H9.5.png");
		try {
			ImageIO.write(edges, "png", processed);
		} catch (IOException e) {
			System.out.println("Unable to save image.");
			e.printStackTrace();
		}
	}

}
