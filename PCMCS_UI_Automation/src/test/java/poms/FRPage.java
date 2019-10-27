package poms;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BasicUtil;

public class FRPage {
	private By createFRButton = By.xpath(".//button[text()='Create']");
	public By createFRPageHeader = By.xpath(".//h1[contains(.,'Create')]");
	private By reportNameInput = By.xpath(".//label[contains(.,'Report Name')]/../..//input");
	private By reportDescInput = By.xpath(".//label[contains(.,'Report Description')]/../..//textarea");
	private By selectQueryLink = By.xpath(".//label[contains(.,'Select A Query')]/../..//a");
	public By selectQueryWizardHeader = By.xpath(".//h1[contains(.,'Select Query Wizard')]");
	//private By querySpan = By.xpath(".//span[contains(.,'%s')]");
	private By filterQuery = By.xpath(".//label[text()='Search']/../input");
	private By selectQueryOKButton = By.xpath(".//h1[contains(.,'Select')]/../../../td//button[text()='OK']");
	private By createFROKButton = By.xpath(".//h1[contains(.,'Create')]/../../../td//button[text()='OK']");
	private By runButton = By.xpath(".//span[text()='Run']");
	private By htmlViewRun = By.xpath(".//td[text()='HTML View']");
	private By pdfViewRun = By.xpath(".//td[text()='PDF View']");
	By OKButton = By.xpath(".//button[contains(.,'OK')]");
	public String xpathForFRRow = ".//div[contains(@title,'List of Financial Reports.')]/div[2]//tbody/tr";
	public WebDriver driver;
	public  Actions builder ;
	public WebDriverWait wait;
	private FuseTablePO tablePO;
	public FRPage(WebDriver driver){
		this.driver = driver;
		builder  = new Actions(driver);
		wait = new WebDriverWait(driver,30);
		tablePO = new FuseTablePO(driver,".//div[contains(@title,'List of Financial Reports')]//table[boolean(@_rowcount)]");
	}
	public ArrayList<String> getAllFRs(){

		return tablePO.getAllRows();
	}
	public int getRowIDForFR(String name){
		ArrayList<String> FRList = getAllFRs();
		for(String eachFR : FRList)
		{
			if(eachFR.contains(name))
				return FRList.indexOf(eachFR);
		}
	
		return -1;
	}
	public FRPage selectFRByName(String name){
		tablePO.selectRow(name);
		return this;
	}
	public FRPage clickCreateButton(){
		tablePO.clickOnCreateButton();
		return this;
	}
	public FRPage inputFRName(String name){
		driver.findElement(reportNameInput).sendKeys(name);
		return this;
	}
	public FRPage inputDesc(String desc){
		driver.findElement(reportDescInput).sendKeys(desc);
		return this;
	}
	public FRPage clickSelectQueryLink(){
		driver.findElement(selectQueryLink).click();
		return this;
	}
	public FRPage selectQuery(String name){
                BasicUtil.highlightElement(driver, driver.findElement(By.xpath("//td[text()='"+name+"']")));
		driver.findElement(By.xpath("//td[text()='"+name+"']")).click();
		return this;
	}
	public FRPage clickSelectQueryOKButton(){
		driver.findElement(selectQueryOKButton).click();
		return this;
	}
	public FRPage clickCreateFROKButton(){
		driver.findElement(createFROKButton).click();
		return this;
	}
	public FRPage filterQueryInput(String name){
		driver.findElement(filterQuery).clear();
		driver.findElement(filterQuery).sendKeys(name);
		driver.findElement(By.xpath(".//img[@title='Search']/..")).click();
		//wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//span[contains(.,'"+name+"')]")));
		return this;
	}
	public FRPage clickActionButton(){
		tablePO.clickOnActionButton();
		return this;
	}
	public FRPage clickRunHTML(String rowName){
		tablePO.selectRow(rowName);
		tablePO.clickOnActionsMenuText(rowName, "Run as HTML");
		return this;
	}
	public FRPage clickRunPDF(String rowName){
		tablePO.selectRow(rowName);
		tablePO.clickOnActionsMenuText(rowName, "Run as PDF");
		return this;
	}
}
