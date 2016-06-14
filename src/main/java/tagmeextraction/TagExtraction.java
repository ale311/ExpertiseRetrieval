package tagmeextraction;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TagExtraction {
	private static String key = Util.getKey();
	public static Map<String, HashSet<String>> getTags(String text, String scopusId){
		HashMap<String, HashSet<String>> result = new HashMap<>();
		try {
			String text2 = text.replaceAll("\\W", "+");
			text2.replace("\\n", "");
			URL url = new URL(Util.getUrlTag()+"?text="+text2+"&gcube-token="+key+"&long_text=true&include_categories=true&include_abstract=true");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			//ho ottenuto il JSON, prendo solo l'elemento annotations che contiene tutti i dati di mio interesse
			JsonElement pages = jsonElement.getAsJsonObject().get("annotations");
			JsonArray array = pages.getAsJsonArray();
			for(JsonElement ele : array){
				String title = ele.getAsJsonObject().get("title").toString();
				title = title.replace(" ", "_");
				title = title.substring(1, title.length()-1);
				HashSet<String> temp = new HashSet<>();
				JsonElement ar = ele.getAsJsonObject().get("dbpedia_categories");
			}
			
		}catch(Exception e){
			
		}
		return null;
		
	}

}
