package tagmeextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale.Category;

import javax.swing.plaf.synth.SynthSpinnerUI;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import scopusextraction.WPCategories;

public class TagMeExtraction {
	private static final String key = "8020b57e2d41b6041c4fd06937acbec7";
	public static void main (String[] args) throws IOException{
		HashMap<String, Double> temp = getTagWeight("Hardness is a measure of how resistant solid matter is to various kinds of permanent shape change when a compressive force is applied. Some materials, such as metal, are harder than others. Macroscopic hardness is generally characterized by strong intermolecular bonds, but the behavior of solid materials under force is complex; therefore, there are different measurements of hardness: scratch hardness, indentation hardness, and rebound hardness.Hardness is dependent on ductility, elastic stiffness, plasticity, strain, strength, toughness, viscoelasticity, and viscosity.Common examples of hard matter are ceramics, concrete, certain metals, and superhard materials, which can be contrasted with soft matter.", "Nanoindentation");
		System.out.println(temp.size());
		for(String s : temp.keySet()){
			System.out.println(s+ "  "+temp.get(s));
		}

	}

	@SuppressWarnings("finally")
	public static Map<String, HashSet<String>> getCategories(String text){
		HashMap<String, HashSet<String>> result = new HashMap<>();
		try {
			String text2 = text.replaceAll("\\W", "+");
			text2.replace("\\n", "");
			URL url = new URL("http://tagme.di.unipi.it/tag?text="+text2+"&key="+key+"&long_text=true&include_categories=true&include_abstract=true");
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
				for(JsonElement e : ar.getAsJsonArray()){
					String cat = e.toString();
					cat = cat.replace(" ", "_");
					//					System.out.println(cat);
					temp.add(cat.substring(1, cat.length()-1));
					result.put(title, temp);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
		finally {
			return result;
		}
	}


	public static Map<String, String> getDescription(String text){
		HashMap<String, String> result = new HashMap<>();
		try {
			String text2 = text.replaceAll("\\W", "+");
			text2.replace("\\n", "");
			URL url = new URL("http://tagme.di.unipi.it/tag?text="+text2+"&key=8020b57e2d41b6041c4fd06937acbec7&long_text=true&include_categories=true&include_abstract=true");
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

				String extr = ele.getAsJsonObject().get("abstract").toString();
				extr = extr.substring(1, extr.length()-1);
				result.put(title, extr);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return result;
		}
	}

	public static HashMap<String, Double> getTagWeight(String text, String keyword) {
		HashMap<String, Double> result = new HashMap<>();
		try {
			String text2 = text.replaceAll("\\W", "+");
			text2.replace("\\n", "");
			URL url = new URL("http://tagme.di.unipi.it/tag?text="+text2+"&key="+key+"&long_text=true");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			//ho ottenuto il JSON, prendo solo l'elemento annotations che contiene tutti i dati di mio interesse
			JsonElement pages = jsonElement.getAsJsonObject().get("annotations");
			JsonArray array = pages.getAsJsonArray();
			for(String tag : getCategories(text2).keySet()){
				try {
					String kw1 = tag;
					String rel = RelExtraction.getRelBetweenTags(keyword,kw1);
					Double drel = Double.valueOf(rel);
					System.out.println("fra "+keyword+" e "+RelExtraction.correggiStringa(kw1)+" esiste relazione di: "+drel);
					result.put(kw1, drel);
				} 
				catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		catch (Exception e){
		}
		return result;
	}
}
