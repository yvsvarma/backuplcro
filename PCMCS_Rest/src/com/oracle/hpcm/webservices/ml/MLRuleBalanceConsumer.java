package com.oracle.hpcm.webservices.ml;

import java.util.HashMap;
import java.util.logging.Logger;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.UserObject;

public class MLRuleBalanceConsumer {
	private String url;
	private UserObject uo;
	private String response;
	public static Logger logger = Logger.getLogger("");
	public MLRuleBalanceConsumer(UserObject uo) {
		this.uo = uo;
		url = uo.getWebServiceURL()+"/applications/";
	}

	public String getRuleBalancing(String appName, String pov,
			String modelViewName) {
		HashMap<String, String> hashMap = null;
		if (modelViewName != null) {
			hashMap = new HashMap<String, String>();
			hashMap.put("modelViewName", modelViewName);
		}
		logger.info("RuleBalancingUrl:"+ url + appName + "/povs/"
				+ pov + "/ruleBalance");
		this.response = JAXRestClient.callGetService(this.uo,url + appName + "/povs/"
				+ pov + "/ruleBalance", hashMap, "*/*");
		logger.info("Response: "+ response);
		if (response != null) {
			return response;
		}
		return null;
	}
}
