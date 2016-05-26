package authorOperations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.register.Register.Int;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class DocumentsByAuthorId {

	protected static Set<String> getJsonDocument(String authorId, String APIKEY ) throws IOException{
		int numeroElementi = calcolaNumeroQuery(authorId, APIKEY);
		HashSet<String> result = new HashSet<String>();
		int i=0;
		while(i<numeroElementi){
			String start = Integer.toString(i);
			URL url = new URL("http://api.elsevier.com/content/search/scopus?query=AU-ID("+authorId+")&field=dc:identifier&apikey="+APIKEY+"&httpAccept=application/json&start="+start);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			JsonElement pages = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("entry");
			JsonArray array = pages.getAsJsonArray();
			for(int j = 0; j < array.size(); j++){
				String docID = array.get(j).getAsJsonObject().get("dc:identifier").toString();
				docID = docID.substring(11, docID.length()-1);
				result.add(docID);
				System.out.println(docID);
			}
			i = i+25;
		}
		return result;
	}

	private static int calcolaNumeroQuery(String authorId, String APIKEY) throws IOException{
		URL url = new URL("http://api.elsevier.com/content/search/scopus?query=AU-ID("+authorId+")&field=dc:identifier&apikey="+APIKEY+"&httpAccept=application/json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement pages = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("opensearch:totalResults");
		int numeroElementi = pages.getAsInt();
		return numeroElementi;
	}
}
