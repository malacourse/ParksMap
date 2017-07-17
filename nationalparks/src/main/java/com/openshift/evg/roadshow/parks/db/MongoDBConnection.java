package com.openshift.evg.roadshow.parks.db;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.openshift.evg.roadshow.parks.model.Park;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmorales on 11/08/16.
 */
@Component
public class MongoDBConnection extends DBConnection{

    private static final String FILENAME = "/nationalparks.json";

    private static final String COLLECTION = "nationalparks";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Environment env;

    private MongoDatabase mongoDB = null;

    public MongoDBConnection() {
    }

    @PostConstruct
    public void initConnection() {
        String mongoHost = env.getProperty("DB_HOST", "127.0.0.1"); // env var MONGODB_SERVER_HOST takes precedence
        String mongoPort = env.getProperty("DB_PORT", "27017"); // env var MONGODB_SERVER_PORT takes precedence
        String mongoUser = env.getProperty("DB_USERNAME", "mongodb"); // env var MONGODB_USER takes precedence
        String mongoPassword = env.getProperty("DB_PASSWORD", "mongodb"); // env var MONGODB_PASSWORD takes precedence
        String mongoDBName = env.getProperty("DB_NAME", "mongodb"); // env var MONGODB_DATABASE takes precedence

        try {
            String mongoURI = "mongodb://" + mongoUser + ":" + mongoPassword + "@" + mongoHost + ":" + mongoPort + "/" + mongoDBName;
            System.out.println("[INFO] Connection string: " + mongoURI);
            MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURI));
            mongoDB = mongoClient.getDatabase(mongoDBName);
        } catch (Exception e) {
            System.out.println("[ERROR] Creating the mongoDB. " + e.getMessage());
            mongoDB = null;
        }
    }


    /**
     *
     */
    public void clear() {
        System.out.println("[DEBUG] MongoDBConnection.clear()");
        if (mongoDB != null) {
            try{
                mongoDB.getCollection(COLLECTION).drop();
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }
        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
    }


    /**
     * @param parks
     */
    public void init(List<Document> parks) {
        System.out.println("[DEBUG] MongoDBConnection.init(...)");
        if (mongoDB != null) {
            try {
                mongoDB.getCollection(COLLECTION).drop();
                mongoDB.getCollection(COLLECTION).insertMany(parks);
                mongoDB.getCollection(COLLECTION).createIndex(new BasicDBObject().append("coordinates", "2d"));
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }
        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }

    }

    /**
     * @return
     */
    public long sizeInDB() {
        long size = 0;

        if (mongoDB != null) {
            try {
                size = mongoDB.getCollection(COLLECTION).count();
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }

        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
        return size;
    }

    /**
     * @param parks
     */
    public void insert(List<Document> parks) {
        if (mongoDB != null) {
            try {
                mongoDB.getCollection(COLLECTION).insertMany(parks);
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }
        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }

    }

    /**
     * @return
     */
    public List<Park> getAll() {
        System.out.println("[DEBUG] MongoDBConnection.getAll()");
        ArrayList<Park> allParksList = new ArrayList<Park>();

        if (mongoDB != null) {
            try {
                MongoCollection parks = mongoDB.getCollection(COLLECTION);
                MongoCursor<Document> cursor = parks.find().iterator();
                try {
                    while (cursor.hasNext()) {
                        allParksList.add(ParkReadConverter.convert(cursor.next()));
                    }
                } finally {
                    cursor.close();
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }
        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
        return allParksList;
    }

    public List<Park> getWithin(float lat1, float lon1, float lat2, float lon2) {
        System.out.println("[DEBUG] MongoDBConnection.getAll()");
        ArrayList<Park> allParksList = new ArrayList<Park>();

        if (mongoDB != null) {
            try {
                MongoCollection parks = mongoDB.getCollection(COLLECTION);
                // make the query object
                BasicDBObject spatialQuery = new BasicDBObject();
                ArrayList<double[]> boxList = new ArrayList<double[]>();
                boxList.add(new double[]{new Float(lat2), new Float(lon2)});
                boxList.add(new double[]{new Float(lat1), new Float(lon1)});
                BasicDBObject boxQuery = new BasicDBObject();
                boxQuery.put("$box", boxList);
                spatialQuery.put("pos", new BasicDBObject("$within", boxQuery));
                System.out.println("Using spatial query: " + spatialQuery.toString());

                MongoCursor<Document> cursor = parks.find(spatialQuery).iterator();
                try {
                    while (cursor.hasNext()) {
                        allParksList.add(ParkReadConverter.convert(cursor.next()));
                    }
                } finally {
                    cursor.close();
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }

        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
        return allParksList;
    }

    /**
     * @param query
     * @return
     */
    public List<Park> getByQuery(BasicDBObject query) {
        System.out.println("[DEBUG] MongoDBConnection.getByQuery()");
        List<Park> parks = new ArrayList<Park>();
        if (mongoDB != null) {
            try {
                MongoCursor<Document> cursor = mongoDB.getCollection(COLLECTION).find(query).iterator();
                int i = 0;
                try {
                    while (cursor.hasNext()) {
                        parks.add(ParkReadConverter.convert(cursor.next()));
                    }
                } finally {
                    cursor.close();
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }

        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
        return parks;
    }
}
