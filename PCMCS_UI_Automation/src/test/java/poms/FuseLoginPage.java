package poms;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

public class FuseLoginPage {
	
	WebDriver driver;
	String username;
	String password;
	String application;
	String url;
	public static int large_timeout;
	public static int small_timeout;
	private WebDriverWait wait;
	By selectApplication = By.id("Application::content");
	By userNameTextBox = By.name("j_username");
	By passWordTextBox = By.name("j_password");
	By loginButton  = By.xpath("//button[@id='signin']");
	By stagingUserNameTextBox = By.name("username");
	By stagingPassWordTextBox = By.name("password");
	By stagingLoginButton  = By.id("signin");
	By identityDomain = By.id("tenantDisplayName");
	By goButton = By.xpath("//button[@id='signin']");
	public FuseLoginPage(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,large_timeout);
		Properties properties = System.getProperties();
		username = properties.getProperty("user");
		password = properties.getProperty("password");
		application = properties.getProperty("ml_application");
		url = properties.getProperty("url");
		String s_large_timeout = System.getProperty("large_timeout");
		String s_small_timeout = System.getProperty("small_timeout");
		if(s_large_timeout==null)
			large_timeout = 100;//default
		else
			large_timeout = Integer.parseInt(s_large_timeout);
		if(s_small_timeout==null)
			small_timeout = 20;//default
		else
			small_timeout = Integer.parseInt(s_small_timeout);
		driver.get(url);
	}


	public FuseNavigatorPage doLogin() throws Exception{
		//new Select(driver.findElement(selectApplication)).selectByVisibleText(application);
		driver.findElement(userNameTextBox).clear();
		driver.findElement(userNameTextBox).sendKeys(username);
		driver.findElement(passWordTextBox).clear();
		driver.findElement(passWordTextBox).sendKeys(password);
		driver.findElement(loginButton).click();
		return (new FuseNavigatorPage(driver));
	}
	public FuseNavigatorPage doLogin(String appName) throws Exception{
		if(System.getProperty("staging").equalsIgnoreCase("true")){
			//String domain = System.getProperty("domain");
			/*driver.findElement(identityDomain).click();
			driver.findElement(identityDomain).clear();
			driver.findElement(identityDomain).sendKeys(domain);
			driver.findElement(goButton).click();*/
			
			driver.findElement(this.stagingUserNameTextBox).click();
			driver.findElement(stagingUserNameTextBox).clear();
			driver.findElement(stagingUserNameTextBox).sendKeys(username);
			driver.findElement(this.stagingPassWordTextBox).click();
			driver.findElement(stagingPassWordTextBox).clear();
			//System.out.println(password);
			driver.findElement(stagingPassWordTextBox).sendKeys(password);
			BasicUtil.wait(2);
			driver.findElement(stagingLoginButton).click();
		}
		else{
			driver.manage().timeouts().implicitlyWait(large_timeout , TimeUnit.SECONDS);
			driver.findElement(userNameTextBox).click();
			driver.findElement(userNameTextBox).clear();
			driver.findElement(userNameTextBox).sendKeys(username);
			driver.findElement(passWordTextBox).click();
			driver.findElement(passWordTextBox).clear();
			BasicUtil.wait(2);
			driver.findElement(passWordTextBox).sendKeys(password);
		
			BasicUtil.wait(2);
			driver.findElement(loginButton).click();
			FuseNavigatorPage navPage = new FuseNavigatorPage(driver);
			wait.until(ExpectedConditions.presenceOfElementLocated(navPage.navigateIconBy));
		}

		return (new FuseNavigatorPage(driver));
	}
}
