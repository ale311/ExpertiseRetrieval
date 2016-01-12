package scopusextraction;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
public class ScopusExtraction {
	private final static String APIKEY = "3b337850042d998802dd243936ecc35f";

	public static void main(String[] args) throws IOException {
		String documentAbstracts = getJsonAbstract("");
	}

	private static String getJsonAbstract(String string) throws IOException {
		URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/84876703352?field=dc:description&apikey="+APIKEY+"&httpAccept=application/json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement pages = jsonElement.getAsJsonObject().get("abstracts-retrieval-response").getAsJsonObject().get("coredata").getAsJsonObject().get("dc:description");
		String result = pages.toString();
		result = result.substring(1, result.length()-1);
		System.out.println(result);
		return null;
	}

	private static String getJsonAuthor(String lastName, String firstName, String affiliation) throws IOException{
		lastName = lastName.replace(" ", "%20");
		firstName = firstName.replace(" ", "%20");
		affiliation = affiliation.replace(" " ,"%20");
		URL url = new URL("http://api.elsevier.com/content/search/author?query=authlast("+lastName+")%20and%20authfirst("+firstName+")%20and%20affil("+affiliation+")&apikey="+APIKEY+"&httpAccept=application/json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement pages = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("entry");
		JsonArray array = pages.getAsJsonArray();
		String result = array.get(0).getAsJsonObject().get("dc:identifier").toString();
		System.out.println(result);
		return result;
	}

	private static Set<String> getJsonDocument(String authID) throws IOException{
		URL url = new URL("http://api.elsevier.com/content/search/scopus?query=AU-ID(7004212771)&field=dc:identifier&apikey="+APIKEY+"&httpAccept=application/json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement pages = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("entry");
		JsonArray array = pages.getAsJsonArray();
		HashSet<String> result = new HashSet<String>();
		for(int i = 0; i < array.size(); i++){
			String docID = array.get(i).getAsJsonObject().get("dc:identifier").toString();
			docID = docID.substring(11, docID.length()-1);
			System.out.println(docID);
			result.add(docID);
		}
		return result;
	}

}
