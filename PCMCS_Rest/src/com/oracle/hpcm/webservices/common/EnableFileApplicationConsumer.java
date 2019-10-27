package com.oracle.hpcm.webservices.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class EnableFileApplicationConsumer {

	private String url;
	private UserObject uo;
	private String response;
	public static Logger logger = Logger.getLogger("GetPOV");
	public EnableFileApplicationConsumer(UserObject uo) {
		this.uo = uo;
		url = uo.getWebServiceURL()+"/fileApplications/";

	}

	public ResultObject enableFileApplication(String appName) throws ParseException {
		logger.log(Level.INFO,"Url : "  +url + appName
				+ "/enableApplication");
		this.response = JAXRestClient.callPostService(uo,url + appName
				+ "/enableApplication", null);
		logger.log(Level.INFO,"Response : "  + this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

}