package scopusextraction;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;

import org.neo4j.graphdb.GraphDatabaseService;

import neo4j.StartGraphDB;

public class LinkAbstract {
	static void inserisciAbstract(GraphDatabaseService graphDb, HashSet<String> result, String APIKEY, String TIPOLINK) throws IOException {
		int i = 0;
		int counterr = 0;
		LinkedList<String> errori = new LinkedList<>();
		System.out.println(result.size());
		try {
			for(String scopus_id : result){
				i++;
				
				//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
				//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
				URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&field=dc:description");
				String surl = (String) url.toString();
				
				String extract = XMLExtraction.estraiAbstract(surl);
				//			System.out.println(extract);
				StartGraphDB.insertRelation(graphDb, scopus_id, "SCOPUS_ID", extract , "ABSTRACT", TIPOLINK);
				System.out.println(i);
				System.out.println(scopus_id);
				if(extract.length()==0 || extract.equals(null)){
					errori.add(scopus_id);
					counterr++;
				}
//					System.out.println(extract.substring(0, 10));
				
//				if(extract.length()>0){
//					inserisciTagDaAbstract(graphDb, extract);
//				}
			}	
			
		} catch (Exception e) {
			// TODO: handle exception
			counterr++;
			
			System.out.println(counterr+ " - "+e);
		}
		System.out.println("numero totale di errori "+counterr);
		for(String s : errori){
			
			System.out.println(s);
		}
	}
}
