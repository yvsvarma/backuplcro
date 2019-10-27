package com.oracle.hpcm.webservices.ml;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ResultObject;

/*http://{HostName}/profitability/rest/"+System.getProperty("version")+"/applications/{applicationName}
 * /povs/{srcPOVMemberGroup}/jobs/copyPOVJob/{destPOVMemberGroup}?
 * manageRule=Value&inputData=Value&adjustmentValues=Value&allocatedValues=Value
 */
public class ProcessLedgerCopyPOVConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("copyPov");
	public ProcessLedgerCopyPOVConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public ResultObject copyPOV(String appName, String srcPov,String destPov, boolean  manageRule,boolean inputData, boolean adjustmentValues, boolean allocatedValues,boolean createDestPOV, String stringDelimter) throws ParseException{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("isManageRule", ""+manageRule);
		hashMap.put("isInputData", ""+inputData);
		hashMap.put("isAdjustmentValues", ""+adjustmentValues);
		hashMap.put("isAllocatedValues", ""+allocatedValues);
		hashMap.put("createDestPOV", ""+createDestPOV);
		hashMap.put("stringDelimter", stringDelimter);
		String copyPovURL = url + appName + "/povs/"+ srcPov + "/jobs/copyPOVJob/"+destPov;
		
		logger.info("copy pov URL : "+copyPovURL);
		logger.info("Query Strings Map ");
		for(Entry<String,String> e: hashMap.entrySet()){
			logger.info(e.getKey() +" : " + e.getValue());
		}
		this.response = JAXRestClient.callPostService(copyPovURL, user, password, hashMap);
		
		logger.info("response : "+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	public static void main(String[] args) throws ParseException {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		ProcessLedgerCopyPOVConsumer WSObj = new ProcessLedgerCopyPOVConsumer();
		WSObj.populatePOVs();
		try {
			ResultObject result = WSObj.copyPOV("ML","2014_February_Actual","2014_December_Actual",false,false,false,false,false,"_");
			System.out
					.println("status: "
							+ result.isResult());
			System.out.println("Response: "+ result.getText());

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}
	public void populatePOVs() throws ParseException{
		String[] pov1 = {"2013","2014","2015"};
		String[] pov2 = {"January","February","March","April","May","June","July","August","September","October","November","December"};
		String[] pov3 = {"Actual"};
		for(String pov3DimMem : pov3)
			for(String pov2DimMem : pov2)
				for(String pov1DimMem : pov1)
					copyPOV("LM1","2013_Actual_January",pov1DimMem+"_"+pov2DimMem+"_"+pov3DimMem,true,true,true,true,true,"_");
		//copyPOV("ML","2014_February_Actual","2014_December_Actual",false,false,false,false,false,"_");
	}
}
