package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

public class DimensionEditorPage {
	public WebDriver driver;
	public  Actions builder ;
	public WebDriverWait wait;
	private FuseTablePO tablePO;
	private By saveButton = By.xpath(".//table[contains(@id,'dimensionDetail')]//button[@title='Save']");
	private By closeButton = By.xpath(".//table[contains(@id,'dimensionDetail')]//button[@title='Close']");
	private By saveCloseButton = By.xpath(".//table[contains(@id,'dimensionDetail')]//button[@title='Save and Close']");
	private By searchButton = By.xpath(".//table[contains(@id,'dimensionDetail')]//img[@title='Search']"); 
	private By searchInputBox = By.xpath(".//table[contains(@id,'dimensionDetail')]//input[@placeholder='Search']");
	private By addChildButton = By.xpath(".//img[@alt='Add Child']");
	private By copyMemberButton = By.xpath(".//img[@alt='Copy Member']");
	private By reparentButton = By.xpath(".//img[@alt='Reparent ']");
	private By deleteButton = By.xpath(".//img[@alt='Delete Member']");
	private By moveUpButton = By.xpath(".//img[@alt='Move Up']");
	private By moveDownButton = By.xpath(".//img[@alt='Move Down']");
	private By addMemberPopupNameInput = By.xpath(".//div[contains(@data-afr-popupid,'addMember')]//tr[@title='Member Name']//input");
	private By addMemberPopupSharedMemSelect = By.xpath(".//div[contains(@data-afr-popupid,'addMember')]//tr[@title='Shared Member']//input");
	private By addMemberPopupOKButton = By.xpath(".//div[contains(@data-afr-popupid,'addMember')]//button[text()='OK']");
	private By addMemberPopupCancelButton = By.xpath(".//div[contains(@data-afr-popupid,'addMember')]//button[text()='Cancel']");
	private By reparentParentMemberName = By.xpath(".//div[contains(@data-afr-popupid,'reParent')]//input");
	private By reparentOKButton = By.xpath(".//div[contains(@data-afr-popupid,'reParent')]//button[text()='OK']");
	private By clearSerachButton = By.xpath(".//a[@alt='Clear Search'])");
	
	public DimensionEditorPage(WebDriver driver){
		this.driver = driver;
		builder  = new Actions(driver);
		wait = new WebDriverWait(driver,30);
	//	tablePO = new FuseTablePO(driver,".//div[contains(@title,'Dimensions')]//table[boolean(@_rowcount)]");
	}
	
	public DimensionEditorPage findMemberUsingSearch(String member){
		driver.findElement(searchInputBox).clear();
		driver.findElement(searchInputBox).sendKeys(member);
		driver.findElement(searchButton).click();
		BasicUtil.waitADF(driver, 10);
		//AdfTreeTable tree = new AdfTreeTable()
		return this;
	}
	
	public String getSelectedMemberName(){
		try{
			WebElement inputBoxMemberName = driver.findElement(By.xpath("//label[text()='Member Name']/..//input"));
			if(inputBoxMemberName!=null)
				return inputBoxMemberName.getAttribute("value");
		}
		catch(NoSuchElementException e){
			return null;
		}
		return null;
	}
	
	public DimensionEditorPage addNewMemberAsChild(String parentMemberName, String childMemberName, boolean isShared){
                BasicUtil.waitADF(driver, 100);
                //log();
		findMemberUsingSearch(parentMemberName);
                BasicUtil.waitADF(driver, 100);
		driver.findElement(this.addChildButton).click();
                BasicUtil.waitADF(driver, 100);
		driver.findElement(this.addMemberPopupNameInput).clear();
                BasicUtil.waitADF(driver, 100);
		driver.findElement(this.addMemberPopupNameInput).sendKeys(childMemberName);
		if(isShared){
                        BasicUtil.waitADF(driver, 100);
			driver.findElement(this.addMemberPopupSharedMemSelect).click();
		}
                BasicUtil.waitADF(driver, 100);
		driver.findElement(this.addMemberPopupOKButton).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(this.addMemberPopupNameInput));
		return this;
	}

	public DimensionEditorPage save() {
		driver.findElement(this.saveButton).click();
		BasicUtil.waitADF(driver, 10);
		return this;
	}
	
	public DimensionManagementPage close(){
		driver.findElement(this.closeButton).click();
		BasicUtil.waitADF(driver, 10);
		return new DimensionManagementPage(driver);
	}

	public DimensionEditorPage deleteMember(String deletedMemberName) {
		findMemberUsingSearch(deletedMemberName);
		driver.findElement(this.deleteButton).click();
		return this;
	}
	public DimensionEditorPage clickReparentButtonForMember(String memberName){
		findMemberUsingSearch(memberName);
		driver.findElement(this.reparentButton).click();
		return this;
	}
	public DimensionEditorPage populateParentDlg(String parent){
		driver.findElement(this.reparentParentMemberName).sendKeys(parent);
		return this;
	}
	public DimensionEditorPage clickReparentOKButton(){
		driver.findElement(this.reparentOKButton).click();
		return this;
	}
	
	
}
