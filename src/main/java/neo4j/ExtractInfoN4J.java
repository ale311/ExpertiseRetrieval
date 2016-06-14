package neo4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.logging.Log;
import org.neo4j.logging.LogProvider;

import authorOperations.LogProviderSetup;
import authorOperations.authorEvaluation;


public class ExtractInfoN4J {
	static String DB_PATH = StartGraphDB.DB_PATH;
	static GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
	static LogProvider lp = LogProviderSetup.getLogProvider();

	public static HashMap<String, Double> getTagsAndWeight(){
		HashMap<String, Double> result = new HashMap<>();
		try ( Transaction ignored = db.beginTx();
				Result res = db.execute( "match (t:TAG)-[r:tag]-(a:ABSTRACT)-[]-(s:SCOPUS_ID)-[r2:ha_autore]-(author) where r.weight>0 return t.TAG, r.weight as peso,count(author) order by peso desc" ) )
		{
			while ( res.hasNext() ){
			        Map<String,Object> row = res.next();
			        String tag = (String) row.get("t.TAG");
			        Double weight = (Double) row.get("peso");
			        String rows=tag+"  "+weight;
			        result.put(tag, weight);
			    }
		}
		return result;
	}
	
	public static HashSet<String> getOnlyTags(){
		HashSet<String> result = new HashSet();
		try ( Transaction ignored = db.beginTx();
				Result res = db.execute( "match (t:TAG)-[r:tag]-(a:ABSTRACT)-[]-(s:SCOPUS_ID)-[r2:ha_autore]-(author) where r.weight>0 return t, r.weight as peso,count(author) order by peso desc" ) )
		{
			Iterator<Node> n_column = res.columnAs( "t" );
			for ( Node node : IteratorUtil.asIterable( n_column ) )
			{
				String property = (String) node.getProperty("TAG");
				result.add(property);
			}
			return result;
		}
	}
}
