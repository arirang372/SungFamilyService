package com.sung.family.constants;

import java.util.Properties;

import com.sung.family.utils.Utils;

public class AppMetadata 
{
	public static String HCC_SUPPORT_APP_NAME = "com.caringondemand.hccsupport";
	
	public static final String DEFAULT_DATE_TIME="1970-01-01 00:00:00";
	public static final String DEFAULT_DATE="1970-01-01";
	public static final String GCM_SERVER_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String ALPHA_NUMERIC ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String OTC_DRUG_TYPE = "OTC";
    public static final String AUTHORIZATION_HEADER = "AUTHORIZATION";
    
	private static final String FORUM_DB_PRODUCTION = "FORUM_DB_PRODUCTION";
	private static final String FORUM_DB_DEV = "FORUM_DB_DEV";
	public static final String FORUM_DB = FORUM_DB_DEV;
    
	public static String getAPIKey()
	{
		return getProperty("GCM_APP_API_KEY");
	}

	public static String getProperty(String key){
		String Result = "";
		Utils proU = Utils.getInstance();
		Properties prop = proU.getProperties(Utils.applicationProperties);
		Result = prop.getProperty(key);
		return Result;
	}
}
