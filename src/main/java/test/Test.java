package test;

import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class Test{
	public static void main (String [] args) throws Exception{

		CSVReader reader = new CSVReader(new FileReader("util/export.csv"));
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String s = nextLine[0];
			if (!s.equals("n")) {
				
			// nextLine[] is an array of values from the line
			s = s.substring(14, s.length()-2);

			System.out.println(s);
			}
		}
	}

}