package com.oracle.hpcm.webservices.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ResultObject;

public class ProcessCreateFileApplicationConsumer {

	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("");
	public ProcessCreateFileApplicationConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/fileApplications/";
	}

	public ResultObject createApplication(String appName, String desc,
			String instanceName, String essApplicationServer,
			String sharedServicesProject, String webserver,
			String ruleDimensionName, String balanceDimensionName,
			String Unicode) throws IOException, ParseException {
		String appURL = url + appName;
		logger.info("CreateFileApp URL: "+appURL);
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("description", desc);
		hashMap.put("instanceName", instanceName);
		hashMap.put("essApplicationServer", essApplicationServer);
		hashMap.put("sharedServicesProject", sharedServicesProject);
		hashMap.put("webServer", webserver);
		hashMap.put("ruleDimensionName", ruleDimensionName);
		hashMap.put("balanceDimensionName", balanceDimensionName);
		hashMap.put("Unicode", "" + Unicode);
		this.response = JAXRestClient.callPostService(appURL, user, password,
				hashMap);
		// System.out.println(this.response);
		logger.info("CreateFileApp response: "+response);
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
		ProcessCreateFileApplicationConsumer WSObj = new ProcessCreateFileApplicationConsumer();
		try {
			//DeleteApplicationConsumer deletAppWS = new DeleteApplicationConsumer();
			//deletAppWS.deleteApp("X1");
			ResultObject result =  WSObj.createApplication("X3", "this is desc.",
					"PROFITABILITY_WEB_APP",
					"EssbaseCluster-1",
					"Default Application Group",
					"slc04ssp.us.oracle.com", "Rule",
					"Balance", "True");
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