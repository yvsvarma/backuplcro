package com.oracle.hpcm.utils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//import com.meterware.httpunit.Base64;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author mbanchho
 */
public class EPMAutomateUtility {

    private static String responseText = "";

    /**
     *
     */
    public static Logger logger = Logger.getLogger("epmautomate");

    /**
     *
     */
    public static Base64 encoder = new Base64();

    /**
     *
     * @param uo
     * @param extDirPath
     * @param fileName
     * @return
     * @throws Exception
     */
    public static byte[] getFileContents(UserObject uo, String extDirPath, String fileName) throws Exception {

        UserObject userCred = uo;
        String url;
        if (extDirPath != null) {
            url = userCred.getInteropWebServiceURL() + "/applicationsnapshots/" + URLEncoder.encode(extDirPath + "/" + fileName, "UTF-8") + "/contents";
        } else {
            url = userCred.getInteropWebServiceURL() + "/applicationsnapshots/" + URLEncoder.encode(fileName, "UTF-8") + "/contents";
        }
        String response = JAXRestClient.callGetService(uo, url, null, "*/*");
        //System.out.println(response);
        //logger.log(Level.INFO,"response :"+response);
        /*JSONObject result = (JSONObject)new JSONParser().parse(response);
		JSONArray linkArray = (JSONArray)result.get("links");
		JSONObject link = (JSONObject)linkArray.get(1);
		String downloadURL = (String)link.get("href")+"/contents";*/
        logger.info("Download url : " + url);
        byte[] downloadedFile = JAXRestClient.callGetFileService(uo, url, null, "*/*");
        return downloadedFile;
    }

//
// BEGIN - Download a file from PBCS
//
    public static void downloadFile(UserObject uo, String path, String fileName) throws Exception {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            URL url = new URL(uo.getInteropWebServiceURL() + String.format("/applicationsnapshots/%s/contents", fileName));
            logger.info("Download url: " + url.getPath());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(false);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoder.encodeToString((uo.getUserName() + ":" + uo.getPassword()).getBytes()));
            connection.setRequestProperty("Content-Type", "multipart/form-data");
            int status = connection.getResponseCode();
            if (status == 200) {
                if (connection.getContentType() != null && connection.getContentType().equals("application/json")) {
                    org.json.JSONObject json = new org.json.JSONObject(getStringFromInputStream(connection.getInputStream()));
                    System.out.println("Error downloading file : " + json.getString("details"));
                } else {
                    inputStream = connection.getInputStream();
                    
                    outputStream = new FileOutputStream(new File(path + fileName));
                    byte[] b = new byte[4096];
                    IOUtils.copyLarge(inputStream, outputStream);
//                    int count;
//                    while ((count = inputStream.read(b)) >= 0) {
//                        outputStream.write(b, 0, count);
//                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                    System.out.println("File download completed.");
                }
            } else {
                throw new Exception("Http status code: " + status);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     *
     * @param uo
     * @param filePath
     * @param fileName
     * @param remoteFolder
     * @return
     * @throws Exception
     */
    public static boolean uploadFileOverwrite(UserObject uo, String filePath, String fileName, String remoteFolder) throws Exception {
        if (doesFileExists(uo, remoteFolder, fileName)) {
            logger.info("File " + fileName + " exists.");
            deleteFile(uo, remoteFolder, fileName);
        }
        if (uploadFile(uo, filePath, fileName, remoteFolder) == false) {
            logger.severe("upload of the file " + fileName + " failed.");
            return false;
        } else {
            logger.info("upload of the file " + fileName + " completed.");
            return true;
        }

    }

    /**
     *
     * @param Userobject
     * @param remoteFolder
     * @param fileName
     * @return
     * @throws ParseException
     */
    public static boolean doesFileExists(UserObject uo, String remoteFolder, String fileName) throws ParseException {
        if (remoteFolder != null) {
            logger.info("Searching for " + remoteFolder + "/" + fileName);
            //String fullFileName = remoteFolder+"/"+fileName;
            for (String file : listFiles(uo)) {
                //logger.info(file);
                if (file.trim().contains(remoteFolder) && file.contains(fileName)) {
                    return true;
                }
            }
        } else {
            logger.info("Searching for " + fileName);
            for (String file : listFiles(uo)) {
                //logger.info(file);
                if (file.contains(fileName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *
     * @param UserObject
     * @return
     * @throws ParseException
     */
    public static String[] listFiles(UserObject uo) throws ParseException {

        UserObject userCred = uo;
        String url = userCred.getInteropWebServiceURL() + "/applicationsnapshots";
        logger.info(url);
        String response = JAXRestClient.callGetService(uo, url, null, "*/*");
        //System.out.println(response);
        logger.log(Level.INFO, "response :" + response);
        JSONResultParser result = new JSONResultParser(response, "");
        ItemsObject items = result.getItemsObject();
        if (!items.isResult()) {
            return null;
        }
        logger.info(items.getText());
        JSONParser parser = new JSONParser();
        JSONArray appArray = (JSONArray) parser.parse(items.getText());
        String[] appNameArray = new String[appArray.size()];
        for (int i = 0; i < appArray.size(); i++) {
            appNameArray[i] = (String) ((JSONObject) appArray.get(i))
                    .get("name");
            logger.finest("->" + appNameArray[i]);
        }
        logger.info("Listed files...");
        return appNameArray;

    }

    /**
     *
     * @param uo
     * @param extDir
     * @param fileName
     * @return
     * @throws ParseException
     */
    public static ResultObject deleteFile(UserObject uo, String extDir, String fileName) throws ParseException {
        String url;
        if (extDir != null) {
            url = uo.getInteropWebServiceURL() + "/applicationsnapshots/" + extDir + "%5C" + fileName;
        } else {
            url = uo.getInteropWebServiceURL() + "/applicationsnapshots/" + fileName;
        }
        logger.info(url);
        String response = JAXRestClient.callDeleteService(uo, url, null);
        logger.log(Level.INFO, "response :" + response);
        JSONResultParser result = new JSONResultParser(response, "");
        return result.getResultObject();

    }

    /**
     *
     * @param uo
     * @param localPath
     * @param fileName
     * @param remotePath
     * @return
     * @throws Exception
     */
    public static boolean uploadFile(UserObject uo, String localPath, String fileName, String remotePath) throws Exception {
        final int DEFAULT_CHUNK_SIZE = 50 * 1024 * 1024;
        InputStream fis = null;
        byte[] lastChunk = null;
        long totalFileSize = new File(localPath + "/" + fileName).length(), totalbytesRead = 0;
        boolean isLast = false, status = true;
        Boolean isFirst = true;
        int packetNo = 1, lastPacketNo = (int) (Math.ceil(totalFileSize / (double) DEFAULT_CHUNK_SIZE));
        try {
            fis = new BufferedInputStream(new FileInputStream(localPath + "/" + fileName));
            while (totalbytesRead < totalFileSize && status) {
                int nextChunkSize = (int) Math.min(DEFAULT_CHUNK_SIZE, totalFileSize - totalbytesRead);
                if (lastChunk == null) {
                    lastChunk = new byte[nextChunkSize];
                    totalbytesRead += fis.read(lastChunk);
                    if (packetNo == lastPacketNo) {
                        isLast = true;
                    }
                    status = sendFileContents(uo, isFirst, isLast, lastChunk, remotePath, fileName);
                    isFirst = false;
                    packetNo = packetNo + 1;
                    lastChunk = null;
                }
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return true;
    }

    private static boolean sendFileContents(UserObject uo, Boolean isFirst, boolean isLast, byte[] lastChunk, String extDirPath, String fileName) throws Exception {
        HttpURLConnection connection = null;
        UserObject userCred = uo;
        URL url;
        try {
            if (extDirPath != null) {
                url = new URL(String.format(userCred.getInteropWebServiceURL() + "/applicationsnapshots/%s/contents?q={\"extDirPath\":\"%s\",\"chunkSize\":%d,\"isFirst\":%b,\"isLast\":%b,\"chunkNo\":%d}", fileName, extDirPath, lastChunk.length, isFirst, isLast, 1));
            } else {
                url = new URL(String.format(userCred.getInteropWebServiceURL() + "/applicationsnapshots/%s/contents?q={\"chunkSize\":%d,\"isFirst\":%b,\"isLast\":%b,\"chunkNo\":%d}", fileName, lastChunk.length, isFirst, isLast, 1));
            }
            //System.out.println(url.toURI().toASCIIString());
            connection = (HttpURLConnection) url.openConnection();
            String auth = encoder.encodeToString((uo.getUserName() + ":" + uo.getPassword()).getBytes());
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setRequestProperty("Authorization", "Basic " + auth);
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
            responseText = status;
            if (statusCode == 200 && status != null) {
                isFirst = false;
                return true;
            } else {
                return false;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String executeRequest(UserObject uo, String urlString, String requestMethod, String payload, String contentType) throws Exception {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setInstanceFollowRedirects(false);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoder.encodeToString((uo.getUserName() + ":" + uo.getPassword()).getBytes()));
            connection.setRequestProperty("Content-Type", contentType);
            if (payload != null) {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(payload);
                writer.flush();
            }
            int status = connection.getResponseCode();
            if (status == 200 || status == 201) {
                return getStringFromInputStream(connection.getInputStream());
            }
            throw new Exception("Http status code: " + status);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
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

    private void getJobStatus(UserObject uo, String pingUrlString, String methodType) throws Exception {
        boolean completed = false;
        while (!completed) {
            String pingResponse = executeRequest(uo, pingUrlString, methodType, null, "application/x-www-form-urlencoded");
            org.json.JSONObject json = new org.json.JSONObject(pingResponse);
            int status = json.getInt("status");
            if (status == -1) {
                try {
                    System.out.println("Please wait...");
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    completed = true;
                    throw e;
                }
            } else {
                if (status > 0) {
                    System.out.println("Error occurred: " + json.getString("details"));
                } else {
                    System.out.println("Completed");
                }
                completed = true;
            }
        }
    }

    private static void getMigrationJobStatus(UserObject uo, String pingUrlString, String methodType) throws Exception {
        boolean completed = false;
        while (!completed) {
            String pingResponse = executeRequest(uo, pingUrlString, methodType, null, "application/x-www-form-urlencoded");
            org.json.JSONObject json = new org.json.JSONObject(pingResponse);
            int status = json.getInt("status");
            if (status == -1) {
                try {
                    System.out.println("Please wait...");
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    completed = true;
                    throw e;
                }
            } else {
                if (status == 1) {
                    System.out.println("Error occured");
                    org.json.JSONArray itemsArray = json.getJSONArray("items");
                    org.json.JSONObject jObj = null;
                    if (itemsArray.length() <= 0) {
                        System.out.println(json.getString("details"));
                    } else {
                        for (int i = 0; i < itemsArray.length(); i++) {
                            jObj = (org.json.JSONObject) itemsArray.get(i);
                            String source = jObj.getString("source");
                            String destination = jObj.getString("destination");
                            String taskURL = null;
                            org.json.JSONArray lArray = jObj.getJSONArray("links");
                            for (int j = 0; j < lArray.length(); j++) {
                                org.json.JSONObject arr = lArray.getJSONObject(j);
                                if (!org.json.JSONObject.NULL.equals(arr) && !org.json.JSONObject.NULL.equals(arr.get("rel")) && arr.get("rel").equals("Job Details")) {
                                    taskURL = (String) arr.get("href");
                                    break;
                                }
                            }
                            System.out.println("Details:");
                            System.out.println("Source: " + source);
                            System.out.println("Destination: " + destination);
                            boolean errorsCompleted = false;
                            String currentMessageCategory = "";
                            String nextPingURL = taskURL;
                            while (!errorsCompleted) {
                                String nextPingResponse = executeRequest(uo, nextPingURL, "GET", null, "application/x-www-form-urlencoded");
                                org.json.JSONObject jsonObj = new org.json.JSONObject(nextPingResponse);
                                int status1 = jsonObj.getInt("status");
                                if (status1 == 0) {
                                    org.json.JSONArray artifactArray = jsonObj.getJSONArray("items");
                                    org.json.JSONObject jRes = null;
                                    for (int k = 0; k < artifactArray.length(); k++) {
                                        jRes = (org.json.JSONObject) artifactArray.get(k);
                                        String artifact = jRes.getString("artifact").toString();
                                        String msgCategory = jRes.getString("msgCategory").toString();
                                        String msgText = jRes.getString("msgText").toString();
                                        if (currentMessageCategory.isEmpty() || !currentMessageCategory.equals(msgCategory)) {
                                            currentMessageCategory = msgCategory;
                                            System.out.println(currentMessageCategory);
                                        }
                                        System.out.println(artifact + " - " + msgText);
                                    }
                                    nextPingURL = "";
                                    org.json.JSONArray nextLinks = jsonObj.getJSONArray("links");
                                    for (int j = 0; j < nextLinks.length(); j++) {
                                        org.json.JSONObject nextArray = nextLinks.getJSONObject(j);
                                        if (!org.json.JSONObject.NULL.equals(nextArray) && !org.json.JSONObject.NULL.equals(nextArray.get("rel")) && nextArray.get("rel").equals("next")) {
                                            nextPingURL = (String) nextArray.get("href");
                                            break;
                                        }
                                    }
                                    if (nextPingURL.isEmpty()) {
                                        errorsCompleted = true;
                                    }
                                } else if (status1 > 0) {
                                    System.out.println("Error occured while fetching error details: " + jsonObj.getString("details"));
                                    errorsCompleted = true;
                                }
                            }
                        }
                    }
                } else if (status == 0) {
                    System.out.println("Completed");
                }
                completed = true;
            }
        }
    }

    /**
     *
     * @param response
     * @param retValue
     * @return
     * @throws Exception
     */
    public static String fetchPingUrlFromResponse(String response, String retValue) throws Exception {
        String pingUrlString = null;
        org.json.JSONObject jsonObj = new org.json.JSONObject(response);
        int resStatus = jsonObj.getInt("status");
        if (resStatus == -1) {
            org.json.JSONArray lArray = jsonObj.getJSONArray("links");
            for (int i = 0; i < lArray.length(); i++) {
                org.json.JSONObject arr = lArray.getJSONObject(i);
                if (arr.get("rel").equals(retValue)) {
                    pingUrlString = (String) arr.get("href");
                }
            }
        }
        return pingUrlString;
    }

    /**
     *
     * @param uo
     * @param applicationSnapshotName
     * @throws Exception
     */
    public static void importSnapshot(UserObject uo, String applicationSnapshotName) throws Exception {
        org.json.JSONObject params = new org.json.JSONObject();
        params.put("type", "import");
        String urlString = String.format("%s/applicationsnapshots/%s/migration?q=%s", uo.getInteropWebServiceURL(), applicationSnapshotName, params.toString());
        String response = executeRequest(uo, urlString, "POST", null, "application/x-www-form-urlencoded");
        System.out.println("Import started successfully");
        getMigrationJobStatus(uo, fetchPingUrlFromResponse(response, "Job Status"), "POST");
    }

    /**
     *
     * @param uo
     * @param applicationSnapshotName
     * @throws Exception
     */
    public static void exportSnapshot(UserObject uo, String applicationSnapshotName) throws Exception {
        org.json.JSONObject params = new org.json.JSONObject();
        params.put("type", "export");
        String urlString = String.format("%s/applicationsnapshots/%s/migration?q=%s", uo.getInteropWebServiceURL(), applicationSnapshotName, params.toString());
        String response = executeRequest(uo, urlString, "POST", null, "application/x-www-form-urlencoded");
        System.out.println("Export started successfully");
        getMigrationJobStatus(uo, fetchPingUrlFromResponse(response, "Job Status"), "POST");
    }

    /**
     *
     * @param uo
     * @param description
     * @throws Exception
     */
    public void provideFeedback(UserObject uo, String description) throws Exception {
        org.json.JSONObject params = new org.json.JSONObject();
        org.json.JSONObject config = new org.json.JSONObject();
        config.put("URL", uo.getInteropWebServiceURL());
        params.put("configuration", config);
        params.put("description", description);

        String urlString = String.format("%s/feedback", uo.getInteropWebServiceURL());
        String response = executeRequest(uo, urlString, "POST", params.toString(), "application/json");
        org.json.JSONObject json = new org.json.JSONObject(response);
        int resStatus = json.getInt("status");
        if (resStatus == 0) {
            System.out.println("Feedback successful");
        } else {
            System.out.println("Error occured: " + json.getString("details"));
        }
    }

}
