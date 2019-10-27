package poms;

import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pagefragments.CreateEditAVPageFragment;
import utils.BasicUtil;
import utils.ClientSynchedWithServer;
/**
 * @author      Mohnish Banchhor <mohnish.banchhor@oracle.com>
 * @version     1.0
 * @since       1.0          
 */
public class AnalysisViewPage {
	private WebDriver driver;
	private WebDriverWait wait;
	Actions builder;
	private String xpathForAVRow = ".//table[contains(@summary,'available for selected application')]/tbody/tr";
	By analysisViewTableItems = By.xpath(xpathForAVRow);
	By deleteConfirmDlg = By.xpath(".//td[contains(.,'You are about to delete')]");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By OKButton = By.xpath(".//button[text()='OK']");
	By noButton = By.xpath(".//button[text()='No']");
	By createButton = By.xpath(".//button[text()='Create']");
	By editMenuLink = By.xpath(".//tr[@title='Edit']/td");
	By closeButton = By.xpath(".//button[text()='Close']");
	By copyMenuLink = By.xpath(".//tr[@title='Copy Analysis View']");
	By diagMenuLink = By.xpath(".//tr[@title='Diagnose']");
	By copyAVInputField = By.xpath(".//label[text()='New Analysis View Name']/../../td[2]/input");
	By editAVInputFieldName = By.xpath(".//label[text()='Name']/../../td[2]/input");
	By editAVCheckBoxEnabled = By.xpath(".//label[text()='Enabled']/../../td[2]//input");
	By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	By refreshButton = By.xpath(".//button[text()='Refresh']");
	FuseTablePO tablePO;
	public AnalysisViewPage(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,10000);
		builder =  new Actions(driver);
		tablePO = new FuseTablePO(driver,"//table[contains( @summary,'available for selected application.')]");
	}
	public AnalysisViewPage clickRefreshButton(){
		WebElement element = driver.findElement(refreshButton);
		element.click();
		new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[contains(@id,'Tracker')]")));
		return this;
	}
	public int getRowIDForAV(String name){
		return tablePO.getRowIDForRow(name);
	}
	public AnalysisViewPage clickOnDeleteAVButton(String name){
		tablePO.clickOnDeleteButton(name);
		return this;
	}
	public boolean doesAVExists(String name){
		new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
		By xpathForAVRowLink = By.xpath( this.xpathForAVRow + "/td[contains(.,'"+name+"')]");
		return !BasicUtil.waitForNonPresenceOfElement(driver, xpathForAVRowLink);
	}
	public AnalysisViewPage clickOnActionButton(String name){
		String xpathForAVRowActionLink =  xpathForAVRow + "/td[contains(.,'"+name+"')]/../td[5]//a";
		builder = new Actions(driver);
		driver.findElement(By.xpath(xpathForAVRowActionLink)).click();
		//builder.click(driver.findElement(By.xpath(xpathForAVRowActionLink))).perform();
		return this;
	}
	public ArrayList<String> getAllAVs(){
		return tablePO.getAllRows();
	}
	public AnalysisViewPage clickYesButton(){
		driver.findElement(yesButton).click();
		return this;
	}
	public AnalysisViewPage clickNoButton(){
		driver.findElement(noButton).click();
		return this;
	}
	public CreateEditAVPageFragment clickCreateButton(){
		tablePO.clickOnCreateButton();
		return new CreateEditAVPageFragment(driver);
	}
	
	public AnalysisViewPage clickOnCopyMenuLink(String name){
		tablePO.clickOnActionsMenuText(name, "Copy");
		return this;
	}
	public AnalysisViewPage clickOnEditMenuLink(String name){
		tablePO.clickOnEditButton(name);
		return this;
	}
	public AnalysisViewPage clickOnDiaMenuLink(String name){
		tablePO.clickOnActionsMenuText(name, "Diagnose");
		return this;
	}
	public AnalysisViewPage typeNewCopyAVName(String newAVName){
		driver.findElement(copyAVInputField).sendKeys(newAVName);
		return this;
	}
	public AnalysisViewPage clickOKButton(){
		driver.findElement(OKButton).click();
		return this;
	}
	public AnalysisViewPage clickCloseButton(){
		driver.findElement(closeButton).click();
		return this;
	}
	public AnalysisViewPage editName(String newName){
		driver.findElement(editAVInputFieldName).clear();
		driver.findElement(editAVInputFieldName).sendKeys(newName);
		return this;
	}
	public AnalysisViewPage clickEnabledCheckBox(){
		driver.findElement(editAVCheckBoxEnabled).click();
		return this;
	}
	public AnalysisViewPage saveAndCloseAVEdit(){
		driver.findElement(saveCloseButton).click();
		return this;
	}
}
