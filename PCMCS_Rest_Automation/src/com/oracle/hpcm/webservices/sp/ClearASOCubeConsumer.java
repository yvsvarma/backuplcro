package com.oracle.hpcm.webservices.sp;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ResultObject;

/*urlHost+"/profitability/rest/"+version+RestURIConstants.APPLICATIONS+"/"+appName+RestURIConstants.POVS+"/"+args[3].trim().replaceAll(STRING_DELIMTER, "_").replaceAll(" ", "%20")+
				RestURIConstants.JOBS+RestURIConstants.COPY_SP_DP_POV_JOB+"/"+args[4].trim().replaceAll(STRING_DELIMTER, "_").replaceAll(" ", "%20")+"?"+"copyCostLayerData="+copyCostLayerFlag+"&copyRevenueLayerData="+copyRevenueLayerFlag+
				"&copyAssignments="+copyAssignmentsFlag+"&copyDriverAssociations="+copyDriverAssociationsFlag+"&copyDriverValues="+copyDriverValuesFlag+"&copyCostRevenueValues="+copyCostRevenueValuesFlag+
				"&copyCalculationRules="+copyCalculationRulesFlag);
		System.out.println("userName = " + userName);
 */
public class ClearASOCubeConsumer{
	private String url;
	private String password;
	private String user;
	private String response;
	public enum LayerName{
		COST,REVENUE
	}
	public static Logger logger = Logger.getLogger("copyPov");
	public ClearASOCubeConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public ResultObject clearASOCube(String appName, String Pov, LayerName layerName,String stringDelimiter) throws ParseException{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("layerName", layerName.toString());
		hashMap.put("stringDelimiter",stringDelimiter);
		String clearASOPovURL = url + appName + "/povs/"+ Pov + "/jobs/clearASOCube";
		this.response = JAXRestClient.callPostService(clearASOPovURL, user, password, hashMap);
		logger.info("copy pov URL : "+clearASOPovURL);
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
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		ClearASOCubeConsumer WSObj = new ClearASOCubeConsumer();
		try {
			System.out
					.println("status: "
							+ WSObj.clearASOCube("BksSP82","2009_January_Actual",LayerName.REVENUE,"_").getText());

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}
}
