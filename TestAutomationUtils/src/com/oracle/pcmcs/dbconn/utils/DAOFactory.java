package com.oracle.pcmcs.dbconn.utils;

import java.util.Properties;

public abstract class DAOFactory {
	//Constants
	private static final String PROPERTY_URL="url";
	private static final String PROPERTY_USER="username";
	private static final String PROPERTY_PASSWORD="password";
	

	public static String getProperty(Properties propertyMap, String key) throws Exception{
		if(key == null)
			throw new Exception("Key is null.");
		if(propertyMap == null)
			throw new Exception("Property Map is null.");
			
		String property =  propertyMap.getProperty(key);
		if(property == null || property.trim().length() == 0){
			throw new Exception("Property for this key is missing.");
		}
		return property;
		
	}
}

