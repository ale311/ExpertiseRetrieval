package tagmeextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TagMeExtraction {
	public static void main (String[] args) throws IOException{
		Set<String> titoli = getJsonDocument("Schumacher won the race in Indianapolis");
		for(String s : titoli){
			System.out.println(s);
		}
	}
	
	
	public static Set<String> getJsonDocument(String text) throws IOException{
		String text2 = text.replaceAll("\\W", "+");
		text2.replace("\\n", "");
		System.out.println(text2);
		URL url = new URL("http://tagme.di.unipi.it/tag?text="+text2+"&key=8020b57e2d41b6041c4fd06937acbec7&long_text=true");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		HashSet<String> result = new HashSet<String>();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement pages = jsonElement.getAsJsonObject().get("annotations");
		JsonArray array = pages.getAsJsonArray();
		for(JsonElement ele : array){
			String title = ele.getAsJsonObject().get("title").toString();
			title = title.substring(1, title.length()-1);
			result.add(title);
		}
		return result;
	}
}
