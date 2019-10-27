package tests;

import java.util.Properties;

import org.testng.annotations.Test;

import com.oracle.hpcm.restclient.PCMCSRestClient;
import com.oracle.hpcm.utils.UserObject;

import utils.PropertiesUtils;
import utils.Utilities;

public class SetupDimensionMgmtAutomation {
	public static PCMCSRestClient restClient;
	
	@Test(priority=1, enabled = true)
	public void deleteApplication() throws Exception{
		PropertiesUtils.processGlobalProperties(new Properties());
			UserObject currentUserCredentials = new UserObject(System.getProperty("user"),System.getProperty("password"),System.getProperty("server")
					,System.getProperty("port"),System.getProperty("epmapiversion"),System.getProperty("version"),System.getProperty("domain"),new Boolean(System.getProperty("staging")).booleanValue(),Integer.parseInt(System.getProperty("timeout"),10)); 
			restClient = new PCMCSRestClient(currentUserCredentials);
			String[] appNames = restClient.getApplications();
			for(String appName : appNames){
				restClient.deleteApplication(appName);
			}
		
	}

	@Test(priority=2,enabled = true)
	public void setupModelThroughImport() throws Exception{
		PropertiesUtils.processGlobalProperties(new Properties());
		UserObject currentUserCredentials = new UserObject(System.getProperty("user"),System.getProperty("password"),System.getProperty("server")
				,System.getProperty("port"),System.getProperty("epmapiversion"),System.getProperty("version"),System.getProperty("domain"),new Boolean(System.getProperty("staging")).booleanValue(),Integer.parseInt(System.getProperty("timeout"),10)); 
		restClient = new PCMCSRestClient(currentUserCredentials);
		restClient.uploadFileToService("./data/", "BksML30.zip", "profitinbox");
		restClient.importTemplate("BksML30", "BksML30.zip");
		Utilities.wait(300);
	}
}
