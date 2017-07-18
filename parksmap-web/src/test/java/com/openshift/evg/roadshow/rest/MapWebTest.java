package com.openshift.evg.roadshow.rest;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import com.openshift.evg.roadshow.rest.gateway.ApiGatewayController;
import com.openshift.evg.roadshow.rest.gateway.model.Backend;


public class MapWebTest  {

    @Test
    public void testGetRemote() {
    	System.out.println("Running test Get Remote");
        try {
        	String backendurl = getBackendUrl();
        	System.out.println("URL" + backendurl);
        	ApiGatewayController ctrl = new ApiGatewayController();
        	Backend be = ctrl.getFromRemote(backendurl);
        	System.out.println("BE:" + be);
        	assert(be != null);
        }catch(Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }
    
    @Test
    public void testAddLocalBackEnd() {

    	System.out.println("Running test Add Local Backend");
        try {
        	String backendurl = getBackendUrl();
        	System.out.println("URL" + backendurl);
        	ApiGatewayController ctrl = new ApiGatewayController();
        	ctrl.add("test",backendurl);
        	Backend be = ctrl.getFromLocal("test");
            assert(be != null);
        }catch(Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    	
    }
    
    @Test
    public void testRemoveBackend() {
    	System.out.println("Running test Remove Backend");
        try {
        	ApiGatewayController ctrl = new ApiGatewayController();
        	String backendurl = getBackendUrl();
        	System.out.println("URL" + backendurl);
        	ctrl.add("test",backendurl);
        	Backend be = ctrl.getFromLocal("test");
            assert(be != null);
            ctrl.remove("test");
        	be = ctrl.getFromLocal("test");
        	System.out.println("BE:" + be);
            assert(be == null);
        }catch(Exception e){
            System.out.println(e.getLocalizedMessage());
        }
        
    }
    
    private String getBackendUrl()
    {
    	  Properties prop = new Properties();
    	  InputStream in = MapWebTest.class.getClassLoader()
                  .getResourceAsStream("test.properties");
    	  try {
			prop.load(in);
	    	in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  return prop.getProperty("backendurl", "");
    }
}