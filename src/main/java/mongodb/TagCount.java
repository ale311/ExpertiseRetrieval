package mongodb;

import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.bson.BSON;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class TagCount {
	public static void main(String[] x){
		final String authorId = "6603406713";
//		HashMap<String, Integer> tagCount = tagCount(authorId);
		int docCount = docCount(authorId);
//		int allTagCount = allTagCount(authorId);
		System.out.println(docCount);
		
//		for(String s : result.keySet()){
//			System.out.println(s+" : "+result.get(s));
//		}
	}
	
	public static int docCount (String authorId){
		int result = 0;
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
			result++;
		}
		return result;
	}
	public static int allTagCount(String authorId){
		int result1 = 0;
		int result2 = 0;
		
		HashMap<String, Integer> tagCount = tagCount(authorId);
		for(String s : tagCount.keySet()){
			result1 = result1 + tagCount.get(s);
		}
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
			for(Object cu : current){
				result2 = result2 + 1;
			}
		}
		return result1;
//		return result2;
	}


	public static HashMap<String, Integer> tagCount(String authorId){
		HashMap<String, Integer> result = new HashMap<>();
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
		int i = 0;
		for(Object jse : jsa){
			org.json.JSONObject jso = (org.json.JSONObject) jse;
			org.json.JSONArray current  =(org.json.JSONArray) jso.get("tag");
			for(Object cu : current){
				int valore = 0;
				org.json.JSONObject c = (org.json.JSONObject) cu;
				String currentTag = c.getString("tag");
				if(result.containsKey(currentTag)){
					 valore = result.get(currentTag)+1;
				}
				else{
					valore = 1;
				}
				result.put(currentTag, valore );
			}
		}
		return result;
	}
	
}


