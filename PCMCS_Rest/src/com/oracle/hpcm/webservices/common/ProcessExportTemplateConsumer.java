package com.oracle.hpcm.webservices.common;

import java.util.HashMap;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class ProcessExportTemplateConsumer {
	private String url;
	UserObject userCredentials;
	private String response;
	public static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("a");
	public ProcessExportTemplateConsumer() {

	}

	public ProcessExportTemplateConsumer(UserObject userObject) {
		userCredentials = userObject;
		url = userCredentials.getWebServiceURL()+"/applications/";
	}

	public ResultObject exportTemplate(String appName, String fileName) throws ParseException {

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("fileName", fileName);

		// ?description="+desc+"&instanceName="+instanceName+"&essApplicationServer="+essApplicationServer+"&sharedServicesProject="+sharedServicesProject+"&fileName="+file.getName()+"&isApplicationOverwrite="+isApplicationOverwrite
		String appURL = url + appName + "/jobs/templateExportJob";

		//this.response = JAXRestClient.callGetService(appURL, user, password,hashMap, "application/json");
		this.response = JAXRestClient.callPostService(userCredentials,appURL,hashMap);
		logger.info(this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

}

// http://{HostName}/profitability/rest/"+System.getProperty("version")+"/applications/{applicationName}
// jobs/templateExportJob?fileName=Value
