package scopusextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import javax.print.DocFlavor.STRING;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EstrazioneCitazioni {
	public static void main(String[] args){
		int nc = numeroCitazioni("78049492003", "b83798f2bae2c7a72f9a4474a5b7293f");
		HashSet<String> cit = getCitazioniDaPagina("78049492003", nc, "b83798f2bae2c7a72f9a4474a5b7293f");
		System.out.println(cit.size());
		for(String s : cit){
			
			System.out.println("estratta la citazione "+s);
		}
	}
	protected static HashSet<String> getAllCitations (String scopusid, String APIKEY){
		int nc = numeroCitazioni(scopusid, APIKEY);
		HashSet<String> result = getCitazioniDaPagina(scopusid, nc, APIKEY);
		return result;
	}
	//	questa classe conterrà due metodi: uno per estrarre il numero di citazioni che si riferiscono ad un documenti
	//	un'altra estrarrà i documenti relativi ad ognuna delle citazioni, per il numero di citazioni già estratto prima
	protected static int numeroCitazioni (String scopusid, String APIKEY){
		int result = 0;
		try {
			URL url = new URL("http://api.elsevier.com/content/search/scopus?query=refeid(2-s2.0-"+scopusid+")&apiKey="+APIKEY);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			result = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("opensearch:totalResults").getAsInt();
			System.out.println("il numero totale di citazioni per il documento "+scopusid+" è "+result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	protected static HashSet<String> getCitazioniDaPagina (String scopusid, int numeroCitazioni, String APIKEY){
		HashSet<String> result = new HashSet<>();
		int currentPage = 0;
		URL url;
		try {
			while(currentPage < numeroCitazioni){

				url = new URL("http://api.elsevier.com/content/search/scopus?query=refeid(2-s2.0-"+scopusid+")&apiKey="+APIKEY+"&start="+currentPage);
				HttpURLConnection request = (HttpURLConnection) url.openConnection();
				request.connect();
				JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
				//			System.out.println("sto analizzando la pagina numero: "+currentPage);
				//			System.out.println("sto analizzando la page numero: "+jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("opensearch:startIndex"));
				JsonElement pages = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("entry").getAsJsonArray();
				String scopus_id = null;
				for(JsonElement elem : pages.getAsJsonArray()){
					scopus_id = elem.getAsJsonObject().get("dc:identifier").getAsString();
					scopus_id = scopus_id.substring(10, scopus_id.length());
					//				System.out.println(scopus_id);
					if(result.contains(scopus_id)){
						System.out.println("presente");
					};
					result.add(scopus_id);
				}
				currentPage = currentPage+25;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
