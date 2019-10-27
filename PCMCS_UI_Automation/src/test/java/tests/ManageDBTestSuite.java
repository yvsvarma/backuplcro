package tests;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import poms.IntelligencePage;
import poms.JobLibPage;
import poms2.ManageDBPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;

/**
 * @author Mohnish Banchhor <mohnish.banchhor@oracle.com>
 * @version 1.0
 * @since 1.0
 */
public class ManageDBTestSuite extends BaseTestSuite {

    /**
     * Manage DB POM
     */
    ManageDBPage manageDBPage;
    /**
     * Classname for logging
     */
    String className = this.getClass().getName();
    /**
     * Dialog of deploy cube confirmation.
     */
    By deployCubeConfirmationDlg = By.xpath(".//*[@id='d1::msgDlg::_cnt']//div[contains(.,'Deploy Cube job')]/../../td/div");

    @Test(priority = 1)
    public void loginTest() throws Exception {
        FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
    }

    /*
* Navigate to Manage Database page
     */
    @Test(priority = 2)
    public void testNavigateToManageDB() {
        manageDBPage = navPage.navigateToManageDB();
        log("Opened the database page.");
    }

    /*
* Veify deploying cube - Create/Replace, verify job status
     */
    @Test(priority = 3)
    public void testReplaceDB() throws Exception {
        testNavigateToManageDB();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h2[text()='Essbase Information']")));
        manageDBPage.clickOnCreateNewDBRadio();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[text()='The existing database and all its data will be deleted.']")));
        manageDBPage.clickOK();
        manageDBPage.clickDeployNowButton();
        log("Clicked on Deploy now OK button.");
        manageDBPage.clickDeployDlgOKButton();
        log("Clicked on Deploy now button.");
        log("Waiting for confirmation dialog");
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(deployCubeConfirmationDlg)));
        AssertJUnit.assertTrue(driver.findElement(deployCubeConfirmationDlg).getText().contains("submitted"));
        driver.findElement(By.xpath(".//button[@id='d1::msgDlg::cancel']")).click();
        log("Deploy cube job is submitted successfully.");
        BasicUtil.waitADF(driver, large_timeout);
        JobLibPage joblibPage = navPage.navigateToJobLibrary();
//ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
        BasicUtil.waitADF(driver, large_timeout);
        joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Deploy Cube", BaseTestSuite.large_timeout * 20);
    }

    /*
* Veify deploying cube - Update Database, Preserve Data ON.  Check whether data is retained
*
     */
    @Test(priority = 4)
    public void testUpdateDBPreseveData() throws Exception {
        testNavigateToManageDB();
//By deployCubeConfirmationDlg = By.xpath(".//*[@id='d1::msgDlg::_cnt']//div[contains(.,'Deploy Cube job')]/../../td/div"
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h2[text()='Essbase Information']")));
        manageDBPage.clickOnUpdateDBRadio(true);
        manageDBPage.clickDeployNowButton();
        log("Clicked on Deploy now OK button.");
        manageDBPage.clickDeployDlgOKButton();
        log("Clicked on Deploy now button.");
        log("Waiting for confirmation dialog");
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(deployCubeConfirmationDlg)));
        Assert.assertTrue(driver.findElement(deployCubeConfirmationDlg).getText().contains("submitted"));
        driver.findElement(By.xpath(".//button[@id='d1::msgDlg::cancel']")).click();
        log("Deploy cube job is submitted successfully.");
        JobLibPage joblibPage = navPage.navigateToJobLibrary();
//ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
        joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Deploy Cube", BaseTestSuite.large_timeout * 20);
    }

    /**
     * Verify deploying cube - Update Database, Preserve Data OFF.
     *
     * @throws Exception
     */
    @Test(priority = 4)
    public void testUpdateDBUnpreserveData() throws Exception {
        testNavigateToManageDB();
//By deployCubeConfirmationDlg = By.xpath(".//*[@id='d1::msgDlg::_cnt']//div[contains(.,'Deploy Cube job')]/../../td/div");
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h2[text()='Essbase Information']")));
        manageDBPage.clickOnUpdateDBRadio(false);
        manageDBPage.clickDeployNowButton();
        log("Clicked on Deploy now OK button.");
        manageDBPage.clickDeployDlgOKButton();
        log("Clicked on Deploy now button.");
        log("Waiting for confirmation dialog");
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(deployCubeConfirmationDlg)));
        Assert.assertTrue(driver.findElement(deployCubeConfirmationDlg).getText().contains("submitted"));
        driver.findElement(By.xpath(".//button[@id='d1::msgDlg::cancel']")).click();
        log("Deploy cube job is submitted successfully.");
        JobLibPage joblibPage = navPage.navigateToJobLibrary();
//ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
        joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Deploy Cube", BaseTestSuite.large_timeout * 20);
    }

    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            manageDBPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToManageDB();
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
                    manageDBPage = navPage.clickHomeButton().navigateToManageDB();

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
