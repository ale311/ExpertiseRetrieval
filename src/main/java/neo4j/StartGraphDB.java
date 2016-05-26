package neo4j;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import scala.annotation.meta.param;

public class StartGraphDB {
	public enum DB_PATH {

	}

	protected static final String DB_PATH = "util/neo4j-community-2.3.2/data/graph.db";
	
	
	public static void main(String[] args) throws IOException {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
	}

	public static int insertDocuments(HashSet<String> result) throws IOException {
		FileUtils.deleteRecursively( new File( DB_PATH ) );
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		ResourceIterator<Node> resultIterator = null;
		// TODO Auto-generated method stub
		for(String scopus_id : result){
			HashSet<String> paperAuthors = new HashSet<String>();
			String paperTitle;
			String paperExtract;
			System.out.println("sto inserendo: "+scopus_id);
			try(Transaction tx = graphDb.beginTx()){
				Map<String,Object> parameters = new HashMap<String, Object>();
				String queryString = "MERGE(p:Paper{ID:{scopus_id}})";
				parameters.put("scopus_id", scopus_id);
				graphDb.execute(queryString, parameters);
				tx.success();
			}
		}
		return 0;
	}

	public static void insertRelation(GraphDatabaseService graphDb, String nomeNodo1, String tipoNodo1, String nomeNodo2, String tipoNodo2, String tipoRel){
		// TODO Auto-generated method stub

		Map<String,Object> parameters = new HashMap<String, Object>();
		String queryString = "merge(n1:"+tipoNodo1+"{"+tipoNodo1+":{nomeNodo1}})"+
				"merge(n2:"+tipoNodo2+"{"+tipoNodo2+":{nomeNodo2}})"+
				"merge(n1)-[:"+tipoRel+"]-(n2)";
		
		parameters.put("nomeNodo1", nomeNodo1);
		parameters.put("nomeNodo2", nomeNodo2);
		parameters.put("tipoRelazione", tipoRel);
		graphDb.execute(queryString, parameters);
	}
	

	public static GraphDatabaseService costruisciGrafo() {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		return graphDb;
	}

	public static GraphDatabaseService formattaGrafo() throws IOException {
		FileUtils.deleteRecursively( new File( DB_PATH ) );
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		return graphDb;
	}

	public static void insertRelationAttribute(GraphDatabaseService graphDb, String nomeNodo1, String tipoNodo1, String attrNodo1, String tipoAttrNodo1, String nomeNodo2, String tipoNodo2, String tipoRel){
		// TODO Auto-generated method stub

		Map<String,Object> parameters = new HashMap<String, Object>();
		String queryString = "merge(n1:"+tipoNodo1+"{"+tipoNodo1+":{nomeNodo1}})"+
				"merge(n2:"+tipoNodo2+"{"+tipoNodo2+":{nomeNodo2},"+tipoAttrNodo1+":{attributo1}})"+
				"merge(n1)-[:"+tipoRel+"]-(n2)";
		
		parameters.put("nomeNodo1", nomeNodo1);
		parameters.put("nomeNodo2", nomeNodo2);
		parameters.put("attributo1", attrNodo1);
		parameters.put("tipoRelazione", tipoRel);
		graphDb.execute(queryString, parameters);
	}

	

	public static void insertRelationAttribute2(GraphDatabaseService graphDb, String nomeNodo1, String tipoNodo1,
			String attrNodo1, String tipoAttrNodo1, String nomeNodo2, String tipoNodo2, String attrNodo2,
			String tipoAttrNodo2, String tipoRel) {
		// TODO Auto-generated method stub
		Map<String,Object> parameters = new HashMap<String, Object>();
		String queryString = "merge(n1:"+tipoNodo1+"{"+tipoNodo1+":{nomeNodo1},"+tipoAttrNodo1+":{attributo1}})"+
				"merge(n2:"+tipoNodo2+"{"+tipoNodo2+":{nomeNodo2},"+tipoAttrNodo2+":{attributo2}})"+
				"merge(n1)-[:"+tipoRel+"]-(n2)";
		
		parameters.put("nomeNodo1", nomeNodo1);
		parameters.put("nomeNodo2", nomeNodo2);
		parameters.put("attributo1", attrNodo1);
		parameters.put("attributo2", attrNodo2);
		parameters.put("tipoRelazione", tipoRel);
		graphDb.execute(queryString, parameters);
	}

	public static void insertRelationProperty(GraphDatabaseService graphDb, String nomeNodo1, String tipoNodo1, 
			String nomeNodo2, String tipoNodo2,
			String tipoRel, String PropertyRel, String ValuePropertyRel){
		System.out.println(PropertyRel);
		System.out.println(ValuePropertyRel);
		Map<String,Object> parameters = new HashMap<String, Object>();
		String queryString = "merge(n1:"+tipoNodo1+"{"+tipoNodo1+":{nomeNodo1}})"+
				"merge(n2:"+tipoNodo2+"{"+tipoNodo2+":{nomeNodo2}})"+
				"merge(n1)-[:"+tipoRel+"{"+PropertyRel+":"+ValuePropertyRel+"}]-(n2)";
		
		parameters.put("nomeNodo1", nomeNodo1);
		parameters.put("nomeNodo2", nomeNodo2);
		parameters.put("ValuePropertyRel", ValuePropertyRel);
		parameters.put("tipoRelazione", tipoRel);
		graphDb.execute(queryString, parameters);
	}
	
}
