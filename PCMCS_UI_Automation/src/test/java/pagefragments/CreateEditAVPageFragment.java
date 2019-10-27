package pagefragments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateEditAVPageFragment {
	private WebDriver driver;
	private WebDriverWait wait;
	Actions builder;
	 
	By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	By nameInputField = By.xpath(".//label[text()='Name']/../following-sibling::td//input");
	By descInputField = By.xpath(".//label[text()='Description']/../following-sibling::td//textarea");
	By enabledCheckBox = By.xpath(".//label[text()='Enabled']/../following-sibling::td//input");
	By rowDimSel = By.xpath(".//label[text()='Row Dimension']/../following-sibling::td//select");
	By colDimSel = By.xpath(".//label[text()='Column Dimension']/../following-sibling::td//select");
	By definitionTab = By.xpath(".//a[text()='Definition']");
	By dataSliceTab = By.xpath(".//a[text()='Data Slice']");
	By analysisOptionsTab = By.xpath(".//a[text()='Analysis Options']");
	By selectColDimMemLink = By.xpath(".//label[text()='Column Members']/../../td[2]//a");
	By selectRowDimMemLink = By.xpath(".//label[text()='Row Members']/../../td[2]//a");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By createButton = By.xpath(".//button[text()='Create']");
	By OKButton = By.xpath(".//button[text()='OK']");
	By cancelButton = By.xpath(".//button[text()='Cancel']");
	By noButton = By.xpath(".//button[text()='No']");
	public CreateEditAVPageFragment(WebDriver driver){
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
		driver.findElement(nameInputField).click();
		driver.findElement(nameInputField).sendKeys(name);
	}
	public void populateDesc(String desc){
		driver.findElement(descInputField).click();
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
	public void clickOnAnalysisOptionsTab(){
		driver.findElement(analysisOptionsTab).click();
	}
	public void selectRowDim(String rowDim){
		Select rowsel = new Select(driver.findElement(rowDimSel));
		rowsel.selectByVisibleText(rowDim);
	}
	public MemberSelectorFusePF editDataSlice(String dimName){
		driver.findElement(By.xpath(".//span[text()='"+dimName+"']/../../td[2]//a")).click();
		return new MemberSelectorFusePF(driver);
	}
	public void selectColDim(String colDim){
		WebElement col = driver.findElement(colDimSel);
		Select colsel = new Select(driver.findElement(colDimSel));
		colsel.selectByVisibleText(colDim);
	}
	public MemberSelectorFusePF clickOnSelectRowMemLink(){
		driver.findElement(selectRowDimMemLink).click();
		return new MemberSelectorFusePF(driver);
	}
	public MemberSelectorFusePF clickOnSelectColMemLink(){
		driver.findElement(selectColDimMemLink).click();
		return new MemberSelectorFusePF(driver);
	}
	public void clickSaveAndClose(){
		driver.findElement(saveCloseButton).click();
	}
}
