package tests;

import com.aventstack.extentreports.Status;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import poms.FRPage;
import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import poms.IntelligencePage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;

public class FRTestSuite extends BaseTestSuite {

    //public static Logger logger = Logger.getLogger("FR");
    FRPage frPage;
    String runFrName = "01 Six Months Income Statement";
    String frName = "0000FR0";
    //String className = this.getClass().getName();

    @Test(priority = 1)
    public void loginTest() throws Exception {
        FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
    }

    @Test(priority = 3, enabled = true)
    public void navigateToFRScreen() throws Exception {
        log(" Navigating to reports.");
        frPage = navPage.navigateToReports();

    }

    @Test(priority = 4, enabled = true)
    public void createFR() {
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Clicking on create button.");
        frPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(frPage.createFRPageHeader));
        frName = frName + (int) Math.floor(Math.random() * 101);
        frPage.inputFRName(frName);
        log("Selecting the Query");
        frPage.clickSelectQueryLink();
        wait.until(ExpectedConditions.visibilityOfElementLocated(frPage.selectQueryWizardHeader));
        //frPage.filterQueryInput("Profitability - Product");
        //BasicUtil.highlightElement(driver, driver.find);
        frPage.selectQuery("Cost Analysis - Activity Contribution to Product");
        frPage.clickSelectQueryOKButton();
        log("Selected the Query");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(frPage.selectQueryWizardHeader));
        frPage.clickCreateFROKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(frPage.createFRPageHeader));
        log("Creation of the FR is successful.");
    }

    @Test(priority = 5, enabled = true)
    public void runExistingFR() {
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//img[contains(@alt,'Create')]")));
        log("Selecting FR.");
        frPage.selectFRByName(frName);
        log("Clicking run button FR.");
        log("Running HTML reports.");
        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[boolean(@_afrrk)]/td[1]/div[contains(.,'"+runFrName+"')]")));
        BasicUtil.waitADF(driver, large_timeout);
        String handleMain = driver.getWindowHandle();
        frPage.clickRunHTML(frName);
        BasicUtil.wait(5);

        // Switch to new window opened
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
            driver.findElement(By.xpath("//td[contains(text(),'BUS1100')]"));
        }

// Perform the actions on new window
// Close the new window, if that window no more required
        driver.close();

// Switch back to original browser (first window)
        driver.switchTo().window(handleMain);
    }

    @Test(priority = 5, enabled = true)
    public void runNewFR() {
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Selecting FR.");
        //frPage.selectFRByName(frName);
        log("Clicking run button FR.");
        log("Running HTML reports.");
        String handleMain = driver.getWindowHandle();
        frPage.clickRunHTML(frName);
        BasicUtil.wait(5);

        // Switch to new window opened
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }

// Perform the actions on new window
// Close the new window, if that window no more required
        driver.close();

// Switch back to original browser (first window)
        driver.switchTo().window(handleMain);
    }

    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            frPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToReports();
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
                    frPage = navPage.navigateToReports();
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
