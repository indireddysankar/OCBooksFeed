package com.bibliofreaks.utils;

import java.io.FileInputStream;

import java.util.Properties;

import org.apache.log4j.Logger;

public class LoadProperties {
	
	/**
	 * @param args
	 * @author siva.reddy
	 */
	
	private static final Logger basicLogger = Logger.getLogger (LoadProperties.class.getName()); 
	
	static Properties properties = new Properties();
	
	public static void load(){
		try{
		  basicLogger.debug("Properties file to be loaded : config.properties");
		  FileInputStream in = new FileInputStream("resources/config.properties");
		  basicLogger.debug("Starting loading properties file");  
		  properties.load(in);
		  in.close();
		}
		catch(Exception ex){
			basicLogger.error("Caught error in LoadProperties.class load meathod : \n"+ex.toString());
		}
		
	}
	
	
	public static String Prop(String PropName) {
		
		 //load properties file
		basicLogger.debug("Entered LoadProperties.class with Property Name : "+PropName);
		basicLogger.debug("Property Value : "+properties.getProperty(PropName)+"\n Returning from LoadProperties.class");
		 return properties.getProperty(PropName);
		
	}
	

}
