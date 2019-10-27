package tests;

import com.aventstack.extentreports.Status;
import java.util.logging.Logger;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import poms.FuseNavigatorPage;
import poms.IntelligencePage;
import poms.ScatterGraphPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;

public class ScatterGraphTestSuite extends BaseTestSuite {

    public static Logger logger = Logger.getLogger("Scatter");
    ScatterGraphPage SGPage;
    String scatterName = "Scatter Test 1";
    String className = this.getClass().getName();

    @Test(priority = 1)
    public void loginTest() throws Exception {
        //FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
    }

    @Test(priority = 3, enabled = true)
    public void NavigateScatterScreen() throws Exception {
        //FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
        log(" Navigating to Intelligence.");
        IntelligencePage intelligencePage = navPage.navigateToIntelligence();
        log("Navigating to SA.");
        SGPage = intelligencePage.clickScatterSubPageLink();

    }

    @Test(priority = 100, enabled = true)
    public void deleteSG() {
        String scatterName = "Scatter Test 1";
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Clicking on SG Delete button.");
        BasicUtil.wait(5);
        log("Clicking on SG Delete Yes.");
        SGPage.clickOnDeleteScatterButton(scatterName).clickYesButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//td/div[text()='Delete']")));
        SGPage.clickRefreshButton();
        Assert.assertTrue(!SGPage.doesScatterExists(scatterName));
        log("scatter is deleted.");

    }

    @Test(priority = 6, enabled = true)
    public void createNewSG() {
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Clicking on SG create button.");
        SGPage.clickOnCreateButton();
        log("Populating create SG form.");
        SGPage.createSGSubPage("Test AV 2", "Test SG 1", "Descr", "Income From Operations", "IFO", "Gross Profit", "");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//h1[contains(.,'Create Scatter')]")));
        SGPage.clickRefreshButton();
        Assert.assertTrue("Scatter of name Test SG 1 does not exist. creation of new scatter failed.", SGPage.doesScatterExists("Test SG 1"));
        log("Created SG.");

    }

    @Test(priority = 5, enabled = false)
    public void editSG() {

        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        SGPage.clickRefreshButton();

        log("Clicking on SG edit button.");
        SGPage.clickOnEditMenuLink("Scatter Test 1");
        log("editing SG.");
        SGPage.clickEnabledCheckBox();
        SGPage.populateXDimLabel("Xlabel");
        SGPage.populateDescField("The new description.");
        log("Clicking on SG Save and close button.");
        SGPage.clickSaveAndClose();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//h1[contains(.,'Edit Scatter')]")));
        SGPage.clickRefreshButton();
        //Assert.assertTrue("Scatter of name Test SG 1 does not exist. creation of new scatter failed.",SGPage.doesScatterExists("Test SG 1"));
        log("Edited SG.");
    }

    @Test(priority = 4, enabled = true)
    public void copySG() {
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }

        log("Clicking on SG copy menu item.");
        SGPage.clickOnCopyMenuLink(scatterName);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[text()='Copy Scatter Analysis']")));
        log("Type Name for new SG..");
        SGPage.typeNewCopyAVName("Copy1");
        log("Click OK.");
        SGPage.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[text()='Copy Scatter Analysis']")));
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Clicking on refresh button");
        SGPage.clickRefreshButton();
        Assert.assertTrue("Scatter of name Copy1 does not exist. creation of new scatter failed.", SGPage.doesScatterExists("Copy1"));
        log("Copied SG successfully.");

    }

    @Test(priority = 5, enabled = true)
    public void diagnoseSG() {
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        SGPage.clickRefreshButton();

        log("Clicking on SG DIag button.");
        SGPage.clickOnDiaMenuLink(scatterName);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Diagnose Scatter Analysis']")));
        log("Clicking on SG Diagnose dialog Close button.");
        SGPage.clickCloseButton();
        log("DIagonsis SG is succesful.");
    }

    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            SGPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToIntelligence().clickScatterSubPageLink();
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
                    log(" Navigating to Scatter Page.");
                    SGPage = navPage.navigateToIntelligence().clickScatterSubPageLink();
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
