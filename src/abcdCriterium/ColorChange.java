package abcdCriterium;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import utils.Point;
import utils.PointListHelper;
import foreignContributions.CannyEdgeDetector;
import utils.ColorUtils;

import br.com.etyllica.linear.Point2D;
import br.com.etyllica.motion.filter.color.ColorStrategy;
import br.com.etyllica.motion.modifier.QuickHullModifier;

public class ColorChange {

	public static void main(String[] args) throws IOException {
		ColorChange c = new ColorChange();
		System.out.println(c.returnColorChangeMetric("N1343_RU.jpg"));
	}


	public double returnColorChangeMetric(String filename) throws IOException {
		//Load the image to be analyzed
		BufferedImage bImage = ImageIO.read(new File(filename));
		//Separate lesion pixels from image:
		List<Point2D> lesion = getLesion(bImage);
		//Get average Delta E
		return getAvgDeltaE(lesion, bImage);
	}

	public List<Point2D> getLesion(BufferedImage image) throws IOException {

		List<Point2D> convexHull;
		//Get coordinates of edges - Note: Pourre's quick hull algorithm uses custom class Point2D
		List<Point2D> edges = PointListHelper.convertPoint2D(getEdgeCoordinates(image));

		//Apply quick hull to create polygon containing all detected edges.
		QuickHullModifier convexHullModifier = new QuickHullModifier();
		convexHull = convexHullModifier.quickHull(edges);
		//Return coordinates of all points in lesion;
		return convexHull;
	}

	public List<Point> getEdgeCoordinates(BufferedImage image) {
		//Get edges of image
		CannyEdgeDetector edgeDetector = new CannyEdgeDetector();
		edgeDetector.setLowThreshold(6f);
		edgeDetector.setHighThreshold(13f);
		edgeDetector.setSourceImage(image);
		edgeDetector.process();
		if(edgeDetector.getBorderCoordinates().size() < 3) {
			edgeDetector.setLowThreshold(2f);
			edgeDetector.setHighThreshold(8f);
			edgeDetector.process();
		}
		return edgeDetector.getBorderCoordinates();
	}

	public double getAvgDeltaE(List<Point2D> lesion, BufferedImage image) {
		double deltaE = 0;
		int[] avgLab;
		int[] lab;
		//Get average color of lesion
		Color avgColor = ColorUtils.getAverageColor(lesion, image);
		//convert avgColor from RGB to L*a*b 
		avgLab = ColorUtils.rgb2lab(avgColor.getRed(), avgColor.getGreen(), avgColor.getBlue());
		int R, G, B;
		int points = lesion.size();
		for(int i=0; i<points; i++) {
			//Get rgb of a single pixel
			int rgb = image.getRGB((int)lesion.get(i).getX(), (int)lesion.get(i).getY());
			// separate rgb values
			R = ColorStrategy.getRed(rgb);
			G = ColorStrategy.getGreen(rgb);
			B = ColorStrategy.getBlue(rgb);
			//convert pixel color from RGB to L*a*b 
			lab = ColorUtils.rgb2lab(R, G, B);
			
			//Calculate delta E 
			deltaE += Math.sqrt(Math.pow(avgLab[0]-lab[0], 2) + Math.pow(avgLab[1]-lab[1], 2) + Math.pow(avgLab[2]-lab[2], 2)); 
		}
		//Return average delta E
		return deltaE/points;
	}

}
