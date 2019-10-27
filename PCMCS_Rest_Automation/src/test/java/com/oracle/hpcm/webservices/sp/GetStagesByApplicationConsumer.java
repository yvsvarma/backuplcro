package com.oracle.hpcm.webservices.sp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;

public class GetStagesByApplicationConsumer {

	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("GetApplicationConsumer");
	public GetStagesByApplicationConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public String[] getStages(String appName) throws ParseException {
		String stageUrl = url +appName+ "/stages";
		this.response = JAXRestClient.callGetService(stageUrl, user, password, null,
				"application/json");
		logger.log(Level.INFO,"url :"+stageUrl);
		logger.log(Level.INFO,"response :"+this.response);
		if (this.response != null) {

			JSONParser parser = new JSONParser();
			JSONResultParser result = new JSONResultParser(this.response);
			ItemsObject stages =  result.getItemsObject(); 
			JSONArray appArray = (JSONArray) parser.parse(stages.getText());
			String[] appNameArray = new String[appArray.size()];
			for (int i = 0; i < appArray.size(); i++) {
				appNameArray[i] = (String) ((JSONObject) appArray.get(i))
						.get("stageName");
			}
			return appNameArray;
			
		}
		return null;
	}
	
	public boolean doesThisStageExists(String appName, String stageName) throws ParseException{
		String[] appNames = getStages(appName);
		for(String s : appNames){
			if(s.equals(stageName))
				return true;
		}
		return false;
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
		GetStagesByApplicationConsumer getAppWSObj = new GetStagesByApplicationConsumer();
		try {
			String[] appNames = getAppWSObj.getStages("BksSP82");
			for(String s : appNames)
				logger.info(s);
		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}

	}
}