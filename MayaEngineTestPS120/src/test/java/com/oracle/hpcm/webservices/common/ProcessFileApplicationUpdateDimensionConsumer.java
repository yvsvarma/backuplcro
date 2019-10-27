package com.oracle.hpcm.webservices.common;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ResultObject;

public class ProcessFileApplicationUpdateDimensionConsumer {

	private String url;
	private String password;
	private String user;
	private String response;
	private static Logger logger = Logger.getLogger("uploadFileDim");
	public ProcessFileApplicationUpdateDimensionConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/fileApplications/";
	}


	public ResultObject uploadFile(String appName, File file) throws IOException, ParseException {
		String appURL = url + appName + "/updateDimension";
		logger.info("URL: "+ appURL);
		this.response = JAXRestClient.callFileAttachement(appURL, user,
				password, file, null);
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
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		ProcessFileApplicationUpdateDimensionConsumer WSObj = new ProcessFileApplicationUpdateDimensionConsumer();
		try {


			
			ResultObject result =  WSObj.uploadFile("LM1CPY", new File(
					"./models/ML/dimfiles/Activities.txt"));
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