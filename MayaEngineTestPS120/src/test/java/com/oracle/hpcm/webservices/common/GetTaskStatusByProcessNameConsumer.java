package com.oracle.hpcm.webservices.common;


import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class GetTaskStatusByProcessNameConsumer {
	private String url;
	private String password;
	private String user;
	private String response;
	public static Logger logger = Logger.getLogger("getTaskStatus");
	public GetTaskStatusByProcessNameConsumer() {
		UserObject userCredentials = PropertiesUtils.getUserObject();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
		url = PropertiesUtils.getWebServiceURL()+"/applications/jobs/ChecktaskStatusJob/";
	}

	public ResultObject getJobStatus(String jobName) throws ParseException {
		String jobURL= url + jobName;
		this.response = JAXRestClient.callGetService(jobURL, user, password,null,"*/*");
		logger.info(this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getResultObject();
	}
	
	public boolean waitForJobToFinish(String jobNameFull, int timeout) throws ParseException{
		ResultObject rs = new ResultObject(false,"","");
/*		if(jobNameFull.split("_").length != 6)
			return false;*/
		logger.entering(this.getClass().getSimpleName(), "waitForJobToFinish");
		logger.info("Waiting for job "+jobNameFull+" to finish.");
		String jobName = jobNameFull;//JobNameElementArray[1]+"_"+JobNameElementArray[2]+"_"+JobNameElementArray[3]+"_"+JobNameElementArray[4];
		logger.info("Checking state for the jobname :"+jobName);
		for(int i = 0 ; i< timeout/20; i++){
			
			try {
				Thread.sleep(20000);
				 rs = getJobStatus(jobName);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//System.out.println("Sttaus of the job"+rs.getStatusMessage());
			if(!rs.getStatusMessage().contains("In Progress")){
				if(rs.getStatusMessage().contains("Success"))  
					return true;
				else
					return false;
			}else{
				logger.info("Checking state for the jobname :"+jobName);
			}
		}
		logger.exiting(this.getClass().getSimpleName(), "waitForJobToFinish");
		return false;
	}
	public String getJobRunState(String jobName,String text ){
		
		//LM1_RunCalcs_D20141027T005836_168= Done,Ledger_Calc_P2042742=Success,Ledger_clear_calc_P2042742=Succes
		logger.info("job status text : "+text);
		String[] textArray = text.split(",");
		if(textArray.length==0)
			return null;
		for(String individualTaskStatus:textArray){
			if(individualTaskStatus.contains(jobName))
				return individualTaskStatus.split("=")[1];
		}
		return null;
	
	}
	public boolean checkFailureInSubtasks() {
					String text = this.response;
					if(text == null)
						return false;
					//LM1_RunCalcs_D20141027T005836_168= Done,Ledger_Calc_P2042742=Success,Ledger_clear_calc_P2042742=Succes
					String[] textArray = text.split(",");
					if(textArray.length==0)
						return false;
					
					for(String jobStatus : textArray){
						String[] taskStatusArray = jobStatus.split("=");
						if(taskStatusArray.length>1){
							if(taskStatusArray[1].toUpperCase().contains("FAILURE"))
								{
									return false;
								}
							if(taskStatusArray[1].toUpperCase().contains("DONE"))
							{
								return true;
							}
						}
							
					}
					return true;
	}
	public boolean isJobActive(String jobNameFull) throws ParseException{
		ResultObject rs = new ResultObject(false,"","");
	
		//logger.entering(this.getClass().getSimpleName(), "waitForJobToFinish");
		logger.info("Waiting for job "+jobNameFull+" to finish.");
		
		String jobName = jobNameFull;//JobNameElementArray[0]+"_"+JobNameElementArray[1]+"_"+JobNameElementArray[2]+"_"+JobNameElementArray[3];
		rs = getJobStatus(jobName);
		String jobStatus = rs.getText();
		if(rs.getStatusMessage().contains("In Progress"))
			return true;
        if(jobStatus.toUpperCase().contains("ACTIVE"))
            return true;
        if(jobStatus.toUpperCase().contains("DONE")){
        	//logger.exiting(this.getClass().getSimpleName(), "waitForJobToFinish");
        	return false;
        }
	    return false;
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
		GetTaskStatusByProcessNameConsumer WSObj = new GetTaskStatusByProcessNameConsumer();
		try {
			boolean text = WSObj.waitForJobToFinish("ML_ML_DeleteMLPOV_D20160202T223018_46b_1",470);
			System.out.println("status: "
					+ text);

		} catch (Exception E) {
			LogUtil.print("Encountered exception " + E.getMessage());
			E.printStackTrace();
		}
	}
}
