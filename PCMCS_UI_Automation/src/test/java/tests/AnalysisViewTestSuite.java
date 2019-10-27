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

import pagefragments.CreateEditAVPageFragment;
import pagefragments.MemberSelectorFusePF;
import poms.AnalysisViewPage;
import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import poms.IntelligencePage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;
import utils.ClientSynchedWithServer;
/**
 * @author      Mohnish Banchhor <mohnish.banchhor@oracle.com>
 * @version     1.0
 * @since       1.0          
 */
public class AnalysisViewTestSuite extends BaseTestSuite{
	/**
	 *  Analysis view Page Object Model
	 */
	AnalysisViewPage AVPage;
	/**
	 *  Classname for logging
	 */
	String className = this.getClass().getName();

/**
 * Simulates login action by user
 * @throws Exception
 */
@Test(priority=1)
public void loginTest() throws Exception{
	FuseLoginPage loginPage = new FuseLoginPage(driver);
	navPage = loginPage.doLogin(ml_application);
	log("Login successful.");
}

/**
 * Navigate to Analysis View Page
 * @throws Exception
 */
@Test(priority=2,enabled=true)
public void navigateToAnalysisViewPage() throws Exception{
	log(" Navigating to Intelligence.");
	IntelligencePage intelligencePage = navPage.navigateToIntelligence();
	log("Navigating to AVs.");
	AnalysisViewPage avPage = intelligencePage.clickAVSubPageLink();
	log("Listing all AVs");
	ArrayList<String> avList = avPage.getAllAVs();
	log("The number of AVs visible : "+avList.size());
	for(String eachAV : avList )
		log("Analysis View : "+eachAV);
	AVPage = avPage;	
}
/**
 * Deletes an existing Analysis View
 * 
 */
@Test(priority=100,enabled=true)
public void testDeleteAnalysisView(){
	String deleteAVName = "10 Department Stores YTD";
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	log("Deleting the Analysis View "+deleteAVName+".");
	AVPage.clickOnDeleteAVButton(deleteAVName).clickYesButton();
	wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//td/div[text()='Delete']")));
	//AVPage.clickRefreshButton();
	BasicUtil.waitADF(driver, large_timeout);
	Assert.assertTrue("Analysis View "+deleteAVName+" is not deleted",!AVPage.doesAVExists(deleteAVName));
	log("Analysis View "+deleteAVName+" is deleted.");	
}
/**
 * Copies an existing Analysis View to another Analysis View
 */
@Test(priority=4,enabled=true)
public void testCopyAnalysisView(){
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	BasicUtil.waitADF(driver, large_timeout);
	log("Clicking on AV Action button.");
	AVPage.clickOnActionButton("10 Chain Level Jan 2016 Profit");
	log("Clicking on AV copy menu item.");
	AVPage.clickOnCopyMenuLink("10 Chain Level Jan 2016 Profit");
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[text()='Copy Analysis View']")));
	log("Input AV copy new name.");
        BasicUtil.waitADF(driver, large_timeout);
	AVPage.typeNewCopyAVName("Copy1");
	log("Click OK.");
	AVPage.clickOKButton();
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	BasicUtil.waitADF(driver, large_timeout);
	Assert.assertTrue("Failed to copy the AV.",AVPage.doesAVExists("Copy1"));
	log("AV is copied.");
	
}
/**
 * Diagnose an Analysis View
 */
@Test(priority=5,enabled=true)
public void testDiagnoseAV(){
	String diagAVName = "10 Chain Level Jan 2016 Profit";
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	driver.findElement(By.xpath(".//span[text()='Analysis Views']")).click();
	//AVPage.clickRefreshButton();
	log("Clicking on AV Action button.");
	driver.findElement(By.xpath(".//span[text()='Analysis Views']")).click();
	BasicUtil.waitADF(driver, small_timeout);
	AVPage.clickOnActionButton(diagAVName);
	log("Clicking on diagnose button.");
	AVPage.clickOnDiaMenuLink(diagAVName);
	log("Waiting for diagnosis.");
	BasicUtil.waitADF(driver, large_timeout);
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Diagnose Analysis View']")));
	log("Clicking on close.");
	BasicUtil.waitADF(driver, large_timeout);
	driver.findElement(By.xpath(".//button[contains(@id,'close') and text()='Close']")).click();
	log("Diagnose of "+diagAVName+" was successful.");
}
/**
 * Create an Analysis view with data slice
 * 
 */
@Test(priority=9,enabled=true)
public void testCreateAnalysisViewWithDataSlice(){
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	BasicUtil.waitADF(driver, large_timeout);
	log("Clicking on AV Create button.");
	CreateEditAVPageFragment createAVPage = AVPage.clickCreateButton();
	log("Waiting for Create AV Dialog.");
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Create Analysis View']")));
	log("Clicking on definition tab.");
	createAVPage.clickOnDefinitionTab();
	log("Populating name.");
	createAVPage.populateName("0000Test AV 6");
	log("Populating desc.");
	createAVPage.populateDesc("New description");
	log("Clicking on enabled button .");
	createAVPage.checkEnabledBox();
	log("Selecting row dimension .");
	createAVPage.selectRowDim("Account");
	log("Selecting row member .");
	MemberSelectorFusePF memSelPage = createAVPage.clickOnSelectRowMemLink();
	log("Selecting row memeber in member selector.");
	memSelPage.addMember("Account","NetIncome:Income From Operations:Gross Profit", "");
	log("Clicking OK in member selector.");
	memSelPage.clickOKButton();
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Create Analysis View']")));
	new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
	log("Selecting column dimension.");
	createAVPage.selectColDim("Product");
	log("clicking column dimension member selector.");
	new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
	memSelPage = createAVPage.clickOnSelectColMemLink();
	log("Selecting column dimension member in memebr selector.");
	memSelPage.addMember("Product","AllProducts:Bikes", "");
	log("Clicking OK in member selector.");
	memSelPage.clickOKButton();
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Create Analysis View']")));
	log("Openeing data slice tab.");
	createAVPage.clickOnDataSliceTab();
	log("Openeing member selector in data slice dim.");
	memSelPage = createAVPage.editDataSlice("Year");
	log("Seelcting member in member selector.");
	memSelPage.addMember("Year", "2014", "");
	log("Clicking OK in member selector.");
	memSelPage.clickOKButton();
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Create Analysis View']")));
	log("Clicking Save and Close.");
	new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
	createAVPage.clickSaveAndClose();
	Assert.assertTrue("Failed to create a new AV.",AVPage.doesAVExists("0000Test AV 6"));
}
/**
 * Edit an exiting Analysis View.
 */
@Test(priority=8,enabled=true,dependsOnMethods="testCopyAnalysisView")
public void testEditAnalysisView(){
	String editAVName = "10 Chain Level Jan 2016 Profit";
	String editedAVName = "0000Renamed Analysis View";
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	BasicUtil.waitADF(driver, large_timeout);
	log("Clicking on AV Action button.");
	AVPage.clickOnActionButton(editAVName);
	log("Clicking on edit av.");
	AVPage.clickOnEditMenuLink(editAVName);
	log("waiting for Edit.");
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Edit Analysis View']")));
	AVPage.editName(editedAVName);
	//AVPage.clickEnabledCheckBox();
	AVPage.saveAndCloseAVEdit();
	wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//h1[text()='Edit Analysis View']")));
	BasicUtil.waitADF(driver, large_timeout);
	Assert.assertTrue("Editing the AV failed.",AVPage.doesAVExists(editedAVName));
	log("editing successful.");
}
/**
 * Create a new Analysis View
 */
@Test(priority=6,enabled=true)
public void testCreateAV(){
	String newAVName = "000Test AV 5";
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	BasicUtil.waitADF(driver, large_timeout);
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath("/html[1]/body[1]/div[1]/form[1]/div[2]/div[2]/div[contains(.,'Used in')]")))
		driver.findElement(By.xpath(".//h1[text()='Analysis Views']")).click();
	log("Clicking on AV Create button.");
	CreateEditAVPageFragment createAVPage = AVPage.clickCreateButton();
	log("Waiting for Create AV Dialog.");
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Create Analysis View']")));
	log("Clicking on definition tab.");
	createAVPage.clickOnDefinitionTab();
	log("Populating name.");
	createAVPage.populateName(newAVName);
	log("Populating desc.");
	createAVPage.populateDesc("New description");
	log("Clicking on enabled button .");
	createAVPage.checkEnabledBox();
	log("Selecting row dimension .");
	createAVPage.selectRowDim("Account");
	log("Selecting row member .");
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//option[text()='Account']")));
	MemberSelectorFusePF memSelPage = createAVPage.clickOnSelectRowMemLink();
	log("Selecting row memeber in member selector.");
	memSelPage.addMember("Account","NetIncome:Income From Operations:Gross Profit", "");
	log("Clicking OK in member selector.");
	memSelPage.clickOKButton();
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Create Analysis View']")));
	BasicUtil.wait(2);
	log("Selecting column dimension.");
	createAVPage.selectColDim("Product");
	log("clicking column dimension member selector.");
	BasicUtil.wait(2);
	memSelPage = createAVPage.clickOnSelectColMemLink();
	log("Selecting column dimension member in memebr selector.");
	memSelPage.addMember("Product","AllProducts:Bikes", "");
	log("Clicking OK in member selector.");
	memSelPage.clickOKButton();
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Create Analysis View']")));
	createAVPage.clickSaveAndClose();
	wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//h1[text()='Create Analysis View']")));
	BasicUtil.waitADF(driver, large_timeout);
	Assert.assertTrue("Creating the AV failed.",AVPage.doesAVExists(newAVName));
	log("editing successful.");
	
}
/**
 * Delete Analysis View which is used in other analytic items.
 * Verify the error.
 */
@Test(priority=7,enabled=true)
public void testDeleteUsedAV(){
	String usedAVName = "10 Chain Profit Margin Percent Trailing 6 Months";
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	driver.findElement(By.xpath(".//span[text()='Analysis Views']")).click();
	new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
	BasicUtil.waitADF(driver, large_timeout);
	log("Clicking on  Delete button for a utilised AV.");
	AVPage.clickOnDeleteAVButton(usedAVName);
	log("Waiting for Error Dialog.");
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//h2[text()='Row and Column Definition']")))
		driver.findElement(By.xpath(".//span[text()='Analysis Views']")).click();
	
	BasicUtil.waitADF(driver, large_timeout);
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//div[text()='Error']")));
	log("Checking for error message.");
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//div[contains(.,'cannot be deleted.')]")));
	BasicUtil.waitADF(driver, large_timeout);
	log( "Clicking on OK.");
	driver.findElement(By.xpath(".//div[text()='Error']/ancestor::tbody/tr[3]//button")).click();
	Assert.assertTrue("Used Analysis View is deleted.", AVPage.doesAVExists(usedAVName));
	log( "Used AV cannot be deleted. Success.");
}
/**
 * Verify validations during Create Analysis View Dialog.
 */
@Test(priority=8,enabled=false)
public void testValidationsCreateAV(){
	if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//div[@class='AFModalGlassPane']")))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFModalGlassPane']")));
	BasicUtil.waitADF(driver, large_timeout);
	log("Clicking on AV Create button.");
	CreateEditAVPageFragment createAVPage = AVPage.clickCreateButton();
	log("Waiting for Create AV Dialog.");
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//h1[text()='Create Analysis View']")));
	log("Clicking on definition tab.");
	createAVPage.clickOnDefinitionTab();
	log("Clicking on save and close.");
	createAVPage.clickSaveAndClose();
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//li[text()='Analysis View name is required']")));
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//li[text()='The Row and Column dimension should be different.']")));
	wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//li[text()='At least one dimension member should be selected for Row and Column dimensions. ']")));
	createAVPage.clickOKButton();
	wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[text()='Errors']")));
	createAVPage.clickCancelButton();
}
    @AfterMethod
    @Override
    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.pass("Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            AVPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToIntelligence().clickAVSubPageLink();
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
                    log("Navigating to AV.");
                    AVPage = intelligencePage.clickAVSubPageLink();
                    
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
