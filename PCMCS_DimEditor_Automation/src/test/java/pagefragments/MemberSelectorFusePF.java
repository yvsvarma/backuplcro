package pagefragments;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

public class MemberSelectorFusePF {
	private WebDriver driver;
	private WebDriverWait wait;
	private By okButton =By.xpath(".//button[text()='OK' and contains(@id,'memSel')]");
	private By leftScroll = By.xpath(".//a[@title='Scroll tabs to the left']");
	public MemberSelectorFusePF(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,120);
	}
	public void addMember(String dimName, String memberPath, String functionName){
		String[] memPath = memberPath.split(":");
		for(int i=0; i<memPath.length;i++ ){
			if(i==memPath.length-1){
				selectMember(memPath[i]);
			}
			else{
				expandTree(memPath[i]);
			}
		}
	}
	
	public void clickOKButton(){
		driver.findElement(okButton).click();
		if(!BasicUtil.waitForNonPresenceOfElement(driver, By.xpath(".//span[text()='Member Selector']")))
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//span[text()='Member Selector']")));
	}
	public void selectMember(String dimMem){
		driver.findElement(By.xpath(".//span[text()='"+dimMem+"']/../../td[1]/a")).click();
	}
	public void clickLeftScroll(){
		WebElement element = driver.findElement(leftScroll);
		if(element.getAttribute("class").contains("Disabled"))
			return;
		element.click();
	}
	public void expandTree(String dimMem){
		driver.findElement(By.xpath(".//span[text()='"+dimMem+"']")).click();
	}
	public void applyFunction(String dimMem, String funcName){
		driver.findElement(By.xpath(".//span[text()='"+dimMem+"']/../../td[3]/a")).click();
		driver.findElement(By.xpath(".//td[text()='"+funcName+"']")).click();
		
	}
}
