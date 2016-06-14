package scopusextraction;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import au.com.bytecode.opencsv.CSVWriter;

public class ScriviCSV {
	static void writeCSV (HashSet<String> result, String position) throws IOException{
		CSVWriter writer = new CSVWriter(new FileWriter(position), '\n');
		// feed in your array (or convert your data to an array)
		List<String> temp = new ArrayList<String>();
		for(String s : result){
			temp.add(s);
		}
		String[] entries =  new String[temp.size()];

		entries = temp.toArray(entries);

		writer.writeNext(entries);
		writer.close();
	}



	static void writeMapCSV(HashMap<String, BigDecimal> result, String position) throws IOException{
		CSVWriter writer = new CSVWriter(new FileWriter(position), '\n');
		// feed in your array (or convert your data to an array)
		List<String> temp = new ArrayList<String>();
		for(String s : result.keySet()){
			temp.add(s+","+result.get(s));
		}
		String[] entries =  new String[temp.size()];

		entries = temp.toArray(entries);

		writer.writeNext(entries);
		writer.close();
	}
}

