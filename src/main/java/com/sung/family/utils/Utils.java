package com.sung.family.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sung.family.models.Review;

public class Utils 
{
	private static Utils instance;

	public static Utils getInstance()
	{
		if(instance == null)
			instance = new Utils();
		
		return instance;
	}
	
	public static final String applicationProperties = "/config/application.properties";
	
	
	public String postToGCM(String url, String apiKey, String jsonObject) 
	{
		String responseString ="";
		
		HttpClient httpClient = HttpClientBuilder.create()
												 .build();
		try 
		{
		    HttpPost request = new HttpPost(url);
		    StringEntity params = new StringEntity(jsonObject);
		    request.addHeader("Authorization", "key=" + apiKey);
		    request.addHeader("Content-Type", "application/json");
		    request.setEntity(params);
		    HttpEntity response = httpClient.execute(request).getEntity();
		   
		    InputStream content = response.getContent();
		    responseString = convertStreamToString(content);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			responseString = ex.getMessage();
		}
		
		System.out.println(responseString);
		return responseString;
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
	
	public boolean isNullOrEmpty(String input)
	{
		if(input == null)
			return true;
		
		if(input.equals("")|| input.equals("0"))
			return true;
		
		return false;
	}
	
	public int getIntValue(int id)
	{
		if(id == -1)
		{
			return 0;
		}
		return id;
	}
	
	public String getStringValue(String input)
	{
		if(input == null || input.equals("")|| input.equals("0"))
			return "";
		
		return input;
	}
	
	
    public String generateRandomEmailAddress(String firstName, String lastName)
    {
    	
    	if(firstName == null || lastName == null || firstName.equals("")|| lastName.equals(""))
    	{
    		return UUID.randomUUID() + "@gmail.com";
    	}
    	
        return firstName + "." + lastName + System.currentTimeMillis() + "@gmail.com";
    }

    public String generateRandomPassword()
    {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }
    
    public String generateRandomPhoneNumber()
    {    	
    	return String.valueOf(getRandomNumber(1000000000, 2000000000));
    }
    
    public int generateRandomPhoneNumberInt()
    {
    	return getRandomNumber(1000000000, 2000000000);
    }
   
	
	public int getRandomNumber(int min, int max) {

	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	

    public String getCurrentDateTime()
    {
        String today = "";
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            today = dateFormat.format(date);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return today;
    }
    
	public String convertDataTimeToDate(String input)
	{
        if (input == null ||input.equals(""))
            return new String("");
        
        String newDate = "";
       
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //original format...
        try 
        {
			Date date = newDateFormat.parse(input);
			newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			newDate = newDateFormat.format(date);
		} 
        catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return newDate;
	}
    

    public String getParameter(String obj)
    {
    	if(obj == null || obj.equals(""))
    	{
    		return "''";
    	}
    	return "'"+StringEscapeUtils.escapeSql(obj)+"'";
    }
    
    
    
	
    public String getRandomGender()
    {
    	String [] genders = {"M", "F", "U"};
    	
    	return genders[this.getRandomNumber(0, 2)];
    }
    
	
	public String getRandomDOBs()
	{
		GregorianCalendar gc = new GregorianCalendar();
		int year = getRandomNumber(1900, 2010);
		gc.set(gc.YEAR, year);
		int dayOfYear = getRandomNumber(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
		gc.set(gc.DAY_OF_YEAR, dayOfYear);
		return gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH);
	}
	
	public String getRandomScheduledTime()
	{
		GregorianCalendar gc = new GregorianCalendar();
		int year = getRandomNumber(2014, 2016);
		gc.set(gc.YEAR, year);
		int dayOfYear = getRandomNumber(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
		gc.set(gc.DAY_OF_YEAR, dayOfYear);
		return gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH) + " " + getRandomNumber(0, 24) + ":" + getRandomNumber(0, 60) + ":" + getRandomNumber(0, 60);
	}
	
	
	public long getRandomScheduledTimeByUnixTime()
	{
		GregorianCalendar gc = new GregorianCalendar();
		int year = getRandomNumber(2014, 2016);
		gc.set(gc.YEAR, year);
		int dayOfYear = getRandomNumber(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
		gc.set(gc.DAY_OF_YEAR, dayOfYear);
	    String fullString= gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH) + " " + getRandomNumber(0, 24) + ":" + getRandomNumber(0, 60) + ":" + getRandomNumber(0, 60);
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    Date date = null;
	    try
	    {
	    	 date = dateFormat.parse(fullString );
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    }
	    
	    return  (long) date.getTime()/1000;
	}
	
	public String trimZerosOnTime(String time)
	{
        if (time == null || time.equals(""))
            return new String("");
        
        String trimmed = "";
        StringTokenizer tt = new StringTokenizer(time, ":");
       
        trimmed += tt.nextToken();
        trimmed += ":";
        trimmed +=tt.nextToken();
       
        return trimmed;
	}
	
	public String trimZerosOnDate(String date)
	{
        if (date == null || date.equals(""))
            return new String("");
        
        date = date.replace(" 00:00:00", "");
       
        return date;
	}
	
    public String convert24HrToAMPMFormat(String time)
    {
        if (time == null || time.equals(""))
            return new String("");

        String converted = "";
        boolean isAM = false;
        StringTokenizer tt = new StringTokenizer(time, ":");
        
        int hr = Integer.parseInt( tt.nextToken());
        int min = Integer.parseInt( tt.nextToken());
        
        if(hr > 12)
        {
        	isAM = false;
        	hr = hr - 12;
        }
        else if(hr >= 0 && hr < 12)
        {
        	isAM = true;
        }
        else if(hr == 12)
        {
        	isAM = false;
        }
        
        String hrConverted = "";
        String minConverted = "";
    	if(hr >= 0 && hr < 10)
    		hrConverted = "0" + hr;
    	else
    		hrConverted = String.valueOf(hr);
    	
    	if(min >= 0 && min < 10)
    		minConverted = "0" + min; 
    	else
    		minConverted = String.valueOf(min);
    	
        if(isAM)
        {
        	converted = String.format("%s:%s %s",hrConverted, minConverted, "AM" );
        }
        else //pm
        {
        	converted = String.format("%s:%s %s",hrConverted, minConverted, "PM" );
        }
           
        return converted;
    }
	
	public long convertDatetimeStringToUnixTime(String dateTimeString)
	{
		if(dateTimeString.equals("")||dateTimeString.equals("None"))
		{
			return 0;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try
		{
			date = dateFormat.parse(dateTimeString );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return  (long) date.getTime() /1000;
	}
	
	public long convertDateStringToUnixTime(String dateTimeString)
	{
		if(dateTimeString.equals("")||dateTimeString.equals("None"))
		{
			return 0;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		try
		{
			date = dateFormat.parse(dateTimeString );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return  (long) date.getTime()/1000;
	}
	
	/*
	 *   This method changes the date format from MM/dd/yyyy to yyyy-MM-dd
	 * 
	 * 
	 */

	
	public String convertDateStringFromDatabaseToApplication(String dateString)
	{
		if(dateString.equals("")|| dateString.equals("None"))
		{
			return "";
		}
		
		DateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try
		{
			date = dateFormat.parse(dateString );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return newFormat.format(date);
	}
	


	private String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	

	public String createSimpleMessage(String regToken, String message)
	{
		JsonObject messageObj = new JsonObject();
		messageObj.addProperty("message", message);
		messageObj.addProperty("action", "simple_message");
		JsonObject obj = new JsonObject();
		obj.addProperty("to", regToken);
		obj.add("data", messageObj);
		
		return obj.toString();
	}
	
	public String createReview(String regToken, Review review)
	{
		Gson gson = new Gson();
		gson.toJson(review);
		
		JsonObject messageObj = new JsonObject();
		messageObj.addProperty("message", gson.toJson(review));
		messageObj.addProperty("action", "write_review");
		
		JsonObject obj = new JsonObject();
		obj.addProperty("to", regToken);
		obj.add("data", messageObj);
		
		return obj.toString();
	}
	
	public final JsonArray toJSON(ResultSet rs) throws JSONException, SQLException 
	{	
	      ResultSetMetaData rsmd = null;
	      String columnName = null;
	      String columnValue = null;
	      JsonArray jArray = new JsonArray();

	      try {
	              rsmd = rs.getMetaData();
	          
	              while (rs.next()) {

	            	  JsonObject joa = new JsonObject();
	            	  int test = rsmd.getColumnCount();
	                      for (int i = 0; i < test; i++) {
	                              columnName = rsmd.getColumnName(i+1);
	                              columnValue = rs.getString(columnName);
	                              joa.addProperty(columnName, columnValue);
	                      }
	                      jArray.add(joa);
	              }
	      } catch (SQLException e) {
	    	  jArray = null;
	      }
	    
	      
	      return jArray;
	}
	

	

}
