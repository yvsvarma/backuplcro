package com.oracle.hpcm.webservices.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class ProcessImportTemplateConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("ImportTemplate");
	public ProcessImportTemplateConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications/";
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
		this.response = JAXRestClient.callPostService(appURL, user, password,
				hashMap);
		logger.info("Response: "+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// http://slc04ljy.us.oracle.com:19000/epm/rest/"+System.getProperty("version")+"/applications/MLBks3/jobs/ledgerDeployCubeJob?isKeepData=true&isReplaceCube=true&isRunNow=true&comment=Test

		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc04ssp.us.oracle.com");
		System.setProperty("port", "19000");
		System.setProperty("version", "v1");
		ProcessImportTemplateConsumer WSObj = new ProcessImportTemplateConsumer();
		try {

			
			ResultObject result =  WSObj.importTemplate("LM1CPY", "this is desc",
					"PROFITABILITY_WEB_APP", "EssbaseCluster-1",
					"Default Application Group",
					"Filename.zip.zip", true);
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
