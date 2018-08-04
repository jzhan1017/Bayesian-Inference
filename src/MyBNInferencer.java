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


public class MyBNInferencer {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, FileNotFoundException {
		String filename = args[0];
		BayesianNetwork bn = null;
		
		// can parse either a .bif or .xml
		if (filename.substring(filename.length()-3).equals("bif")) {
			// for eclipse
			BIFParser parser = new BIFParser(new FileInputStream("src/examples/" + filename));
			// for terminal
//			BIFParser parser = new BIFParser(new FileInputStream("examples/" + filename));
			bn = parser.parseNetwork();
		} else if (filename.substring(filename.length()-3).equals("xml")) {
			XMLBIFParser xml_file = new XMLBIFParser();
			bn = xml_file.readNetworkFromFile("src/examples/" + filename);
//			bn = xml_file.readNetworkFromFile("examples/" + filename);
		} else {
			throw new FileNotFoundException("Must be .xml or .bif");
		}

		RandomVariable X = bn.getVariableByName(args[1]);
		
		String print = "\nGiven: ";
		Assignment assignment = new Assignment();
		for (int i = 2; i < args.length; i = i+2) {
			
			String variable = args[i];
			RandomVariable randomVariable = bn.getVariableByName(variable);
			String value = args[i+1];
			assignment.set(randomVariable, value);
			
			print += variable + " = " + value + ", ";
		}
		System.out.println(print);

		// time took to run exact inference
		long startTime = System.currentTimeMillis();
		ExactInference e = new ExactInference();
		Distribution distribution = e.ask(bn, X, assignment);
		long endtime = System.currentTimeMillis();
		System.out.println("Time: " + (endtime-startTime) + "ms");
		System.out.println(distribution);
	}
	


}
