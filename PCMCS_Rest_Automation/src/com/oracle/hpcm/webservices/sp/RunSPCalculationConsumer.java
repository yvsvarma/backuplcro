package com.oracle.hpcm.webservices.sp;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ResultObject;

public class RunSPCalculationConsumer{
	private String url;
	private String password;
	private String user;
	private String response;
	public enum LayerName{
		COST,REVENUE
	}
	public static Logger logger = Logger.getLogger("copyPov");
	public RunSPCalculationConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public ResultObject runSPCalcJob(String appName, String Pov ,String layerName, String  clearCalculatedStageList
			,String clearAllStageList, String generateStageList,
			String calculateStageList, boolean isTransferData) throws ParseException{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("layerName", layerName);
		hashMap.put("clearCalculatedStageList", ""+clearCalculatedStageList);
		hashMap.put("clearAllStageList", ""+clearAllStageList);
		hashMap.put("generateStageList", ""+generateStageList);
		hashMap.put("calculateStageList", ""+calculateStageList);
		hashMap.put("isTransferData", ""+isTransferData);

		String runJobURL = url + appName + "/povs/"+ Pov + "/jobs/calcScriptsProcessJob";
		this.response = JAXRestClient.callPostService(runJobURL, user, password, hashMap);
		logger.info("Run SP Job URL : "+runJobURL);
		logger.info("Query Strings Map ");
		for(Entry<String,String> e: hashMap.entrySet()){
			logger.info(e.getKey() +" : " + e.getValue());
		}
		logger.info("response : "+this.response);
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
		System.setProperty("server", "slc01mab.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		RunSPCalculationConsumer WSObj = new RunSPCalculationConsumer();
		try {
			System.out
					.println("status: "
							+ WSObj.runSPCalcJob("BksSP82","2011_January_Actual","COST","Ledger Data","","Ledger Data","Ledger Data",true).getText());
		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}
}