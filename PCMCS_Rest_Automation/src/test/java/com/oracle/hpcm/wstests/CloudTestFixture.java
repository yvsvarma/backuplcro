
package com.oracle.hpcm.wstests;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oracle.hpcm.utils.EPMAutomateUtility;
import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.PovDTO;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;
import com.oracle.hpcm.webservices.common.DeleteApplicationConsumer;
import com.oracle.hpcm.webservices.common.DeletePOVConsumer;
import com.oracle.hpcm.webservices.common.EnableFileApplicationConsumer;
import com.oracle.hpcm.webservices.common.EssbaseLoadDataConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationByTypeConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationPropertiesConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationTypeConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationsConsumer;
import com.oracle.hpcm.webservices.common.GetEssbaseApplicationsConsumer;
import com.oracle.hpcm.webservices.common.GetPOVConsumer;
import com.oracle.hpcm.webservices.common.GetTaskStatusByProcessNameConsumer;
import com.oracle.hpcm.webservices.common.ProcessCreateFileApplicationConsumer;
import com.oracle.hpcm.webservices.common.ProcessExportTemplateConsumer;
import com.oracle.hpcm.webservices.common.ProcessFileApplicationUpdateDimensionConsumer;
import com.oracle.hpcm.webservices.common.ProcessImportTemplateConsumer;
import com.oracle.hpcm.webservices.ml.MLDeployCubeConsumer;
import com.oracle.hpcm.webservices.ml.MLRuleBalanceConsumer;
import com.oracle.hpcm.webservices.ml.MLRunCalculationConsumer;
import com.oracle.hpcm.webservices.ml.ProcessLedgerClearPOVConsumer;
import com.oracle.hpcm.webservices.ml.ProcessLedgerCopyPOVConsumer;

public class CloudTestFixture {

    private String mlAppName;
    private String exportFileName;
    public static Logger logger = Logger.getLogger("MLTestFixture");
    public static int timeout;

    @BeforeClass
    public void setUp() throws Exception {
        logger.log(Level.INFO, "***********Test Environment Setup***********");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyPort", "8888");
        Properties prop = System.getProperties();
        System.out.println(System.getProperty("domain"));
        PropertiesUtils.processGlobalProperties(prop);
        PropertiesUtils.processModelProperties(prop);
        UserObject userCredentials = PropertiesUtils.getUserObject();
        timeout = PropertiesUtils.getTimeout();
        String user = userCredentials.getUserName();
        String password = userCredentials.getPassword();
        System.out.println(user + " " + password);
        //BeginTest.processArgs(args);
        mlAppName = "ML";
        /* delete all apps before*/
        logger.info("Deleting all existing apps.");
        for (String appName : new GetApplicationsConsumer().getApplicationsNames()) {
            logger.info("Deleteing app " + appName);
            ResultObject ro = new DeleteApplicationConsumer().deleteApp(appName);
            Assert.assertTrue(ro.isResult(), "Deletion of app failed to create a deletion job.");
            GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
            Assert.assertTrue(getStatusObj.waitForJobToFinish(ro.getText(), timeout), "App deletion job failed to finish successfully.");

        }
        /*		Assert.assertTrue("EPMA Import Assert.failed.", epmaDeployer.deploy(mlAppName));
		LCMImporter.execute(mlAppName);*/
        //SFTPUtil.transfer(new File("./models/ML/template.zip"));
        boolean templateFileName = EPMAutomateUtility.uploadFileOverwrite("./models/ML/", "template.zip", "profitinbox");
        Assert.assertTrue(templateFileName, "File Transfer Assert.failed.");
        ProcessImportTemplateConsumer importAppObj = new ProcessImportTemplateConsumer();
        ResultObject result = importAppObj.importTemplate(mlAppName, "Created by webservice automation.",
                "PROFITABILITY_WEB_APP", "EssbaseCluster-1",
                "Default Application Group",
                "template.zip", true);
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));
//		Thread.sleep(180000);
    }

    @Test(enabled = true, priority = 2, description = "Target webservice : GetApplication, checks whether ML application exists or not.", groups = "getApplication")
    public void testGetApplicationsTC1() throws Exception {
        logger.log(Level.INFO, "*********testGetApplicationsTC1***********");
        Assert.assertNotNull(new GetApplicationsConsumer()
                .doesApplicationExists(mlAppName));
    }
    
    @Test(enabled = false, priority = 3, description = "Target webservice : GetApplicationsByType, checks whether ML application is returned as ML app.", groups = "getApplicationByType")
    public void testGetApplicationsByTypeTC1() throws Exception {
        logger.log(Level.INFO, "*********testGetApplicationsByTypeTC1***********");
        try {
            GetApplicationByTypeConsumer getAppWSObj = new GetApplicationByTypeConsumer();

            Assert.assertTrue(getAppWSObj.doesApplicationExistsOfThisAppType(
                    mlAppName, GetApplicationByTypeConsumer.MANAGEMENT_LEDGER));
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = false, groups = "getApplicationByType", priority = 4, description = "Target webservice : GetApplicationsByType, checks whether ML application is not returned as DP type.")
    public void testGetApplicationsByTypeTC2() throws Exception {
        logger.log(Level.INFO, "*********testGetApplicationsByTypeTC2***********");
        try {
            GetApplicationByTypeConsumer getAppWSObj = new GetApplicationByTypeConsumer();

            Assert.assertFalse(getAppWSObj.doesApplicationExistsOfThisAppType(
                    mlAppName,
                    GetApplicationByTypeConsumer.DETAILED_PROFITABILITY));
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = true, groups = "getApplicationType", priority = 7, description = "Target webservice : GetApplicationType, checks whether correct app type is returned for ML app.")
    public void testGetApplicationTypeTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testGetApplicationTypeTC1***********");
        try {
            GetApplicationTypeConsumer getAppTypeWSObj = new GetApplicationTypeConsumer();
            ResultObject result = getAppTypeWSObj.getApplicationType(mlAppName);
            Assert.assertTrue(result.isResult());
            Assert.assertTrue(result.getText().equals(
                    GetApplicationTypeConsumer.MANAGEMENT_LEDGER));
            // assertNull(getAppTypeWSObj.getApplicationType("XYZ"));

        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = true, groups = "getApplicationType", priority = 8, description = "Target webservice : GetApplicationType, checks whether errro is returned for a non existant app.")
    public void testGetApplicationTypeTC3() throws Exception {
        logger.log(Level.INFO,
                "***********testGetApplicationTypeTC2***********");
        try {
            GetApplicationTypeConsumer getAppTypeWSObj = new GetApplicationTypeConsumer();
            ResultObject result = getAppTypeWSObj.getApplicationType("XVSB");
            Assert.assertFalse(result.isResult());
            Assert.assertTrue(result.getText().startsWith("Invalid application name"));

        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = true, groups = "GetPOV", priority = 9, description = "Target webservice : GetPov, checks correct povs are returned for ML app.")
    public void testGetPOVTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testGetPOVTC1***********");
        try {
            GetPOVConsumer getPovWSObj = new GetPOVConsumer();
            ItemsObject povitems = getPovWSObj.getPOVString(mlAppName);
            Assert.assertTrue(povitems.isResult());
            PovDTO[] povs = PovDTO.getPOVSFromJSONText(povitems.getText());
            Assert.assertTrue(povs.length > 4);
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = true, groups = "GetPOV", priority = 11, description = "Target webservice : GetPOV ,Assert no pov is returned for non existing app")
    public void testGetPOVTC3() throws Exception {
        logger.log(Level.INFO, "***********testGetPOVTC3***********");
        try {
            GetPOVConsumer getPovWSObj = new GetPOVConsumer();
            ItemsObject povitems = getPovWSObj.getPOVString("XYZA");
            Assert.assertFalse(povitems.isResult());
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }
 
    @Test(enabled = true, groups = "processLedgerCopyPOV", priority = 12, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is submitted.")
    public void testCopyPOVTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC1***********");

        ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
        ResultObject result = copyPovWSObj.copyPOV(mlAppName,
                "2014_January_Actual", "2015_January_Actual", true, true,
                true, true, true, "_");
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));

    }

    @Test(enabled = true, groups = "processLedgerCopyPOV", priority = 13, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for published dest pov.")
    public void testCopyPOVTC2() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC2***********");
        try {
            ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
            ResultObject result = copyPovWSObj.copyPOV(mlAppName,
                    "2014_January_Actual", "2015_February_Actual", true, true,
                    true, true, true, "_");
            Assert.assertFalse(result.isResult());
            Assert.assertTrue(result.getText().startsWith("POV must be in draft status in order to perform this operation."));
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = true, groups = "processLedgerCopyPOV", priority = 13, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is  submitted for existing dest pov.")
    public void testCopyPOVTC7() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC7***********");
        ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
        ResultObject result = copyPovWSObj.copyPOV(mlAppName,
                "2014_January_Actual", "2014_January_February", true, true,
                true, true, true, "_");
        Assert.assertFalse(result.isResult());
        Assert.assertTrue(result.getText().startsWith("Invalid dimension member group combination"));
    }

    @Test(enabled = true, groups = "processLedgerCopyPOV", priority = 14, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for Archived dest pov.")
    public void testCopyPOVTC3() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC3***********");
        ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
        ResultObject result = copyPovWSObj.copyPOV(mlAppName,
                "2014_January_Actual", "2015_March_Actual", true, true,
                true, true, true, "_");
        Assert.assertFalse(result.isResult());
        Assert.assertTrue(result.getText().startsWith("POV must be in draft status in order to perform this operation."));
    }

    @Test(enabled = true, groups = "processLedgerCopyPOV", priority = 15, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for non existing src pov.")
    public void testCopyPOVTC4() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC4***********");
        ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
        ResultObject result = copyPovWSObj.copyPOV(mlAppName,
                "2013_January_Actual", "2015_January_Actual", true, true,
                true, true, false, "_");
        Assert.assertFalse(result.isResult());
        Assert.assertTrue(result.getText().startsWith("Invalid dimension member group combination"));
    }

    @Test(enabled = true, groups = "processLedgerCopyPOV", priority = 16, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for non existing dest pov.")
    public void testCopyPOVTC5() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC5***********");
        ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
        ResultObject result = copyPovWSObj.copyPOV(mlAppName,
                "2014_January_Actual", "2016_January_Actual", true, true,
                true, true, false, "_");
        Assert.assertFalse(result.isResult());
        Assert.assertTrue(result.getText().startsWith("Invalid dimension member group combination"));
    }
    //Bug here

    @Test(enabled = true, groups = "processLedgerCopyPOV", priority = 17, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for all false copy options.")
    public void testCopyPOVTC6() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC6***********");

        ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
        ResultObject result = copyPovWSObj.copyPOV(mlAppName, "2014_January_Actual",
                "2016_January_Actual", false, false, false, false, false, "_");
        Assert.assertFalse(result.isResult());
        //Assert.assertTrue(result.getText().startsWith("Invalid dimension member group combination"));
    }

    @Test(enabled = true, groups = "processCreateFileApplication", priority = 20, description = "Target Webservice = processCreateFileApplication, assert that duplicate file name causes exception.")
    public void testCreateFileAppTC2() throws Exception {
        logger.log(Level.INFO,
                "***********testCreateFileAppTC2***********");
        try {
            ProcessCreateFileApplicationConsumer WSObj = new ProcessCreateFileApplicationConsumer();
            Assert.assertFalse(WSObj.createApplication(mlAppName, "this is desc.",
                    "PROFITABILITY_WEB_APP",
                    "EssbaseCluster-1",
                    "Default Application Group",
                    System.getProperty("apsserver"), "Rule",
                    "Balance", "True").isResult());
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = true, groups = "enableFileApplication", priority = 22, description = "Target Webservice = enableFileApplication, assert that non existing file app cannot be enabled.")
    public void testEnableFileAppTC2() throws Exception {
        logger.log(Level.INFO,
                "***********testEnableFileAppTC2***********");
        EnableFileApplicationConsumer getAppWSObj = new EnableFileApplicationConsumer();
        Assert.assertFalse(getAppWSObj.enableFileApplication("MT1").isResult());
    }

    @Test(enabled = true, groups = "processExportTemplate", priority = 24, description = "Target Webservice = processExportTemplate, assert that ML apps can be exported.")
    public void testExportTemplate() throws Exception {
        logger.log(Level.INFO,
                "***********testExportTemplate***********");
        ProcessExportTemplateConsumer WSObj = new ProcessExportTemplateConsumer();
        int number = (int) (Math.random() * 100);
        exportFileName = "Filename" + number;
        ResultObject result = WSObj.exportTemplate(mlAppName, exportFileName);
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));

    }

    @Test(enabled = true, groups = "MLDeployCube", priority = 26, description = "Target Webservice = MLDeployCube, assert that ML apps can be imported.")
    public void testDeployCube() throws Exception {
        logger.log(Level.INFO,
                "***********testGetPOVTC1***********");
        MLDeployCubeConsumer WSObj = new MLDeployCubeConsumer();
        ResultObject result = WSObj.runDeployCubeJob(mlAppName, false, true, true,
                "Hello World!");
        String jobName = result.getText();
        Assert.assertTrue(result.isResult());
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));
    }

    @Test(enabled = true, groups = "EssbaseLoadData", priority = 27, description = "Target Webservice = EssbaseLoadData, assert that Loading data into essbase works for ML.")
    public void testLoadDataTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testLoadDataTC1***********");
        EssbaseLoadDataConsumer wsObj = new EssbaseLoadDataConsumer();
        EPMAutomateUtility.uploadFileOverwrite("./models/ML/", "inpdata.txt", "profitinbox");
        //SFTPUtil.transfer(new File("./models/ML/inpdata.txt"));	
        ResultObject result = wsObj.loadFile(mlAppName, new File("./models/ML/inpdata.txt"), true, "OVERWRITE_EXISTING_VALUES", "inpdata.txt", "");
        String jobName = result.getText();
        Assert.assertTrue(result.isResult(), "EssbaseLoadData webservice returned error : " + result.getText());
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));
    }

    @Test(enabled = true, groups = "MLRunCalculation", priority = 28, description = "Target Webservice = MLRunCalculation, assert that ML calculation job works.")
    public void testRunCalcJob() throws Exception {
        logger.log(Level.INFO,
                "***********testRunCalcJob***********");
        MLRunCalculationConsumer WSObj = new MLRunCalculationConsumer();
        ResultObject results = WSObj.runCalculation(mlAppName, "2014_January_Actual", true, true, true, "", "", "", "", "ALL_RULES", "_");
        String jobName = results.getText();
        Assert.assertTrue(results.isResult(), "MLRunCalculation webservice Assert.failed due to this error: " + results.getText());
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));
    }

    @Test(enabled = true, groups = "ProcessRuleBalancing", priority = 29, description = "Target Webservice = ProcessRuleBalancing, assert that ML rule balancing is returned.")
    public void testGetRuleBalancing() throws Exception {
        logger.log(Level.INFO,
                "***********testGetRuleBalancing***********");
        MLRuleBalanceConsumer WSObj = new MLRuleBalanceConsumer();
        Assert.assertNotNull(WSObj.getRuleBalancing(mlAppName, "2014_January_Actual", ""));
    }

    @Test(enabled = true, groups = "ProcessLegdgerClearPOV", priority = 30, description = "Target Webservice = ProcessLegdgerClearPOV, assert that ML clear pov job works.")
    public void testClearMLPOV() throws Exception {
        logger.log(Level.INFO,
                "***********testClearMLPOV***********");
        ProcessLedgerClearPOVConsumer WSObj = new ProcessLedgerClearPOVConsumer();
        ResultObject result = WSObj.clearPOV(mlAppName, "2014_January_Actual", false, false, false, true, "_");
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));
    }

    @Test(enabled = true, groups = "deletePOV", priority = 41, description = "Target Webservice = deletePOV, assert that delete pov job for ml  is successfull.")
    public void testDeleteMLPOV() throws Exception {
        logger.log(Level.INFO,
                "***********deletePOV***********");
        DeletePOVConsumer WSObj = new DeletePOVConsumer();
        ResultObject result = WSObj.deletePOV(mlAppName, "2015_January_Actual");
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));

    }

    @Test(enabled = true, groups = "getApplicationProperties", priority = 42, description = "Target Webservice = getApplicationProperties, assert that app props are returned successfully.")
    public void testgetApplicationProperties() throws Exception {
        logger.log(Level.INFO,
                "***********deletePOV***********");
        GetApplicationPropertiesConsumer getAppWSObj = new GetApplicationPropertiesConsumer();
        ResultObject result = getAppWSObj.getApplicationProps();
        Assert.assertTrue(result.isResult());
        String appProps = result.getText();
        Assert.assertNotNull(appProps);
        Assert.assertTrue(appProps.contains("essbaseAppServers"));
        Assert.assertTrue(appProps.contains("webServer"));
        Assert.assertTrue(appProps.contains("instanceNames"));
        Assert.assertTrue(appProps.contains("sharedServicesProjects"));

    }

    @Test(enabled = true, groups = "GetEssbaseApplications", priority = 43, description = "Target Webservice = GetEssbaseApplications, assert that essbase apps are returned.")
    public void testGetEssbaseApplicationsConsumer() throws Exception {
        logger.log(Level.INFO,
                "***********testGetEssbaseApplicationsConsumer***********");
        GetEssbaseApplicationsConsumer getAppWSObj = new GetEssbaseApplicationsConsumer();
        ResultObject result = getAppWSObj.getEssApplicationsNames("EssbaseCluster-1");
        Assert.assertTrue(result.isResult());
        String cubes = result.getText();
        Assert.assertNotNull(cubes);
    }
    //

    @Test(enabled = true, groups = "deleteApplication", priority = 44, description = "Target Webservice = deleteApllication, assert that ML apps can be deleted.")
    public void testDeleteAppsTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testDeleteAppsTC1***********");
        ResultObject ro = new DeleteApplicationConsumer().deleteApp(mlAppName);
        Assert.assertTrue(ro.isResult(), "Deletion of app failed to create a deletion job.");
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(ro.getText(), timeout), "App deletion job failed to finish successfully.");
        Thread.sleep(180000);

    }

    @Test(enabled = true, groups = "processImportTemplate", priority = 45, description = "Target Webservice = processImportTemplate, assert that ML apps can be imported.")
    public void testImportTemplate() throws Exception {
        logger.log(Level.INFO,
                "***********testImportTemplate***********");
        ProcessImportTemplateConsumer WSObj = new ProcessImportTemplateConsumer();
        if (new GetApplicationsConsumer().doesApplicationExists("MLT")) {
            ResultObject ro = new DeleteApplicationConsumer().deleteApp("MLT");
            Assert.assertTrue(ro.isResult(), "Deletion of app failed to create a deletion job.");
            GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
            Assert.assertTrue(getStatusObj.waitForJobToFinish(ro.getText(), timeout), "App deletion job failed to finish successfully.");

        }

        ResultObject result = WSObj.importTemplate("MLT", "this is desc",
                "PROFITABILITY_WEB_APP", "EssbaseCluster-1",
                "Default Application Group",
                "template.zip", true);
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, timeout));
        Thread.sleep(120000);

    }

    @Test(enabled = true, groups = "deleteApplication", priority = 46, description = "Target Webservice = deleteApllication, assert that ML apps can be deleted.")
    public void testDeleteAppsTC2() throws Exception {
        logger.log(Level.INFO,
                "***********testDeleteAppsTC1***********");
        //DeleteApplicationConsumer deleteWSObj = new DeleteApplicationConsumer();
        ResultObject ro = new DeleteApplicationConsumer().deleteApp("MLT");
        Assert.assertTrue(ro.isResult(), "Deletion of app failed to create a deletion job.");
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(ro.getText(), timeout), "App deletion job failed to finish successfully.");
        Thread.sleep(180000);
    }

    @Test(enabled = true, groups = "processCreateFileApplication", priority = 47, description = "Target Webservice = processCreateFileApplication, assert that file type application is created.")
    public void testCreateFileAppTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testCreateFileAppTC1***********");
        try {
            if (new GetApplicationsConsumer().doesApplicationExists("MLT1")) {
                new DeleteApplicationConsumer().deleteApp("MLT1");
            }
            Thread.sleep(2000);
            ProcessCreateFileApplicationConsumer WSObj = new ProcessCreateFileApplicationConsumer();
            Assert.assertTrue(WSObj.createApplication("MLT1", "this is desc.",
                    "PROFITABILITY_WEB_APP",
                    "EssbaseCluster-1",
                    "Default Application Group",
                    System.getProperty("apsserver"), "Rule",
                    "Balance", "True").isResult());
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = true, groups = "enableFileApplication", priority = 48, description = "Target Webservice = enableFileApplication, assert that file app can be enabled.")
    public void testEnableFileAppTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testEnableFileAppTC1***********");
        try {
            EnableFileApplicationConsumer getAppWSObj = new EnableFileApplicationConsumer();
            Assert.assertTrue(getAppWSObj.enableFileApplication("MLT1").isResult());
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Assert.failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = true, groups = "processFileApplicationUpdateDimension", priority = 49, description = "Target Webservice = processFileApplicationUpdateDimension, assert that dimension files are uploaded.")
    public void testUploadFileDims() throws Exception {
        logger.log(Level.INFO,
                "***********testUploadFileDims***********");
        String[] extensions = new String[]{"txt"};
        IOFileFilter filter = new SuffixFileFilter(extensions, IOCase.INSENSITIVE);
        Iterator<File> iter = FileUtils.iterateFiles(new File("./models/ML/dimfiles"), filter, null);
        while (iter.hasNext()) {
            File file = iter.next();
            ProcessFileApplicationUpdateDimensionConsumer WSObj = new ProcessFileApplicationUpdateDimensionConsumer();
            //SFTPUtil.transfer(new File("./models/ML/dimfiles/"+file.getName()));
            boolean dimFileName = EPMAutomateUtility.uploadFileOverwrite("./models/ML/dimfiles/", file.getName(), "profitinbox");
            Assert.assertTrue(dimFileName, "Upload of dimension file Assert.failed.");
            //ResultObject result =  WSObj.uploadFile("ML","Activities.txt");
            Assert.assertTrue(WSObj.uploadFile("MLT1", file.getName()).isResult());

        }
    }

    @Test(enabled = true, groups = "deleteApplication", priority = 50, description = "Target Webservice = deleteApllication, assert that ML apps can be deleted.")
    public void testDeleteAppsTC4() throws Exception {
        logger.log(Level.INFO,
                "***********testDeleteAppsTC1***********");
        ResultObject ro = new DeleteApplicationConsumer().deleteApp("MLT1");
        Assert.assertTrue(ro.isResult(), "Deletion of app failed to create a deletion job.");
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(ro.getText(), timeout), "App deletion job failed to finish successfully.");

    }

    @Test(enabled = true, groups = "deleteApplication", priority = 51, description = "Target Webservice = deleteApllication, assert that deletion of nonexistant app throws exception.")
    public void testDeleteAppsTC3() throws Exception {
        logger.log(Level.INFO,
                "***********testDeleteAppsTC3***********");
        ResultObject ro = new DeleteApplicationConsumer().deleteApp("ZYX");
        Assert.assertFalse(ro.isResult(), "Deletion of nonexitant app creates a deletion job.");

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
