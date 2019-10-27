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

public class MLDeployCubeConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("deployCube");
	public MLDeployCubeConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications/";
	}

	public ResultObject runDeployCubeJob(String appName, boolean isKeepData,
			boolean isReplaceCube, boolean isRunNow, String comment) throws ParseException {
		logger.entering(this.getClass().getSimpleName(), "runDeployCube");
		String deployURL = url + appName
				+ "/jobs/ledgerDeployCubeJob";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("isKeepData", "" + isKeepData);
		hashMap.put("isReplaceCube", "" + isReplaceCube);
		hashMap.put("isRunNow", "" + isRunNow);
		hashMap.put("comment", comment);
		logger.info("Request: "+deployURL);
		this.response = JAXRestClient.callPostService(deployURL, user, password, hashMap);
		logger.info("Response: "+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	public static void main(String[] args) throws ParseException {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "epm_default_cloud_admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc10vic.us.oracle.com");
		System.setProperty("port", "9000");
		System.setProperty("version", "v1");
		MLDeployCubeConsumer WSObj = new MLDeployCubeConsumer();
		ResultObject result = WSObj.runDeployCubeJob("RLM1", true, true, true,
				"Hello World!");
		try {
			System.out.println("status: "
					+ result.isResult());
			System.out.println("Message: "
					+ result.getText());

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}

}
