/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.hpcm.wstests;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oracle.hpcm.utils.EPMADeployer;
import com.oracle.hpcm.utils.ItemsObject;
import com.oracle.hpcm.utils.LCMImport;
import com.oracle.hpcm.utils.PovDTO;
import com.oracle.hpcm.utils.PropertiesUtils;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.webservices.common.DeleteApplicationConsumer;
import com.oracle.hpcm.webservices.common.DeletePOVConsumer;
import com.oracle.hpcm.webservices.common.EnableFileApplicationConsumer;
import com.oracle.hpcm.webservices.common.EssbaseLoadDataConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationByTypeConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationPropertiesConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationTypeConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationsConsumer;
import com.oracle.hpcm.webservices.common.GetEssbaseApplicationsConsumer;
import com.oracle.hpcm.webservices.common.GetEssbaseDimConsumer;
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
import com.oracle.hpcm.webservices.sp.ClearASOCubeConsumer;
import com.oracle.hpcm.webservices.sp.ClearPOVDataConsumer;
import com.oracle.hpcm.webservices.sp.GetStagesByApplicationConsumer;
import com.oracle.hpcm.webservices.sp.ProcessCopyPOVDataConsumer;
import com.oracle.hpcm.webservices.sp.ProcessGenealogyPathConsumer;
import com.oracle.hpcm.webservices.sp.ProcessGenealogyPathsWithOutASOClear;
import com.oracle.hpcm.webservices.sp.ProcessMultiPOVCalcScriptConsumer;
import com.oracle.hpcm.webservices.sp.RunSPCalculationConsumer;
import com.oracle.hpcm.webservices.sp.SPDeployCubeConsumer;
import com.oracle.hpcm.webservices.sp.ClearASOCubeConsumer.LayerName;
import com.oracle.hpcm.webservices.sp.SPDeployCubeConsumer.CubeType;
import java.util.Properties;
import org.testng.Assert;

public class CommonTestFixture {

    public EPMADeployer epmaDeployer;
    public LCMImport LCMImporter;
    private String mlAppName;
    private String spAppName;
    private String exportFileName;
    public static Logger logger = Logger.getLogger("MLTestFixture");

    @BeforeClass
    public void setUp() throws Exception {
        logger.log(Level.INFO, "***********Test Environment Setup***********");

        Properties prop = System.getProperties();
        //System.out.println(System.getProperty("domain"));
        PropertiesUtils.processGlobalProperties(prop);
        PropertiesUtils.processModelProperties(prop);
        mlAppName = "ML";
        spAppName = "SP";
        epmaDeployer = new EPMADeployer();
        LCMImporter = new LCMImport();
        //System.out.println("App name is "+mlAppName);

        if (new GetApplicationsConsumer().doesApplicationExists(mlAppName)) {
            new DeleteApplicationConsumer().deleteApp(mlAppName);
            //new CubeOperation().deleteEssApp(mlAppName+"C");

        }
        if (new GetApplicationsConsumer().doesApplicationExists(spAppName)) {
            new DeleteApplicationConsumer().deleteApp(spAppName);
            //new CubeOperation().deleteEssApp(mlAppName+"C");

        }
        System.setProperty("model.dir","./models/");
        Assert.assertTrue(epmaDeployer.deploy(mlAppName), "EPMA Import failed.");
        LCMImporter.execute(mlAppName);

        if (new GetApplicationsConsumer().doesApplicationExists(spAppName)) {
            new DeleteApplicationConsumer().deleteApp(spAppName);
            //new CubeOperation().deleteEssApp(spAppName+"C");
            //new CubeOperation().deleteEssApp(spAppName+"R");
        }
        
        Assert.assertTrue(epmaDeployer.deploy(spAppName), "EPMA Import failed.");
        LCMImporter.execute(spAppName);

    }

    @Test(priority = 2, description = "Target webservice : GetApplication, checks whether ML application exists or not.", groups = "getApplication")
    public void testGetApplicationsTC1() throws Exception {
        logger.log(Level.INFO, "*********testGetApplicationsTC1***********");
        Assert.assertNotNull(new GetApplicationsConsumer()
                .doesApplicationExists(mlAppName));
    }

    @Test(priority = 3, description = "Target webservice : GetApplication, checks whether SP application exists or not.", groups = "getApplication")
    public void testGetApplicationsTC2() throws Exception {
        logger.log(Level.INFO, "*********testGetApplicationsTC2***********");
        Assert.assertNotNull(new GetApplicationsConsumer()
                .doesApplicationExists(spAppName));
    }

    @Test(priority = 3, description = "Target webservice : GetApplicationsByType, checks whether ML application is returned as ML app.", groups = "getApplicationByType")
    public void testGetApplicationsByTypeTC1() throws Exception {
        logger.log(Level.INFO, "*********testGetApplicationsByTypeTC1***********");
        try {
            GetApplicationByTypeConsumer getAppWSObj = new GetApplicationByTypeConsumer();

            Assert.assertTrue(getAppWSObj.doesApplicationExistsOfThisAppType(
                    mlAppName, GetApplicationByTypeConsumer.MANAGEMENT_LEDGER));
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "getApplicationByType", priority = 4, description = "Target webservice : GetApplicationsByType, checks whether ML application is not returned as DP type.")
    public void testGetApplicationsByTypeTC2() throws Exception {
        logger.log(Level.INFO, "*********testGetApplicationsByTypeTC2***********");
        try {
            GetApplicationByTypeConsumer getAppWSObj = new GetApplicationByTypeConsumer();

            Assert.assertFalse(getAppWSObj.doesApplicationExistsOfThisAppType(
                    mlAppName,
                    GetApplicationByTypeConsumer.DETAILED_PROFITABILITY));
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "getApplicationByType", priority = 5, description = "Target webservice : GetApplicationsByType, checks whether SP application is returned as SP type app.")
    public void testGetApplicationsByTypeTC3() throws Exception {
        logger.log(Level.INFO, "*********testGetApplicationsByTypeTC3***********");
        try {
            GetApplicationByTypeConsumer getAppWSObj = new GetApplicationByTypeConsumer();

            Assert.assertTrue(getAppWSObj.doesApplicationExistsOfThisAppType(
                    spAppName,
                    GetApplicationByTypeConsumer.STANDARD_PROFITABILITY));
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "getApplicationType", priority = 7, description = "Target webservice : GetApplicationType, checks whether correct app type is returned for ML app.")
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
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "getApplicationType", priority = 8, description = "Target webservice : GetApplicationType, checks whether correct app type is returned for SP app.")
    public void testGetApplicationTypeTC2() throws Exception {
        logger.log(Level.INFO,
                "***********testGetApplicationTypeTC2***********");
        try {
            GetApplicationTypeConsumer getAppTypeWSObj = new GetApplicationTypeConsumer();
            ResultObject result = getAppTypeWSObj.getApplicationType(spAppName);
            Assert.assertTrue(result.isResult());
            Assert.assertTrue(result.getText().equals(
                    GetApplicationTypeConsumer.STANDARD_PROFITABILITY));
            // assertNull(getAppTypeWSObj.getApplicationType("XYZ"));

        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "getApplicationType", priority = 8, description = "Target webservice : GetApplicationType, checks whether errro is returned for a non existant app.")
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
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "GetPOV", priority = 9, description = "Target webservice : GetPov, checks correct povs are returned for ML app.")
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
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "GetPOV", priority = 10, description = "Target webservice : GetPov, checks correct povs are returned for SP app.")
    public void testGetPOVTC2() throws Exception {
        logger.log(Level.INFO, "***********testGetPOVTC2***********");
        try {
            GetPOVConsumer getPovWSObj = new GetPOVConsumer();
            ItemsObject povitems = getPovWSObj.getPOVString(spAppName);
            Assert.assertTrue(povitems.isResult());
            PovDTO[] povs = PovDTO.getPOVSFromJSONText(povitems.getText());
            Assert.assertTrue(povs.length >= 2);
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "GetPOV", priority = 11, description = "Target webservice : GetPOV ,Assert no pov is returned for non existing app")
    public void testGetPOVTC3() throws Exception {
        logger.log(Level.INFO, "***********testGetPOVTC3***********");
        try {
            GetPOVConsumer getPovWSObj = new GetPOVConsumer();
            ItemsObject povitems = getPovWSObj.getPOVString("XYZA");
            Assert.assertFalse(povitems.isResult());
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "processLedgerCopyPOV", priority = 12, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is submitted.")
    public void testCopyPOVTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC1***********");
        try {
            ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
            ResultObject result = copyPovWSObj.copyPOV(mlAppName,
                    "2014_January_Actual", "2015_January_Actual", true, true,
                    true, true, true, "_");
            Assert.assertTrue(result.isResult());

        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "processLedgerCopyPOV", priority = 13, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for published dest pov.")
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
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "processLedgerCopyPOV", priority = 14, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for Archived dest pov.")
    public void testCopyPOVTC3() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC3***********");
        try {
            ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
            ResultObject result = copyPovWSObj.copyPOV(mlAppName,
                    "2014_January_Actual", "2015_March_Actual", true, true,
                    true, true, true, "_");
            Assert.assertFalse(result.isResult());
            Assert.assertTrue(result.getText().startsWith("POV must be in draft status in order to perform this operation."));
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "processLedgerCopyPOV", priority = 15, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for non existing src pov.")
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

    @Test(groups = "processLedgerCopyPOV", priority = 16, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for non existing dest pov.")
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

    @Test(groups = "processLedgerCopyPOV", priority = 17, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for all false copy options.")
    public void testCopyPOVTC6() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC6***********");

        ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
        ResultObject result = copyPovWSObj.copyPOV(mlAppName, "2014_January_Actual",
                "2016_January_Actual", false, false, false, false, false, "_");
        Assert.assertFalse(result.isResult());
        //Assert.assertTrue(result.getText().startsWith("Invalid dimension member group combination"));
    }

    @Test(groups = "processLedgerCopyPOV", priority = 18, description = "Target Webservice = ProcessLedgerCopyPOV, assert that copy pov job is not submitted for SP app.")
    public void testCopyPOVTC7() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVTC7***********");
        try {
            ProcessLedgerCopyPOVConsumer copyPovWSObj = new ProcessLedgerCopyPOVConsumer();
            ResultObject result = copyPovWSObj.copyPOV(spAppName, "2014_January_Actual",
                    "2016_January_Actual", false, true, false, false, true, "_");
            Assert.assertFalse(result.isResult());
            Assert.assertTrue(result.getText().startsWith("Invalid operation for application type: ''GENERAL''"));
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "processCreateFileApplication", priority = 19, description = "Target Webservice = processCreateFileApplication, assert that file type application is created.")
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
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "processCreateFileApplication", priority = 20, description = "Target Webservice = processCreateFileApplication, assert that duplicate file name causes exception.")
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
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(enabled = false, groups = "processFileApplicationUpdateDimension", priority = 21, description = "Target Webservice = processFileApplicationUpdateDimension, assert that dimension files are uploaded.")
    public void testUploadFileDims() throws Exception {
        logger.log(Level.INFO,
                "***********testUploadFileDims***********");
        String[] extensions = new String[]{"txt"};
        IOFileFilter filter = new SuffixFileFilter(extensions, IOCase.INSENSITIVE);
        Iterator<File> iter = FileUtils.iterateFiles(new File("./models/ml/dimfiles"), filter, null);
        while (iter.hasNext()) {
            File file = iter.next();
            ProcessFileApplicationUpdateDimensionConsumer WSObj = new ProcessFileApplicationUpdateDimensionConsumer();
            Assert.assertTrue(WSObj.uploadFile("MLT1", file).isResult());

        }
    }

    @Test(groups = "processCopyPOV", priority = 22, description = "Target Webservice = processCopyPOV, assert that sp copy pov job works.")
    public void testCopyPOVSP() throws Exception {
        logger.log(Level.INFO,
                "***********testCopyPOVSP***********");
        ProcessCopyPOVDataConsumer WSObj = new ProcessCopyPOVDataConsumer();
        ResultObject result = WSObj.copyPOV(spAppName, "2009_January_Actual", "2012_January_Actual", true, true, true, true, true, true, false);
        Assert.assertTrue(result.isResult());
        Assert.assertNotNull(result.getText());
    }

    @Test(groups = "enableFileApplication", priority = 21, description = "Target Webservice = enableFileApplication, assert that file app can be enabled.")
    public void testEnableFileAppTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testEnableFileAppTC1***********");
        try {
            EnableFileApplicationConsumer getAppWSObj = new EnableFileApplicationConsumer();
            Assert.assertTrue(getAppWSObj.enableFileApplication("MLT1").isResult());
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.getMessage());
            Assert.fail("Failing this test due to exception from test class.\n Exception : "
                    + E.getMessage());
        }
    }

    @Test(groups = "enableFileApplication", priority = 22, description = "Target Webservice = enableFileApplication, assert that non existing file app cannot be enabled.")
    public void testEnableFileAppTC2() throws Exception {
        logger.log(Level.INFO,
                "***********testEnableFileAppTC2***********");
        EnableFileApplicationConsumer getAppWSObj = new EnableFileApplicationConsumer();
        Assert.assertFalse(getAppWSObj.enableFileApplication("MT1").isResult());
    }

    @Test(groups = "enableFileApplication", priority = 23, description = "Target Webservice = enableFileApplication, assert that SP apps cannot be enabled.")
    public void testEnableFileAppTC3() throws Exception {
        logger.log(Level.INFO,
                "***********testEnableFileAppTC3***********");
        EnableFileApplicationConsumer getAppWSObj = new EnableFileApplicationConsumer();
        Assert.assertFalse(getAppWSObj.enableFileApplication(spAppName).isResult());
    }

    @Test(groups = "processExportTemplate", priority = 24, description = "Target Webservice = processExportTemplate, assert that ML apps can be exported.")
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
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "processImportTemplate", priority = 25, description = "Target Webservice = processImportTemplate, assert that ML apps can be imported.")
    public void testImportTemplate() throws Exception {
        logger.log(Level.INFO,
                "***********testImportTemplate***********");
        ProcessImportTemplateConsumer WSObj = new ProcessImportTemplateConsumer();
        if (new GetApplicationsConsumer().doesApplicationExists("MLT")) {
            new DeleteApplicationConsumer().deleteApp("MLT");
        }
        Thread.sleep(2000);

        ResultObject result = WSObj.uploadTemplate("MLT", "this is desc",
                "PROFITABILITY_WEB_APP", "EssbaseCluster-1",
                "Default Application Group",
                exportFileName + ".zip", true);
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "MLDeployCube", priority = 26, description = "Target Webservice = MLDeployCube, assert that ML apps can be imported.")
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
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));
    }

    @Test(enabled = false, groups = "EssbaseLoadData", priority = 27, description = "Target Webservice = EssbaseLoadData, assert that Loading data into essbase works for ML.")
    public void testLoadDataTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testLoadDataTC1***********");
        EssbaseLoadDataConsumer wsObj = new EssbaseLoadDataConsumer();
        ResultObject result = wsObj.loadFile("ML", new File("./models/ML/inpdata.txt"), true, "OVERWRITE_EXISTING_VALUES", "data.txt", "");
        String jobName = result.getText();
        Assert.assertTrue(result.isResult(), "EssbaseLoadData webservice returned error : " + result.getText());
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));
    }

    @Test(groups = "MLRunCalculation", priority = 28, description = "Target Webservice = MLRunCalculation, assert that ML calculation job works.")
    public void testRunCalcJob() throws Exception {
        logger.log(Level.INFO,
                "***********testRunCalcJob***********");
        MLRunCalculationConsumer WSObj = new MLRunCalculationConsumer();
        ResultObject results = WSObj.runCalculation(mlAppName, "2014_January_Actual", true, true, true, "", "", "", "", "ALL_RULES");
        String jobName = results.getText();
        Assert.assertTrue(results.isResult(), "MLRunCalculation webservice failed due to this error: " + results.getText());
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));
    }

    @Test(groups = "ProcessRuleBalancing", priority = 29, description = "Target Webservice = ProcessRuleBalancing, assert that ML rule balancing is returned.")
    public void testGetRuleBalancing() throws Exception {
        logger.log(Level.INFO,
                "***********testGetRuleBalancing***********");
        MLRuleBalanceConsumer WSObj = new MLRuleBalanceConsumer();
        Assert.assertNotNull(WSObj.getRuleBalancing(mlAppName, "2014_January_Actual", ""));
    }

    @Test(groups = "ProcessLegdgerClearPOV", priority = 30, description = "Target Webservice = ProcessLegdgerClearPOV, assert that ML clear pov job works.")
    public void testClearMLPOV() throws Exception {
        logger.log(Level.INFO,
                "***********testClearMLPOV***********");
        ProcessLedgerClearPOVConsumer WSObj = new ProcessLedgerClearPOVConsumer();
        ResultObject result = WSObj.clearPOV(mlAppName, "2014_January_Actual", false, false, false, true);
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));
    }

    @Test(groups = "getStagesByApplication", priority = 31, description = "Target Webservice = getStagesByApplication, assert that SP application stages are returned.")
    public void testGetStages() throws Exception {
        logger.log(Level.INFO,
                "***********testGetStages***********");
        GetStagesByApplicationConsumer getStagesWSObj = new GetStagesByApplicationConsumer();
        Assert.assertTrue(getStagesWSObj.doesThisStageExists(spAppName, "Profitability"));
        Assert.assertFalse(getStagesWSObj.doesThisStageExists(spAppName, "YSXB"));
        Assert.assertTrue(getStagesWSObj.getStages(spAppName).length == 4);
    }

    @Test(groups = "deployCubeSP", priority = 32, description = "Target Webservice = deployCubeSP, assert that SP application calc cube deploy is successful.")
    public void testDeployCubeSPTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testDeployCubeSPTC1***********");
        SPDeployCubeConsumer WSObj = new SPDeployCubeConsumer();
        ResultObject result = WSObj.runDeployCubeJob(spAppName, CubeType.CALCULATION_CUBE, true, false, true, false, false, false);
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "deployCubeSP", priority = 33, description = "Target Webservice = deployCubeSP, assert that SP application calc cube deploy is successful.")
    public void testDeployCubeSPTC2() throws Exception {
        logger.log(Level.INFO,
                "***********testDeployCubeSPTC2***********");
        SPDeployCubeConsumer WSObj = new SPDeployCubeConsumer();
        ResultObject result = WSObj.runDeployCubeJob(spAppName, CubeType.REPORTING_CUBE, true, false, true, false, false, false);
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "RunSPCalculationConsumer", priority = 34, description = "Target Webservice = RunSPCalculationConsumer, assert that SP application calculation job successful.")
    public void testRunSPCalculationConsumer() throws Exception {
        logger.log(Level.INFO,
                "***********RunSPCalculationConsumer***********");
        GetStagesByApplicationConsumer getStagesWSObj = new GetStagesByApplicationConsumer();
        RunSPCalculationConsumer WSObj = new RunSPCalculationConsumer();
        String[] stages = getStagesWSObj.getStages(spAppName);
        for (String stage : stages) {
            logger.info("Starting calc job for stage : " + stage);
            ResultObject result = WSObj.runSPCalcJob(spAppName, "2012_January_Actual", "COST", stage, "", stage, stage, true);
            Assert.assertTrue(result.isResult());
            String jobName = result.getText();
            Assert.assertNotNull(jobName);
            GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
            Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));
        }

    }

    @Test(groups = "copyPovData", priority = 35, description = "Target Webservice = copyPovData, assert that pov copy job for sp is successfull.")
    public void testSPCopyPovData() throws Exception {
        logger.log(Level.INFO,
                "***********copyPovDataSP***********");

        ProcessCopyPOVDataConsumer WSObj = new ProcessCopyPOVDataConsumer();
        ResultObject result = WSObj.copyPOV(spAppName, "2012_January_Actual", "2009_January_Actual", true, true, true, true, true, true, false);
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "clearPovData", priority = 36, description = "Target Webservice = clearPovData, assert that clear pov job for sp is successfull.")
    public void testSPClearPovData() throws Exception {
        logger.log(Level.INFO,
                "***********testSPClearPovData***********");

        ClearPOVDataConsumer WSObj = new ClearPOVDataConsumer();
        try {
            ResultObject result = WSObj.clearPOV(spAppName, "2009_January_Actual", "Profitability", true, true, true, true, false, true, true, "_");
            Assert.assertTrue(result.isResult());
            String successText = result.getText();
            Assert.assertNotNull(successText);

        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("204"));
        }

    }

    @Test(groups = "processGenealogyExecutionPaths", priority = 37, description = "Target Webservice = processGenealogyExecutionPaths, assert that genealogy calc are executed successfully..")
    public void testprocessGenealogyExecutionPaths() throws Exception {
        logger.log(Level.INFO,
                "***********testProcessGenealogyExecutionPaths***********");

        ProcessGenealogyPathConsumer WSObj = new ProcessGenealogyPathConsumer();
        ResultObject result = WSObj.runGenCalc(spAppName, "2012_January_Actual", ProcessGenealogyPathConsumer.LayerName.COST, "1-2-3-4");
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "ProcessGenealogyPathsWithOutASOClear", priority = 38, description = "Target Webservice = ProcessGenealogyPathsWithOutASOClear, assert that genealogy calc are executed successfully..")
    public void testProcessGenealogyPathsWithOutASOClear() throws Exception {
        logger.log(Level.INFO,
                "***********testProcessGenealogyExecutionPaths***********");

        ProcessGenealogyPathsWithOutASOClear WSObj = new ProcessGenealogyPathsWithOutASOClear();
        ResultObject result = WSObj.runGenCalcWithOutASOClear(spAppName, "2012_January_Actual", ProcessGenealogyPathsWithOutASOClear.LayerName.COST, "1-2-3-4");
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "ProcessMultiPOVCalcScriptConsumer", priority = 39, description = "Target Webservice = ProcessMultiPOVCalcScriptConsumer, assert that multi pov calc are executed successfully..")
    public void testProcessMultiPOVCalcScriptConsumer() throws Exception {
        logger.log(Level.INFO,
                "***********testProcessMultiPOVCalcScriptConsumer***********");

        ProcessMultiPOVCalcScriptConsumer WSObj = new ProcessMultiPOVCalcScriptConsumer();
        ResultObject result = WSObj.runMultiPovSPCalcJob(spAppName, "2009_January_Actual", "2012_January_Actual", ProcessMultiPOVCalcScriptConsumer.LayerName.COST, true, false);
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "ClearASOCube", priority = 40, description = "Target Webservice = ClearASOCube, assert that clear aso cube job for sp is successfull.")
    public void testClearASOCube() throws Exception {
        logger.log(Level.INFO,
                "***********testClearASOCube***********");

        ClearASOCubeConsumer WSObj = new ClearASOCubeConsumer();
        ResultObject result = WSObj.clearASOCube(spAppName, "2011_January_Actual", LayerName.COST, "_");
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "deletePOV", priority = 40, description = "Target Webservice = deletePOV, assert that delete pov job for sp is successfull.")
    public void testDeleteSPPOV() throws Exception {
        logger.log(Level.INFO,
                "***********deletePOV***********");
        DeletePOVConsumer WSObj = new DeletePOVConsumer();
        ResultObject result = WSObj.deletePOV(spAppName, "2011_January_Actual");
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "deletePOV", priority = 41, description = "Target Webservice = deletePOV, assert that delete pov job for ml  is successfull.")
    public void testDeleteMLPOV() throws Exception {
        logger.log(Level.INFO,
                "***********deletePOV***********");
        DeletePOVConsumer WSObj = new DeletePOVConsumer();
        ResultObject result = WSObj.deletePOV(mlAppName, "2015_January_Actual");
        Assert.assertTrue(result.isResult());
        String jobName = result.getText();
        Assert.assertNotNull(jobName);
        GetTaskStatusByProcessNameConsumer getStatusObj = new GetTaskStatusByProcessNameConsumer();
        Assert.assertTrue(getStatusObj.waitForJobToFinish(jobName, 300));

    }

    @Test(groups = "getApplicationProperties", priority = 42, description = "Target Webservice = getApplicationProperties, assert that app props are returned successfully.")
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

    @Test(groups = "GetEssbaseApplications", priority = 43, description = "Target Webservice = GetEssbaseApplications, assert that essbase apps are returned.")
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

    @Test(groups = "GetEssbaseDimensions", priority = 44, description = "Target Webservice = GetEssbaseDimensions, assert that essbase dimension are returned correctly.")
    public void testEssbaseDimsReporting() throws Exception {
        logger.log(Level.INFO,
                "***********testEssbaseDimsReporting***********");
        GetEssbaseDimConsumer getAppWSObj = new GetEssbaseDimConsumer();
        ResultObject result = getAppWSObj.getDims("EssbaseCluster-1", spAppName + "R", spAppName + "R");
        Assert.assertTrue(result.isResult());
        String dimNames = result.getText();
        Assert.assertNotNull(dimNames);
        Assert.assertTrue(dimNames.contains("[Measures, AllocationType, Year, PL_Region, Scenario, Period, CO_Products, PL_Products, CO_Customers, PL_Customers, GL_Accounts, ACT_Activities, ACT_Activities_intra, GL_CostCenters, ACT_CostCenters, ACT_CostCenters_intra]"));

    }

    @Test(groups = "GetEssbaseDimensions", priority = 45, description = "Target Webservice = GetEssbaseDimensions, assert that essbase dimension are returned correctly.")
    public void testEssbaseDimsCalculating() throws Exception {
        logger.log(Level.INFO,
                "***********testEssbaseDimsCalculating***********");
        GetEssbaseDimConsumer getAppWSObj = new GetEssbaseDimConsumer();
        ResultObject result = getAppWSObj.getDims("EssbaseCluster-1", spAppName + "C", spAppName + "C");
        Assert.assertTrue(result.isResult());
        String dimNames = result.getText();
        Assert.assertNotNull(dimNames);
        Assert.assertTrue(dimNames.contains("[Measures, AllocationType, Year, PL_Region, Scenario, Period, CO_Products, PL_Products, CO_Customers, PL_Customers, GL_Accounts, ACT_Activities, ACT_Activities_intra, GL_CostCenters, ACT_CostCenters, ACT_CostCenters_intra]"));

    }

    @Test(enabled = true, groups = "deleteApplication", priority = 100, description = "Target Webservice = deleteApllication, assert that ML apps can be deleted.")
    public void testDeleteAppsTC1() throws Exception {
        logger.log(Level.INFO,
                "***********testDeleteAppsTC1***********");
        DeleteApplicationConsumer deleteWSObj = new DeleteApplicationConsumer();
        Assert.assertTrue(deleteWSObj.deleteApp(mlAppName).isResult());
    }

    @Test(enabled = true, groups = "deleteApplication", priority = 100, description = "Target Webservice = deleteApllication, assert that SP apps can be deleted.")
    public void testDeleteAppsTC2() throws Exception {
        logger.log(Level.INFO,
                "***********testDeleteAppsTC2***********");
        DeleteApplicationConsumer deleteWSObj = new DeleteApplicationConsumer();
        Assert.assertTrue(deleteWSObj.deleteApp(spAppName).isResult());
    }

    @Test(groups = "deleteApplication", priority = 100, description = "Target Webservice = deleteApllication, assert that deletion of nonexistant app throws exception.")
    public void testDeleteAppsTC3() throws Exception {
        logger.log(Level.INFO,
                "***********testDeleteAppsTC3***********");
        DeleteApplicationConsumer deleteWSObj = new DeleteApplicationConsumer();
        Assert.assertFalse(deleteWSObj.deleteApp("ZYX").isResult());
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
