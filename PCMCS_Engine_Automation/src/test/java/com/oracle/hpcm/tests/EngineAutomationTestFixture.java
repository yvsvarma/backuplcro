package com.oracle.hpcm.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oracle.hpcm.utils.CompareUtil;
import com.oracle.hpcm.utils.EPMAutomateUtility;
import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;
import com.oracle.hpcm.webservices.common.DeleteApplicationConsumer;
import com.oracle.hpcm.webservices.common.EssbaseLoadDataConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationsConsumer;
import com.oracle.hpcm.webservices.common.GetTaskStatusByProcessNameConsumer;
import com.oracle.hpcm.webservices.common.ProcessImportTemplateConsumer;
import com.oracle.hpcm.webservices.ml.MLDeployCubeConsumer;
import com.oracle.hpcm.webservices.ml.MLRuleBalanceConsumer;
import com.oracle.hpcm.webservices.ml.MLRunCalculationConsumer;



public class EngineAutomationTestFixture{
	public static Logger logger = Logger.getLogger("MLTestFixture");
	public int timeout;
	public String appName;
	public String modelName;
	
	@Parameters("applicationNameFromTestNG")
	@BeforeClass
	public void setUp(@Optional("") String applicationNameFromTestNG ) throws Exception {
		logger.log(Level.INFO, "***********Test Environment Setup***********");
		logger.log(Level.INFO,"Model name: "+modelName);
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		Properties prop = System.getProperties();
		//System.out.println(System.getProperty("domain"));
		PropertiesUtils.processGlobalProperties(prop);
		PropertiesUtils.processModelProperties(prop);
		appName = System.getProperty("application_name");
		if(applicationNameFromTestNG.equals(""))
			modelName =  System.getProperty("model_name");
		else{
			modelName = applicationNameFromTestNG;
			appName = modelName;
		}
		UserObject userCredentials = PropertiesUtils.getUserObject();
		timeout = PropertiesUtils.getTimeout();
		String user = userCredentials.getUserName();
		String password = userCredentials.getPassword();
		System.out.println(user + " "+ password);
		//BeginTest.processArgs(args
		/* delete all apps before*/
//		logger.info("Deleting all existing apps.");
		
		for (String appName : new GetApplicationsConsumer().getApplicationsNames()) {
			logger.info("Deleteing app "+appName);
			ResultObject ro = new DeleteApplicationConsumer().deleteApp(appName);
			Assert.assertTrue(ro.isResult(),"Deletion of app failed to create a deletion job.");
			GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
			Assert.assertTrue(getStatusObj.waitForJobToFinish(ro.getText(), timeout),"App deletion job failed to finish successfully.");
			
		}
		
	}

	
	@Test(priority=1,enabled=true)
	public void testImportApplication() throws Exception{
		boolean templateFileName = EPMAutomateUtility.uploadFileOverwrite("./models/"+modelName+"/", "template.zip","profitinbox");
		Assert.assertTrue(templateFileName,"File Transfer Assert.failed.");
		ProcessImportTemplateConsumer importAppObj = new ProcessImportTemplateConsumer();
		ResultObject result = importAppObj.importTemplate(appName, "Created by webservice automation.",
				"PROFITABILITY_WEB_APP", "EssbaseCluster-1",
				"Default Application Group",
				"template.zip", true);
		AssertJUnit.assertTrue(result.isResult());
		String jobName =  result.getText();
		Assert.assertNotNull(jobName);
		GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
		AssertJUnit.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));
		//Thread.sleep(180000);
	}
	@Test(groups = "MLDeployCube" ,priority = 2,enabled=true)
	public void testDeployCube() throws Exception {
		logger.log(Level.INFO,
				"***********testGetPOVTC1***********");
		MLDeployCubeConsumer WSObj = new MLDeployCubeConsumer();
		ResultObject result =  WSObj.runDeployCubeJob(appName, false, true, true,
				""); 
		String jobName = result.getText();
		AssertJUnit.assertTrue(result.isResult());
		Assert.assertNotNull(jobName);
		GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
		AssertJUnit.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));
	}
	@Test(priority = 3, description = "load Data",enabled=true)
	public void testLoadData() throws Exception {
		logger.log(Level.INFO,
				"***********testLoadData***********");
		if( new File("./models/"+modelName+"/inpdata.txt").exists()){
			EssbaseLoadDataConsumer wsObj = new EssbaseLoadDataConsumer();
			EPMAutomateUtility.uploadFileOverwrite("./models/"+modelName+"/", "inpdata.txt","profitinbox");
			//SFTPUtil.transfer(new File("./models/ML/inpdata.txt"));	
			ResultObject result = wsObj.loadFile(appName, new File("./models/"+modelName+"/inpdata.txt"), true, "OVERWRITE_EXISTING_VALUES","inpdata.txt", "");
			 String jobName  = result.getText();
			 Assert.assertTrue(result.isResult(),"EssbaseLoadData webservice returned error : "+result.getText());
			 Assert.assertNotNull(jobName);
			GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
			Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));
		}
	}
	
	@Test(groups = "MLRunCalculation" ,priority = 5, description = "Calculation",enabled=true)
	public void testRunCalcJob() throws Exception {
		logger.log(Level.INFO,"***********testRunCalcJob***********");
        Hashtable<String, String> PovMap     = new Hashtable<String, String>();
        Hashtable<String, String> PovMapPassed     = new Hashtable<String, String>();
        Hashtable<String, String> PovMapFailed   = new Hashtable<String, String>();
        JSONParser parser = new JSONParser();
        GetTaskStatusByProcessNameConsumer jobStatusObject = new GetTaskStatusByProcessNameConsumer();
        JSONObject jsonObject = (JSONObject)parser.parse(new FileReader("./models/"+modelName+"/POV"));
        JSONArray jArray =(JSONArray) jsonObject.get("POVArray");
        for(int i =0; i < jArray.size();i++){
            JSONObject pov = (JSONObject)jArray.get(i);
            MLRunCalculationConsumer WSObj = new MLRunCalculationConsumer();
            String stringDelimiter = "_";
            Object delimiter = pov.get("stringDelimiter");
            if(delimiter != null)
            	stringDelimiter = (String)delimiter;
            String POV = (String)pov.get("POV");
            POV=POV.replace(":", stringDelimiter);
            logger.info("Running Calculation for POV : "+POV);
    		ResultObject results =WSObj.runCalculation(appName, POV, true , true , true, "", "", "","", "ALL_RULES", stringDelimiter);
    		if(results.isResult())
    		{
    			String jobName = results.getText();
                logger.info("Calculation job have been started for "+POV+" : "+jobName);
                PovMap.put(POV,jobName);

            }else{
    			String error = results.getText();
                logger.warning("Calculation job have failed to start for "+POV+". Error returned : "+error);
            }
        

        	int attemptsCount = (PovMap.size()+1)*80;
            int run_calc_timeout = PropertiesUtils.getTimeout()*(PovMap.size()+1);
            int waitPerAttempt = run_calc_timeout /attemptsCount;
            boolean AllJobStatus = false;
            int checkAttempts = 0;
            logger.info("Run Calculation Timeout(Seconds): "+run_calc_timeout);
            do{
            	 
            	Thread.sleep(waitPerAttempt*1000);          	  
            	checkAttempts++;
                logger.info("Checking for Calulation job statuses. Iteration No: "+checkAttempts);
                if(PovMap.isEmpty()){
                    AllJobStatus=true;
                }
                Enumeration<String> povNamesEnum= PovMap.keys();
                while(povNamesEnum.hasMoreElements()){
                    String currentPov = povNamesEnum.nextElement();
                    String jobName=PovMap.get(currentPov);
                    

                    boolean jobStatus = jobStatusObject.isJobActive(jobName);
                    if(jobStatus)
                        continue;
                    else{
                    	if(jobStatusObject.checkFailureInSubtasks()){
	                        PovMapPassed.put(currentPov,jobName );
	                        PovMap.remove(currentPov);
	                        logger.info("Checking for Calulation job id "+ jobName + ",  Status : PASS");
                    	}else{
                    		 
                                 PovMapFailed.put(currentPov,jobName );
                                 PovMap.remove(currentPov);
                                 logger.severe("Checking for Calulation job id "+ jobName + ", Status : Failed");
                    	}
                    }
                   
                }
                 if(PovMap.isEmpty())
                        {
                            AllJobStatus=true;
                            break;
                        }
            }while(!AllJobStatus && checkAttempts <= attemptsCount);
        if(checkAttempts>attemptsCount){
        	logger.severe(" Timeout for run calculation exceeded!!!!!!!!!!");
        }
        if(PovMapPassed.isEmpty()){
             logger.severe(" All calculation jobs have failed.");
        }
        if(!PovMapFailed.isEmpty())
            logger.warning(" Some calculation jobs have failed.");
        if(PovMapFailed.isEmpty())
            logger.info(" All calculation jobs have passed.");
        Assert.assertTrue(PovMapFailed.isEmpty(),"Some calculation Job have failed");
		}	
}
	
	@Test(groups = "ProcessRuleBalancing" ,priority = 6, description = "Target Webservice = ProcessRuleBalancing, assert that ML rule balancing is returned.")
	public void testGetRuleBalancing() throws Exception {
		logger.log(Level.INFO,
				"***********testGetRuleBalancing***********");
        Hashtable<String, String> PovMapPassed     = new Hashtable<String, String>();
        Hashtable<String, String> PovMapFailed    = new Hashtable<String, String>();
        JSONParser parser = new JSONParser();
       // MLCheckJobStatusConsumer jobStatusObject = new MLCheckJobStatusConsumer();
		 
        JSONObject jsonObject = (JSONObject)parser.parse(new FileReader("./models/"+modelName+"/POV"));

        JSONArray jArray =(JSONArray) jsonObject.get("POVArray");
        for(int i=0; i < jArray.size();i++){
            
            
            JSONObject pov = (JSONObject)jArray.get(i);

       
            String stringDelimiter = "_";
            Object delimiter = pov.get("stringDelimiter");
            if(delimiter != null)
            	stringDelimiter = (String)delimiter;
            String POV = (String)pov.get("POV");
            POV=POV.replace(":", stringDelimiter);
            logger.info(" Checking Rule Balancing for POV : "+POV);
            MLRuleBalanceConsumer ruleBalanceConsumerObj = new MLRuleBalanceConsumer();
            ItemsObject ruleBalanceingItem = ruleBalanceConsumerObj.getRuleBalancing(appName, POV , "",stringDelimiter);
            if(!ruleBalanceingItem.isResult()){
            	logger.warning("Getting Rule balance for POV "+POV +" failed with following error."+ruleBalanceingItem.getText());
            }
            String ruleBalanceingText = ruleBalanceingItem.getText();
            if(ruleBalanceingText==null)
            {
                logger.warning(" Retrieval of Rule Balancing failed for POV: "+POV);
                PovMapFailed.put(POV,"");
                continue;
            }
            else{
            	
            	String itemsText = ruleBalanceingText;//.substring(ruleBalanceingText.indexOf("items\":")+7, ruleBalanceingText.indexOf(",\"status\""));
            	logger.info("Actual Rule Balancing: "+itemsText);
            	String ruleBalancingFilePath = "./logs/"+POV+".txt";
			File ruleBalanceFile = new File(ruleBalancingFilePath);
			FileOutputStream opfile = new FileOutputStream(ruleBalanceFile);
        		opfile.write(itemsText.getBytes());
        		opfile.close();
        		logger.info("Expected rule balancing : "+FileUtils.readFileToString(new File("./models/"+modelName+"/balancing/"+POV+".txt")));
         		if(CompareUtil.compareJSONRuleBalancing("./models/"+modelName+"/balancing/"+POV+".txt", ruleBalancingFilePath))
        		{
        			 PovMapPassed.put(POV,"");
        			 logger.info(" Rule Balancing compare passed for POV: "+POV);
        		}else{
        			 PovMapFailed.put(POV,"");
        			 logger.warning("[checkRuleBalancing] Rule Balancing compare failed for POV: "+POV);
        		}
        		
            }
            
        }
        if(PovMapPassed.isEmpty()){
        	 logger.severe("[checkRuleBalancing] All rule balancing comparisons have failed.");
        }
        if(!PovMapFailed.isEmpty())
        	logger.warning("[checkRuleBalancing] Some rule balancing comparisons failed.");
        else
        	logger.info("[checkRuleBalancing] All rule balancing comparisons have passed.");
        Assert.assertTrue(PovMapFailed.isEmpty(),"Some rule balancing compares have failed.");
	}
@Test(groups = "deleteApplication" ,priority = 7,enabled=false)
	public void testDeleteAppPost() throws Exception {
		logger.log(Level.INFO,
				"***********testDeleteAppsPost***********");
		ResultObject ro = new DeleteApplicationConsumer().deleteApp(appName);
		Assert.assertTrue(ro.isResult(),"Deletion of app failed to create a deletion job.");
		GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
		Assert.assertTrue(getStatusObj.waitForJobToFinish(ro.getText(), timeout),"App deletion job failed to finish successfully.");
		//Thread.sleep(180000);
	
	}
	
		
@AfterMethod
	public void afterMethod(ITestResult result){
		if(result.isSuccess()){
			logger.info("Test case " + result.getMethod().getMethodName() + " is a pass.");
		}else{
			if(result.getStatus()==ITestResult.FAILURE){
				logger.severe("Test case " + result.getMethod().getMethodName() + " is a fail.");
				//Throwable ex = result.getThrowable();
				//logger.log(Level.SEVERE,"Exception : "+ex.getMessage());
			}
			//logger.info("Test case " + result.getMethod().getMethodName() + " is a pass.");
		}
	}
}
