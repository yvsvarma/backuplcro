package poms2;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pagefragments.PovSelectionPageFragment;

public class SystemReportPage extends PovSelectionPageFragment{
	public WebDriver driver;
	public WebDriverWait wait;
	By reportNameSel = By.xpath(".//label[text()='Report Name']/../../td[2]/select");
	By okButton = By.xpath(".//button[contains(@id,':msgDlg::cancel')]");
	By reportTypeSel = By.xpath(".//label[text()='Report Type']/../../td[2]/select");
	By runButton = By.xpath(".//tr/td[1]/button[@title='Run']");
	By ruleSetSel= By.xpath(".//label[contains(.,'Rule Set ')]/../../td[2]//select");
	By ruleSel = By.xpath(".//tr[@title='Rule']/td[2]//select");
	By jobIDSel = By.xpath(".//label[text()='Job Id']/../../td[2]//select");
	By summaryValueOnlyRadio = By.xpath(".//tr[contains(.,'Summary Values Only')]/td[2]//input");
	By summaryValueDataSampleRadio = By.xpath(".//tr[contains(.,'Summary Values and Data Sample')]/td[2]//input");
	By driverDataCheckBox = By.xpath(".//label[contains(.,'Driver Data')]/../..//input");
	By sourceDataCheckBox = By.xpath(".//label[contains(.,'Source Data')]/../..//input");
	public enum ReportName {
		Program_Documentation("Program Documentation"),
		Dimension_Statistics("Dimension Statistics"),
		Rule_Data_Validation("Rule Data Validation"),
		Execution_Statistics("Execution Statistics");
		private String name;       

	    private ReportName(String s) {
	        name = s;
	    }

	    public boolean equalsName(String otherName) {
	        return (otherName == null) ? false : name.equals(otherName);
	    }

	    public String toString() {
	       return this.name;
	    }
	}
	
	public enum ReportType {
		PDF,XML,HTML,EXCEL,WORD;
	}
	public enum RuleDataType {
		SUMMARY,ALL;
	}
	
	public SystemReportPage(WebDriver driver){
		super(driver);
		this.driver = driver;
		wait = new WebDriverWait(driver,100);
	}
	public SystemReportPage selectReportName(ReportName reportName){
		
		Select reportNameSelect = new Select(driver.findElement(reportNameSel));
		reportNameSelect.selectByVisibleText(reportName.toString());
		return this;
	}
	public SystemReportPage selectReportType(ReportType reportType){
		Select reportTypeSelect = new Select(driver.findElement(reportTypeSel));
		reportTypeSelect.selectByVisibleText(reportType.toString());
		return this;
	}
	public SystemReportPage runReport(){
		driver.findElement(runButton).click();
		return this;
	}
	public SystemReportPage selectRuleSet(String ruleSetName){
		Select ruleSetSelect = new Select(driver.findElement(ruleSetSel));
		ruleSetSelect.selectByVisibleText(ruleSetName);
		return this;
	}
	public SystemReportPage selectRule(String ruleName){
		Select ruleSelect = new Select(driver.findElement(ruleSel));
		ruleSelect.selectByVisibleText(ruleName);
		return this;
	}
	public SystemReportPage clickRuleDataReportType(RuleDataType dataType){
		if(dataType.name().equals("ALL"))
			driver.findElement(summaryValueDataSampleRadio).click();
		else
			driver.findElement(summaryValueOnlyRadio).click();
		return this;
	}

	public SystemReportPage selectPOV(String year, String period, String scenario) throws InterruptedException{
		PovSelectionPageFragment povSelectPage = new PovSelectionPageFragment(driver);
		povSelectPage.changePOV(year, period, scenario);
		return this;
	}
	public SystemReportPage selectFirstJobId(){
		Select jobIDSelect = new Select(driver.findElement(jobIDSel));
		try{
			jobIDSelect.selectByIndex(1);
		}catch(NoSuchElementException ex){
			jobIDSelect.selectByIndex(0);
		}
		return this;
	}
	public SystemReportPage selectJobIdByIndex(int index){
		Select jobIDSelect = new Select(driver.findElement(jobIDSel));
		try{
			jobIDSelect.selectByIndex(index);
		}catch(NoSuchElementException ex){
			jobIDSelect.selectByIndex(0);
		}
		return this;
	}
	public SystemReportPage clickOKButton(){
		driver.findElement(this.okButton).click();
		return this;
	}
	public SystemReportPage changeStateForSourceDataCheckBox(boolean state){
		boolean currentState = true;
		if(driver.findElement(this.sourceDataCheckBox).getAttribute("checked")==null)
			currentState = false;
		
		if(state ^ currentState )
			driver.findElement(this.sourceDataCheckBox).click();
		return this;
	} 
	public SystemReportPage changeStateForDriverDataCheckBox(boolean state){
		boolean currentState = true;
		if(driver.findElement(this.driverDataCheckBox).getAttribute("checked")==null)
			currentState = false;
		
		if(state ^ currentState )
			driver.findElement(this.driverDataCheckBox).click();
		return this;
	}
}
