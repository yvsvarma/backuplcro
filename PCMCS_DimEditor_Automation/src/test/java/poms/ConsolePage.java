package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConsolePage {
	private By applicationSubPageLink = By.xpath(".//img[@title='Application']/..");
	//private By usersSubPageLink = By.xpath(".//img[@title='Users and Groups']/..");
//	private By dataGrantsSubPageLink = By.xpath(".//img[@title='Data Grants']/..");
	public WebDriver driver;
	public  Actions builder ;
	public WebDriverWait wait;
	public ConsolePage(WebDriver driver){
		this.driver = driver;
		builder  = new Actions(driver);
		wait = new WebDriverWait(driver,30);
	}
	public ConsoleApplicationPage clickApplicationSubPageLink(){
		driver.findElement(applicationSubPageLink).click();
		return new ConsoleApplicationPage(driver);
	}
}
