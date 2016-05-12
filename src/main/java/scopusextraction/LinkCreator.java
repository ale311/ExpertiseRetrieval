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

public class LinkCreator {
	 static void inserisciCreatore(GraphDatabaseService graphDb, HashSet<String> result, String APIKEY, String TIPOLINK) throws IOException {
		// TODO Auto-generated method stub
		int i = 0;
		for(String scopus_id : result){
			i++;
//			System.out.println("analizzo il documento: "+scopus_id);
			//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
			//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=creator");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			if (jsonElement!=null) {
				try {
					JsonArray jsonArray = jsonElement.getAsJsonObject()
							.get("abstracts-retrieval-response").getAsJsonObject()
							.get("coredata").getAsJsonObject().get("dc:creator")
							.getAsJsonObject().get("author").getAsJsonArray();
					String authorId = jsonArray.get(0).getAsJsonObject().get("@auid").toString();
//					String surname = jsonArray.get(0).getAsJsonObject().get("preferred-name").getAsJsonObject().get("ce:surname").toString();
//					String givenName = jsonArray.get(0).getAsJsonObject().get("preferred-name").getAsJsonObject().get("ce:given-name").toString();
//					String authorName = surname.substring(1, surname.length()-1)+" "+givenName.substring(1, givenName.length()-1);

					authorId = authorId.substring(1, authorId.length()-1);
//					System.out.println("documento: "+scopus_id+ " creato da: "+authorName);
//					StartGraphDB.insertRelationAttribute(graphDb, scopus_id, "SCOPUS_ID", authorName, "AUTHOR_NAME", authorId, "AUTHOR_ID", TIPOLINK);
//					StartGraphDB.insertRelationAttribute(graphDb, scopus_id, "SCOPUS_ID", authorName, "AUTHOR_NAME",  authorId, "AUTHOR_ID", TIPOLINK1);
					StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", authorId, "AUTHOR_ID", TIPOLINK);
//					StartGraphDB.insertRelationAttribute2(graphDb, authorId, "AUTHOR_ID", authorName, "AUTHOR_NAME", affiliationId, "AFFILIATION_ID", affiliationName, "AFFILIATION_NAME", TIPOLINK2);
//					StartGraphDB.insertRelation(graphDb, authorId, "AUTHOR_ID", affiliationId, "AFFILIATION_ID", TIPOLINK2);
				
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			System.out.println(i);
		}	
	}


}
