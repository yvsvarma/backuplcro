package com.oracle.hpcm.utils;

public class ResultObject {
	private boolean result;
	private String statusMessage;
	private String details;


    
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public boolean isResult() {
		return result;
	}

	public String getDetails() {
		return details;
	}
	public String getText() {
		return details;
	}
        
              

	public ResultObject(boolean result, String statusMessage, String details) {
		super();
		this.result = result;
		this.statusMessage = statusMessage;
		this.details=details;

	}

 
   

}
