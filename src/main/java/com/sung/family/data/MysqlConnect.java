package com.sung.family.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

public class MysqlConnect 
{
	private Connection connection;
    private Statement statement;
    private MysqlConnect instance;
    
    public MysqlConnect()
    {
    	
    }
    
    public MysqlConnect(String dbName) 
    {
        String driver = getProperty("SQLDRIVER");
        String userName = getProperty("SQLUSERNAME");
        String password = getProperty("SQLPASSWORD");
    	String url = getProperty(dbName);
    	
        try 
        {
            Class.forName(driver).newInstance();
            this.connection = (Connection)DriverManager.getConnection(url, userName, password);
        }
        catch (Exception sqle) 
        {
            sqle.printStackTrace();
        }
    }
    
    public PreparedStatement getStatement(String stmt) throws SQLException
    {
		return (PreparedStatement) connection.prepareStatement(stmt);
    }
    
    public Connection getConnect()
    {
    	return this.connection;
    }
    
    
    public PreparedStatement buildProcedure(String name, Object... params)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CALL ");
		sb.append(name);
		sb.append("(");
		
		for (int i = 0; i < params.length; i++)
		{
			if(i != params.length - 1)	
				sb.append("?,");
			else
				sb.append("?");
		}
		sb.append(")");
		
		PreparedStatement prep = null;
		try 
		{
			prep= getStatement(sb.toString());

			for (int i =0; i<params.length; i++)
			{
				if (params[i] instanceof String)
				{
					prep.setString(i+1, (String)params[i]);
				}
				else if(params[i] instanceof  Integer){
					prep.setInt(i+1, (Integer)params[i]);
				}
				else if(params[i] instanceof Double){
					prep.setDouble(i+1, (Double)params[i]);
				}
				else if(params[i] instanceof Float){
					prep.setFloat(i+1, (Float)params[i]);
				}
				else if(params[i] instanceof Long){
					prep.setLong(i+1, (Long)params[i]);
				}
				else if(params[i] instanceof Short){
					prep.setShort(i+1,(Short)params[i] );
				}
				else if(params[i] == null){
					prep.setNull(i+1, Types.VARCHAR);
				}
				else
				{
					throw new IllegalArgumentException("Library does not support " + params[i].getClass().getName() );
				}
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return prep;
	}

	
    public MysqlConnect getInstance() 
    {
    	String url = getProperty("MYSQL_URL");
        instance = new MysqlConnect(url);
        
        return instance;
    }
    
    public void close(Object ... params)
    {
    	try
    	{
	    	for(Object e : params)
	    	{
	    		if(e != null)
	    		{
		    		if(e instanceof PreparedStatement)
		    		{
		    			PreparedStatement ps =(PreparedStatement) e;
		    			ps.close();
		    		}
		    		else if(e instanceof ResultSet)
		    		{
		    			ResultSet rs =(ResultSet) e;
		    			rs.close();
		    		}
		    		else if(e instanceof MysqlConnect)
		    		{
		    			closeConnection();
		    		}
	    		}
	    	}
	    	
			try 
			{
			    AbandonedConnectionCleanupThread.shutdown();
			}
			catch (InterruptedException e)
			{
			    e.printStackTrace();
			}
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    		
			try 
			{
			    AbandonedConnectionCleanupThread.shutdown();
			}
			catch (InterruptedException e)
			{
			    e.printStackTrace();
			}
    	}
    }
    
    public void closeConnection()
    {
		try 
		{
			if(connection != null && !connection.isClosed())
		   	   connection.close();
			
		   	if(statement != null)
		   		statement.close();
		   	
		   	instance = null;
			connection = null;
			statement = null;
			
			try 
			{
			    AbandonedConnectionCleanupThread.shutdown();
			}
			catch (InterruptedException e)
			{
			    e.printStackTrace();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			try 
			{
			    AbandonedConnectionCleanupThread.shutdown();
			}
			catch (InterruptedException ex)
			{
			    ex.printStackTrace();
			}
		}
		
    }

	public String getProperty(String key)
	{
		String Result = "";
		Properties prop = getProperties("/config/application.properties");
		Result = prop.getProperty(key);
		return Result;
		
	}
  
	public Properties getProperties(String fileName) 
	{
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = this.getClass().getResourceAsStream(fileName);
			prop.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}
    
}