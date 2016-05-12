package authorExtraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import tagmeextraction.TagMeExtraction;

public class AuthorRetrieve {
	private final static String APIKEY = "b83798f2bae2c7a72f9a4474a5b7293f";
	private final static String KEYWORD = "Nanoindentation";

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String lastName = "Gurini";
		String firstName = "Feltoni";
		String affiliation = "Universita degli Studi Roma Tre";
		HashSet<Author> possibileAuthors = SearchAuthorIdByName.searchIDbyName(lastName, firstName,affiliation, APIKEY);
		HashMap<String, String> textByScopusId = new HashMap<>();
		HashMap<String, HashMap<String, Double>> tagWeightByScopusId = new HashMap<>();
		//ABUSO: estraggo il primo nome solo per test

		Author a = possibileAuthors.iterator().next();
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "mydb" );
		DBCollection coll = db.getCollection("collection");
		BasicDBObject mdbauthor = new BasicDBObject("_id", a.authorID)
				.append("authorId", a.authorID)
				.append("authorSurname", a.getSurname())
				.append("authorName", a.getName())
				.append("affiliation", new BasicDBObject("affiliationId", a.getAffiliation())
						.append("affiliationName", a.getAffiliation_name()));

		//		System.out.println(a.getAuthorID());
		Set<String> insiemeDocumenti = DocumentsByAuthorId.getJsonDocument(a.getAuthorID(), APIKEY);
		for(String scopus_id : insiemeDocumenti){
			textByScopusId.put(scopus_id, TextsByDocumentId.getTextByScopusId(scopus_id,APIKEY));
			//qui inserisco nella mappa contenente i testi, le chiavi (scopusID) e i documenti (i testi liberi)
		}
		System.out.println(textByScopusId.size());
		//ho ottenuto l'insieme di documenti con associati i testi
		for(String key : textByScopusId.keySet()){
			int i = 1;
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
			i++;
		}
		BasicDBObject d2 = new BasicDBObject();
		for(String chiave : tagWeightByScopusId.keySet()){

			System.out.println(chiave+": "+tagWeightByScopusId.get(chiave));

			//insert data for id,name and speed
			d2.put(chiave, textByScopusId.get(chiave));

			mdbauthor.append("pubblicazioni",d2);
			//			mdbauthor.append("pubblicazioni",new BasicDBObject("scopus_id", chiave));


		}
		coll.insert(mdbauthor);
	}
}
