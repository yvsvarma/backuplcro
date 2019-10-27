package poms;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

public class ProfitCurvesPage {
	private WebDriver driver;
	private WebDriverWait wait;
	Actions builder;
	By nameInputField = By.xpath(".//label[text()='Name']/../following-sibling::td//input");
	By descInputField = By.xpath(".//label[text()='Description']/../following-sibling::td//textarea");
	By enabledCheckBox = By.xpath(".//label[text()='Enabled']/../following-sibling::td//input");
	By analViewSel = By.xpath(".//tr[@title='Analysis View']/td[2]/a[contains(.,'Select')]");
	By measureMemberSel = By.xpath(".//label[text()='Measure Member']/../following-sibling::td//select");
	By xaxisLabelInput = By.xpath(".//label[text()='X Axis Label']/../following-sibling::td//input");
	By yaxisLabelInput = By.xpath(".//label[text()='Y Axis Label']/../following-sibling::td//input");
	//By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	public String xpathForProfitRow = ".//table[contains(@summary,'Profit Curves')]/tbody/tr";
	By profitTableItems = By.xpath(xpathForProfitRow);
	By deleteConfirmDlg = By.xpath(".//td[contains(.,'You are about to delete')]");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By createButton = By.xpath(".//button[text()='Create']");
	By OKButton = By.xpath(".//button[text()='OK']");
	By noButton = By.xpath(".//button[text()='No']");
	By refreshButton = By.xpath(".//img[@alt='Refresh']");
	By editMenuLink = By.xpath(".//tr[@title='Edit']/td[2]");
	By closeButton = By.xpath(".//button[text()='Close']");
	By copyMenuLink = By.xpath(".//tr[@title='Copy Profit Curve']");
	By diagMenuLink = By.xpath(".//tr[contains(@title,'Diagnose')]//td[contains(.,'Diagnose')]");
	By copySGInputField = By.xpath(".//label[text()='New Profit Curve Name']/../../td[2]/input");
	By editAVInputFieldName = By.xpath(".//label[text()='Name']/../../td[2]/input");
	By editAVCheckBoxEnabled = By.xpath(".//label[text()='Enabled']/../../td[2]//input");
	By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	FuseTablePO tablePO;
	
	public ProfitCurvesPage(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,10000);
		builder =  new Actions(driver);
		tablePO = new FuseTablePO(driver,"//table[contains( @summary,'Profit Curves')]");
	}
	
	public int getRowIDForProfit(String name){
		ArrayList<String> profitList = getAllprofits();
		for(String eachprofit : profitList)
		{
			if(eachprofit.contains(name))
				return profitList.indexOf(eachprofit);
		}
	
		return -1;
	}
	public boolean doesProfitExists(String name){
		By xpathForProfitRowLink = By.xpath( xpathForProfitRow + "/td[contains(.,'"+name+"')]");
		return !BasicUtil.waitForNonPresenceOfElement(driver, xpathForProfitRowLink);
	}
	public ProfitCurvesPage clickOnDeleteProfitButton(String name){
		this.tablePO.clickOnDeleteButton(name);
		return this;
	}

	public ProfitCurvesPage clickOnActionButton(String name){
		tablePO.clickOnActionButton(name);
		return this;
	}
	public ArrayList<String> getAllprofits(){
		return tablePO.getAllRows(); 
	}
	public ProfitCurvesPage clickYesButton(){
		driver.findElement(yesButton).click();
		return this;
	}
	public ProfitCurvesPage clickNoButton(){
		driver.findElement(noButton).click();
		return this;
	}
	public ProfitCurvesPage clickRefreshButton(){
		WebElement element = driver.findElement(refreshButton);
		element.click();
		BasicUtil.waitADF(driver, 100);
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[contains(@id,'Tracker')]")));
		return this;
	}
	
	public ProfitCurvesPage clickOnCopyMenuLink(String rowName){
		tablePO.selectRow(rowName);
		tablePO.clickOnActionsMenuText(rowName, "Copy");
		return this;
	}
	public ProfitCurvesPage clickOnEditMenuLink(String rowName){
		tablePO.selectRow(rowName);
		tablePO.clickOnEditButton(rowName);;
		return this;
	}
	public ProfitCurvesPage clickOnDiaMenuLink(String rowName){
		tablePO.selectRow(rowName);
		tablePO.clickOnActionsMenuText(rowName, "Diagnose");
		return this;
	}

	public ProfitCurvesPage clickOKButton(){
		driver.findElement(OKButton).click();
		return this;
	}
	public ProfitCurvesPage clickCloseButton(){
		driver.findElement(closeButton).click();
		return this;
	}
	public ProfitCurvesPage editName(String newName){
		driver.findElement(editAVInputFieldName).clear();
		driver.findElement(editAVInputFieldName).sendKeys(newName);
		return this;
	}
	public ProfitCurvesPage clickEnabledCheckBox(){
		driver.findElement(editAVCheckBoxEnabled).click();
		return this;
	}
	public ProfitCurvesPage clickSaveAndClose(){
		driver.findElement(saveCloseButton).click();
		return this;
	}

	public ProfitCurvesPage clickOnCreateButton() {
		tablePO.clickOnCreateButton();
		return this;
		
	}
	public ProfitCurvesPage populateNameField(String SGName) {
		driver.findElement(nameInputField).sendKeys(SGName);;
		return this;
		
	}
	public ProfitCurvesPage populateDescField(String desc) {
		driver.findElement(descInputField).sendKeys(desc);;
		return this;
		
	}
	public ProfitCurvesPage clickOnEnabled() {
		driver.findElement(enabledCheckBox).click();
		return this;
		
	}
	public ProfitCurvesPage selectAV(String avName) {
		driver.findElement(analViewSel).click();
		BasicUtil.waitADF(driver, 50);
		List<WebElement> elements = driver.findElements(By.xpath(".//td[contains(.,'"+avName+"') and @class='x10t']"));
		//.//td[contains(.,'Test AV 1') and @class='x10t']
		for(WebElement each : elements)
			{
				System.out.println(each.getText() +" "+each.getTagName());
				if(each.isDisplayed())
					each.click();
			}
		
		driver.findElement(By.xpath(".//h1[text()='Analysis Views']//ancestor::table//button[text()='OK']")).click();
		return this;
		
	}
	public ProfitCurvesPage selectMeasureDim(String measureDim) {
		BasicUtil.wait(3);
		Select xsel = new Select(driver.findElement(measureMemberSel));
		xsel.selectByVisibleText(measureDim);
		return this;
	}

	public ProfitCurvesPage populateYDimLabel(String yAxisLabel) {
		driver.findElement(yaxisLabelInput).sendKeys(yAxisLabel);
		return this;
	}
	public ProfitCurvesPage populateXDimLabel(String xAxisLabel) {
		driver.findElement(xaxisLabelInput).sendKeys(xAxisLabel);
		return this;
	}
	public ProfitCurvesPage createPCSubPage(String AVName, String SGName, String desc, String measure, String XDimLabel, String YDimLabel) {
		populateNameField(SGName);
		populateDescField(desc);
		clickOnEnabled();
		selectAV(AVName);
		selectMeasureDim(measure);
		populateXDimLabel(XDimLabel);
		populateYDimLabel(YDimLabel);
		clickSaveAndClose();
		return this;
		
	}
	public ProfitCurvesPage applyFilter(){
		//driver.findElement(arg0)
		return this;
	}

	public void typeNewCopyPCName(String newPCName) {
		driver.findElement(copySGInputField).sendKeys(newPCName);	
	}
}
