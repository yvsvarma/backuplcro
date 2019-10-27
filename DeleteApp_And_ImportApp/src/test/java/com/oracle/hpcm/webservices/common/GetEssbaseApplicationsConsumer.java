package com.oracle.hpcm.webservices.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class GetEssbaseApplicationsConsumer {

	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("GetApplicationConsumer");
	public GetEssbaseApplicationsConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/aamApplications/";
	}

	public ResultObject getEssApplicationsNames(String essbaseAppServer) throws ParseException  {
		this.response = JAXRestClient.callGetService(url+essbaseAppServer, user, password, null,
				"*/*");
		logger.log(Level.INFO,"url : "+url+essbaseAppServer);
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
		GetEssbaseApplicationsConsumer getAppWSObj = new GetEssbaseApplicationsConsumer();
		try {
			ResultObject appNames = getAppWSObj.getEssApplicationsNames("EssbaseCluster-1");
				System.out.println(appNames.getText());
		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}

	}
}