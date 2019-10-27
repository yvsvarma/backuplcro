package com.oracle.hpcm.webservices.common;

import java.util.HashMap;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class ProcessExportTemplateConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("a");
	public ProcessExportTemplateConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications/";
	}

	public ResultObject exportTemplate(String appName, String fileName) throws ParseException {

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("fileName", fileName);

		// ?description="+desc+"&instanceName="+instanceName+"&essApplicationServer="+essApplicationServer+"&sharedServicesProject="+sharedServicesProject+"&fileName="+file.getName()+"&isApplicationOverwrite="+isApplicationOverwrite
		String appURL = url + appName + "/jobs/templateExportJob";

		//this.response = JAXRestClient.callGetService(appURL, user, password,hashMap, "application/json");
		this.response = JAXRestClient.callPostService(appURL, user, password,hashMap);
		logger.info(this.response);
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
		ProcessExportTemplateConsumer WSObj = new ProcessExportTemplateConsumer();
		try {
			ResultObject result =  WSObj.exportTemplate("00LM1", "Filename");
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

// http://{HostName}/profitability/rest/"+System.getProperty("version")+"/applications/{applicationName}
// jobs/templateExportJob?fileName=Value
