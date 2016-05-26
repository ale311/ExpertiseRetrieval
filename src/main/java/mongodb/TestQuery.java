package mongodb;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.omg.CORBA.Current;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import authorOperations.AuthorExtraction;
import scala.util.parsing.json.JSONArray;
import scala.util.parsing.json.JSONObject;

public class TestQuery {
	public static void main (String [] args){
		HashMap<String, Double> result = AuthorExtraction.authorExtractionTagWeight("6603406713");
		for(String s: result.keySet()){
			System.out.println(s);
			System.out.println(result.get(s));
		}
	}
}
