package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import foreignContributions.CannyEdgeDetector;
import foreignContributions.QuickHullModifier;
import foreignContributions.SymmetryDetector;

public class AsymmetryTest {
	
	//This is to visualize the effects of the algorithm
	public static void main(String [] args) {
		AsymmetryTest aT = new AsymmetryTest();
		File file = new File("nevus.jpg");
		CannyEdgeDetector detector = new CannyEdgeDetector();
		detector.setLowThreshold(5f);
		detector.setHighThreshold(12f);
		try {
			detector.setSourceImage(ImageIO.read(file));
		} catch (IOException e) {
			System.out.println("Image not found.");
			e.printStackTrace();
		}
		detector.process();
		
		BufferedImage edgeIm = detector.getEdgesImage();
		try {
			ImageIO.write(edgeIm, "png", new File("edges.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		QuickHullModifier quickHull = new QuickHullModifier();
		List<Point2D> points = aT.convertPointToPoint2D(detector.getBorderCoordinates());
		List<Point2D> convexHull = quickHull.quickHull(points);
		Point2D firstPoint = convexHull.get(0);
		BufferedImage bImage = new BufferedImage(detector.getEdgesImage().getWidth(), detector.getEdgesImage().getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		BufferedImage bImage2 = new BufferedImage(detector.getEdgesImage().getWidth(), detector.getEdgesImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bImage.createGraphics();
		Graphics2D g2 = bImage2.createGraphics();
		
		g.setColor(Color.GRAY);
		g2.setColor(Color.BLUE);
		//Avoiding Null Pointer
		Point2D lastPoint = firstPoint;
		Polygon polygon = new Polygon();
		for(Point2D point: convexHull){
			polygon.addPoint((int)point.getX(), (int)point.getY());
			g2.drawLine((int)point.getX(), (int)point.getY(), (int)lastPoint.getX(), (int)lastPoint.getY());
						
			lastPoint = point;
			
		}
		
		g2.drawLine((int)lastPoint.getX(), (int)lastPoint.getY(), (int)firstPoint.getX(), (int)firstPoint.getY());
		g.drawPolygon(polygon);
		g.fillPolygon(polygon);
		g.dispose();
		File processed = new File("quickHull2.png");
		try {
			ImageIO.write(bImage, "png", processed);
		} catch (IOException e) {
			System.out.println("Unable to save image.");
			e.printStackTrace();
		}
		
		aT.runSymmetryDetector("quickHull2.png");
		g2.dispose();
		BufferedImage original = detector.getSourceImage();
		BufferedImage combined = new BufferedImage(detector.getEdgesImage().getWidth(), detector.getEdgesImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g3 = combined.getGraphics();
		
		g3.drawImage(original, 0, 0, null);
		g3.drawImage(bImage2, 0, 0, null);
			File processed2 = new File("polygon2.png");
			try {
				ImageIO.write(combined, "png", processed2);
			} catch (IOException e) {
				System.out.println("Unable to save image.");
				e.printStackTrace();
			}

		}
		
		
	//Pourre's quick hull algorithm uses a custom class Point2D
	public List<Point2D> convertPointToPoint2D(List<Point> points) {
		List<Point2D> listPoint2D = new ArrayList<Point2D>();
		for(Point point:points) {
			listPoint2D.add(new Point2D(point.getX(),point.getY()));
		}
		return listPoint2D;
	}

	public void runSymmetryDetector(String name) {
		//obtain our image
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//create the detector
		SymmetryDetector detector = new SymmetryDetector();
		//optionally adjust parameters here
		detector.setScoreThreshold(0.9f);
		//strictly optional: observe the algorithm running
		BufferedImage obs = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		detector.setObservation(obs.createGraphics());
		//run the detector - symmetries are available from the returned object
		SymmetryDetector.Symmetry symmetry = detector.detectSymmetry(image);
		//the image obs will contain a graphical summary of execution
		File processed = new File("assymetry2.png");
		try {
			ImageIO.write(obs, "png", processed);
		} catch (IOException e) {
			System.out.println("Unable to save image.");
			e.printStackTrace();
		}
	}

}
