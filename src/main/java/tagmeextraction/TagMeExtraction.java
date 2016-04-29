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
	public static void main (String[] args) throws IOException{
		WPExtract cp = getWPDescription("Within the frame of this work, coloured films based on single layered titanium oxynitride (TiNxOy) compounds were prepared. The films were deposited by r.f. magnetron sputtering under variation of process parameters such as bias voltage and flow rate of reactive gases. Colour varied from shiny golden type for low oxygen contents (characteristic of TiN films) to dark blue for higher oxygen contents. The information on the composition was obtained by Rutherford backscattering spectrometry. X-ray diffraction results revealed the development of a face-centred cubic phase with 〈111〉 orientation (TiN type; lattice parameter of ∼ 0.429 nm) and traces of dispersed oxide phases. Nanoindentation experiments showed values of hardness between 20 and 40 GPa, strongly dependent on the composition and microstructure. Compressive stresses between -0.5 and -6 GPa were determined by the deflection method. © 2003 Elsevier Science B.V. All rights reserved");
		for(String s : cp.tag){
			//			System.out.println(s);
		}

	}

	@SuppressWarnings("finally")
	public static Map<String, HashSet<String>> getCategories(String text){
		HashMap<String, HashSet<String>> result = new HashMap<>();
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
			title = title.replace(" ", "_");
			title = title.substring(1, title.length()-1);
			tag.add(title);
			String extr = ele.getAsJsonObject().get("abstract").toString();
			introWP.add(extr.substring(1, extr.length()-1));
			JsonElement ar = ele.getAsJsonObject().get("dbpedia_categories");
			for(JsonElement e : ar.getAsJsonArray()){
				String cat = e.toString();
				cat = cat.replace(" ", "_");
				//				System.out.println(cat);
				categories.add(cat.substring(1, cat.length()-1));
			}
		}
		currentWP.setTag(tag);
		currentWP.setCategories(categories);
		currentWP.setExtract(introWP);
		//		è l'insieme contente tutte le info legate ad un determinato abstract: 
		//		i tag, le categorie di appartenenza dei tag e anche il capitolo introduttivo del tag.
		return currentWP;
	}



	//	public static Set<String> getJsonDocument(String text) throws IOException{
	//		String text2 = text.replaceAll("\\W", "+");
	//		text2.replace("\\n", "");
	//		
	//		URL url = new URL("http://tagme.di.unipi.it/tag?text="+text2+"&key=8020b57e2d41b6041c4fd06937acbec7&long_text=true&include_categories=true&include_abstract=true");
	//		HttpURLConnection request = (HttpURLConnection) url.openConnection();
	//		request.connect();
	//		HashSet<String> result = new HashSet<String>();
	//		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
	//		JsonElement pages = jsonElement.getAsJsonObject().get("annotations");
	//		JsonArray array = pages.getAsJsonArray();
	//		for(JsonElement ele : array){
	//			String title = ele.getAsJsonObject().get("title").toString();
	//			String extract = null;
	//			JsonElement ar = ele.getAsJsonObject().get("dbpedia_categories");
	//			for(JsonElement cat : ar.getAsJsonArray()){
	//				System.out.println(cat.toString());
	//			}
	//		}
	//		return result;
	//	}
}