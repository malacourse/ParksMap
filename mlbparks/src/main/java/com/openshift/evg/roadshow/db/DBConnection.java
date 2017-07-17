package com.openshift.evg.roadshow.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.openshift.evg.roadshow.model.Coordinates;
import com.openshift.evg.roadshow.model.DataPoint;
import com.openshift.evg.roadshow.model.MLBPark;
import org.bson.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmorales on 11/08/16.
 */
public class DBConnection {

    private static final String FILENAME = "mlbparks.json";

    private static final String COLLECTION = "parks";


    
    public void connect()
    {
    	
    }
    
    public long sizeInDB() {
        return 0;
    }


    /*
     * Load from embedded list of parks using FILENAME
     */
    public List<Document> loadParks() {
        System.out.println("[DEBUG] DBConnection.loadParks()");

        List<Document> docs = new ArrayList<Document>();
        try {
            docs.addAll(loadParks(getClass().getClassLoader().getResourceAsStream(FILENAME)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docs;
    }


    public List<Document> loadParks(String fileLocation) {
        System.out.println("[DEBUG] DBConnection.loadParks(" + fileLocation + ")");

        List<Document> docs = new ArrayList<Document>();
        try {
            docs.addAll(loadParks(new FileInputStream(new File(fileLocation))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docs;
    }

    public List<Document> loadParks(InputStream is) {
        System.out.println("[DEBUG] DBConnection.loadParks(InputStream)");
        String currentLine = null;

        List<Document> docs = new ArrayList<Document>();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        try {
            while ((currentLine = in.readLine()) != null) {
                docs.add(Document.parse(currentLine.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docs;
    }

    
    public void init(List<Document> parks) {
        System.out.println("[DEBUG] DBConnection.init(...)");

    }


    /**
     * @param database
     * @return
     */
    public List<DataPoint> getAll() {
        System.out.println("[DEBUG] DBConnection.getAll()");
        return null;
    }

    public List<DataPoint> getByQuery(BasicDBObject query) {
        System.out.println("[DEBUG] DBConnection.getByQuery()");

        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        return dataPoints;
    }

    /**
     * @param current
     * @return
     */
    public DataPoint getPark(Document current) {
        MLBPark park = new MLBPark();
        System.out.println("[DEBUG] DBConnection.getPark()");
        
        if (current.getObjectId("_id") != null)
        	park.setId(current.getObjectId("_id").toString());
        else
        	park.setId(1);
        park.setName(current.getString("name"));
        park.setBallpark(current.getString("ballpark"));

        Coordinates cord = new Coordinates(current.get("coordinates", List.class));
        park.setPosition(cord);
        park.setLatitude(cord.getLatitude());
        park.setLongitude(cord.getLongitude());

        park.setLeague(current.getString("league"));
        park.setPayroll(current.getInteger("payroll"));

        return park;
    }


}
