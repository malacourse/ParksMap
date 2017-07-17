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
public class DBConnection {

    private static final String FILENAME = "nationalparks.json";

    private static final String COLLECTION = "nationalparks";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Environment env;

    private MongoDatabase mongoDB = null;

    public DBConnection() {
    }

    @PostConstruct
    public void initConnection() {
    }

    /*
     * Load from embedded list of parks using FILENAME
     */
    public List<Document> loadParks() {
        System.out.println("[DEBUG] DBConnection.loadParks()");

        try {
            return loadParks(getClass().getClassLoader().getResourceAsStream(FILENAME));
            //return loadParks(resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX + FILENAME).getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        }
        return new ArrayList<Document>();
    }

    public List<Document> loadParks(String fileLocation) {
        System.out.println("[DEBUG] DBConnection.loadParks(" + fileLocation + ")");

        try {
            return loadParks(new FileInputStream(new File(fileLocation)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        }
        return new ArrayList<Document>();
    }

    public List<Document> loadParks(InputStream is) {
        System.out.println("[DEBUG] DBConnection.loadParks(InputStream)");

        List<Document> docs = new ArrayList<Document>();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        try {
            String currentLine = null;
            int i = 1;
            while ((currentLine = in.readLine()) != null) {
                String s = currentLine.toString();
                // System.out.println("line "+ i++ + ": " + s);
                Document doc = Document.parse(s);
                docs.add(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading parks. Return empty list");
            }
        }
        return docs;
    }


    /**
     *
     */
    public void clear() {
    }


    /**
     * @param parks
     */
    public void init(List<Document> parks) {
    }

    /**
     * @return
     */
    public long sizeInDB() {
        long size = 0;
        return size;
    }

    /**
     * @param parks
     */
    public void insert(List<Document> parks) {

    }

    /**
     * @return
     */
    public List<Park> getAll() {
        System.out.println("[DEBUG] MongoDBConnection.getAll()");
        return null;
    }

    public List<Park> getWithin(float lat1, float lon1, float lat2, float lon2) {
        System.out.println("[DEBUG] MongoDBConnection.getAll()");
        return null;
    }

    /**
     * @param query
     * @return
     */
    public List<Park> getByQuery(BasicDBObject query) {
        System.out.println("[DEBUG] MongoDBConnection.getByQuery()");
        return null;
    }
}
