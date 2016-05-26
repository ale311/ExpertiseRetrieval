package scopusextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Citation {

	public static void main (String [] args ) throws IOException{
		getCitationByScopusId("0034866521", "b3a71de2bde04544495881ed9d2f9c5b");
	}
	public static Map<String, String> getCitationByScopusId(String source, String APIKEY) throws IOException{
		HashMap<String, String> result = new HashMap<>();
		try {
			URL url;
			url = new URL("http://api.elsevier.com/content/abstract/citations?scopus_id="+source+"&apiKey="+APIKEY+"&httpAccept=application/json");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			JsonArray jsa = jsonElement.getAsJsonObject().get("abstract-citations-response").getAsJsonObject().get("citeInfoMatrix").getAsJsonObject().get("citeInfoMatrixXML").getAsJsonObject().get("citationMatrix").getAsJsonObject().get("citeInfo").getAsJsonArray();
			for(JsonElement o : jsa){
				System.out.println(o);
			}
		} catch (MalformedURLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return result;
	}
}
