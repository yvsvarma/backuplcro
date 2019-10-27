package com.oracle.hpcm.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

//
@Deprecated
public class WorkspaceUtils {
	private String user;
	private String password;
	private String url;
	private String response;
	public String ssoToken = null;
	public boolean isLogged = false;

	public String getSSOToken(String uName, String pWord) throws SAXException,
			IOException, ParserConfigurationException {
		ssoToken = null;
		isLogged = false;
		this.user = uName;
		this.password = pWord;
		this.url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port");

		String logonUrl = url + "/workspace/logon";

		LogUtil.print("[EPMADeployer.login]baseHostUrl : " + url);
		LogUtil.print("[EPMADeployer.login]logonUrl :  " + url + logonUrl);

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("sso_username", user);
		hashMap.put("sso_password", password);
		this.response = JAXRestClient.callPostFormService(logonUrl, user,
				password, hashMap);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new ByteArrayInputStream(response.getBytes()));

		Element e = (Element) doc.getFirstChild();
		String status = e.getAttribute("type");
		LogUtil.print("Tried login with provided crendentials. Status of attempt : "
				+ status);
		//ssoToken = doc.getFirstChild().getFirstChild().getTextContent();
		return ssoToken;
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
		WorkspaceUtils obj = new WorkspaceUtils();
		try {
			System.out.println(obj.getSSOToken("admin", "password1"));
		} catch (Exception E) {

		}
	}

}
