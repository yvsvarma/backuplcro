package com.oracle.hpcm.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oracle.hpcm.restclient.PCMCSRestClient;
import com.oracle.hpcm.restclient.PCMCSRestClient.AnalyticItemType;
import com.oracle.hpcm.restclient.ReturnObject;
import com.oracle.hpcm.utils.EPMAutomateUtility;
import com.oracle.hpcm.utils.UserObject;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class MigrationTestSuite {

    public static LogManager logManager = LogManager.getLogManager();
    public static Logger logger = Logger.getLogger("");

    static {
        try {
            InputStream ins = new FileInputStream(new File("./logging.properties"));
            logManager.readConfiguration(ins);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MigrationTestSuite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MigrationTestSuite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MigrationTestSuite.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            logger = logManager.getLogger("");
        }
    }

    public static String appName;
    public static PCMCSRestClient clientCurrent, clientPrevious;
    public static ExtentReports extentReport;
    public static ExtentTest currentTest;
    private static final String SNAPSHOT_FOLDER_HPCM = "/HPCM-BksML30/resource";
    private static final String PATH_TO_SCRATCH = "./scratch/";
    private static final String PATH_TO_SNAPSHOTS = "../testdata/snapshots/";
    private static final String PATH_TO_TEMPLATES = "../testdata/templates/";
    private static final String SNAPSHOT_FILENAME = "BksML30_S.zip";
    private static final String TEMPLATE_FILENAME = "BksML30.zip";
    private static final String PATH_TO_SCRATCH_EXPECTED = "./scratch/expected/";
    private static final String PATH_TO_SCRATCH_ACTUAL = "./scratch/actual/";
    private static final String PATH_TO_REPORT = "./Report.html";
    private static final String CURRENT_TEMPLATE_EXPORT = "current_template_exported";
    private static final String MIGRATION_TEMPLATE_FROM_CURRENT = "current_template_migration";
    private static final String MIGRATION_TEMPLATE_FROM_PREVIOUS = "previous_template_migration";
    private static final String BACKPORT_LCM_CURRENT = "LCM_export_backport_current";
    private static final String BACKPORT_LCM_PREVIOUS = "LCM_export_backport_previous";
    private static final String BACKPORT_CURRENT_TEMPLATE_EXPORT = "backport_current_template_export";
    private static final String BACKPORTED_PREVIOUS_TEMPLATE_EXPORT = "backported_previous_template_export";
    private static final String LCM_EXPORT_MIGRATION_CURRENT = "migrated_LCM_current_export";
    private static final String LCM_EXPORT_MIGRATION_PREVIOUS = "migration_LCM_previousF_export";
    private static final String APP_NAME = "BksML30";
    private static final String AMW_SNAPSHOT_EXPORT = "AMW_LCM_EXPORT";

    public static void logInfo(String message) {
        logger.log(Level.INFO, message);
        currentTest.log(LogStatus.INFO, message);
    }

    public static void logError(String message) {
        logger.log(Level.SEVERE, message);
        currentTest.log(LogStatus.ERROR, message);
    }
    private static final String CURRENT_SNAPSHOT_EXPORT = "current_snapshot_export";

    @BeforeClass
    public void setup() throws IOException {

        extentReport = new ExtentReports(PATH_TO_REPORT);
        currentTest = extentReport.startTest("setup");
        logInfo("***********Test Environment Setup***********");
        Properties prop = System.getProperties();
        //System.out.println(System.getProperty("domain"));
        PropertiesUtils.processGlobalProperties(prop);
        //PropertiesUtils.processModelProperties(prop);

        UserObject currentUserCredentials = new UserObject(System.getProperty("user_c"), System.getProperty("password_c"), System.getProperty("server_c"),
                System.getProperty("port_c"), System.getProperty("epmapiversion_c"), System.getProperty("version_c"), System.getProperty("domain_c"), new Boolean(System.getProperty("staging_c")).booleanValue(), Integer.parseInt(System.getProperty("timeout_c"), 10));
        clientCurrent = new PCMCSRestClient(currentUserCredentials);
        if (System.getProperty("isMigration").toLowerCase().equals("true")) {
            UserObject previousUserCredentials = new UserObject(System.getProperty("user_p"), System.getProperty("password_p"), System.getProperty("server_p"),
                    System.getProperty("port_p"), System.getProperty("epmapiversion_p"), System.getProperty("version_p"), System.getProperty("domain_p"), new Boolean(System.getProperty("staging_p")).booleanValue(), Integer.parseInt(System.getProperty("timeout_p"), 10));
            clientPrevious = new PCMCSRestClient(previousUserCredentials);
        }
        FileUtils.forceMkdir(new File(PATH_TO_SCRATCH_EXPECTED));
        FileUtils.forceMkdir(new File(PATH_TO_SCRATCH_ACTUAL));
        currentTest.log(LogStatus.PASS, "Passed!");
        //extentReport.endTest(currentTest);
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        currentTest = extentReport.startTest(method.getName());
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.isSuccess() == true) {
            currentTest.log(LogStatus.PASS, result.getTestName() + " is Passed!");
            logger.info(currentTest.getTest().getName() + " has Passed.");
        } else {

            currentTest.log(LogStatus.FAIL, result.getTestName() + " is Fail!");
            currentTest.log(LogStatus.FATAL, result.getThrowable());
            logger.info(currentTest.getTest().getName() + " has failed.");
            logger.log(Level.SEVERE, "failure.", result.getThrowable());

        }
        extentReport.flush();
        extentReport.endTest(currentTest);
    }

    // @AfterClass
    public void tearDown() throws IOException {
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_EXPECTED));
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_ACTUAL));
        extentReport.endTest(currentTest);
        extentReport.flush();
        //extentReport.close();
    }

    public void templateImportExportDownload(PCMCSRestClient restClient, String sourceTemplatePath, String importedTemplateName, String destExportTemplatePath, String exportedTemplateName) throws Exception {
        logInfo("Cleaning " + restClient.getUserObject().getServer() + ".");
        restClient.cleanEnviornment();
        logInfo("Uploading file to the service.");
        //EPMAutomateUtility.uploadFileOverwrite(clientCurrent.getUserObject(), PATH_TO_TEMPLATE,TEMPLATE_FILENAME, "proofitinbox");
        restClient.uploadFileToService(sourceTemplatePath, importedTemplateName, "profitinbox");
        logInfo("Importing Template for BksML30 on " + restClient.getUserObject().getServer() + ".");
        restClient.importTemplate(APP_NAME, importedTemplateName);
        logInfo("Exporting Template for BksML30  on " + restClient.getUserObject().getServer() + ".");
        restClient.exportTemplate(APP_NAME, exportedTemplateName);
        logInfo("Downloading exported template  on " + restClient.getUserObject().getServer() + ".");
        restClient.downloadFile("profitoutbox", exportedTemplateName + ".zip", destExportTemplatePath);
        logInfo("Downloading exported template from  " + exportedTemplateName + ".zip from " + restClient.getUserObject().getServer() + ".");
    }

    public void LCMImportExportDownload(PCMCSRestClient restClient, String sourceSnapshotPath, String snapshotName, String destSnapshotPath, String exportedSnapshotName) throws Exception {
        logInfo("Cleaning " + restClient.getUserObject().getServer() + ".");
        restClient.cleanEnviornment();
        restClient.epmAutomateLogin();
        ReturnObject ro;
        restClient.runCommand("epmautomate deletefile " + snapshotName);
        ro = restClient.runCommand("epmautomate uploadfile " + sourceSnapshotPath + snapshotName + ".zip");
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM File upload failed. Error: " + ro.getOutput());
        logInfo("Performing LCM Import to system " + restClient.getUserObject().getServer() + ".");
        ro = restClient.runCommand("epmautomate importsnapshot " + snapshotName);
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM import failed for  LCM. Error: " + ro.getOutput());
        logInfo("Performing LCM EXPORT from " + restClient.getUserObject().getServer() + ".");
        ro = restClient.runCommand("epmautomate exportsnapshot " + snapshotName);
        Assert.assertTrue(ro.getOutput().contains("success"), "Export LCM failed. Error: " + ro.getOutput());
        logInfo("Downloading file from system " + restClient.getUserObject().getServer() + ".");
        ro = restClient.runCommand("epmautomate downloadfile " + snapshotName + " " + destSnapshotPath + exportedSnapshotName + ".zip");
        Assert.assertTrue(ro.getOutput().contains("success"), "Download of LCM failed. Error: " + ro.getOutput());

        restClient.epmautomateLogout();
    }

    public void LCMImportAMWExport(PCMCSRestClient restClient, String sourceSnapshotPath, String snapshotName, String destSnapshotPath, String exportedSnapshotName) throws Exception {
        logInfo("Cleaning " + restClient.getUserObject().getServer() + ".");
        restClient.cleanEnviornment();
        restClient.epmAutomateLogin();
        ReturnObject ro;
        restClient.runCommand("epmautomate deletefile " + snapshotName);
        ro = restClient.runCommand("epmautomate uploadfile " + sourceSnapshotPath + snapshotName + ".zip");
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM File upload failed. Error: " + ro.getOutput());
        logInfo("Performing LCM Import to system " + restClient.getUserObject().getServer() + ".");
        ro = restClient.runCommand("epmautomate importsnapshot " + snapshotName);
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM import failed for  LCM. Error: " + ro.getOutput());
        logInfo("Performing AMW Run from " + restClient.getUserObject().getServer() + ".");
        ro = restClient.runCommand("epmautomate runDailyMaintenance -f");
        Assert.assertTrue(ro.getOutput().contains("success"), "AMW failed. Error: " + ro.getOutput());
        logInfo("Performing AMW Run finished on " + restClient.getUserObject().getServer() + ".");
        logInfo("Performing LCM EXPORT from " + restClient.getUserObject().getServer() + ".");
        ro = restClient.runCommand("epmautomate exportsnapshot " + snapshotName);
        Assert.assertTrue(ro.getOutput().contains("success"), "Export LCM failed. Error: " + ro.getOutput());
        logInfo("Downloading file from system " + restClient.getUserObject().getServer() + ".");
        ro = restClient.runCommand("epmautomate downloadfile " + snapshotName + " " + destSnapshotPath + exportedSnapshotName + ".zip");
        Assert.assertTrue(ro.getOutput().contains("success"), "Download of LCM failed. Error: " + ro.getOutput());

        restClient.epmautomateLogout();
    }

    public SoftAssert executeAnalyticsAndAssert(PCMCSRestClient restClient, SoftAssert s_assert, String expectedIntelligenceFolderPath) throws Exception {
        logInfo("Running analytics.");
        File expectedDir = new File(expectedIntelligenceFolderPath + "AnalysisViews");
        runAnalytics(restClient, expectedDir, AnalyticItemType.ANALYSIS_VIEW, s_assert, currentTest);
        expectedDir = new File(expectedIntelligenceFolderPath + "ProfitCurves");
        runAnalytics(restClient, expectedDir, AnalyticItemType.WHALE, s_assert, currentTest);
        expectedDir = new File(expectedIntelligenceFolderPath + "ScatterAnalysis");
        runAnalytics(restClient, expectedDir, AnalyticItemType.SCATTER, s_assert, currentTest);
        return s_assert;
    }

    @Test(priority = 1, enabled = true)
    public void templateImportExportCurrentMachine() throws Exception {
        // extentReport = new ExtentReports("Report.html");

        logInfo("---------------------------Current template import testing-----------------------------");
        templateImportExportDownload(clientCurrent, PATH_TO_TEMPLATES, TEMPLATE_FILENAME, PATH_TO_SCRATCH, CURRENT_TEMPLATE_EXPORT);

        logInfo("Unzipping imported and exported templates from current system.");

        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_EXPECTED));
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_ACTUAL));
        ZipUtils.unzip(PATH_TO_TEMPLATES + TEMPLATE_FILENAME, PATH_TO_SCRATCH_EXPECTED);
        ZipUtils.unzip(PATH_TO_SCRATCH + CURRENT_TEMPLATE_EXPORT + ".zip", PATH_TO_SCRATCH_ACTUAL);

        String expectedTemplateFolderPath = PATH_TO_SCRATCH_EXPECTED + TEMPLATE_FILENAME.replace(".zip", "");
        String actualTemplateFolderPath = PATH_TO_SCRATCH_ACTUAL + CURRENT_TEMPLATE_EXPORT;
        SoftAssert s_assert = new SoftAssert();
        FolderCompareUtil.verifyDirsAreEqual(new File(expectedTemplateFolderPath), new File(actualTemplateFolderPath), s_assert, currentTest);

        //read all analytic items and run, verify that they all go fine
        s_assert = executeAnalyticsAndAssert(clientCurrent, s_assert, expectedTemplateFolderPath + "/Analytics/Intelligence/");
        s_assert.assertAll();

        //currentTest.log(LogStatus.PASS, "Passed!!");
        //xtentReport.endTest(currentTest);
    }
//\scratch\actual\PCM_BksML30_20170321_075446\Analytics\Dashborads\Six Months Trending Analysis - Department Stores
//10 Chain Profit Margin Percent Trailing 6 Months

    @Test(priority = 3, enabled = true)
    public void backportingTemplate() throws Exception {
        logInfo("Backport Testing Current To Previous");
        logInfo("Expected model after exporting from current system zipFile : ./scratch/" + BACKPORT_CURRENT_TEMPLATE_EXPORT + ".zip");
        logInfo("Actual model after backporting to previous system File : ./scratch/" + BACKPORTED_PREVIOUS_TEMPLATE_EXPORT + ".zip");
        logInfo("---------------------------Backport testing-----------------------------");
        if (clientPrevious == null) {
            throw new SkipException("Skipping because migration and backport testing is not enabled.");
        }
        templateImportExportDownload(clientCurrent, PATH_TO_TEMPLATES, TEMPLATE_FILENAME, PATH_TO_SCRATCH, BACKPORT_CURRENT_TEMPLATE_EXPORT);
        templateImportExportDownload(clientPrevious, PATH_TO_SCRATCH, BACKPORT_CURRENT_TEMPLATE_EXPORT + ".zip", PATH_TO_SCRATCH, BACKPORTED_PREVIOUS_TEMPLATE_EXPORT);

        logInfo("Unzip the files.");

        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_EXPECTED));
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_ACTUAL));

        ZipUtils.unzip(PATH_TO_SCRATCH + BACKPORT_CURRENT_TEMPLATE_EXPORT + ".zip", PATH_TO_SCRATCH_EXPECTED);
        ZipUtils.unzip(PATH_TO_SCRATCH + BACKPORTED_PREVIOUS_TEMPLATE_EXPORT + ".zip", PATH_TO_SCRATCH_ACTUAL);

        String expectedTemplateFolderPath = PATH_TO_SCRATCH_EXPECTED + BACKPORT_CURRENT_TEMPLATE_EXPORT;
        String actualTemplateFolderPath = PATH_TO_SCRATCH_ACTUAL + BACKPORTED_PREVIOUS_TEMPLATE_EXPORT;
        SoftAssert s_assert = new SoftAssert();
        FolderCompareUtil.verifyDirsAreEqual(new File(expectedTemplateFolderPath), new File(actualTemplateFolderPath), s_assert, currentTest);

        //read all analytic items and run, verify that they all go fine
        s_assert = executeAnalyticsAndAssert(clientPrevious, s_assert, expectedTemplateFolderPath + "/Analytics/Intelligence/");
        s_assert.assertAll();

        // currentTest.log(LogStatus.PASS, "Passed!!");
    }

    @Test(priority = 2, enabled = true)
    public void LCMCurrentMachine() throws Exception {

        UserObject uo = clientCurrent.getUserObject();
        logInfo("---------------------------Current LCM import testing-----------------------------");
        logInfo("Current LCM import testing.");
        FileUtils.copyFileToDirectory(new File(PATH_TO_SNAPSHOTS + SNAPSHOT_FILENAME), new File(PATH_TO_SCRATCH));

        logInfo("Expected LCM File : ./scratch/" + SNAPSHOT_FILENAME);
        logInfo("Actual LCM File after export : ./scratch/" + CURRENT_SNAPSHOT_EXPORT + ".zip");
        ReturnObject ro;
        String snapshot_name = SNAPSHOT_FILENAME.replace(".zip", "");

        LCMImportExportDownload(clientCurrent, PATH_TO_SNAPSHOTS, snapshot_name, PATH_TO_SCRATCH, CURRENT_SNAPSHOT_EXPORT);

        logInfo("Unzipping imported and exported templates from current system.");
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_EXPECTED));
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_ACTUAL));
        ZipUtils.unzip(PATH_TO_SNAPSHOTS + SNAPSHOT_FILENAME, PATH_TO_SCRATCH_EXPECTED);
        ZipUtils.unzip(PATH_TO_SCRATCH + CURRENT_SNAPSHOT_EXPORT + ".zip", PATH_TO_SCRATCH_ACTUAL);

        SoftAssert s_assert = new SoftAssert();
        FolderCompareUtil.verifyDirsAreEqual(new File(PATH_TO_SCRATCH_EXPECTED + SNAPSHOT_FOLDER_HPCM), new File(PATH_TO_SCRATCH_ACTUAL + SNAPSHOT_FOLDER_HPCM), s_assert, currentTest);
        //read all analytic items and run, verify that they alll go fine.
        String path = PATH_TO_SCRATCH_EXPECTED + "/HPCM-BksML30/resource/Analytics/Intelligence/";
        s_assert = executeAnalyticsAndAssert(clientCurrent, s_assert, path);
        s_assert.assertAll();
    }
//\scratch\actual\PCM_BksML30_20170321_075446\Analytics\Dashborads\Six Months Trending Analysis - Department Stores
//10 Chain Profit Margin Percent Trailing 6 Months

    @Test(priority = 6, enabled = true)
    public void migrationTemplateFromPreviousToCurrent() throws Exception {

        logInfo("Actual Model after migration to the current system : " + MIGRATION_TEMPLATE_FROM_CURRENT);
        logInfo("Expected Model from previous system : " + MIGRATION_TEMPLATE_FROM_PREVIOUS);
        logInfo("Migration of Template Previous To Current");
        logger.info("---------------------------Migration testing-----------------------------");
        if (clientPrevious == null) {
            //currentTest.setState("Skipped");
            throw new SkipException("Skipping because migration and backport testing is not enabled.");
        }
        templateImportExportDownload(clientPrevious, PATH_TO_TEMPLATES, TEMPLATE_FILENAME, PATH_TO_SCRATCH, MIGRATION_TEMPLATE_FROM_PREVIOUS);
        templateImportExportDownload(clientCurrent, PATH_TO_SCRATCH, MIGRATION_TEMPLATE_FROM_PREVIOUS + ".zip", PATH_TO_SCRATCH, MIGRATION_TEMPLATE_FROM_CURRENT);

        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_EXPECTED));
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_ACTUAL));
        ZipUtils.unzip(PATH_TO_SCRATCH + MIGRATION_TEMPLATE_FROM_PREVIOUS + ".zip", PATH_TO_SCRATCH_EXPECTED);
        ZipUtils.unzip(PATH_TO_SCRATCH + MIGRATION_TEMPLATE_FROM_CURRENT + ".zip", PATH_TO_SCRATCH_ACTUAL);
        //File dir = new File("./scratch/actual/template_export_current");
        SoftAssert s_assert = new SoftAssert();
        logger.info("---------------------------Verify-----------------------------");
        FolderCompareUtil.verifyDirsAreEqual(new File(PATH_TO_SCRATCH_EXPECTED + MIGRATION_TEMPLATE_FROM_PREVIOUS), new File(PATH_TO_SCRATCH_ACTUAL + MIGRATION_TEMPLATE_FROM_CURRENT), s_assert, currentTest);
        //read all analytic items and run, verify that they alll go fine. this will read from the previous
        //template and run on the current machine to see all are fine

        s_assert = executeAnalyticsAndAssert(clientCurrent, s_assert, PATH_TO_SCRATCH_EXPECTED + MIGRATION_TEMPLATE_FROM_PREVIOUS + "/Analytics/Intelligence/");
        //s_assert.assertAll();
    }

    @Test(priority = 7, enabled = true)
    public void migrationLCM() throws Exception {

        String snapshot_name = SNAPSHOT_FILENAME.replace(".zip", "");
        logInfo("---------------------------Migration LCM testing-----------------------------");
        logInfo("Expected LCM File before migration exported out of previous system : ./scratch/" + LCM_EXPORT_MIGRATION_PREVIOUS);
        logInfo("Actual LCM File after migration exported out of current system : ./scratch/" + LCM_EXPORT_MIGRATION_CURRENT);

        if (clientPrevious == null) {
            throw new SkipException("Skipping because migration and backport testing is not enabled.");
        }
        LCMImportExportDownload(clientPrevious, PATH_TO_SNAPSHOTS, snapshot_name, PATH_TO_SCRATCH, LCM_EXPORT_MIGRATION_PREVIOUS);
        LCMImportExportDownload(clientCurrent, PATH_TO_SCRATCH, LCM_EXPORT_MIGRATION_PREVIOUS, PATH_TO_SCRATCH, LCM_EXPORT_MIGRATION_CURRENT);

        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_EXPECTED));
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_ACTUAL));
        ZipUtils.unzip(PATH_TO_SCRATCH + LCM_EXPORT_MIGRATION_PREVIOUS + ".zip", PATH_TO_SCRATCH_EXPECTED);
        ZipUtils.unzip(PATH_TO_SCRATCH + LCM_EXPORT_MIGRATION_CURRENT + ".zip", PATH_TO_SCRATCH_ACTUAL);
        //File dir = new File("./scratch/actual/template_export_current");
        SoftAssert s_assert = new SoftAssert();
        FolderCompareUtil.verifyDirsAreEqual(new File(PATH_TO_SCRATCH_EXPECTED + SNAPSHOT_FOLDER_HPCM), new File(PATH_TO_SCRATCH_ACTUAL + SNAPSHOT_FOLDER_HPCM), s_assert, currentTest);
        //read all analytic items and run, verify that they alll go fine.

        //read all analytic items and run, verify that they alll go fine. this will read from the previous
        //template and run on the current machine to see all are fine
        String path = PATH_TO_SCRATCH_EXPECTED + "HPCM-BksML30/resource/Analytics/Intelligence/";
        s_assert = executeAnalyticsAndAssert(clientCurrent, s_assert, path);
        s_assert.assertAll();
    }

    @Test(priority = 6, enabled = true)
    public void backportingLCM() throws Exception {

        logger.info("---------------------------Backport LCM testing-----------------------------");
        logInfo("Expected File exported from current to be backported :" + PATH_TO_SCRATCH + BACKPORT_LCM_CURRENT + ".zip");
        logInfo("Actual File exported from previous after backporting :" + PATH_TO_SCRATCH + BACKPORT_LCM_PREVIOUS + ".zip");

        if (clientPrevious == null) {
            throw new SkipException("Skipping because migration and backport testing is not enabled.");
        }

        String snapshot_name = SNAPSHOT_FILENAME.replace(".zip", "");

        LCMImportExportDownload(clientPrevious, PATH_TO_SNAPSHOTS, snapshot_name, PATH_TO_SCRATCH, BACKPORT_LCM_CURRENT);
        LCMImportExportDownload(clientCurrent, PATH_TO_SCRATCH, BACKPORT_LCM_CURRENT, PATH_TO_SCRATCH, BACKPORT_LCM_PREVIOUS);

        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_EXPECTED));
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_ACTUAL));
        ZipUtils.unzip(PATH_TO_SCRATCH + BACKPORT_LCM_CURRENT + ".zip", PATH_TO_SCRATCH_EXPECTED);
        ZipUtils.unzip(PATH_TO_SCRATCH + BACKPORT_LCM_PREVIOUS + ".zip", PATH_TO_SCRATCH_ACTUAL);
        //File dir = new File("./scratch/actual/template_export_current");
        SoftAssert s_assert = new SoftAssert();
        FolderCompareUtil.verifyDirsAreEqual(new File(PATH_TO_SCRATCH_EXPECTED + SNAPSHOT_FOLDER_HPCM), new File(PATH_TO_SCRATCH_ACTUAL + SNAPSHOT_FOLDER_HPCM), s_assert, currentTest);
        //read all analytic items and run, verify that they alll go fine.

        //read all analytic items and run, verify that they alll go fine. this will read from the previous
        //template and run on the current machine to see all are fine
        String path = PATH_TO_SCRATCH_EXPECTED + "HPCM-BksML30/resource/Analytics/Intelligence/";
        s_assert = executeAnalyticsAndAssert(clientPrevious, s_assert, path);
        s_assert.assertAll();
    }
/*
    @Test(priority = 7, enabled = true)
    public void AMWUpgrade() throws Exception {

        logInfo("---------------------------AMW LCM import testing-----------------------------");
        UserObject uo = clientCurrent.getUserObject();
        logInfo("---------------------------Current LCM import testing-----------------------------");
        logInfo("Current LCM import testing.");
        FileUtils.copyFileToDirectory(new File(PATH_TO_SNAPSHOTS + SNAPSHOT_FILENAME), new File(PATH_TO_SCRATCH));

        logInfo("Expected LCM File : ./scratch/" + SNAPSHOT_FILENAME);
        logInfo("Actual LCM File after export : ./scratch/" + AMW_SNAPSHOT_EXPORT + ".zip");
        ReturnObject ro;
        String snapshot_name = SNAPSHOT_FILENAME.replace(".zip", "");

        LCMImportAMWExport(clientCurrent, PATH_TO_SNAPSHOTS, snapshot_name, PATH_TO_SCRATCH, AMW_SNAPSHOT_EXPORT);

        logInfo("Unzipping imported and exported templates from current system.");
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_EXPECTED));
        FileUtils.cleanDirectory(new File(PATH_TO_SCRATCH_ACTUAL));
        ZipUtils.unzip(PATH_TO_SNAPSHOTS + SNAPSHOT_FILENAME, PATH_TO_SCRATCH_EXPECTED);
        ZipUtils.unzip(PATH_TO_SCRATCH + AMW_SNAPSHOT_EXPORT + ".zip", PATH_TO_SCRATCH_ACTUAL);

        SoftAssert s_assert = new SoftAssert();
        FolderCompareUtil.verifyDirsAreEqual(new File(PATH_TO_SCRATCH_EXPECTED + SNAPSHOT_FOLDER_HPCM), new File(PATH_TO_SCRATCH_ACTUAL + SNAPSHOT_FOLDER_HPCM), s_assert, currentTest);
        //read all analytic items and run, verify that they alll go fine.
        String path = PATH_TO_SCRATCH_EXPECTED + "/HPCM-BksML30/resource/Analytics/Intelligence/";
        s_assert = executeAnalyticsAndAssert(clientCurrent, s_assert, path);
        s_assert.assertAll();
    }
*/
    public void runAnalytics(PCMCSRestClient client, File folder, AnalyticItemType itemType, SoftAssert s_assert, ExtentTest current) throws ParseException, Exception {
        File expectedDir = folder;
        if (!expectedDir.exists()) {
            return;
        }
        LinkedList<File> filelist = (LinkedList<File>) FileUtils.listFiles(expectedDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file : filelist) {
            logInfo("Checking " + itemType.name() + ": " + file.getName());
            if (null == client.runAnalyticItem(APP_NAME, file.getName(), itemType)) {
                s_assert.assertNotNull(null, "Failed Running " + itemType.toString() + ": " + file.getName());
                logError(file.getName() + ":Failed Running " + itemType.toString() + ". Possible failed import in the current machine.");

            }
        }
    }
    //s_assert.assertAll();
}
