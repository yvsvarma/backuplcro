package com.oracle.hpcm.webservices.ml;

import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class MLRunCalculationConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public Logger logger = Logger.getLogger("");
	public MLRunCalculationConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications/";
	}
	
	/**
	 * @param appName
	 * @param POV
	 * @param isClearCalculated
	 * @param isExecuteCalculations
	 * @param isRunNow
	 * @param subsetStart
	 * @param subsetEnd
	 * @param ruleName
	 * @param ruleSetName
	 * @param exeType
	 * @param stringDelimiter
	 * @return
	 * @throws ParseException
	 */
	public ResultObject runCalculation(String appName, String POV,boolean isClearCalculated, boolean isExecuteCalculations, boolean isRunNow, String subsetStart, String subsetEnd, String ruleName, String ruleSetName, String exeType, String stringDelimiter) throws ParseException{
		String urlRunCalc = url + appName+ "/povs/" + POV + "/jobs/runLedgerCalculationJob";
		HashMap<String,String> hsMap = new HashMap<String,String>();
		hsMap.put("isClearCalculated", ""+isClearCalculated);
		hsMap.put("isExecuteCalculations", ""+isExecuteCalculations);
		//boolean isRunNow, String subsetStart, String subsetEnd, String ruleName, String ruleSetName, String exeType
		hsMap.put("isRunNow", ""+isRunNow);
		hsMap.put("subsetStart", subsetStart);
		hsMap.put("subsetEnd", subsetEnd);
		hsMap.put("ruleName", ruleName);
		hsMap.put("ruleSetName", ruleSetName);
		hsMap.put("exeType", exeType);
		hsMap.put("stringDelimiter", stringDelimiter);
		this.response = JAXRestClient.callPostService(urlRunCalc, user, password, hsMap);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	public static void main(String[] args)  {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		MLRunCalculationConsumer WSObj = new MLRunCalculationConsumer();
		try {
			ResultObject results =WSObj.runCalculation("RBML12", "2014_January_Actual", false , true , true, "", "", "","", "ALL_RULES","_");
			System.out.println("status: "+ results.isResult() );
			System.out
			.println("message: "
					+ results.getText() );

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}

}
