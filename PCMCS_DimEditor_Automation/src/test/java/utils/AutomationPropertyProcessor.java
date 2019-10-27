/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbanchho
 */
public class AutomationPropertyProcessor {

    public static Logger logger = Logger.getLogger("PropertiesUtils");

    public static int getTimeout() {
        if (System.getProperty("timeout") == null) {
            return 300;
        } else {
            int nTimeout = Integer.parseInt(System.getProperty("timeout"));
            if (nTimeout < 100) {
                return 100;
            }
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
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        // Read properties from command line

        String[] requiredPropertiesNamesList = {"browser", "server", "port", "user", "password",
            "version", "epmapiversion", "timeout", "hub", "staging","db_server","db_port","db_user","db_password","db_sid","essbase_port"};

        for (String propertyName : requiredPropertiesNamesList) {
            String propValueFromCommandLine = systemPoroperties.getProperty(propertyName);

            if (propValueFromCommandLine != null) {
                logger.log(Level.INFO, "{0}={1}", new Object[]{propertyName, propValueFromCommandLine});
                System.setProperty(propertyName, propValueFromCommandLine);
            } else {
                if (propValueFromCommandLine == null
                        && doesGlobalPropertyFileExists
                        && globalProp.getProperty(propertyName) != null) {
                    logger.log(Level.INFO, "Required {0} is not provided as an argument. Defaulting to global.properties value which is {1}.", new Object[]{propertyName, globalProp.getProperty(propertyName)});
                    System.setProperty(propertyName,
                            globalProp.getProperty(propertyName));
                } else {
                    if (propertyName.equals("staging")) {
                        System.setProperty("staging", "false");
                        
                    }
                    if (propertyName.equals("timeout")) {
                        System.setProperty("timeout", "300");
                        
                    }
                    logger.log(Level.INFO, "Required {0} is neither provided as an argument nor is present in global.properties value. Exiting.", propertyName);
                    System.exit(1);
                }
            }

        }
    }

    public static String getWebServiceURL() {

        String staging = System.getProperty("staging");
        String url;
        if (staging.equalsIgnoreCase("false")) {
            url = String.format("http://%s:%s/epm/rest/%s", System.getProperty("server"), System.getProperty("port"), System.getProperty("version"));
        } else {
            url = String.format("https://%s/epm/rest/%s", System.getProperty("server"), System.getProperty("version"));
        }
        return url;
    }

    public static String getInteropWebServiceURL() {

        String staging = System.getProperty("staging");
        String url;
        if (staging.equalsIgnoreCase("false")) {
            url = String.format("http://%s:%s/interop/rest/%s", System.getProperty("server"), System.getProperty("port"), System.getProperty("epmapiversion"));
        } else {
            url = String.format("https://%s/interop/rest/%s", System.getProperty("server"), System.getProperty("epmapiversion"));
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
        System.setProperty("testreport.zip", "./logs/testreport" + timestamp + ".zip");
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

    public static String getProfitURL() {
        String staging = System.getProperty("staging");
        String url;
        if (staging.equalsIgnoreCase("false")) {
            url = String.format("http://%s:%s/epm", System.getProperty("server"), System.getProperty("port"));
        } else {
            url = String.format("https://%s/epm", System.getProperty("server"));
        }
        return url;
    }

}
