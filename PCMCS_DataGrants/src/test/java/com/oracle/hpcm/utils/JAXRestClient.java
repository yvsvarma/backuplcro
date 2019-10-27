package com.oracle.hpcm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.meterware.httpunit.Base64;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

public class JAXRestClient {
	public static Logger logger = Logger.getLogger("cleine");
	static String[] jsonArrayParamNames = {"paths","clearCalculatedStageList","clearAllStageList","generateStageList","calculateStageList"};
	private static List<String> jsonArrayList = Arrays.asList(jsonArrayParamNames);
        
        private static String domain = System.getProperty("domain");
	public static String callPostService(String url, String username,
			String password, HashMap<String, String> queryMap) {
			DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
			if(System.getProperty("staging").equals("true"))
				{
					try {		
					SSLContext sslContext = SSLContext.getInstance("SSL");
					ServerTrustManager serverTrustManager = new ServerTrustManager();
					sslContext.init(null, new TrustManager[]{serverTrustManager}, null);
					defaultClientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null,sslContext));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (KeyManagementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				Client client = Client.create(defaultClientConfig);
			client.setConnectTimeout(PropertiesUtils.getTimeout()*1000);
			client.setReadTimeout(PropertiesUtils.getTimeout()*1000);
			client.addFilter(new HTTPBasicAuthFilter(username, password));

			WebResource webResource = client.resource(url);
			MultivaluedMap<String, String> params = new MultivaluedMapImpl();
			JSONObject payload = new JSONObject();
			if (queryMap != null)
				for (String key : queryMap.keySet())
					if(jsonArrayList.contains(key))
					{
						String text = queryMap.get(key);
						String[] elementsArray = text.split(";");
						List<String> elementList = Arrays.asList(elementsArray); 
						JSONArray array = new JSONArray(); 
						array.addAll(elementList);
						payload.put(key, elementList);
					}
					else
						payload.put(key, queryMap.get(key));
			//logger.info("payload :"+payload.toJSONString());
			ClientResponse response = webResource.queryParams(params).entity(payload.toJSONString()).post(
					ClientResponse.class);

			String responseText = response.getEntity(String.class);
			return responseText;

	}
	public static String callPostFormService(String url, String username,
			String password, HashMap<String, String> queryMap) {
		try {
			DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
			if(System.getProperty("staging").equals("true"))
				{
					try {		
					SSLContext sslContext = SSLContext.getInstance("SSL");
					ServerTrustManager serverTrustManager = new ServerTrustManager();
					sslContext.init(null, new TrustManager[]{serverTrustManager}, null);
					defaultClientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null,sslContext));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (KeyManagementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				Client client = Client.create(defaultClientConfig);
			client.setConnectTimeout(PropertiesUtils.getTimeout()*1000);
			client.setReadTimeout(PropertiesUtils.getTimeout()*1000);
			if (username != null)
				client.addFilter(new HTTPBasicAuthFilter(username, password));
			WebResource webResource = client.resource(url);
			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			for (String key : queryMap.keySet())
				formData.add(key, queryMap.get(key));

			ClientResponse response = webResource.type(
					MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(
					ClientResponse.class, formData);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			String responseText = response.getEntity(String.class);
			return responseText;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public static String callDeleteService(String url, String username,
			String password, HashMap<String, String> queryMap) {
		try {
			DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
			if(System.getProperty("staging").equals("true"))
				{
					try {		
					SSLContext sslContext = SSLContext.getInstance("SSL");
					ServerTrustManager serverTrustManager = new ServerTrustManager();
					sslContext.init(null, new TrustManager[]{serverTrustManager}, null);
					defaultClientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null,sslContext));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (KeyManagementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				Client client = Client.create(defaultClientConfig);
			client.setConnectTimeout(PropertiesUtils.getTimeout()*1000);
			client.setReadTimeout(PropertiesUtils.getTimeout()*1000);
			client.addFilter(new HTTPBasicAuthFilter(username, password));

			WebResource webResource = client.resource(url);
			MultivaluedMap<String, String> params = new MultivaluedMapImpl();
			if (queryMap != null)
				for (String key : queryMap.keySet())
					params.add(key, queryMap.get(key));
			ClientResponse response = webResource.queryParams(params)
					.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
			String responseText = response.getEntity(String.class);
			return responseText;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public static String callGetService(String url, String username,
			String password, HashMap<String, String> queryMap,
			String acceptString) {
		try {
			DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
                        boolean staging = false;
                        if(System.getProperty("staging") != null && System.getProperty("staging").equalsIgnoreCase("true")) {
                            staging = true;
                        }
			if(staging)
				{
					try {		
					SSLContext sslContext = SSLContext.getInstance("SSL");
					ServerTrustManager serverTrustManager = new ServerTrustManager();
					sslContext.init(null, new TrustManager[]{serverTrustManager}, null);
					defaultClientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null,sslContext));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (KeyManagementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				Client client = Client.create(defaultClientConfig);
			client.setConnectTimeout(PropertiesUtils.getTimeout()*1000);
			client.setReadTimeout(PropertiesUtils.getTimeout()*1000);
                        
                        //System.out.println("auth user name *** "+authUserName);
			client.addFilter(new HTTPBasicAuthFilter(username, password));
			if (queryMap != null){
				JSONObject queryParams = new JSONObject();
				for(String key: queryMap.keySet()){
					queryParams.put(key, queryMap.get(key));
				}
				logger.info("QueryParams = "+queryParams.toJSONString());
				url = url + "?queryParameter="+URLEncoder.encode(queryParams.toJSONString(),"UTF-8");
				logger.info("Url with queryParams = "+url);
			}
			WebResource webResource = client.resource(url);
			MultivaluedMap<String, String> params = new MultivaluedMapImpl();
			
			ClientResponse response = webResource.queryParams(params)
					.accept(acceptString).get(ClientResponse.class);

			String responseText = response.getEntity(String.class);
			return responseText;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public static String callFileAttachement(String url, String username,
			String password, File file, HashMap<String, String> queryMap) {
		try {

			
			byte[] dataFileContent = new byte[(int) file.length()];
			dataFileContent = read(file);

			Client client = Client.create();
			client.setConnectTimeout(PropertiesUtils.getTimeout()*1000);
			client.setReadTimeout(PropertiesUtils.getTimeout()*1000);
		    WebResource service = client.resource(url);
		    String auth = new String(Base64.encode(username+":"+password));
		    MultivaluedMap<String, String> params = new MultivaluedMapImpl();
			if (queryMap != null)
				for (String key : queryMap.keySet())
					params.add(key, queryMap.get(key));
		 
		    // Construct a MultiPart with two body parts
		    MultiPart multiPart = new MultiPart().bodyPart(new BodyPart(dataFileContent, MediaType.APPLICATION_OCTET_STREAM_TYPE));
		      
		 
		    // POST the request
		    ClientResponse wresponse = service.queryParams(params).header("Authorization", "Basic " + auth).type("multipart/mixed").post(ClientResponse.class, multiPart);

			String responseText = wresponse.getEntity(String.class);
			return responseText;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
    private static byte[] read(File file) throws IOException {

        byte []buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if ( ios.read(buffer) == -1 ) {
                throw new IOException("EOF reached while trying to read the whole file");
            }        
        } finally { 
            try {
                 if ( ios != null ) 
                      ios.close();
            } catch ( IOException e) {
            }
        }

        return buffer;
    }
}