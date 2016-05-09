package scopusextraction;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import tagmeextraction.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.impl.core.IsolatedTransactionTokenCreator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import neo4j.StartGraphDB;
import scala.util.parsing.json.JSONObject;
public class ScopusExtraction {
//	private final static String APIKEY = "703fc00b8c3e5217790b6f7c98759d02";
	private final static String APIKEY = "b83798f2bae2c7a72f9a4474a5b7293f";
//	private final static String APIKEY = "0d63be8ff78f8c7127fd35d77a502294";
//	private final static String APIKEY = "544c126025d93754f02fe6615f634c27";
//	private final static String APIKEY = "b61139dd20be6485a3f9332b01ccfd95";
	

	
	private static final String HA_TITOLO = "ha_titolo";
	private static final String HA_CREATORE = "ha_creatore";
	private static final String HA_AUTORE = "ha_autore";
	private static final String CREATO = "data_di_pubblicazione";
	private static final String TIPO_AGGREGAZIONE = "ha_tipo_di_aggregazione";
	private static final String CITEDBY = "ha_numero_di_citazioni";
	private static final String HA_PUBLICATION_NAME = "ha_nome_di_pubblicazione";
	private static final String HA_PUBLISHER = "ha_publisher";
	private static final String HA_SUBJECT_AREA = "ha_subject_area";
	private static final String HA_ABSTRACT = "ha_abstract";
	private static final String HA_AFFILIATION_NAME = "ha_affiliazione";
	private static final String HA_AFFILIATION_ID = "ha_affiliation_id";
	private static final String HA_SEDE = "sede";
	private static final String IN_COUNTRY = "country";
	private static final String HA_TAG = "tag";
	private static final String HA_CATEGORIA = "categoria";
	private static final String HA_WP_INTRO = "intro";

	private static final Integer count = 25;
	private static final Integer start = 0;
	private static final Integer numOfPages = null;
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		Integer currentPage = 0;
		HashSet<String> result = new HashSet<String>();
		HashMap<String, String> csv = new HashMap<>();
		GraphDatabaseService graphDb = neo4j.StartGraphDB.costruisciGrafo();
//		GraphDatabaseService graphDb = neo4j.StartGraphDB.formattaGrafo();
//		result = LeggiCSV.getAbstractCSV("util/abstract.csv");
		
		result = LeggiCSV.getScopusIDCSV("util/export.csv");
//		csv = LeggiCSV.getRelations("util/scopusid-autorid.csv");

//		for(String source : csv.keySet()){
//			String target = csv.get(source);
//			StartGraphDB.insertRelation(graphDb, source, "SCOPUS_ID", target, "AUTHOR_ID", "ha_autore");
//			System.out.println("sto inserendo "+source);
//		}
//		ScriviCSV.writeCSV(result, "util/extract.csv");
		
//		System.out.println(result.size());

//		int ok = neo4j.StartGraphDB.insertDocuments(result);
//		System.out.println("analizzo titolo");
//		LinkTitle.inserisciTitolo(graphDb,result,APIKEY, HA_TITOLO);
//		System.out.println("analizzo creatore");
//		LinkCreator.inserisciCreatore(graphDb, result, APIKEY, HA_CREATORE);
//		System.out.println("analizzo subj area");
//		LinkSubjectAreas.inserisciSubjectAreas(graphDb, result, APIKEY, HA_SUBJECT_AREA);
//		System.out.println("analizzo affiliation");
//		LinkAffiliationDocument.inserisciAffiliation(graphDb, result, APIKEY, HA_AFFILIATION_ID, HA_SEDE, IN_COUNTRY);
		System.out.println("analizzo autori");
		LinkAuthors.inserisciAutori(graphDb,result, APIKEY, HA_AUTORE, HA_AFFILIATION_ID);
//		System.out.println("analizzo numero di citazioni");
//		LinkCountCitedBy.inserisciNumberOfCitations(graphDb, result, APIKEY, CITEDBY);
//		System.out.println("analizzo abstract");
//		LinkAbstract.inserisciAbstract(graphDb, result, APIKEY, HA_ABSTRACT);
//		for(String extract : result){
//			LinkTag.inserisciTagDaAbstract(graphDb, extract, HA_TAG, HA_CATEGORIA, HA_WP_INTRO);
//		}
	}


	private static Set<String> getJsonObject (String world, int currentPage, Set<String> result) throws IOException{
		URL url = new URL("http://api.elsevier.com/content/search/scopus?query="+world+"&sort=citedby-count&start="+currentPage
				+"&count="+count+"&apikey="+APIKEY);
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
			if(currentPage>1000)break;
		}
		return result;
	}
	private static int estraiOggetti(String world, int currentPage, Set<String> result) throws IOException {
		
		URL url = new URL("http://api.elsevier.com/content/search/scopus?query=all("+world+")"+"&apikey="+APIKEY+"&start="+currentPage+"&count="+count+"&sort=citedby-count");

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
	}
}
