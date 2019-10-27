package com.oracle.hpcm.utils;


import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.meterware.httpunit.Base64;


public class EPMAutomateUtility {
	private static String responseText="";
	public static Logger logger = Logger.getLogger("epmautomate");
	public static void main(String[] args) throws Exception {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		System.setProperty("user", "epm_default_cloud_admin");
		System.setProperty("password", "password1");
		System.setProperty("server", "slc10vic.us.oracle.com");
		System.setProperty("port", "9000");
		System.setProperty("deleteport", "9000");
		System.setProperty("epmapiversion", "11.1.2.3.600");
		System.setProperty("model", "BML12");
		String fileName = "template.zip";
		uploadFileOverwrite("./models/BML12/",fileName,"profitinbox");

	}

	public static boolean uploadFileOverwrite(String filePath, String fileName,String remoteFolder) throws Exception{
		if(doesFileExists(remoteFolder,fileName)){
			logger.info("File "+fileName+" exists.");
			deleteFile(remoteFolder,fileName);
				if(uploadFile(filePath,fileName,remoteFolder).isResult()==false)
				{
					logger.severe("upload of the file "+fileName+" failed.");
					return false;
				}else{
					logger.info("upload of the file "+fileName+" completed.");
					return true;
				}
		}else{
			if(uploadFile(filePath,fileName,remoteFolder).isResult()==false)
			{
				logger.severe("upload of the file "+fileName+" failed.");
				return false;
			}else{
				logger.info("upload of the file "+fileName+" completed.");
				return true;
			}
		}
	}
	public static boolean doesFileExists(String remoteFolder,String fileName) throws ParseException{
		logger.info("Searching for "+remoteFolder+"/"+fileName);
		String fullFileName = remoteFolder+"/"+fileName;
		for(String file : listFiles()){
			//logger.info(file);
				if(file.trim().contains(remoteFolder) && file.contains(fileName))
					return true;
		}
		return false;
	}
	public static String[] listFiles() throws ParseException{
		String url = PropertiesUtils.getInteropWebServiceURL()+"/applicationsnapshots";
		UserObject userCred = PropertiesUtils.getUserObject();
		String response = JAXRestClient.callGetService(url, userCred.getUserName(), userCred.getPassword(), null, "*/*");
		//System.out.println(response);
		//logger.log(Level.INFO,"response :"+response);
		JSONResultParser result = new JSONResultParser(response,"");
		ItemsObject items =  result.getItemsObject();
		if(!items.isResult())
			return null;
		//logger.info(items.getText());
		JSONParser parser = new JSONParser();
		JSONArray appArray = (JSONArray) parser.parse(items.getText());
		String[] appNameArray = new String[appArray.size()];
		for (int i = 0; i < appArray.size(); i++) {
			appNameArray[i] = (String) ((JSONObject) appArray.get(i))
					.get("name");
			//logger.finest("->"+appNameArray[i]);
		}
		return appNameArray;

	}
	public static ResultObject deleteFile(String extDir,String fileName) throws ParseException{
		String url = PropertiesUtils.getInteropWebServiceURL()+"/applicationsnapshots/"+extDir+"%5C"+fileName;
		UserObject userCred = PropertiesUtils.getUserObject();
		logger.info(url);
		String response = JAXRestClient.callDeleteService(url, userCred.getUserName(), userCred.getPassword(), null);
		//logger.log(Level.INFO,"response :"+response);
		JSONResultParser result = new JSONResultParser(response,"");
		return result.getResultObject();

	}
	public static ResultObject uploadFile(String localPath, String fileName , String remotePath) throws Exception {
	 final int DEFAULT_CHUNK_SIZE = 50 * 1024 * 1024;
	 InputStream fis = null;
	 byte[] lastChunk = null;
	 long totalFileSize = new File(localPath+fileName).length(), totalbytesRead = 0;
	 boolean isLast = false, status = true;
	 Boolean isFirst = true;
	 int packetNo = 1, lastPacketNo = (int) (Math.ceil(totalFileSize / (double) DEFAULT_CHUNK_SIZE));
	 try {
	 fis = new BufferedInputStream(new FileInputStream(localPath+fileName));
	 while (totalbytesRead < totalFileSize && status) {
		 int nextChunkSize = (int) Math.min(DEFAULT_CHUNK_SIZE, totalFileSize - totalbytesRead);
		 if (lastChunk == null) {
			 lastChunk = new byte[nextChunkSize];
			 totalbytesRead += fis.read(lastChunk);
			 if (packetNo == lastPacketNo)
				isLast = true;
			 	status = sendFileContents(isFirst, isLast, lastChunk,remotePath,fileName);
			 	isFirst=false;
			 	packetNo = packetNo + 1;
			 	lastChunk = null;
		 }
	 }
	 } finally {
	 if (fis != null)
	 fis.close();
	 }
	 //logger.info(responseText);
		
		JSONResultParser result = new JSONResultParser(responseText,"");
		return result.getResultObject();
	}


	
	private static boolean sendFileContents(Boolean isFirst, boolean isLast, byte[] lastChunk, String extDirPath, String fileName) throws Exception {
	HttpURLConnection connection = null;
	UserObject userCred = PropertiesUtils.getUserObject();
	try {
	URL url = new URL(String.format(PropertiesUtils.getInteropWebServiceURL()+"/applicationsnapshots/%s/contents?q={\"extDirPath\":\"%s\",\"chunkSize\":%d,\"isFirst\":%b,\"isLast\":%b,\"chunkNo\":%d}",fileName,extDirPath, lastChunk.length, isFirst, isLast,1));
	//System.out.println(url.toURI().toASCIIString());
	connection = (HttpURLConnection) url.openConnection();
	  String auth = new String(Base64.encode(userCred.getUserName()+":"+userCred.getPassword()));
	connection.setRequestMethod("POST");
	connection.setInstanceFollowRedirects(false);
	connection.setDoOutput(true);
	connection.setUseCaches(false);
	connection.setDoInput(true);
	connection.setRequestProperty("Authorization", "Basic " +auth);
	connection.setRequestProperty("Content-Type", "application/octet-stream");
	DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	wr.write(lastChunk);
	wr.flush();
	int statusCode = connection.getResponseCode();
	InputStream is = connection.getInputStream();
    int ch;
    StringBuffer sb = new StringBuffer();
    while ((ch = is.read()) != -1) {
      sb.append((char) ch);
    }
	String status = sb.toString();
	responseText=status;
	if (statusCode == 200 && status != null) {
	isFirst = false;
		return true;
	}else{
		return false;
	}
	} finally {
	if (connection != null) 
	connection.disconnect();
	}
}
}

