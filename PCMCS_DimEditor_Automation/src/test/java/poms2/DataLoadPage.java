package poms2;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

public class DataLoadPage {
	public WebDriver driver;
	public WebDriverWait wait;
	By addRowButton = By.xpath(".//img[@alt='Add Row']/..");
	By inputFileUpload = By.xpath(".//input[@type='file']");
	By OKButton = By.xpath(".//button[contains(.,'OK')]");
	By loadButton = By.xpath(".//button[contains(.,'Load')]");
	By selectFileLink = By.xpath(".//a[text()='Select File']");
	By clientRadioButton = By.xpath(".//label[contains(.,'Client')]/../span");
	By clearDatabaseCheckbox = By.xpath(".//label[text()='Clear Database Before Load']/../..//input");
	By addToExistingValuesRadio = By.xpath(".//label[text()='Add To Existing Values']/../..//input");
	By overwriteValuesRadio = By.xpath(".//label[text()='Overwrite Values']/../..//input");
	public DataLoadPage(WebDriver driver){
		this.driver = driver;
		wait = new WebDriverWait(driver,100);
	}
	public DataLoadPage clickAddRow(){
		BasicUtil.wait(3);
		driver.findElement(addRowButton).click();
		return this;
	}
	public DataLoadPage clickOverwriteDataRadio(){
		driver.findElement(this.overwriteValuesRadio).click();
		return this;
	}
	public DataLoadPage clickAddToExistingRadio(){
		driver.findElement(this.addToExistingValuesRadio).click();
		return this;
	}
	public DataLoadPage clickClearDatabaseCheckbox(){
		driver.findElement(this.clearDatabaseCheckbox).click();
		return this;
	}
	public DataLoadPage clickSelectFile(){
		driver.findElement(selectFileLink).click();
		return this;
	}
	public DataLoadPage clickClientRadioButton(){
		driver.findElement(clientRadioButton).click();
		return this;
	}
	public DataLoadPage attachDataloadFile(File file) throws InterruptedException{
		Thread.sleep(2000);
		driver.findElement(inputFileUpload).sendKeys(file.getAbsolutePath());
		return this;
	}
	public DataLoadPage clickOKButton(){
		driver.findElement(OKButton).click();
		return this;
	}
	public DataLoadPage clickLoadButton(){
		driver.findElement(loadButton).click();
		return this;
	}
}
