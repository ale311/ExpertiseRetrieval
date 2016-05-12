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

public class LinkSubjectAreas {
	static void inserisciSubjectAreas(GraphDatabaseService graphDb, HashSet<String> result, String APIKEY, String TIPOLINK) throws IOException {
		int i = 0;
		for(String scopus_id : result){
			i++;
			//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
			//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=subject-area");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			if (jsonElement!=null) {
				
			JsonArray jsonarray = jsonElement.getAsJsonObject()
					.get("abstracts-retrieval-response").getAsJsonObject().get("subject-areas")
					.getAsJsonObject().get("subject-area").getAsJsonArray();
//			System.out.println("L'array riferito al documento n: "+scopus_id+"è: "+jsonarray.toString());
			for (JsonElement jse : jsonarray){
				if (jse!=null) {

					String sa = jse.getAsJsonObject().get("@abbrev").toString();
					if (sa!=null) {
						
					sa = sa.substring(1, sa.length()-1);
					//				System.out.println(sa);
					StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", sa , "SUBJECT_AREA", TIPOLINK);
					System.out.println(i);
					}
				}
			}
			}
		}	
	}
}
