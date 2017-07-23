package com.openshift.evg.roadshow.db;

public class ConnectionFactory {
	
	public static DBConnection connect()
	{
		try {
		    String dbType = System.getenv("DB_TYPE");
	        if (dbType == null || dbType.equals("")) {
	        	System.out.println("Returning Test DB Connection");
	        	return new TestConnection();
	        }
	        else
	        {
	        	System.out.println("Returning MongoDB Connection");
	        	return new MongoDBConnection();
	        }
			
			
		}
		catch (Exception ex)
		{
			
		}
		return null;
	}

}
