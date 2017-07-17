package com.openshift.evg.roadshow.db;

import com.mongodb.BasicDBObject;
import com.openshift.evg.roadshow.model.Coordinates;
import com.openshift.evg.roadshow.model.DataPoint;
import com.openshift.evg.roadshow.model.MLBPark;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jmorales on 11/08/16.
 */
public class TestConnection extends DBConnection{

    private static final String FILENAME = "/mlbparks.json";

    private static final String COLLECTION = "parks";

    List<DataPoint> points = new ArrayList<DataPoint>();

    public void connect() {
        System.out.println("[DEBUG] TestDBConnection.connect()");

    }



    public void init(List<Document> parks) {
        System.out.println("[DEBUG] TestDBConnection.init(...)" + parks.size());

        try {
            Iterator<Document> d = parks.iterator();
            
            while (d.hasNext())
            {
            	Document doc = d.next();
            	DataPoint point = getPark(doc);
            	points.add(point);
            }
        	
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        }
        
        System.out.println("Items after insert: " + points.size());
    }

    public long sizeInDB() {
        return points.size();
    }


    /**
     * @param database
     * @return
     */
    public List<DataPoint> getAll() {
        System.out.println("[DEBUG] TestDBConnection.getAll()");

        return points;
    }

    public List<DataPoint> getByQuery(BasicDBObject query) {
        System.out.println("[DEBUG] MongoDBConnection.getByQuery()");

        return null;
    }


}
