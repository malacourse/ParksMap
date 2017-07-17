package com.openshift.evg.roadshow.parks.db;

import com.mongodb.BasicDBObject;
import com.openshift.evg.roadshow.parks.model.Park;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jmorales on 11/08/16.
 */
@Component
public class TestDBConnection extends DBConnection{

    private static final String FILENAME = "nationalparks.json";

    private static final String COLLECTION = "nationalparks";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Environment env;

    private List<Park> _parks = null;

    public TestDBConnection() {
    }

    @PostConstruct
    public void initConnection() {
    }

    /**
     *
     */
    public void clear() {
        System.out.println("[DEBUG] TestDBConnection.clear()");
        if (_parks != null) _parks.clear();
    }


    /**
     * @param parks
     */
    public void init(List<Document> parks) {
        System.out.println("[DEBUG] TestDBConnection.init(...)");
        try {
        	_parks = new ArrayList<Park>();
            Iterator<Document> d = parks.iterator();
            
            while (d.hasNext())
            {
            	Document doc = d.next();
            	Park point = ParkReadConverter.convert(doc);
            	_parks.add(point);
            }
        	
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        }

    }

    /**
     * @return
     */
    public long sizeInDB() {
        long size = 0;

        if (_parks != null) size = _parks.size();
        return size;
    }

    /**
     * @param parks
     */
    public void insert(List<Document> parks) {
        if (_parks != null) {
            try {
                Iterator<Document> d = parks.iterator();
                
                while (d.hasNext())
                {
                	Document doc = d.next();
                	Park point = ParkReadConverter.convert(doc);
                	_parks.add(point);
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error connecting to MongoDB. " + e.getMessage());
            }
        } else {
            System.out.println("[ERROR] testDB could not be initiallized. No operation with DB will be performed");
        }

    }

    /**
     * @return
     */
    public List<Park> getAll() {
        System.out.println("[DEBUG] TestDBConnection.getAll()");
        return _parks;
    }

    public List<Park> getWithin(float lat1, float lon1, float lat2, float lon2) {
        System.out.println("[DEBUG] TestDBConnection.getAll()");
        ArrayList<Park> allParksList = new ArrayList<Park>();

        if (_parks != null) {
        } else {
            System.out.println("[ERROR] testDB could not be initiallized. No operation with DB will be performed");
        }
        return allParksList;
    }

    /**
     * @param query
     * @return
     */
    public List<Park> getByQuery(BasicDBObject query) {
        System.out.println("[DEBUG] TestDBConnection.getByQuery()");
        List<Park> parks = new ArrayList<Park>();
        if (_parks != null) {

        } else {
            System.out.println("[ERROR] mongoDB could not be initiallized. No operation with DB will be performed");
        }
        return parks;
    }
}
