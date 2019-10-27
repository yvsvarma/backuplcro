package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;

import com.oracle.hpcm.restclient.PCMCSRestClient;
import com.oracle.hpcm.utils.UserObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import poms.FuseNavigatorPage;
import utils.BasicUtil;
import utils.PropertiesUtils;

public class BaseTest {
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
	public String getScreenShot(String fileName) throws Exception 
	{
		String image64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
		return image64;
		
	}
}
