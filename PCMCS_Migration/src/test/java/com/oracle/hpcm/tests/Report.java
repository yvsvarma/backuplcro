package com.oracle.hpcm.tests;

import java.util.HashMap;

public class Report {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExpFileName() {
		return expFileName;
	}
	public void setExpFileName(String expFileName) {
		this.expFileName = expFileName;
	}
	public String getActFileName() {
		return actFileName;
	}
	public void setActFileName(String actFileName) {
		this.actFileName = actFileName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public HashMap<String, String> getComparisons() {
		return comparisons;
	}
	public void addComparisons(String key, String value) {
		this.comparisons.put(key, value);
	}
	public String name= "";
	public String expFileName="";
	public String actFileName="";
	public String state="";
	public HashMap<String,String> comparisons=new HashMap<String,String>();
	
}
