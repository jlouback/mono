package abcdCriterium;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import utils.CannyEdgeDetector;
import utils.Point;

public class ImageProcessing {
	
	public static void main(String [] args) {
		ArrayList<Point> borderCoordinates = returnBorderCoordinates(new File("mole2.jpeg"));
		BorderIrregularity borderIrregularity = new BorderIrregularity();
		System.out.println("the metric:" + borderIrregularity.returnIrregularityMetric(borderCoordinates));
	}
	
	public static ArrayList<Point> returnBorderCoordinates(File image) {
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
		return detector.getBorderCoordinates();
	}

}
