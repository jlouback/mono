package abcdCriterium;

import java.util.ArrayList;

import utils.Point;

public class BorderIrregularity {
	
	private ArrayList<Point> points;
	private Point centroid;
	private double meanRadius;
	
	public double returnIrregularityMetric(ArrayList<Point> border) {
		this.points = border;
		findCentroid();
		findMeanRadius();
		return radiiStandardDeviation();
	}
	
	public void findCentroid() {
		int xMean = 0;
		int yMean = 0;
		for(int i=0; i<points.size(); i++) {
			xMean += points.get(i).getX();
			yMean += points.get(i).getY();
		}
		xMean = xMean/points.size();
		yMean = yMean/points.size();
		centroid = new Point(xMean, yMean);
		System.out.println("Centroid coord: " + xMean + " " + yMean);
	}
	
	public void findMeanRadius() {
		int x = 0;
		int y = 0;
		double sumRadius = 0;
		for(int i=0; i<points.size(); i++) {
			x = points.get(i).getX();
			y = points.get(i).getY();
			sumRadius += Math.sqrt( Math.pow(x - centroid.getX(), 2) + Math.pow(y - centroid.getY(), 2)); 
		}
		meanRadius = sumRadius/points.size();
		System.out.println(meanRadius);
	}
	
	public double radiiStandardDeviation() {
		int x = 0;
		int y = 0;
		double distance;
		double variance = 0;
		for(int i=0; i<points.size(); i++) {
			x = points.get(i).getX();
			y = points.get(i).getY();
			distance = (float) Math.sqrt( Math.pow(x - centroid.getX(), 2) + Math.pow(y - centroid.getY(), 2)); 
			variance += Math.pow((distance - meanRadius), 2);
		}
		variance = variance/points.size();
		return Math.sqrt(variance);
	}
	
}
