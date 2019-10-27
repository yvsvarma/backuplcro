package com.oracle.hpcm.webservices.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class ProcessFileApplicationUpdateDimensionConsumer {

	private String url;
	UserObject uo;
	private String response;
	private static Logger logger = Logger.getLogger("uploadFileDim");
	public ProcessFileApplicationUpdateDimensionConsumer(UserObject uo) {
		this.uo = uo;

		url = uo.getWebServiceURL()+"/fileApplications/";
	}


	public ResultObject uploadFile(String appName, String filename) throws IOException, ParseException {
		String appURL = url + appName + "/updateDimension";
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("dataFileName", filename);
		logger.info("URL: "+ appURL);
		this.response = JAXRestClient.callPostService(this.uo,appURL, params);
		logger.info("Response: "+ response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}


}