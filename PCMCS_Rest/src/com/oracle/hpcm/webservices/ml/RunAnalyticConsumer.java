package com.oracle.hpcm.webservices.ml;


import java.util.HashMap;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;

import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.JAXRestClient;
import com.oracle.hpcm.utils.JSONResultParser;
import com.oracle.hpcm.utils.UserObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;


/*http://{HostName}/profitability/rest/"+System.getProperty("version")+"/applications/{applicationName}/povs
 * 
 */
public class RunAnalyticConsumer {
	private String url;
	private UserObject uo;
	private String response;
	public static Logger logger = Logger.getLogger("runAnalytic");
	public RunAnalyticConsumer(UserObject uo) {
		this.uo =uo;
		url = uo.getWebServiceURL()+"/applications/";
	}

	public ItemsObject runItem(String appName,String name,String type) throws ParseException {
            try {
                String uri = URLEncoder.encode("{\"analyticItemName\":\""+name+"\",\"analyticItemType\":\""+type+"\"}", "UTF-8");
                String analyticURL= url + appName
                        + "/analytics/runAnalyticItem?queryParameter=" +uri;
                HashMap<String,String> queryMap = new HashMap<String, String>();
                
                this.response = JAXRestClient.callGetService(uo, analyticURL,queryMap,"application/json");
                logger.info("Request URL: "+analyticURL);
                logger.finest("Response: "+this.response);
                JSONResultParser result = new JSONResultParser(this.response);
                return result.getItemsObject();
            } catch (UnsupportedEncodingException ex) {
               logger.log(Level.SEVERE, null, ex);
            }
            return null;
	}

	
}
