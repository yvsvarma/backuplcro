package com.oracle.hpcm.webservices.common;


import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;

public class GetTaskStatusByProcessNameConsumer {
	private String url;
	private UserObject userCredential;
	private String response;
	public static Logger logger = Logger.getLogger("getTaskStatus");
	public GetTaskStatusByProcessNameConsumer(UserObject userObject) {
		userCredential = userObject;
		url = userObject.getWebServiceURL()+"/applications/jobs/ChecktaskStatusJob/";
	}

	public ResultObject getJobStatus(String jobName) throws ParseException {
		String jobURL= url + jobName;
		this.response = JAXRestClient.callGetService(this.userCredential,jobURL,null,"*/*");
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
		//String[] JobNameElementArray = jobNameFull.split("_");
		String jobName = jobNameFull;//JobNameElementArray[1]+"_"+JobNameElementArray[2]+"_"+JobNameElementArray[3]+"_"+JobNameElementArray[4];
		logger.info("Checking state for the jobname :"+jobName);
		for(int i = 0 ; i< timeout/20; i++){
			
			try {
				Thread.sleep(10000);
				 rs = getJobStatus(jobName);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("Status of the job : "+rs.getStatusMessage());
			if(!rs.getStatusMessage().contains("In Progress")){
/*				{String jobStatus = getJobRunState(jobName,rs.getText());
				logger.info("jobtext "+jobStatus);
	            if(jobStatus.toUpperCase().equals("NEW"))
	                continue;
	            if(jobStatus.toUpperCase().equals("ACTIVE"))
	                continue;
	            if(jobStatus.toUpperCase().equals("DONE")){
	            	logger.exiting(this.getClass().getSimpleName(), "waitForJobToFinish");
	            	return checkFailureInSubtasks();*/
	            if(rs.getStatusMessage().contains("Success")){
	            	return true;
	            }if(rs.getStatusMessage().contains("Error")){
	            	return false;
	            }
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
							if(taskStatusArray[1].toUpperCase().equals("FAILURE"))
								{
									return false;
								}
							if(taskStatusArray[1].toUpperCase().equals("Done"))
							{
								return true;
							}
						}
							
					}
					return true;
	}
}
