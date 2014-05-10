package abcdCriterium;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import utils.Point;
import utils.Point2D;
import foreignContributions.CannyEdgeDetector;
import foreignContributions.QuickHullModifier;
import foreignContributions.SymmetryDetector;

public class Asymmetry {
	
	public static void main(String[] args) throws IOException {
		Asymmetry ass = new Asymmetry();
		System.out.println(ass.returnAsymmetryMetric("nevus.jpg"));
	}


	public int returnAsymmetryMetric(String filename) throws IOException {
	
			//Get name of image with polygon representing lesion area
			String polygonImage = runQuickHull(filename);
			File file = new File(polygonImage);
			
			// Run symmetry detector on image of polygon
			//create the detector
			SymmetryDetector detector = new SymmetryDetector();
			detector.setScoreThreshold(0.9f);
			//run the detector - symmetries are available from the returned object
			SymmetryDetector.Symmetry symmetry = detector.detectSymmetry(ImageIO.read(file));
			
			//Delete generated polygon Image
			file.delete();
			
			//The asymetry metric is the number of symmetries found.
			int asymmetryMetric = symmetry.getAngles().length;
			return asymmetryMetric;
	}


	public String runQuickHull(String filename) throws IOException {
		BufferedImage image = ImageIO.read(new File(filename));
		//Create polygon encapsulating lesion border points with Pourre's quick hull algorithm
		QuickHullModifier quickHull = new QuickHullModifier();
		//Pourre's quick hull algorithm uses custom class Point2D
		List<Point2D> point2D = Point2D.convertPoint2D(getEdgeCoordinates(filename));
		List<Point2D> convexHull = quickHull.quickHull(point2D);
		//Draw polygon image to be used in symmetry detector
		BufferedImage polygonImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D polygonGraphics = polygonImage.createGraphics();
		polygonGraphics.setColor(Color.GRAY);
		Point2D firstPoint = convexHull.get(0);
		Point2D lastPoint = firstPoint;
		Polygon polygon = new Polygon();
		for(Point2D point: convexHull){
			polygon.addPoint((int)point.getX(), (int)point.getY());		
			lastPoint = point;
		}
		polygonGraphics.drawPolygon(polygon);
		polygonGraphics.fillPolygon(polygon);
		polygonGraphics.dispose();
		
		//Save image of polygon
		String polygonFile = "quickHull.png";
		File savedPolygon = new File(polygonFile);
		try {
			ImageIO.write(polygonImage, "png", savedPolygon);
		} catch (IOException e) {
			System.out.println("Unable to save image.");
			e.printStackTrace();
		}
		return polygonFile;
	}
	
	public List<Point> getEdgeCoordinates(String filename) {
		//Get edges of image
		CannyEdgeDetector edgeDetector = new CannyEdgeDetector();
		edgeDetector.setLowThreshold(5f);
		edgeDetector.setHighThreshold(12f);
		try {
			edgeDetector.setSourceImage(ImageIO.read(new File(filename)));
		} catch (IOException e) {
			System.out.println("Image not found.");
			e.printStackTrace();
		}
		edgeDetector.process();
		return edgeDetector.getBorderCoordinates();
	}

}
