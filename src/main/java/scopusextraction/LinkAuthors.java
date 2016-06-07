package scopusextraction;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.neo4j.graphdb.GraphDatabaseService;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import neo4j.StartGraphDB;

public class LinkAuthors {
	static void setScoreAuthors (GraphDatabaseService graphDb, HashMap<String, HashMap<String, BigDecimal>> hits, String APIKEY, String tipoLink){
		for (String scopus_id : hits.keySet()){
//			scorro i documenti, per ogni documento ho degli autori
			for(String authorId : hits.get(scopus_id).keySet()){
				
				System.out.println("autore "+authorId);
				System.out.println("documento "+scopus_id);
				BigDecimal value = hits.get(scopus_id).get(authorId);
				System.out.println("valore "+value);
				StartGraphDB.insertRelationProperty(graphDb, scopus_id, "SCOPUS_ID", authorId, "AUTHOR_ID", tipoLink, tipoLink, value.toString());
			}
		}
	}
	static void inserisciAutori (GraphDatabaseService graphDb, HashSet<String> result, String APIKEY, String TIPOLINK1, String TIPOLINK2) throws IOException {
		// TODO Auto-generated method stub
		int i = 0;
		for(String scopus_id : result){
			i++;
			System.out.println("scopus id "+scopus_id+" , numero di sequenza "+i);
			//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
			//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&view=full&field=authors");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			try {
				
				JsonArray jsonArray = 
						jsonElement
						.getAsJsonObject().get("abstracts-retrieval-response")
						.getAsJsonObject().get("authors")
						.getAsJsonObject().get("author")
						.getAsJsonArray();

				for(JsonElement jse : jsonArray){
					String authorId = jse.getAsJsonObject().get("@auid").toString();
					authorId = authorId.substring(1, authorId.length()-1);
					StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", authorId, "AUTHOR_ID", TIPOLINK1);
					System.out.println("ho inserito autore "+authorId+" per documento "+scopus_id);
				
					
					
				String surname = jse.getAsJsonObject().get("preferred-name").getAsJsonObject().get("ce:surname").toString();
				String givenName = jse.getAsJsonObject().get("preferred-name").getAsJsonObject().get("ce:given-name").toString();
				String authorName = surname.substring(1, surname.length()-1)+" "+givenName.substring(1, givenName.length()-1);
				
				StartGraphDB.insertRelation(graphDb, authorId, "AUTHOR_ID", authorName, "AUTHOR_NAME", "ha_nome");
				
				String affiliationId;
				String affiliationName = null;
				String affiliationCountry;
				String affiliationCity;
				System.out.println(authorId+" "+authorName);
				JsonElement jso = jse.getAsJsonObject().get("affiliation");
				if (jso != null) {

					try {

						affiliationId = jso.getAsJsonObject().get("@id").toString();
						affiliationId = affiliationId.substring(1, affiliationId.length()-1);

						URL url2 = new URL("http://api.elsevier.com/content/affiliation/affiliation_id/"+affiliationId+"?apiKey="+APIKEY+"&httpAccept=application/json");
						HttpURLConnection request2 = (HttpURLConnection) url2.openConnection();
						request2.connect();
						JsonElement jsonElement2 = new JsonParser().parse(new InputStreamReader((InputStream) request2.getContent()));
						JsonElement element = jsonElement2.getAsJsonObject().get("affiliation-retrieval-response");

						affiliationName = element.getAsJsonObject().get("affiliation-name").toString();
						affiliationName = affiliationName.substring(1, affiliationName.length()-1);

						affiliationCity = element.getAsJsonObject().get("city").toString();
						affiliationCity = affiliationCity.substring(1, affiliationCity.length()-1);

						affiliationCountry = element.getAsJsonObject().get("country").toString();
						affiliationCountry = affiliationCountry.substring(1, affiliationCountry.length()-1);

//						StartGraphDB.insertRelationAttribute(graphDb, scopus_id, "SCOPUS_ID", authorName, "AUTHOR_NAME",  authorId, "AUTHOR_ID", TIPOLINK1);
//						StartGraphDB.insertRelationAttribute2(graphDb, authorId, "AUTHOR_ID", authorName, "AUTHOR_NAME", affiliationId, "AFFILIATION_ID", affiliationName, "AFFILIATION_NAME", TIPOLINK2);
						StartGraphDB.insertRelation(graphDb, authorId, "AUTHOR_ID", affiliationId, "AFFILIATION_ID", TIPOLINK2);
					} catch (Exception e) {
						//					//salvo l'array:
						try {
							JsonElement jsa = jso.getAsJsonArray().get(0);
							affiliationId = jsa.getAsJsonObject().get("@id").toString();
							affiliationId = affiliationId.substring(1, affiliationId.length()-1);
							URL url2 = new URL("http://api.elsevier.com/content/affiliation/affiliation_id/"+affiliationId+"?apiKey="+APIKEY+"&httpAccept=application/json");
							HttpURLConnection request2 = (HttpURLConnection) url2.openConnection();
							request2.connect();
							JsonElement jsonElement2 = new JsonParser().parse(new InputStreamReader((InputStream) request2.getContent()));
							JsonElement element = jsonElement2.getAsJsonObject().get("affiliation-retrieval-response");
							affiliationName = element.getAsJsonObject().get("affiliation-name").toString();
							affiliationName = affiliationName.substring(1, affiliationName.length()-1);
							
							affiliationCity = element.getAsJsonObject().get("city").toString();
							affiliationCity = affiliationCity.substring(1, affiliationCity.length()-1);
							
							affiliationCountry = element.getAsJsonObject().get("country").toString();
							affiliationCountry = affiliationCountry.substring(1, affiliationCountry.length()-1);

//							StartGraphDB.insertRelationAttribute(graphDb, scopus_id, "SCOPUS_ID", authorName, "AUTHOR_NAME",  authorId, "AUTHOR_ID", TIPOLINK1);
							StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", authorId, "AUTHOR_ID", TIPOLINK1);
							
							StartGraphDB.insertRelation(graphDb, authorId, "AUTHOR_ID", authorName, "AUTHOR_NAME", "ha_nome");

//							StartGraphDB.insertRelationAttribute2(graphDb, authorId, "AUTHOR_ID", authorName, "AUTHOR_NAME", affiliationId, "AFFILIATION_ID", affiliationName, "AFFILIATION_NAME", TIPOLINK2);
							StartGraphDB.insertRelation(graphDb, authorId, "AUTHOR_ID", affiliationId, "AFFILIATION_ID", TIPOLINK2);
						} catch (Exception e2) {
							// TODO: handle exception
							if(e instanceof java.lang.NullPointerException){
								//non fare nulla, ma stampa solo 
							}
						}

					}
				}
			}	
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
		}
	}
	

	
}
