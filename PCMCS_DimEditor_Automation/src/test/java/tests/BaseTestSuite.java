package tests;

import com.oracle.hpcm.restclient.PCMCSRestClient;
import com.oracle.hpcm.utils.UserObject;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
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
	public static LogManager logManager = LogManager.getLogManager();
	public static Logger logger = Logger.getLogger("");

	static {
		try {
			InputStream ins = new FileInputStream(new File("./logging.properties"));
			logManager.readConfiguration(ins);
		} catch (Exception ex) {
			Logger.getLogger(BaseTestSuite.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			logger = logManager.getLogger("");
		}
	}
	//public static Logger logger = Logger.getLogger("BaseTestSuite");
	public String ml_application;
	public static int large_timeout;
	public static int small_timeout;
	// String Node = "http://localhost:4444/wd/hub";

	@BeforeTest
	public void setUp() throws Exception {
		//Setup the properties and user va

		String version = System.getProperty("java.version");
		reporter = new ExtentReports("./Report.html", true);
		//reporter.attachReporter(new ExtentHtmlReporter("./Report.html"));

		//reporter.attachReporter()
		BasicUtil.log("Java : "+version);

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
			small_timeout = Integer.parseInt(s_small_timeout) / 10;
		}
		//Setup logs
		//
		System.setProperty("url", AutomationPropertyProcessor.getProfitURL());
		UserObject currentUserCredentials = new UserObject(System.getProperty("user"), System.getProperty("password"), System.getProperty("server"),
				System.getProperty("port"), System.getProperty("epmapiversion"), System.getProperty("version"), System.getProperty("domain"), new Boolean(System.getProperty("staging")).booleanValue(), Integer.parseInt(System.getProperty("timeout"), 10));
		restClient = new PCMCSRestClient(currentUserCredentials);
		// System.out.println("hub is at "+System.getProperty("hub"));

		/*driver = Utilities.getDriver();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, large_timeout);
		BasicUtil.log("Browser Opened");
		ml_application = System.getProperty("ml_application");
		driver.manage().timeouts().implicitlyWait(large_timeout, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		loginPage = new FuseLoginPage(driver);*/
		BasicUtil.log("Out of setup.");
	}

	@AfterTest
	public void afterTestSuite(ITestContext ctx) throws Exception {
		driver.close();
		driver.quit();
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
		reporter.flush();

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
		String output = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		currentTest.addBase64ScreenShot(output);
		//currentTest.addScreenCaptureFromPath(destFileName);
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
		currentTest.log(LogStatus.INFO, message);
		System.out.println("[" + this.getClass().getName() + "][" + currentTest.getTest().getName() + "]" + message);
	}

	@BeforeMethod
	public synchronized void methodSetup(Method caller) {
		currentTest = reporter.startTest(this.getClass().getName() + "." + caller.getName(), getTestDescription(caller));
		//currentTest = reporter.createTest(this.getClass().getName() +"."+caller.getName(),getTestDescription(caller));
	}

}
