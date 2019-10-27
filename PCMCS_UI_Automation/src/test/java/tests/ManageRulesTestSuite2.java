package tests;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import pagefragments.MemberSelectorPageFragment;
import pagefragments.RulePageFragment;
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
public class ManageRulesTestSuite2 extends BaseTestSuite {

    ManageRulesPage rulesPage;
    String className = this.getClass().getName();

    @Test(priority = 1)
    public void loginTest() throws Exception {
//        FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin(ml_application);
        log("Login successful.");
    }

    @Test(priority = 2, enabled = true)
    public void navigateToRulesScreen() throws Exception {
        //FuseNavigatorPage navPage = (FuseNavigatorPage) nextPage;
        log(" Navigating to Rules Page.");
        rulesPage = navPage.navigateToManageRules();

    }

    @Test(priority = 3, enabled = true)
    public void createAllocationRule() {
        log("Selecting the POV.");
        rulesPage.selectPOV("2015", "December", "Actual");
        log("Clicking Create button for the new ruleset.");
        rulesPage.selectRuleSet("Occupancy Expense Allocations");
        BasicUtil.waitADF(driver, large_timeout);
        rulesPage.clickAddRuleButton();
        BasicUtil.wait(5);
        BasicUtil.waitADF(driver, small_timeout);
        RulePageFragment newRulePage = rulesPage.clickAllocationRuleDropdown();
        BasicUtil.waitADF(driver, small_timeout);
        newRulePage.goToDescriptionTab();
        newRulePage.setRuleName("New Allocation Rule");
        newRulePage.setRuleDescription("This is description.");
        newRulePage.setRuleSequence("2");
        MemberSelectorPageFragment memSel = newRulePage.goToSourceTab();

        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        //memSel.addMember("CostCenters:AllCostCenters:CC4000", true);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:NetIncome:Income From Operations:Operating Expenses:Personnel Expenses:PER2400", false);
        //memSel.addMember("Accounts:NetIncome:Income From Operations:Gross Profit", true);
        BasicUtil.waitADF(driver, large_timeout);

        memSel = newRulePage.goToDestinationTab();

        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:Statistics:Sales Statistics:STAT1001", false);
        BasicUtil.waitADF(driver, large_timeout);

        //BasicUtil.waitADF(driver, small_timeout);
        newRulePage.goToDriverTab();
        newRulePage.clickAllocateEvenlyRadio();
        //BasicUtil.waitADF(driver, small_timeout);
        BasicUtil.waitADF(driver, large_timeout);
        newRulePage.clickRuleSaveButton();

    }

    @Test(priority = 4, enabled = true)
    public void createAllocationRuleWithDriver() {
        log("Selecting the POV.");
        rulesPage.selectPOV("2015", "December", "Actual");
        log("Clicking Create button for the new ruleset.");
        rulesPage.selectRuleSet("Occupancy Expense Allocations");
        BasicUtil.waitADF(driver, large_timeout);
        rulesPage.clickAddRuleButton();
        BasicUtil.wait(5);
        BasicUtil.waitADF(driver, small_timeout);
        RulePageFragment newRulePage = rulesPage.clickAllocationRuleDropdown();
        BasicUtil.waitADF(driver, small_timeout);
        newRulePage.goToDescriptionTab();
        newRulePage.setRuleName("New Allocation Rule 2");
        newRulePage.setRuleDescription("This is description.");
        newRulePage.setRuleSequence("1");
        MemberSelectorPageFragment memSel = newRulePage.goToSourceTab();
        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        //memSel.addMember("CostCenters:AllCostCenters:CC4000", true);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:NetIncome:Income From Operations:Operating Expenses:Personnel Expenses:PER2400", false);
        //memSel.addMember("Accounts:NetIncome:Income From Operations:Gross Profit", true);
        BasicUtil.waitADF(driver, large_timeout);

        memSel = newRulePage.goToDestinationTab();

        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:Statistics:Sales Statistics:STAT1001", false);
        BasicUtil.waitADF(driver, large_timeout);

        //BasicUtil.waitADF(driver, small_timeout);
        newRulePage.goToDriverTab();
        memSel = newRulePage.clickSpecifyDriverRadio();

        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:Statistics:Sales Statistics:STAT1001", false);

        //BasicUtil.waitADF(driver, small_timeout);
        BasicUtil.waitADF(driver, large_timeout);
        newRulePage.clickRuleSaveButton();

    }

    @Test(priority = 4, enabled = true)
    public void createAllocationRuleSAS() {
        log("Selecting the POV.");
        rulesPage.selectPOV("2015", "December", "Actual");
        log("Clicking Create button for the new ruleset.");
        rulesPage.selectRuleSet("Occupancy Expense Allocations");
        BasicUtil.waitADF(driver, large_timeout);
        rulesPage.clickAddRuleButton();
        BasicUtil.wait(5);
        BasicUtil.waitADF(driver, small_timeout);
        RulePageFragment newRulePage = rulesPage.clickAllocationRuleDropdown();
        BasicUtil.waitADF(driver, small_timeout);
        newRulePage.goToDescriptionTab();
        newRulePage.setRuleName("New Allocation Rule SAS");
        newRulePage.setRuleDescription("This is description.");
        newRulePage.setRuleSequence("1");
        MemberSelectorPageFragment memSel = newRulePage.goToSourceTab();
        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        //memSel.addMember("CostCenters:AllCostCenters:CC4000", true);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:NetIncome:Income From Operations:Operating Expenses:Personnel Expenses:PER2400", false);
        //memSel.addMember("Accounts:NetIncome:Income From Operations:Gross Profit", true);
        BasicUtil.waitADF(driver, large_timeout);

        memSel = newRulePage.goToDestinationTab();
        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.selectDimensionSAS("Accounts");
        BasicUtil.waitADF(driver, large_timeout);

        //BasicUtil.waitADF(driver, small_timeout);
        newRulePage.goToDriverTab();
        memSel = newRulePage.clickSpecifyDriverRadio();
        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        //BasicUtil.waitADF(driver, small_timeout);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:Statistics:Sales Statistics:STAT1001", false);
        BasicUtil.waitADF(driver, large_timeout);

        newRulePage.clickRuleSaveButton();
        BasicUtil.waitADF(driver, large_timeout);

    }

    @Test(priority = 4, enabled = true)
    public void createAllocationRuleWithOffset() {
        log("Selecting the POV.");
        rulesPage.selectPOV("2015", "December", "Actual");
        log("Clicking Create button for the new ruleset.");
        rulesPage.selectRuleSet("Occupancy Expense Allocations");
        BasicUtil.waitADF(driver, large_timeout);
        rulesPage.clickAddRuleButton();
        BasicUtil.wait(5);
        BasicUtil.waitADF(driver, small_timeout);
        RulePageFragment newRulePage = rulesPage.clickAllocationRuleDropdown();
        BasicUtil.waitADF(driver, small_timeout);
        newRulePage.goToDescriptionTab();
        newRulePage.setRuleName("New Allocation Rule offset");
        newRulePage.setRuleDescription("This is description.");
        newRulePage.setRuleSequence("1");
        MemberSelectorPageFragment memSel = newRulePage.goToSourceTab();
        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        //memSel.addMember("CostCenters:AllCostCenters:CC4000", true);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:NetIncome:Income From Operations:Operating Expenses:Personnel Expenses:PER2400", false);
        //memSel.addMember("Accounts:NetIncome:Income From Operations:Gross Profit", true);
        BasicUtil.waitADF(driver, large_timeout);

        memSel = newRulePage.goToDestinationTab();
        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:Statistics:Sales Statistics:STAT1001", false);
        BasicUtil.waitADF(driver, large_timeout);

        //BasicUtil.waitADF(driver, small_timeout);
        newRulePage.goToDriverTab();
        memSel = newRulePage.clickSpecifyDriverRadio();
        memSel.addMember("CostCenters:AllCostCenters:CC2001", false);
        //BasicUtil.waitADF(driver, small_timeout);
        BasicUtil.waitADF(driver, large_timeout);
        memSel.addMember("Accounts:Statistics:Sales Statistics:STAT1001", false);
        BasicUtil.waitADF(driver, large_timeout);

        newRulePage.goToOffsetTab();
        memSel = newRulePage.clickAlternateOffsetRadio();
        memSel.addMember("Accounts:Statistics:Sales Statistics:STAT1001", false);
        BasicUtil.waitADF(driver, large_timeout);
        newRulePage.clickRuleSaveButton();

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
                    log(" Navigating to DM.");
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
