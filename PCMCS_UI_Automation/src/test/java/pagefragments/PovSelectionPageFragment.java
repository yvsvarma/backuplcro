package pagefragments;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

public class PovSelectionPageFragment {
	public WebDriver driver;
	public WebDriverWait wait;
	public By yearSelect = By.xpath(".//label[contains(.,'Year')]/../../td/select");
	public By periodSelect = By.xpath(".//label[contains(.,'Period')]/../../td/select");
	public By scenarioSelect = By.xpath(".//label[contains(.,'Scenario')]/../../td/select");
	public By disabledPOVLink = By.xpath(".//img[contains(@src,'HPM_go.png')]/..");
	public By enabledPOVLink = By.xpath(".//img[contains(@src,'HPM_go1.png')]");
	public PovSelectionPageFragment(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,100);
	}
	public String getYearSelected() {
		return driver.findElement(yearSelect).getAttribute("title");
	}
	public String getPeriodSelected() {
		 return driver.findElement(periodSelect).getAttribute("title");
	}
	public String getScenarioSelected() {
		return driver.findElement(scenarioSelect).getAttribute("title");
	}
	public void setYear(String year) {
		driver.findElement(yearSelect).click();
		new Select(driver.findElement(yearSelect)).selectByVisibleText(year);
	}
	public void setPeriod(String period) {
		driver.findElement(periodSelect).click();
		new Select(driver.findElement(periodSelect)).selectByVisibleText(period);
	}
	public void setScenario(String scenario) {
		driver.findElement(scenarioSelect).click();
		new Select(driver.findElement(scenarioSelect)).selectByVisibleText(scenario);
	}
	public PovSelectionPageFragment changePOV(String year,String period,String scenario){
;
		BasicUtil.log("Already Selected POV is Year: "+getYearSelected()+" Period: "+getPeriodSelected() + " Scenario:"+getScenarioSelected());
		if(!getScenarioSelected().equals(scenario) || !getYearSelected().equals(year) || !getPeriodSelected().equals(period))
		{
			BasicUtil.log("Chaning POV members.");
			setYear(year);
			BasicUtil.waitADF(driver,100);
			setPeriod(period);
			BasicUtil.waitADF(driver,100);
			setScenario(scenario);
			BasicUtil.waitADF(driver,100);
			BasicUtil.log("Wait for enablement of the pov go button.");
			wait.until(ExpectedConditions.presenceOfElementLocated(enabledPOVLink));
			//BasicUtil.highlightElement(driver, driver.findElement(enabledPOVLink));
			BasicUtil.log("Clicking on POV go/change button.");
			WebElement element = driver.findElement(enabledPOVLink);
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", element);
			//driver.findElement(enabledPOVLink).sendKeys(Keys.ENTER);;
			BasicUtil.log("Wait for change of the pov.");
			wait.until(ExpectedConditions.presenceOfElementLocated(disabledPOVLink));
		}
		return this;
	}
	public void SelectCombobox(String ComboName, String value){
		List<WebElement> webelements = driver.findElements(By.xpath("//label[text()='"+ComboName+"']/../following-sibling::td//select"));
		for(WebElement w: webelements){
			if(w.isDisplayed()){
				w.sendKeys(value);
				break;
			}
		}

	}
	
}
