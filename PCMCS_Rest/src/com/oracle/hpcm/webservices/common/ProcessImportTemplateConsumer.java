package com.oracle.hpcm.webservices.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class ProcessImportTemplateConsumer {
	private String url,response;
	private UserObject uo;
	public static Logger logger = Logger.getLogger("ImportTemplate");
	public ProcessImportTemplateConsumer(UserObject uo) {
		this.uo = uo;

		url = uo.getWebServiceURL()+"/applications/";
	}

	public ResultObject importTemplate(String appName, String desc,
			String instanceName, String essApplicationServer,
			String sharedServicesProject, String fileName,
			boolean isApplicationOverwrite) throws IOException, SAXException, ParseException {

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("description", desc);
		hashMap.put("instanceName", instanceName);
		hashMap.put("essApplicationServer", essApplicationServer);
		hashMap.put("sharedServicesProject", sharedServicesProject);
		hashMap.put("fileName", fileName);
		hashMap.put("isApplicationOverwrite", "" + isApplicationOverwrite);
		// ?description="+desc+"&instanceName="+instanceName+"&essApplicationServer="+essApplicationServer+"&sharedServicesProject="+sharedServicesProject+"&fileName="+file.getName()+"&isApplicationOverwrite="+isApplicationOverwrite
		String appURL = url + appName + "/jobs/templateImportJob";
		logger.info("Import Url: "+ appURL);
		logger.info("ImportUrl: "+appURL);
		this.response = JAXRestClient.callPostService(this.uo,appURL,hashMap);
		logger.info("Response: "+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	
}
