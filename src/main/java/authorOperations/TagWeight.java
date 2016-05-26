package authorOperations;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tagmeextraction.TagMeExtraction;

public class TagWeight {
	protected static Map getTagWeightByScopusId (HashMap<String, String> textByScopusId, HashMap<String, HashMap<String, Double>> tagWeightByScopusId, String KEYWORD){
		for(String key : textByScopusId.keySet()){
			//			System.out.println(key);
			String text = textByScopusId.get(key);
			//			System.out.println(text);
			HashSet<String> tag = new HashSet<>();

			HashMap<String, Double> interna = new HashMap<>();
			//temp contiene come chiave,  tag estratti dal suo abstract
			interna = TagMeExtraction.getTagWeight(text, KEYWORD);
			for(String currentTag : interna.keySet()){
				//				System.out.println("chiave: "+currentTag+", "+"valore: "+interna.get(currentTag));
				tagWeightByScopusId.put(key, interna);
			}
		}
		return tagWeightByScopusId;
	}

	protected static Map getTextByScopusId(Set<String> insiemeDocumenti,
			HashMap<String, String> textByScopusId, String APIKEY) {
		// TODO Auto-generated method stub
		for(String scopus_id : insiemeDocumenti){
			try {
				textByScopusId.put(scopus_id, TextsByDocumentId.getTextByScopusId(scopus_id,APIKEY));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e);
			}
			//qui inserisco nella mappa contenente i testi, le chiavi (scopusID) e i documenti (i testi liberi)
			
		}
		return textByScopusId;
	}

}
