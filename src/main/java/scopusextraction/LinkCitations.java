package scopusextraction;

import java.util.HashSet;

import org.neo4j.graphdb.GraphDatabaseService;

import neo4j.StartGraphDB;

public class LinkCitations {

	public static void inserisciCitations(GraphDatabaseService graphDb, HashSet<String> result, String APIKEY,
			String cita) {
		// TODO Auto-generated method stub
		for(String scopus_id : result){
			HashSet<String> citazioni = new HashSet<>();
			citazioni = EstrazioneCitazioni.getAllCitations(scopus_id, APIKEY);
			for(String cit : citazioni){
				StartGraphDB.insertRelation(graphDb, cit, "SCOPUS_ID", scopus_id , "SCOPUS_ID", cita);
			}
		}
	}
	
}
