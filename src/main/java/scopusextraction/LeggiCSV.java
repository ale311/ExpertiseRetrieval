package scopusextraction;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import au.com.bytecode.opencsv.CSVReader;

public class LeggiCSV {
	static HashSet<String> getAbstractCSV(String position) throws IOException{
		HashSet<String> result = new HashSet<String>();
		CSVReader reader = new CSVReader(new FileReader(position));
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String s = nextLine[0];
			if (!s.equals("n")) {
				// nextLine[] is an array of values from the line
				s = s.substring(13, s.length()-2);

				//			System.out.println(s);
				result.add(s);
			}
		}
		return result;
	}
	static HashSet<String> getScopusIDCSV(String position) throws IOException{
		HashSet<String> result = new HashSet<String>();
		CSVReader reader = new CSVReader(new FileReader(position));
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String s = nextLine[0];
			if (!s.equals("n")) {

//				 nextLine[] is an array of values from the line
				s = s.substring(3, s.length());

				System.out.println(s);
				result.add(s);
			}
		}
		return result;
	}
	
	static HashMap<String, String> getRelations(String position) throws IOException{
		HashMap<String, String> result = new HashMap<String, String>();
		CSVReader reader = new CSVReader(new FileReader(position));
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String s = nextLine[0];
			s = s.substring(2);
			String t = nextLine [1];
			t = t.substring(2);
			System.out.println("source "+s);
			System.out.println(("target "+t));
			
			result.put(s, t);
		}
		return result;
	}
}
