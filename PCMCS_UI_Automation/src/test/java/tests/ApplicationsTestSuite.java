package tests;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import poms.ConsoleApplicationPage;
import poms.FuseLoginPage;
import utils.BasicUtil;

public class ApplicationsTestSuite extends BaseTestSuite {
	/**
	 *  Application Page Object Model
	 */
	ConsoleApplicationPage appPage;
	/**
	 *  Classname for logging
	 */
	String className = this.getClass().getName();
	
@Test(priority=1)
public void loginTest() throws Exception{
		FuseLoginPage loginPage = new FuseLoginPage(driver);
		navPage = loginPage.doLogin(ml_application);
		BasicUtil.log(className,"Login successful.");
}

/**
 * Navigate to Application Page
 * @throws Exception
 */
@Test(priority=2,enabled=true)
	public void navigateToApplicationPage() throws Exception{
		BasicUtil.log(className," Navigating to Applications Page.");
		appPage = navPage.navigateToConsole().clickApplicationSubPageLink();
		BasicUtil.log(className,"Appname is = "+appPage.getApplication());
		BasicUtil.log(className,"Enabled? = "+appPage.isEnabledApplication());
}
@Test(priority=3,enabled=false)
public void deleteApplication() throws Exception{
	BasicUtil.log(className,"Selecting the application.");
	appPage.selectApplication();
	BasicUtil.log(className,"Deleteing application.");
	appPage.clickApplicationActionButton();
	BasicUtil.waitADF(driver, large_timeout);
	appPage.clickDeleteApplication();
	BasicUtil.waitADF(driver, large_timeout);
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[contains(.,'You are about to delete the application')]")));
	appPage.clickOkOnDialogs();
	BasicUtil.waitADF(driver, large_timeout);
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[@id='Username']")));
}
@Test(priority=4,enabled=false)
public void relogin() throws Exception{
	FuseLoginPage loginPage = new FuseLoginPage(driver);
	navPage = loginPage.doLogin(ml_application);
	BasicUtil.log(className,"Login successful.");
}

//@Test(priority=3,enabled=true)
//public void testUploadDimensionMulti() throws Exception{
//	BasicUtil.log(className,"Selecting the application.");
//	appPage.selectApplication();
//	BasicUtil.log(className,"Clicking on upload dimension.");
//	appPage.clickApplicationActionButton();
//	BasicUtil.waitADF(driver, large_timeout);
//	//UpdateDimensionPageFragment updateDimPF = appPage.clickUploadDimensionButton();
//	BasicUtil.waitADF(driver, large_timeout);
//	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[text()='Update Options']")));
//	BasicUtil.log(className,"Setting up options for upload dimension.");
//	updateDimPF.setupUpdateOptions(false, true, true, true);
//	BasicUtil.waitADF(driver, large_timeout);
//	BasicUtil.log(className,"Uploading files.");
//	updateDimPF.clickAddRow();
//	updateDimPF.clickSelectFileLink();
//	appPage.selectLocal();
//	appPage.uploadFile(new File("./data/Drivers.txt"));
//	appPage.clickOkOnDialogs();
//	BasicUtil.log(className,"Uploading files.");
//	updateDimPF.clickAddRow();
//	updateDimPF.clickSelectFileLink();
//	appPage.selectLocal();
//	appPage.uploadFile(new File("./data/Drivers.txt"));
//	appPage.clickOkOnDialogs();
//	appPage.clickOkOnDialogs();
//}
}
