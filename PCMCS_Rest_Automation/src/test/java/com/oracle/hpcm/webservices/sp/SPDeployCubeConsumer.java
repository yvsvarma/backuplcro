package com.oracle.hpcm.webservices.sp;

import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ResultObject;

public class SPDeployCubeConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("deployCube");
	public enum CubeType{
		CALCULATION_CUBE,REPORTING_CUBE
	}
	public SPDeployCubeConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public ResultObject runDeployCubeJob(String appName, CubeType cubeType,boolean isFirstTimeDeployment,
			boolean isReplaceDatabase, boolean isUpdateDatabase, boolean isarchiveDataBeforeDeploy,
			boolean isArchiveDataAndReloadAfterDeploy, boolean isDeleteDataArchiveAfterReload) throws ParseException {
		logger.entering(this.getClass().getSimpleName(), "runDeployCube");
		String deployURL = url + appName
				+ "/jobs/spDeployCubeJob";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("cubeType",  cubeType.toString());
		hashMap.put("isFirstTimeDeployment", "" + isFirstTimeDeployment);
		hashMap.put("isReplaceDatabase", "" + isReplaceDatabase);
		hashMap.put("isUpdateDatabase", "" + isUpdateDatabase);
		hashMap.put("isarchiveDataBeforeDeploy", "" + isarchiveDataBeforeDeploy);
		hashMap.put("isArchiveDataAndReloadAfterDeploy", "" + isArchiveDataAndReloadAfterDeploy);
		hashMap.put("isDeleteDataArchiveAfterReload", "" + isDeleteDataArchiveAfterReload);
		logger.info("Request: "+deployURL);
		this.response = JAXRestClient.callPostService(deployURL, user, password, hashMap);
		logger.info("Response: "+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	public static void main(String[] args) {
/*		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");*/
		System.setProperty("user", "admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("apsserver", "slc04ssp.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		SPDeployCubeConsumer WSObj = new SPDeployCubeConsumer();
		try {
			System.out.println("status: "+ WSObj.runDeployCubeJob("BksSP82", CubeType.CALCULATION_CUBE, true, false, true,false, false, false).getText());
		

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}

}
