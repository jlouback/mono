package utils;

import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.linear.Point2D;

public class PointListHelper {

	public static List<Point2D> cloneList(List<Point2D> points){
		
		List<Point2D> clone = new ArrayList<Point2D>();
		
		for(Point2D point: points){
			clone.add(new Point2D(point.getX(), point.getY()));
		}
		
		return clone;
		
	}
	
	public static List<Point2D> convertPoint2D(List<Point> points) {
		List<Point2D> listPoint2D = new ArrayList<Point2D>();
		for(Point point:points) {
			listPoint2D.add(new Point2D(point.getX(),point.getY()));
		}
		return listPoint2D;
	}
	
}
