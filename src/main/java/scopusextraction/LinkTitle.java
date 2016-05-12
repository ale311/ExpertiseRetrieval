package scopusextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import org.neo4j.graphdb.GraphDatabaseService;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import neo4j.StartGraphDB;

class LinkTitle {
	static void inserisciTitolo(GraphDatabaseService graphDb, HashSet<String> result, String APIKEY, String TIPOLINK) throws IOException {
		// TODO Auto-generated method stub
		int i=0;
		for(String scopus_id : result){
			i++;
			//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
			//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=title");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			String title = jsonElement.getAsJsonObject()
					.get("abstracts-retrieval-response").getAsJsonObject().get("coredata").getAsJsonObject().get("dc:title").toString();
			title = title.substring(1, title.length()-1);
//			System.out.println(title);
			StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", title, "TITLE", TIPOLINK);
			System.out.println(i);
		}	
	}
}
