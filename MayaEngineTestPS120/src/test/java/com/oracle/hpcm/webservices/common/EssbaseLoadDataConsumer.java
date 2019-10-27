package com.oracle.hpcm.webservices.common;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;

public class EssbaseLoadDataConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("essbaseLoad");
	public EssbaseLoadDataConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public ResultObject loadFile(String appName, File file, boolean clearAllDataFlag , String dataLoadValue ,String dataFileName , String rulesFileName) throws ParseException {
		logger.entering(this.getClass().getSimpleName(), "loadFile");
		String appURL = url+appName+"/jobs/essbaseDataLoadJob";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("clearAllDataFlag", ""+clearAllDataFlag);
		hashMap.put("dataLoadValue", dataLoadValue);
		hashMap.put("dataFileName", dataFileName);
		hashMap.put("rulesFileName", rulesFileName);
		logger.info("Request: "+appURL);
		this.response = JAXRestClient.callFileAttachement(appURL, user,
				password, file, hashMap);
		logger.info("Response: "+this.response);
		logger.exiting(this.getClass().getSimpleName(), "loadFile");
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}
	public static void main(String[] args) throws ParseException {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc06vya.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("model.dir", "./models/");
		System.setProperty("version", "v1");
		EssbaseLoadDataConsumer wsObj = new EssbaseLoadDataConsumer();
		ResultObject result = wsObj.loadFile("ML", new File("./models/ML/inpdata.txt"), true, "OVERWRITE_EXISTING_VALUES", "data.txt", "");
		System.out.println(result.getText());
	}
}
