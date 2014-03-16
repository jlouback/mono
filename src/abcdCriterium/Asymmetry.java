package abcdCriterium;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import foreignContributions.SymmetryDetector;

public class Asymmetry {
	
	public static void main(String [] args) {
		Asymmetry as = new Asymmetry();
		as.runDetector();
	}

	public void runDetector() {
		//obtain our image
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("mole.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//create the detector
		SymmetryDetector detector = new SymmetryDetector();
		//optionally adjust parameters here
		detector.setScoreThreshold(0.9f);
		//strictly optional: observe the algorithm running
		BufferedImage obs = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		detector.setObservation(obs.createGraphics());
		//run the detector - symmetries are available from the returned object
		SymmetryDetector.Symmetry symmetry = detector.detectSymmetry(image);
		//the image obs will contain a graphical summary of execution
		File processed = new File("assymetry3.png");
		try {
			ImageIO.write(obs, "png", processed);
		} catch (IOException e) {
			System.out.println("Unable to save image.");
			e.printStackTrace();
		}
	}

}
