package tests;

import com.oracle.hpcm.restclient.PCMCSRestClient;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import utils.Executor;
import utils.ReturnObject;

public class EPMAutomateTestSuite {

    static ReturnObject ro;
    static String user;
    static String port;
    static String password;
    static String url;
    static String resturl;
    static String server;
    static String domain;
    static String pathToEPMAutomate;
    static boolean staging;
    static boolean useSSL;
    static PCMCSRestClient client;
    private static final String PATH_TO_TEST_DATA = "../testdata/";
    private static final String PATH_TO_SNAPSHOTS = PATH_TO_TEST_DATA + "snapshots/";
    private static final String PATH_TO_TEMPLATES = PATH_TO_TEST_DATA + "templates/";
    private static final String PATH_TO_FILES = PATH_TO_TEST_DATA + "files/";
    private static final String SNAPSHOT_FILENAME = "BksML30_S.zip";
    private static final String TEMPLATE_FILENAME = "BksML30.zip";
    private static final String APP_NAME = "BksML30";

    @BeforeMethod
    public void beforeMethod(Method method) {
        Reporter.log("Running " + method.getName(), true);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {

            Reporter.log("Test case " + result.getMethod().getMethodName() + " is a pass.");

        } else {
            if (result.getStatus() == ITestResult.FAILURE) {

                Reporter.log("Test case " + result.getMethod().getMethodName() + " is a fail.", true);
                Throwable t = result.getThrowable();
                Reporter.log(t.getMessage(), true);

            }

            //
            //log("test have been executed.");
        }
    }

    @BeforeSuite
    public void setup() throws IOException, SAXException, ParseException, InterruptedException {

        Reporter.log("In Setup", true);
        //pathToEPMAutomate = System.getProperty("path");

        user = System.getProperty("user");
        password = System.getProperty("password");
        server = System.getProperty("server");
        domain = System.getProperty("domain");
        port = System.getProperty("port");
        staging = Boolean.getBoolean(System.getProperty("Staging"));
//        user = "epm_default_cloud_admin";
//        password = "epm_cloud";
//        server = "slcar283.usdv1.oraclecloud.com";
//        domain = "x";
//        port = "9410";
//        staging = false;
        Reporter.log("Username: " + user, true);
        Reporter.log("password: " + password, true);
        Reporter.log("Url: " + url, true);
        Reporter.log("Domain: " + domain, true);
        //Reporter.log("Path to EPM Automate: "+pathToEPMAutomate, true)resturl = url+"/epm/rest/v1";

        if (!staging) {
            url = "http://" + server + ":" + port;
            useSSL = false;
            //restuser = user;
        } else {
            url = "https://" + server;
            useSSL = true;
            //restuser = domain+"."+user;
        }

        client = new PCMCSRestClient(user, server, password, port, domain, staging, "v1", "11.1.2.3.600", 300);

    }

    @Test(priority = 0)
    public void deleteApps() throws Exception {
        Reporter.log("Use SSL? :" + useSSL);
        Reporter.log("Deleting other apps if exists.", true);
        //GetApplicationsRest getAppRest = new GetApplicationsRest();
        client.cleanEnviornment();
    }

    @Test(priority = 1)
    public void testLogin() throws IOException, InterruptedException {
        //Executor.runCommand("cd "+pathToEPMAutomate);
        Reporter.log("------------EPMAutomate Version------------", true);
        //Executor.runCommand(String.format("epmautomate"));
        Reporter.log("------------EPMAutomate Version------------", true);
        if (!staging) {
            Executor.runCommand(String.format("epmautomate login %s %s %s", user, password, url));
        } else {
            Executor.runCommand(String.format("epmautomate login %s %s %s %s", user, password, url, domain));
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
    public void testLCMImport() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate importsnapshot " + SNAPSHOT_FILENAME.replace(".zip", ""));
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM Import failed. Error: " + ro.getOutput());
    }

    @Test(priority = 4)
    public void testDeployCube() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate deploycube " + APP_NAME + " isKeepData=false isReplaceCube=true isRunNow=true");
        Assert.assertTrue(ro.getOutput().contains("success"), "Deploy cube failed. Error: " + ro.getOutput());
    }

    @Test(priority = 4)
    public void testDeployCubeError1() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate deploycube");
        Assert.assertTrue(ro.getOutput().contains("Invalid parameter: APPLICATION_NAME"), "Deploy cube error scenario failed. Error: " + ro.getOutput());
    }

    @Test(priority = 4)
    public void testDeployCubeError2() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate deploycube BksML3");
        Assert.assertTrue(ro.getOutput().contains("No options are passed to process this request"), "Deploy cube error scenario failed. Error: " + ro.getOutput());
    }

    @Test(priority = 4)
    public void testDeployCubeError3() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate deploycube " + APP_NAME + " isKeepData=true isReplaceCube=true isRunNow=true");
        Assert.assertTrue(ro.getOutput().contains("Keep Data cannot be true when Create or Replace Cube option is selected"), "Deploy cube error scenario failed. Error: " + ro.getOutput());
    }

    @Test(priority = 5)
    public void testCopyPOV() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate copypov " + APP_NAME + " 2016_January_Actual 2014_March_Actual isManageRule=true isInputData=true createDestPOV=true stringDelimiter=_");
        Assert.assertTrue(ro.getOutput().contains("success"), "CopyPOV failed. Error: " + ro.getOutput());
    }

    @Test(priority = 5)
    public void testCopyPOVError() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate copypov BksML12 2016_January_Actual 2014_March_Actual isManageRule=true isInputData=true createDestPOV=true stringDelimiter=_");
        Assert.assertTrue(ro.getOutput().contains("Invalid application name"), "CopyPOV error scenario failed. Error: " + ro.getOutput());
    }

    @Test(priority = 6)
    public void testClearPOV() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate clearpov " + APP_NAME + " 2014_March_Actual isManageRule=true isInputData=true isAdjustmentValues=true isAllocatedValues=true stringDelimiter=_");
        Assert.assertTrue(ro.getOutput().contains("success"), "ClearPOV failed. Error: " + ro.getOutput());
    }

    @Test(priority = 6)
    public void testClearPOVError1() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate clearpov " + APP_NAME + " 2014_March_Actual stringDelimiter=_");
        Assert.assertTrue(ro.getOutput().contains("No selections made to clear POV data."), "ClearPOV error scenario failed. Error: " + ro.getOutput());
    }

    @Test(priority = 7)
    public void testDeletePOV() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate deletepov " + APP_NAME + " 2016_March_Actual");
        Assert.assertTrue(ro.getOutput().contains("success"), "DeletePOV failed. Error: " + ro.getOutput());
    }

    @Test(priority = 7)
    public void testDeletePOVError() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate deletepov " + APP_NAME + " 2016_Mar_Actual");
        Assert.assertTrue(ro.getOutput().contains("Invalid dimension member group combination"), "DeletePOV failed. Error: " + ro.getOutput());
    }

    @Test(priority = 8)
    public void testRunCalc() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate runcalc " + APP_NAME + " 2016_January_Actual isClearCalculated=true isExecuteCalculations=true isRunNow=true subsetStart=10 subsetEnd=20 exeType=ALL_RULES stringDelimiter=_");
        Assert.assertTrue(ro.getOutput().contains("success"), "RunCalc failed. Error: " + ro.getOutput());
    }

    @Test(priority = 9)
    public void testExportTemplate() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate exporttemplate " + APP_NAME + " BksML30_Template.zip");
        Assert.assertTrue(ro.getOutput().contains("success"), "Export Template failed. Error: " + ro.getOutput());
    }

    @Test(priority = 10,enabled =false)
    public void testExportQuery() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate exportqueryresults " + APP_NAME + " queryName=\"Profitability - Product\" fileName=\"MyQueryResult.txt\" exportOnlyLevel0Flg=true");
        Assert.assertTrue(ro.getOutput().contains("success"), "Export Query failed. Error: " + ro.getOutput());
    }

    @Test(priority = 10)
    public void testExportQueryError1() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate exportqueryresults " + APP_NAME + " queryName=XYZ fileName=xyx.txt exportOnlyLevel0Flg=true");
        Assert.assertTrue(ro.getOutput().contains("Invalid parameter: fileName"), "Export Query error sceanrio failed. Error: " + ro.getOutput());
    }

    // Modified the expected error message.
    
    @Test(priority = 10)
    public void testExportQueryError2() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate exportqueryresults " + APP_NAME);
        Assert.assertTrue(ro.getOutput().contains("Invalid parameter: fileName"), "Export Query error sceanrio failed. Error: " + ro.getOutput());
    }

    @Test(priority = 10)
    public void testExportQueryError3() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate exportqueryresults");
        Assert.assertTrue(ro.getOutput().contains("Invalid parameter: APPLICATION_NAME"), "Export Query error sceanrio failed. Error: " + ro.getOutput());
    }

    @Test(priority = 11)
    public void testApplyDG() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate applydatagrants " + APP_NAME);
        Assert.assertTrue(ro.getOutput().contains("success"), "Data grant application failed. Error: " + ro.getOutput());
    }

    @Test(priority = 11)
    public void testApplyDGError() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate applydatagrants BksML12");
        Assert.assertTrue(ro.getOutput().contains("Invalid application name: BksML12"), "Data grant invalid app sceanrio. Error: " + ro.getOutput());
    }

    @Test(priority = 11)
    public void testApplyDGError2() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate applydatagrants");
        Assert.assertTrue(ro.getOutput().contains("Invalid parameter: APPLICATION_NAME"), "Data grant invalid app name scenario. Error: " + ro.getOutput());
    }

    @Test(priority = 12)
    public void testUploadTemplateFile() throws IOException, InterruptedException, ParseException, SAXException, Exception {
        Reporter.log("Delete the application before importing a new app.");
        client.cleanEnviornment();
        client.uploadFileToService(PATH_TO_TEMPLATES, TEMPLATE_FILENAME, "profitinbox");
    }

    @Test(priority = 13, enabled = true)
    public void testImportTemplate() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate importtemplate BksML30 " + TEMPLATE_FILENAME);

        Assert.assertTrue(ro.getOutput().contains("success"), "Import Template failed. Error: " + ro.getOutput());
    }

    @Test(priority = 15)
    public void testUploadDimFile() throws IOException, InterruptedException {
        String[] filesForUpload = {"Year.txt", "Customer_x", "Account_x", "Region_x"};
        for (String file : filesForUpload) {
            ro = Executor.runCommand("epmautomate listfiles");

            if (ro.getOutput().contains(file)) {
                Executor.runCommand("epmautomate deletefile profitinbox/"+file);
            }
            File upload = new File(PATH_TO_FILES + file);
            String uploadPath = upload.getAbsolutePath();
            ro = Executor.runCommand("epmautomate uploadfile " + uploadPath + " profitinbox");
            Assert.assertTrue(ro.getOutput().contains("success"), "file upload failed. Error: " + ro.getOutput());
        }
    }

    @Test(priority = 16, enabled = true, description = "Postive testcase for update in POV.")
    public void testLoadDimData() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate loaddimdata "+APP_NAME+" dataFileName=Year.txt");
        Assert.assertTrue(ro.getOutput().contains("success"), "Import Template failed. Error: " + ro.getOutput());
    }

    @Test(priority = 16, enabled = true, description = "negative testcase if filename parameter is not passed in the command.")
    public void testLoadDimDataError1() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate loaddimdata Bks1s2");
        Assert.assertTrue(ro.getOutput().contains("Invalid parameter: dataFileName"), "Load dimdata error failed. Error: " + ro.getOutput());
    }

    @Test(priority = 16, enabled = true, description = "Positive testcase for update of business dimension.")
    public void testLoadDimDataTC2() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate loaddimdata "+APP_NAME+" dataFileName=Year.txt");
        Assert.assertTrue(ro.getOutput().contains("success"), "Dimension upload failed.Error: " + ro.getOutput());
    }

    @Test(priority = 16, enabled = false, description = "Positive testcase for update of multiple business and attribute dimensions with delimiter .")
    public void testLoadDimDataTC3() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate loaddimdata "+APP_NAME+" dataFileName=\"Customer_x&Account_x&Region_x\" stringDelimiter=\"&\"");
        Assert.assertTrue(ro.getOutput().contains("success"), "Dimension upload failed. Error: " + ro.getOutput());
    }

    @Test(priority = 16, enabled = false, description = "Positive testcase for update of multiple business and attribute dimensions without delimiter .")
    public void testLoadDimDataTC4() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate loaddimdata "+APP_NAME+" dataFileName=\"Customer_x,Account_x,Region_x\"");
        Assert.assertTrue(ro.getOutput().contains("success"), "Dimension upload failed. Error: " + ro.getOutput());
    }

    @Test(priority = 16, enabled = true, description = "neagtive testcase for Dim file doesnot exist in folder.")
    public void testLoadDimDataError2() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate loaddimdata "+APP_NAME+" dataFileName=\"CustomerX\"");
        Assert.assertTrue(ro.getOutput().contains("does not Exist at location "), "Expected error is not thrown for neagtive testcase for Dim file doesnot exist in folder : " + ro.getOutput());
    }

    @Test(priority = 16, enabled = true, description = "neagtive testcase for invalid args.")
    public void testLoadDimDataError3() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate loaddimdata "+APP_NAME+" dataFileNam=\"CustomerX\" stringDelimite=\".\"");
        Assert.assertTrue(ro.getOutput().contains("Invalid parameter: dataFileName"), "Nagtive testcase for invalid ars does not return the correct error. " + ro.getOutput());
    }

    @Test(priority = 17, enabled = true)
    public void testEnableApp() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate enableapp "+APP_NAME);
        Assert.assertTrue(ro.getOutput().contains("success"), "enableapp failed. Error: " + ro.getOutput());
    }

    @Test(priority = 17, enabled = true)
    public void testEnableAppError1() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate enableapp");
        Assert.assertTrue(ro.getOutput().contains("Invalid parameter"), "enableapp failed. Error: " + ro.getOutput());
    }

    @Test(priority = 17, enabled = true)
    public void testEnableAppError2() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate enableapp msam");
        Assert.assertTrue(ro.getOutput().contains("Invalid application name:"), "enableapp failed. Error: " + ro.getOutput());
    }

    @Test(priority = 18)
    public void testDeployCubeAfterUploadDimension() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate deploycube "+APP_NAME+" isKeepData=false isReplaceCube=true isRunNow=true comment=TestCubeDeploy");
        Assert.assertTrue(ro.getOutput().contains("success"), "Deploy cube failed. Error: " + ro.getOutput());
    }
//@AfterSuite

    public void cleanup() throws IOException, SAXException, ParseException, InterruptedException {
        client.cleanEnviornment();
    }
}
