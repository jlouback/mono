package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import abcdCriterium.BorderIrregularity;
import foreignContributions.CannyEdgeDetector;

public class ImageProcessing {

	public static void main(String [] args) {
		BufferedReader csv = null;
		try {
			csv = new BufferedReader(new FileReader("csv_StauntonMa2.csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Issues with csv");
			e.printStackTrace();
		}

		String line;
		try {
			line = csv.readLine();
			while ((line = csv.readLine()) != null) {  
				//Get data from stauntonMa algorithm results.
				String[] stauntonMa = line.split(",");
				//First column is image filename
				String imageFile = stauntonMa[0];
				//Construct path
				String imagePath = returnFilepath(imageFile);
				File file = new File(imagePath);
				CannyEdgeDetector detector = new CannyEdgeDetector();
				detector.setLowThreshold(6f);
				detector.setHighThreshold(13f);
				detector.setSourceImage(ImageIO.read(file));
				detector.process();
				if(detector.getBorderCoordinates().size()<70) {
					detector.setLowThreshold(2f);
					detector.setHighThreshold(8f);
					detector.process();
					System.out.println(imagePath);
				}
				String newFile = imageFile + ".png";
				ImageIO.write(detector.getEdgesImage(), "png", new File(newFile));

			}  
			csv.close();
		} catch (IOException e) {
			System.out.println("Issues");
			e.printStackTrace();
		}

		
	}

	//Images are divided into Melanoma and Nevi directories
	public static String returnFilepath(String filename) {
		String filepath = "";
		if(filename.startsWith("M")) {
			filepath = "/Users/julianalouback/Desktop/Mono/Mono/Melanoma/" + filename;
		} else {
			filepath = "/Users/julianalouback/Desktop/Mono/Mono/Nevi/" + filename;
		}
		return filepath;
	}

}
