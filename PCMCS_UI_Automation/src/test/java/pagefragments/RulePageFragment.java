package pagefragments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RulePageFragment {
	public WebDriver driver;
	public WebDriverWait wait;
	By ruleNameInput = By.xpath(".//label[text()='Rule Name']/../..//input");
	By descTextArea = By.xpath(".//label[text()='Description']/../..//textarea");
	By enabledCheckBox = By.xpath(".//label[text()='Enabled']/../..//input");
	By sequenceInput = By.xpath(".//label[text()='Sequence']/../..//input");

	By ruleSetContextCheckBox = By.xpath(".//label[text()='Use Rule Set Context']/../..//input");
	By specifyAllocatedCheckBox = By.xpath(".//label[text()='Specify Allocated Amount']/../..//input");
	By amountAllocationRadio = By.xpath(".//label[text()='Amount']/..//input");
	By percentageAllocationRadio = By.xpath(".//label[text()='Percentage']/..//input");
	By allocationAmountInput = By.xpath(".//label[text()='Allocated Amount']/../..//input");
	By specifyDriverLocRadio = By.xpath(".//label[text()='Specify Driver Location']/..//input");
	By allocateEvenlyRadio = By.xpath(".//label[text()='Allocate Evenly']/..//input");


	By sourceOffsetRadio = By.xpath(".//label[text()='Source']/..//input");
	By alternateOffsetRadio = By.xpath(".//label[text()='Alternate Offset Location']/..//input");
	By descriptionTab = By.xpath(".//a[text()='Description']");
	By sourceTab = By.xpath(".//a[text()='Source']");
	By destinationTab = By.xpath(".//a[text()='Destination']");
	By offsetTab = By.xpath(".//a[text()='Offset']");
	By driverBasisTab = By.xpath(".//a[text()='Driver Basis']");
	By estimatedCountLabel = By.xpath("//label[contains(.,'Estimated Destination Count')]");	
	By saveButton = By.xpath(".//div[@title='Saves the Rule Set.']//img");
	By ruleSaveButton = By.xpath(".//h2[text()='Rule Definition']/../../..//img");
	public RulePageFragment(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,100);
	}
	public void setRuleName(String name){
		driver.findElement(this.ruleNameInput).clear();
		driver.findElement(this.ruleNameInput).sendKeys(name);
	}
	public void setRuleDescription(String desc){
		driver.findElement(this.descTextArea).clear();
		driver.findElement(this.descTextArea).sendKeys(desc);
	}
	public void setRuleSequence(String sequence){
		driver.findElement(this.sequenceInput).clear();
		driver.findElement(this.sequenceInput).sendKeys(sequence);
	}
	public void setAllocationAmount(String amount){
		driver.findElement(this.allocationAmountInput).clear();
		driver.findElement(this.allocationAmountInput).sendKeys(amount);
	}
	public void clickUseRSContextCheckBox(){
		driver.findElement(this.ruleSetContextCheckBox).click();
	}
	public void clickSpecifyAllocationAmountCheckbox(){
		driver.findElement(this.specifyAllocatedCheckBox).click();
	}
	public void clickAllcationAmountRadio(){
		driver.findElement(this.amountAllocationRadio).click();
	}
	public void clickPercentageAmountRadio(){
		driver.findElement(this.percentageAllocationRadio).click();
	}
	public void clickAllocateEvenlyRadio(){
		driver.findElement(this.allocateEvenlyRadio).click();
	}
	public MemberSelectorPageFragment clickSpecifyDriverRadio(){
		driver.findElement(this.specifyDriverLocRadio).click();
		return new MemberSelectorPageFragment(driver);
	}
	public void clickSourceOffsetRadio(){
		driver.findElement(this.sourceOffsetRadio).click();
	}
	public MemberSelectorPageFragment clickAlternateOffsetRadio(){
		driver.findElement(this.alternateOffsetRadio).click();
		return new MemberSelectorPageFragment(driver);
	}
	
	public void clickSaveButton(){
		driver.findElement(saveButton).click();
	}
	public void clickRuleSaveButton(){
		driver.findElement(ruleSaveButton).click();
	}
	
	public String getSequenceInput() {
		return driver.findElement(sequenceInput).getAttribute("value");
	}
	public boolean isEnabled() {
		if(driver.findElement(enabledCheckBox).getAttribute("checked")==null)
			return false;
		else
			return true;
	}
	public boolean isUseRuleSetContext() {
		if(driver.findElement(this.ruleSetContextCheckBox).getAttribute("checked")==null)
			return false;
		else
			return true;
	}

	public RulePageFragment goToDescriptionTab(){
		driver.findElement(this.descriptionTab).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(this.ruleNameInput));
		return this;
	}
	public MemberSelectorPageFragment goToSourceTab(){
		driver.findElement(this.sourceTab).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(this.specifyAllocatedCheckBox));
		return new MemberSelectorPageFragment(driver);
	}
	public MemberSelectorPageFragment goToDestinationTab(){
		driver.findElement(this.destinationTab).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(this.estimatedCountLabel));
		return new MemberSelectorPageFragment(driver);
	}
	public RulePageFragment goToOffsetTab(){
		driver.findElement(this.offsetTab).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(this.sourceOffsetRadio));
		return this;
	}
	public RulePageFragment goToDriverTab(){
		driver.findElement(this.driverBasisTab).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(this.allocateEvenlyRadio));
		return this;
	}
	
}
