package com.oracle.hpcm.webservices.common;




import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.LogUtil;


public class GetPOVJobsConsumer {
	private String url;
	private String password;
	private String user;
	private String response;

	public GetPOVJobsConsumer() {
		user = System.getProperty("user");
		password = System.getProperty("password");
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port")
				+ "/profitability/rest/"+System.getProperty("version")+"/applications/";
	}

	public String getPOVJobs(String appName) {
		this.response = JAXRestClient.callGetService(url + appName
				+ "/povs/jobs", user, password,null,null);
		if (this.response != null) {
			return this.response;
/*			String jobNameFull = this.response;
			String[] stringArray = jobNameFull.split("_");
			String jobName = stringArray[1] + "_" + stringArray[2] + "_"
					+ stringArray[3] + "_" + stringArray[4];
			return jobName;*/
		}
		return null;
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
		GetPOVJobsConsumer WSObj = new GetPOVJobsConsumer();
		try {
			String text = WSObj.getPOVJobs("dLMA");
			System.out.println("status: "
					+ text);
			
		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}
}
