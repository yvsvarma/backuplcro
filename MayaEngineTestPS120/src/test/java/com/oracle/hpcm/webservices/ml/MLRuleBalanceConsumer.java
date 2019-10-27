package com.oracle.hpcm.webservices.ml;

import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;

public class MLRuleBalanceConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("");
	public MLRuleBalanceConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public ItemsObject getRuleBalancing(String appName, String pov,
			String modelViewName) throws ParseException {
		HashMap<String, String> hashMap = null;
		if (modelViewName != null) {
			hashMap = new HashMap<String, String>();
			hashMap.put("modelViewName", modelViewName);
		}
		logger.info("RuleBalancingUrl:"+ url + appName + "/povs/"
				+ pov + "/ruleBalance");
		this.response = JAXRestClient.callGetService(url + appName + "/povs/"
				+ pov + "/ruleBalance", user, password, hashMap, "*/*");
		logger.info("Response: "+ response);
		if (response != null) {
			JSONResultParser result = new JSONResultParser(this.response);
			return result.getItemsObject();
		}
		return null;
	}

	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		MLRuleBalanceConsumer WSObj = new MLRuleBalanceConsumer();
		try {
			System.out
					.println("status: "
							+ WSObj.getRuleBalancing("dLMA",
									"2014_January_Actual", ""));

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}
}
