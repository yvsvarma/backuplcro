package tests;

import com.aventstack.extentreports.Status;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import poms.ConsoleApplicationPage;
import poms.ConsolePage;
import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import poms.JobLibPage;
import poms2.DataLoadPage;
import poms2.ManageCalcPage;
import poms2.ManageDBPage;
import poms2.RuleBalancingPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;
import utils.ClientSynchedWithServer;
import utils.Executor;
import utils.ReturnObject;

public class E2ETestSuite extends BaseTestSuite {

	
	//    public static Logger logger = Logger.getLogger("TestSuiteE2E");
	String className = this.getClass().getName();
	static ReturnObject ro;

	//@Test(priority = 2, enabled = true)
	public void testLCMImportFileUpload() throws Exception 
	{
		//Executor.runCommand("cd "+pathToEPMAutomate);
		log("Started LCM Import File Uploading Process");
		String user = System.getProperty("user");
		String password = System.getProperty("password");
		String url = System.getProperty("url");
		String domain = System.getProperty("domain");
		String staging = System.getProperty("staging");
		Reporter.log("Username: " + user, true);
		Reporter.log("password: " + password, true);
		Reporter.log("Url: " + url, true);
		Reporter.log("Domain: " + domain, true);
		//Reporter.log("Path to EPM Automate: "+pathToEPMAutomate, true)
		restClient.uploadFileToService("../testdata/templates", "BksML30.zip", "profitinbox");
		restClient.importTemplate("ML", "BksML30.zip");
		log("Completed LCM Import Process");
	}



	@Test(priority = 2)
	public void loginTest() throws Exception 
	{
		testLCMImportFileUpload();
		loginPage = new FuseLoginPage(driver);
		navPage = loginPage.doLogin(ml_application);
		log("Login successful.");
		//
	}

	@Test(priority = 3, enabled = false)
	public void uploadImportApplication() throws Exception 
	{

		log("Openeing Console Page.");
		ConsolePage consolePage = navPage.navigateToConsole();

		ConsoleApplicationPage consoleAppPage = consolePage.clickApplicationSubPageLink();
		log("Checking if Appplication sub page is displayed.");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[contains(text(),'Applications')]")));
		//Copy template file into new file with random file name
		File srcFile = new File("./testdata/templates/BskML30.zip");
		File destFile = new File(srcFile.getParent() + "/" + RandomUtils.nextInt(0, 9000) + ".zip");
		FileUtils.copyFile(srcFile, destFile);
		log("Clicking on action button.");
		consoleAppPage.clickNewActionButton();
		log("Clicking on import template action.");
		consoleAppPage.clickImportTemplate();
		log("Clicking on select file link.");
		consoleAppPage.clickOnSelectFileLink();
		log("Clicking on select client radio.");
		consoleAppPage.selectLocal();
		log("Clicking on upload the template from client.");
		consoleAppPage.uploadTemplate(destFile);
		log("Clicking on OK button.");
		consoleAppPage.clickOkOnDialogs();
		//AssertJUnit.assertTrue(driver.findElement(By.xpath(".//label[contains(.,'Application Name')]/../../td[2]")).getText().contains("BksML12"));
		log("Clicking on Next button.");
		new WebDriverWait(driver, 120).until(new ClientSynchedWithServer());
		consoleAppPage.clickNextButtonImportDialog();
		log("Clicking on finish button.");
		consoleAppPage.importAppFromTemplate(System.getProperty("ml_application"));
		AssertJUnit.assertTrue(driver.findElement(By.xpath(".//div[contains(.,'Import File job has been submitted.')]")).isDisplayed());
		log("File was imported successfully");
		driver.findElement(By.xpath(".//button[contains(@id,'ok') and boolean(@_afrfoc)]")).click();
		navPage = consoleAppPage.returnToNav();

		FileUtils.forceDelete(destFile);

	}

	@Test(priority = 4, enabled = true)
	public void checkJobLibForImportJobSuccess() throws Exception 
	{
		if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		{
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
		}
		navPage.ClickSignOutDropDown(System.getProperty("id"));
		log("Clicking signout button.");
		navPage.clickSignOutLink();
		navPage.clickSignoutOKButton();
		log("Signed out.");
		FuseLoginPage loginPage = new FuseLoginPage(driver);
		navPage = loginPage.doLogin(ml_application);
		log("Opening job lib Page.");
		JobLibPage consoleJobLibPage = navPage.navigateToJobLibrary();
		log("Checking for latest job status.");
		consoleJobLibPage.waitForjobToFinish(System.getProperty("ml_application"), "Import Template", 600);
	}

	@Test(priority = 5, enabled = true)
	public void logOffAndRelogin() throws Exception 
	{
		navPage.ClickSignOutDropDown(System.getProperty("id"));
		log("Clicking signout button.");
		navPage.clickSignOutLink();
		log("Signed out.");
		navPage = navPage.clickSignoutOKButton().doLogin(ml_application);
		log("Signed out again with newly created app.");
		if (System.getProperty("staging") != null && System.getProperty("staging").equalsIgnoreCase("true")) 
		{
			driver.findElement(By.xpath("//button[@id='Confirm']")).click();
		}
	}

	@Test(priority = 6, enabled = true)
	public void deployCube() throws InterruptedException 
	{
		//log("Waiting for the cube auto dployment.");
		//Thread.sleep(100000);
		By deployCubeConfirmationDlg = By.xpath(".//*[@id='d1::msgDlg::_cnt']//div[contains(.,'Deploy Cube job')]/../../td/div");
		ManageDBPage manageDBPage = navPage.navigateToManageDB();
		log("Opened the database page.");
		//manageDBPage.clickOnCreateNewDBRadio();
		//log("Clicked on new db radio button.");
		manageDBPage.clickDeployNowButton();
		log("Clicked on Deploy now OK button.");
		manageDBPage.clickDeployDlgOKButton();
		log("Clicked on Deploy now button.");
		log("Waiting for confirmation dialog");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(deployCubeConfirmationDlg)));
		AssertJUnit.assertTrue(driver.findElement(deployCubeConfirmationDlg).getText().contains("submitted"));
		driver.findElement(By.xpath(".//button[@id='d1::msgDlg::cancel']")).click();
		log("Deploy cube job is submitted successfully.");
		//manageDBPage.clickOK();
	}

	@Test(priority = 7, enabled = true)
	public void checkJobLibForDeployCubeJobSuccess() throws Exception 
	{
		log("Checking for the Deploy Cube Job Status");
		JobLibPage joblibPage = navPage.navigateToJobLibrary();
		//ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
		joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Deploy Cube", BaseTestSuite.large_timeout * 20);
		log("Done with checking the Dpeloy Cube Job Status");
	}

	@Test(priority = 8, enabled = true)
	public void dataLoad() throws Exception 
	{
		log("Uploading the input data file to the inbox from Rest");
		restClient.uploadFileToService("../testdata/templates", "input.txt", "profitinbox");
		
		log("User is on Database Screen");
		By deployCubeConfirmationDlg = By.xpath(".//*[@id='d1::msgDlg::_cnt']//div[contains(.,'Data Load job')]/../../td/div");
		By loadButton = By.xpath(".//button[@id='f2:0:cb2' and contains (text(),'Load')]");
		DataLoadPage dataLoadPage = navPage.navigateToDataLoad();
		log("Waiting for the Essbase Load Options Text on Data Load Tab");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Essbase Load Options')]")));
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(.,'Essbase Load Options')]")));
		log("In data load page.");
		dataLoadPage.clickAddRow();
		log("adding a row.");
		dataLoadPage.clickSelectFile();
		log("Clicked on select file link.");
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//label[contains(.,'Client')]/../span")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@id='f2:0:r5:1:sbr2::content' and @type='radio']")));
		dataLoadPage.clickServerRadioButton(); 
		log("Clicked on server radio button.");
		dataLoadPage.selectInputFile();
		//dataLoadPage.clickClientRadioButton();
		log("Selected the input file from server drop down");
		//Thread.sleep(300);
		//dataLoadPage.attachDataloadFile(new File("C:\\Users\\hpcm_qa\\Downloads\\input.txt"));
		//dataLoadPage.attachDataloadFile(new File("./data/input.txt"));
		//log("Attached the file.");
		Thread.sleep(300);
		dataLoadPage.clickOKButton();
		log("Clicked on OK button.");
		//BasicUtil.waitADF(driver, large_timeout);
		//Thread.sleep(200);
		log("Clicking on the load button");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(loadButton)));
		Thread.sleep(1000);
		dataLoadPage.clickLoadButton();
		log("Clicked on Load data button.");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(deployCubeConfirmationDlg)));
		AssertJUnit.assertTrue(driver.findElement(deployCubeConfirmationDlg).getText().contains("submitted"));
		driver.findElement(By.xpath(".//button[@id='d1::msgDlg::cancel']")).click();
		log("Load data job has been submitted.");
		//manageDBPage.clickOK();
	}

	@Test(priority = 9, enabled = true)
	public void checkJobLibForLoadDataJobSuccess() throws Exception 
	{
		//FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
		JobLibPage joblibPage = navPage.navigateToJobLibrary();
		//ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
		joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Essbase Data Load", BaseTestSuite.large_timeout * 5);
		log("Load data job is successfull");
	}

	@Test(priority = 10, enabled = true)
	public void runCalculation() throws InterruptedException 
	{
		log("User is on Run Calculcations Page");
		By ledgerClacConfirmationDlg = By.xpath(".//*[@id='d1::msgDlg::_cnt']//div[contains(.,'Ledger Calculation job')]/../../td/div");
		//FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
		ManageCalcPage calcPage = navPage.navigateToManageCalculation();
		calcPage.changePOV("2016", "January", "Actual");
		Thread.sleep(5000);
		calcPage.runCalcJob();
		Thread.sleep(5000);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(ledgerClacConfirmationDlg)));
		AssertJUnit.assertTrue(driver.findElement(ledgerClacConfirmationDlg).getText().contains("submitted"));
		driver.findElement(By.xpath(".//button[@id='d1::msgDlg::cancel']")).click();
		//manageDBPage.clickOK();
	}

	@Test(priority = 11, enabled = true)
	public void checkJobLibForCalcJobSuccess() throws Exception 
	{
		JobLibPage joblibPage = navPage.navigateToJobLibrary();
		//ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
		joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Ledger Calculation", BaseTestSuite.large_timeout * 5);
		log("Run Calculations job is successfull");
	}

	@Test(priority = 12, enabled = true)
	public void getRuleBalancing() throws Exception 
	{
		log("User is on Rule Balancing Page");
		RuleBalancingPage ruleBalancePage = navPage.navigateToRuleBalancing();
		ruleBalancePage.changePOV("2016", "January", "Actual");
		ruleBalancePage.selectMVDefault();
		ruleBalancePage.clickRefresh();
		File rbExistingFile = new File("./test-output/ruleBalance.xls");
		if (rbExistingFile.exists()) 
		{
			FileUtils.forceDelete(rbExistingFile);
		}
		//ruleBalancePage.exportData();
		//ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
		//joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Ledger Calculation", 200);
	}

	@AfterMethod
	@Override
	public void afterMethod(ITestResult result) throws Exception 
	{
		if (result.isSuccess()) 
		{
			BasicUtil.waitADF(driver, large_timeout);
			currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
			navPage = ((FuseNavigatorPage) navPage).clickHomeButton();
			reporter.flush();
		} 
		else 
		{
			if (result.getStatus() == ITestResult.FAILURE) 
			{
				currentTest.fail("Test case " + result.getMethod().getMethodName() + " is a fail.");
				Throwable t = result.getThrowable();
				currentTest.log(Status.FAIL, t);
				getScreenShot();
				BasicUtil.wait(2);
				try {

					driver.quit();
					BasicUtil.wait(5);
					navPage = setupDriverAndLogin();
					wait = new WebDriverWait(driver, large_timeout);
					log("Login successful.");

					reporter.flush();
				} catch (Exception e) {
					driver.quit();
					log("Failure in aftertest method. Abandoning the setup.");
					Assert.fail("Driver setup was disturbed by login/navigation failures.");
					reporter.flush();
					throw e;
				}
			}

			//
			//log("test have been executed.");
		}
	}
}
