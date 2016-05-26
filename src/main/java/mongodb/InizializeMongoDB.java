package mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class InizializeMongoDB {
	public static DBCollection buildDb(){
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "mydb" );
		DBCollection coll = db.getCollection("collection");
		return coll;
	}
	
}
