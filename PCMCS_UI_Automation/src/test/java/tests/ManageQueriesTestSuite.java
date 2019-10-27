package tests;

import com.aventstack.extentreports.Status;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import poms2.ManageQueriesPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;
import utils.ClientSynchedWithServer;

public class ManageQueriesTestSuite extends BaseTestSuite {

    public static Logger logger = Logger.getLogger("TestSuiteE2E");
    private ManageQueriesPage MQPage;
    private String newQueryName = "Profitability - Test";
    private String queryName = "Profitability - Customer";
    private String copyQueryName = "Copy Query";
    String className = this.getClass().getName();

    @Test(priority = 1)
    public void loginTest() throws Exception {
        FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        BasicUtil.log(className, "Login successful.");
    }

    @Test(priority = 3, enabled = true)
    public void NavigatemanageQueriesScreen() throws Exception {
        //navPage = (FuseNavigatorPage)nextPage;
        MQPage = navPage.navigateToManageQueries();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h1[contains(text(),'Manage Queries')]")));

    }

    @Test(priority = 3, enabled = true)
    public void createQuery() throws InterruptedException {
        BasicUtil.log(className, "Clicking on Add Query button.");
        MQPage.clickAddButton();
        BasicUtil.log(className, "Adding details to query.");
        MQPage.populateAddQueryScreenPage1(ml_application, newQueryName);
        new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
        BasicUtil.log(className, "Clicking on Next button.");
        BasicUtil.waitADF(driver, large_timeout);
        MQPage.clickNextButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(.,'Program Context')]")));
        BasicUtil.log(className, "Clicking on Finish button.");
        BasicUtil.waitADF(driver, large_timeout);
        MQPage.clickFinishButton();
        BasicUtil.log(className, "waiting for new query wizard to be invisible.");
        //wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//td/div[contains(.,'New Query Wizard')]")));
        //MQPage.filterQueryByAppName(ml_application);
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td/span[contains(.,'"+newQueryName+"')]")));
    }

    @Test(priority = 100, enabled = true)
    public void deleteQuery() throws Exception {
        BasicUtil.log(className, "Filtering and clicking on query to select it.");
        MQPage.filterQueryByAppName(ml_application);
        MQPage.filterQueryByQueryName(queryName);
        MQPage.selectRow(ml_application, queryName);
        BasicUtil.log(className, "Clicking on Delete button.");
        MQPage.clickDeleteQueryButton();
        BasicUtil.log(className, "Clicking on yes button.");
        MQPage.clickYesButton();
        MQPage.filterQueryByQueryName(copyQueryName);
        BasicUtil.log(className, "Clicking on Delete button.");
        MQPage.selectRow(ml_application, copyQueryName);
        MQPage.clickDeleteQueryButton();
        BasicUtil.log(className, "Clicking on yes button.");
        MQPage.clickYesButton();

    }

    @Test(priority = 6, enabled = true)
    public void editQuery() throws Exception {
        BasicUtil.log(className, "Clicking on query to select it.");
        MQPage.filterQueryByAppName(ml_application);
        BasicUtil.log(className, "Selecting the query.");
        MQPage.selectRow(ml_application, queryName);
        //wait.until(ExpectedConditions.attributeContains(By.xpath(".//td[contains(.,'Query Name')]/../td[2]/input"), "value", queryName));
        BasicUtil.log(className, "Editing the query.");
        MQPage.changeDescription("Description is edited.");
        BasicUtil.log(className, "Saving the query.");
        MQPage.clickSaveButton();
    }

    @Test(priority = 5, enabled = true)
    public void copyQuery() throws Exception {
        BasicUtil.log(className, "Clicking on query to select it.");
        MQPage.filterQueryByAppName(ml_application);
        MQPage.selectRow(ml_application, queryName);
        BasicUtil.log(className, "Copying the query.");
        MQPage.clickCopyButton();
        BasicUtil.log(className, "Inputing the new name for the query.");
        //wait.until(ExpectedConditions.attributeContains(By.xpath(".//td[contains(.,'Query Name')]/../td[2]/input"), "value", queryName));
        MQPage.typeNameQueryCopy(copyQueryName);
        BasicUtil.log(className, "Clicking OK button.");
        MQPage.clickOKButton();
        //MQPage.clickYesButton();
    }

    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            MQPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToManageQueries();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h1[contains(text(),'Manage Queries')]")));
            BasicUtil.waitADF(driver, large_timeout);
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

                } catch (Exception e) {
                    driver.quit();
                    log("Failure in aftertest method. Abandoning the setup.");
                    org.testng.Assert.fail("Driver setup was disturbed by login/navigation failures.");
                    reporter.flush();
                    //throw e;
                }
                BasicUtil.wait(5);
                navPage = setupDriverAndLogin();
                wait = new WebDriverWait(driver, large_timeout);
                log("Login successful.");
                log(" Navigating to MQ.");
                MQPage = navPage.navigateToManageQueries();
                reporter.flush();
            }

            //
            //log("test have been executed.");
        }
    }
}
