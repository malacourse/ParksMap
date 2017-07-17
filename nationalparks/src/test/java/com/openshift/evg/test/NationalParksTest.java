package com.openshift.evg.test;

import java.util.List;

import org.bson.Document;
import org.junit.Test;

import com.openshift.evg.roadshow.parks.db.DBConnection;
import com.openshift.evg.roadshow.parks.db.TestDBConnection;
import com.openshift.evg.roadshow.parks.model.Park;
import com.openshift.evg.roadshow.parks.rest.Parks;

public class NationalParksTest  {

    @Test
    public void testLoadMlbParks() {
    	System.out.println("Testing load");
    	DBConnection conn = new TestDBConnection();
    	Parks parks = new Parks(conn);
    	String res = parks.load();
    	System.out.println("Res:" + res);
    	assert(res.startsWith("Items inserted in database: 10") == true);
    }
    
    @Test
    public void testGetAllParks() {

        try {
        	System.out.println("Testing get all");
        	DBConnection conn = new TestDBConnection();
        	Parks parks = new Parks(conn);
        	String res = parks.load();
        	List<Park> list = parks.getAllParks();
            assert(list.size() == 10);
        }catch(Exception e){
            System.out.println("[ERROR] Connecting to database");
        }
    	
    }
    
    @Test
    public void testClearParks() {

        try {
        	System.out.println("Testing clear");
        	DBConnection conn = new TestDBConnection();
        	List<Document> docs = conn.loadParks();
        	conn.init(docs);
        	List<Park> list = conn.getAll();
            assert(list.size() == 10);
            conn.clear();
        	list = conn.getAll();
            assert(list.size() == 0);
        }catch(Exception e){
            System.out.println("[ERROR] Connecting to database");
        }
    	
   }
    
}