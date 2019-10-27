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
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;


public class EngineAutomationTestFixture{
	public static Logger logger = Logger.getLogger("MLTestFixture");
	public int timeout;
	public String appName;
	public String modelName;
        static boolean staging;
        private static final String SNAPSHOT_FILENAME = System.getProperty("snapShotName");
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
		String stageVar = System.getProperty("staging");
		System.out.println("Debug1:"+ stageVar);
		if(stageVar.equalsIgnoreCase("true")) {
		staging = true;
		}
                UserObject userCredentials = PropertiesUtils.getUserObject();
		timeout = PropertiesUtils.getTimeout();
		user = userCredentials.getUserName();
		password = userCredentials.getPassword();
                epmautomateurl = PropertiesUtils.getEpmAutomateUrl();
                url = PropertiesUtils.getWebServiceURL();
                domain = System.getProperty("domain");
      		System.out.println(user + " "+ password);
      		System.out.println("Here**** "+staging + " "+ domain);

        }

                
                

/*Deleting existing app*/
 
        
	@Test(priority = 1)
public void deleteExistingApp() throws IOException, SAXException, ParseException{
    	for (String appName : new GetApplicationsConsumer().getApplicationsNames()) {
			logger.info("Deleteing app "+appName);
			ResultObject ro = new DeleteApplicationConsumer().deleteApp(appName);
			Assert.assertTrue(ro.isResult(),"Deletion of app failed to create a deletion job.");
			GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
			Assert.assertTrue(getStatusObj.waitForJobToFinish(ro.getText(), timeout),"App deletion job failed to finish successfully.");
			
		}
}
	
        
        
        
    //@Parameters({ "user", "password" })
    @Test(priority = 2)
    public void login() throws IOException, InterruptedException {
        //Executor.runCommand("cd "+pathToEPMAutomate);
        if (!staging) {
            Executor.runCommand(String.format("epmautomate login %s %s %s", user, password, epmautomateurl));
        } else {
            Executor.runCommand(String.format("epmautomate login %s %s %s %s", user, password, epmautomateurl, domain));
        }
    }

    
    @Test(priority = 2)
    public void testLCMImportFileUpload() throws IOException, InterruptedException {
        Executor.runCommand("epmautomate deletefile " + SNAPSHOT_FILENAME.replace(".zip", ""));
        File template = new File(PATH_TO_SNAPSHOTS + SNAPSHOT_FILENAME);
        String templatePath = template.getAbsolutePath();
        ro = Executor.runCommand("epmautomate uploadfile " + templatePath);
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM File upload failed. Error: " + ro.getOutput());
    }
     
    
    @Test(priority = 3)
    public void lcmImport() throws IOException, InterruptedException {
        Executor.runCommand("epmautomate deletefile " + SNAPSHOT_FILENAME.replace(".zip", ""));
        File template = new File(PATH_TO_SNAPSHOTS + SNAPSHOT_FILENAME);
        String templatePath = template.getAbsolutePath();
        ro = Executor.runCommand("epmautomate uploadfile " + templatePath);
        ro = Executor.runCommand("epmautomate importsnapshot " + SNAPSHOT_FILENAME.replace(".zip", ""));
       // Assert.assertTrue(ro.getOutput().contains("success"), "LCM Import failed. Error: " + ro.getOutput());
       Executor.runCommand(String.format("epmautomate logout"));
    }


    @Test(priority = 4)
    public void logout() throws IOException, InterruptedException {
        //Executor.runCommand("cd "+pathToEPMAutomate);
            Executor.runCommand(String.format("epmautomate logout"));
    }
  
  /*  
    @Parameters({ "user", "password" })

            	@Test(priority = 5, description = "load Data",enabled=true)
	public void attemptDataLoad(String user, String password) throws Exception {
		logger.log(Level.INFO,
				"***********checking write access by Essbase dataload ***********");
         List<String> dataGrantsFailed = new ArrayList<String>();
        JSONParser parser = new JSONParser();
                
                  JSONObject jsonObject = (JSONObject)parser.parse(new FileReader("./models/"+modelName+"/POV_"+user));

        JSONArray jArray =(JSONArray) jsonObject.get("POVArray");
        for(int i=0; i < jArray.size();i++){
            boolean jobCreated = false;
            JSONObject pov = (JSONObject)jArray.get(i);
     
            String stringDelimiter = "_";
            Object delimiter = pov.get("stringDelimiter");
            if(delimiter != null)
            	stringDelimiter = (String)delimiter;
            String dataPOV = (String)pov.get("dataPOV");
            String dgName = (String)pov.get("dgName");
            String testscenario = (String)pov.get("modelPOV");
                
                
		if( new File("./models/"+modelName+"/dataload/"+dataPOV+".txt").exists()){
			EssbaseLoadDataConsumer wsObj = new EssbaseLoadDataConsumer();
			EPMAutomateUtility.uploadFileOverwrite("./models/"+modelName+"/dataload/", dataPOV+".txt","profitinbox");
			//SFTPUtil.transfer(new File("./models/ML/inpdata.txt"));	
			//ResultObject result = wsObj.loadFile(appName, new File("./models/"+modelName+"/"+user+"/"+dataPOV+".txt"), true, "OVERWRITE_EXISTING_VALUES","inpdata.txt", "");
			ResultObject result1 = wsObj.loadFileNew(user,password,appName, new File("./models/"+modelName+"/dataload/"+dataPOV+".txt"), false, "OVERWRITE_EXISTING_VALUES",dataPOV+".txt", "");

                        
                        logger.log(Level.INFO,"*************"+result1.isResult()+"**************");
                        
                        
                       // String statusMessage = result1.getStatusMessage();
                        String responseMessage=result1.getText();
                        
                        String notAutherizedMsg="User is not authorized for the following action: allow deployment.";
                                       
                        
                        if(user.equalsIgnoreCase("DGVIEWER")){
                            if(responseMessage.equals(notAutherizedMsg))
                             logger.log(Level.INFO, user+ " -" +dgName+" Write PASS - "+dataPOV);  
                            else {
                               logger.log(Level.INFO, user+ " -" +dgName+" Write PASS - "+dataPOV+ "Reason is - "+responseMessage);
                               dataGrantsFailed.add(dgName);
                            }
                            
                           
                        }
                        
                        else{
                            if(user.equalsIgnoreCase("GROUPUSER")){
                               if(dgName.equals("DG6")){
                                   if(!responseMessage.equals(notAutherizedMsg)){
                                        logger.log(Level.INFO, user+ " -" +dgName+" Write PASS - "+dataPOV+ "Reason is - "+responseMessage);
                                      jobCreated = true;
                                    }
                                      
                                   else{
                                       logger.log(Level.WARNING, user+ " -" +dgName+" Write FAIL - "+dataPOV+" Reason is - "+responseMessage);
                                       dataGrantsFailed.add(dgName);

                                   }
                                       
                               }
                               else{
                                    if(responseMessage.equals(notAutherizedMsg))
                                        logger.log(Level.INFO, user+ " -" +dgName+" Write PASS - "+dataPOV+ "Reason is - "+responseMessage);
                                    else{
                                       logger.log(Level.WARNING, user+ " -" +dgName+" Write FAIL - "+dataPOV+" Reason is - "+responseMessage);
                                            dataGrantsFailed.add(dgName);
                                    }
                               }
                             
                            }
                            
                            else{
                               if(dgName.equals("DG1") || dgName.equals("DG2") || dgName.equals("DG3")){
                                   logger.log(Level.INFO, "*******response message : "+ responseMessage);           
                                      if(!responseMessage.equals(notAutherizedMsg)) {
                                        logger.log(Level.INFO, user+ " -" +dgName+" Write PASS - "+dataPOV+ "Reason is - "+responseMessage);
                                        jobCreated = true;
                                      }
                                      else {
                                       logger.log(Level.WARNING, user+ " -" +dgName+" Write FAIL - "+dataPOV+" Reason is - "+responseMessage);
                                        dataGrantsFailed.add(dgName);
                                      }
                                       
                               }
                               else {
                                    if(responseMessage.equals(notAutherizedMsg))
                                        logger.log(Level.INFO, user+ " -" +dgName+" Write PASS - "+dataPOV+ "Reason is - "+responseMessage);
                                    else {
                                       logger.log(Level.WARNING, user+ " -" +dgName+" Write FAIL - "+dataPOV+" Reason is - "+responseMessage);
                                       dataGrantsFailed.add(dgName);
                                    }
                               }

                            }
                            
                        }
                        
			//Assert.assertTrue(result1.isResult(),"EssbaseLoadData webservice returned error : "+result1.getText());
                        if(jobCreated) {
                            String jobName  = result1.getText();
                            Assert.assertNotNull(jobName);
                            GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
                            Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));    
                        }
			
		}
                
        }
                logger.log(Level.INFO, "Failed Datagrants size : "+ dataGrantsFailed.size());
                if(dataGrantsFailed.size() > 0) {
                    for(String dataGrant : dataGrantsFailed) {
                        logger.log(Level.INFO, "Failed Datagrant : "+ dataGrant);           
                    }
                }
                Assert.assertTrue(dataGrantsFailed.isEmpty(),"Some data grants have failed.");
                
	}
               
        
    */    
    
    
    @Parameters({ "user", "password" })
    
	@Test(groups = "ProcessRuleBalancing" ,priority = 6, description = "Target Webservice = ProcessRuleBalancing, assert that ML rule balancing is returned.")
	public void checkReadAccess(String user, String password) throws Exception {
		logger.log(Level.INFO,
				"***********testGetRuleBalancing***********");
        Hashtable<String, String> PovMapPassed     = new Hashtable<String, String>();
        Hashtable<String, String> PovMapFailed    = new Hashtable<String, String>();
        JSONParser parser = new JSONParser();
       // MLCheckJobStatusConsumer jobStatusObject = new MLCheckJobStatusConsumer();
		 
        JSONObject jsonObject = (JSONObject)parser.parse(new FileReader("./models/"+modelName+"/POV_"+user));

        JSONArray jArray =(JSONArray) jsonObject.get("POVArray");
        for(int i=0; i < jArray.size();i++){
                
            JSONObject pov = (JSONObject)jArray.get(i);
     
            String stringDelimiter = "_";
            Object delimiter = pov.get("stringDelimiter");
            if(delimiter != null)
            	stringDelimiter = (String)delimiter;
            String dataPOV = (String)pov.get("dataPOV");
            String testscenario = (String)pov.get("modelPOV");
            //String modelPOV = (String)pov.get("modelPOV");
            //POV=POV.replace(":", stringDelimiter);
            logger.info(" Checking Rule Balancing for POV : "+dataPOV);
            MLRuleBalanceConsumer ruleBalanceConsumerObj = new MLRuleBalanceConsumer();
             UserObject userCredentials = PropertiesUtils.getUserObject(user, password);
		
		String userName = userCredentials.getUserName();
		String pwd = userCredentials.getPassword();
            ruleBalanceConsumerObj.setUser(userName);
            ruleBalanceConsumerObj.setPassword(pwd);
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
            	String ruleBalancingFilePath = "./logs/"+user+"/"+dataPOV+".txt";
                    logger.info("Actual Rule Balancing is placed at -"+ruleBalancingFilePath);
			File ruleBalanceFile = new File(ruleBalancingFilePath);
			FileOutputStream opfile = new FileOutputStream(ruleBalanceFile);
        		opfile.write(itemsText.getBytes());
        		opfile.close();
        		logger.info("Expected rule balancing is taken from: /models/"+modelName+"/balancing/"+user+"/"+dataPOV+".txt");
         		if(CompareUtil.compareJSONRuleBalancing("./models/"+modelName+"/balancing/"+user+"/"+dataPOV+".txt", ruleBalancingFilePath))
        		{
        			 PovMapPassed.put(dataPOV,"");
                                 logger.info(testscenario);
        			 logger.info(user +" - Rule Balancing compare passed for POV: "+dataPOV);
        		}else{
        			 PovMapFailed.put(dataPOV,"");
        			 logger.warning(user +"[checkRuleBalancing] Rule Balancing compare failed for POV: "+dataPOV);
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
        @Parameters({ "user", "password" })
    	@Test(groups = "ProcessRuleBalancing" ,priority = 6, description = "Target Webservice = ProcessRuleBalancing, assert that ML rule balancing is returned.")
	public void checkWriteAccess(String user, String password) throws Exception {
		logger.log(Level.INFO,
				"***********testGetRuleBalancing***********");
        Hashtable<String, String> PovMapPassed     = new Hashtable<String, String>();
        Hashtable<String, String> PovMapFailed    = new Hashtable<String, String>();
        JSONParser parser = new JSONParser();
       // MLCheckJobStatusConsumer jobStatusObject = new MLCheckJobStatusConsumer();
		 
        JSONObject jsonObject = (JSONObject)parser.parse(new FileReader("./models/"+modelName+"/POV_"+user));

        JSONArray jArray =(JSONArray) jsonObject.get("POVArray");
        for(int i=0; i < jArray.size();i++){
                
            JSONObject pov = (JSONObject)jArray.get(i);
     
            String stringDelimiter = "_";
            Object delimiter = pov.get("stringDelimiter");
            if(delimiter != null)
            	stringDelimiter = (String)delimiter;
            String dataPOV = (String)pov.get("dataPOV");
            String testscenario = (String)pov.get("modelPOV");
            //String modelPOV = (String)pov.get("modelPOV");
            //POV=POV.replace(":", stringDelimiter);
            logger.info(" Checking Rule Balancing for POV : "+dataPOV);
            MLRuleBalanceConsumer ruleBalanceConsumerObj = new MLRuleBalanceConsumer();
            ruleBalanceConsumerObj.setUser(user);
            ruleBalanceConsumerObj.setPassword(password);
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
            	String ruleBalancingFilePath = "./logs/afterWrite/"+user+"/"+dataPOV+".txt";
                    logger.info("Actual Rule Balancing is placed at -"+ruleBalancingFilePath);
			File ruleBalanceFile = new File(ruleBalancingFilePath);
			FileOutputStream opfile = new FileOutputStream(ruleBalanceFile);
        		opfile.write(itemsText.getBytes());
        		opfile.close();
        		logger.info("Expected rule balancing is taken from: /models/"+modelName+"/balancing_after_write/"+user+"/"+dataPOV+".txt");
         		if(CompareUtil.compareJSONRuleBalancing("./models/"+modelName+"/balancing_after_write/"+user+"/"+dataPOV+".txt", ruleBalancingFilePath))
        		{
        			 PovMapPassed.put(dataPOV,"");
                                 logger.info(testscenario);
        			 logger.info(user +" - Rule Balancing compare passed for POV: "+dataPOV);
        		}else{
        			 PovMapFailed.put(dataPOV,"");
        			 logger.warning(user +"[checkRuleBalancing] Rule Balancing compare failed for POV: "+dataPOV);
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
