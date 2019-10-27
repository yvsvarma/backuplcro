package tests;

import com.aventstack.extentreports.Status;
import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import poms.FuseNavigatorPage;
import poms.JobLibPage;
import poms2.DataLoadPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;

/**
 * @author Mohnish Banchhor <mohnish.banchhor@oracle.com>
 * @version 1.0
 * @since 1.0
 */
public class DataLoadTestSuite extends BaseTestSuite {

    /**
     * Data Load POM
     */
    DataLoadPage dataLoadPage;
    /**
     * Classname for logging
     */
    //String className = this.getClass().getName();
    /**
     * Dialog of deploy cube confirmation.
     */
    By deployCubeConfirmationDlg = By.xpath(".//*[@id='d1::msgDlg::_cnt']//div[contains(.,'Deploy Cube job')]/../../td/div");

    @Test(priority = 1)
    public void loginTest() throws Exception {
        //FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
    }

    /*
 * Navigate to Manage Database page
     */
    @Test(priority = 2)
    public void testNavigateToDataLoad() {
        dataLoadPage = navPage.navigateToDataLoad();
        log("Opened the dataload page.");
    }

    /*
 * Verify Data Load - Upload Input data (Clear Database Before Load: OFF, "Add To Existing Values" Selected 
     */
    @Test(priority = 3)
    public void testDataLoad() throws Exception {
        testNavigateToDataLoad();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h2[text()='Essbase Load Options']")));
        By deployCubeConfirmationDlg = By.xpath(".//*[@id='d1::msgDlg::_cnt']//div[contains(.,'Data Load job')]/../../td/div");
        //FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
        dataLoadPage = navPage.navigateToDataLoad();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(.,'Essbase Load Options')]")));
        log("In data load page.");
        log("Clear Database option is checked in data load page.");
        dataLoadPage.clickClearDatabaseCheckbox();
        dataLoadPage.clickAddRow();
        log("adding a row.");
        dataLoadPage.clickSelectFile();
        log("Clicked on select file link.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//label[contains(.,'Client')]/../span")));
        dataLoadPage.clickClientRadioButton();
        log("Clicked on client radio button.");
        dataLoadPage.attachDataloadFile(new File("./data/input.txt"));
        log("Attached the file.");
        dataLoadPage.clickOKButton();
        log("Clicked on OK button.");
        BasicUtil.waitADF(driver, large_timeout);
        dataLoadPage.clickLoadButton();
        log("Clicked on Load data button.");
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(deployCubeConfirmationDlg)));
        AssertJUnit.assertTrue(driver.findElement(deployCubeConfirmationDlg).getText().contains("submitted"));
        driver.findElement(By.xpath(".//button[@id='d1::msgDlg::cancel']")).click();
        log("Load data job has been submitted.");
        JobLibPage joblibPage = navPage.navigateToJobLibrary();
        //ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
        joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Data Load", BaseTestSuite.large_timeout * 20);
    }

    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            dataLoadPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToDataLoad();
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
                    log(" Navigating to DataLaodPage");
                    dataLoadPage = navPage.navigateToDataLoad();
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
