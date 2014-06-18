package abcdCriterium;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RunABCD {
	
	public static void main(String[] args) throws IOException {
		RunABCD runner = new RunABCD();
		runner.runABCD("csv_data_FinalAdd.csv");
	}


	//Function to add ABC criteria to StauntonMa results in csvFilename file
	public void runABCD(String csvFilename) throws IOException {
		BufferedReader csv;
		FileWriter csvOutput;
		String line;
		String imageFile;
		String imagePath;
		String data;
		int A;
		double B;
		double C;
		
		//Initialize metric classes;
		Asymmetry asymmetry = new Asymmetry();
		BorderIrregularity border = new BorderIrregularity();
		ColorChange color = new ColorChange();
		
		//Initialize input and ouput csv files
		csv = new BufferedReader(new FileReader(csvFilename));
		csvOutput = new FileWriter("csv_data2.csv");

		line = csv.readLine();
		while ((line = csv.readLine()) != null) {  
			//Get data from stauntonMa algorithm results.
			String[] stauntonMa = line.split(",");
			//First column is image filename
			imageFile = stauntonMa[0];
			//Construct path
			imagePath = returnFilepath(imageFile);
			System.out.println(imagePath);
			//Obtain metrics for image
			A = asymmetry.returnAsymmetryMetric(imagePath);
			B = border.returnIrregularityMetric(imagePath);
			C = color.returnColorChangeMetric(imagePath);
			//Write metrics to csv
			data = line + "," + A + "," + B + "," + C + "\n";
			csvOutput.append(data);
		}  
		
		csv.close();
		csvOutput.close();
	}
	
	//Images are divided into Melanoma and Nevi directories
	public String returnFilepath(String filename) {
		String filepath = "/Users/julianalouback/Desktop/NewNevi2/" + filename;
//		if(filename.startsWith("M")) {
//			filepath = "/Users/julianalouback/Desktop/Mono/Mono/Melanoma/" + filename;
//		} else {
//			filepath = "/Users/julianalouback/Desktop/Mono/Mono/Nevi/" + filename;
//		}
		return filepath;
	}

}
