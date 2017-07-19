package com.openshift.evg.roadshow.db;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.openshift.evg.roadshow.model.Coordinates;
import com.openshift.evg.roadshow.model.DataPoint;
import com.openshift.evg.roadshow.model.MLBPark;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by jmorales on 11/08/16.
 */
public class MongoDBConnection extends DBConnection{

    private static final String PROPERTIES = "/etc/config/parksmap.properties";

    private static final String COLLECTION = "parks";

    // TODO: Get this from a Config file
    String dbHost = System.getenv("DB_HOST");
    String dbPort = System.getenv("DB_PORT");
    String dbUsername = System.getenv("DB_USERNAME");
    String dbPassword = System.getenv("DB_PASSWORD");
    String dbName = System.getenv("DB_NAME");
    
    MongoDatabase db = null;
    
    public void connect() {
        System.out.println("[DEBUG] MongoDBConnection.connect()");
        
        Properties props = new Properties();
        try {
			props.load(new FileInputStream(PROPERTIES));
		    dbHost = props.getProperty("DB_HOST");
		    dbPort = props.getProperty("DB_PORT");
		    dbUsername = props.getProperty("DB_USERNAME");
		    dbPassword = props.getProperty("DB_PASSWORD");
		    dbName = props.getProperty("DB_NAME");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        List<MongoCredential> creds = new ArrayList<MongoCredential>();

        boolean configError = false;
        if (dbHost == null || dbHost.equals("")) {
            configError = true;
            System.out.println("[ERROR] DB_HOST environment variable not set");
        }
        if (dbPort == null || dbUsername.equals("")) {
            configError = true;
            System.out.println("[ERROR] DB_PORT environment variable not set");
        }
        if (dbUsername == null || dbUsername.equals("")) {
            configError = true;
            System.out.println("[ERROR] DB_USERNAME environment variable not set");
        }
        if (dbPassword == null || dbPassword.equals("")) {
            configError = true;
            System.out.println("[ERROR] DB_PASSWORD environment variable not set");
        }
        if (dbName == null || dbName.equals("")) {
            configError = true;
            System.out.println("[ERROR] DB_NAME environment variable not set");
        }

        if (configError) throw new RuntimeException("Error in configuration");
        System.out.println("DB_HOST=" + dbHost);
        System.out.println("DB_PORT=" + dbUsername);
        System.out.println("DB_USERNAME=" + dbUsername);
        System.out.println("DB_PASSWORD=" + dbPassword);
        System.out.println("DB_NAME=" + dbName);

        creds.add(MongoCredential.createCredential(dbUsername, dbName, dbPassword.toCharArray()));

        MongoClient mongoClient = new MongoClient(new ServerAddress(dbHost, Integer.valueOf(dbPort)), creds);
        db = mongoClient.getDatabase(dbName);
    }


    public void clear(MongoDatabase database) {
        System.out.println("[DEBUG] MongoDBConnection.clear()");

        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        collection.drop();
    }


    public void init(List<Document> parks) {
        System.out.println("[DEBUG] MongoDBConnection.init(...)");

        MongoCollection<Document> collection = db.getCollection(COLLECTION);

        System.out.println("Items before insert: " + collection.count());
        if (collection.count() != 0) {
            collection.drop();
            System.out.println("Items droped");
            System.out.println("Items after drop: " + collection.count());
        }
        collection.insertMany(parks);
        System.out.println("Items after insert: " + collection.count());
    }

    public long sizeInDB() {
        MongoCollection<Document> collection = db.getCollection(COLLECTION);
        return collection.count();
    }

    public void insert(MongoDatabase database, List<Document> parks) {
        MongoCollection<Document> collection = database.getCollection(COLLECTION);

        System.out.println("Items before insert: " + collection.count());
        collection.insertMany(parks);
        System.out.println("Items after insert: " + collection.count());
    }

    /**
     * @param database
     * @return
     */
    public MongoCollection<Document> getCollection(MongoDatabase database) {
        return database.getCollection(COLLECTION);
    }

    /**
     * @param database
     * @return
     */
    public List<DataPoint> getAll(MongoDatabase database) {
        System.out.println("[DEBUG] MongoDBConnection.getAll()");

        int i = 0;
        FindIterable<Document> iterable = this.getCollection(database).find();
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        for (Document current : iterable) {
            DataPoint dataPoint = getPark(current);
            System.out.println("Adding item " + i++ + ": " + dataPoint);
            dataPoints.add(dataPoint);
        }
        return dataPoints;
    }

    public List<DataPoint> getByQuery(BasicDBObject query) {
        System.out.println("[DEBUG] MongoDBConnection.getByQuery()");

        int i = 0;
        FindIterable<Document> iterable = this.getCollection(db).find(query);
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        for (Document current : iterable) {
            DataPoint dataPoint = getPark(current);
            System.out.println("Adding item " + i++ + ": " + dataPoint);
            dataPoints.add(dataPoint);
        }
        return dataPoints;
    }



    public static void main(String[] args) {
        MongoDBConnection con = new MongoDBConnection();
        List<Document> parks = con.loadParks("/Users/jmorales/repositories/jorgemoralespou/openshift/roadshow-mongodb/parks-mongo/src/main/resources/parks.json");
        con.connect();
        con.init(parks);
        System.out.println("Number of national parks in the DB: " + con.sizeInDB());

        System.out.println("Get list of parks in DB");
        List<DataPoint> dataPointList = con.getAll();
        for (DataPoint dataPoint : dataPointList) {
            System.out.println("DataPoint: " + dataPoint.toString());
        }
    }


}
