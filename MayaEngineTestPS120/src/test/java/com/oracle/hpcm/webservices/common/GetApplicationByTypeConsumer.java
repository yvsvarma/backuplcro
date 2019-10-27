package com.oracle.hpcm.webservices.common;

import java.util.HashMap;
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

public class GetApplicationByTypeConsumer {
	public static final String MANAGEMENT_LEDGER = "MANAGEMENT_LEDGER";
	public static final String STANDARD_PROFITABILITY = "GENERAL";
	public static final String DETAILED_PROFITABILITY = "DETAIL";
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("GetApplicationByTypeConsumer");

	public GetApplicationByTypeConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://"
				+ System.getProperty("server")
				+ ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/applicationsByType";
	}

	private boolean validateAppType(String appType) {
		if (appType.equals(GetApplicationByTypeConsumer.MANAGEMENT_LEDGER)
				|| appType
						.equals(GetApplicationByTypeConsumer.DETAILED_PROFITABILITY)
				|| appType
						.equals(GetApplicationByTypeConsumer.STANDARD_PROFITABILITY))
			return true;
		throw new IllegalArgumentException("Application type " + appType
				+ " is not expected. please use standard app types.");
	}

	public String[] getApplicationsNames(String appType) throws ParseException {
		validateAppType(appType);
		HashMap<String,String> queryMap = new HashMap<String, String>();
		queryMap.put("queryParameter", "{\"applicationType\":\""+appType+"\"}");
		this.response = JAXRestClient.callGetService(url , user,
				password, queryMap, "*/*");
		logger.log(Level.INFO, "URL : "+url + appType);
		logger.log(Level.INFO, "Response: "+this.response);
		String[] appNameArray = null;
		if (this.response != null && !this.response.endsWith("null")) {
			
			JSONResultParser result = new JSONResultParser(this.response);
			ItemsObject items = result.getItemsObject();
			JSONParser parser = new JSONParser();
			if(items.getText().contains("[")){
				JSONArray appArray = (JSONArray) parser.parse(items.getText());
				appNameArray = new String[appArray.size()];
				for (int i = 0; i < appArray.size(); i++) {
					appNameArray[i] = (String) ((JSONObject) appArray.get(i))
							.get("name");
				}
			}else{
				appNameArray = new String[1];
				JSONObject jObj = (JSONObject) parser.parse(items.getText());
				appNameArray[0]=((String)jObj.get("name"));
			}
			
			return appNameArray;

		}
		return null;
	}

	public boolean doesApplicationExistsOfThisAppType(String appName,
		String appType) throws ParseException {
		String[] appNameArray = getApplicationsNames(appType);
		if(appNameArray == null)
			{
				logger.log(Level.FINEST, "appNameArray is null. ");
				return false;
			}
		logger.log(Level.FINEST, "appNameArray is not null. "+appNameArray);
		for (String s : appNameArray) {
			if (s.equals(appName))
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
		GetApplicationByTypeConsumer getAppWSObj = new GetApplicationByTypeConsumer();
		try {
			// String[] appNames =
			// getAppWSObj.getApplicationsNames(GetApplicationByTypeConsumer.MANAGEMENT_LEDGER);
			// for (String s: appNames)
			// System.out.println(s);
			if (getAppWSObj.doesApplicationExistsOfThisAppType("ML",
					GetApplicationByTypeConsumer.MANAGEMENT_LEDGER))
				System.out.println("Application exists.");
			else
				System.out.println("Application does not exist.");

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}

	}
}