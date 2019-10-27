package poms2;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import pagefragments.PovSelectionPageFragment;
import utils.BasicUtil;

public class RuleBalancingPage extends PovSelectionPageFragment{
	By selectMV = By.xpath(".//label[contains(.,'Model View')]/../..//select");
	By refreshButton = By.xpath(".//span[contains(.,'Refresh')]/..");
	By columnHeadingTable = By.xpath(".//div[text()='Rules']/../../../../../table[contains(@summary,'headers')]");
	By actionButtonMenu = By.xpath(".//a[text()='Actions']");
	By viewButtonMenu = By.xpath(".//a[text()='View']");
	By exportButtonMenu = By.xpath(".//img[@title='Export the data to an Excel file.']");
	
	public RuleBalancingPage(WebDriver driver){
		super(driver);
	}
	public RuleBalancingPage selectMVDefault(){	
		new Select(driver.findElement(selectMV)).selectByVisibleText("Default Model View");;
		return this;
	}

	public RuleBalancingPage clickRefresh(){
		driver.findElement(refreshButton).click();
		BasicUtil.wait(5);
		return this;
	}
/*	public RuleBalancingPage printRowHeadings(){
		//BasicUtil.highlightElement(driver, driver.findElement(columnHeadingTable));
		WebTable.getWebTable(driver, xpathColHeading);
		return this;
	}*/
	public RuleBalancingPage clickActionMenu(){
		driver.findElement(actionButtonMenu).click();
		return this;
	}
	public RuleBalancingPage clickViewMenu(){
		driver.findElement(viewButtonMenu).click();
		return this;
	}
	public RuleBalancingPage exportData(){
		driver.findElement(exportButtonMenu).click();
		return this;
	}
		
}
