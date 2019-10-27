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

import poms.FuseNavigatorPage;
import poms.JobLibPage;
import poms2.POVManagerPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;

public class POVManagerTestSuite extends BaseTestSuite {

    POVManagerPage povPage;
    String className = this.getClass().getName();

    @Test(priority = 1)
    public void loginTest() throws Exception {
        //FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
    }

    @Test(priority = 2, enabled = true)
    public void NavigatePOVScreen() throws Exception {
        //FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
        log(" Navigating to POV Manager.");
        povPage = navPage.navigateToPovManager();
        log("Listing all POVs");
        ArrayList<String> povList = povPage.getAllPOVs();
        log("The number of POVs visible : " + povList.size());
        for (String eachPOV : povList) {
            log("Points of View : " + eachPOV);
        }
        //AVPage = avPage;	 exist
        Assert.assertTrue(povPage.doesThisPOVExists("2014_January_Actual_Draft"), "under test Pov does not exists.");
        povPage.seletRow("2014_January_Actual_Draft");
    }

    @Test(priority = 100, enabled = true)
    public void deletePOV() throws Exception {
        String deletePOVString = "2015_February_Actual_Draft";
        log("Selecting the pov row.");
        povPage.seletRow(deletePOVString);
        BasicUtil.waitADF(driver, 100);
        log("Deleting the pov row.");
        povPage.clickDeletePOVButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(povPage.deletePOVConfirm));
        log("Deletion confirm dlg is displayed.Clicking yes.");
        driver.findElement(By.xpath(".//div[contains(@id,'confirmDelete')]//button[@id='f2:0:pc1:d6::yes']")).click();
        //BasicUtil.waitForGlassPaneToBeInvisible(driver, wait);
        //FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
        JobLibPage joblibPage = navPage.navigateToJobLibrary();
        //ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
        joblibPage.selectFirstJob(System.getProperty("ml_application"), "Delete POV");
        joblibPage.printJobProps();
        joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Delete POV", 200);
        navPage.navigateToPovManager();
        BasicUtil.waitADF(driver, 100);
        Assert.assertTrue(!povPage.doesThisPOVExists(deletePOVString), "The POV deletion failed.");
    }

    @Test(priority = 3, enabled = true)
    public void createPOV() {
        log("Creating a new pov row.");
        povPage.clickCreatePOVButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(povPage.createPOVConfirm));
        log("Create dlg is displayed.Seleting members.");
        povPage.selectYear("2015");
        povPage.selectPeriod("February");
        povPage.selectScenario("Actual");
        log("Clicking OK. It will create a new pov.");
        povPage.clickOKButton();
        BasicUtil.waitADF(driver, 100);
        povPage.clickRefreshButton();
        BasicUtil.waitADF(driver, 100);
        Assert.assertTrue(povPage.doesThisPOVExists("2015_February_Actual_Draft"), "The POV creation failed.");
    }

    @Test(priority = 4, enabled = true)
    public void changePOVStateToArchived() {
        String changePOVString = "2014_February_Actual_Draft";
        String changedPOVString = "2014_February_Actual_Archived";
        log("Selecting the pov row.");
        povPage.seletRow(changePOVString);
        BasicUtil.waitADF(driver, 100);
        log("Changeing the pov row.");
        povPage.clickChangePOVButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(povPage.changePOVConfirm));
        log("Change  dlg is displayed.Slecting state and clicking OK.");
        povPage.selectState("Archived");
        driver.findElement(By.xpath(".//div[contains(@id,'change')]//button[text()='OK']")).click();
        BasicUtil.waitADF(driver, 100);
        Assert.assertTrue(!povPage.doesThisPOVExists(changePOVString), "The POV Change failed.");
        Assert.assertTrue(povPage.doesThisPOVExists(changedPOVString), "The POV Change failed.");
    }

    @Test(priority = 5, enabled = true)
    public void changePOVStateToPublished() {
        String changePOVString = "2014_February_Actual_Archived";
        String changedPOVString = "2014_February_Actual_Published";
        log("Selecting the pov row.");
        povPage.seletRow(changePOVString);
        BasicUtil.waitADF(driver, 100);
        log("Changing the pov row.");
        povPage.clickChangePOVButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(povPage.changePOVConfirm));
        log("Change  dlg is displayed.Slecting state and clicking OK.");
        povPage.selectState("Published");
        driver.findElement(By.xpath(".//div[contains(@id,'change')]//button[text()='OK']")).click();
        BasicUtil.waitADF(driver, 100);
        Assert.assertTrue(!povPage.doesThisPOVExists(changePOVString), "The POV Change failed.");
        Assert.assertTrue(povPage.doesThisPOVExists(changedPOVString), "The POV Change failed.");
    }

    @Test(priority = 6, enabled = true)
    public void changePOVStateToDraft() {
        String changePOVString = "2014_February_Actual_Published";
        String changedPOVString = "2014_February_Actual_Draft";
        log("Selecting the pov row.");
        povPage.seletRow(changePOVString);
        BasicUtil.waitADF(driver, 100);
        log("Changeing the pov row.");
        povPage.clickChangePOVButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(povPage.changePOVConfirm));
        log("Change  dlg is displayed.Slecting state and clicking OK.");
        povPage.selectState("Draft");
        driver.findElement(By.xpath(".//div[contains(@id,'change')]//button[text()='OK']")).click();
        BasicUtil.waitADF(driver, 100);
        Assert.assertTrue(!povPage.doesThisPOVExists(changePOVString), "The POV Change failed.");
        Assert.assertTrue(povPage.doesThisPOVExists(changedPOVString), "The POV Change failed.");
    }

    @Test(priority = 7, enabled = true)
    public void clearPOV() throws Exception {
        String POVString = "2010_March_Budget_Draft";
        log("Selecting the pov row.");
        povPage.seletRow(POVString);
        BasicUtil.waitADF(driver, 100);
        log("Deleting the pov row.");
        povPage.clickClearPOVButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(povPage.clearPOVConfirm));
        log("Clear POV dlg is displayed.Selecting options and clicking OK.");
        povPage.clickAdjustmentDataCheckBox();
        povPage.clickAllocatedDataCheckBox();
        povPage.clickInputDataCheckBox();
        povPage.clickManageRuleCheckBox();
        driver.findElement(By.xpath(".//span[text()='OK']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(.,'Clear POV job has been submitted.')]")));
        driver.findElement(By.xpath(".//button[contains(@id,'Error') and text()='OK']")).click();
        BasicUtil.waitForGlassPaneToBeInvisible(driver, wait);
//        FuseNavigatorPage navPage = (FuseNavigatorPage) nextPage;
        JobLibPage joblibPage = navPage.navigateToJobLibrary();
        //ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
        joblibPage.selectFirstJob(System.getProperty("ml_application"), "Clear POV");
        joblibPage.printJobProps();
        joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Clear POV", 200);
        navPage.navigateToPovManager();
        //BasicUtil.waitADF(driver,100);	
    }

    @Test(priority = 8, enabled = true)
    public void copyPOV() throws Exception {
        String POVString = "2014_January_Actual_Draft";
        log("Selecting the pov row.");
        povPage.seletRow(POVString);
        BasicUtil.waitADF(driver, 100);
        log("Copying the pov row.");
        povPage.clickCopyPOVButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(povPage.copyPOVConfirm));
        log("Copy POV dlg is displayed.Selecting options and clicking OK.");
        povPage.selectYear("2015");
        povPage.selectPeriod("December");
        povPage.selectScenario("Budget");
        //.//div[contains(@id,'copy')]//label[text()='Manage Rules']/..//input
        //povPage.clickAdjustmentDataCheckBox();
        //povPage.clickAllocatedDataCheckBox();
        povPage.clickInputDataCheckBox();
        povPage.clickManageRuleCheckBox();
        driver.findElement(By.xpath(".//span[text()='OK']")).click();
        //	Destination Points of View does not exist. Do you want to create one
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(.,'Confirm Point of View Group Create')]")));
        povPage.clickYesButton();
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(.,'Copy POV job has been submitted.')]")));
        driver.findElement(By.xpath(".//button[contains(@id,'Error') and text()='OK']")).click();
        BasicUtil.waitForGlassPaneToBeInvisible(driver, wait);
        //FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
        JobLibPage joblibPage = navPage.navigateToJobLibrary();
        //ConsoleJobLibPage consoleJobLibPage = consolePage.clickJobLibSubPageLink();
        joblibPage.selectFirstJob(System.getProperty("ml_application"), "Copy POV");
        joblibPage.printJobProps();
        joblibPage.waitForjobToFinish(System.getProperty("ml_application"), "Copy POV", 200);
        navPage.navigateToPovManager();
        //BasicUtil.waitADF(driver,100);	
    }

    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            povPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToPovManager();
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
                    povPage = navPage.navigateToPovManager();
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
