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

import org.neo4j.graphdb.GraphDatabaseService;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import neo4j.StartGraphDB;
import scala.util.parsing.json.JSONObject;
public class ScopusExtraction {
	private final static String APIKEY = "3b337850042d998802dd243936ecc35f";
	private static final String HA_TITOLO = "ha_titolo";
	private static final String HA_CREATORE = "ha_creatore";
	private static final String HA_AUTORE = "ha_autore";
	private static final String CREATO = "data_di_pubblicazione";
	private static final String TIPO_AGGREGAZIONE = "ha_tipo_di_aggregazione";
	private static final String CYTED_BY = "ha_numero_di_citazioni";
	private static final String HA_PUBLICATION_NAME = "ha_nome_di_pubblicazione";
	private static final String HA_PUBLISHER = "ha_publisher";
	private static final String HA_SUBJECT_AREA = "ha_subject_area";
	private static final String HA_ABSTRACT = "ha_abstract";
	private static final String HA_AFFILIATION_NAME = "ha_affiliazione";
	private static final String HA_AFFILIATION_ID = null;
	

	public static void main(String[] args) throws IOException {
		Integer currentPage = 4980;
		HashSet<String> result = new HashSet<String>();
		getJsonObject("nanoindentation", currentPage, result);
		System.out.println(result.size());
		//inizializzo il grafo lanciando i metodi del package neo4j
//		int ok = neo4j.StartGraphDB.insertDocuments(result);
//		GraphDatabaseService graphDb = neo4j.StartGraphDB.costruisciGrafo();
		GraphDatabaseService graphDb = neo4j.StartGraphDB.formattaGrafo();
//		inserisciTitolo(graphDb, result);
//		inserisciCreatore(graphDb, result);
//		inserisciSubjectAreas(graphDb, result);
		inserisciAffiliation(graphDb, result);
		//aggiungo entità al grafo
	}
//con questo metodo si inserisce, nel graphDB, il titolo associato allo scopus_id del paper
	private static void inserisciTitolo(GraphDatabaseService graphDb, HashSet<String> result) throws IOException {
		// TODO Auto-generated method stub
		for(String scopus_id : result){
//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=title");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			String title = jsonElement.getAsJsonObject()
					.get("abstracts-retrieval-response").getAsJsonObject().get("coredata").getAsJsonObject().get("dc:title").toString();
			title = title.substring(1, title.length()-1);
			System.out.println(title);
			StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", title, "title", HA_TITOLO);
		}	
	}
	
	private static void inserisciCreatore(GraphDatabaseService graphDb, HashSet<String> result) throws IOException {
		// TODO Auto-generated method stub
		for(String scopus_id : result){
//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=creator");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			JsonArray jsonArray = jsonElement.getAsJsonObject()
					.get("abstracts-retrieval-response").getAsJsonObject()
					.get("coredata").getAsJsonObject().get("dc:creator")
					.getAsJsonObject().get("author").getAsJsonArray();
			String authorId = jsonArray.get(0).getAsJsonObject().get("@auid").toString();
			String surname = jsonArray.get(0).getAsJsonObject().get("preferred-name").getAsJsonObject().get("ce:surname").toString();
			String givenName = jsonArray.get(0).getAsJsonObject().get("preferred-name").getAsJsonObject().get("ce:given-name").toString();
			String authorName = surname.substring(1, surname.length()-1)+" "+givenName.substring(1, givenName.length()-1);
			authorId = authorId.substring(1, authorId.length()-1);
			System.out.println(authorName);
			StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", authorName, "creator_name", HA_CREATORE);
		}	
	}

	
	private static void inserisciSubjectAreas(GraphDatabaseService graphDb, HashSet<String> result) throws IOException {
		// TODO Auto-generated method stub
		for(String scopus_id : result){
//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=subject-area");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			JsonArray jsonarray = jsonElement.getAsJsonObject()
					.get("abstracts-retrieval-response").getAsJsonObject().get("subject-areas")
					.getAsJsonObject().get("subject-area").getAsJsonArray();
			System.out.println("L'array riferito al documento n: "+scopus_id+"è: "+jsonarray.toString());
			for (JsonElement jse : jsonarray){
				String sa = jse.getAsJsonObject().get("@abbrev").toString();
				sa = sa.substring(1, sa.length()-1);
				System.out.println(sa);
				StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", sa , "SUBJECT_AREA", HA_SUBJECT_AREA);
			}
		}	
	}
	
	private static void inserisciAffiliation(GraphDatabaseService graphDb, HashSet<String> result) throws IOException {
		// TODO Auto-generated method stub
		for(String scopus_id : result){
//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=affiliation");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			JsonElement jso = jsonElement.getAsJsonObject()
					.get("abstracts-retrieval-response").getAsJsonObject().get("affiliation");
			String affiliationId;
			String affiliationName;
			String affiliationCountry;
			String affiliationCity;
			try {
				affiliationId = jso.getAsJsonObject().get("@id").toString();
				affiliationId = affiliationId.substring(1, affiliationId.length()-1);
				
				affiliationName = jso.getAsJsonObject().get("affilname").toString();
				affiliationName = affiliationName.substring(1, affiliationName.length()-1);
				
				affiliationCity = jso.getAsJsonObject().get("affiliation-city").toString();
				affiliationCity = affiliationCity.substring(1, affiliationCity.length()-1);
				
				affiliationCountry = jso.getAsJsonObject().get("affiliation-country").toString();
				affiliationCountry = affiliationCountry.substring(1, affiliationCountry.length()-1);
				
			} catch (Exception e) {
				//salvo l'array:
				JsonElement jsa = jso.getAsJsonArray().get(0);
				// TODO: handle exception
//				System.out.println(e);
				//in questo caso è un array, quindi lo leggo come tale
				affiliationId = jsa.getAsJsonObject().get("@id").toString();
				affiliationId = affiliationId.substring(1, affiliationId.length()-1);
				
				affiliationName = jsa.getAsJsonObject().get("affilname").toString();
				affiliationName = affiliationName.substring(1, affiliationName.length()-1);
				
				affiliationCity = jsa.getAsJsonObject().get("affiliation-city").toString();
				affiliationCity = affiliationCity.substring(1, affiliationCity.length()-1);
				
				affiliationCountry = jsa.getAsJsonObject().get("affiliation-country").toString();
				affiliationCountry = affiliationCountry.substring(1, affiliationCountry.length()-1);	
				
			}		
					
			System.out.println("id riferito al documento n: "+scopus_id+"è: "+affiliationId);
			System.out.println("nome affiliazione del documento n: "+scopus_id+"è: "+affiliationName);
			System.out.println(("città: "+affiliationCity));
			System.out.println(("country: "+affiliationCountry));
			
//			StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", affiliationId , "AFFILIATION_ID", HA_AFFILIATION_ID);
			StartGraphDB.insertRelationAttribute(graphDb, scopus_id, "SCOPUS_ID", affiliationName , "AFFILIATION_NAME",affiliationId, "AFFILIATION_ID",  HA_AFFILIATION_NAME);
//			StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", sa , "SUBJECT_AREA", HA_SUBJECT_AREA);
//			for (JsonElement jse : jsonarray){
//				String sa = jse.getAsJsonObject().get("@abbrev").toString();
//				sa = sa.substring(1, sa.length()-1);
//				System.out.println(sa);
//				StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", sa , "SUBJECT_AREA", HA_SUBJECT_AREA);
//			}
		}	
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
//	questo metodo riceve i documenti (intesi come SCOPUSID) inerenti un certo topic (intesa come WORLD) e li memorizza in RESULT
	private static Set<String> getJsonObject (String world, int currentPage, Set<String> result) throws IOException{
		URL url = new URL("http://api.elsevier.com/content/search/scopus?query="+world+"&sort=citedby&start=4980&count=10&apikey="+APIKEY);
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
		URL url = new URL("http://api.elsevier.com/content/search/scopus?query=all("+world+")"+"&apikey="+APIKEY+"&start="+currentPage+"&count=10");
		
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		
		
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));

		System.out.println("sto analizzando la pagina numero: "+currentPage);
		System.out.println("sto analizzando la page numero: "+jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("opensearch:startIndex"));
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
//  con questo metodo si estrae un singolo abstract inerente ad un documento che matcha con il suo SCOPUSID
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
			result.add(docID);
		}
		return result;
	}
}
