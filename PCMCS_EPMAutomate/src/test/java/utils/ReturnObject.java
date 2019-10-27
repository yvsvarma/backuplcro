package utils;

import org.testng.Reporter;

public class ReturnObject {
	public String getOutput() {
		return output;
	}
	public String getError() {
		return error;
	}
	public int getReturnValue() {
		return returnValue;
	}
	synchronized public void appendLineToOutput(String line){
		this.output = this.output + "\n" + line;
		Reporter.log(line,true);
	}
	synchronized public void appendLineToError(String line){
		this.error = this.error + "\n" + line;
		if(line!=null)
			Reporter.log(line,true);
	}
	synchronized public void setReturnValue(int returnValue) {
		this.returnValue = returnValue;
		Reporter.log("Return Value : "+returnValue,true);
	}
	
	public ReturnObject(String output, String error, int returnValue) {
		super();
		this.output = output;
		this.error = error;
		this.returnValue = returnValue;
	}
	public ReturnObject() {
	}
	
	 private String output;
	private String error;
	private int returnValue;
}
