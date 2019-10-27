package poms;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pagefragments.CreateEditDBPageFragment;
import utils.BasicUtil;
import utils.ClientSynchedWithServer;
/**
 * @author      Mohnish Banchhor <mohnish.banchhor@oracle.com>
 * @version     1.0
 * @since       1.0          
 */
public class DashboardPage {
	private WebDriver driver;
	private WebDriverWait wait;
	Actions builder;
	private FuseTablePO tablePO;
	private String xpathForDBRow = ".//table[contains(@summary,'Dashboards')]/tbody/tr";
	By DBTableItems = By.xpath(xpathForDBRow);
	By deleteConfirmDlg = By.xpath(".//td[contains(.,'You are about to delete')]");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By OKButton = By.xpath(".//button[text()='OK']");
	By noButton = By.xpath(".//button[text()='No']");
	By createButton = By.xpath(".//button[text()='Create']");
	By editMenuLink = By.xpath(".//tr[@title='Edit']/td");
	By closeButton = By.xpath(".//button[text()='Close']");
	By copyMenuLink = By.xpath(".//tr[@title='Copy Analysis View']");
	By diagMenuLink = By.xpath(".//tr[@title='Diagnose']");
	By copyDBInputField = By.xpath(".//label[text()='New Dashboard Name']/../../td[2]/input");
	By editAVInputFieldName = By.xpath(".//label[text()='Name']/../../td[2]/input");
	By editAVCheckBoxEnabled = By.xpath(".//label[text()='Enabled']/../../td[2]//input");
	By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	By refreshButton = By.xpath(".//button[text()='Refresh']");
	WebElement tableElement;
	public DashboardPage(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,10000);
		builder =  new Actions(driver);
		tablePO = new FuseTablePO(driver,"//table[contains( @summary,'List of Dashboards available')]");
	}
	public DashboardPage clickRefreshButton(){
		WebElement element = driver.findElement(refreshButton);
		element.click();
		new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[contains(@id,'Tracker')]")));
		return this;
	}
	public int getRowIDForDB(String name){
		ArrayList<String> dbList;
		dbList = tablePO.getAllRows();
		if(dbList.contains(name))
			return dbList.indexOf(name);
		return -1;
	}
	public DashboardPage clickOnDeleteDBButton(String name){
		tablePO.clickOnDeleteButton(name);
		return this;
	}
	public boolean doesDBExists(String name){
		new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
		By xpathFordbRowLink = By.xpath( xpathForDBRow + "/td[contains(.,'"+name+"')]");
		return !BasicUtil.waitForNonPresenceOfElement(driver, xpathFordbRowLink);
	}
	public DashboardPage clickOnActionButton(String name){
		tablePO.clickOnActionButton();
		return this;
	}
	public ArrayList<String> getAllDBs(){

		return tablePO.getAllRows();
	}
	public DashboardPage clickYesButton(){
		driver.findElement(yesButton).click();
		return this;
	}
	public DashboardPage clickNoButton(){
		driver.findElement(noButton).click();
		return this;
	}
	public CreateEditDBPageFragment clickCreateButton(){
		tablePO.clickOnCreateButton();
		return new CreateEditDBPageFragment(driver);
	}
	
	public DashboardPage clickOnCopyMenuLink(){
		wait.until(ExpectedConditions.elementToBeClickable(copyMenuLink));
		 driver.findElement(copyMenuLink).click();
		return this;
	}
	public DashboardPage clickOnEditMenuLink(){
		driver.findElement(editMenuLink).click();
		return this;
	}
	public DashboardPage clickOnDiaMenuLink(){
		driver.findElement(diagMenuLink).click();
		return this;
	}
	public DashboardPage typeNewCopyAVName(String newAVName){
		driver.findElement(copyDBInputField).sendKeys(newAVName);
		return this;
	}
	public DashboardPage clickOKButton(){
		driver.findElement(OKButton).click();
		return this;
	}
	public DashboardPage clickCloseButton(){
		driver.findElement(closeButton).click();
		return this;
	}

}
