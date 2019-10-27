package com.oracle.hpcm.webservices.common;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.UserObject;

public class GetApplicationsConsumer {

	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("GetApplicationConsumer");
	public GetApplicationsConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications";
		System.out.println(user + " "+ password);
	}

	public String[] getApplicationsNames() throws IOException, SAXException,
			ParseException {
		this.response = JAXRestClient.callGetService(url, user, password, null,
				"application/json");
		logger.log(Level.INFO,"url :"+this.url);
		logger.log(Level.INFO,"response :"+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		ItemsObject items =  result.getItemsObject();
		if(!items.isResult())
			return null;
		logger.info(items.getText());
		JSONParser parser = new JSONParser();
		JSONArray appArray = (JSONArray) parser.parse(items.getText());
		String[] appNameArray = new String[appArray.size()];
		for (int i = 0; i < appArray.size(); i++) {
			appNameArray[i] = (String) ((JSONObject) appArray.get(i))
					.get("name");
		}
		return appNameArray;

	}

	public boolean doesApplicationExists(String appName) throws IOException,
			SAXException, ParseException {
		String[] appNameArray = getApplicationsNames();
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
		System.setProperty("user", "usinternalops65151.mohnish");
		System.setProperty("password", "Welc0me!");
		System.setProperty("server", "pcmcs-usinternalops65151.stg-epm.us1.oraclecloud.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		GetApplicationsConsumer getAppWSObj = new GetApplicationsConsumer();
		try {
			String[] appNames = getAppWSObj.getApplicationsNames();
			for (String s : appNames)
				System.out.println(s);
			if (getAppWSObj.doesApplicationExists("SP"))
				System.out.println("Application exists.");
			else
				System.out.println("Application does not exist.");
		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}

	}
}