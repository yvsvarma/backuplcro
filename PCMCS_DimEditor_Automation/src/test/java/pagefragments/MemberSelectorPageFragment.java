package pagefragments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

public class MemberSelectorPageFragment {
	private WebDriver driver;
	private WebDriverWait wait;
	private By moveToTheRightDimMemButton = By.xpath(".//img[@title='Add Member' and contains(@src,'right')]");
	private By addMemberButton = By.xpath(".//a/img[@alt='Add Member']");
	private By deleteMemberButton = By.xpath(".//img[@title='Delete Member' ]");
	private By removeAllMemberButton = By.xpath(".//img[@title='Remove Member' and contains(@src,'all')]");
	private By okButton =By.xpath(".//button[contains(.,'OK')]");
	public MemberSelectorPageFragment(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,120);
	}
	public void clickOKButton(){
		driver.findElement(okButton).click();
	}
	public void selectDimension(String dimName,boolean isEdit){
		WebElement view;
		view = driver.findElement(By.xpath(".//div[@title='Member Selection']/../div[1]//*[text()='"+dimName+"' and not(self::option)]/.."));

		
		Actions actions = new Actions(driver);
		actions.moveToElement(view);
		BasicUtil.waitADF(driver, 100);
		actions.doubleClick(view);
		actions.build().perform();
		BasicUtil.waitADF(driver, 100);
		
	}
	public void selectDimensionSAS(String dimName){
		WebElement view = driver.findElement(By.xpath(".//div[@title='Member Selection']/../div[1]//td[contains(.,'Accounts')]/..//input"));

		Actions actions = new Actions(driver);
		actions.click(view);
		actions.build().perform();
		
	}
	public void clickAddDimMemButton(){
		BasicUtil.waitADF(driver, 100);
		WebElement moveToTheRight =driver.findElement(moveToTheRightDimMemButton);
		Actions actions = new Actions(driver);
		actions.moveToElement(moveToTheRight);
		actions.doubleClick(moveToTheRight);
		actions.build().perform();
	}
	public void waitForSelectDimMemSelPanel(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Select Dimension Members']")));
	}
	public void expandTree(String dimMem){
		driver.findElement(By.xpath("//div[text()='"+dimMem+"']/span/a[@title='Expand']")).click();
	}
	public void selectMember(String dimMem){
		driver.findElement(By.xpath("//div[text()='"+dimMem+"']")).click();
	}
	public void addMemberButton(){
			driver.findElement(addMemberButton).click();
	}
	public void addMember(String memberPath,boolean isEdit){
		String[] memPath = memberPath.split(":");
		String dimName = memPath[0];
		
		if(dimName!=null){
			selectDimension(dimName,isEdit);
			BasicUtil.wait(2);
			addMemberButton();
			waitForSelectDimMemSelPanel();
		}
		for(int i =1; i<memPath.length;i++ ){
			if(i==memPath.length-1){
				selectMember(memPath[i]);		
				clickAddDimMemButton();
			}
			else{
				expandTree(memPath[i]);
				BasicUtil.wait(2);
			}
		}
		clickOKButton();
		BasicUtil.wait(2);
		//selectDimension(dimName,true);
	}
	public void removeAllMembers(){
		driver.findElement(removeAllMemberButton).click();
		BasicUtil.waitADF(driver, 100);
	}
	public void deleteMemberForDimension(String dimension, String member){
		selectDimension(dimension,false);
		BasicUtil.waitADF(driver, 10);
		selectDimension(member,false);
		BasicUtil.waitADF(driver, 10);
		driver.findElement(deleteMemberButton).click();
		BasicUtil.waitADF(driver, 10);
	}
	public void deleteAllMembersForDimension(String dimension,boolean isEdit){
		selectDimension(dimension,isEdit);
		addMemberButton();
		waitForSelectDimMemSelPanel();
		removeAllMembers();
		clickOKButton();
		
	}
	public void collapseAll(){
		driver.findElement(By.xpath(".//td[contains(@id,'content')]//td[1]//a[text()='View']")).click();
		BasicUtil.waitADF(driver, 10);
		driver.findElement(By.xpath("//td[contains(.,'Collapse All')]")).click();
		BasicUtil.waitADF(driver, 10);
	}
	
}
