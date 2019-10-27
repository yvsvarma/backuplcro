package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.oracle.hpcm.restclient.PCMCSRestClient;
import com.oracle.hpcm.utils.UserObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import utils.AutomationPropertyProcessor;
import utils.BasicUtil;
import utils.PropertiesUtils;
import utils.Utilities;

public class BaseTestSuite {

    public static ExtentReports reporter = null;
    public static ExtentTest currentTest = null;
    public static ExtentTest testSuite = null;
    public FuseLoginPage loginPage;
    public PCMCSRestClient restClient;
    public RemoteWebDriver driver;
    public Method afterFail;
    public FuseNavigatorPage navPage;
    public WebDriverWait wait;
    //public static Logger logger = Logger.getLogger("BaseTestSuite");
    public String ml_application = "ML";
    public static int large_timeout;
    public static int small_timeout;
    // String Node = "http://localhost:4444/wd/hub";
    public static final String IMAGE_FOLDER = "./reports/img/";
    public static final String REPORT_FOLDER = "./reports/";
    @BeforeTest
    public void setUp() throws Exception {
        //
        File reportFolder = new File(REPORT_FOLDER);
        File reportImageFolder = new File(IMAGE_FOLDER);
        FileUtils.forceMkdir(reportFolder);
        FileUtils.forceMkdir(reportImageFolder);
        reporter = new ExtentReports();
        reporter.attachReporter(new ExtentHtmlReporter(REPORT_FOLDER+this.getClass().getSimpleName()+".html"));

        //reporter.attachReporter()
        System.setProperty("org.uncommons.reportng.escape-output", "false");
        BasicUtil.log("In setup.");


        Properties prop = System.getProperties();
        AutomationPropertyProcessor.processGlobalProperties(prop);
        String s_large_timeout = System.getProperty("timeout");
        String s_small_timeout = System.getProperty("timeout");
        if (s_large_timeout == null) {
            large_timeout = 100;//default
        } else {
            large_timeout = Integer.parseInt(s_large_timeout);
        }
        if (s_small_timeout == null) {
            small_timeout = 20;//default
        } else {
            small_timeout = Integer.parseInt(s_small_timeout)/10;
        }
        //Setup logs
        //
        System.setProperty("url", PropertiesUtils.getProfitURL());
        UserObject currentUserCredentials = new UserObject(System.getProperty("user"), System.getProperty("password"), System.getProperty("server"),
                System.getProperty("port"), System.getProperty("epmapiversion"), System.getProperty("version"), System.getProperty("domain"), new Boolean(System.getProperty("staging")).booleanValue(), Integer.parseInt(System.getProperty("timeout"), 10));
        restClient = new PCMCSRestClient(currentUserCredentials);
        driver = Utilities.getDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, large_timeout);
        BasicUtil.log("Browser Opened");
        driver.manage().timeouts().implicitlyWait(large_timeout, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        loginPage = new FuseLoginPage(driver);
    }

    @AfterTest
    public void afterTestSuite(ITestContext ctx) throws Exception {
        driver.close();
        int totalNumberOfTest = ctx.getAllTestMethods().length;
        int passedCount = ctx.getPassedTests().size();
        int failedCount = ctx.getFailedTests().size();
        int skippedCount = ctx.getSkippedTests().size();
        String testSuiteName = ctx.getSuite().getName();
        System.setProperty("testreport.zip", "./test-output/");
        System.setProperty("concise.report", "./test-output/emailable-report.html");
        BasicUtil.log(testSuiteName, "Total # of tests : " + totalNumberOfTest);
        BasicUtil.log(testSuiteName, "Passed # of tests : " + passedCount);
        BasicUtil.log(testSuiteName, "Failed # of tests : " + failedCount);
        BasicUtil.log(testSuiteName, "Skipped # of tests : " + skippedCount);
        BasicUtil.log("", "Is test suite a success : " + (totalNumberOfTest == passedCount));
        String isSuccessText = (totalNumberOfTest == passedCount) ? "Success" : "Failure";
        BasicUtil.log("Test Suite result : " + isSuccessText);

    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.log("BaseTestSuite", "Test case " + result.getMethod().getMethodName() + " is a pass.");
        } else {
            if (result.getStatus() == ITestResult.FAILURE) {
                BasicUtil.log("BaseTestSuite", "Test case " + result.getMethod().getMethodName() + " is a fail.");
                getScreenShot();
                Throwable ex = result.getThrowable();
                BasicUtil.log("BaseTestSuite", "Exception : " + ex.getMessage());
                try {
                    //Close the driver.
                    driver.quit();
                    navPage = setupDriverAndLogin();

                    BasicUtil.log("re-Login successful.");
                    BasicUtil.log("Navigating to navigator menu item .");
                } catch (Exception e) {
                    driver.quit();
                    Assert.fail("Driver setup was disturbed by login/navigation failures.");
                    throw e;
                }
            }
        }
        reporter.flush();
    }

    public void getScreenShot() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String destFileName =sdf.format(timestamp) + ".png";
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(IMAGE_FOLDER + destFileName);
        FileUtils.copyFile(srcFile, destFile);
        currentTest.addScreenCaptureFromPath("./img/"+destFileName);
    }

    private String getTestName(Method caller) {
        Test testAnnotation = caller.getAnnotation(Test.class);
        if (testAnnotation != null) {
            return testAnnotation.testName();
        }
        return "";
    }

    private String getTestDescription(Method caller) {
        Test testAnnotation = caller.getAnnotation(Test.class);
        if (testAnnotation != null) {
            return testAnnotation.description();
        }
        return "";
    }

    public FuseNavigatorPage setupDriverAndLogin() throws Exception {
        driver = Utilities.getDriver();
        driver.manage().timeouts().implicitlyWait(large_timeout, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(System.getProperty("url"));
        wait = new WebDriverWait(driver, large_timeout);
        return new FuseLoginPage(driver).doLogin(ml_application);
    }

    public void log(String message) {
        currentTest.log(Status.INFO, message);
        System.out.println("["+this.getClass().getName() + "][" + currentTest.getModel().getName() + "]" + message);
    }
    
    public static void logit(String message) {
        currentTest.log(Status.INFO, message);
    }
    @BeforeMethod
    public synchronized void methodSetup(Method caller) {
        currentTest = reporter.createTest(this.getClass().getName() +"."+caller.getName(),getTestDescription(caller));
    }

}
