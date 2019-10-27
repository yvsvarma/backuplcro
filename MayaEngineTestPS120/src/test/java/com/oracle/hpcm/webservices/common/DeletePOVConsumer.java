package com.oracle.hpcm.webservices.common;


import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ResultObject;


/*http://{HostName}/profitability/rest/"+System.getProperty("version")+"/applications/{applicationName}/povs
 * 
 */
public class DeletePOVConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("");
	public DeletePOVConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("deleteport")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public ResultObject deletePOV(String appName,String POV) throws ParseException {
		logger.info("URL :" + url + appName
				+ "/povs/");
		this.response = JAXRestClient.callDeleteService(url + appName
				+ "/povs/" + POV, user, password,null);
		logger.info("Resposne : " + response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("deleteport", "6756");
		System.setProperty("version", "v1");
		DeletePOVConsumer WSObj = new DeletePOVConsumer();
		try {
			ResultObject result = WSObj.deletePOV("ML","2014_January_Actual");
			System.out.println("\nstatus: "
					+ result.getText());


		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}
}
