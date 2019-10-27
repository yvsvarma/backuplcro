package poms2;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

public class ManageQueriesPage {
	private WebDriver driver;
	private WebDriverWait wait;
	By addQueryButton = By.xpath(".//img[contains(@title,'Create new Smart View query')]");
	By applicationSelect = By.xpath(".//td/label[text()='Application']/../../td/select");
	By queryNameInput = By.xpath(".//td/label[text()='Query Name']/../../td/input");
	By nextButton = By.xpath(".//button[text()='Next']");
	By finishButton = By.xpath(".//button[text()='Finish']");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By okButton =By.xpath(".//button[contains(.,'OK')]");
	By filterQueryAppnameInput = By.xpath(".//th[1]//input");
	By filterQueryByQueryNameInput = By.xpath(".//th[2]//input");
	By deleteQueryButton = By.xpath(".//img[contains(@title,'Delete Smart View query')]");
	By saveQueryButton = By.xpath(".//img[contains(@title,'Save Smart View query')]");
	By descTextArea = By.xpath(".//textarea");
	By copyButton = By.xpath(".//img[contains(@title,'Create a copy of selected Smart View query')]");
	By newQueryNameInput = By.xpath(".//label[text()='New Smart View Query Name']/../../td[2]/input");
	//
	public ManageQueriesPage(WebDriver driver){
		this.driver = driver;
		wait = new WebDriverWait(driver,10000);
	}
	
	public void clickAddButton(){
		driver.findElement(addQueryButton).click();
	}
	
	public void populateAddQueryScreenPage1(String appName, String queryName) throws InterruptedException{
		Thread.sleep(2000);
		Select selectApp = new Select(driver.findElement(applicationSelect));
                if(selectApp == null)
                    System.out.println("Nulllllllllllllll");
		selectApp.selectByVisibleText(appName);
		driver.findElement(queryNameInput).sendKeys(queryName);
		
	}
	public void clickNextButton(){
		driver.findElement(nextButton).click();
	}
	public void clickOKButton(){
		driver.findElement(okButton).click();
	}
	public void clickFinishButton(){
		driver.findElement(finishButton).click();
	}
	public void clickYesButton(){
		BasicUtil.wait(2);
		driver.findElement(yesButton).click();
	}
	public void filterQueryByAppName(String appName) throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(filterQueryAppnameInput).clear();
		driver.findElement(filterQueryAppnameInput).sendKeys(appName);
		driver.findElement(filterQueryAppnameInput).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
	}
	public void filterQueryByQueryName(String queryName) throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(filterQueryByQueryNameInput).clear();
		driver.findElement(filterQueryByQueryNameInput).sendKeys(queryName);
		driver.findElement(filterQueryByQueryNameInput).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
	}
	public void selectQueryByName(String queryName){
		WebElement query = driver.findElement(By.xpath(".//td/span[contains(.,'"+queryName+"')]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(query);
		actions.doubleClick(query);
		actions.build().perform();
	}
	
	public void clickDeleteQueryButton() throws InterruptedException{
				Thread.sleep(5000);
				driver.findElement(deleteQueryButton).click();
	}
	public void selectRow(String applicationname, String queryname) throws Exception {
		driver.findElement(By.xpath("//td[text()='" + applicationname + "']/following-sibling::td[text()='" + queryname + "']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//img[contains(@src,'remove_dis')]")));
		Thread.sleep(5000);
	}
	public void changeDescription(String desc) {
		BasicUtil.wait(2);
		driver.findElement(descTextArea).sendKeys(desc);
	}
	public void clickSaveButton() {
		BasicUtil.wait(5);
		driver.findElement(saveQueryButton).click();
	}
	public void clickCopyButton() {
		BasicUtil.wait(2);
		driver.findElement(copyButton).click();
	}
	public void typeNameQueryCopy(String newName) {
		wait.until(ExpectedConditions.elementToBeClickable(newQueryNameInput));
		driver.findElement(newQueryNameInput).sendKeys(newName);
	}
}
