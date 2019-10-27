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
import java.io.IOException;
import org.testng.Reporter;
import com.oracle.hpcm.utils.Executor;
import com.oracle.hpcm.utils.ReturnObject;


public class EngineAutomationTestFixture{
	public static Logger logger = Logger.getLogger("MLTestFixture");
	public int timeout;
	public String appName;
	public String modelName;
        static boolean staging;
        //private static final String SNAPSHOT_FILENAME = "BksML30.zip";
        private static final String SNAPSHOT_FILENAME = System.getProperty("snapshot.filename");

        private static final String PATH_TO_TEST_DATA = "./models/";
        private static final String PATH_TO_SNAPSHOTS = PATH_TO_TEST_DATA + "snapshots/";
        private String url;
        private String epmautomateurl;
	private String password;
	private String user;
      	private String domain;
        static ReturnObject ro;


               

	
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
		staging = Boolean.getBoolean(System.getProperty("Staging"));
                UserObject userCredentials = PropertiesUtils.getUserObject();
		timeout = PropertiesUtils.getTimeout();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
                epmautomateurl = PropertiesUtils.getEpmAutomateUrl();
                url = PropertiesUtils.getWebServiceURL();
                domain = System.getProperty("domain");
      		System.out.println(user + " "+ password);


                
                

/*Deleting existing app*/
		
		for (String appName : new GetApplicationsConsumer().getApplicationsNames()) {
			logger.info("Deleteing app "+appName);
			ResultObject ro = new DeleteApplicationConsumer().deleteApp(appName);
			Assert.assertTrue(ro.isResult(),"Deletion of app failed to create a deletion job.");
			GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
			Assert.assertTrue(getStatusObj.waitForJobToFinish(ro.getText(), timeout),"App deletion job failed to finish successfully.");
			
		}
		
	}

    @Test(priority = 1)
    public void testLogin() throws IOException, InterruptedException {
        //Executor.runCommand("cd "+pathToEPMAutomate);
        Reporter.log("------------EPMAutomate Version------------", true);
        //Executor.runCommand(String.format("epmautomate"));
        Reporter.log("------------EPMAutomate Version------------", true);
        if (!staging) {
            Executor.runCommand(String.format("epmautomate login %s %s %s", user, password, epmautomateurl));
        } else {
            Executor.runCommand(String.format("epmautomate login %s %s %s %s", user, password, epmautomateurl, domain));
        }
    }

    @Test(priority = 2)
    public void testLCMImportFileUpload() throws IOException, InterruptedException {
	logger.log(Level.INFO, "Uploading snapshot file");
        Executor.runCommand("epmautomate deletefile " + SNAPSHOT_FILENAME.replace(".zip", ""));
        File template = new File(PATH_TO_SNAPSHOTS + SNAPSHOT_FILENAME);
        String templatePath = template.getAbsolutePath();
        ro = Executor.runCommand("epmautomate uploadfile " + templatePath);
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM File upload failed. Error: " + ro.getOutput());
    }

    @Test(priority = 3)
    public void testLCMImport() throws IOException, InterruptedException {
        logger.log(Level.INFO, "Importing applicaiton");
        ro = Executor.runCommand("epmautomate importsnapshot " + SNAPSHOT_FILENAME.replace(".zip", ""));
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM Import failed. Error: " + ro.getOutput());
    }

        
        /*
	@Test(priority=1,enabled=true)
	public void testImportApplication() throws Exception{
		boolean templateFileName = EPMAutomateUtility.uploadFileOverwrite("./models/"+modelName+"/", "template.zip","profitinbox");
		Assert.assertTrue(templateFileName,"File Transfer Assert.failed.");
		ProcessImportTemplateConsumer importAppObj = new ProcessImportTemplateConsumer();
		ResultObject result = importAppObj.importTemplate(appName, "To verify Express Calculation by Automation",
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
            String dataPOV = (String)pov.get("dataPOV");
            String modelPOV = (String)pov.get("modelPOV");
            dataPOV=dataPOV.replace("_", stringDelimiter);
            modelPOV=modelPOV.replace("_", stringDelimiter);

            logger.info("Running Calculation for Data POV : "+dataPOV+" and running on "+modelPOV+" model POV");
                //Thread.sleep(5000);
                //System.out.println("waiting 5sec before triggering next calculation");
    		ResultObject results =WSObj.runCalculation(appName, dataPOV, true , true , true, "", "", "","", "ALL_RULES",modelPOV, stringDelimiter);
    		if(results.isResult())
    		{
    			String jobName = results.getText();
                                    logger.info("Calculation job started for Data POV : "+dataPOV+" and running on "+modelPOV+" model POV");
                    PovMap.put(dataPOV,jobName);

            }else{
    			String error = results.getText();
                logger.warning("Calculation job have failed to start for "+dataPOV+". Error returned : "+error);
            }
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
            String dataPOV = (String)pov.get("dataPOV");
            //String modelPOV = (String)pov.get("modelPOV");
            //POV=POV.replace(":", stringDelimiter);
            logger.info(" Checking Rule Balancing for POV : "+dataPOV);
            MLRuleBalanceConsumer ruleBalanceConsumerObj = new MLRuleBalanceConsumer();
            ItemsObject ruleBalanceingItem = ruleBalanceConsumerObj.getRuleBalancing(appName, dataPOV , "",stringDelimiter);
            if(!ruleBalanceingItem.isResult()){
            	logger.warning("Getting Rule balance for POV "+dataPOV +" failed with following error."+ruleBalanceingItem.getText());
            }
            String ruleBalanceingText = ruleBalanceingItem.getText();
            if(ruleBalanceingText==null)
            {
                logger.warning(" Retrieval of Rule Balancing failed for POV: "+dataPOV);
                PovMapFailed.put(dataPOV,"");
                continue;
            }
            else{
            	
            	String itemsText = ruleBalanceingText;//.substring(ruleBalanceingText.indexOf("items\":")+7, ruleBalanceingText.indexOf(",\"status\""));
            	//logger.info("Actual Rule Balancing: "+itemsText);
            	String ruleBalancingFilePath = "./logs/"+dataPOV+".txt";
                    logger.info("Actual Rule Balancing is placed at -"+ruleBalancingFilePath);
			File ruleBalanceFile = new File(ruleBalancingFilePath);
			FileOutputStream opfile = new FileOutputStream(ruleBalanceFile);
        		opfile.write(itemsText.getBytes());
        		opfile.close();
        		logger.info("Expected rule balancing is taken from: /models/"+modelName+"/balancing/"+dataPOV+".txt");
         		if(CompareUtil.compareJSONRuleBalancing("./models/"+modelName+"/balancing/"+dataPOV+".txt", ruleBalancingFilePath))
        		{
        			 PovMapPassed.put(dataPOV,"");
        			 logger.info(" Rule Balancing compare passed for POV: "+dataPOV);
        		}else{
        			 PovMapFailed.put(dataPOV,"");
        			 logger.warning("[checkRuleBalancing] Rule Balancing compare failed for POV: "+dataPOV);
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
/*	
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
	*/
		
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
