package com.oracle.hpcm.webservices.dp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ResultObject;

public class GetDriverDefinitionConsumer {

	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("getAssgRule");
	public GetDriverDefinitionConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public ItemsObject getDrvDef(String appName) throws ParseException {
		String driverDefURL = url +appName+ "/getDriverDefinitions";

		this.response = JAXRestClient.callGetService(driverDefURL, user, password, null,
				"application/json");
		logger.log(Level.INFO,"Drv Def URL:"+driverDefURL);
		logger.log(Level.INFO,"response :"+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getItemsObject();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// http://slc04ljy.us.oracle.com:19000/profitability/rest/"+System.getProperty("version")+"/applications/MLBks3/jobs/ledgerDeployCubeJob?isKeepData=true&isReplaceCube=true&isRunNow=true&comment=Test

		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		GetDriverDefinitionConsumer getAppWSObj = new GetDriverDefinitionConsumer();
		try {
			ResultObject result = getAppWSObj.getDrvDef("BksDP30");
			
			System.out.println("Status: "+ result.isResult());
			System.out.println("Message: "+ result.getText());
		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}

	}
}