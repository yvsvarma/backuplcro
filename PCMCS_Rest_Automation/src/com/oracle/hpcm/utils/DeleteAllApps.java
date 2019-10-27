package com.oracle.hpcm.utils;

import java.util.logging.Logger;

import com.oracle.hpcm.webservices.common.DeleteApplicationConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationsConsumer;

public class DeleteAllApps {
	public static Logger logger = Logger.getLogger("delete"); 
public static void main(String[] args) {
	System.setProperty("user", args[3]);
	System.setProperty("password", args[4]);
	System.setProperty("server", args[0]);
	System.setProperty("port", args[1]);
	System.setProperty("version", args[2]);
	System.setProperty("deleteport", "9000");
	GetApplicationsConsumer getAppWSObj = new GetApplicationsConsumer();	
	DeleteApplicationConsumer WSObj = new DeleteApplicationConsumer();
	try {
		String[] appNames = getAppWSObj.getApplicationsNames();
		for(String app : appNames){
			if(app!=null )
			{
				String text = WSObj.deleteApp(app).getText();
				logger.info("\nstatus: "
					+ text);
			}
		}

	} catch (Exception E) {
		logger.severe("Encountered exception " + E.getMessage());
		E.printStackTrace();
	}
}
}
