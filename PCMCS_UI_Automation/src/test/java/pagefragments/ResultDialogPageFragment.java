package pagefragments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ResultDialogPageFragment {
	private WebDriver driver;
	private WebDriverWait wait;
	Actions builder;
	
	By addFilterLink = By.xpath(".//a[@title='Add Filter']");
	By filterOperatorSelect = By.xpath(".//label[text()='Operator']/../../td[2]//select");
	By filterValueInput = By.xpath(".//label[text()='Value']/../../td[2]//input");
	public ResultDialogPageFragment(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,10000);
		builder =  new Actions(driver);
	}
	public void clickOnAddFilter(){
		driver.findElement(addFilterLink).click();
		}
	public void clickOnMemToAddFilter(String dimMem){
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()="+dimMem+"']")));
		driver.findElement(By.xpath("//span[text()="+dimMem+"']")).click();
	}
	
}
