package poms;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pagefragments.UpdateDimensionPageFragment;
import utils.BasicUtil;

public class ConsoleApplicationPage {
	WebDriver driver;
	String appName;
	WebDriverWait wait;
	By applicationTableRow = By.xpath(".//table[contains(@summary,'List of deployed applications')]//tr[1]");
	By newActionButton = By.xpath(".//button[@title='New']");
	By importTemplateMenuItem = By.xpath(".//tr[@title='Import Template']");
	By passWordTextBox = By.name("Password");
	By loginButton  = By.id("LogBtn");
	By appNameInputField = By.xpath(".//label[contains(.,'Application Name')]/../../td/input");
	By nextButtonImportTemplate = By.xpath(".//button[@title='Next' and text()='Next']");
	By selectImportTemplate = By.xpath("//div//tr/td/select");
	By localTemplateCheckBox = By.xpath(".//label[contains(.,'Client')]/../span");
	By templateUploadInput = By.xpath(".//input[@type='file']");
	By finishButton = By.xpath("//button[@title='Finish']");
	By dialogOKButton = By.xpath(".//button[text()='OK' and @title='OK']");
	By selectFileLink = By.xpath(".//a[text()='Select File']");
	By selectFilePopUp = By.xpath(".//h1[text()='File Selection']");
	By selectClientFileRadio = By.xpath(".//label[text()='Client']");
	
	By deleteButton = By.xpath(".//span[text()='Delete']");
	By validateButton = By.xpath(".//span[text()='Validate and Enable']");
	By reRegisterButton = By.xpath(".//span[text()='Re-Register']");
	By metadataValidationButton = By.xpath(".//span[text()='Metadata Validation']");
	By updateDimensionButton = By.xpath(".//tr[text()='Update Dimensions']");
	By exportButton = By.xpath(".//span[text()='Export Template']");
	public ConsoleApplicationPage(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,10000);
	}
	public ConsoleApplicationPage closeDialog(){
		driver.findElement(By.xpath(".//div/../../td[3]/div/a")).click();
		return this;
	}
	public ConsoleApplicationPage clickNewActionButton(){
		driver.findElement(newActionButton).click();
		return this;
	}
	public ConsoleApplicationPage clickOnSelectFileLink(){
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(selectFileLink));
		driver.findElement(selectFileLink).click();
		return this;
	}
	
	public ConsoleApplicationPage selectLocal(){
		
		//temperarory work around for a bug, remove it later
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(selectFilePopUp));
		driver.findElement(selectClientFileRadio).click();
		return this;
	}
	public ConsoleApplicationPage clickImportTemplate(){
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(importTemplateMenuItem));
		driver.findElement(importTemplateMenuItem).click();
		return this;
	}
	public ConsoleApplicationPage importAppFromTemplate(String appName){
		driver.findElement(appNameInputField).click();
		driver.findElement(appNameInputField).clear();
		driver.findElement(appNameInputField).sendKeys(appName);
		driver.findElement(finishButton).click();
		
		return this;

		

		//if(driver.findElement(By.xpath("//td[text()='"+AppName+"']")).isDisplayed())

	}
	public ConsoleApplicationPage selectTemplate(File template){
		Select select=new Select(driver.findElement(selectImportTemplate));
		select.selectByVisibleText(template.getName());
		driver.findElement(nextButtonImportTemplate).click();
		return this;
	}
	public ConsoleApplicationPage uploadTemplate(File template) throws InterruptedException{
		driver.findElement(localTemplateCheckBox).click();
		Thread.sleep(1000);
		driver.findElement(templateUploadInput).sendKeys(template.getAbsolutePath());
		return this;

	}
	public ConsoleApplicationPage uploadFile(File template) throws InterruptedException{
		driver.findElement(localTemplateCheckBox).click();
		Thread.sleep(1000);
		driver.findElement(templateUploadInput).sendKeys(template.getAbsolutePath());
		return this;

	}
	public ConsoleApplicationPage clickNextButtonImportDialog() throws InterruptedException{
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//fieldset/legend[text()='Import File Details']")));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[text()='Process Indicator']")));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='AFBlockingGlassPane']")));
		wait.until(ExpectedConditions.elementToBeClickable(nextButtonImportTemplate));
		WebElement nextButton = driver.findElement(nextButtonImportTemplate);
		nextButton.click();
		return this;
	}
	public ConsoleApplicationPage clickOkOnDialogs(){
		driver.findElement(dialogOKButton).click();
		return this;
	}
	public FuseNavigatorPage returnToNav() {
		return new FuseNavigatorPage(driver);
	}
	
	public String getApplication(){
		WebElement appRow;
		try{
		appRow = driver.findElement(this.applicationTableRow);
		}catch(NoSuchElementException e){
			BasicUtil.log(this.getClass().getName(), "No Apps found or app table is not visible.");
			return null;
		}
		String appName = (appRow.findElement(By.xpath(".//td[1]/a"))).getText();
		return appName;
	}
	public ConsoleApplicationPage clickApplicationActionButton(){
		WebElement appRow;
		try{
		appRow = driver.findElement(this.applicationTableRow);
		}catch(NoSuchElementException e){
			BasicUtil.log(this.getClass().getName(), "No Apps found or app table is not visible.");
			return this;
		}
		(appRow.findElement(By.xpath(".//td[5]/a"))).click();
		return this;
	}
	public ConsoleApplicationPage selectApplication(){
		WebElement appRow;
		try{
		appRow = driver.findElement(this.applicationTableRow);
		}catch(NoSuchElementException e){
			BasicUtil.log(this.getClass().getName(), "No Apps found or app table is not visible.");
			return this;
		}
		appRow.click();
		return this;
	}
	public boolean isEnabledApplication(){
		WebElement appRow;
		try{
		appRow = driver.findElement(this.applicationTableRow);
		}catch(NoSuchElementException e){
			BasicUtil.log(this.getClass().getName(), "No Apps found or app table is not visible.");
			return false;
		}
		List<WebElement> list = appRow.findElements(By.xpath(".//td[4]/img"));
		return list.size()>0?true:false;
	}
	public ConsoleApplicationPage clickApplicationSummary(){
		WebElement appRow;
		try{
		appRow = driver.findElement(this.applicationTableRow);
		}catch(NoSuchElementException e){
			BasicUtil.log(this.getClass().getName(), "No Apps found or app table is not visible.");
			return this;
		}
		(appRow.findElement(By.xpath(".//td[1]/a"))).click();
		return this;
	}
	public UpdateDimensionPageFragment clickUploadDimensionButton(){
		driver.findElement(this.updateDimensionButton).click();
		return new UpdateDimensionPageFragment(driver);
	}

	public ConsoleApplicationPage clickDeleteApplication(){
		driver.findElement(this.deleteButton).click();
		return this;
	}
	
}
