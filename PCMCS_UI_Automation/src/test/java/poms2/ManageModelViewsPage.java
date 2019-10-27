package poms2;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pagefragments.MemberSelectorPageFragment;
import utils.BasicUtil;

public class ManageModelViewsPage extends MemberSelectorPageFragment{
	private WebDriver driver;
	private WebDriverWait wait;
	By descTextArea = By.xpath(".//textarea");
	By addQueryButton = By.xpath(".//img[contains(@title,'Create new Smart View query')]");
	By addNewModelViewButton = By.xpath("//img[@title='Creates a new Model View']/..");
	By modelViewNameInput = By.xpath(".//label[text()='Model View Name']/../following-sibling::td/input");
	By nextButton = By.xpath(".//button[text()='Next']");
	By finishButton = By.xpath(".//button[text()='Finish']");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By okButton =By.xpath(".//button[contains(.,'OK')]");
	By saveButton = By.xpath(".//img[@title='Saves the Model View']/..");
	By filterViewNameInput = By.xpath(".//th[1]//input");
	By filterDescInput = By.xpath(".//th[2]//input");
	By deleteViewButton = By.xpath(".//img[contains(@title,'Delete')]");
	By copyButton = By.xpath(".//img[contains(@title,'Copies the')]");
	By newViewNameInput = By.xpath(".//label[text()='Name of the Model View']/../../td[2]/input");

	public ManageModelViewsPage(WebDriver driver){
		super(driver);
		this.driver = driver;
		wait = new WebDriverWait(driver,10000);
	}
	public void typeNameViewCopy(String newName) {
		wait.until(ExpectedConditions.elementToBeClickable(newViewNameInput));
		driver.findElement(newViewNameInput).sendKeys(newName);
	}
	public void clickDeleteViewButton(){
		driver.findElement(deleteViewButton).click();
		BasicUtil.wait(3);
	}
	public void clickNewModelViewButton(){
		driver.findElement(addNewModelViewButton).click();
	}
	public void clickYesButton(){
		BasicUtil.wait(2);
		driver.findElement(yesButton).click();
	}
	public void inputNewViewName(String name){
		driver.findElement(modelViewNameInput).sendKeys(name);
	}
	public void saveView(){
		BasicUtil.wait(3);
		driver.findElement(saveButton).click();
	}

	public void filterByViewName(String viewName) throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(filterViewNameInput).clear();
		driver.findElement(filterViewNameInput).sendKeys(viewName);
		driver.findElement(filterViewNameInput).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
	}
	public void clearFilterByAppName() throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(filterViewNameInput).clear();
		driver.findElement(filterViewNameInput).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
	}
	public void changeDescription(String desc) {
		BasicUtil.wait(2);
		driver.findElement(descTextArea).sendKeys(desc);
	}
	public void clearFilterByViewName() throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(filterViewNameInput).clear();
		driver.findElement(filterViewNameInput).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
	}
	public void selectRow(String viewname) throws Exception {
		driver.findElement(By.xpath("//td[text()='" + viewname + "']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//img[contains(@src,'remove_dis')]")));
		Thread.sleep(2000);
	}
	public void clickCopyButton() throws Exception {
		driver.findElement(copyButton).click();
		Thread.sleep(2000);
	}
	
}
