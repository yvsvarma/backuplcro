package com.oracle.hpcm.webservices.ml;

import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.UserObject;

/**
 * @author mbanchho
 *
 */
public class MLRuleBalanceConsumer {
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String password;
	/**
	 * 
	 */
	private String user;
	/**
	 * 
	 */
	private String response;
	/**
	 * 
	 */
	public static Logger logger = Logger.getLogger("");
	/**
	 * 
	 */
	public MLRuleBalanceConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications/";
	}

/**
 * @param appName
 * @param pov
 * @param modelViewName
 * @return
 * @throws ParseException
 */
public ItemsObject getRuleBalancing(String appName, String pov,
			String modelViewName,String stringDelimiter) throws ParseException {
		HashMap<String, String> hashMap = null;
		if (modelViewName != null) {
			hashMap = new HashMap<String, String>();
			hashMap.put("modelViewName", modelViewName);
			hashMap.put("stringDelimiter", stringDelimiter);
		}
		logger.info("RuleBalancingUrl:"+ url + appName + "/povs/"
				+ pov + "/ruleBalance");
		this.response = JAXRestClient.callGetService(url + appName + "/povs/"
				+ pov + "/ruleBalance", user, password, hashMap, "*/*");
		logger.info("Response: "+ response);
		if (response != null) {
			JSONResultParser result = new JSONResultParser(this.response);
			ItemsObject items = result.getItemsObject();
			return items;
			
		}
		ItemsObject obj = new ItemsObject(true,response, "", "");
		return obj;
	}

}
