package com.oracle.hpcm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

import com.oracle.hpcm.restclient.ProfitabilityRestResponse;

public class RestClient {
	public static ProfitabilityRestResponse executeRequest(String urlString, String requestMethod, String payload,
			String contentType,String userName, String password, boolean isSecure) throws Exception {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlString);
			if(!isSecure)
				connection = (HttpURLConnection) url.openConnection();
			else
				connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod(requestMethod);
			connection.setInstanceFollowRedirects(false);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Authorization",
					"Basic " + Base64.encodeBase64("Test".getBytes(userName + ":" + password)));
			connection.setRequestProperty("Content-Type", contentType);
			if (payload != null) {
				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
				writer.write(payload);
				writer.flush();
			}

			int status = connection.getResponseCode();

			if (status == 200) {
				String repsonse =  getStringFromInputStream(connection.getInputStream());
				
			} else {
				System.out.println("Error occured while executing request");
				System.out.println("Response error code : " + status);
				String responseStr = getStringFromInputStream(connection.getErrorStream());
				return null;
			}
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return null;
	}
private static String getStringFromInputStream(InputStream is) {
         BufferedReader br = null;
         StringBuilder sb = new StringBuilder();
         String line;
         
         try {
                 br = new BufferedReader(new InputStreamReader(is));
                 while ((line = br.readLine()) != null) {
                         sb.append(line);
                 }
         } catch (IOException e) {
                 e.printStackTrace();
         } finally {
                 if (br != null) {
                         try {
                                 br.close();
                         } catch (IOException e) {
                                 e.printStackTrace();
                         }
                 }
         }
         
         return sb.toString();
 }
}
