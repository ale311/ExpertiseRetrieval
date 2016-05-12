package scopusextraction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
}
