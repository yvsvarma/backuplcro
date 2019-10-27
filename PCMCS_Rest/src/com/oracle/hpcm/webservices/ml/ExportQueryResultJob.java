package com.oracle.hpcm.webservices.ml;

import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.UserObject;

public class ExportQueryResultJob {
	private String url;
	private UserObject uo;
	private String response;
	public static Logger logger = Logger.getLogger("runAnalytic");
	public ExportQueryResultJob(UserObject uo) {
		this.uo =uo;
		url = uo.getWebServiceURL() + "/";
	}

	public ItemsObject exportQuery(String appName,String queryName,String fileName, boolean exportOnlyLevel0Flg) throws ParseException {
		String queryExportURI= url + "applications/"+appName
				+ "/jobs/exportQueryResultsJob/";
		HashMap<String,String> queryMap = new HashMap<String, String>();
		queryMap.put("queryName",queryName);
		queryMap.put("fileName", fileName);
		queryMap.put("exportOnlyLevel0Flg", ""+exportOnlyLevel0Flg);
		this.response = JAXRestClient.callPostService(uo, queryExportURI,queryMap);
		logger.info("Request URL: "+queryExportURI);
		logger.info("Response: "+this.response);
		JSONResultParser result = new JSONResultParser(this.response);
		return result.getItemsObject();
	}
}
