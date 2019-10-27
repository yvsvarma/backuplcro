package poms2;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pagefragments.MemberSelectorPageFragment;
import pagefragments.PovSelectionPageFragment;
import pagefragments.RulePageFragment;
import pagefragments.RuleSetPageFragment;
import utils.BasicUtil;

public class ManageRulesPage {
	private WebDriver driver;
	private WebDriverWait wait;
	Actions builder;
	By createNewRuleSetButtonXPath = By.xpath(".//img[@title='Create Rule Set']/..");
	By deleteRuleSetButtonXPath = By.xpath(".//img[@title='Delete Rule Set']/..");
	By copyRuleSetButtonXPath = By.xpath(".//img[@title='Copy Rule Set']/..");
	By copyRuleButtonXPath = By.xpath(".//img[@title='Copy Rule']/..");
	By createNewRuleButtonXPath = By.xpath(".//img[@title='Create Rule']/..");
	By deleteRuleSetXPath = By.xpath(".//img[@title='Delete Rule']/..");
	By editGCButton= By.xpath(".//span[contains(.,'Edit')]");
	By GCDesc= By.xpath("//label[contains(.,'Description')]/../..//textarea");
	By saveGCButton= By.xpath("//div[@title='Save the Global Context']//a");
	By gcRows = By.xpath(".//table[@summary='Global Context Dimension Selections']/tbody/tr");
	By ruleSetRowsXPath  = By.xpath(".//table[@summary='Rule Sets']/tbody/tr//table//tr");
	By ruleRowsXPath  = By.xpath(".//table[@summary='Rules']/tbody/tr");
	By copyRuleSetInput = By.xpath(".//label[text()='New Rule Set Name']/../..//input");
	By createAllocationRuleDropdown = By.xpath(".//tr[@title='Create Allocation Rule']/td[text()='Allocation']");
	By createCustomRuleDropdown = By.xpath(".//tr[@title='Create Custom Rule']/td[text()='Custom Calculation']");
	
	public ManageRulesPage(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,10000);
		builder =  new Actions(driver);
	}
	
	public ManageRulesPage selectPOV(String year, String period, String scenario){
		PovSelectionPageFragment pov = new PovSelectionPageFragment(driver);
		pov.changePOV(year, period, scenario);
		return this;
	}
	public MemberSelectorPageFragment clickEditGCButton(){
		driver.findElement(editGCButton).click();
		return new MemberSelectorPageFragment(driver);
	}
	public ManageRulesPage setGCDesc(String desc){
		driver.findElement(GCDesc).clear();
		driver.findElement(GCDesc).sendKeys(desc);
		return this;
	}
	public ManageRulesPage clickSaveGCButton(){
		driver.findElement(saveGCButton).click();
		return this;
	}
	public HashMap<String,String> getGlobalContextSelection(){
		HashMap<String,String> gcSelectionMap = new HashMap<String,String>();
		List<WebElement> gcList = driver.findElements(gcRows);
		for(WebElement globalContextRowElement : gcList){
			String dimName = globalContextRowElement.findElement(By.xpath(".//td[1]")).getText();
			String memName = globalContextRowElement.findElement(By.xpath(".//td[2]")).getText();
			gcSelectionMap.put(dimName, memName);
			BasicUtil.log(dimName + " : "+memName);
		}
		return gcSelectionMap;
	}
	public HashMap<String,String> getRuleSets(){
		HashMap<String,String> rsSelectionMap = new HashMap<String,String> ();
		List<WebElement> rsList = driver.findElements(ruleSetRowsXPath);
		int i = 1;
		for(WebElement rsRowElement : rsList){
			String rowNumber = ""+i;
			String ruleSetName = rsRowElement.findElement(By.xpath(".//td[2]")).getText();
			rsSelectionMap.put(rowNumber, ruleSetName);
			//BasicUtil.log(rowNumber + " : "+ruleSetName);
			i++;
		}
		return rsSelectionMap;
	}
	public HashMap<String,String> getRules(){
		HashMap<String,String> ruleSelectionMap = new HashMap<String,String> ();
		List<WebElement> ruleList = driver.findElements(ruleRowsXPath);
		int i = 1;
		for(WebElement ruleRowElement : ruleList){
			String rowNumber = ""+i;
			String ruleName = ruleRowElement.findElement(By.xpath(".//td[2]")).getText();
			ruleSelectionMap.put(rowNumber, ruleName);
			//BasicUtil.log(rowNumber + " : "+ruleName);
			i++;
		}
		return ruleSelectionMap;
	}
	public RuleSetPageFragment selectRuleSet(String ruleSetName){
		HashMap<String,String> rsMap =  getRuleSets();
		if(rsMap.containsValue(ruleSetName))
		{
			for(String key : rsMap.keySet()){
				if((ruleSetName.equals((String)rsMap.get(key)))){
					driver.findElement(By.xpath(".//table[@summary='Rule Sets']/tbody/tr["+key+"]")).click();
				}
			}
		}
		return new RuleSetPageFragment(driver);
	}
	public ManageRulesPage selectRule(String ruleName){
		HashMap<String,String> ruleMap =  getRules();
		if(ruleMap.containsValue(ruleName))
		{
			for(String key : ruleMap.keySet()){
				if((ruleName.equals((String)ruleMap.get(key)))){
					driver.findElement(By.xpath(".//table[@summary='Rules']/tbody/tr["+key+"]")).click();
				}
			}
		}
		return this;
	}
	public RuleSetPageFragment clickAddRuleSetButton(){
		driver.findElement(this.createNewRuleSetButtonXPath).click();
		return new RuleSetPageFragment(driver);
	}
	public ManageRulesPage clickDeleteRuleSetButton(){
		driver.findElement(this.deleteRuleSetButtonXPath).click();
		return this;
	}
	public ManageRulesPage clickCopyRuleSetButton(){
		driver.findElement(this.copyRuleSetButtonXPath).click();
		return this;
	}
	public ManageRulesPage clickAddRuleButton(){
		driver.findElement(this.createNewRuleButtonXPath).click();
		return this;
	}

	public RulePageFragment clickAllocationRuleDropdown(){
		driver.findElement(this.createAllocationRuleDropdown).click();
		return new RulePageFragment(driver);
	}
	
	public RulePageFragment clickCustomRuleDropdown(){
		driver.findElement(this.createCustomRuleDropdown).click();
		return new RulePageFragment(driver);
	}
	public ManageRulesPage clickDeleteRuleButton(){
		driver.findElement(this.deleteRuleSetXPath).click();
		return this;
	}
	public ManageRulesPage clickCopyRuleButton(){
		driver.findElement(this.copyRuleButtonXPath).click();
		return this;
	}
	public ManageRulesPage setNewCopyRuleSetName(String rsName){
		driver.findElement(copyRuleSetInput).sendKeys(rsName);
		return this;
	}
	public boolean doesThisRuleSetExists(String name){
		HashMap<String,String> ruleSetMap = getRuleSets();
		for(String key : ruleSetMap.keySet()){
			if(ruleSetMap.get(key).equals(name))
				return true;
		}
		return false;
	}
}
