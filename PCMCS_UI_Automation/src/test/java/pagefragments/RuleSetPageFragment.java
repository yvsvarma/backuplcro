package pagefragments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RuleSetPageFragment {
	public WebDriver driver;
	public WebDriverWait wait;
	By saveButton = By.xpath(".//div[@title='Saves the Rule Set.']//img");
	By contextTab = By.xpath(".//a[text()='Context']");
	By DescriptionTab = By.xpath(".//a[text()='Description']");
	By ruleSetNameInput = By.xpath(".//td/label[text()='Rule Set Name']/../../td/input");
	By ruleSetDescriptionTextArea = By.xpath(".//td/label[text()='Description']/../../td/textarea");
	By sequenceInput = By.xpath(".//td/label[text()='Sequence']/../../td/input");
	By enabledCheckBox = By.xpath(".//td/label[text()='Enabled']/../../td//input");
	By useGlobalContextCheckBox = By.xpath(".//td/label[text()='Use Global Context']/../../td//input");
	By numberOfIteraionInput = By.xpath(".//td/label[text()='Number of Iterations']/../../td//input");

	
	public enum ExecutionType {
		Parallel_Execution("Parallel Execution"),
		Serial_Execution("Serial Execution"),
		Iterative_Execution("Iterative Execution");
		private String name;       

	    private ExecutionType(String s) {
	        name = s;
	    }

	    public boolean equalsName(String otherName) {
	        return (otherName == null) ? false : name.equals(otherName);
	    }

	    public String toString() {
	       return this.name;
	    }
	}
	public RuleSetPageFragment(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,100);
	}
	//.//td/label[text()='Serial Execution']/../../td//input
	public RuleSetPageFragment selectRuleSetExecutionType(ExecutionType ex){
		driver.findElement(By.xpath(".//td/label[text()='"+ex.toString()+"']/../../td//input")).click();
		return this;
	}	//.//td/label[text()='Serial Execution']/../../td//input
	public ExecutionType getRuleSetExecutionType(){
		for(ExecutionType e : ExecutionType.values()){
			String checkedAttr = driver.findElement(By.xpath(".//td/label[text()='"+e.toString()+"']/../../td//input")).getAttribute("checked");
			if(checkedAttr==null)
				continue;
			else
				return e;
		}
		return null;
	}
	public void clickSaveButton(){
		driver.findElement(saveButton).click();
	}
	public String getRuleSetName() {
		return driver.findElement(ruleSetNameInput).getAttribute("value");
	}
	public String getRuleSetDescription() {
		return driver.findElement(ruleSetDescriptionTextArea).getText();
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
	public boolean isUseGlobalContext() {
		if(driver.findElement(useGlobalContextCheckBox).getAttribute("checked")==null)
			return false;
		else
			return true;
	}
	public String getNumberOfIteraionInput() {
		return driver.findElement(numberOfIteraionInput).getText();
	}
	public void setRuleSetName(String ruleSetName) {
		driver.findElement(ruleSetNameInput).clear();
		driver.findElement(ruleSetNameInput).sendKeys(ruleSetName);
	}
	public void setRuleSetDescription(String ruleSetDescription) {
		driver.findElement(ruleSetDescriptionTextArea).clear();
		driver.findElement(ruleSetDescriptionTextArea).sendKeys(ruleSetDescription);
	}
	public void setSequence(String sequence) {		
		driver.findElement(sequenceInput).clear();
		driver.findElement(sequenceInput).sendKeys(sequence);
		
	}
	public void clickEnabledCheckBox() {
		driver.findElement(enabledCheckBox).click();
	}
	public void clickUseGlobalContextCheckBox() {
		driver.findElement(useGlobalContextCheckBox).click();
	}
	public void setNumberOfIteraionInput(String numberOfIteraion) {
		driver.findElement(numberOfIteraionInput).clear();
		driver.findElement(numberOfIteraionInput).sendKeys(numberOfIteraion);
	}
	public RuleSetPageFragment goToDescriptionTab(){
		driver.findElement(this.DescriptionTab).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//label[contains(.,'Rule Set Name')]")));
		return this;
	}
	public MemberSelectorPageFragment goToContextTab(){
		driver.findElement(this.contextTab).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//h3[contains(.,'Dimensions')]")));
		return new MemberSelectorPageFragment(driver);
	}
	
}
