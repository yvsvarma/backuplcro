package com.oracle.hpcm.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;


public class LCMImport {
	public static Logger logger = Logger.getLogger("LCMImport");
	String token;
	String appGuid;
	ZipFile lcmFile;
	WebConversation wc;
	String url;
	String appName;
	static int artifactsCount = 0;
	static int errorsCount = 0;
	static ArrayList < String > failedArtifacts = new ArrayList < String > ();
	public LCMImport() {
		this.token = System.getProperty("ssotoken");
		this.url = "http://" + System.getProperty("server") + ":" + System.getProperty("port");

	}

	public boolean execute(String app) throws Exception {
		appName = app;
		String lcmFileName = System.getProperty("model.dir")+ appName + "/lcm_hpcm.zip";
		logger.log(Level.INFO,"LCM file path :"+ lcmFileName);
		File f = new File(lcmFileName);
		if (!f.exists()) {
			logger.log(Level.SEVERE," LCM file does not exists.");
		}
		try {
			
			lcmFile = new ZipFile(f);
		} catch (Exception ex) {}
		boolean isImportPassed = false;
		logger.log(Level.INFO," Starting the LCM import for application :"+appName);
		this.appGuid = this.getGUIDFromAppName(appName);
		logger.log(Level.INFO," Application " + appName + " GUID: " + appGuid);
		String resourcePath = getFolderPath(lcmFile, "resource/");
		if (resourcePath != null) {
			String infoPath = getFolderPath(lcmFile, "info/");
			if (infoPath != null) {
				logger.log(Level.INFO,"resourcePath: " + resourcePath);
				logger.log(Level.INFO,"infoPath: " + infoPath);
			}


			ZipEntry entry = lcmFile.getEntry(infoPath + "listing.xml");
			logger.log(Level.INFO," Reading Listing.xml.");
			InputStream listingXml;

			listingXml = lcmFile.getInputStream(entry);
			if (listingXml != null) {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();


				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(listingXml);


				if (doc.getFirstChild().getNodeName() == "artifactListing") {
					String[] artifactsType = new String[] {
						"Preferences", "ModelDataMaster", "ModelDataDetails", "Tables", "TableJoins", "Stages", "POV", "Model", "Program", "Drivers", "RegularAssignments", "AssignmentRules", "DriverExceptions", "AssignmentRuleSelections", "DriverRules", "StageObjectCalculations", "CalculationRules", "ModelView", "SmartViewQueries"
					};
					for (int i = 0; i < artifactsType.length; i++) {
						String artifactTypeName = artifactsType[i];
						XPathFactory xPathfactory = XPathFactory.newInstance();
						XPath xpath = xPathfactory.newXPath();
						XPathExpression expr = xpath.compile("//*[@type=\"" + artifactTypeName + "\"]");
						NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
						for (int j = 0; j < nl.getLength(); j++) {
							Element element = (org.w3c.dom.Element) nl.item(j);
							String path = element.getAttribute("path");
							String name = element.getAttribute("name");
							// String id = element.getAttribute("artifactId");
							if (name.trim().startsWith("essbase")) continue;
							logger.log(Level.INFO,"Importing Artifact             type: " + artifactTypeName + "     Artifact's name: " + name + "      path:" + path);

							if (path.endsWith("/")) {
								if (name.startsWith("/")) path = path.substring(0, path.length() - 1) + name;
								else path += name;
							} else {
								if (name.startsWith("/")) path += name;
								else path += "/" + name;
							}
							// make the full path
							String absResPath = resourcePath.substring(0, resourcePath.length() - 1) + path;
							// logger.log(Level.INFO,"Printing Total Path: "+ absResPath );
							ZipEntry resourceEntry = lcmFile.getEntry(absResPath);
							InputStream artifactStream = lcmFile.getInputStream(resourceEntry);
							importArtifact(element, artifactStream);
						}

					}

				}

			}

			logger.log(Level.INFO," Total Artifacts Imported: " + (artifactsCount - errorsCount) + " Errors: " + errorsCount);
			if (errorsCount == 0) {
				logger.log(Level.INFO," LCM Import completed successfully.");


				isImportPassed = true;
			} else {
				if (artifactsCount - errorsCount == 0) {

					logger.log(Level.WARNING," LCM Import completed Failed.");

				} else {
					logger.log(Level.INFO," LCM Import completed partially.");
					isImportPassed = true;
				}
			}

		}
		return isImportPassed;
	}

	static String getFolderPath(ZipFile zip, String folder) {
		Enumeration < ZipEntry > entries = (Enumeration < ZipEntry > ) zip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry e = entries.nextElement();
			//logger.log(Level.INFO,"Entry : "+e.getName());
			if (e.isDirectory() && e.getName().endsWith(folder)) {
				return e.getName();
			}
		}
		return null;
	}


	private void importArtifact(Element artifactElement, InputStream artifactStream) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, MalformedURLException, IOException, SAXException, InterruptedException {
		artifactsCount++;

		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		Document document = documentBuilder.newDocument();

		// resources element
		Element root = document.createElement("resources");
		document.appendChild(root);

		// resource element
		Element resource = document.createElement("resource");
		root.appendChild(resource);

		Element option = document.createElement("Options");
		root.appendChild(option);

		// set an attribute to staff element
		Attr artifactPathAttr = document.createAttribute("artifactPath");
		artifactPathAttr.setValue(artifactElement.getAttribute("path"));
		resource.setAttributeNode(artifactPathAttr);


		Attr parentPathAttr = document.createAttribute("parentPath");
		parentPathAttr.setValue(artifactElement.getAttribute("path"));
		resource.setAttributeNode(parentPathAttr);

		Attr artifactIdAttr = document.createAttribute("artifactId");
		artifactIdAttr.setValue(artifactElement.getAttribute("name"));
		resource.setAttributeNode(artifactIdAttr);

		Attr descriptionAttr = document.createAttribute("description");
		descriptionAttr.setValue("");
		resource.setAttributeNode(descriptionAttr);

		Attr displayNameAttr = document.createAttribute("displayName");
		displayNameAttr.setValue(artifactElement.getAttribute("name"));
		resource.setAttributeNode(displayNameAttr);

		Attr lastUpdatedAttr = document.createAttribute("lastUpdated");
		lastUpdatedAttr.setValue("1400314588893");
		resource.setAttributeNode(lastUpdatedAttr);


		Attr typeAttr = document.createAttribute("type");
		typeAttr.setValue(artifactElement.getAttribute("type"));
		resource.setAttributeNode(typeAttr);

		Attr sizeAttr = document.createAttribute("size");
		sizeAttr.setValue("20");
		resource.setAttributeNode(sizeAttr);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(writer));
		String output = "<?xml version='1.0' encoding='UTF-8'?>" + writer.getBuffer().toString().replaceAll("\n|\r", "");

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5 * 1000).build();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post1 = new HttpPost(url + "/profitability/lcm");
		List < NameValuePair > urlParameters = new ArrayList < NameValuePair > ();
		urlParameters.add(new BasicNameValuePair("lcm_TaskId", "0"));
		urlParameters.add(new BasicNameValuePair("sso_token", token));
		urlParameters.add(new BasicNameValuePair("action", "beginImport"));
		urlParameters.add(new BasicNameValuePair("lcm_EndOperation", "true"));
		urlParameters.add(new BasicNameValuePair("lcm_migrationCtx", "88"));
		urlParameters.add(new BasicNameValuePair("application", this.appGuid));
		urlParameters.add(new BasicNameValuePair("artifact_query", output));
		urlParameters.add(new BasicNameValuePair("ECID-Context", "XYZXYZ"));

		post1.setEntity(new UrlEncodedFormEntity(urlParameters));
		httpClient = new DefaultHttpClient();

		HttpResponse response1 = httpClient.execute(post1);
		if (response1.getEntity() != null) {
			response1.getEntity().consumeContent();
		} 
		Thread.sleep(2);
		HttpPost post2 = new HttpPost(url + "/profitability/lcm" + "?sso_token=" + URLEncoder.encode(token, "utf-8") + "&action=doImport&application=" + URLEncoder.encode(appGuid, "utf-8") + "&ECID-Context=XYZXYZ");
		InputStreamEntity is;
		is = new InputStreamEntity(artifactStream, -1);
		post2.setEntity(is);
		HttpResponse response2 = httpClient.execute(post2);
		if (response2.getStatusLine().getStatusCode() == 200) {
			httpClient = new DefaultHttpClient();

			HttpPost post3 = new HttpPost(url + "/profitability/lcm");
			List < NameValuePair > urlParameters1 = new ArrayList < NameValuePair > ();
			urlParameters1.add(new BasicNameValuePair("lcm_TaskId", "0"));
			urlParameters1.add(new BasicNameValuePair("sso_token", token));
			urlParameters1.add(new BasicNameValuePair("action", "endImport"));
			urlParameters1.add(new BasicNameValuePair("lcm_EndOperation", "false"));
			urlParameters1.add(new BasicNameValuePair("lcm_migrationCtx", "88"));
			urlParameters1.add(new BasicNameValuePair("application", this.appGuid));
			urlParameters1.add(new BasicNameValuePair("ECID-Context", "XYZXYZ"));

			post3.setEntity(new UrlEncodedFormEntity(urlParameters1));
			HttpResponse response3 = httpClient.execute(post3);
			if (response3.getStatusLine().getStatusCode() == 200) logger.log(Level.INFO,"Artifact imported successfully.");
			else {
				logger.log(Level.SEVERE," Artifact import failed. ");
			}

		} else {
			logger.log(Level.SEVERE,"Artifact import failed.");
			errorsCount++;
		}

	}
	public String getGUIDFromAppName(String appName) throws Exception {

		wc = new WebConversation();
		wc.getClientProperties().setAcceptGzip(false);
		
		WebRequest guidReq = new PostMethodWebRequest(url + "/interop/framework/cas/getApplications");
		guidReq.setParameter("LOCALE_LANGUAGE", "en_US");
		guidReq.setParameter("REQUEST_LOCALE", "en_US");
		guidReq.setParameter("accessibilityMode", "false");
		guidReq.setParameter("rightToLeft", "false");
		guidReq.setParameter("sso_token", token);
		guidReq.setParameter("themeSelection", "BpmTadpole");
		guidReq.setParameter("projectName", "Default Application Group");
		WebResponse wr = wc.sendRequest(guidReq);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(wr.getText()));
		Document doc = db.parse(is);
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("//*[@srcappname=\"" + appName + "\"]");
		NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		if (nl.getLength() < 1) return null;
		else {
			for (int i = 0; i < nl.getLength(); i++) {
				Element n = (org.w3c.dom.Element) nl.item(i);
				if (n.getAttribute("id").startsWith("HPM:")) {
					return n.getAttribute("id").substring(4);
				}
			}
		}
		return null;
	}

}