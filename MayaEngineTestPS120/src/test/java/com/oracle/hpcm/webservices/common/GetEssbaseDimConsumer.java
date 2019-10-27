package com.oracle.hpcm.webservices.common;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ResultObject;

public class GetEssbaseDimConsumer {

	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("GetApplicationConsumer");
	public GetEssbaseDimConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/aamApplications/";
	}

	public ResultObject getDims(String essbaseAppServer, String appName ,String cubeName) throws ParseException {
		String dimUrl = url+essbaseAppServer+"/"+appName;
		HashMap<String,String> hashMap = new HashMap<String,String>();
		hashMap.put("queryParameter", "{\"cubeName\":\""+cubeName+"\"}");
		this.response = JAXRestClient.callGetService(dimUrl, user, password, hashMap,
				"*/*");
		logger.log(Level.INFO,"url : "+dimUrl);
		logger.log(Level.INFO,"response : "+this.response);
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
		GetEssbaseDimConsumer getAppWSObj = new GetEssbaseDimConsumer();
		try {
			ResultObject appNames = getAppWSObj.getDims("EssbaseCluster-1","SPC","SPC");
				System.out.println(appNames.getText());
		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}

	}
}