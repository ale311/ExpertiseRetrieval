package scopusextraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import org.neo4j.graphdb.GraphDatabaseService;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import neo4j.StartGraphDB;

public class LinkAffiliationDocument {
	static void inserisciAffiliation(GraphDatabaseService graphDb, HashSet<String> result, String APIKEY, String TIPOLINK1, String TIPOLINK2, String TIPOLINK3) throws IOException {
		int i = 0;
		for(String scopus_id : result){
			i++;
			//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
			//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&httpAccept=application/json&field=affiliation");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			if (jsonElement!=null) {

				JsonElement jso = jsonElement.getAsJsonObject()
						.get("abstracts-retrieval-response").getAsJsonObject().get("affiliation");
				String affiliationId;
				String affiliationName;
				String affiliationCountry;
				String affiliationCity;
				if (jso!=null) {

					try {
						affiliationId = jso.getAsJsonObject().get("@id").toString();
						affiliationId = affiliationId.substring(1, affiliationId.length()-1);

						affiliationName = jso.getAsJsonObject().get("affilname").toString();
						affiliationName = affiliationName.substring(1, affiliationName.length()-1);

						affiliationCity = jso.getAsJsonObject().get("affiliation-city").toString();
						affiliationCity = affiliationCity.substring(1, affiliationCity.length()-1);

						affiliationCountry = jso.getAsJsonObject().get("affiliation-country").toString();
						affiliationCountry = affiliationCountry.substring(1, affiliationCountry.length()-1);
						StartGraphDB.insertRelationAttribute(graphDb, scopus_id, "SCOPUS_ID", affiliationName , "AFFILIATION_NAME",affiliationId, "AFFILIATION_ID",  TIPOLINK1);
						StartGraphDB.insertRelationAttribute(graphDb, affiliationCity, "CITY", affiliationName, "AFFILIATION_NAME", affiliationId, "AFFILIATION_ID",  TIPOLINK2);
						StartGraphDB.insertRelation(graphDb, affiliationCity, "CITY", affiliationCountry, "COUNTRY", TIPOLINK3);
					} catch (Exception e) {
						//salvo l'array:
						try {
							JsonElement jsa = jso.getAsJsonArray().get(0);
							//in questo caso è un array, quindi lo leggo come tale
							affiliationId = jsa.getAsJsonObject().get("@id").toString();
							affiliationId = affiliationId.substring(1, affiliationId.length()-1);

							affiliationName = jsa.getAsJsonObject().get("affilname").toString();
							affiliationName = affiliationName.substring(1, affiliationName.length()-1);

							affiliationCity = jsa.getAsJsonObject().get("affiliation-city").toString();
							affiliationCity = affiliationCity.substring(1, affiliationCity.length()-1);

							affiliationCountry = jsa.getAsJsonObject().get("affiliation-country").toString();
							affiliationCountry = affiliationCountry.substring(1, affiliationCountry.length()-1);	

							StartGraphDB.insertRelationAttribute(graphDb, scopus_id, "SCOPUS_ID", affiliationName , "AFFILIATION_NAME",affiliationId, "AFFILIATION_ID",  TIPOLINK1);
							StartGraphDB.insertRelationAttribute(graphDb, affiliationCity, "CITY", affiliationName, "AFFILIATION_NAME", affiliationId, "AFFILIATION_ID",  TIPOLINK2);
							StartGraphDB.insertRelation(graphDb, affiliationCity, "CITY", affiliationCountry, "COUNTRY", TIPOLINK3);

						} catch (Exception e2) {
						}

					}		
				}
				System.out.println(i);
			}	
		}
	}
}
