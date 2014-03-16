package foreignContributions;

import static java.lang.Math.PI;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <p><em>This software has been released into the public domain.
 * <strong>Please read the notes in this source file for additional information.
 * </strong></em></p>
 * 
 * @author Tom Gibara
 *
 */

public class SymmetryDetector {

    // statics

    private static int drawString(Graphics2D g, String string, int x, int y) {
        AffineTransform identity = new AffineTransform();
        g.setTransform(identity);
        g.setColor(Color.BLACK);
        Font font = Font.decode("Arial-BOLD-12");
        GlyphVector vector = font.createGlyphVector(g.getFontRenderContext(), string);
        Shape shape = vector.getOutline();
        g.setStroke(new BasicStroke(3));
        g.setFont(font);
        g.translate(x, y + g.getFontMetrics().getAscent());
        g.draw(shape);
        g.setColor(Color.WHITE);
        g.fill(shape);
        g.setTransform(identity);
        return y + g.getFontMetrics().getHeight();
    }

    // fields

    private Graphics2D observation = null;
    private byte backgroundValue = 0;
    private byte foregroundValue = (byte) 0xff;
    private int resolution = 64;
    private int valueThreshold = -1;
    private int radiusCount = 10;
    private int maximumRadius = -2;
    private float scoreThreshold = 0.8f;
    private double angularAliasing = PI/18; //10 degrees
    
    // accessors
    
    /**
     * The value of background pixels. Any value that is not equal to this
     * value is treated as a pixel of the object. This object can automatically
     * identify pixels as background-pixels subject to a specified value
     * threshold. The default value is 0.
     * 
     * @param backgroundValue the value of background pixels
     */
    
    public void setBackgroundValue(byte backgroundValue) {
        this.backgroundValue = backgroundValue;
        foregroundValue = (byte) ~backgroundValue;
    }
    
    /**
     * @return the value of background pixels
     */
    
    public byte getBackgroundValue() {
        return backgroundValue;
    }

    /**
     * If a non-null graphics context is supplied to this method. The object
     * will render its computations. It is expected (but not required) that the
     * supplied graphics context matches the dimensions of the image supplied to
     * detectSymmetry.
     * 
     * @param observation a graphics context through which the algorithm may be
     * observed
     */
    
    public void setObservation(Graphics2D observation) {
        this.observation = observation;
    }
    
    /**
     * @return the observing graphics context
     */
    
    public Graphics2D getObservation() {
        return observation;
    }

    /**
     * This object can threshold the grayscale image as the algorithm executes.
     * This is slower than operating the algorithm on pre-thresholded data, but
     * probably faster than thresholding the data separately. The threshold
     * should be in the range 0-255. Supplying a threshold of -1 disables the
     * thresholding. The default value is -1.
     * 
     * @param threshold the threshold that separates background/foreground pixels
     */
    
    public void setValueThreshold(int threshold) {
    	if (threshold < -1 || threshold > 255) throw new IllegalArgumentException();
        this.valueThreshold = threshold;
    }
    
    /**
     * @return the threshold that separates background/foreground pixels or -1
     */
    
    public int getValueThreshold() {
        return valueThreshold;
    }
    
    /**
     * The number of angles examined for symmetry within an arc of PI radians.
     * Must be strictly positive. The default value is 64
     * 
     * @param resolution the angular resolution
     */
    
    public void setResolution(int resolution) {
    	if (resolution < 1) throw new IllegalArgumentException();
        this.resolution = resolution;
    }
    
    /**
     * @return the angular resolution
     */
    
    public int getResolution() {
        return resolution;
    }
    
    /**
     * The number of radii used to test symmetry. Must be strictly positive.
     * The default value is 10.
     * 
     * @param radiusCount the number of radii tested
     */
    
    public void setRadiusCount(int radiusCount) {
    	if (radiusCount < 1) throw new IllegalArgumentException();
        this.radiusCount = radiusCount;
    }
    
    /**
     * @return the number of radii tested
     */
    
    public int getRadiusCount() {
        return radiusCount;
    }
    
    /**
     * If an approximate object radius (distance from the centroid to the
     * furthest object pixel) is already known, it can be supplied to this
     * object, otherwise the object radius is calculated as part of the
     * algorithm. Supplying a value of -1 indicates that a very fast
     * approximation should be used. Supplying a value of -2 indicates that the
     * radius should be measured. The default value is -2.
     * 
     * @param maximumRadius the approximate object radius or -1 or -2
     */
    
    public void setMaximumRadius(int maximumRadius) {
    	if (maximumRadius < -2) throw new IllegalArgumentException();
        this.maximumRadius = maximumRadius;
    }
    
    /**
     * @return the approximate object radius or -1 or -2
     */
    
    public int getMaximumRadius() {
        return maximumRadius;
    }

    /**
     * A value between 0 and 1 that controls the strictness with which axes of
     * symmetry are judged. The appropriate value for this parameter is entirely
     * dependent on the application of this algorithm. The default value is 0.9.
     * 
     * @param scoreThreshold a value between 0 and 1
     */
    
    public void setScoreThreshold(float scoreThreshold) {
    	if (scoreThreshold < 0 || scoreThreshold > 1) throw new IllegalArgumentException();
        this.scoreThreshold = scoreThreshold;
    }
    
    /**
     * @return a value between 0 and 1
     */
    
    public float getScoreThreshold() {
        return scoreThreshold;
    }

    /**
     * To suppress the clustering of almost identical angles of symmetry,
     * the algorithm attempts to merge adjacent angles into one more accurate
     * response. This paramter specifies (an approximate) angle of separation
     * below which axes of symmetry are combined. The default value is Pi/18
     * (10 degrees).
     * 
     * @param angularAliasing the least angle between axes of symmetry in radians
     */
    
    public void setAngularAliasing(double angularAliasing) {
    	if (angularAliasing < 0 || angularAliasing > PI) throw new IllegalArgumentException();
		this.angularAliasing = angularAliasing;
	}
    
    /**
     * @return the least angle between axes of symmetry in radians
     */
    
    public double getAngularAliasing() {
		return angularAliasing;
	}
    
    //methods

    /**
     * Attempts to identify the axes of reflectional symmetry within the
     * supplied image. The image must have type BufferedImage.TYPE_BYTE_GRAY.
     * If the image has not already been thresholded to two values, a
     * valueThreshold must have been set on this object.
     * 
     * @param image the image containing the object
     * @return the identified symmetry
     */
    
    public Symmetry detectSymmetry(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        byte[] pixels = (byte[]) image.getData().getDataElements(0,0,width,height, null);

        if (observation != null) {
            observation.drawImage(image, null, 0, 0);
        }

        return detectSymmetry(width, height, pixels);
    }

    /**
     * Attempts to identify the axes of reflectional symmetry within the
     * supplied pixel data. If the data contains more than two values, a
     * valueThreshold must have been set on this object.
     * 
     * @param width the number of columns of pixel data
     * @param height the number of rows of pixel data
     * @param pixels an array containing the grayscale pixel data
     * @return the identified symmetry
     */
    
    public Symmetry detectSymmetry(int width, int height, byte[] pixels) {
        if (observation != null) {
            observation.setStroke(new BasicStroke(1));
            observation.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            observation.setColor(Color.BLUE);
        }

        //TODO resolve order of thresholding
        if (observation != null) {
            int offset = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    byte b = valueThreshold < 0 ? pixels[offset] : (pixels[offset] & 0xff) > valueThreshold ? backgroundValue : foregroundValue; 
                    if (b != backgroundValue) {
                        observation.drawLine(x, y, x, y);
                    }
                    offset++;
                }
            }
        }
        
        if (observation != null) {
            observation.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            observation.setStroke(new BasicStroke(2));
            observation.setColor(Color.GREEN);
        }

        //find the centroid
        final double centerX;
        final double centerY;
        {
            int count = 0;
            int sumX = 0;
            int sumY = 0;
            {
                int offset = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        byte b = valueThreshold < 0 ? pixels[offset] : (pixels[offset] & 0xff) > valueThreshold ? backgroundValue : foregroundValue; 
                        if (b != backgroundValue) {
                            sumX += x;
                            sumY += y;
                            count++;
                        }
                        offset++;
                    }
                }
            }
            centerX = sumX / (double) count;
            centerY = sumY / (double) count;
        }

        if (observation != null) {
            observation.drawLine((int) centerX - 10, (int) centerY, (int) centerX + 10, (int) centerY);
            observation.drawLine((int) centerX, (int) centerY - 10, (int) centerX, (int) centerY + 10);
        }

        //compute the maximum radius if necessary
        double maxRadius;
        if (maximumRadius == -1) {
            maxRadius = Math.max(width, height) / 2.0;
        } else if (maximumRadius == -2) {
            int cx = (int) round(centerX);
            int cy = (int) round(centerY);
            int furthestX = cx;
            int furthestY = cy;
            int furthestD = 0;
            //TODO this could be heavily optimized
            int offset = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    byte b = valueThreshold < 0 ? pixels[offset] : (pixels[offset] & 0xff) > valueThreshold ? backgroundValue : foregroundValue; 
                    if (b != backgroundValue) {
                        int d = (x-cx)*(x-cx)+(y-cy)*(y-cy);
                        if (d > furthestD) {
                            furthestX = x;
                            furthestY = y;
                            furthestD = d;
                        }
                    }
                    offset++;
                }
            }
            maxRadius = sqrt(furthestD);
            if (observation != null) {
                observation.drawLine(furthestX - 10, (int) furthestY - 10, (int) furthestX + 10, (int) furthestY + 10);
                observation.drawLine(furthestX + 10, (int) furthestY - 10, (int) furthestX - 10, (int) furthestY + 10);
            }
        } else {
            maxRadius = maximumRadius;
        }
        
        //choose the radii
        double[] radii = new double[radiusCount];
        double radiusQuotient = radiusCount + 1;
        for (int i = 0; i < radiusCount; i++) {
            radii[i] = (i + 1) * maxRadius / radiusQuotient;
        }

        if (observation != null) {
            for (double radius : radii) {
                observation.drawOval((int)(centerX - radius), (int)(centerY - radius), (int)(2 * radius), (int)(2 * radius));
            }
        }

        //identify the angles
        double[] angles = judge(width, height, pixels, centerX, centerY, radii);
        
        if (observation != null) {
            for (double angle : angles) {
                double dx = maxRadius * cos(angle);
                double dy = - maxRadius * sin(angle);
                observation.drawLine((int)(centerX - dx), (int)(centerY - dy), (int)(centerX + dx), (int)(centerY + dy));
            }
        }
        
        //return an object
        Symmetry symmetry = new Symmetry(centerX, centerY, angles);
        
        if (observation != null) {
        	int y = 5;
        	observation.setComposite(AlphaComposite.SrcOver);
        	y = drawString(observation, String.format("Angular Resolution: %d", resolution), 5, y);
        	y = drawString(observation, String.format("Angular Aliasing: %1.2f", angularAliasing), 5, y);
        	y = drawString(observation, String.format("Max Radius: %3.1f", maxRadius), 5, y);
        	y = drawString(observation, String.format("Threshold: %1.2f", scoreThreshold), 5, y);
        	y = drawString(observation, String.format("Radii: %d", radiusCount), 5, y);
        	//TO fit in image
        	String[] strings = symmetry.toString().split("\\[");
        	String symmetry1 = strings[0];
        	drawString(observation, "Symmetry: " + symmetry1, 5, height-25);
        }
        
		return symmetry;
    }

    private double[] judge(final int width, final int height, final byte[] pixels, final double centerX, final double centerY, final double[] radii) {
    	
    	//establish state and constants
    	final int[] scores = new int[resolution];
        final byte[] samples = new byte[resolution * 2];
        final double alpha = PI / resolution;
        final double beta = alpha / 2.0;

        //take samples at each radius
        for (double radius : radii) {
            for (int i = -resolution; i < resolution; i++) {
                double theta = beta + alpha * i;
                double x = centerX + radius * cos(theta);
                double y = centerY - radius * sin(theta);
                int coordX = (int) Math.round(x);
                int coordY = (int) Math.round(y);
                if (coordX < 0 || coordX >= width || coordY < 0 || coordY >= height) {
                    samples[i + resolution] = backgroundValue;
                } else {
                    int offset = coordX + width * coordY;
                    byte b = valueThreshold < 0 ? pixels[offset] : (pixels[offset] & 0xff) > valueThreshold ? backgroundValue : foregroundValue; 
                    samples[i + resolution] = b;
                }
            }
            
            score(samples, scores);
        }
        
        //identify candidates
        int benchmark = round(scoreThreshold * 2 * resolution * radii.length);
        List<Integer> candidates = new ArrayList<Integer>();
        for (int i = 0; i < scores.length; i++) {
            int j = i == 0 ? scores.length - 1 : i -1;
            int k = i == scores.length - 1 ? 0 : i + 1;
            int score = scores[i];
            if (score >= benchmark && score >= scores[j] && score >= scores[k]) {
                candidates.add(i);
            }
        }
        
        //merge adjacent angles
        //TODO address merging of 'wrap-around' adjacent angles
        SortedSet<AngularIndex> indices = new TreeSet<AngularIndex>();
        int space = (int) ceil(angularAliasing / alpha);
        int first = -1;
        int weightedSum = -1;
        int directSum = -1;
        int count = -1;
        for (Iterator<Integer> i = candidates.iterator(); i.hasNext();) {
			int index = i.next();
			if (first != -1 && index - first < space) {
				int score = scores[index];
				weightedSum += score * index;
				directSum += score;
				count += 1;
			} else {
				if (first != -1) {
					AngularIndex angularIndex = new AngularIndex(first, weightedSum / directSum, directSum / count);
					indices.add( angularIndex );
				}
				first = index;
				int score = scores[index];
				weightedSum = score * index;
				directSum = score;
				count = 1;
			}
		}
		if (first != -1) {
			AngularIndex angularIndex = new AngularIndex(first, weightedSum / directSum, directSum / count);
			indices.add( angularIndex );
		}

        //convert to radians
        double[] angles = new double[ indices.size() ];
        Iterator<AngularIndex> it = indices.iterator();
        for (int i = 0; i < angles.length; i++) {
        	AngularIndex index = it.next();
            angles[i] = beta + alpha * (index.index - resolution);
        }
        
        return angles;
    }
    
    private void score(byte[] samples, int[] scores) {
        for (int j = 0; j < scores.length; j++) {
            for (int i = 0; i < samples.length; i++) {
                int f = j + i;
                if (f >= samples.length) f -= samples.length;
                int b = j - i - 1;
                if (b < 0) b += samples.length;
                if (samples[f] == samples[b])  scores[j]++;
            }
        }
    }

    // inner classes
    
    private static class AngularIndex implements Comparable<AngularIndex> {
    	
    	private final int id;
    	private final double index;
    	private final double score;
    	
    	AngularIndex(int id, double index, double score) {
    		this.id = id;
    		this.index = index;
    		this.score = score;
    	}
    	
    	public int compareTo(AngularIndex that) {
    		if (this.score < that.score) return -1;
    		if (this.score > that.score) return 1;
    		if (this.id < that.id) return -1;
    		if (this.id > that.id) return 1;
    		return 0;
    	}
    	
    }

	/**
	 * Records the reflective symmetries identified. 
	 */
	
	public static class Symmetry {
		
		private final double centerX;
		private final double centerY;
		private final double[] angles;
		
		public Symmetry(double centerX, double centerY, double[] angles) {
			this.centerX = centerX;
			this.centerY = centerY;
			this.angles = angles;
		}
		
		/**
		 * The x coordinate of the object's centroid in screen coordinates.
		 * 
		 * @return the centroid's x coordinate
		 */
		
		public double getCenterX() {
			return centerX;
		}
		
		/**
		 * The y coordinate of the object's centroid in screen coordinates.
		 * 
		 * @return the centroid's y coordinate
		 */
		
		public double getCenterY() {
			return centerY;
		}
		
		/**
		 * The axes of symmetry in radians. 0 radians is parallel to the x
		 * axis. Positive rotation is counter-clockwise in screen
		 * coordinates. Note: modification of the returned array will
		 * change the internal state of this object.
		 * 
		 * @return the axes of symmetry
		 */
		
		public double[] getAngles() {
			return angles;
		}
		
		
		@Override
		public String toString() {
			DecimalFormat formatCoords = new DecimalFormat("0.0");
			DecimalFormat formatAngles = new DecimalFormat("0.000");
			StringBuilder sb = new StringBuilder();
			sb.append("(")
				.append(formatCoords.format(centerX))
				.append(", ")
				.append(formatCoords.format(centerY))
				.append(") [");
			for (int i = 0; i < angles.length; i++) {
				if (i != 0) sb.append(", ");
				sb.append(formatAngles.format(angles[i]));
			}
			sb.append("]");
			return sb.toString();
		}
	}

}
