package tests;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import pagefragments.MemberSelectorPageFragment;
import pagefragments.RulePageFragment;
import pagefragments.RuleSetPageFragment;
import pagefragments.RuleSetPageFragment.ExecutionType;
import poms.FuseNavigatorPage;
import poms2.ManageRulesPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;

/*
 * Required data
 * POV: 2014:feb:Actual Blank POV
 * POV: 2015:December:Actual copy of 2014:January:Actual
 * 
 * 
 * 
 * */
public class ManageRulesTestSuite extends BaseTestSuite {
	ManageRulesPage rulesPage;
	String className = this.getClass().getName();


@Test(priority=1)
public void loginTest() throws Exception{
	//FuseLoginPage loginPage = new FuseLoginPage(driver);
	navPage = loginPage.doLogin(ml_application);
	log("Login successful.");
}

@Test(priority=2,enabled=true)
public void navigateToRulesScreen() throws Exception{
	//FuseNavigatorPage navPage = (FuseNavigatorPage)nextPage;
	log(" Navigating to Rules Page.");
	rulesPage = navPage.navigateToManageRules();

}
@Test(priority=3,enabled=true)
public void editRuleSet(){
	log( "Selecting the POV.");
	rulesPage.selectPOV("2015", "December", "Actual");
	log( "Select the ruleset.");
	RuleSetPageFragment ruleSetPage = rulesPage.selectRuleSet("Activity Costing");
	BasicUtil.waitADF(driver, large_timeout);
	Assert.assertTrue(ruleSetPage.getRuleSetName().equals("Activity Costing"));
	//Assert.assertTrue(ruleSetPage.getSequenceInput().equals("3"));
	log( "Setting the deccription of the ruleset.");
	ruleSetPage.setRuleSetDescription("Changed description");
	log( "Setting the sequence of the ruleset.");
	ruleSetPage.setSequence("1");
	log( "Clicking the save button of the ruleset.");
	ruleSetPage.clickSaveButton();
	BasicUtil.waitADF(driver, small_timeout);
	log( "Refreshing the ruleset.");
	rulesPage.selectRuleSet("Occupancy Expense Allocations");
	BasicUtil.waitADF(driver, small_timeout);
	log( "Reselecting the ruleset.");
	ruleSetPage = rulesPage.selectRuleSet("Activity Costing");
	BasicUtil.waitADF(driver, large_timeout);
	//Assert.assertTrue(ruleSetPage.getRuleSetDescription().equals("Changed description"));
	Assert.assertTrue(ruleSetPage.getSequenceInput().equals("1"));
}
@Test(priority=4,enabled=true)
public void editRuleSetExecutionType(){
	log( "Selecting the POV.");
	rulesPage.selectPOV("2015", "December", "Actual");
	log( "Select the ruleset.");
	RuleSetPageFragment ruleSetPage = rulesPage.selectRuleSet("Activity Costing");
	BasicUtil.waitADF(driver, large_timeout);
	Assert.assertTrue(ruleSetPage.getRuleSetName().equals("Activity Costing"));
	Assert.assertTrue(ruleSetPage.getRuleSetExecutionType().equals(ExecutionType.Serial_Execution), "");
	BasicUtil.waitADF(driver, large_timeout);
	//Assert.assertTrue(ruleSetPage.getSequenceInput().equals("3"));
	log( "Editing the execution type of the ruleset.");
	ruleSetPage.selectRuleSetExecutionType(ExecutionType.Iterative_Execution);
	ruleSetPage.clickSaveButton();
	BasicUtil.waitADF(driver, small_timeout);
	log( "Refreshing the ruleset.");
	rulesPage.selectRuleSet("Occupancy Expense Allocations");
	BasicUtil.waitADF(driver, small_timeout);
	log( "Reselecting the ruleset.");
	ruleSetPage = rulesPage.selectRuleSet("Activity Costing");
	BasicUtil.waitADF(driver, large_timeout);
	Assert.assertTrue(ruleSetPage.getRuleSetExecutionType().equals(ExecutionType.Iterative_Execution), "Execution typedid not changed to iterative.");
}
@Test(priority=5,enabled=false)
public void editRuleSetContext(){
	log( "Selecting the POV.");
	rulesPage.selectPOV("2015", "December", "Actual");
	log( "Select the ruleset.");
	RuleSetPageFragment ruleSetPage = rulesPage.selectRuleSet("Manufacturing COGs Related Expense Assignment");
	BasicUtil.waitADF(driver, large_timeout);
	Assert.assertTrue(ruleSetPage.getRuleSetName().equals("Manufacturing COGs Related Expense Assignment"));
	//Assert.assertTrue(ruleSetPage.getSequenceInput().equals("3"));
	log( "Editing the context of the ruleset.");
	MemberSelectorPageFragment memSelPage = ruleSetPage.goToContextTab();
	BasicUtil.waitADF(driver, large_timeout);
	log( "Delete the context for a dimension.");
	memSelPage.deleteAllMembersForDimension("Customers", false);
	BasicUtil.waitADF(driver, large_timeout);
	log( "Select a new context.");
	memSelPage.addMember("Customer:AllCustomers:Big Box:BB100", false);
	
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	log( "Saving the ruleset.");
	ruleSetPage.clickSaveButton();
	BasicUtil.waitADF(driver, small_timeout);
	log( "Refreshing the ruleset.");
	rulesPage.selectRuleSet("Occupancy Expense Allocations");
	BasicUtil.waitADF(driver, small_timeout);
	log( "Reselecting the ruleset.");
	ruleSetPage = rulesPage.selectRuleSet("Manufacturing COGs Related Expense Assignment");
	BasicUtil.waitADF(driver, large_timeout);
	memSelPage = ruleSetPage.goToContextTab();
	BasicUtil.waitADF(driver, large_timeout);
	memSelPage.selectDimension("Customers", false);
	BasicUtil.waitADF(driver, large_timeout);
	
	Assert.assertTrue(!BasicUtil.waitForNonPresenceOfElement(driver,By.xpath(".//td[text()='BB100']")), "RuleSet Context failed to change.");
}

@Test(priority=6,enabled=true)
public void createNewRuleSet(){
	log( "Selecting the POV.");
	rulesPage.selectPOV("2014", "February", "Actual");
	log( "Clicking Create button for the new ruleset.");
	RuleSetPageFragment ruleSetPage = rulesPage.clickAddRuleSetButton();
	BasicUtil.waitADF(driver, large_timeout);
	log( "Setting name for the new ruleset.");
	ruleSetPage.setRuleSetName("Test Rule Set");
	log( "Setting description for the new ruleset.");
	ruleSetPage.setRuleSetDescription("Description");
	log( "Setting Enabled for the new ruleset.");
	ruleSetPage.clickEnabledCheckBox();
	log( "Setting sequence for the new ruleset.");
	ruleSetPage.setSequence("1");
	log( "Setting exceution type for the new ruleset.");
	ruleSetPage.selectRuleSetExecutionType(ExecutionType.Parallel_Execution);
	log( "Setting use gc checkbox for the new ruleset.");
	ruleSetPage.clickUseGlobalContextCheckBox();
	log( "Setting context for the new ruleset.");
	MemberSelectorPageFragment memSelPage = ruleSetPage.goToContextTab();
	BasicUtil.waitADF(driver, large_timeout);
	log( "Select a new context.");
	memSelPage.addMember("Customer:AllCustomers:Big Box:BB100", false);
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	log( "Saving the ruleset.");
	ruleSetPage.clickSaveButton();
	BasicUtil.waitADF(driver, small_timeout);
	log( "Refreshing the ruleset.");
	rulesPage.selectPOV("2014", "January", "Actual");
	BasicUtil.waitADF(driver, small_timeout);
	rulesPage.selectPOV("2014", "February", "Actual");
	log( "Reselecting the ruleset.");
	ruleSetPage = rulesPage.selectRuleSet("Test Rule Set");
	
}
//.//div[contains(@id,'ruleSetConfirmDel')]//button[text()='Yes']
@Test(priority=7,enabled=true)
public void copyRuleSet(){
	String copyRuleSetName = "Copy Rule Set";
	log( "Selecting the POV.");
	rulesPage.selectPOV("2015", "December", "Actual");
	log( "Select the ruleset.");
	rulesPage.selectRuleSet("Manufacturing COGs Related Expense Assignment");
	BasicUtil.waitADF(driver, large_timeout);
	log( "Copying the ruleset.");
	BasicUtil.wait(3);
	rulesPage.clickCopyRuleSetButton();
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(.,'Copy Rule Set in current point of view')]")));
	rulesPage.setNewCopyRuleSetName(copyRuleSetName);
	driver.findElement(By.xpath(".//div[contains(@id,'copyRuleSet')]//button[text()='OK']")).click();
	BasicUtil.waitADF(driver, large_timeout);
	BasicUtil.wait(3);
	//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(.,'Copy Rule Set in current point of view')]")));
	Assert.assertTrue(rulesPage.doesThisRuleSetExists(copyRuleSetName),"Copied Rule set does not exist.");
}
@Test(priority=8,enabled=true)
public void deleteRuleSet(){
	log( "Selecting the POV.");
	rulesPage.selectPOV("2015", "December", "Actual");
	log( "Clicking delete button for the new ruleset.");
	RuleSetPageFragment ruleSetPage = rulesPage.selectRuleSet("Activity Costing");
	BasicUtil.waitADF(driver, large_timeout);
	Assert.assertTrue(ruleSetPage.getRuleSetName().equals("Activity Costing"));
	log( "Deleting the rule set.");
	rulesPage.clickDeleteRuleSetButton();
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[contains(@id,'ruleSetConfirmDel')]//button[text()='Yes']")));
	driver.findElement(By.xpath(".//div[contains(@id,'ruleSetConfirmDel')]//button[text()='Yes']")).click();
	
}
@Test(priority=9,enabled=false)
public void createAllocationRule(){
	log( "Selecting the POV.");
	rulesPage.selectPOV("2015", "December", "Actual");
	log( "Clicking Create button for the new ruleset.");
	rulesPage.selectRuleSet("Occupancy Expense Allocations");
	BasicUtil.waitADF(driver, large_timeout);
	rulesPage.clickAddRuleButton();
	BasicUtil.waitADF(driver, small_timeout);
	RulePageFragment newRulePage = rulesPage.clickAllocationRuleDropdown();
	BasicUtil.waitADF(driver, small_timeout);
	newRulePage.goToDescriptionTab();
	newRulePage.setRuleName("New Allocation Rule");
	newRulePage.setRuleDescription("This is description.");
	newRulePage.setRuleSequence("2");
	MemberSelectorPageFragment memSel =newRulePage.goToSourceTab();
	memSel.addMember("Accounts:NetIncome:Income From Operations:Operating Expenses:Personnel Expenses:PER2400", false);
	//memSel.addMember("Accounts:NetIncome:Income From Operations:Gross Profit", true);
	memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
	//memSel.addMember("CostCenters:AllCostCenters:CC4000", true);
	BasicUtil.waitADF(driver, small_timeout);
	memSel =newRulePage.goToDestinationTab();
	memSel.addMember("Accounts:Statistics:Sales Statistics:STAT1001", false);
	memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
	BasicUtil.waitADF(driver, small_timeout);
	newRulePage.goToDriverTab();
	newRulePage.clickAllocateEvenlyRadio();
	BasicUtil.waitADF(driver, small_timeout);
	newRulePage.clickSaveButton();
	
}
@AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            rulesPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToManageRules();
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
                    log(" Navigating to MQ.");
                    rulesPage = navPage.navigateToManageRules();
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
