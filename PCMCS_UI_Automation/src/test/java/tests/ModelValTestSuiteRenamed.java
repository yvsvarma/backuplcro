package tests;

import com.aventstack.extentreports.Status;
import com.oracle.hpcm.utils.EPMAutomateUtility;
import com.oracle.hpcm.utils.UserObject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import poms2.ModelValidationPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;
import utils.ReturnObject;
import utils.Utilities;

public class ModelValTestSuiteRenamed extends BaseTestSuite {

    public static Logger logger = Logger.getLogger("TestSuiteE2E");
    String className = this.getClass().getName();
    static ReturnObject ro;
    ModelValidationPage mvpage;
    By modelValProgressBar = By.xpath(".//td[text()='Validating Artifacts']");
    
    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            //mvpage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToModelValidationPage();
            reporter.flush();
        } else {
            if (result.getStatus() == ITestResult.FAILURE) {
                currentTest.fail("Test case " + result.getMethod().getMethodName() + " is a fail.");
                log("Test case " + result.getMethod().getMethodName() + " is a fail.");
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
                    log(" Navigating to Model Validations.");
                    mvpage = navPage.navigateToModelValidationPage();
                    reporter.flush();
                } catch (Exception e) {
                    driver.quit();
                    log("Failure in aftertest method. Abandoning the setup.");
                    org.testng.Assert.fail("Driver setup was disturbed by login/navigation failures.");
                    reporter.flush();
                    throw e;
                }
            }

            //
            //log("test have been executed.");
        }
    }
   /* @Test(priority = 1, enabled = false)
    public void testLCMImportFileUpload() throws Exception {
        //Executor.runCommand("cd "+pathToEPMAutomate);
        ml_application = "BksML30";
        UserObject uo = restClient.getUserObject();
        restClient.cleanEnviornment();
        Assert.assertTrue(EPMAutomateUtility.uploadFileOverwrite(uo, "../testdata/templates", "MVD_Template.zip", "profitinbox"), "Upload of LCM file failed.");
        restClient.importTemplate("BksML30", "MVD_Template.zip");
    }
*/
    @Test(priority = 2, enabled = true)
    public void loginTest() throws Exception {
        FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
        mvpage = navPage.navigateToModelValidationPage();

    }

  
    
     @Test(priority = 3, enabled = true)
    public void uploadDimensionFileRename() throws Exception {
        log("Load dimension to the application.");
        ml_application = "BksML30";
        UserObject uo = restClient.getUserObject();
        EPMAutomateUtility.uploadFileOverwrite(uo, "../testdata/files", "Account_Renamed.txt", "profitinbox");
        Assert.assertTrue(restClient.uploadDimension(ml_application, "Account_Renamed.txt"), "Update dimension failed for Account_mv.txt");
       Assert.assertTrue( restClient.deployCube(ml_application, true, false, true, ""),"Deploy cube job failed.");
    }

    @Test(priority = 7, enabled = true)
    public void getMVforAnalyticsRenameMember() throws IOException, Exception {
        String actualFilePath = "./target/Analytics_Rename.txt";
        String expectedFilePath = "../testdata/files/Analytics_Rename.txt";
        SoftAssert assertions = new SoftAssert();
        log("Opening tab for Analytics.");
        mvpage.clickTab(mvpage.analyticsTab);
        log("Clicking on run button.");
        BasicUtil.wait(5);
        mvpage.clickRunButton();
        BasicUtil.wait(8);
		if(driver.findElement(modelValProgressBar).isDisplayed())
		{
			mvpage.closeProgressBar();
			log("Run Validation Progress Bar is Closed Manually");
		}
		else
		{
			log("Run Validation Progress Bar is Auto Closed");
		}
        //Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr")), actualFilePath);
		Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr|.//table[contains(@class,'x132')]//tr")), actualFilePath);
        assertions = this.readFileAndAssert(expectedFilePath, actualFilePath);
        assertions.assertAll();
        //wait.until(ExpectedConditions.visibilityOfElementLocated(this.modelValProgressBar));
    }

    @Test(priority = 8, enabled = true)
    public void getMVforDGRenameMember() throws IOException, Exception {
        SoftAssert assertions = new SoftAssert();
        String actualFilePath = "./target/DataGrants_Rename.txt";
        String expectedFilePath = "../testdata/files/DataGrants_Rename.txt";
        log("Opening tab for datagarnts.");
        mvpage.clickTab(mvpage.dgTab);
        log("Clicking on run button.");
        BasicUtil.wait(5);
        mvpage.clickRunButton();
        BasicUtil.wait(8);
		if(driver.findElement(modelValProgressBar).isDisplayed())
		{
			mvpage.closeProgressBar();
			log("Run Validation Progress Bar is Closed Manually");
		}
		else
		{
			log("Run Validation Progress Bar is Auto Closed");
		}
        //Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr")), actualFilePath);
		Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr|.//table[contains(@class,'x132')]//tr")), actualFilePath);
        assertions = this.readFileAndAssert(expectedFilePath, actualFilePath);
        assertions.assertAll();
        //wait.until(ExpectedConditions.visibilityOfElementLocated(this.modelValProgressBar));
    }

    @Test(priority = 6, enabled = true)
    public void getMVforQueriesRenameMember() throws IOException, Exception {
        SoftAssert assertions = new SoftAssert();
        String actualFilePath = "./target/Queries_Rename.txt";
        String expectedFilePath = "../testdata/files/Queries_Rename.txt";
        log("Opening tab for Queries.");
        mvpage.clickTab(mvpage.queriesTab);
        log("Clicking on run button.");
        BasicUtil.wait(5);
        mvpage.clickRunButton();
        BasicUtil.wait(8);
		if(driver.findElement(modelValProgressBar).isDisplayed())
		{
			mvpage.closeProgressBar();
			log("Run Validation Progress Bar is Closed Manually");
		}
		else
		{
			log("Run Validation Progress Bar is Auto Closed");
		}
        //Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr")), actualFilePath);
		Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr|.//table[contains(@class,'x132')]//tr")), actualFilePath);
        assertions = this.readFileAndAssert(expectedFilePath, actualFilePath);
        assertions.assertAll();
        //wait.until(ExpectedConditions.visibilityOfElementLocated(this.modelValProgressBar));
    }

    @Test(priority = 5, enabled = true)
    public void getMVforModelViewRenameMember() throws IOException, Exception {
        SoftAssert assertions = new SoftAssert();
        String actualFilePath = "./target/ModelViews_Rename.txt";
        String expectedFilePath = "../testdata/files/ModelViews_Rename.txt";
        log("Opening tab for Model Views.");
        mvpage.clickTab(mvpage.modelViewTabs);
        log("Clicking on run button.");
        BasicUtil.wait(5);
        mvpage.clickRunButton();
        BasicUtil.wait(8);
		if(driver.findElement(modelValProgressBar).isDisplayed())
		{
			mvpage.closeProgressBar();
			log("Run Validation Progress Bar is Closed Manually");
		}
		else
		{
			log("Run Validation Progress Bar is Auto Closed");
		}
        //Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr")), actualFilePath);
		Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr|.//table[contains(@class,'x132')]//tr")), actualFilePath);
        assertions = this.readFileAndAssert(expectedFilePath, actualFilePath);
        assertions.assertAll();
        //assertions.assertAll();
        //wait.until(ExpectedConditions.visibilityOfElementLocated(this.modelValProgressBar));
    }

    @Test(priority = 4, enabled = true)
    public void getMVforRulesRenameMember() throws IOException, Exception {
        SoftAssert assertions = new SoftAssert();
        String actualFilePath = "./target/Rules_Rename.txt";
        String expectedFilePath = "../testdata/files/Rules_Rename.txt";
        log("Opening tab for rules.");
        mvpage.clickTab(mvpage.rulesTabs);
        mvpage.getPovbar().changePOV("2016", "January", "Actual");
        log("Clicking on run button.");
        BasicUtil.wait(5);
        mvpage.selectDropDown(mvpage.xpathForRuleSetFilter, "All");
        BasicUtil.wait(1);
        mvpage.selectDropDown(mvpage.xpathForRuleFilter, "All Rules");
        BasicUtil.wait(2);
        mvpage.clickRunButton();
        BasicUtil.wait(8);
		if(driver.findElement(modelValProgressBar).isDisplayed())
		{
			mvpage.closeProgressBar();
			log("Run Validation Progress Bar is Closed Manually");
		}
		else
		{
			log("Run Validation Progress Bar is Auto Closed");
		}
        //Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr")), actualFilePath);
		Utilities.extractMessagesFromTable(driver.findElements(By.xpath(".//table[contains(@class,'x109')]//tr|.//table[contains(@class,'x132')]//tr")), actualFilePath);
        assertions = this.readFileAndAssert(expectedFilePath, actualFilePath);
        assertions.assertAll();
        //wait.until(ExpectedConditions.visibilityOfElementLocated(this.modelValProgressBar));
    }
    
    SoftAssert readFileAndAssert(String expectedFilePath, String actualFilePath) throws Exception {
        FileReader expected = new FileReader(new File(expectedFilePath));
        FileReader actual = new FileReader(new File(actualFilePath));
        List<String> expectedLines = IOUtils.readLines(expected);
        List<String> actualLines = IOUtils.readLines(actual);
        SoftAssert assertions = new SoftAssert();
        assertions.assertTrue(expectedLines.size() == actualLines.size(), "There is difference in message counts between exp and actual files.");
        String actualMessageJoined = String.join("\n", actualLines);
        for (String expectedLine : expectedLines) {
            assertions.assertTrue(actualMessageJoined.contains(expectedLine), "Expected message Not Found ->" + expectedLine + ".\nActual message :\n" + actualMessageJoined);
        }
        return assertions;
    }

}
