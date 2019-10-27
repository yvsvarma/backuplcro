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
public class DeleteApplicationConsumer {
	private String url;
	private String response;
	public static Logger logger = Logger.getLogger("deleteApplication");
	public UserObject userCredentials ;
	public DeleteApplicationConsumer(UserObject userObject) {
		userCredentials = userObject;
		url = userCredentials.getWebServiceURL()+"/applications/";
	}

	public ResultObject deleteApp(String appName) throws ParseException {
		logger.info("Delete Url :" + url + appName);
		this.response = JAXRestClient.callDeleteService(userCredentials,url + appName
				, null);
		logger.info("Response :" + this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

}
