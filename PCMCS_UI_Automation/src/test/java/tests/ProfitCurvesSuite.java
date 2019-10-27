package tests;

import com.aventstack.extentreports.Status;
import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import poms.FuseNavigatorPage;
import poms.IntelligencePage;
import poms.ProfitCurvesPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;

/**
 * @author Mohnish Banchhor <mohnish.banchhor@oracle.com>
 * @version 1.0
 * @since 1.0
 */
public class ProfitCurvesSuite extends BaseTestSuite {

    /**
     * Profit Curve Page Object Model
     */
    private ProfitCurvesPage PCPage;
    /**
     * existing profit curve name holder
     */
    private String profitName = "All Customers";
    /**
     * classname for logging
     */
    private String className = this.getClass().getName();

    /**
     * Simulates login from a user
     *
     *
     */
    @Test(priority = 1)
    public void loginTest() throws Exception {
        //FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
    }

    /**
     * Navigate to the profit curve page
     *
     *
     */
    @Test(priority = 3, enabled = true)
    public void navigateToProfitCurvePage() throws Exception {
        //FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
        log(" Navigating to Intelligence.");
        IntelligencePage intelligencePage = navPage.navigateToIntelligence();
        log("Navigating to Profit Curve page.");
        PCPage = intelligencePage.clickProfitCurvesSubPageLink();
        log("Listing all PC");
        ArrayList<String> profitCurvesList = PCPage.getAllprofits();
        log("The number of Profit Curves visible : " + profitCurvesList.size());
        for (String eachProfitCurve : profitCurvesList) {
            log("Profit curve : " + eachProfitCurve);
        }
    }

    /**
     * Create new profit curve
     *
     *
     */
    @Test(priority = 6, enabled = true)
    public void testCreateNewProfitCurve() {
        String newProfitCurveName = "New Profit Curve";
        log("waiting for  Profit Curve Create button.");
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(PCPage.xpathForProfitRow)));
        log("Clicking on Profit Curve Create button.");
        PCPage.clickOnCreateButton();
        log("Putting values into the create Profit Curvepage.");
        PCPage.createPCSubPage("Test AV 2", newProfitCurveName, "Descrption", "Income From Operations", "IFO", "");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//h1[contains(.,'Create Profit')]")));
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        PCPage.clickRefreshButton();
        Assert.assertTrue(PCPage.doesProfitExists(newProfitCurveName));
        log("Profit Curveis created successfully.");
    }

    /**
     * Edit an exiting profit curve.
     *
     *
     */
    @Test(priority = 7, enabled = true)
    public void testEditPC() {

        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        //PCPage.clickRefreshButton();
        log("Clicking on Profit Curve action button.");
        PCPage.clickOnActionButton(profitName);
        log("Clicking on Profit Curve edit button.");
        PCPage.clickOnEditMenuLink(profitName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h1[contains(.,'Edit')]")));
        log("editing Profit Curve.");
        PCPage.clickEnabledCheckBox();
        log("Changing xaxis label.");
        PCPage.populateXDimLabel("Xlabel");
        log("Changing new description.");
        PCPage.populateDescField("The new description.");
        log("Save and close.");
        PCPage.clickSaveAndClose();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//h1[contains(.,'Edit Profit')]")));
        log("editing Profit Curveis success.");
    }

    /**
     * Copy an exiting profit curve into a new profit curve.
     *
     *
     */
    @Test(priority = 4, enabled = true)
    public void testCopyPC() {
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        PCPage.clickRefreshButton();
        log("Clicking on Profit Curve Action button.");
        PCPage.clickOnActionButton(profitName);
        log("Clicking on Profit Curve copy menu item.");
        PCPage.clickOnCopyMenuLink(profitName);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[text()='Copy Profit Curve']")));
        log("Copying PC.");
        PCPage.typeNewCopyPCName("Copy1");
        log("Clicking OK button.");
        PCPage.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[text()='Copy Profit Curve']")));
        PCPage.clickRefreshButton();
        Assert.assertTrue(PCPage.doesProfitExists("Copy1"));
        log("Copying Profit Curveis success.");

    }

    /**
     * Diagnose an existing profit curve
     *
     *
     */
    @Test(priority = 5, enabled = true)
    public void testDiagnosePC() {
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        PCPage.clickRefreshButton();
        log("Clicking on Profit CurveAction button.");
        PCPage.clickOnActionButton(profitName);
        log("Clicking on Profit CurveDIag button.");
        PCPage.clickOnDiaMenuLink(profitName);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[contains(.,'Diagnose')]")));
        log("Clicking on Profit CurveDIag close button.");
        PCPage.clickCloseButton();
    }

    /**
     * Deletes an existing profit curve
     *
     *
     */
    @Test(priority = 100, enabled = true)
    public void testDeleteProfitCurve() {
        String deleteProfitName = "Profit Curve 2";
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Refreshing Profit Curvepage.");
        //PCPage.clickRefreshButton();
        log("Clicking on Profit Curve Delete button.");
        PCPage.clickOnDeleteProfitButton(deleteProfitName).clickYesButton();
        log("Deleting PC.");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//td/div[text()='Delete']")));
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        PCPage.clickRefreshButton();
        Assert.assertTrue(!PCPage.doesProfitExists(deleteProfitName));
        log("profit curve is deleted.");

    }

    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            PCPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToIntelligence().clickProfitCurvesSubPageLink();
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
                    log(" Navigating to DM.");
                    PCPage = navPage.navigateToIntelligence().clickProfitCurvesSubPageLink();
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
