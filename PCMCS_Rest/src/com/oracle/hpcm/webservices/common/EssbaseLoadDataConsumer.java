package com.oracle.hpcm.webservices.common;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class EssbaseLoadDataConsumer {
	private String url;
	private String response;
	private UserObject uo;
	public static Logger logger = Logger.getLogger("essbaseLoad");
	public EssbaseLoadDataConsumer(UserObject uo) {
		this.uo = uo;
		url = uo.getWebServiceURL()+"/applications/";
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
		this.response = JAXRestClient.callPostService(uo,appURL,hashMap);
		logger.info("Response: "+this.response);
		logger.exiting(this.getClass().getSimpleName(), "loadFile");
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}
}
