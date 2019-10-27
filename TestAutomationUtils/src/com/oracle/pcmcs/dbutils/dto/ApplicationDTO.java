package com.oracle.pcmcs.dbutils.dto;

public class ApplicationDTO {
	private long id;
	private String name;
	private boolean isEnabled;
	
	public ApplicationDTO(){
		super();
	}
	
	public ApplicationDTO(long id, String name,boolean isEnabled){
		super();
		this.id = id;
		this.name = name;
		this.isEnabled = isEnabled;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
}
