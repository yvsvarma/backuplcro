package com.oracle.hpcm.utils;

import java.io.File;
import java.util.HashMap;

//
public class SharedServicesUtils {

	private String url;

	public SharedServicesUtils() {
		url = "http://" + System.getProperty("server") + ":"
				+ System.getProperty("port");
	}

	public String uploadFileToSS(File file, String SSOToken) {
		// /interop/framework/lcm/validateFSDestinationAction?
		// destinationFileSystemPath=lcm_hpcm.zip&
		// ssoToken=token
		// &isZip=true HTTP/1.1
		// POST /interop/lcmupload?trxID=-1&fileName=lcm_hpcm.zip&fileSize=33160
		// &chunkSize=33160&action=upload&loadArtifactStatus=false&isFirst=true&isLast=true&unzip=true&service=LCM
		// &EPM_FUNCTION=Application%20Management&EPM_ACTION=Upload&EPM_OBJECT=lcm_hpcm.zip
		// HTTP/1.1
		String InitURL = url
				+ "/interop/framework/lcm/validateFSDestinationAction";
		HashMap<String, String> hashMapInit = new HashMap<String, String>();
		hashMapInit.put("fileName", file.getName());
		hashMapInit.put("ssoToken", SSOToken);
		hashMapInit.put("isZip", "true");
		String response = JAXRestClient.callGetService(InitURL, null, null,
				hashMapInit, "*/*");
		System.out.println("init : " + response);
		String uploadURL = url + "/interop/lcmupload";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("trxID", "-1");
		hashMap.put("fileName", file.getName());
		hashMap.put("fileSize", "33972");
		hashMap.put("chunkSize", "33972");
		hashMap.put("action", "upload");
		hashMap.put("loadArtifactStatus", "false");
		hashMap.put("isFirst", "true");
		hashMap.put("isLast", "true");
		hashMap.put("unzip", "false");
		hashMap.put("service", "lcm");
		hashMap.put("EPM_FUNCTION", "Application Management");
		hashMap.put("EPM_ACTION", "Upload");
		hashMap.put("EPM_OBJECT", file.getName());

		response = JAXRestClient.callFileAttachement(uploadURL, null, null,
				file, hashMap);
		return response;
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
		WorkspaceUtils wkObj = new WorkspaceUtils();
		SharedServicesUtils obj = new SharedServicesUtils();

		try {
			String sso = wkObj.getSSOToken("admin", "password1");
			System.out.println("sso = " + sso);
			System.out.println(obj.uploadFileToSS((new File(
					"./model/BksML12/File_Export.zip")), sso));
		} catch (Exception E) {

		}
	}

}
