package poms2;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pagefragments.PovSelectionPageFragment;
import utils.BasicUtil;

public class ModelValidationPage {
	public WebDriver driver;
	public WebDriverWait wait;
	public By rulesTabs = By.xpath(".//a[text()='Rule Sets and Rules ']");
	public By modelViewTabs = By.xpath(".//a[text()='Model Views']");
	public By queriesTab = By.xpath(".//a[text()='Queries']");
	public By analyticsTab = By.xpath(".//a[text()='Analytics']");
	public By dgTab = By.xpath(".//a[text()='Data Grants']");
	By runButton = By.xpath(".//span[text()='Run']/..");
	By progressBarClose = By.xpath(".//div[@class='x1ca']//a[@title='Close']");
	public By xpathForRuleSetFilter = By.xpath(".//tr[@title='Rule Sets to Validation']//select");
	public By xpathForRuleFilter  = By.xpath(".//tr[@title='Select the rule status to filter']//select");
	public ModelValidationPage(WebDriver driver){
		this.driver = driver;
		wait = new WebDriverWait(driver,100);
	}
	public ModelValidationPage clickTab(By xpath){
		driver.findElement(xpath).click();
                BasicUtil.waitADF(driver, 1000);
		//driver.findElement(addRowButton).click();
		return this;
	}
	public ModelValidationPage selectDropDown(By xpath, String option){
		Select select = new Select(driver.findElement(xpath));
		select.selectByVisibleText(option);
		return this;
	}
	public ModelValidationPage clickRunButton(){
		driver.findElement(runButton).click();
		return this;
	}
	public PovSelectionPageFragment getPovbar() {
		return new PovSelectionPageFragment(driver);
	}
	public ModelValidationPage closeProgressBar(){
		driver.findElement(progressBarClose).click();
		return this;
	}
}
