/**
 * @author James Zhan
 *
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import core.*;
import parser.*;

public class LikelihoodMain  {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

			
		int N = Integer.parseInt(args[0]);	
		String filename = args[1];
		BayesianNetwork bn;	
		if (filename.substring(filename.length()-3).equals("bif")) {
			BIFParser parser = new BIFParser(new FileInputStream("examples/" + filename));
			bn = parser.parseNetwork();
		} else if (filename.substring(filename.length()-3).equals("xml")) {
			XMLBIFParser xml_file = new XMLBIFParser();
			bn = xml_file.readNetworkFromFile("examples/" + filename);
		} else {
			throw new FileNotFoundException("Must be .xml or .bif");
		}
		RandomVariable X = bn.getVariableByName(args[2]);	
		String print = "\nLikelihoodWeighting Testing\nGiven: ";	
		Assignment assignment = new Assignment();	
		for (int i = 3; i < args.length; i = i+2) {
		
			String variable = args[i];
			RandomVariable randomVariable = bn.getVariableByName(variable);
			int j = i + 1;
			String value = args[j];
			assignment.set(randomVariable, value);
			
			print += variable + " = " + value + ", ";
		}

		System.out.println(print + "with Sample Size: " + N);
		
		
		ApproximateInference a = new ApproximateInference();
		long startTime = System.currentTimeMillis();
		Distribution distribution = a.likelihoodWeighting(X, assignment, bn, N);
		long endtime = System.currentTimeMillis();
		System.out.println("Time: " + (endtime-startTime) + "ms");
		System.out.println(distribution);
		
		
	

	}
	

}

