package abcdCriterium;

import java.util.ArrayList;

import utils.Point;

public class BorderIrregularity {
	
	private ArrayList<Point> points;
	private Point centroid;
	private double meanRadius;
	private double standardDeviation;
	
	public double returnIrregularityMetric(ArrayList<Point> border) {
		this.points = border;
		findCentroid();
		findMeanRadius();
		return radiiCoefficientOfVariance();
	}

	public void findCentroid() {
		int xMax = 0;
		int xMin = points.get(0).getX();
		int yMax = 0;
		int yMin = points.get(0).getY();
		for(int i=0; i<points.size(); i++) {
			if(xMax<points.get(i).getX()){
				xMax = points.get(i).getX();
			}
			if(xMin>points.get(i).getX()){
				xMin = points.get(i).getX();
			}
			if(yMax<points.get(i).getY()){
				yMax = points.get(i).getY();
			}
			if(yMin>points.get(i).getY()){
				yMin = points.get(i).getY();
			}
			
		}
		int x = xMin + ((xMax - xMin)/2);
		int y = yMin + ((yMax - yMin)/2);
		centroid = new Point(x, y);
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
	}
	
	public double radiiCoefficientOfVariance() {
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
		standardDeviation = Math.sqrt(variance);
		return standardDeviation/meanRadius;
	}
	
}
