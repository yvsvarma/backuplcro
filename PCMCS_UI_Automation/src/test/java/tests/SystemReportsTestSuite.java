package tests;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import poms.FuseNavigatorPage;
import poms2.SystemReportPage;
import poms2.SystemReportPage.ReportName;
import poms2.SystemReportPage.ReportType;
import poms2.SystemReportPage.RuleDataType;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;
import utils.FileWatcher;

public class SystemReportsTestSuite extends BaseTestSuite {
	private SystemReportPage reportsPage;
	String className = this.getClass().getName();
	String downloadPath = "./target";
	
@Test(priority=1)
public void loginTest() throws Exception{
	//FuseLoginPage loginPage = new FuseLoginPage(driver);
	navPage = loginPage.doLogin(ml_application);
	Reporter.log("Login successful.");
}

@Test(priority=2,enabled=true)
public void NavigateToSRScreen() throws Exception{
	//FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
	log("Navigating to the System reports .");
	reportsPage = navPage.navigateToSystemReports();
	//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h1[contains(text(),'Manage Queries')]")));
	
	
}
@Test(priority=4,enabled=true)
public void checkProgramDocumentationHTML() throws Exception{
	log( "Selecting the POV.");
	reportsPage.selectPOV("2014","January", "Actual");
	log("Selecting Program documentation.");
	reportsPage.selectReportName(ReportName.Program_Documentation);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting HTML as report type.");
	reportsPage.selectReportType(ReportType.HTML);
	reportsPage.runReport();	
	Assert.assertTrue(FileWatcher.watch(downloadPath,"html"));
	BasicUtil.waitADF(driver, large_timeout);
}
@Test(priority=5,enabled=true)
public void checkProgramDocumentationPDF() throws Exception{
	log( "Selecting the POV.");
	reportsPage.selectPOV("2014","January", "Actual");
	log("Selecting Program documentation.");
	reportsPage.selectReportName(ReportName.Program_Documentation);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting PDF as report type.");
	reportsPage.selectReportType(ReportType.PDF);
	reportsPage.runReport();	
	Assert.assertTrue(FileWatcher.watch(downloadPath,"pdf"));
	BasicUtil.waitADF(driver, large_timeout);
}

@Test(priority=7,enabled=true)
public void checkProgramDocumentationWord() throws Exception{
	log( "Selecting the POV.");
	reportsPage.selectPOV("2014","January", "Actual");
	log("Selecting Program documentation.");
	reportsPage.selectReportName(ReportName.Program_Documentation);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting WORD as report type.");
	reportsPage.selectReportType(ReportType.WORD);
	reportsPage.runReport();	
	Assert.assertTrue(FileWatcher.watch(downloadPath,"doc"));
	BasicUtil.waitADF(driver, large_timeout);
}
@Test(priority=8,enabled=true)
public void checkProgramDocumentationExcel() throws Exception{
	log( "Selecting the POV.");
	reportsPage.selectPOV("2014","January", "Actual");
	log("Selecting Program documentation.");
	reportsPage.selectReportName(ReportName.Program_Documentation);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Excel as report type.");
	reportsPage.selectReportType(ReportType.EXCEL);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"xls"));
	BasicUtil.waitADF(driver, large_timeout);
	
}

@Test(priority=9,enabled=true)
public void checkDimensionStatsPDF() throws Exception{
	log("Selecting Dimension Stats.");
	reportsPage.selectReportName(ReportName.Dimension_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting PDF as report type.");
	reportsPage.selectReportType(ReportType.PDF);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"pdf"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=10,enabled=true)
public void checkDimensionStatsHTML() throws Exception{
	log("Selecting Dimension Stats.");
	reportsPage.selectReportName(ReportName.Dimension_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting HTML as report type.");
	reportsPage.selectReportType(ReportType.HTML);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"html"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=11,enabled=true)
public void checkDimensionStatsWord() throws Exception{
	log("Selecting Dimension Stats.");
	reportsPage.selectReportName(ReportName.Dimension_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Word as report type.");
	reportsPage.selectReportType(ReportType.WORD);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"doc"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=12,enabled=true)
public void checkDimensionStatsXML() throws Exception{
	log("Selecting Dimension Stats.");
	reportsPage.selectReportName(ReportName.Dimension_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting XML as report type.");
	reportsPage.selectReportType(ReportType.XML);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"xml"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=13,enabled=true)
public void checkDimensionStatsExcel() throws Exception{
	log("Selecting Dimension Stats documentation.");
	reportsPage.selectReportName(ReportName.Dimension_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Excel as report type.");
	reportsPage.selectReportType(ReportType.EXCEL);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"xls"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=14,enabled=true)
public void checkExecutionStatsError() throws Exception{
	log("Selecting Execution Stats report.");
	reportsPage.selectReportName(ReportName.Execution_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Excel as report type.");
	reportsPage.selectReportType(ReportType.EXCEL);
	reportsPage.runReport();
	BasicUtil.waitADF(driver, large_timeout);
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[text()='Operation cannot proceed because the job id is invalid or deleted in another session. Please select a valid job id and then retry your operation. ']")));
	reportsPage.clickOKButton();
	BasicUtil.waitADF(driver, large_timeout);
}
@Test(priority=15,enabled=true)
public void checkExecutionStatsExcel() throws Exception{
	log("Selecting Execution Stats report.");
	reportsPage.selectReportName(ReportName.Execution_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	reportsPage.selectFirstJobId();
	log("Selecting Excel as report type.");
	reportsPage.selectReportType(ReportType.EXCEL);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"xls"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=16,enabled=true)
public void checkExecutionStatsPDF() throws Exception{
	log("Selecting Execution Stats report.");
	reportsPage.selectReportName(ReportName.Execution_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	reportsPage.selectFirstJobId();
	log("Selecting PDF as report type.");
	reportsPage.selectReportType(ReportType.PDF);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"pdf"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=17,enabled=true)
public void checkExecutionStatsHTML() throws Exception{
	log("Selecting Execution Stats report.");
	reportsPage.selectReportName(ReportName.Execution_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	reportsPage.selectFirstJobId();
	log("Selecting HTML as report type.");
	reportsPage.selectReportType(ReportType.HTML);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"html"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=18,enabled=true)
public void checkExecutionStatsXML() throws Exception{
	log("Selecting Execution Stats report.");
	reportsPage.selectReportName(ReportName.Execution_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	reportsPage.selectFirstJobId();
	log("Selecting XML as report type.");
	reportsPage.selectReportType(ReportType.XML);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"xml"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=19,enabled=true)
public void checkExecutionStatsWord() throws Exception{
	log("Selecting Execution Stats report.");
	reportsPage.selectReportName(ReportName.Execution_Statistics);
	BasicUtil.waitADF(driver,small_timeout);
	reportsPage.selectFirstJobId();
	log("Selecting Word as report type.");
	reportsPage.selectReportType(ReportType.WORD);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"doc"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=20,enabled=true)
public void checkRuleDataSummaryHTML() throws Exception{
	log("Selecting Rule Data Validation Summary report.");
	reportsPage.selectReportName(ReportName.Rule_Data_Validation);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting POV.");
	reportsPage.selectPOV("2014", "January", "Actual");
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting HTML as report type.");
	reportsPage.selectReportType(ReportType.HTML);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Ruleset.");
	reportsPage.selectRuleSet("Activity Costing");
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Rule.");
	reportsPage.selectRule("Activity Costing Assignments");
	
	log("Selecting data raport type as summary.");
	reportsPage.clickRuleDataReportType(RuleDataType.SUMMARY);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"html"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=21,enabled=true)
public void checkRuleDataAllXML() throws Exception{
	log("Selecting Rule Data Validation Summary and Sample report.");
	reportsPage.selectReportName(ReportName.Rule_Data_Validation);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting POV.");
	reportsPage.selectPOV("2014", "January", "Actual");
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting HTML as report type.");
	reportsPage.selectReportType(ReportType.XML);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Ruleset.");
	reportsPage.selectRuleSet("Activity Costing");
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Rule.");
	reportsPage.selectRule("Activity Costing Assignments");
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting data raport type as summary and sample.");
	reportsPage.clickRuleDataReportType(RuleDataType.ALL);
	BasicUtil.waitADF(driver,small_timeout);
	reportsPage.changeStateForDriverDataCheckBox(true);
	reportsPage.changeStateForSourceDataCheckBox(true);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"xml"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@Test(priority=22,enabled=true)
public void checkRuleDataOnlyDriverExcel() throws Exception{
	log("Selecting Rule Data Validation Summary and Sample report.");
	reportsPage.selectReportName(ReportName.Rule_Data_Validation);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting POV.");
	reportsPage.selectPOV("2014", "January", "Actual");
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Excel as report type.");
	reportsPage.selectReportType(ReportType.EXCEL);
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Ruleset.");
	reportsPage.selectRuleSet("Occupancy Expense Allocations");
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting Rule.");
	reportsPage.selectRule("Rent and Utilities Reassignment");
	BasicUtil.waitADF(driver,small_timeout);
	log("Selecting data raport type as summary and sample.");
	reportsPage.clickRuleDataReportType(RuleDataType.ALL);
	BasicUtil.waitADF(driver,small_timeout);
	reportsPage.changeStateForDriverDataCheckBox(true);
	reportsPage.changeStateForSourceDataCheckBox(false);
	reportsPage.runReport();
	Assert.assertTrue(FileWatcher.watch(downloadPath,"xls"));
	BasicUtil.waitADF(driver, large_timeout);
	
}
@AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            reportsPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToSystemReports();
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
                    log(" Navigating to system reports.");
                    reportsPage = navPage.navigateToSystemReports();
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
