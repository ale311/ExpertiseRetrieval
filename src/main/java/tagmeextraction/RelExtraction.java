package tagmeextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class RelExtraction {
	public static void main (String[]x) throws IOException{
		
		String s = getRelBetweenTags("nanoindentation", "hardness");
		System.out.println(s);
	}
	
	public static String getRelBetweenTags(String kw1, String kw2)throws IOException{
		
		kw1 = correggiStringa(kw1);
		kw2 = correggiStringa(kw2);
		
		URL url = new URL(Util.getUrlrel()+"?gcube-token="+Util.getKey()+"&tt="+kw1+"%20"+kw2);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		HashSet<String> result = new HashSet<String>();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonArray jsonArray = jsonElement.getAsJsonObject().get("result")
				.getAsJsonArray();
		String rel = jsonArray.get(0).getAsJsonObject().get("rel").toString();
		rel = rel.substring(1,rel.length()-1);
//		String rel = ele.getAsJsonObject().get("rel").toString();
		return rel;
	}
	
	protected static String correggiStringa (String daCorreggere){
		String corretta = daCorreggere;
		//imposto uppercase sul primo carattere
		corretta = corretta.substring(0,1).toUpperCase() + corretta.substring(1);
		//elimino spazi che danno problemi e li sostituisco con underscore
		corretta = corretta.replace(" ", "_");
		
		return corretta;
	}
}