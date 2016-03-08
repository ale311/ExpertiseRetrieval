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
		Integer currentPage = 0;
		HashSet<String> result = new HashSet<String>();
		getJsonObject("nanoindentation", currentPage, result);
		for(String s : result){
			System.out.println(s);
		}
		System.out.println(result.size());
	}

	public static Set<String> estraiAbstract(String[] author) throws IOException {
		// TODO Auto-generated method stub
		HashSet<String> estratti = new HashSet<String>();
		Set<String> documenti = new HashSet<String>();
		String cognome = author[0];
		String nome = author[1];
		String affiliazione = author[2];

		String autore = getJsonAuthor(cognome, nome, affiliazione);
		documenti =  getJsonDocument(autore);
		for(String s : documenti){
			String x = getJsonAbstract(s);
			estratti.add(x);
		}

		return estratti;
	}

	private static Set<String> getJsonObject (String world, int currentPage, Set<String> result) throws IOException{
		URL url = new URL("http://api.elsevier.com/content/search/scopus?query=all("+world+")"+"&count=200&apikey="+APIKEY);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		Integer totalPages = null;
		
		
		
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement js_numberOfResults = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("opensearch:totalResults");
		String temp_numberOfResults = js_numberOfResults.toString();
		temp_numberOfResults = temp_numberOfResults.substring(1, temp_numberOfResults.length()-1);
		Integer numberOfResults = Integer.parseInt(temp_numberOfResults);
		JsonElement js_itemsPerPage = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("opensearch:itemsPerPage");
		String temp_itemsPerPage = js_itemsPerPage.toString();
		temp_itemsPerPage = temp_itemsPerPage.substring(1, temp_itemsPerPage.length()-1);
		Integer itemsPerPage = Integer.parseInt(temp_itemsPerPage);
		int numberOfPages = numberOfResults/itemsPerPage;
		System.out.println("il numero totale di risultati è "+temp_numberOfResults);
		System.out.println("il numero di pagine è "+numberOfPages);
		System.out.println("il numero di elementi per ogni pagina è "+itemsPerPage);
		
		//		ho ottenuto fin qui il numero di elementi e di elementi per pagina (quindi ricavo le il numero di pagine per l'estrazione)
//		lancio un metodo interno che esegue N volte lo stesso algoritmo per memorizzare tutto dentro una struttura dati globale ovviamente
		while (currentPage < numberOfResults){
			
			estraiOggetti(world, currentPage, result);
			System.out.println("la dimensione di result è: "+result.size());
			currentPage = currentPage + itemsPerPage;
			if(currentPage>4800)break;
		}
		return result;
		
	
	}
	private static int estraiOggetti(String world, int currentPage, Set<String> result) throws IOException {
		URL url = new URL("http://api.elsevier.com/content/search/scopus?query=all("+world+")"+"&apikey="+APIKEY+"&start="+currentPage+"&count=200");
		
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		
		
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));

		System.out.println("sto analizzando la pagina numero: "+currentPage);
		System.out.println("sto analizzando la page numero: "+jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("opensearch:startIndex"));
//		System.out.println(jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("opensearch:startIndex"));
		
		JsonElement pages = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("entry").getAsJsonArray();
		String scopus_id = null;
		for(JsonElement elem : pages.getAsJsonArray()){
			scopus_id = elem.getAsJsonObject().get("dc:identifier").getAsString();
			scopus_id = scopus_id.substring(10, scopus_id.length());
			System.out.println(scopus_id);
			if(result.contains(scopus_id)){
				System.out.println("presente");
			};
			result.add(scopus_id);
			
		}
		return (Integer) currentPage;
		// TODO Auto-generated method stub
		
	}

	private static String getJsonAbstract(String docID) throws IOException {
		URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+docID+"?field=dc:description&apikey="+APIKEY+"&httpAccept=application/json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement pages = jsonElement.getAsJsonObject().get("abstracts-retrieval-response").getAsJsonObject().get("coredata").getAsJsonObject().get("dc:description");
		String result = pages.toString();
		result = result.substring(1, result.length()-1);
		System.out.println(result);
		return result;
	}

	
	private static String getJsonFullText(String docID) throws IOException {
		URL url = new URL("http://api.elsevier.com/content/article/scopus_id/"+docID+"?&apikey="+APIKEY+"&httpAccept=application/json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement pages = jsonElement.getAsJsonObject().get("full-text-retrieval-response").getAsJsonObject().get("originalText");
		String result = pages.toString();
		result = result.substring(1, result.length()-1);
		System.out.println(result);
		return result;
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
		for(JsonElement ele : array){
//			System.out.println(ele.getAsJsonObject().get("dc:identifier").toString());
		}
		String result = array.get(0).getAsJsonObject().get("dc:identifier").toString();
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
//			System.out.println(docID);
			result.add(docID);
		}
		return result;
	}
}
