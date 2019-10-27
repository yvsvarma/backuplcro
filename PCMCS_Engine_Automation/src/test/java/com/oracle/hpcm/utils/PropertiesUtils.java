package com.oracle.hpcm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;

public class PropertiesUtils {
	public static Logger logger = Logger.getLogger("PropertiesUtils");
	public static int getTimeout(){
		if(System.getProperty("timeout")==null){
			return 300;
		}else{
			int nTimeout = Integer.parseInt(System.getProperty("timeout"));
			return nTimeout;
		}
	}
	public static void processGlobalProperties(Properties systemPoroperties) {
		boolean doesGlobalPropertyFileExists = false;
		Properties globalProp = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("./conf/global.properties");
			globalProp.load(input);
			doesGlobalPropertyFileExists = true;

		} catch (IOException ex) {
			doesGlobalPropertyFileExists = false;
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Read properties from command line

		String[] requiredPropertiesNamesList = { "server", "port","user", "password",
				"version","epmapiversion","staging","timeout","model_name","application_name"};

		for (String propertyName : requiredPropertiesNamesList) {
			String propValueFromCommandLine = systemPoroperties.getProperty(propertyName);

			if (propValueFromCommandLine != null) {
				logger.info(propertyName+"="+propValueFromCommandLine);
				System.setProperty(propertyName, propValueFromCommandLine);
			} else {
				if (propValueFromCommandLine == null
						&& doesGlobalPropertyFileExists
						&& globalProp.getProperty(propertyName) != null) {
					logger.log(
							Level.INFO,
							"Required "
									+ propertyName
									+ " is not provided as an argument. Defaulting to global.properties value. ");
					System.setProperty(propertyName,
							globalProp.getProperty(propertyName));
				} else {
					if(propertyName.equals("staging"))
					{
						System.setProperty("staging", "false");
						break;
					}
					if(propertyName.equals("timeout"))
					{
						System.setProperty("timeout", "300");
						break;
					}
					logger.log(

							Level.INFO,
							"Required "
									+ propertyName
									+ " is neither provided as an argument nor is present in global.properties value. Exiting.");
					System.exit(1);
				}
			}

		}
		String staging = System.getProperty("staging");
		if(staging != null){
			if(staging.equalsIgnoreCase("false")){
				String propValueFromCommandLine=systemPoroperties.getProperty("port");
				if (propValueFromCommandLine == null)
						if (propValueFromCommandLine == null
						&& doesGlobalPropertyFileExists
						&& globalProp.getProperty("port") != null) 		{logger.log(
								Level.INFO,
								"Required port is not provided as an argument. Defaulting to global.properties value. ");
						System.setProperty("port",
								globalProp.getProperty("port"));}
						else{
							logger.log(
									Level.INFO,
									"Required port is neither provided as an argument nor is present in global.properties value. Exiting.");
							System.exit(1);
						}
			}
			else{
				String propValueFromCommandLine=systemPoroperties.getProperty("domain");
				if (propValueFromCommandLine == null)
				if (propValueFromCommandLine == null
						&& doesGlobalPropertyFileExists
						&& globalProp.getProperty("domain") != null) {					
							logger.log(
								Level.INFO,
								"Required domain is not provided as an argument. Defaulting to global.properties value. ");
								System.setProperty("domain",globalProp.getProperty("domain"));
						}
						else{
							logger.log(
									Level.INFO,
									"Required domain is neither provided as an argument nor is present in global.properties value. Exiting.");
							System.exit(1);
						}
			}
		}else{
			//Staging will set to false
			System.setProperty("staging", "false");
			String propValueFromCommandLine=systemPoroperties.getProperty("port");
			if (propValueFromCommandLine == null
					&& doesGlobalPropertyFileExists
					&& globalProp.getProperty("port") != null) 		{logger.log(
							Level.INFO,
							"Required port is not provided as an argument. Defaulting to global.properties value. ");
					System.setProperty("port",
							globalProp.getProperty("port"));}
					else{
						logger.log(
								Level.INFO,
								"Required port is neither provided as an argument nor is present in global.properties value. Exiting.");
						System.exit(1);
					}
		}
	}
	public static String getWebServiceURL(){
		
		String staging = System.getProperty("staging");
		String url;
		if(staging.equalsIgnoreCase("false")){
			url = String.format("http://%s:%s/epm/rest/%s", System.getProperty("server"),System.getProperty("port"),System.getProperty("version"));
		}else{
			url = String.format("https://%s/epm/rest/%s", System.getProperty("server"),System.getProperty("version"));
		}
		return url;
	}
	public static String getInteropWebServiceURL(){
		
		String staging = System.getProperty("staging");
		String url;
		if(staging.equalsIgnoreCase("false")){
			url = String.format("http://%s:%s/interop/rest/%s", System.getProperty("server"),System.getProperty("port"),System.getProperty("epmapiversion"));
		}else{
			url = String.format("https://%s/interop/rest/%s", System.getProperty("server"),System.getProperty("epmapiversion"));
		}
		return url;
	}
	public static UserObject getUserObject(){
		String staging = System.getProperty("staging");
		UserObject user;
		if(staging.equalsIgnoreCase("false")){
			user = new UserObject(System.getProperty("user"),System.getProperty("password"),System.getProperty("domain"),false);
		}else{
			user = new UserObject(System.getProperty("user"),System.getProperty("password"),System.getProperty("domain"),true);
		}
		return user;
	}
	public static void processModelProperties(Properties prop) {

		System.setProperty("model.dir", "./models/");
		GregorianCalendar cal = new GregorianCalendar();
		String timestamp = "" + cal.get(Calendar.DAY_OF_MONTH)
				+ cal.get(Calendar.MONTH)
				+ cal.get(Calendar.HOUR)
				+ cal.get(Calendar.MINUTE);
		System.setProperty("logging.file", "./logs/wslogs.log");
		System.setProperty("testreport.zip", "./logs/testreport"+timestamp+".zip");
		System.setProperty("concise.report",
				"./test-output/emailable-report.html");
		System.setProperty("model.prop.file", System.getProperty("model.dir")
				+ "model.properties");

		// check if model exits in models folder

		if (!(new File(System.getProperty("model.dir")).exists())) {
			logger.log(Level.INFO, "Models folder does not exist. Exiting.");
			System.exit(1);
		}

		// Setup model related properties

		boolean doesModelPropFileExists = false;
		// read from prop file

		Properties modelProp = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(System.getProperty("model.dir")
					+ "model.properties");
			modelProp.load(input);
			doesModelPropFileExists = true;

		} catch (IOException ex) {
			doesModelPropFileExists = false;
			// ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Set timeout properties

	}

}
