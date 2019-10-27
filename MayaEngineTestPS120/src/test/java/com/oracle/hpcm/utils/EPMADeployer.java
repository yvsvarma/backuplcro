package com.oracle.hpcm.utils;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.meterware.httpunit.HttpException;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;


public class EPMADeployer {

	String url;
	String ssoToken;
	//String appName;
	public static Logger logger = Logger.getLogger("EPMADeployer");;

	public EPMADeployer() throws Exception {
		
		this.url = "http://" + System.getProperty("server") + ":" + System.getProperty("port");

		WebConversation conversation = new WebConversation();
		String logonUrl = "/workspace/logon";

		logger.log(Level.INFO,"baseHostUrl : " + url);
		logger.log(Level.INFO,"logonUrl :  " + url + logonUrl);
		WebRequest request = new PostMethodWebRequest(url + logonUrl);
		request.setParameter("sso_username", System.getProperty("user"));
		request.setParameter("sso_password", System.getProperty("password"));
		conversation.getClientProperties().setAcceptGzip(false);
		WebResponse response = conversation.getResponse(request);

		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new ByteArrayInputStream(response.getText().getBytes()));
		
		Element e = (Element)doc.getFirstChild();
		String status = e.getAttribute("type");
		logger.log(Level.INFO," Tried login with provided crendentials. Status of attempt : "+status);
		assert(status.equals("success"));
		this.ssoToken = doc.getFirstChild().getFirstChild().getTextContent();
		logger.log(Level.INFO," Connection exists. ssoToken :" + ssoToken);
		System.setProperty("ssotoken", this.ssoToken);
				
		
	}


	public boolean delete(String appName) throws IOException, Exception {
		
		logger.log(Level.INFO,"Deleting the application " + appName);
		sendEPMARequest(appName, null, "delete");
		return true;

	}
	/**
	 *Deploy the application, uses the application name from System.getProperty("appName") and model name from System.getProperty("model.dir")
	 *return true for successful deploy else return false.
	 * @return boolean
	 */
	public boolean deploy(String appName) throws Exception {
		InputStream appStream;
		//System.getProperty("")
		logger.log(Level.INFO,"Deploying the application :" + appName + " at "+System.getProperty("model.dir")+appName+"/");
		appStream = getCompressedAppStream(System.getProperty("model.dir")+appName+"/");
		boolean status = sendEPMARequest(appName, appStream, "deploy");
		logger.log(Level.INFO," Deployement action completed :" + appName);
		return status;
	}

	/**
	 * This method sends the requests to EPMA using sendAppData method.
	 * It can used to either delete or deploy any application.
	 * @param  Name of the application
	 * @param  Input Stream of the model directory.
	 * @param  name of the action which needs to be performed. Either "deploy" or "delete".
	 * @return      boolean
	 */
	protected boolean sendEPMARequest(String appName, InputStream appStream, String action) throws IOException, Exception {
		ServerSocket serverSocket = new ServerSocket(0);
		Integer localPort = serverSocket.getLocalPort();
		logger.log(Level.INFO,"Opened local socket at port: " + localPort);
		String callbackIP = null;
		if (callbackIP == null || callbackIP == "") {
			callbackIP = InetAddress.getLocalHost().getHostAddress();
		}
		String callBackAddress = "http://" + callbackIP + ":" + localPort.toString();
		int epma_deployment_timeout = 180000; //default timeout
		if (System.getProperty("epmadeploytimeout") != null) {
			//set new timeout if the property is set
			epma_deployment_timeout = Integer.parseInt(System.getProperty("epmadeploytimeout")) * 1000;
		}
		logger.log(Level.INFO,"epma_deployment_timeout(Milli Seconds): " + epma_deployment_timeout);
		serverSocket.setSoTimeout(epma_deployment_timeout);

		sendAppData(appName, appStream, callBackAddress, action);

		boolean waitResponse = true;
		while (waitResponse) {
			logger.log(Level.INFO," Accepting callback connection from server.");
			Socket socket;
			try {
				socket = serverSocket.accept();
			} catch (java.net.SocketTimeoutException ste) {
				logger.log(Level.WARNING," WARNING: EPMA Deployment timeout reached. Assuming "+action+" action succeeded.");
				serverSocket.close();
				return true;
			}
			InputStream is = socket.getInputStream();
			logger.log(Level.INFO," Got callback request from server.");
			byte[] buf = new byte[512 * 1024];
			int end = is.read(buf);
			String text = new String(buf, 0, end);

			logger.log(Level.INFO," Text: " + text);
			waitResponse = false;
			is.close();
			logger.log(Level.INFO,"Callback request proceeded.");
			serverSocket.close();
			if (text.indexOf("type=\"success\"") > 0) {
				return true;
			} else {
				return false;
			}
		}
		serverSocket.close();
		return false;
	}

	protected void sendAppData(String appName, InputStream appData, String callbackUrl, String action) throws Exception {
		logger.log(Level.INFO,"Sending application data to " + url + " for action = "+action+".");
		String xmlHeader = "<application name='" + appName + "' " +
			"product='PROFITABILITY_PRODUCT' instanceName='Default' " + "appServer='EssbaseCluster-1' host='EssbaseCluster-1' project='Default Application Group' />";
		String AppListenerURL = "/profitability/HPMApplicationListener?action=deploy&lang_id=en&app_name=" + appName;
		WebConversation conversation = new WebConversation();
		WebRequest request;
		if (action.equals("deploy")) {
			request = new PostMethodWebRequest(url + AppListenerURL, appData, "application/xml");
			request.setHeaderField("Content-Type", "application/x-gzip");
		} else {
			AppListenerURL = "/profitability/HPMApplicationListener?action=delete&lang_id=en&app_name=" + appName;
			request = new PostMethodWebRequest(url + AppListenerURL);
		}
		request.setParameter("action", action);
		request.setParameter("lang_id", "en");
		request.setParameter("app_name", appName);

		request.setHeaderField("awb-sso-token", URLEncoder.encode(ssoToken, "utf-8"));
		request.setHeaderField("awb-jobstatus-payload", "");
		request.setHeaderField("awb-header-xml", URLEncoder.encode(xmlHeader, "utf-8"));
		request.setHeaderField("awb-callback-url", callbackUrl);
		request.setHeaderField("awb-jobstatus-callback", "");

		conversation.getClientProperties().setAcceptGzip(false);
		try {

			WebResponse response = conversation.sendRequest(request);
			response.close();
		} catch (HttpException e) {
			logger.log(Level.WARNING,"Exception caught. Continuing.");
		}
		logger.log(Level.INFO,"Sending application data to server is complete.");
	}

	protected InputStream getCompressedAppStream(String modelDir) throws IOException {
		String appXmlName = modelDir + "application.xml";
		String appXmlGzName = appXmlName + ".gz";
		File appXmlGzFile = new File(appXmlGzName);
		if (!appXmlGzFile.exists()) {
                    logger.log(Level.WARNING,"appliaction.xml.gz : File Not found. Compressing the application.xml file to gzip encoding.");
			appXmlGzFile = compress(appXmlName);
			
		} else {
			logger.log(Level.INFO," appliaction.xml.gz found in the folder.");
		}
		return new FileInputStream(appXmlGzFile);
	}

	

	static File compress(String fname) throws IOException {
		File inFile = new File(fname);
		assert inFile.exists();
		logger.log(Level.INFO," Compressing the application.xml " + fname);
		File outFile = File.createTempFile("application.xml", ".gz");
		GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outFile));
		copyStream(new FileInputStream(inFile), out);
		out.flush();
		out.close();
		return outFile;
	}

	static void copyStream(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[256 * 1024];
		int len;
		while ((len = is.read(buf)) > 0) {
			os.write(buf, 0, len);
		}
	}

}