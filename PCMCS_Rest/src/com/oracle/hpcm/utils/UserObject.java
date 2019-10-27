package com.oracle.hpcm.utils;

public class UserObject {
	public String getServer(){
		return server;
	}
	public String getPort(){
		return port;
	}
	public String getUserName() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	public String getDomain() {
		return domain;
	}
	public boolean isStaging(){
		return staging;
	}
	//user, password, server, port, epmapiversion, version,domain,staging,timeout
	public UserObject(String user, String password, String server, String port, String epmapiversion, String version, String domain, boolean staging, int timeout) {
		if(staging){
			this.user = domain + "." + user;
		}else{
			this.user = user;
		}
		this.staging=staging;
		this.password = password;
		this.domain = domain;
		this.port=port;
		this.epmapiversion=epmapiversion;
		this.staging=staging;
		this.server=server;
		this.version=version;
		this.timeout=timeout;
		System.out.println("[init]"+this.port);
	}

	private int timeout;
	private boolean staging;
	private String user;
	private String password;
	private String domain;
	private String server;
	private String port;
	private String epmapiversion;
	private String version;
	public String getWebServiceURL(){
		
		String url;
		if(!this.staging){
			url = String.format("http://%s:%s/epm/rest/%s", this.server,this.port,this.version);
		}else{
			url = String.format("https://%s/epm/rest/%s", this.server,this.version);
		}
		return url;
	}
	public String getInteropWebServiceURL(){
		
		String url;
		if(!this.staging){
			url = String.format("http://%s:%s/interop/rest/%s",this.server,this.port,this.epmapiversion);
		}else{
			url = String.format("https://%s/interop/rest/%s", this.server,this.epmapiversion);
		}
		return url;
	}
	public int getTimeout() {
		// TODO Auto-generated method stub
		return timeout;
	}
}
