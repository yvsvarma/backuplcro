package com.oracle.hpcm.webservices.ml;

import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class MLRunCalculationConsumer {
	private String url;
	private UserObject uo;
	private String response;
	public Logger logger = Logger.getLogger("");
	public MLRunCalculationConsumer(UserObject uo) {
		this.uo =uo;
		url = uo.getWebServiceURL()+"/applications/";
	}

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
		this.response = JAXRestClient.callPostService(uo,urlRunCalc, hsMap);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	
}
