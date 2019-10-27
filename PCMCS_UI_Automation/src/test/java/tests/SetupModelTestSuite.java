package tests;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.oracle.hpcm.restclient.PCMCSRestClient;
import com.oracle.hpcm.utils.UserObject;

import utils.PropertiesUtils;

public class SetupModelTestSuite{
	public static String appName = "BksML30";
	public static PCMCSRestClient restClient;
	public static Logger logger = Logger.getLogger("");
	@Test
	public void setupModelTrhoughImport() throws Exception{
		logger.log(Level.INFO, "***********Test Environment Setup***********");
		//logger.log(Level.INFO,"Model name: "+);
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		Properties prop = System.getProperties();
		//System.out.println(System.getProperty("domain"));
		PropertiesUtils.processGlobalProperties(prop);
		//PropertiesUtils.processModelProperties(prop);
		
		UserObject currentUserCredentials = new UserObject(System.getProperty("user"),System.getProperty("password"),System.getProperty("server")
				,System.getProperty("port"),System.getProperty("epmapiversion"),System.getProperty("version"),System.getProperty("domain"),new Boolean(System.getProperty("staging")).booleanValue(),Integer.parseInt(System.getProperty("timeout"),10)); 
		restClient = new PCMCSRestClient(currentUserCredentials);
		restClient.uploadFileToService("./data/", "BksML30.zip", "profitinbox");
		restClient.importTemplate("BksML30", "BksML30.zip");
	}
}
