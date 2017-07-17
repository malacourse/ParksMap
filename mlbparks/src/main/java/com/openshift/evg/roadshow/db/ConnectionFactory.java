package com.openshift.evg.roadshow.db;

public class ConnectionFactory {
	
	public static DBConnection connect()
	{
		try {
		    String dbHost = System.getenv("DB_HOST");
	        if (dbHost == null || dbHost.equals("")) {
	        	return new TestConnection();
	        }
	        else
	        {
	        	return new MongoDBConnection();
	        }
			
			
		}
		catch (Exception ex)
		{
			
		}
		return null;
	}

}
