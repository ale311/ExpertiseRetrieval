package neo4j;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

public class StartGraphDB {
	private static final String DB_PATH = "util/neo4j-community-2.3.2/data/graph.db";
	public static void main(String[] args) throws IOException {
		Date currentDate = new Date();

		System.out.println( "Starting database " + "GraphDB NanoIndentation Community "+currentDate.toString() );

//		FileUtils.deleteRecursively(new File(DB_PATH));
		
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		
//		FileUtils.deleteRecursively(new File(DB_PATH));
		
		ResourceIterator<Node> resultIterator = null;
		try(Transaction tx = graphDb.beginTx()){
			Map<String,Object> parameters = new HashMap<String, Object>();
			String queryString = "merge(u:Utente{Utente:{username}})"+
					"merge(n:Nazione{Nazione:{Nazione}})"+
					"merge(u)-[:VIVE_IN]-(n)";
			parameters.put("username", "Daniele");
			parameters.put("Nazione", "Italia");

			resultIterator = graphDb.execute(queryString, parameters).columnAs("utente vive in nazione");
		}
//		graphDb.shutdown();
//		currentDate = new Date();
//		System.out.println("shutdown avvenuto con successo alle ore: "+currentDate.toString());
	}
}