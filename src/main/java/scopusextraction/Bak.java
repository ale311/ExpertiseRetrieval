package scopusextraction;

public class Bak {
//	  con questo metodo si estrae un singolo abstract inerente ad un documento che matcha con il suo SCOPUSID
//	private static String getJsonAbstract(String docID) throws IOException {
//		URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+docID+"?field=dc:description&apikey="+APIKEY+"&httpAccept=application/json");
//		HttpURLConnection request = (HttpURLConnection) url.openConnection();
//		request.connect();
//		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
//		JsonElement pages = jsonElement.getAsJsonObject().get("abstracts-retrieval-response").getAsJsonObject().get("coredata").getAsJsonObject().get("dc:description");
//		String result = pages.toString();
//		result = result.substring(1, result.length()-1);
//		System.out.println(result);
//		return result;
//	}
//
//
//	private static String getJsonFullText(String docID) throws IOException {
//		URL url = new URL("http://api.elsevier.com/content/article/scopus_id/"+docID+"?&apikey="+APIKEY+"&httpAccept=application/json");
//		HttpURLConnection request = (HttpURLConnection) url.openConnection();
//		request.connect();
//		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
//		JsonElement pages = jsonElement.getAsJsonObject().get("full-text-retrieval-response").getAsJsonObject().get("originalText");
//		String result = pages.toString();
//		result = result.substring(1, result.length()-1);
//		System.out.println(result);
//		return result;
//	}
	
//	private static String getJsonAuthor(String lastName, String firstName, String affiliation) throws IOException{
//		lastName = lastName.replace(" ", "%20");
//		firstName = firstName.replace(" ", "%20");
//		affiliation = affiliation.replace(" " ,"%20");
//		URL url = new URL("http://api.elsevier.com/content/search/author?query=authlast("+lastName+")%20and%20authfirst("+firstName+")%20and%20affil("+affiliation+")&apikey="+APIKEY+"&httpAccept=application/json");
//		HttpURLConnection request = (HttpURLConnection) url.openConnection();
//		request.connect();
//		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
//		JsonElement pages = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("entry");
//		JsonArray array = pages.getAsJsonArray();
//		String result = array.get(0).getAsJsonObject().get("dc:identifier").toString();
//		return result;
//	}
//
//	private static Set<String> getJsonDocument(String authID) throws IOException{
//		URL url = new URL("http://api.elsevier.com/content/search/scopus?query=AU-ID(7004212771)&field=dc:identifier&apikey="+APIKEY+"&httpAccept=application/json");
//		HttpURLConnection request = (HttpURLConnection) url.openConnection();
//		request.connect();
//		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
//		JsonElement pages = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("entry");
//		JsonArray array = pages.getAsJsonArray();
//		HashSet<String> result = new HashSet<String>();
//		for(int i = 0; i < array.size(); i++){
//			String docID = array.get(i).getAsJsonObject().get("dc:identifier").toString();
//			docID = docID.substring(11, docID.length()-1);
//			result.add(docID);
//		}
//		return result;
//	}
}
