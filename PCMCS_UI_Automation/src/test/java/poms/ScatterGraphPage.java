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
import utils.ClientSynchedWithServer;

public class ScatterGraphPage {
	private WebDriver driver;
	private WebDriverWait wait;
	Actions builder;
	By nameInputField = By.xpath(".//label[text()='Name']/../following-sibling::td//input");
	By descInputField = By.xpath(".//label[text()='Description']/../following-sibling::td//textarea");
	By enabledCheckBox = By.xpath(".//label[text()='Enabled']/../following-sibling::td//input");
	By analViewSel = By.xpath(".//tr[@title='Analysis View']/td[2]/a[contains(.,'Select')]");
	By xaxisDimSel = By.xpath(".//label[text()='X Axis Member']/../following-sibling::td//select");
	By xaxisLabel = By.xpath(".//label[text()='X Axis Label']/../following-sibling::td//input");
	By yaxisDimSel = By.xpath(".//label[text()='Y Axis Member']/../following-sibling::td//select");
	By yaxisLabel = By.xpath(".//label[text()='Y Axis Label']/../following-sibling::td//input");
	//By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	public String xpathForScatterRow = ".//table[contains(@summary,'Scatter Analysis')]/tbody/tr";
	By scatterTableItems = By.xpath(xpathForScatterRow);
	By deleteConfirmDlg = By.xpath(".//td[contains(.,'You are about to delete')]");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By createButton = By.xpath(".//button[text()='Create']");
	By OKButton = By.xpath(".//button[text()='OK']");
	By noButton = By.xpath(".//button[text()='No']");
	By refreshButton = By.xpath(".//img[@alt='Refresh']");
	By editMenuLink = By.xpath(".//tr[@title='Edit']/td[2]");
	By closeButton = By.xpath(".//button[text()='Close']");
	By copyMenuLink = By.xpath(".//tr[@title='Copy Scatter Analysis']/td[2]");
	By diagMenuLink = By.xpath(".//tr[contains(@title,'Diagnose')]//td[contains(.,'Diagnose')]");
	By copySGInputField = By.xpath(".//label[text()='New Scatter Analysis Name']/../../td[2]/input");
	By editAVInputFieldName = By.xpath(".//label[text()='Name']/../../td[2]/input");
	By editAVCheckBoxEnabled = By.xpath(".//label[text()='Enabled']/../../td[2]//input");
	By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	FuseTablePO tablePO;
	public ScatterGraphPage(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,10000);
		builder =  new Actions(driver);
		tablePO  = new FuseTablePO(driver,"//table[contains( @summary,'Scatter Analysis')]");
	}
	
	public int getRowIDForScatter(String name){
		ArrayList<String> scatterList = getAllScatters();
		for(String eachScatter : scatterList)
		{
			if(eachScatter.contains(name))
				return scatterList.indexOf(eachScatter);
		}
	
		return -1;
	}
	public ScatterGraphPage clickOnDeleteScatterButton(String name){
		tablePO.clickOnDeleteButton(name);
		return this;
	}

	public ScatterGraphPage clickOnActionButton(String name){

		tablePO.clickOnActionButton(name);
		return this;
	}
	public ArrayList<String> getAllScatters(){
		return tablePO.getAllRows(); 
	}
	public ScatterGraphPage clickYesButton(){
		driver.findElement(yesButton).click();
		return this;
	}
	public ScatterGraphPage clickNoButton(){
		driver.findElement(noButton).click();
		return this;
	}

	public boolean doesScatterExists(String name){
		By xpathForScatterRowLink = By.xpath( xpathForScatterRow + "/td[contains(.,'"+name+"')]");
		return !BasicUtil.waitForNonPresenceOfElement(driver, xpathForScatterRowLink);
	}
	public ScatterGraphPage clickRefreshButton(){
		WebElement element = driver.findElement(refreshButton);
		element.click();
		new WebDriverWait(driver, 100).until(new ClientSynchedWithServer());
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[contains(@id,'Tracker')]")));
		return this;
	}
	
	public ScatterGraphPage clickOnCopyMenuLink(String name){
		tablePO.clickOnActionsMenuText(name, "Copy");
		return this;
	}
	public ScatterGraphPage clickOnEditMenuLink(String name){
		
		tablePO.clickOnEditButton(name);
		return this;
	}
	public ScatterGraphPage clickOnDiaMenuLink(String name){
		tablePO.clickOnActionsMenuText(name, "Diagnose");
		return this;
	}
	public ScatterGraphPage typeNewCopyAVName(String newAVName){
		driver.findElement(copySGInputField).sendKeys(newAVName);
		return this;
	}
	public ScatterGraphPage clickOKButton(){
		driver.findElement(OKButton).click();
		return this;
	}
	public ScatterGraphPage clickCloseButton(){
		driver.findElement(closeButton).click();
		return this;
	}
	public ScatterGraphPage editName(String newName){
		driver.findElement(editAVInputFieldName).clear();
		driver.findElement(editAVInputFieldName).sendKeys(newName);
		return this;
	}
	public ScatterGraphPage clickEnabledCheckBox(){
		driver.findElement(editAVCheckBoxEnabled).click();
		return this;
	}
	public ScatterGraphPage clickSaveAndClose(){
		driver.findElement(saveCloseButton).click();
		return this;
	}

	public ScatterGraphPage clickOnCreateButton() {
		tablePO.clickOnCreateButton();
		return this;
		
	}
	public ScatterGraphPage populateNameField(String SGName) {
		driver.findElement(nameInputField).sendKeys(SGName);;
		return this;
		
	}
	public ScatterGraphPage populateDescField(String desc) {
		driver.findElement(descInputField).sendKeys(desc);;
		return this;
		
	}
	public ScatterGraphPage clickOnEnabled() {
		driver.findElement(enabledCheckBox).click();
		return this;
		
	}
	public ScatterGraphPage selectAV(String avName) {
		driver.findElement(analViewSel).click();
		BasicUtil.wait(2);
		List<WebElement> elements = driver.findElements(By.xpath(".//td[contains(.,'"+avName+"') and @class='x10t']"));
		//.//td[contains(.,'Test AV 1') and @class='x10t']
		for(WebElement each : elements)
			{
				System.out.println(each.getText() +" "+each.getTagName());
				if(each.isDisplayed())
					each.click();
			}
		
		driver.findElement(By.xpath(".//div[contains(@title,'Analysis View')]/h1/ancestor::tbody//button[contains(@title,'OK') and text()='OK']")).click();
		return this;
		
	}
	public ScatterGraphPage selectXDimMem(String xAxisDimMem) {
		BasicUtil.wait(3);
		Select xsel = new Select(driver.findElement(xaxisDimSel));
		xsel.selectByVisibleText(xAxisDimMem);
		return this;
	}
	public ScatterGraphPage selectYDimMem(String yAxisDimMem) {
		BasicUtil.wait(3);
		Select xsel = new Select(driver.findElement(yaxisDimSel));
		xsel.selectByVisibleText(yAxisDimMem);
		return this;
	}
	public ScatterGraphPage populateYDimLabel(String yAxisLabel) {
		driver.findElement(yaxisDimSel).sendKeys(yAxisLabel);
		return this;
	}
	public ScatterGraphPage populateXDimLabel(String xAxisLabel) {
		driver.findElement(xaxisDimSel).sendKeys(xAxisLabel);
		return this;
	}
	public ScatterGraphPage createSGSubPage(String AVName, String SGName, String desc, String XDimMemName, String XDimLabel,String YDimMemName, String YDimLabel) {
		populateNameField(SGName);
		populateDescField(desc);
		clickOnEnabled();
		selectAV(AVName);
		selectXDimMem(XDimMemName);
		selectYDimMem(YDimMemName);
		populateXDimLabel(XDimLabel);
		populateYDimLabel(YDimLabel);
		clickSaveAndClose();
		return this;
		
	}
	public ScatterGraphPage applyFilter(){
		//driver.findElement(arg0)
		return this;
	}
}
