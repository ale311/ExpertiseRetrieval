package scopusextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import org.neo4j.graphdb.GraphDatabaseService;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import neo4j.StartGraphDB;

public class LinkNumberOfPages {
		static void inserisciNumberOfPages(GraphDatabaseService graphDb, HashSet<String> result, String APIKEY, String TIPOLINK) throws IOException {
			try {
				int i = 0;
				for(String scopus_id : result){
					i++;
					System.out.println(scopus_id);
					System.out.println(i);
					URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json");
					HttpURLConnection request = (HttpURLConnection) url.openConnection();
					request.connect();
					JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
					if (jsonElement!=null) {
						try {
							JsonElement jsonStartingPage = jsonElement.getAsJsonObject()
									.get("abstracts-retrieval-response").getAsJsonObject()
									.get("coredata").getAsJsonObject()
									.get("prism:startingPage");
							JsonElement jsonEndingPage = jsonElement.getAsJsonObject()
									.get("abstracts-retrieval-response").getAsJsonObject()
									.get("coredata").getAsJsonObject()
									.get("prism:endingPage");
							String sp = jsonStartingPage.toString();
							String ep = jsonEndingPage.toString();
							sp = sp.substring(1, sp.length()-1);
							ep = ep.substring(1, ep.length()-1);
							int startPage = Integer.valueOf(sp);
							int endPage = Integer.valueOf(ep);
							int totalPages = endPage-startPage;
							String s_totalPages = Integer.toString(totalPages);
							System.out.println(s_totalPages);
							StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", s_totalPages, "NUMBER_OF_PAGES", TIPOLINK);

						} catch (Exception e) {
							// TODO: handle exception
						}
					}
//					System.out.println(i);
				}	

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
		}
}
