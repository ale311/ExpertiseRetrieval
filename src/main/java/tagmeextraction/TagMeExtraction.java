package tagmeextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale.Category;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TagMeExtraction {
	public static void main (String[] args) throws IOException{
		WPExtract cp = getWPDescription("Schumacher won the race in Indianapolis");
		for(String s : cp.extract){
			System.out.println(s);
		}
		
	}

	public static Set<String> getJsonDocument(String text) throws IOException{
		String text2 = text.replaceAll("\\W", "+");
		String ab = "false";
		text2.replace("\\n", "");
//		System.out.println(text2);
		
		URL url = new URL("http://tagme.di.unipi.it/tag?text="+text2+"&key=8020b57e2d41b6041c4fd06937acbec7&long_text=true&include_categories=true&include_abstract=true");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		HashSet<String> result = new HashSet<String>();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement pages = jsonElement.getAsJsonObject().get("annotations");
		JsonArray array = pages.getAsJsonArray();
		for(JsonElement ele : array){
			String title = ele.getAsJsonObject().get("title").toString();
			String extract = null;
			JsonElement ar = ele.getAsJsonObject().get("dbpedia_categories");
			for(JsonElement cat : ar.getAsJsonArray()){
				System.out.println(cat.toString());
			}
		}
		return result;
	}
	
	
	
	public static WPExtract getWPDescription(String text) throws IOException{
		WPExtract currentWP = new WPExtract();
		//preparo i 3 set che conterranno le stringhe che estraggo da tagme
		HashSet<String> tag = new HashSet<String>();
		HashSet<String> categories = new HashSet<String>();
		HashSet<String> introWP = new HashSet<String>();
		//preparo il testo per fare l'estrazione del JSON da tagme
		String text2 = text.replaceAll("\\W", "+");
		text2.replace("\\n", "");
		URL url = new URL("http://tagme.di.unipi.it/tag?text="+text2+"&key=8020b57e2d41b6041c4fd06937acbec7&long_text=true&include_categories=true&include_abstract=true");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		HashSet<String> result = new HashSet<String>();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		//ho ottenuto il JSON, prendo solo l'elemento annotations che contiene tutti i dati di mio interesse
		JsonElement pages = jsonElement.getAsJsonObject().get("annotations");
		JsonArray array = pages.getAsJsonArray();
		//estraggo le varie componenti e le inserisco nei giusti insiemi
		for(JsonElement ele : array){
			String title = ele.getAsJsonObject().get("title").toString();
			tag.add(title.substring(1, title.length()-1));
			String extr = ele.getAsJsonObject().get("abstract").toString();
			introWP.add(extr.substring(1, extr.length()-1));
			JsonElement ar = ele.getAsJsonObject().get("dbpedia_categories");
			for(JsonElement e : ar.getAsJsonArray()){
				String cat = e.toString();
				categories.add(cat.substring(1, cat.length()-1));
			}
		}
		currentWP.setTag(tag);
		currentWP.setCategories(categories);
		currentWP.setExtract(introWP);
		
		return currentWP;
	}
	
	
}
