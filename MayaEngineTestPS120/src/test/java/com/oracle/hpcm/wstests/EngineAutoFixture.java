/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.hpcm.wstests;

import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oracle.hpcm.utils.CompareUtil;
import com.oracle.hpcm.utils.CubeOperation;

import com.oracle.hpcm.utils.EPMADeployer;
import com.oracle.hpcm.utils.LCMImport;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;
import com.oracle.hpcm.webservices.common.DeleteApplicationConsumer;
import com.oracle.hpcm.webservices.common.EssbaseLoadDataConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationsConsumer;
import com.oracle.hpcm.webservices.common.GetTaskStatusByProcessNameConsumer;
import com.oracle.hpcm.webservices.ml.MLDeployCubeConsumer;
import com.oracle.hpcm.webservices.ml.MLRuleBalanceConsumer;
import com.oracle.hpcm.webservices.ml.MLRunCalculationConsumer;

public class EngineAutoFixture {

    public EPMADeployer epmaDeployer;
    public LCMImport LCMImporter;
    private String mlAppName;
    //public static Logger logger = Logger.getLogger("MLTestFixture");
    public int timeout;
    public String modelName;

    public static Logger logger = Logger.getLogger("MLTestFixture");

    public EngineAutoFixture() throws SecurityException, IOException {
    }

    @BeforeClass
    @Parameters("applicationNameFromTestNG")
    public void setUp(@Optional("") String applicationNameFromTestNG) throws Exception {
        logger.log(Level.INFO, "***********Test Environment Setup***********");

        //System.out.println("App name is "+mlAppName);
        Properties prop = System.getProperties();
        //System.out.println(System.getProperty("domain"));
        PropertiesUtils.processGlobalProperties(prop);
        PropertiesUtils.processModelProperties(prop);
        mlAppName = System.getProperty("application_name");
        if (applicationNameFromTestNG.equals("")) {
            modelName = System.getProperty("model_name");
        } else {
            modelName = applicationNameFromTestNG;
            mlAppName = modelName;
        }
        epmaDeployer = new EPMADeployer();
        LCMImporter = new LCMImport();
        UserObject userCredentials = PropertiesUtils.getUserObject();
        timeout = PropertiesUtils.getTimeout();
        String user = userCredentials.getUserName();
        String password = userCredentials.getPassword();
        System.out.println(user + " " + password);
        //BeginTest.processArgs(args
        /* delete all apps before*/

        //setup the properties
        //System.setProperty("server", value)
        if (new GetApplicationsConsumer().doesApplicationExists(mlAppName)) {
            new DeleteApplicationConsumer().deleteApp(mlAppName);
            new CubeOperation(mlAppName).deleteEssApp();

        }
        AssertJUnit.assertTrue("EPMA Import failed.", epmaDeployer.deploy(mlAppName));
        LCMImporter.execute(mlAppName);

    }

    @Test(groups = "MLDeployCube", priority = 1, description = "Target Webservice = MLDeployCube, assert that ML apps can be imported.")
    public void testDeployCube() throws Exception {
        logger.log(Level.INFO,
                "***********Deloycube***********");
        MLDeployCubeConsumer WSObj = new MLDeployCubeConsumer();
        ResultObject result = WSObj.runDeployCubeJob(mlAppName, false, true, true,
                "Hello World!");
        String jobName = result.getText();
        Assert.assertTrue(result.isResult());
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));
    }

    @Test(enabled = true, groups = "EssbaseLoadData", priority = 2, description = "Target Webservice = EssbaseLoadData, assert that Loading data into essbase works for ML.")
    public void testLoadDataWS() throws Exception {
        logger.log(Level.INFO,
                "***********testLoadDataTC1***********");
//        EssbaseLoadDataConsumer wsObj = new EssbaseLoadDataConsumer();
//        ResultObject result = wsObj.loadFile(mlAppName, new File("./models/"
//                + mlAppName
//                + "/inpdata.txt"), true, "OVERWRITE_EXISTING_VALUES", "data.txt", "");
//        String jobName = result.getText();
//        Assert.assertTrue(result.isResult(), "EssbaseLoadData webservice returned error : " + result.getText());
//        Assert.assertNotNull(jobName);
//        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
//        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));
        try {
            CubeOperation EssObj = new CubeOperation(mlAppName);
            EssObj.loadDataFromFile("./models/"
                    + mlAppName
                    + "/inpdata.txt");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Following exception occured during data load: " + e.getMessage());
            throw e;
        }

    }

    @Test(groups = "MLRunCalculation", priority = 3, description = "Target Webservice = MLRunCalculation, assert that ML calculation job works.")
    public void testRunCalcJob() throws Exception {
        logger.log(Level.INFO, "***********testRunCalcJob***********");
        Hashtable<String, String> PovMap = new Hashtable<String, String>();
        Hashtable<String, String> PovMapPassed = new Hashtable<String, String>();
        Hashtable<String, String> PovMapFailed = new Hashtable<String, String>();
        JSONParser parser = new JSONParser();
        GetTaskStatusByProcessNameConsumer jobStatusObject = new GetTaskStatusByProcessNameConsumer();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("./models/" + modelName + "/POV"));
        JSONArray jArray = (JSONArray) jsonObject.get("POVArray");
        for (int i = 0; i < jArray.size(); i++) {
            JSONObject pov = (JSONObject) jArray.get(i);
            String POV = (String) pov.get("POV");
            POV = POV.replace(":", "_");
            logger.info("Running POV : " + POV);
            MLRunCalculationConsumer WSObj = new MLRunCalculationConsumer();
            ResultObject results = WSObj.runCalculation(mlAppName, POV, true, true, true, "", "", "", "", "ALL_RULES");
            if (results.isResult()) {
                String jobName = results.getText();
                logger.info("Calculation job have been started for " + POV + " : " + jobName);
                PovMap.put(POV, jobName);

            } else {
                String error = results.getText();
                logger.warning("Calculation job have failed to start for " + POV + ". Error returned : " + error);
            }
        }

        int attemptsCount = (PovMap.size() + 1) * 80;
        int run_calc_timeout = PropertiesUtils.getTimeout() * (PovMap.size() + 1);
        int waitPerAttempt = run_calc_timeout / attemptsCount;
        boolean AllJobStatus = false;
        int checkAttempts = 0;
        logger.info("Run Calculation Timeout(Seconds): " + run_calc_timeout);
        do {

            Thread.sleep(waitPerAttempt * 1000);
            checkAttempts++;
            logger.info("Checking for Calulation job statuses. Iteration No: " + checkAttempts);
            if (PovMap.isEmpty()) {
                AllJobStatus = true;
            }
            Enumeration<String> povNamesEnum = PovMap.keys();
            while (povNamesEnum.hasMoreElements()) {
                String currentPov = povNamesEnum.nextElement();
                String jobName = PovMap.get(currentPov);

                boolean jobStatus = jobStatusObject.isJobActive(jobName);
                if (jobStatus) {
                    continue;
                } else {
                    if (jobStatusObject.checkFailureInSubtasks()) {
                        PovMapPassed.put(currentPov, jobName);
                        PovMap.remove(currentPov);
                        logger.info("Checking for Calulation job id " + jobName + ",  Status : PASS");
                    } else {

                        PovMapFailed.put(currentPov, jobName);
                        PovMap.remove(currentPov);
                        logger.severe("Checking for Calulation job id " + jobName + ", Status : Failed");
                    }
                }

            }
            if (PovMap.isEmpty()) {
                AllJobStatus = true;
                break;
            }
        } while (!AllJobStatus && checkAttempts <= attemptsCount);
        if (checkAttempts > attemptsCount) {
            logger.severe(" Timeout for run calculation exceeded!!!!!!!!!!");
        }
        if (PovMapPassed.isEmpty()) {
            logger.severe(" All calculation jobs have failed.");
        }
        if (!PovMapFailed.isEmpty()) {
            logger.warning(" Some calculation jobs have failed.");
        }
        if (PovMapFailed.isEmpty()) {
            logger.info(" All calculation jobs have passed.");
        }
        Assert.assertTrue(PovMapFailed.isEmpty(), "Some calculation Job have failed");
    }

    @Test(groups = "ProcessRuleBalancing", priority = 4, description = "Target Webservice = ProcessRuleBalancing, assert that ML rule balancing is returned.")
    public void testGetRuleBalancing() throws Exception {
        logger.log(Level.INFO,
                "***********testGetRuleBalancing***********");
        Hashtable<String, String> PovMapPassed = new Hashtable<String, String>();
        Hashtable<String, String> PovMapFailed = new Hashtable<String, String>();
        JSONParser parser = new JSONParser();
        // MLCheckJobStatusConsumer jobStatusObject = new MLCheckJobStatusConsumer();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("./models/" + modelName + "/POV"));

        JSONArray jArray = (JSONArray) jsonObject.get("POVArray");
        for (int i = 0; i < jArray.size(); i++) {

            JSONObject pov = (JSONObject) jArray.get(i);
            String POV = (String) pov.get("POV");
            POV = POV.replace(":", "_");
            logger.info(" Checking Rule Balancing for POV : " + POV);

            MLRuleBalanceConsumer ruleBalanceConsumerObj = new MLRuleBalanceConsumer();
            String ruleBalanceingText = ruleBalanceConsumerObj.getRuleBalancing(mlAppName, POV, "").getItemsText();

            if (ruleBalanceingText == null) {
                logger.warning(" Retrieval of Rule Balancing failed for POV: " + POV);
                PovMapFailed.put(POV, "");
                continue;
            } else {

                String itemsText = ruleBalanceingText;//.substring(ruleBalanceingText.indexOf("items\":")+7, ruleBalanceingText.indexOf(",\"status\""));
                logger.info("Actual Rule Balancing: " + itemsText);
                FileUtils.forceMkdir(new File("./logs/"));
                String ruleBalancingFilePath = "./logs/" + mlAppName + POV + ".txt";
                FileOutputStream opfile = new FileOutputStream(new File(ruleBalancingFilePath));
                opfile.write(itemsText.getBytes());
                opfile.close();
                logger.info("Expected rule balancing : " + FileUtils.readFileToString(new File("./models/" + modelName + "/balancing/" + POV + ".txt")));
                if (CompareUtil.compareJSONRuleBalancing("./models/" + modelName + "/balancing/" + POV + ".txt", ruleBalancingFilePath)) {
                    PovMapPassed.put(POV, "");
                    logger.info(" Rule Balancing compare passed for POV: " + POV);
                } else {
                    PovMapFailed.put(POV, "");
                    logger.warning("[checkRuleBalancing] Rule Balancing compare failed for POV: " + POV);
                }

            }

        }
        if (PovMapPassed.isEmpty()) {
            logger.severe("[checkRuleBalancing] All rule balancing comparisons have failed.");
        }
        if (!PovMapFailed.isEmpty()) {
            logger.warning("[checkRuleBalancing] Some rule balancing comparisons failed.");
        } else {
            logger.info("[checkRuleBalancing] All rule balancing comparisons have passed.");
        }
        Assert.assertTrue(PovMapFailed.isEmpty(), "Some rule balancing compares have failed.");
    }

    @Test(enabled = false, groups = "deleteApplication", priority = 5, description = "Target Webservice = deleteApllication, assert that deletion of nonexistant app throws exception.")
    public void testDeleteApp() throws Exception {
        logger.log(Level.INFO,
                "***********testDeleteAppsTC3***********");
        DeleteApplicationConsumer deleteWSObj = new DeleteApplicationConsumer();
        AssertJUnit.assertTrue(deleteWSObj.deleteApp(mlAppName).isResult());
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.isSuccess()) {
            logger.info("Test case " + result.getMethod().getMethodName() + " is a pass.");
        } else {
            if (result.getStatus() == ITestResult.FAILURE) {
                logger.severe("Test case " + result.getMethod().getMethodName() + " is a fail.");
                //Throwable ex = result.getThrowable();
                //logger.log(Level.SEVERE,"Exception : "+ex.getMessage());
            }
            //logger.info("Test case " + result.getMethod().getMethodName() + " is a pass.");
        }
    }
}
