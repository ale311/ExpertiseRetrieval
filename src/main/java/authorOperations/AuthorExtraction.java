package authorOperations;

import java.util.HashMap;
import java.util.HashSet;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import mongodb.InizializeMongoDB;

public class AuthorExtraction {
	
	public static HashMap<String, Double> authorExtractionTagWeight(String authorId){
		HashMap<String, Double> result = new HashMap<>();
		DBCollection coll = InizializeMongoDB.buildDb();
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("authorId", authorId);
		BasicDBObject fields = new BasicDBObject();
		BasicDBObject projection = new BasicDBObject();
		projection.put("pubblicazioni.tag", 1);
		DBObject object = coll.findOne(whereQuery, projection);
		String jsonString = object.toString();

		org.json.JSONObject js = new org.json.JSONObject(jsonString);
		org.json.JSONArray jsa = (org.json.JSONArray) js.get("pubblicazioni");
		for(Object jse : jsa){
			org.json.JSONObject jso = (org.json.JSONObject) jse;
			org.json.JSONArray current  =(org.json.JSONArray) jso.get("tag");
			//			System.out.println(current);
			for(Object cu : current){
				org.json.JSONObject c = (org.json.JSONObject) cu;
				result.put(c.getString("tag"), c.getDouble("weight"));
			}
		}
		return result;
	}
	
	
	public static HashSet<String> authorExtractionOnlytag(String authorId){
		HashSet<String> result = new HashSet<>();
		DBCollection coll = InizializeMongoDB.buildDb();
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("authorId", authorId);
		BasicDBObject fields = new BasicDBObject();
		BasicDBObject projection = new BasicDBObject();
		projection.put("pubblicazioni.tag", 1);
		DBObject object = coll.findOne(whereQuery, projection);
		String jsonString = object.toString();

		org.json.JSONObject js = new org.json.JSONObject(jsonString);
		org.json.JSONArray jsa = (org.json.JSONArray) js.get("pubblicazioni");
		for(Object jse : jsa){
			org.json.JSONObject jso = (org.json.JSONObject) jse;
			org.json.JSONArray current  =(org.json.JSONArray) jso.get("tag");
			//			System.out.println(current);
			for(Object cu : current){
				org.json.JSONObject c = (org.json.JSONObject) cu;
				result.add(c.getString("tag"));
			}
		}
		return result;
	}
}
