package com.oracle.hpcm.utils;

import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class JSONResultParser {
	private String jsonText;
	private JSONObject jObj;
	private String statusMessage;
	private String details; 
	private int status;
	private boolean result = false;
	public static Logger logger = Logger.getLogger("JSONParser");
	public JSONResultParser(String jsonText) throws ParseException{
		this.jsonText = jsonText;
		JSONParser parser = new JSONParser();
		if(this.jsonText==null)
			this.jsonText="{}";
		if(this.jsonText.equals("Not Found"))
			this.jsonText="{\"statusText\":\"Failed\",\"details\":\"404 Not Found\"}";
		jObj = (JSONObject) parser.parse(this.jsonText);
		statusMessage=(String)jObj.get("statusMessage");
		details=(String)jObj.get("details");
		status = ((Long)jObj.get("status")).intValue();
		logger.info("Status="+status+"\tStatusMessage = "+statusMessage+"\tDetails = "+details);
		if(statusMessage==null){
			result = false;
		}else{
			if(statusMessage.equals("Success") && status <= 0)
			{
				result = true;
			}
			else{
				if(statusMessage.equals("In Progress") && status == -1)
					result = true;
				else
					result = false;
			}
		}
	}
	public boolean isPass(){
		return result;
	}
	public String getStatusMessage(){
		return statusMessage;
	}

	public ResultObject getResultObject(){
			return new ResultObject(result,getStatusMessage(),getDetails());
	}
	private String getDetails() {
		return details;
	}
	public ItemsObject getItemsObject(){
		if(!getItemsText().equals("[]") && getItemsText()!=null ){
			result = true;
		}
		return new ItemsObject(result,getDetails(),getDetails(),getItemsText());
	}
	public String getItemsText(){
		if(jObj.get("items")==null)
			return "[]";
		return jObj.get("items").toString();
	}
}
