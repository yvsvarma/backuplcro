package com.oracle.hpcm.webservices.ml;

import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class MLDeployCubeConsumer {
	private String url;
	UserObject uo;
	private String response;
	public static Logger logger = Logger.getLogger("deployCube");
	public MLDeployCubeConsumer(UserObject uo) {
		this.uo= uo;
		url = uo.getWebServiceURL()+"/applications/";
	}

	public ResultObject runDeployCubeJob(String appName, boolean isKeepData,
			boolean isReplaceCube, boolean isRunNow, String comment) throws ParseException {
		logger.entering(this.getClass().getSimpleName(), "runDeployCube");
		String deployURL = url + appName
				+ "/jobs/ledgerDeployCubeJob";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("isKeepData", "" + isKeepData);
		hashMap.put("isReplaceCube", "" + isReplaceCube);
		hashMap.put("isRunNow", "" + isRunNow);
		hashMap.put("comment", comment);
		logger.info("Request: "+deployURL);
		this.response = JAXRestClient.callPostService(uo, deployURL, hashMap);
		logger.info("Response: "+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	

}
