package test;

	import com.mongodb.BasicDBObject;
	import com.mongodb.BulkWriteOperation;
	import com.mongodb.BulkWriteResult;
	import com.mongodb.Cursor;
	import com.mongodb.DB;
	import com.mongodb.DBCollection;
	import com.mongodb.DBCursor;
	import com.mongodb.DBObject;
	import com.mongodb.MongoClient;
	import com.mongodb.ParallelScanOptions;
	import com.mongodb.ServerAddress;
	
	import java.util.List;
	import java.util.Set;
	
	import static java.util.concurrent.TimeUnit.SECONDS;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import redis.clients.jedis.Jedis;

public class Test{

	public static void main (String [] args) throws Exception{
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "mydb" );
		DBCollection coll = db.getCollection("testCollection");
		BasicDBObject doc = new BasicDBObject("name", "MongoDB")
		        .append("type", "database")
		        .append("count", 1)
		        .append("info", new BasicDBObject("x", 203).append("y", 102));
		coll.insert(doc);
	}

}