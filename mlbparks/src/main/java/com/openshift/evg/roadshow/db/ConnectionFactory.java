package com.openshift.evg.roadshow.db;

public class ConnectionFactory {
	
	public static DBConnection connect()
	{
		try {
		    String dbType = System.getenv("DB_TYPE");
	        if (dbType == null || dbType.equals("")) {
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
