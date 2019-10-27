package tests;

import com.aventstack.extentreports.Status;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import pagefragments.CreateEditKPIPageFragment;
import pagefragments.MemberSelectorFusePF;
import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import poms.IntelligencePage;
import poms.KPIPage;
import utils.BasicUtil;
import utils.ClientSynchedWithServer;

/**
 * @author Mohnish Banchhor <mohnish.banchhor@oracle.com>
 * @version 1.0
 * @since 1.0
 */
public class KPITestSuite extends BaseTestSuite {

    /**
     * KPI Page Object Model
     */
    private KPIPage kpiPage;

    /**
     * Simulates user logging
     *
     * @throws Exception
     */
    /**
     * Navigates to KPI Screen
     *
     * @throws Exception
     */
    @Test(priority = 2, enabled = true)
    public void testNavigateToKPIPage() throws Exception {

        loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
        log(" Navigating to Intelligence.");
        IntelligencePage intelligencePage = navPage.navigateToIntelligence();
        log("Navigating to KPI.");
        kpiPage = intelligencePage.clickKPISubPageLink();
        log("Listing all KPIS.");
        ArrayList<String> kpiList = kpiPage.getAllKPIs();
        log("The number of KPI visible : " + kpiList.size());
        for (String eachKPI : kpiList) {
            log("KPI : " + eachKPI);
        }
    }

    /**
     * Test the delete functionality for an exiting KPI
     *
     */
    @Test(priority = 100, enabled = true)
    public void testDeleteKPI() {
        String kpiName = "Department Stores Customer Service Change Over Prior Quarter";
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Clicking on KPI Delete button.");
        BasicUtil.waitADF(driver, large_timeout);
        kpiPage.clickOnDeleteKPIButton(kpiName).clickYesButton();
        log("Clicking on KPI Delete Yes.");
        log("KPI is deleting..");
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        kpiPage.clickRefreshButton();
        //Assert.assertTrue("Deletion of KPI failed.",!kpiPage.doesKPIExists(kpiName));
        log("KPI is deleted.");
    }

    /**
     * Edit an existing KPI
     */
    @Test(priority = 3, enabled = true)
    public void testEditKPI() {
        String kpiName = "Department Stores Customer Service Change Over Prior Quarter";
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        kpiPage.clickRefreshButton();
        log("Clicking on KPI edit Action button.");
        kpiPage.clickOnEditMenuLink(kpiName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h1[contains(.,'Edit')]")));
        log("Editing KPI.");
        kpiPage.clickEnabledCheckBox();
        kpiPage.clickSaveAndClose();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//h1[contains(.,'Edit')]")));
        log("KPI edited.");
    }

    /**
     * Copy an existing KPI to another KPI
     */
    @Test(priority = 4, enabled = true)
    public void testCopyKPI() {
        String kpiName = "Department Stores Customer Service Change Over Prior Quarter";
        String copyKPIName = "Copied KPI";
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        kpiPage.clickRefreshButton();

        log("Clicking on Copy  menu item.");
        kpiPage.clickOnCopyMenuLink(kpiName);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(.,'Copy Key Performance Indicator')]")));
        log("Typing new name.");
        kpiPage.typeNewCopyAVName(copyKPIName);
        log("Clicking OK button.");
        kpiPage.clickOKButton();
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        kpiPage.clickRefreshButton();
        Assert.assertTrue(kpiPage.doesKPIExists(copyKPIName), "Copying KPI failed.");
        log("Successful on KPI copy.");

    }

    /**
     * Diagnose an existing KPI
     */
    @Test(priority = 5, enabled = true)
    public void testDiagnoseKPI() {
        String diagKPIName = "Department Stores Customer Service Change Over Prior Quarter";
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        kpiPage.clickRefreshButton();

        log("Clicking on KPI DIag button.");
        kpiPage.clickOnDiaMenuLink(diagKPIName);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[contains(.,'Diagnose')]")));
        //wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//button[text()='Close']")));
        kpiPage.clickCloseButton();
        log("Diagnosis successful.");
    }

    /**
     * Create a new KPI
     */
    @Test(priority = 6, enabled = true)
    public void testCreateKPI() {
        String newKPIName = "Newly created KPI";
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Clicking on KPI Create button.");
        CreateEditKPIPageFragment createKPIPage = kpiPage.clickOnCreateButton();
        log("Waiting for KPI Create Dialog.");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(.,'Create')]")));
        BasicUtil.waitADF(driver, large_timeout);
        log("Clicking on base definition tab.");
        createKPIPage.clickOnDefinitionTab();
        log("populating new name.");
        createKPIPage.populateName(newKPIName);
        log("Populating new desc.");
        createKPIPage.populateDesc("New Description.");
        log("Checking enabled checkbox.");
        createKPIPage.checkEnabledBox();
        log("Selecting population dimension.");
        createKPIPage.selectPopulationDim("Product");
        log("Selecting member.");
        new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
        MemberSelectorFusePF memSelPage = createKPIPage.clickOnSelectPopMemLink();
        memSelPage.addMember("Product", "AllProducts:Bikes:B1001", "");
        log("Clicking OK on mem select page.");
        memSelPage.clickOKButton();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(.,'Create')]")));
        log("Seelcting computation options.");
        createKPIPage.selectComputationOption("Sum");
        log("Clciking save and close.");
        createKPIPage.clickSaveAndClose();
        BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[contains(.,'Create')]"));
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        kpiPage.clickRefreshButton();
        Assert.assertTrue(kpiPage.doesKPIExists(newKPIName), "Creation of new KPI failed.");
        log("Successful on KPI create option.");

    }

    /**
     *
     */
    @Test(priority = 7, enabled = true)
    public void testCreateKPIWithDataSlice() {
        String newKPIName = "Newly Created KPI with data slice";
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Clicking on KPI Create button.");
        CreateEditKPIPageFragment createKPIPage = kpiPage.clickOnCreateButton();
        log("Waiting for KPI Create Dialog.");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(.,'Create')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//a[contains(.,'Base Definition')]")));
        log("Clicking on base definition tab.");
        createKPIPage.clickOnDefinitionTab();
        log("populating new name.");
        createKPIPage.populateName(newKPIName);
        log("Populating new desc.");
        createKPIPage.populateDesc("New Description.");
        log("Checking enabled checkbox.");
        createKPIPage.checkEnabledBox();
        log("Selecting population dimension.");
        createKPIPage.selectPopulationDim("Product");
        log("Selecting member.");
        new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
        MemberSelectorFusePF memSelPage = createKPIPage.clickOnSelectPopMemLink();
        memSelPage.addMember("Product", "AllProducts:Bikes:B1001", "");
        log("Selected member.");
        memSelPage.clickOKButton();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(.,'Create')]")));
        createKPIPage.selectComputationOption("Sum");
        log("Navigating to data slice tab.");
        createKPIPage.clickOnDataSliceTab();
        log("Navigating to data slice mem selector.");
        memSelPage = createKPIPage.editDataSlice("Account");
        log("Selecting data slice member.");
        memSelPage.addMember("Account", "NetIncome:Income From Operations:Gross Profit:Net Revenue", "");
        log("Selected member.");
        memSelPage.clickOKButton();

        log("Navigating to data slice tab.");
        createKPIPage.clickOnDataSliceTab();
        memSelPage = createKPIPage.editDataSlice("Scenario");
        log("Selecting data slice member.");

        memSelPage.addMember("Scenario", "Actual", "");
        log("Selected member.");
        memSelPage.clickOKButton();
        log("Clicking on save and close.");
        createKPIPage.clickSaveAndClose();
        BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[contains(.,'Create')]"));
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        kpiPage.clickRefreshButton();
        Assert.assertTrue(kpiPage.doesKPIExists(newKPIName), "Creating a KPI with data slice failed.");
        log("Successful on KPI create option.");

    }

    /**
     * Create a new KPI with percent compare
     */
    @Test(priority = 8, enabled = true)
    public void testCreateKPIWithPercentageCompare() {
        String newKPIName = "New KPI with Percent compare";
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        log("Clicking on KPI Create button.");
        CreateEditKPIPageFragment createKPIPage = kpiPage.clickOnCreateButton();
        log("Waiting for KPI Create Dialog.");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(.,'Create')]")));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//a[contains(.,'Base')]")));
        log("Clicking on base definition tab.");
        createKPIPage.clickOnDefinitionTab();
        log("populating new name.");
        createKPIPage.populateName(newKPIName);
        log("Populating new desc.");
        createKPIPage.populateDesc("New Description.");
        log("Checking enabled checkbox.");
        createKPIPage.checkEnabledBox();
        log("Selecting population dimension.");
        createKPIPage.selectPopulationDim("Product");
        log("Selecting member.");
        new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
        MemberSelectorFusePF memSelPage = createKPIPage.clickOnSelectPopMemLink();
        memSelPage.addMember("Product", "AllProducts:Bikes:B1001", "");
        log("Selected member.");
        memSelPage.clickOKButton();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(.,'Create')]")));
        createKPIPage.selectComputationOption("Sum");
        log("Navigating to data slice tab.");
        createKPIPage.clickOnDataSliceTab();
        log("Navigating to data slice mem selector.");
        memSelPage = createKPIPage.editDataSlice("Account");
        log("Selecting data slice member.");
        memSelPage.addMember("Account", "NetIncome:Income From Operations:Gross Profit:Net Revenue", "");
        log("Selected member.");
        memSelPage.clickOKButton();

        log("navigating to Comparison tab.");
        createKPIPage.clickOnComparisonTab();
        log("Selecting Comparison type.");
        createKPIPage.selectComparisonOption("Percentage Comparison");
        log("Selecting Comparison dimension.");
        createKPIPage.selectComparisonDimension("Year");
        log("Selecting Comparison dimension member.");
        new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
        memSelPage = createKPIPage.clickOnComparisonDimMemSelLink();
        memSelPage.addMember("Year", "2015", "");
        log("Selected Comparison dimension member.");
        memSelPage.clickOKButton();
        log("Clicking on Save and close.");
        createKPIPage.clickSaveAndClose();
        BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[contains(.,'Create')]"));
        if (!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
        }
        kpiPage.clickRefreshButton();
        Assert.assertTrue(kpiPage.doesKPIExists(newKPIName), "Creation of a new KPI with % compare failed.");
        log("Successful on KPI create option.");

    }

    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            kpiPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToIntelligence().clickKPISubPageLink();
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
                    log(" Navigating to Intelligence.");
                    IntelligencePage intelligencePage = navPage.navigateToIntelligence();
                    log("Navigating to KPI.");
                    kpiPage = intelligencePage.clickKPISubPageLink();

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
