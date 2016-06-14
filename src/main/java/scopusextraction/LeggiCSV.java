package scopusextraction;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jfree.data.Values;

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
	
	static HashMap getScore(String position) throws IOException{
		HashMap<String, BigDecimal> result = new HashMap<>();
		Reader in = new FileReader("util/sortedAuthorityWithError.csv");
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {
		    String columnOne = record.get(0);
		    String autore = columnOne.substring(3);
		    String columnTwo = record.get(1);
		    String auth = columnTwo.replace(",", ".");
		    Double authority = Double.parseDouble(auth);
		    BigDecimal a = new BigDecimal(auth);
		    result.put(autore, a);
		}
		return result;
	}
	
	static HashMap getScorePresent(String position) throws IOException{
		HashMap<String, BigDecimal> result = new HashMap<>();
		Reader in = new FileReader("util/sortedAuthorityWithError.csv");
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {
		    String columnOne = record.get(0);
		    String autore = columnOne.substring(3);
		    String columnTwo = record.get(1);
		    String auth = columnTwo.replace(",", ".");
		    Double authority = Double.parseDouble(auth);
		    BigDecimal a = new BigDecimal(auth);
		    if(result.containsKey(autore)){
		    	if(result.get(autore).floatValue()>authority.floatValue()){
		    		//non fare nulla, ho il valore più alto già memorizzato nella mia mappa
		    	}
		    	else{
		    		result.put(autore, a);
		    	}
		    }
		    else{
		    	//questo ramo indica che non è presente nella mia mappa l'autore e quindi va memorizzato
		    	result.put(autore, a);
		    }
		}
		return result;
	}
}
