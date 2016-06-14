package authorOperations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.Tag;
import mongodb.InizializeMongoDB;
import tagmeextraction.TagMeExtraction;

public class AuthorInsert {
	private final static String APIKEY = "b83798f2bae2c7a72f9a4474a5b7293f";
	private final static String KEYWORD = "nanoindentation";
	
	
	
		
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String lastName = " oliver";
		String firstName = "warren c";
		String affiliation = "Nanomechanics Inc.";
		HashSet<Author> possibileAuthors = SearchAuthorIdByName.searchIDbyName(lastName, firstName,affiliation, APIKEY);
		HashMap<String, String> textByScopusId = new HashMap<>();
		HashMap<String, HashMap<String, Double>> tagWeightByScopusId = new HashMap<>();
		//ABUSO: estraggo il primo nome solo per test
		Author a = possibileAuthors.iterator().next();
		
		DBCollection coll = InizializeMongoDB.buildDb();
		
		BasicDBObject mdbauthor = new BasicDBObject("_id", a.authorID)
				.append("authorId", a.authorID)
				.append("authorSurname", a.getSurname())
				.append("authorName", a.getName())
				.append("affiliation", new BasicDBObject("affiliationId", a.getAffiliation())
						.append("affiliationName", a.getAffiliation_name()));

		Set<String> insiemeDocumenti = DocumentsByAuthorId.getJsonDocument(a.getAuthorID(), APIKEY);
		textByScopusId = (HashMap<String, String>) TagWeight.getTextByScopusId(insiemeDocumenti,textByScopusId, APIKEY);
		
		tagWeightByScopusId = (HashMap<String, HashMap<String, Double>>) TagWeight.getTagWeightByScopusId(textByScopusId, tagWeightByScopusId, KEYWORD);
		BasicDBObject d2 = new BasicDBObject();
		BasicDBList listText = new BasicDBList();
		for(String chiave : tagWeightByScopusId.keySet()){
			HashMap<String, Double> currentMap = tagWeightByScopusId.get(chiave);
			System.out.println(chiave+": "+tagWeightByScopusId.get(chiave));
			BasicDBObject dbpubblicazione = new BasicDBObject("_id", chiave)
					.append("scopus_id", chiave)
					.append("text", textByScopusId.get(chiave));
			listText.add(dbpubblicazione);
			BasicDBList listWeight = new BasicDBList();
			for(String currentTag : tagWeightByScopusId.get(chiave).keySet()){
				Double currentWeight = tagWeightByScopusId.get(chiave).get(currentTag);
				if (currentWeight>0) {
					
				BasicDBObject dbweight = new BasicDBObject("_id", currentTag)
						.append("tag", currentTag)
						.append("weight", currentWeight);
				listWeight.add(dbweight);
				}
				dbpubblicazione.append("tag", listWeight);
			}
		}
		coll.insert(mdbauthor.append("pubblicazioni", listText));
		System.out.println(textByScopusId.size());
	}
}
