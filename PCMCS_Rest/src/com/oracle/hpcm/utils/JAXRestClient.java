package com.oracle.hpcm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

//import com.meterware.httpunit.Base64;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import java.net.URLEncoder;

public class JAXRestClient {
	public static Logger logger = Logger.getLogger("cleine");
	static String[] jsonArrayParamNames = {"paths","clearCalculatedStageList","clearAllStageList","generateStageList","calculateStageList"};
	private static List<String> jsonArrayList = Arrays.asList(jsonArrayParamNames);
	public static String callPostService(UserObject uo, String uri,HashMap<String, String> queryMap) {
		
			DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
			if(uo.isStaging())
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
			client.setConnectTimeout(uo.getTimeout()*1000);
			client.setReadTimeout(uo.getTimeout()*1000);
			client.addFilter(new HTTPBasicAuthFilter(uo.getUserName(), uo.getPassword()));

			WebResource webResource = client.resource(uri);
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
			logger.info("payload :"+payload.toJSONString());
			ClientResponse response = webResource.queryParams(params).entity(payload.toJSONString()).post(
					ClientResponse.class);

			String responseText = response.getEntity(String.class);
			return responseText;

	}
	
	public static String callDeleteService(UserObject uo, String uri, HashMap<String, String> queryMap) {
		
		try {
			DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
			if(uo.isStaging())
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
			client.setConnectTimeout(uo.getTimeout()*1000);
			client.setReadTimeout(uo.getTimeout()*1000);
			client.addFilter(new HTTPBasicAuthFilter(uo.getUserName(), uo.getPassword()));

			WebResource webResource = client.resource(uri);
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
	public static String callGetService(UserObject uo, String uri, HashMap<String, String> queryMap,
			String acceptString) {
		
		try {
                        
			DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
			if(uo.isStaging())
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
			client.setConnectTimeout(uo.getTimeout()*1000);
			client.setReadTimeout(uo.getTimeout()*1000);
			client.addFilter(new HTTPBasicAuthFilter(uo.getUserName(), uo.getPassword()));
			
			WebResource webResource = client.resource(uri);
			MultivaluedMap<String, String> params = new MultivaluedMapImpl();
			if (queryMap != null)
				for (String key : queryMap.keySet()){
					params.add(key, queryMap.get(key));
                                }
			ClientResponse response = webResource.queryParams(params)
					.accept(acceptString).get(ClientResponse.class);
			
			String responseText = response.getEntity(String.class);
			return responseText;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public static byte[] callGetFileService(UserObject uo,String uri, HashMap<String, String> queryMap,
			String acceptString) {
		
		try {
			DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
			if(uo.isStaging())
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
			client.setConnectTimeout(uo.getTimeout()*1000);
			client.setReadTimeout(uo.getTimeout()*1000);
			client.addFilter(new HTTPBasicAuthFilter(uo.getUserName(), uo.getPassword()));
			
			WebResource webResource = client.resource(uri);
			MultivaluedMap<String, String> params = new MultivaluedMapImpl();
			if (queryMap != null)
				for (String key : queryMap.keySet())
					params.add(key, queryMap.get(key));
			ClientResponse response = webResource.queryParams(params)
					.accept(acceptString).get(ClientResponse.class);
			
			byte[] responseText = response.getEntity(byte[].class);
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