package com.oracle.hpcm.webservices.dp;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;

public class GetAssignmentRuleDefinitionsConsumer {

	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("getAssgRule");
	public GetAssignmentRuleDefinitionsConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public ItemsObject getAssgRules(String appName,String stageName) throws ParseException {
		String assignmentRulesURL = url +appName+ "/assignmentRules";
		HashMap<String,String> hashMap = new HashMap<String,String>();
		hashMap.put("stageName", stageName);
		logger.log(Level.INFO,"Assg URL:"+assignmentRulesURL);
		this.response = JAXRestClient.callGetService(assignmentRulesURL, user, password, hashMap,
				"application/json");
		logger.log(Level.INFO,"response :"+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getItemsObject();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// http://slc04ljy.us.oracle.com:19000/profitability/rest/"+System.getProperty("version")+"/applications/MLBks3/jobs/ledgerDeployCubeJob?isKeepData=true&isReplaceCube=true&isRunNow=true&comment=Test

		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		GetAssignmentRuleDefinitionsConsumer getAppWSObj = new GetAssignmentRuleDefinitionsConsumer();
		try {
			ItemsObject result =  getAppWSObj.getAssgRules("BksDP301","Customer Activity Cost");
			System.out.println("Status: "+ result.isResult());
			System.out.println("Message: "+ result.getText());
		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}

	}
}