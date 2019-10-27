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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import static tests.EPMAutomateTestSuite.ro;
import static tests.EPMAutomateTestSuite.user;

import utils.Executor;
import utils.ReturnObject;

public class DemoE2EModelEPMAutomate {

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

    @Test(priority = 1)
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
    public void testLCMImportFileUploadE2E() throws IOException, InterruptedException {
        Executor.runCommand("epmautomate deletefile " + SNAPSHOT_FILENAME.replace(".zip", ""));
        File template = new File(PATH_TO_SNAPSHOTS + SNAPSHOT_FILENAME);
        String templatePath = template.getAbsolutePath();
        ro = Executor.runCommand("epmautomate uploadfile " + templatePath);
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM File upload failed. Error: " + ro.getOutput());
    }

    @Test(priority = 3)
    public void testLCMImport() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate importsnapshot "+SNAPSHOT_FILENAME.replace(".zip", ""));
        Assert.assertTrue(ro.getOutput().contains("success"), "LCM Import failed. Error: " + ro.getOutput());
    }

    @Test(priority = 4)
    public void testUploadDimFileE2E() throws IOException, InterruptedException {
        String[] filesForUpload = {"Year.txt", "Customer_x"};
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

    @Test(priority = 5, enabled = true)
    public void testLoadDimDataE2E() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate loaddimdata "+APP_NAME+" dataFileName=Year.txt,Customer_x");
        Assert.assertTrue(ro.getOutput().contains("success"), "Load Dimdata failed. Error: " + ro.getOutput());
    }

    @Test(priority = 6, enabled = true)
    public void testEnableApp() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate enableapp "+APP_NAME);
        Assert.assertTrue(ro.getOutput().contains("success"), "enableapp failed. Error: " + ro.getOutput());
    }

    @Test(priority = 7)
    public void testDeployCubeAfterUploadDimension() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate deploycube "+APP_NAME+" isKeepData=true isReplaceCube=false isRunNow=true ");
        Assert.assertTrue(ro.getOutput().contains("success"), "Deploy cube failed. Error: " + ro.getOutput());
    }

    @Test(priority = 8)
    public void testRunCalc() throws IOException, InterruptedException {
        ro = Executor.runCommand("epmautomate runcalc "+APP_NAME+" 2016_January_Actual isClearCalculated=true isExecuteCalculations=true isRunNow=true subsetStart=10 subsetEnd=20 exeType=ALL_RULES comment=Test stringDelimiter=_");
        Assert.assertTrue(ro.getOutput().contains("success"), "RunCalc failed. Error: " + ro.getOutput());
    }

    public void cleanup() throws IOException, SAXException, ParseException, InterruptedException {
        client.cleanEnviornment();
    }
}
