package utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
public class BasicUtil {
	
	public static WebDriver driverInstance=null; 
	public static long driverHwnd;
	public static int browserYOffset=0;
	
	public static void registerDriver(WebDriver driver){
		driverInstance=driver;
	}
	public static FirefoxProfile getFirefoxDriverProfile(String downloadPath) throws Exception {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.dir", downloadPath);
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/vnd.ms-excel,application/octet-stream,application/pdf,text/plain,text/html,application/msword,application/xml,application/excel");
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.focusWhenStarting", false);
		profile.setPreference("browser.download.manager.useWindow", false);
		profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		profile.setPreference("browser.download.manager.closeWhenDone", false);
		profile.setPreference("pdfjs.disabled",true);
		profile.setPreference("plugin.scan.plid.all", false);
		return profile;
	}
	public static void log(String message){
		Reporter.log(message,true);
	}
	public static boolean waitForNonPresenceOfElement(WebDriver driver ,By by){
	    try {
	        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	        driver.findElement(by);
	        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	        return false;
	    }
	    catch(Exception e){
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        return true;
	    }
	}
	public static WebElement findVisibleElement(WebDriver driver,By by) throws Exception{
		List<WebElement> list = driver.findElements(by);
		for(WebElement wb : list){
			if(wb.isDisplayed())
				highlightElement(driver,wb);
				return wb;
		}
		throw new Exception("Element was not visble : "+by.toString());
	}
	public static void waitForGlassPaneToBeInvisible(WebDriver driver, WebDriverWait wait){
		
		if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
		
	}
	/**
	 *Dynamic wait which waits for the ADF Client to be sync with
	 *server. 
		
	 *
	 * @param  driver  webdriver instance
	 * @param  timeout timeout in seconds
	 * @return      void
	 */
	public static void waitADF(WebDriver driver, int timeout){
		new WebDriverWait(driver, timeout).until(new ClientSynchedWithServer());
	}
	/**
	 *Returns the webdriver 
		
	 *
	 * @param  browserName  webdriver instance type firefox or internet explorer
	 * @param  profileName download path for ff or default profile, not used in IE driver
	 * @return      WebDriver
	 */
	public static WebDriver getBrowserDriver(String browserName,String profileName) throws Exception{
		
		final String OS = System.getProperty("os.name").toLowerCase();
		log("OS : "+OS);
		if(browserName.equalsIgnoreCase("firefox") && OS.indexOf("win")>=0){
			System.setProperty("webdriver.gecko.driver",".\\lib\\geckodriver.exe");
		}
		
		if(browserName.equalsIgnoreCase("firefox") && OS.indexOf("nux")>=0){
			System.setProperty("webdriver.gecko.driver",".\\lib\\geckodriver");

		}
		
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
		WebDriver driver = new FirefoxDriver();
		return driver;
	}
	
	
	
	
	
	public static boolean checkPage(WebDriver driver,String tagName,String att){
	List<WebElement> tags = driver.findElements(By.tagName(tagName));
    	//int tagcount = tags.size(); 
     System.out.println("Frequency = " + tags.size()); 
     
      for (WebElement myElement : tags){
    	 // String tag = myElement.getText(); 
    	  System.out.println(myElement.getTagName() + " : " + myElement.getAttribute(att));
    	  highlightElement(driver, myElement);
    	  
      }
      
      if(tags.size()>0){
    	  return true;
      }
      return false;
	}
	
	public static void wait(int time){
		
		try{
			//log.info("Waiting for "+time+" Seconds.");
			  Thread.currentThread();
			//do what you want to do before sleeping
			  Thread.sleep(time * 1000);//sleep for 1000 * time ms
			}
			catch(InterruptedException ie){
			}
		
	}
	public static WebElement waitForWebElement(By byPath, int timeout){
            int quantumOfWait=timeout*1000/20;
            
            for(int attempt=0;attempt<20;attempt++){
			List<WebElement> webElementList = BasicUtil.driverInstance.findElements(byPath);
			if(!webElementList.isEmpty())
			{
				return webElementList.get(0);
			}
                        try {
                            Thread.sleep(quantumOfWait);
                        } 
                        catch (InterruptedException ex) {
                            //Reporter.log(BasicUtil.class.getName()).log(Level.SEVERE, null, ex);
                        }
		}
            return null;
        }
	public static void highlightElement(WebDriver driver, WebElement element) {
		for (int i = 0; i < 2; i++) { 
			JavascriptExecutor js = (JavascriptExecutor) driver; 
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: yellow; border: 3px solid yellow;");
			//js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, ""); 
	} }
	
	public static WebElement getElementWithAttribute(List<WebElement> elementList,String attribute, String attributeValue){
		for(WebElement wb: elementList){
			
			if(wb.getAttribute(attribute)!=null)
			{
				//System.out.println(wb.getAttribute(attribute));
				if(wb.getAttribute(attribute).equals(attributeValue)){
					return wb;
				}
			}
		}
		return null;
	}
	
	public static WebElement findThatDamnWebElement(WebDriver wd, String xpath){
		try{
			WebElement we= wd.findElement(By.linkText(xpath));
			if(we!=null){
				
				highlightElement(wd,we);
				return we;
			}
			
		}
		catch(NoSuchElementException nse){
			List<WebElement> welForFrames=wd.findElements(By.tagName("frame"));
			List<WebElement> welForIFrames=wd.findElements(By.tagName("iframe"));
			
			welForFrames.addAll(welForIFrames);
			List<WebElement> wel=welForFrames;
			if(wel.size()==0){
				return null;
			}
			for(WebElement frameElement : wel){
					
					wd.switchTo().frame(frameElement);
					WebElement xPectedWE=findThatDamnWebElement(wd,xpath);
					if(xPectedWE !=null){
						highlightElement(wd,xPectedWE);
						return xPectedWE;
					}
					wd.switchTo().defaultContent();
			}
			return null;
		}
	
	return null;
	}
	
	public static void bringFocus(WebDriver driver,String id){
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("document.getElementById('"+id+"').focus();");
	}
	public static void log(String msgClass, String message){
		Reporter.log("["+msgClass+"]: " + message,true);
		//System.out.println("["+msgClass+"]: " + message);
	}
	public static void  moveMouse(WebDriver driver,WebElement wb)
	{
		try { 
		Robot robot = new Robot();
		int offsetPage=70;
		if(driver.getClass().getSimpleName().equals("InternetExplorerDriver")){
			offsetPage=55;
		}else{
			offsetPage=70;
		}
		
		int x= wb.getLocation().getX();
		int y= wb.getLocation().getY() + offsetPage;
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}catch (AWTException e) {
			e.printStackTrace();
		} 
	}
	public static void mouseOverOnElement(WebDriver driver,WebElement tagElement)
	{
	try
	{
		Actions builder=new Actions(driver);
		Point coordinate=tagElement.getLocation();
		Robot robot=new Robot();
		builder.moveToElement(tagElement, 5,5).build().perform();
		builder.moveByOffset(1, 1).build().perform();
		robot.mouseMove(coordinate.getX()+20,coordinate.getY()+100);

	}
	catch(Exception e)
	{
	//log.info("Error message in mouseOverOnElement method-->"+e);
	}
	}
	

	public static void click(WebDriver driver,WebElement element){
		Actions builder=new Actions(driver);
		Actions seriesOfActions=builder.moveToElement(element).click();
		seriesOfActions.perform();
		//executor.executeScript("arguments[0].click();", element);
	}
	
	

	
	public static void executeJS(WebDriver driver,String jsString) {
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		
		try {
			executor.executeScript(jsString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public static WebDriver getRegisteredDriver() {
		return driverInstance;
		
	}
	
	
	/**
	 *Dynamic wait which waits for the ADF Client to be sync with
	 *server. 
		
	 *
	 * @param  driver  webdriver instance
	 * @param  sourceElement source to pick up
	 * @param  destinationElement destination to drop
	 * @return      void
	 */
	public static void dragAndDrop(WebDriver driver, WebElement sourceElement, WebElement destinationElement){
		if (sourceElement.isDisplayed() && destinationElement.isDisplayed()) {
			Actions action = new Actions(driver);
			action.dragAndDrop(sourceElement, destinationElement).build().perform();
		} else {
			BasicUtil.log("Element was not displayed to drag");
		}
	}
}
