package com.oracle.hpcm.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.oracle.hpcm.utils.UserObject;

public class PropertiesUtils {
	public static Logger logger = Logger.getLogger("PropertiesUtils");
	public static int getTimeout(){
		if(System.getProperty("timeout")==null){
			return 300;
		}else{
			int nTimeout = Integer.parseInt(System.getProperty("timeout"));
			if(nTimeout<100)
				return 100;
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

		String[] requiredPropertiesNamesList = { "server_c", "user_c", "password_c",
				"version_c","epmapiversion_c","staging_c","timeout_c","isMigration","path_to_epmautomate"};

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
					if(propertyName.equals("staging_c"))
					{
						System.setProperty("staging_c", "false");
						break;
					}
					if(propertyName.equals("timeout_c"))
					{
						System.setProperty("timeout_c", "300");
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
		String staging = System.getProperty("staging_c");
		if(staging != null){
			if(staging.equalsIgnoreCase("false")){
				String propValueFromCommandLine=systemPoroperties.getProperty("port_c");
				if (propValueFromCommandLine == null)
						if (propValueFromCommandLine == null
						&& doesGlobalPropertyFileExists
						&& globalProp.getProperty("port_c") != null) 		{logger.log(
								Level.INFO,
								"Required port_c is not provided as an argument. Defaulting to global.properties value. ");
						System.setProperty("port_c",
								globalProp.getProperty("port_c"));}
						else{
							logger.log(
									Level.INFO,
									"Required port_c is neither provided as an argument nor is present in global.properties value. Exiting.");
							System.exit(1);
						}
			}
			else{
				String propValueFromCommandLine=systemPoroperties.getProperty("domain_c");
				if (propValueFromCommandLine == null)
				if (propValueFromCommandLine == null
						&& doesGlobalPropertyFileExists
						&& globalProp.getProperty("domain_c") != null) {					
							logger.log(
								Level.INFO,
								"Required domain_c is not provided as an argument. Defaulting to global.properties value. ");
								System.setProperty("domain_c",globalProp.getProperty("domain_c"));
						}
						else{
							logger.log(
									Level.INFO,
									"Required domain_c is neither provided as an argument nor is present in global.properties value. Exiting.");
							System.exit(1);
						}
			}
		}else{
			//Staging will set to false
			System.setProperty("staging_c", "false");
			String propValueFromCommandLine=systemPoroperties.getProperty("port_c");
			if (propValueFromCommandLine == null
					&& doesGlobalPropertyFileExists
					&& globalProp.getProperty("port_c") != null) 		{logger.log(
							Level.INFO,
							"Required port_c is not provided as an argument. Defaulting to global.properties value. ");
					System.setProperty("port_c",
							globalProp.getProperty("port_c"));}
					else{
						logger.log(
								Level.INFO,
								"Required port_c is neither provided as an argument nor is present in global.properties value. Exiting.");
						System.exit(1);
					}
		}
		if(System.getProperty("isMigration")!=null && System.getProperty("isMigration").toLowerCase().equals("false"))
			return;
		String[] requiredPropertiesNamesListPrevious = { "server_p", "user_p", "password_p",
				"version_p","epmapiversion_p","staging_p","timeout_p"};

		for (String propertyName : requiredPropertiesNamesListPrevious) {
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
					if(propertyName.equals("staging_p"))
					{
						System.setProperty("staging_p", "false");
						break;
					}
					if(propertyName.equals("timeout_p"))
					{
						System.setProperty("timeout_p", "300");
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
		staging = System.getProperty("staging_p");
		if(staging != null){
			if(staging.equalsIgnoreCase("false")){
				String propValueFromCommandLine=systemPoroperties.getProperty("port_p");
				if (propValueFromCommandLine == null)
						if (propValueFromCommandLine == null
						&& doesGlobalPropertyFileExists
						&& globalProp.getProperty("port_p") != null) 		{logger.log(
								Level.INFO,
								"Required port_p is not provided as an argument. Defaulting to global.properties value. ");
						System.setProperty("port_p",
								globalProp.getProperty("port_p"));}
						else{
							logger.log(
									Level.INFO,
									"Required port_p is neither provided as an argument nor is present in global.properties value. Exiting.");
							System.exit(1);
						}
			}
			else{
				String propValueFromCommandLine=systemPoroperties.getProperty("domain_p");
				if (propValueFromCommandLine == null)
				if (propValueFromCommandLine == null
						&& doesGlobalPropertyFileExists
						&& globalProp.getProperty("domain_p") != null) {					
							logger.log(
								Level.INFO,
								"Required domain_p is not provided as an argument. Defaulting to global.properties value. ");
								System.setProperty("domain_p",globalProp.getProperty("domain_p"));
						}
						else{
							logger.log(
									Level.INFO,
									"Required domain_p is neither provided as an argument nor is present in global.properties value. Exiting.");
							System.exit(1);
						}
			}
		}else{
			//Staging will set to false
			System.setProperty("staging_p", "false");
			String propValueFromCommandLine=systemPoroperties.getProperty("port_p");
			if (propValueFromCommandLine == null
					&& doesGlobalPropertyFileExists
					&& globalProp.getProperty("port_p") != null) 		{logger.log(
							Level.INFO,
							"Required port_p is not provided as an argument. Defaulting to global.properties value. ");
					System.setProperty("port_p",
							globalProp.getProperty("port_p"));}
					else{
						logger.log(
								Level.INFO,
								"Required port_p is neither provided as an argument nor is present in global.properties value. Exiting.");
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

		Properties modelProp = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(System.getProperty("model.dir")
					+ "model.properties");
			modelProp.load(input);

		} catch (IOException ex) {
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
