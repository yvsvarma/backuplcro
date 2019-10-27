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

/*http://{HostName}/profitability/rest/"+System.getProperty("version")+"/applications/\
 * {applicationName}/povs/{povMemberGroup}/jobs/clearPOVJob?
 * manageRule=Value&inputData=Value&adjustmentValues=Value&allocatedValues=Value
 * 
 */
public class ProcessLedgerClearPOVConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public Logger logger = Logger.getLogger("");
	public ProcessLedgerClearPOVConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications/";
	}

	public ResultObject clearPOV(String appName, String pov, boolean  manageRule,boolean inputData, boolean adjustmentValues, boolean allocatedValues,String delimiter) throws ParseException{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("isManageRule", ""+manageRule);
		hashMap.put("isInputData", ""+inputData);
		hashMap.put("isAdjustmentValues", ""+adjustmentValues);
		hashMap.put("isAllocatedValues", ""+allocatedValues);
		hashMap.put("isAllocatedValues", ""+allocatedValues);
		hashMap.put("stringDelimiter", delimiter);
		this.response = JAXRestClient.callPostService(url + appName + "/povs/"+ pov + "/jobs/clearPOVJob", user, password, hashMap);
		logger.info("Response: "+ this.response);
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
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		ProcessLedgerClearPOVConsumer WSObj = new ProcessLedgerClearPOVConsumer();
		try {
			ResultObject result = WSObj.clearPOV("RLM1","2013_January_Actual", false,true,false,false,"_");
			System.out
					.println("status: "
							+ result.isResult() );
			System.out.println("Message: "
					+ result.getText());

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}
}
