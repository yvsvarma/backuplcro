package com.oracle.hpcm.webservices.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class ProcessCreateFileApplicationConsumer {

	private String url;
	private UserObject uo;
	private String response;
	public static Logger logger = Logger.getLogger("");
	public ProcessCreateFileApplicationConsumer(UserObject uo) {
		this.uo=uo;
		url = uo.getWebServiceURL()+"/fileApplications/";
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
		this.response = JAXRestClient.callPostService(uo,appURL, 
				hashMap);
		// System.out.println(this.response);
		logger.info("CreateFileApp response: "+response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	
}