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
import com.oracle.hpcm.utils.UserObject;

public class GetApplicationsConsumer {

	private String url;
	private UserObject uo;
	private String response;
	public static Logger logger = Logger.getLogger("GetApplicationConsumer");
	public GetApplicationsConsumer(UserObject userCred) {
		this.uo= userCred;
		url = uo.getWebServiceURL()+"/applications";
		//System.out.println(user + " "+ password);
	}

	public String[] getApplicationsNames() throws IOException, SAXException,
			ParseException {
		this.response = JAXRestClient.callGetService(this.uo,url,  null,
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
}