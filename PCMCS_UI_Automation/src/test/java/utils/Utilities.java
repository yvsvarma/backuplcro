package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

//import utils.reporting.ExtentTestManager;
public class Utilities {

	public static WebDriver driverInstance = null;
	public static Logger logger = Logger.getLogger("");

	public static void waitADF(WebDriver driver, int timeout) {
		new WebDriverWait(driver, timeout).until(new ClientSynchedWithServer());
	}
	public static void extractMessagesFromTable(List<WebElement> table, String filePath) throws IOException{
		File file = new File(filePath);
		//IOUtils.write(data, output);
		FileWriter writer = new FileWriter(file); 
		for(WebElement tr : table){
			List<WebElement> tds = tr.findElements(By.xpath(".//td"));
			String[] row= new String[tds.size()];
			int i =0;
			for(WebElement td: tds){
				row[i++]=td.getText();
			}
			String joinedString = String.join(";", row);
			writer.write(joinedString+"\n");
		}
		writer.close();
	}
	/**
	 * Returns the webdriver *
	 *
	 * @param browserName webdriver instance type firefox or internet explorer
	 * @param profileName download path for ff or default profile, not used in
	 * IE driver
	 * @return WebDriver
	 */
	public static RemoteWebDriver getDriver() throws Exception {

		RemoteWebDriver driver = null;
		String downloadPath = "./target";
		String seleniumHub = System.getProperty("hub");
		String browser = System.getProperty("browser");
		//log("Attached the file.");
		//modified

		if (browser == null || browser.equalsIgnoreCase("FIREFOX")) 
		{
			FirefoxOptions options = new FirefoxOptions();

			options.addPreference("browser.download.folderList", 2);
			options.addPreference("browser.download.manager.showWhenStarting", false);
			options.addPreference("browser.download.dir", downloadPath);
			options.addPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			if (seleniumHub != null) {// &&  browser.equalsIgnoreCase("FIREFOX"))
				driver = new RemoteWebDriver(new URL(seleniumHub), options);
			} else 
			{
				System.setProperty("webdriver.gecko.driver","C:\\Users\\hpcm_qa\\Downloads\\geckodriver.exe");
				//System.setProperty("webdriver.gecko.driver","C:\\Users\\skdbs\\Desktop\\automation\\browsers\\geckodriver.exe");
				driver = new FirefoxDriver(options);
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
			chromeOptions.addArguments("--disable-extensions");//to disable browser extension popup
			chromeOptions.addArguments("disable-infobars");
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

	public static FirefoxProfile getFirefoxDriverProfile(String downloadPath) throws Exception {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.dir", downloadPath);
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.ms-excel,application/octet-stream,application/pdf,text/plain,text/html,application/msword,application/xml,application/excel");
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.focusWhenStarting", false);
		profile.setPreference("browser.download.manager.useWindow", false);
		profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		profile.setPreference("browser.download.manager.closeWhenDone", false);
		profile.setPreference("pdfjs.disabled", true);
		profile.setPreference("plugin.scan.plid.all", false);
		return profile;
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
