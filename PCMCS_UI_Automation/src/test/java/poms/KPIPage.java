package poms;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import pagefragments.CreateEditKPIPageFragment;
import utils.BasicUtil;

public class KPIPage {
	private WebDriver driver;
	Actions builder;
	WebDriverWait wait;
	//By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	public String xpathForKPIRow = ".//table[contains(@summary,'List of KPI')]/tbody/tr";
	By KPITableItems = By.xpath(xpathForKPIRow);
	By deleteConfirmDlg = By.xpath(".//td[contains(.,'You are about to delete')]");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By createButton = By.xpath(".//img[@title='Create Key Performance Indicator']");
	By OKButton = By.xpath(".//button[text()='OK']");
	By noButton = By.xpath(".//button[text()='No']");
	By refreshButton = By.xpath(".//img[@alt='Refresh']");
	By editMenuLink = By.xpath(".//img[@title='Edit Analysis View']");
	By closeButton = By.xpath(".//button[text()='Close']");
	By copyMenuLink = By.xpath(".//tr[@title='Copy']");
	By diagMenuLink = By.xpath(".//tr[contains(@title,'Diagnose')]//td[contains(.,'Diagnose')]");
	By copyKPIInputField = By.xpath(".//label[text()='New Key Performance Indicator Name']/../../td[2]/input");
	By editKPIInputFieldName = By.xpath(".//label[text()='Name']/../../td[2]/input");
	By editKPICheckBoxEnabled = By.xpath(".//label[text()='Enabled']/../../td[2]//input");
	By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	By deleteKPIButton = By.xpath(".//img[@title='Delete Key Performance Indicator']");
	Wait<WebDriver> fluentWait;
	private FuseTablePO tablePO;
	public KPIPage(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,100);
		builder =  new Actions(driver);
		tablePO = new FuseTablePO(driver,"//table[contains( @summary,'available for this application.')]");
		//wait.until(ExpectedConditions.and(ExpectedConditions.attributeContains(locator, attribute, value)).alertIsPresent() );
	}
	
	public int getRowIDForKPI(String name){
		ArrayList<String> KPIList = getAllKPIs();
		for(String eachKPI : KPIList)
		{
			if(eachKPI.contains(name))
				return KPIList.indexOf(eachKPI);
		}
	
		return -1;
	}
	public KPIPage clickOnDeleteKPIButton(String name){
		
		tablePO.clickOnDeleteButton(name);
		return this;
	}

	public KPIPage clickOnActionButton(String name){
		tablePO.clickOnActionButton(name);
		return this;
	}
	public ArrayList<String> getAllKPIs(){
		

		return tablePO.getAllRows();
	}
	public KPIPage clickYesButton(){
		driver.findElement(yesButton).click();
		return this;
	}
	public KPIPage clickNoButton(){
		driver.findElement(noButton).click();
		return this;
	}
	public KPIPage clickRefreshButton(){
		WebElement element = driver.findElement(refreshButton);
		element.click();
		BasicUtil.waitADF(driver, 100);
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[contains(@id,'Tracker')]")));
		return this;
	}
	
	public KPIPage clickOnCopyMenuLink(String kpiName){
		tablePO.clickOnActionsMenuText(kpiName, "Copy");
		System.out.println("Clicked on the menu button.");
		return this;
	}
	public KPIPage clickOnEditMenuLink(String kpiName){
		
		tablePO.clickOnEditButton(kpiName);
		return this;
		
	}
	public KPIPage clickOnDiaMenuLink(String kpiName){
		tablePO.clickOnActionsMenuText(kpiName, "Diagnose");
		return this;
	}
	public KPIPage typeNewCopyAVName(String newAVName){
                BasicUtil.highlightElement(driver, driver.findElement(copyKPIInputField));
		driver.findElement(copyKPIInputField).sendKeys(newAVName);
		return this;
	}
	public KPIPage clickOKButton(){
		driver.findElement(OKButton).click();
		return this;
	}
	public KPIPage clickCloseButton(){
		driver.findElement(closeButton).click();
		return this;
	}
	public KPIPage editName(String newName){
		driver.findElement(editKPIInputFieldName).clear();
		driver.findElement(editKPIInputFieldName).sendKeys(newName);
		return this;
	}
	public KPIPage clickEnabledCheckBox(){
		driver.findElement(editKPICheckBoxEnabled).click();
		return this;
	}
	public KPIPage clickSaveAndClose(){
		driver.findElement(saveCloseButton).click();
		return this;
	}


	public CreateEditKPIPageFragment clickOnCreateButton() {
		tablePO.clickOnCreateButton();
		return new CreateEditKPIPageFragment(driver);
		
	}

	public boolean doesKPIExists(String kpiName) {
		// TODO Auto-generated method stub
		List<String> ls = getAllKPIs();
		for(String name : ls){
			if(name.contains(kpiName))
				return true;
		}
		return false;
	}
	

}
