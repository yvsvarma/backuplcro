package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oracle.pcmcs.logging.CustomLogger;
import com.oracle.pcmcs.logging.ILogger;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

//import utils.reporting.ExtentTestManager;
public class Utilities {

	public static WebDriver driverInstance = null;
	public static ILogger logger = CustomLogger.getLogger(Utilities.class);

	public static void waitADF(WebDriver driver, int timeout) {
		new WebDriverWait(driver, timeout).until(new ClientSynchedWithServer());
	}



	/**
	 * Returns the webdriver *
	 *
	 * @param browserName webdriver instance type firefox or internet explorer
	 * @param profileName download path for ff or default profile, not used in
	 * IE driver
	 * @return WebDriver
	 */
	public static RemoteWebDriver getDriver() throws Exception 
	{
		RemoteWebDriver driver = null;
		String downloadPath = "./target";
		String seleniumHub = System.getProperty("hub");
				//System.getProperty("hub");
		String browser = System.getProperty("browser");

		if (browser == null || browser.equalsIgnoreCase("FIREFOX")) 
		{
			DesiredCapabilities capability = DesiredCapabilities.firefox();
			capability.setBrowserName("firefox");
			//capability.setPlatform(Platform.LINUX);
			//            capabilities.setCapability(FirefoxDriver.PROFILE, profile);
			//            profile.setPreference("browser.download.folderList", 2);
			//            profile.setPreference("browser.download.manager.showWhenStarting", false);
			//            profile.setPreference("browser.download.dir", downloadPath);
			//            profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			if (seleniumHub != null) 
			{// &&  browser.equalsIgnoreCase("FIREFOX"))
				System.out.println("hub is at "+seleniumHub);
				driver = new RemoteWebDriver(new URL(seleniumHub),capability);
			} else 
			{
				driver = new FirefoxDriver();
			}
		}
		else if (browser.equalsIgnoreCase("CHROME")) 
		{
			HashMap<String, Object> chromePrefs = new HashMap<>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", downloadPath);
			ChromeOptions chromeOptions = new ChromeOptions();
			HashMap<String, Object> chromeOptionsMap = new HashMap<>();
			chromeOptions.setExperimentalOption("prefs", chromePrefs);
			chromeOptions.addArguments("--test-type");
			chromeOptions.addArguments("--disable-extensions"); //to disable browser extension popup

			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			if (seleniumHub != null) 
			{
				driver = new RemoteWebDriver(new URL(seleniumHub),cap);
			}
			else
			{
				System.setProperty("webdriver.chrome.driver", "C:\\Users\\hpcm_qa\\Downloads\\chromedriver.exe");
				//System.setProperty("webdriver.chrome.driver", "C:\\Users\\skdbs\\Desktop\\automation\\browsers\\chromedriver.exe");
				driver = new ChromeDriver(chromeOptions);
			}
		}
		return driver;
	}



	public static void wait(int time) {

		try {
			//log.info("Waiting for "+time+" Seconds.");
			Thread.currentThread();
			//do what you want to do before sleeping
			Thread.sleep(time * 1000);//sleep for 1000 * time ms
		} catch (InterruptedException ie) {
		}

	}

}
