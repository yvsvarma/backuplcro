package poms2;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import pagefragments.PovSelectionPageFragment;
import utils.BasicUtil;

public class ManageCalcPage extends PovSelectionPageFragment{

	By clearCalculatedCheckBox = By.xpath(".//label[contains(.,'Clear Calculated Data')]/../span/input");
	By captureEssbaseScriptsCheckBox = By.xpath(".//label[contains(.,'Capture Essbase Debug Scripts')]/../span/input");
	By executeCalcCheckBox = By.xpath(".//label[contains(.,'Execute Calculation')]/../span/input");
	By allRulesRadioButton = By.xpath(".//label[contains(.,'All Rules')]/../span/input");
	By runNowButton = By.xpath(".//button[contains(.,'Run Now')]");
	public ManageCalcPage(WebDriver driver){
		super(driver);
	}
	public ManageCalcPage checkAllRules(){
		driver.findElement(allRulesRadioButton).click();
		return this;
	}
	public ManageCalcPage checkEssbaseScriptOption(){
		driver.findElement(captureEssbaseScriptsCheckBox).click();
		return this;
	}
	public ManageCalcPage runCalcJob(){
		
		BasicUtil.highlightElement(driver, 	driver.findElement(runNowButton));
		driver.findElement(runNowButton).click();
		return this;
	}
	

	
	
}
