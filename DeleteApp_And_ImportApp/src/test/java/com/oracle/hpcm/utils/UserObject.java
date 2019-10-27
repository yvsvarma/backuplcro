package com.oracle.hpcm.utils;

public class UserObject {
	public String getUserName() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	public String getDomain() {
		return domain;
	}
	public UserObject(String user, String password, String domain, boolean staging) {
		if(staging){
			this.user = domain + "." + user;
		}else{
			this.user = user;
		}
		this.password = password;
		this.domain = domain;
	}
	private String user;
	private String password;
	private String domain;
}
