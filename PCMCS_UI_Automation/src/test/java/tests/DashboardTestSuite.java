package tests;

import com.aventstack.extentreports.Status;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import pagefragments.CreateEditDBPageFragment;
import poms.DashboardPage;
import poms.FuseNavigatorPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;

/**
 * @author Mohnish Banchhor <mohnish.banchhor@oracle.com>
 * @version 1.0
 * @since 1.0
 */
public class DashboardTestSuite extends BaseTestSuite {

    /**
     * Dashboard Page Object Model
     */
    private DashboardPage DBPage;

    /**
     * Classname for reporter logging.
     */
    //private String className = this.getClass().getName();

    /**
     * Test method, which simulates the user logging by enter
     * domain/username/password.
     *
     */
    @Test(priority = 1)
    public void testLogin() throws Exception {
        //FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
    }

    /**
     * Verify all errors those can occur during blank entry of line chart.
     * Errors : Please select an Analysis View. Please select a Member
     *
     */
    @Test(priority = 4, enabled = true)
    public void verifyErrorsFromBlankLineChart() throws Exception {
        BasicUtil.waitADF(driver, large_timeout);
        log("Clicking on create button.");
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        log("Waiting for create dashboard dialog.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        log("Populating chart name.");
        createDBPF.populateName("DashBoard Chart");
        log("Populating chart description.");
        createDBPF.populateDesc("DashBoard Description");
        log("Checking the enabled checkbox.");
        createDBPF.checkEnabledBox();
        log("Drag and drop line chart type.");
        createDBPF.dragAndDropVisualization("Line", 1);
        BasicUtil.waitADF(driver, large_timeout);
        log("Clicking on save and close.");
        createDBPF.clickSaveAndClose();
        log("Waiting for error popup.");
        BasicUtil.waitADF(driver, large_timeout);
        String error = createDBPF.getErrorsFromPopup();
        log("Printing errors: " + error);
        Assert.assertTrue(error.contains("Please select an Analysis View"));
        Assert.assertTrue(error.contains("Please select a Member"));
        log("Clicking OK button to close the error popup.");
        createDBPF.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Errors']")));
        log("Clicking close button to close the create dashboard dialog.");
        createDBPF.clickCancelButton();
    }

    /**
     * Verify all errors those can occur during blank entry of pie chart. Errors
     * : Please select an Analysis View. Errors : Please select a Member
     *
     */
    @Test(priority = 5, enabled = true)
    public void verifyErrorsFromBlankPieChart() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        log("Clicking on create button.");
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        log("Waiting for create dashboard dialog.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        log("Populating chart name.");
        createDBPF.populateName("DashBoard Chart");
        log("Populating chart description.");
        createDBPF.populateDesc("DashBoard Description");
        log("Checking the enabled checkbox.");
        createDBPF.checkEnabledBox();
        log("Drag and drop Pie chart to cell 1.");
        createDBPF.dragAndDropVisualization("Pie", 1);
        BasicUtil.waitADF(driver, large_timeout);
        log("Clicking on save and close.");
        createDBPF.clickSaveAndClose();
        log("Waiting for error popup.");
        BasicUtil.waitADF(driver, large_timeout);
        String error = createDBPF.getErrorsFromPopup();
        log("Printing errors: " + error);
        Assert.assertTrue(error.contains("Please select an Analysis View"));
        Assert.assertTrue(error.contains("Please select a Member"));
        log("Clicking OK button to close the error popup.");
        createDBPF.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Errors']")));
        log("Clicking close button to close the create dashboard dialog.");
        createDBPF.clickCancelButton();
    }

    /**
     * Verify all errors those can occur during blank entry of bar chart. Errors
     * : Please select an Analysis View. Errors : Please select a Member
     *
     * @throws java.lang.Exception
     */
    @Test(priority = 6, enabled = true)
    public void verifyErrorsFromBlankBarChart() throws Exception {
        BasicUtil.waitADF(driver, large_timeout);
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        log("Click on create dashboard button.");
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        log("Waiting for create dashboard dialog.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        log("Populating data on create dashboard dialog.");
        createDBPF.populateName("DashBoard Chart");
        createDBPF.populateDesc("DashBoard Description");
        createDBPF.checkEnabledBox();
        log("Drag and drop the bar chart.");
        createDBPF.dragAndDropVisualization("Bar", 1);
        BasicUtil.waitADF(driver, large_timeout);
        log("Click on save and close.");
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        log("Waiting for error popup.");
        String error = createDBPF.getErrorsFromPopup();
        log(error);
        Assert.assertTrue(error.contains("Please select an Analysis View"));
        Assert.assertTrue(error.contains("Please select a Member"));
        log("Click on the OK button.");
        createDBPF.clickOKButton();
        log("Close the create dadhboard popup.");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Errors']")));
        createDBPF.clickCancelButton();
    }

    /**
     * Verify all errors those can occur during blank entry of Stacked Area
     * chart. Errors : Please select an Analysis View. Please select a Member
     *
     */
    @Test(priority = 7, enabled = true)
    public void verifyErrorsFromBlankSAChart() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        log("Click on create dashboard button.");
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        log("Waiting for the create dadhboard popup.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        log("populating the create dashboard  dialog.");
        createDBPF.populateName("DashBoard Chart");
        createDBPF.populateDesc("DashBoard Description");
        createDBPF.checkEnabledBox();
        log("Drag and drop the stacked area chart.");
        createDBPF.dragAndDropVisualization("Stacked Area", 1);
        BasicUtil.waitADF(driver, large_timeout);
        log("Click on save and close");
        createDBPF.clickSaveAndClose();
        log("Get the errors from error popup.");
        BasicUtil.waitADF(driver, large_timeout);
        String error = createDBPF.getErrorsFromPopup();
        log(error);
        Assert.assertTrue(error.contains("Please select an Analysis View"));
        Assert.assertTrue(error.contains("Please select a Member"));
        log("Click on OK button.");
        createDBPF.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Errors']")));
        log("Click on the cancel button to close create dadhboard popup.");
        createDBPF.clickCancelButton();
    }

    /**
     * Verify all errors those can occur during blank entry of Scatter chart.
     * Error : Please select an Analysis View. Error : Please select X/Y-Axis
     * Member.
     *
     */
    @Test(priority = 8, enabled = true)
    public void verifyErrorsFromBlankScatterChart() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        log("Click on create db button.");
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("DashBoard Chart");
        createDBPF.populateDesc("DashBoard Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Scatter", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        String error = createDBPF.getErrorsFromPopup();
        log(error);
        Assert.assertTrue(error.contains("Please select an Analysis View"));
        Assert.assertTrue(error.contains("Please select X-Axis Member."));
        Assert.assertTrue(error.contains("Please select Y-Axis Member."));
        createDBPF.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Errors']")));
        log("Click on the cancel button to close create dadhboard popup.");
        createDBPF.clickCancelButton();
    }

    /**
     * Verify all errors those can occur during blank entry of Bubble chart.
     * Please select X-Axis Member. Please select Y-Axis Member. Please select
     * an Analysis View. Please select a Member for bubble size.
     *
     */
    @Test(priority = 9, enabled = true)
    public void verifyErrorsFromBlankBubbleChart() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("DashBoard Chart");
        createDBPF.populateDesc("DashBoard Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Bubble", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        String error = createDBPF.getErrorsFromPopup();
        log(error);
        Assert.assertTrue(error.contains("Please select X-Axis Member."));
        Assert.assertTrue(error.contains("Please select Y-Axis Member."));
        Assert.assertTrue(error.contains("Please select an Analysis View"));
        Assert.assertTrue(error.contains("Please select a Member for bubble size."));
        createDBPF.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Errors']")));
        createDBPF.clickCancelButton();
    }

    /**
     * Verify all errors those can occur during blank entry of Column chart.
     * Errors : Please select a Member. Please select an Analysis View.
     *
     */
    @Test(priority = 10, enabled = true)
    public void verifyErrorsFromBlankColumnChart() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("DashBoard Chart");
        createDBPF.populateDesc("DashBoard Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Column", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        String error = createDBPF.getErrorsFromPopup();
        log(error);
        Assert.assertTrue(error.contains("Please select an Analysis View"));
        Assert.assertTrue(error.contains("Please select a Member"));
        createDBPF.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Errors']")));
        createDBPF.clickCancelButton();
    }

    /**
     * Verify all errors those can occur during blank entry of Column chart.
     * Errors : Please select a KPI.
     *
     */
    @Test(priority = 11, enabled = true)
    public void verifyErrorsFromBlankKPIChart() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("DashBoard Chart");
        createDBPF.populateDesc("DashBoard Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Key Performance Indicator", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        String error = createDBPF.getErrorsFromPopup();
        log(error);
        Assert.assertTrue(error.contains("Please select a KPI."));
        //Assert.assertTrue(error.contains("Please select a Member"));
        createDBPF.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Errors']")));
        createDBPF.clickCancelButton();
    }

    @Test(priority = 12, enabled = true)
    public void verifyErrorsFromCombination() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("DashBoard Chart");
        createDBPF.populateDesc("DashBoard Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Line", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.dragAndDropVisualization("Bubble", 2);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.dragAndDropVisualization("Key Performance Indicator", 4);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        String error = createDBPF.getErrorsFromPopup();
        log(error);
        Assert.assertTrue(error.contains("Cell 1"));
        Assert.assertTrue(error.contains("Please select an Analysis View"));
        Assert.assertTrue(error.contains("Please select a Member"));
        Assert.assertTrue(error.contains("Cell 2"));
        Assert.assertTrue(error.contains("Please select X-Axis Member."));
        Assert.assertTrue(error.contains("Please select Y-Axis Member."));
        Assert.assertTrue(error.contains("Please select an Analysis View"));
        Assert.assertTrue(error.contains("Please select a Member for bubble size."));
        Assert.assertTrue(error.contains("Cell 4"));
        Assert.assertTrue(error.contains("Please select a KPI."));
        createDBPF.clickOKButton();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Errors']")));
        createDBPF.clickCancelButton();
    }

    @Test(priority = 13, enabled = true)
    public void createLineChartDB() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("Line Chart");
        createDBPF.populateDesc("Line Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Line", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(1);
        createDBPF.populateLineChartPrefrences("Test AV 2", "Line Chart");
        BasicUtil.waitADF(driver, large_timeout);
        driver.findElement(By.xpath(".//h1[text()='Cell Preferences']/ancestor::div[contains(@style,'position:absolute;top')]//button[text()='OK']")).click();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Create')]")));
        Assert.assertTrue(DBPage.doesDBExists("Line Chart"));
    }

    @Test(priority = 14, enabled = true)
    public void createPieChartDB() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("Pie Chart");
        createDBPF.populateDesc("Pie Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Pie", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(1);
        createDBPF.populatePieChartPrefrences("Test AV 2", "Pie Chart", "Income From Operations");
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOKButton();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Create')]")));
        Assert.assertTrue(DBPage.doesDBExists("Pie Chart"));
    }

    @Test(priority = 15, enabled = true)
    public void createBarChartDB() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("Bar Chart");
        createDBPF.populateDesc("Bar Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Bar", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(1);
        createDBPF.populateBarChartPrefrences("Test AV 2", "Bar Chart", "Income From Operations", "Gross Profit");
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOKButton();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Create')]")));
        Assert.assertTrue(DBPage.doesDBExists("Bar Chart"));
    }

    @Test(priority = 15, enabled = true)
    public void createStackedAreaChartDB() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("Stacked Chart");
        createDBPF.populateDesc("Stacked Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Stacked Area", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(1);
        createDBPF.populateStackedChartPrefrences("Test AV 2", "SA Header", "Income From Operations", "Gross Profit");
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOKButton();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Create')]")));
        Assert.assertTrue(DBPage.doesDBExists("Stacked Chart"));
    }

    @Test(priority = 15, enabled = true)
    public void createScatterChartDB() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("Scatter Chart");
        createDBPF.populateDesc("Scatter Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Scatter", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(1);
        createDBPF.populateScatterChartPrefrences("Test AV 2", "Scatter Header", "Income From Operations", "Gross Profit");
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOKButton();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Create')]")));
        Assert.assertTrue(DBPage.doesDBExists("Scatter Chart"));
    }

    @Test(priority = 16, enabled = true)
    public void createBubbleChartDB() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("Bubble Chart");
        createDBPF.populateDesc("Bubble Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Bubble", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(1);
        createDBPF.populateBubbleChartPrefrences("Test AV 2", "bubble Header", "Income From Operations", "Gross Profit", "Operating Expenses");
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOKButton();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Create')]")));
        Assert.assertTrue(DBPage.doesDBExists("Bubble Chart"));
    }

    @Test(priority = 16, enabled = true)
    public void createColumnChartDB() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("Column Chart");
        createDBPF.populateDesc("Column Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Column", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(1);
        createDBPF.populateColumnChartPrefrences("Test AV 2", "Column Header", "Income From Operations", "Gross Profit", "Operating Expenses");
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOKButton();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Create')]")));
        Assert.assertTrue(DBPage.doesDBExists("Column Chart"));
    }

    @Test(priority = 16, enabled = true)
    public void createKPIChartDB() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("KPI Chart");
        createDBPF.populateDesc("KPI Description");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Key Performance Indicator", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(1);
        createDBPF.populateKPIChartPrefrences("KPI 2", "KPI Header");
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOKButton();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));
        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Create')]")));
        Assert.assertTrue(DBPage.doesDBExists("KPI Chart"));
    }

    @Test(priority = 17, enabled = true)
    public void createCombinedChartDB() throws Exception {
        //BasicUtil.waitForGlassPaneToBeInvisible(driver,wait);
        BasicUtil.waitADF(driver, large_timeout);
        CreateEditDBPageFragment createDBPF = DBPage.clickCreateButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Create Dashboard')]")));
        createDBPF.populateName("Combined Chart");
        createDBPF.populateDesc("Description for a combined chart");
        createDBPF.checkEnabledBox();
        createDBPF.dragAndDropVisualization("Line", 1);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(1);
        createDBPF.populateLineChartPrefrences("Test AV 2", "Line Chart", "All");
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOKButton();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));
        createDBPF.dragAndDropVisualization("Column", 2);
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOnCellPreference(2);
        createDBPF.populateColumnChartPrefrences("Test AV 2", "Column Header", "Income From Operations", "Gross Profit", "Operating Expenses");
        BasicUtil.waitADF(driver, large_timeout);
        createDBPF.clickOKButton();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Cell Preferences')]")));

        createDBPF.clickSaveAndClose();
        BasicUtil.waitADF(driver, large_timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[contains(.,'Create')]")));
        Assert.assertTrue(DBPage.doesDBExists("Combined Chart"));
    }

    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            DBPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToDashboards();
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
                    log(" Navigating to Dashboards.");
                    DBPage = navPage.navigateToDashboards();
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
