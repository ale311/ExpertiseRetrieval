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

public class LinkYearOfPublication {
	static void inserisciYearOfPublication(GraphDatabaseService graphDb, HashSet<String> result, String APIKEY, String TIPOLINK) throws IOException {
		try {
			int i = 0;
			for(String scopus_id : result){
				i++;
				System.out.println(scopus_id);
				System.out.println(i);
				URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=prism:coverDate");
				HttpURLConnection request = (HttpURLConnection) url.openConnection();
				request.connect();
				JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
				if (jsonElement!=null) {
					try {
						JsonElement jsone = jsonElement.getAsJsonObject()
								.get("abstracts-retrieval-response").getAsJsonObject()
								.get("coredata").getAsJsonObject()
								.get("prism:coverDate");
						String dateOfPublication = jsone.toString();
						dateOfPublication = dateOfPublication.substring(1, dateOfPublication.length()-1);
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						String dateInString = dateOfPublication;

						try {

							Date date = formatter.parse(dateInString);
//							System.out.println(formatter.format(date));
							Calendar c = Calendar.getInstance();
							c.setTime(date);
							int year = c.get(Calendar.YEAR);
							String s_year = Integer.toString(year);
							System.out.println(s_year);
							StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", s_year, "ANNO_PUBBLICAZIONE", TIPOLINK);

						} catch (ParseException e) {
							e.printStackTrace();
						}

					    

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