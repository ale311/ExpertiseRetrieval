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

public class Test{

	public static void main (String [] args) throws Exception{
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "mydb" );
		DBCollection coll = db.getCollection("collection");
		lanciaTest(coll);
	}
	
	public static void lanciaTest (DBCollection coll){
		
		BasicDBObject query = new BasicDBObject("weight", new BasicDBObject("$gt", 0.50));

		DBCursor cursor = coll.find(query);
		try {
		    while (cursor.hasNext()) {
		        System.out.println(cursor.next());
		    }
		} finally {
		    cursor.close();
		}
	}

}