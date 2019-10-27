package pagefragments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateEditKPIPageFragment {
	private WebDriver driver;
	private WebDriverWait wait;
	Actions builder;
	
	By displayAliasCheckBox = By.xpath(".//button[text()='.//tr[@title='Display Alias for Members']//input']");
	By suffixInput = By.xpath(".//label[text()='Value Suffix']/../following-sibling::td//input");
	By prefixInput = By.xpath(".//label[text()='Value Prefix']/../following-sibling::td//input");
	By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	By nameInputField = By.xpath(".//label[text()='Name']/../following-sibling::td//input");
	By descInputField = By.xpath(".//label[text()='Description']/../following-sibling::td//textarea");
	By enabledCheckBox = By.xpath(".//label[text()='Enabled']/../following-sibling::td//input");
	By populationDimensionSel	 = By.xpath(".//label[text()='Population Dimension']/../following-sibling::td//select");
	By compariosnDimensionSel	 = By.xpath(".//label[text()='Comparison Dimension']/../following-sibling::td//select");
	By definitionTab = By.xpath(".//a[text()='Base Definition']");
	By dataSliceTab = By.xpath(".//a[text()='Data Slice']");
	By statisticsTab = By.xpath(".//a[text()='Statistics']");
	By scoreCategoryTab = By.xpath(".//a[text()='Score Category']");
	By comparisonTab = By.xpath(".//a[text()='Comparison']");
	By displayOptionsTab = By.xpath(".//a[text()='Display Options']");
	public By populationDimensionMemberSelLink = By.xpath(".//label[text()='Population Dimension Member']/../../td[2]//a");
	public By comparisonDimensionMemberSelLink = By.xpath(".//label[text()='Comparison Member']/../../td[2]//a");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By createButton = By.xpath(".//button[text()='Create']");
	By OKButton = By.xpath(".//button[text()='OK']");
	By cancelButton = By.xpath(".//button[text()='Cancel']");
	By noButton = By.xpath(".//button[text()='No']");
	public CreateEditKPIPageFragment(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,10000);
		builder =  new Actions(driver);
	}
	public void clickOKButton(){
		driver.findElement(OKButton).click();
	}
	public void clickCancelButton(){
		driver.findElement(cancelButton).click();

	}
	public void populateName(String name){
		driver.findElement(nameInputField).sendKeys(name);
	}
	public void populateDesc(String desc){
		driver.findElement(descInputField).sendKeys(desc);
	}
	public void checkEnabledBox(){
		driver.findElement(enabledCheckBox).click();
	}
	public void clickOnDefinitionTab(){
		driver.findElement(definitionTab).click();
	}
	public void clickOnDataSliceTab(){
		driver.findElement(dataSliceTab).click();
	}
	public void clickOnDisplayOptionsTab(){
		driver.findElement(displayOptionsTab).click();
	}
	public void clickOnStatTab(){
		driver.findElement(statisticsTab).click();
	}
	public void clickOnScoreCategoryTab(){
		driver.findElement(scoreCategoryTab).click();
	}
	public void clickOnComparisonTab(){
		driver.findElement(comparisonTab).click();
	}
	
	public void selectPopulationDim(String populationDim){
		Select rowsel = new Select(driver.findElement(populationDimensionSel));
		rowsel.selectByVisibleText(populationDim);
	}
	public MemberSelectorFusePF editDataSlice(String dimName){
		driver.findElement(By.xpath(".//span[text()='"+dimName+"']/../../td[2]//a")).click();
		return new MemberSelectorFusePF(driver);
	}
	public MemberSelectorFusePF clickOnSelectPopMemLink(){
		driver.findElement(populationDimensionMemberSelLink).click();
		return new MemberSelectorFusePF(driver);
	}

	public void clickSaveAndClose(){
		driver.findElement(saveCloseButton).click();
	}
	public void selectComputationOption(String compOption){
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//label[text()='Computation Option']")));
		driver.findElement(By.xpath(".//label[text()='"+compOption+"']")).click();
	}
	public void selectStatisticalOption(String statOption){
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//label[text()='Statistical Option']")));
		driver.findElement(By.xpath(".//label[text()='"+statOption+"']")).click();
	}
	public void selectComparisonOption(String comparisonOption){
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//label[text()='Comparison Option']")));
		driver.findElement(By.xpath(".//label[text()='"+comparisonOption+"']")).click();
	}
	public void selectComparisonDimension(String comparisonDimension){
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//label[text()='Comparison Dimension']")));
		Select rowsel = new Select(driver.findElement(compariosnDimensionSel));
		rowsel.selectByVisibleText(comparisonDimension);
	}
	public MemberSelectorFusePF clickOnComparisonDimMemSelLink(){
		driver.findElement(comparisonDimensionMemberSelLink).click();
		return new MemberSelectorFusePF(driver);
	}
	public void selectDisplayOptions(boolean alias, String prefix, String suffix){
		if (alias)
			driver.findElement(displayAliasCheckBox).click();
		driver.findElement(suffixInput).clear();
		driver.findElement(suffixInput).sendKeys(suffix);
		driver.findElement(prefixInput).clear();
		driver.findElement(prefixInput).sendKeys(prefix);
	
	}
}
