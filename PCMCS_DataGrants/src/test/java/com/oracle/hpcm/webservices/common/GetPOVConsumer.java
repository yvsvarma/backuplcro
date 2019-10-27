package com.oracle.hpcm.webservices.common;


import java.util.Properties;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.PovDTO;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.UserObject;


/*http://{HostName}/profitability/rest/"+System.getProperty("version")+"/applications/{applicationName}/povs
 * 
 */
public class GetPOVConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("GetPOV");
	public GetPOVConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications/";
	}

	public ItemsObject getPOVString(String appName) throws ParseException {
		String povURL= url + appName
				+ "/povs";
		this.response = JAXRestClient.callGetService(povURL, user, password,null,"application/json");
		logger.info("Request URL: "+povURL);
		logger.info("Response: "+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getItemsObject();
	}

	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		Properties prop = System.getProperties();
		//System.out.println(System.getProperty("domain"));
		PropertiesUtils.processGlobalProperties(prop);
		PropertiesUtils.processModelProperties(prop);
		GetPOVConsumer WSObj = new GetPOVConsumer();
		try {
			ItemsObject items = WSObj.getPOVString("BksML30");
			System.out.println("status: "
					+ items.isResult());
			PovDTO[] povArray = PovDTO.getPOVSFromJSONText(items.getText());
			for(PovDTO pov : povArray){
				System.out.println(pov);
			}

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}
}
