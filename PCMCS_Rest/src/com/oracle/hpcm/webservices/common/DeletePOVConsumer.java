package com.oracle.hpcm.webservices.common;


import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;


/*http://{HostName}/profitability/rest/"+System.getProperty("version")+"/applications/{applicationName}/povs
 * 
 */
public class DeletePOVConsumer {
	private String url;
	private String response;
	private UserObject userCredentials; 
	public static Logger logger = Logger.getLogger("");
	public DeletePOVConsumer(UserObject uo) {
		this.userCredentials = uo;
		this.url = uo.getWebServiceURL()+"/applications/";
	}

	public ResultObject deletePOV(String appName,String POV) throws ParseException {
		logger.info("URL :" + url + appName
				+ "/povs/"+POV);
		this.response = JAXRestClient.callDeleteService(userCredentials,url + appName
				+ "/povs/" + POV,null);
		logger.info("Resposne : " + response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}
}
