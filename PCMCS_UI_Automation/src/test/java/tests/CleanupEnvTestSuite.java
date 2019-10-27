package tests;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.oracle.hpcm.restclient.PCMCSRestClient;
import com.oracle.hpcm.utils.UserObject;

import utils.PropertiesUtils;

public class CleanupEnvTestSuite{
	public static String appName = "BksML30";
	public static PCMCSRestClient restClient;
	public static Logger logger = Logger.getLogger("");
	@Test
	public void setupModelTrhoughImport() throws Exception{
		logger.log(Level.INFO, "***********Test Environment Setup***********");
		Properties prop = System.getProperties();
		PropertiesUtils.processGlobalProperties(prop);
		UserObject currentUserCredentials = new UserObject(System.getProperty("user"),System.getProperty("password"),System.getProperty("server")
				,System.getProperty("port"),System.getProperty("epmapiversion"),System.getProperty("version"),System.getProperty("domain"),new Boolean(System.getProperty("staging")).booleanValue(),Integer.parseInt(System.getProperty("timeout"),10)); 
		restClient = new PCMCSRestClient(currentUserCredentials);
                restClient.cleanEnviornment();
	}
}
