package com.oracle.pcmcs.dbutils.dto;

import com.oracle.pcmcs.enums.EnumsClass.ANALYTICS_ITEM_TYPE;

public class AnalyticDTO {
	public AnalyticDTO(ANALYTICS_ITEM_TYPE type, long id, long applicationId, String name, String definition,
			String description, boolean isEnabled) {
		super();
		this.type = type;
		this.id = id;
		this.applicationId = applicationId;
		this.name = name;
		this.definition = definition;
		this.description = description;
		this.isEnabled = isEnabled;
	}
	public AnalyticDTO(){
		super();
	}
	private ANALYTICS_ITEM_TYPE type;
	private long id;
	private long applicationId;
	private String name;
	private String definition;
	private String description;
	private boolean	isEnabled;
	public void setType(ANALYTICS_ITEM_TYPE type) {
		this.type = type;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public ANALYTICS_ITEM_TYPE getType() {
		return type;
	}
	public long getId() {
		return id;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public String getName() {
		return name;
	}
	public String getDefinition() {
		return definition;
	}
	public String getDescription() {
		return description;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	
}
