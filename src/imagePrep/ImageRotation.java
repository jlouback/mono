package imagePrep;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageRotation {
	
	public static void main(String[] args) {
		File file = new File("0010.png");
		processSingleImage(file, 30);
		
	}

	private static void processSingleImage(File image, int degrees) {
		
		int rotations = 360/degrees;
		int angle = degrees;
		String imageName = image.getAbsolutePath();
		imageName = imageName.substring(0, imageName.lastIndexOf('.'));
		
		for(int i=1; i<=rotations; i++){
			BufferedImage bufferedCopy = rotateImage(image, degrees);
			String copyImageName = imageName + '-' + i + ".png"; 
			File outputfile = new File(copyImageName);
			try {
				ImageIO.write(bufferedCopy, "png", outputfile);
			} catch (IOException e) {
				System.out.println("Could not save new image.");
				e.printStackTrace();
			}
			degrees += angle;
		}
		
	}
	
	private static BufferedImage rotateImage(File sourceImage, int degrees) {

		BufferedImage bufferedSource = null;
		try {
			bufferedSource = ImageIO.read(sourceImage);
		} catch (IOException e) {
			System.out.println("Couldn't find source image.");
			e.printStackTrace();
		}
		
		double sin = Math.abs(Math.sin(Math.toRadians(degrees)));
		double cos = Math.abs(Math.cos(Math.toRadians(degrees)));

		int sourceWidth = bufferedSource.getWidth();
		int sourceHeight = bufferedSource.getHeight();

		int newWidth = (int) Math.floor(sourceWidth*cos + sourceHeight*sin);
		int newHeight = (int) Math.floor(sourceWidth*cos + sourceHeight*sin);

		BufferedImage bufferedCopy = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = bufferedCopy.createGraphics();

		graphics.translate((newWidth-sourceWidth)/2, (newHeight-sourceHeight)/2);
		graphics.rotate(Math.toRadians(degrees), sourceWidth/2, sourceHeight/2);
		graphics.drawRenderedImage(bufferedSource, null);
		graphics.dispose();
		
		return bufferedCopy;
	}

}
