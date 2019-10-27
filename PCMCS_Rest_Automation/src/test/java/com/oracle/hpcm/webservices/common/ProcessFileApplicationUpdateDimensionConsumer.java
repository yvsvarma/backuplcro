package com.oracle.hpcm.webservices.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.EPMAutomateUtility;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class ProcessFileApplicationUpdateDimensionConsumer {

	private String url;
	private String password;
	private String user;
	private String response;
	private static Logger logger = Logger.getLogger("uploadFileDim");
	public ProcessFileApplicationUpdateDimensionConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/fileApplications/";
	}


	public ResultObject uploadFile(String appName, String filename) throws IOException, ParseException {
		String appURL = url + appName + "/updateDimension";
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("dataFileName", filename);
		logger.info("URL: "+ appURL);
		this.response = JAXRestClient.callPostService(appURL, user,
				password, params);
		logger.info("Response: "+ response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
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
		System.setProperty("server", "slc01lxw.us.oracle.com");
		System.setProperty("port", "9000");
		System.setProperty("deleteport", "9000");
		System.setProperty("model.dir", "./models/");
		System.setProperty("version", "v1");
		System.setProperty("apiversion", "11.1.2.3.600");
		ProcessFileApplicationUpdateDimensionConsumer WSObj = new ProcessFileApplicationUpdateDimensionConsumer();
		try {


			//
			EPMAutomateUtility.uploadFileOverwrite("./models/ML/dimfiles/", "Activities.txt","inbox/");
			ResultObject result =  WSObj.uploadFile("MLT","Activities.txt");
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