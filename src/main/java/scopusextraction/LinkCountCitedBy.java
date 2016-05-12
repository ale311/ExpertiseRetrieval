package scopusextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import org.neo4j.graphdb.GraphDatabaseService;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import neo4j.StartGraphDB;

public class LinkCountCitedBy {
	static void inserisciNumberOfCitations(GraphDatabaseService graphDb, HashSet<String> result, String APIKEY, String TIPOLINK) throws IOException {
		try {
			int i = 0;
			for(String scopus_id : result){
				i++;
				System.out.println(scopus_id);
				System.out.println(i);
				URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=citedby-count");
				HttpURLConnection request = (HttpURLConnection) url.openConnection();
				request.connect();
				JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
				if (jsonElement!=null) {
					try {
						JsonElement jsone = jsonElement.getAsJsonObject()
								.get("abstracts-retrieval-response").getAsJsonObject()
								.get("coredata").getAsJsonObject()
								.get("citedby-count");
						String count = jsone.toString();
						count = count.substring(1, count.length()-1);
						System.out.println(count);
						StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", count, "CITEDBY_COUNT", TIPOLINK);

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
//				System.out.println(i);
			}	

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
}
