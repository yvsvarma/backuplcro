package com.oracle.hpcm.webservices.common;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.EPMAutomateUtility;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class EssbaseLoadDataConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("essbaseLoad");
	public EssbaseLoadDataConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications/";
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
		this.response = JAXRestClient.callPostService(appURL, user,
				password,hashMap);
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
		System.setProperty("user", "epm_default_cloud_admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc01lxw.us.oracle.com");
		System.setProperty("port", "9000");
		System.setProperty("deleteport", "9000");
		System.setProperty("model.dir", "./models/");
		System.setProperty("version", "v1");
		System.setProperty("apiversion", "11.1.2.3.600");
		EssbaseLoadDataConsumer wsObj = new EssbaseLoadDataConsumer();
		try {
			EPMAutomateUtility.uploadFileOverwrite("./models/ML/", "inpdata.txt","inbox/");
			ResultObject result = wsObj.loadFile("BksML12", new File("./models/ML/inpdata.txt"), true, "OVERWRITE_EXISTING_VALUES", "inpdata.txt", "");
			System.out.println(result.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
