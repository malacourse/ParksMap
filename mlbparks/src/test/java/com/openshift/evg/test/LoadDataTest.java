package com.openshift.evg.test;

import java.util.List;

import org.bson.Document;
import org.junit.Test;

import com.openshift.evg.roadshow.db.ConnectionFactory;
import com.openshift.evg.roadshow.db.DBConnection;
import com.openshift.evg.roadshow.model.DataPoint;
import com.openshift.evg.roadshow.rest.MLBParks;

public class LoadDataTest  {

    @Test
    public void testLoadMlbParks() {
    	System.out.println("Testing load");
    	MLBParks parks = new MLBParks();
    	String res = parks.load();
    	assert(res.startsWith("0 Items") == false);
    }
    
    @Test
    public void testGetAllParks() {

        try {
            //MongoDBConnection con = new MongoDBConnection();
            DBConnection con = ConnectionFactory.connect();
            List<Document> parks = con.loadParks();
            con.init(parks);
            List<? extends DataPoint> points = con.getAll();
            assert(points.size() > 0);
        }catch(Exception e){
            System.out.println("[ERROR] Connecting to database");
        }
    	
    }
    
    @Test
    public void testGetPark() {

        try {
            //MongoDBConnection con = new MongoDBConnection();
            DBConnection con = ConnectionFactory.connect();
            List<Document> parks = con.loadParks();
            con.init(parks);
            DataPoint point = con.getPark(parks.get(0));
            assert(point.getName() != null);
        }catch(Exception e){
            System.out.println("[ERROR] Connecting to database");
        }
    	
    }
    
}