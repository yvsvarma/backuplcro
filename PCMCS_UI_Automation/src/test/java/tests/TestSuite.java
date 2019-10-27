package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.oracle.hpcm.restclient.PCMCSRestClient;
import com.oracle.hpcm.utils.UserObject;

import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import utils.BasicUtil;
import utils.PropertiesUtils;

public class TestSuite {
	public RemoteWebDriver driver;
	public Method afterFail;
	public Object nextPage;
	public WebDriverWait wait;
	public static Logger logger = Logger.getLogger("BaseTestSuite");
	public String ml_application;
	public static int large_timeout;
	public static int small_timeout;
	FuseNavigatorPage navPage;
	public static PCMCSRestClient restClient;
@BeforeTest
	public void setUp() throws Exception{
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		BasicUtil.log("In setup.");
		Properties props = new Properties();
		File propFile = new File("./conf/global.properties");
		if(propFile.exists() && propFile.canRead()){
			InputStream is = new FileInputStream(propFile);
			props.load(is);
		}
		PropertiesUtils.processGlobalProperties(props);
		UserObject currentUserCredentials = new UserObject(System.getProperty("user"),System.getProperty("password"),System.getProperty("server")
				,System.getProperty("port"),System.getProperty("epmapiversion"),System.getProperty("version"),System.getProperty("domain"),new Boolean(System.getProperty("staging")).booleanValue(),Integer.parseInt(System.getProperty("timeout"),10)); 
		restClient = new PCMCSRestClient(currentUserCredentials);
		System.setProperty("id", System.getProperty("user"));
		System.setProperty("url", PropertiesUtils.getProfitURL());
		large_timeout = Integer.parseInt(System.getProperty("timeout"), 10)/10;
		driver = (RemoteWebDriver)BasicUtil.getBrowserDriver(System.getProperty("browser"), "default");
		wait= new WebDriverWait(driver,large_timeout);
		BasicUtil.log("Browser Opened");
		ml_application = System.getProperty("ml_application");
		driver.manage().timeouts().implicitlyWait(large_timeout, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	
@AfterTest
	public void afterTestSuite(ITestContext ctx) throws Exception{
		driver.close();
		int totalNumberOfTest = ctx.getAllTestMethods().length;
		int passedCount = ctx.getPassedTests().size();
		int failedCount = ctx.getFailedTests().size();
		int skippedCount = ctx.getSkippedTests().size();
		String testSuiteName =ctx.getSuite().getName();
		System.setProperty("testreport.zip", "./test-output/");
		System.setProperty("concise.report", "./test-output/emailable-report.html");
		BasicUtil.log(testSuiteName,"Total # of tests : "+totalNumberOfTest);
		BasicUtil.log(testSuiteName,"Passed # of tests : "+passedCount);
		BasicUtil.log(testSuiteName,"Failed # of tests : "+failedCount);
		BasicUtil.log(testSuiteName,"Skipped # of tests : "+skippedCount);
		BasicUtil.log("","Is test suite a success : "+ (totalNumberOfTest==passedCount));
		String isSuccessText =(totalNumberOfTest==passedCount)?"Success":"Failure";
		BasicUtil.log("Test Suite result : "+isSuccessText);

	}

@AfterMethod
	public void afterMethod(ITestResult result) throws Exception{
		
		if(result.isSuccess()){
			BasicUtil.log("BaseTestSuite","Test case " + result.getMethod().getMethodName() + " is a pass.");
		}else{
			if(result.getStatus()==ITestResult.FAILURE){
				BasicUtil.log("BaseTestSuite","Test case " + result.getMethod().getMethodName() + " is a fail.");
				reportLogScreenshot(getScreenShot("ScreenShot"+RandomUtils.nextInt(0,9000)+".jpg"),result.getMethod().getMethodName());
				Throwable ex = result.getThrowable();
				BasicUtil.log("BaseTestSuite","Exception : "+ex.getMessage());
				try{
					//Close the driver.
					driver.close();
					navPage = setupDriverAndLogin();
					
					BasicUtil.log("re-Login successful.");
					nextPage = navPage;
							
					BasicUtil.log("Navigating to navigator menu item .");
				}
				catch(Exception e){
					driver.close();
					Assert.fail("Driver setup was disturbed by login/navigation failures.");
					throw e;
				}
			}			
		}
	}
	public String getScreenShot(String fileName) throws Exception 
	{
		String image64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
		return image64;
		
		/*try{
	        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        File destFile = new File("./test-output/"+fileName);
	        FileUtils.copyFile(srcFile,destFile);
	        
		}
		catch(Exception e){
			BasicUtil.log("Could not write the error screenshot due to an exception.");
			e.printStackTrace();
		}
		/*System.setProperty("debug","true");
		if(System.getProperty("debug")!=null)
		{
			String image64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
			return image64;
	    }else{
	    	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    	FileUtils.copyFile(scrFile, new File(fileName));
	    }*/
		//return "";
	}
	protected void reportLogScreenshot(String image64, String methodName) {
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		Reporter.log("<p align=\"left\">Error screenshot for test name "+methodName+" at " + new Date()+ "</p>");
		Reporter.log("<p><img width=\"400\" height=\"300\" src=\"data:image/png;base64," + image64  + "\" alt=\""+methodName+" failure screenshot at " + new Date()+ "\"/></p><br />");
	   
	}
	public FuseNavigatorPage setupDriverAndLogin() throws Exception{
		driver = (RemoteWebDriver) BasicUtil.getBrowserDriver(System.getProperty("browser"), "default");
		driver.manage().timeouts().implicitlyWait(large_timeout, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(System.getProperty("url"));
		wait = new WebDriverWait(driver,large_timeout);
		return new FuseLoginPage(driver).doLogin(ml_application);
	}
	public FuseNavigatorPage resetDriverToLogin() throws Exception{
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(large_timeout, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(System.getProperty("url"));
		wait = new WebDriverWait(driver,large_timeout);
		return new FuseLoginPage(driver).doLogin(ml_application);
	}
	
}
