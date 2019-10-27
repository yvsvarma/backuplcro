package tests;

import com.aventstack.extentreports.Status;
import java.util.logging.Logger;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import poms.FuseNavigatorPage;
import poms2.ManageModelViewsPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;

public class ModelViewTestSuite extends BaseTestSuite {
	public static Logger logger = Logger.getLogger("TestSuiteE2E");
	private ManageModelViewsPage MVPage;
	private String viewName = "Balancing - 1 Operating Expenses";
	private String copyViewName= "Copy View";
	String className = this.getClass().getName();

@Test(priority=1)
public void loginTest() throws Exception{
	//FuseLoginPage loginPage = new FuseLoginPage(driver);
	navPage = loginPage.doLogin(ml_application);
	Reporter.log("Login successful.");
}

@Test(priority=3,enabled=true)
public void NavigateToMVScreen() throws Exception{
	//FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
	log("Navigating to the Model Views.");
	MVPage = navPage.navigateToModelViews();
	//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h1[contains(text(),'Manage Queries')]")));
	
	
}
@Test(priority=4,enabled=true)
public void addMV() throws Exception{
	log("Clciking on new view button.");
	MVPage.clickNewModelViewButton();
	log("Putting new name.");
	MVPage.inputNewViewName("View 1");
	log("Selecting members.");
	MVPage.addMember("CostCenters:AllCostCenters:CC8000",false);
	log("Saving the view.");
	MVPage.saveView();
}
@Test(priority=5,enabled=true)
public void editMV() throws Exception{
	log("Filtering the view.");
	MVPage.filterByViewName(viewName);
	log("Selecting the view.");
	MVPage.selectRow(viewName);
	log("Change the description.");
	MVPage.changeDescription("this is the decription.");
	log("Save view.");
	MVPage.saveView();

}
@Test(priority=6,enabled=true)
public void copyMV() throws Exception{
	log("Filtering the view.");
	MVPage.filterByViewName(viewName);
	log("Selecting the view.");
	MVPage.selectRow(viewName);
	log("Copying the view.");
	MVPage.clickCopyButton();
	log("typing the new name for copy view.");
	MVPage.typeNameViewCopy(copyViewName);
	log("Clicking OK.");
	MVPage.clickOKButton();
	

}
@Test(priority=100,enabled=true)
public void deleteMV() throws Exception{
	log("Filtering the view.");
	MVPage.filterByViewName(viewName);
	log("Selecting the view.");
	MVPage.selectRow(viewName);
	log("Deleting the view.");
	MVPage.clickDeleteViewButton();
	log("Clicking OK.");
	MVPage.clickYesButton();

}
    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            MVPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToModelViews();
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
                    log(" Navigating to MV.");
                    MVPage = navPage.navigateToModelViews();
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

}
