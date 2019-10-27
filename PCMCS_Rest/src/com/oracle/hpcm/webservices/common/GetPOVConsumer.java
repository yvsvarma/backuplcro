package com.oracle.hpcm.webservices.common;


import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.UserObject;


/*http://{HostName}/profitability/rest/"+System.getProperty("version")+"/applications/{applicationName}/povs
 * 
 */
public class GetPOVConsumer {
	private String url;
	private UserObject uo;
	private String response;
	public static Logger logger = Logger.getLogger("GetPOV");
	public GetPOVConsumer(UserObject uo) {
		this.uo =uo;
		url = uo.getWebServiceURL()+"/applications/";
	}

	public ItemsObject getPOVString(String appName) throws ParseException {
		String povURL= url + appName
				+ "/povs";
		this.response = JAXRestClient.callGetService(uo, povURL,null,"application/json");
		logger.info("Request URL: "+povURL);
		logger.info("Response: "+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getItemsObject();
	}

	
}
