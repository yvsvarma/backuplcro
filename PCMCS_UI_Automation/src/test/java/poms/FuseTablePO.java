package poms;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;
import utils.ClientSynchedWithServer;
import org.openqa.selenium.JavascriptExecutor;
public class FuseTablePO {
	private WebDriver driver;
	private WebDriverWait wait;
	private int short_timeout;
	@SuppressWarnings("unused")
	private int large_timeout;
	private String s_xpathTable;
	
	public FuseTablePO(WebDriver driver, String xpathTableElement){
		this.driver = driver;
		this.s_xpathTable=xpathTableElement;
		
		String s_large_timeout = System.getProperty("large_timeout");
		String s_small_timeout = System.getProperty("small_timeout");
		short_timeout = s_small_timeout==null?20:Integer.parseInt(s_small_timeout);
		large_timeout = s_large_timeout==null?100:Integer.parseInt(s_large_timeout);
		wait = new WebDriverWait(driver,short_timeout);
		
	}
	By xpathRefreshButton = By.xpath("//button/span[contains(.,'Refresh')]");
	By xpathActionButton = By.xpath("//button/span[contains(.,'Actions')]");
	By xpathCreateIcon = By.xpath(".//img[contains(@alt,'Create')]");
	By xpathDeleteIcon = By.xpath(".//img[contains(@alt,'Delete')]/..");
	By xpathEditIcon = By.xpath(".//img[contains(@alt,'Edit')]/..");
	By xpathInspectIcon = By.xpath(".//img[contains(@alt,'Inspect')]/..");
	
	
	public int getRowIDForRow(String itemName){
		ArrayList<String> dbList;
		dbList = getAllRows();
		for(String name : dbList)
			if(name.contains(itemName)) return dbList.indexOf(itemName)+1;
		return -1;
	}
	public ArrayList<String> getAllRows(){
		//sBasicUtil.highlightElement(driver, tableElement);
		List<WebElement> table = driver.findElements(By.xpath(this.s_xpathTable+"//tr[boolean(@_afrrk)]/td[1]/div"));
		ArrayList<String> list = new ArrayList<String>();
		for(WebElement row : table){
			String name = row.getText();
			if(!name.trim().equals(""))
				list.add(name.trim());
				//System.out.println("row : "+name.trim());
		}
		return list;
	}
	public void selectRow(String rowName){
		//if not visible scroll into view,to do
                BasicUtil.waitADF(driver, short_timeout);
		WebElement wb = driver.findElement(By.xpath(this.s_xpathTable+"//tr[boolean(@_afrrk)]/td[1]//a[contains(.,'"+rowName+"')]/.."));
		wb.click();
	}
	public void clickOnCreateButton(){
		new Actions(driver).moveToElement(driver.findElement(this.xpathCreateIcon)).build().perform();
		BasicUtil.waitADF(driver,10);
		new Actions(driver).moveToElement(driver.findElement(this.xpathCreateIcon)).click().build().perform();
	}
	public void clickOnEditButton(String itemName){
		selectRow(itemName);
		System.out.println(driver.findElement(this.xpathEditIcon).getAttribute("class"));
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver driver){
				WebElement wb = driver.findElement(By.xpath(".//img[contains(@alt,'Edit')]/.."));
				System.out.println(wb.getAttribute("class"));
				if(wb.getAttribute("class").contains("Disabled")){
					return false;
				}
				return true;
			}
		});
		driver.findElement(this.xpathEditIcon).click();
	}
	public void clickOnDeleteButton(String itemName){
		selectRow(itemName);
		System.out.println(driver.findElement(this.xpathDeleteIcon).getAttribute("class"));
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver driver){
				WebElement wb = driver.findElement(By.xpath(".//img[contains(@alt,'Delete')]/.."));
				System.out.println(wb.getAttribute("class"));
				if(wb.getAttribute("class").contains("Disabled"))
					return false;
				return true;
			}
		});
		driver.findElement(this.xpathDeleteIcon).click();
	}
	public void clickOnActionButton(String itemName){
		selectRow(itemName);

		driver.findElement(this.xpathActionButton).click();
	}
	public void clickOnActionButton(){
		driver.findElement(this.xpathActionButton).click();
	}
	public void clickOnCopyButton(String itemName){
		
	}
	public void clickOnRowAction(String rowName) {
		WebElement wb = driver.findElement(By.xpath(this.s_xpathTable+"//tr[boolean(@_afrrk)]/td[1]/div[contains(.,'"+rowName+"')]/../../td//a[@title='Actions']"));
		wb.click();
	}
	
	public void clickOnActionsMenuText(String rowName,String text){

		clickOnRowAction(rowName);
		wait.until(new ClientSynchedWithServer());
		WebElement ls = driver.findElement(By.xpath(".//tr[contains(@title,'"+text+"')]"));
		wait.until(ExpectedConditions.visibilityOf(ls));
		//utils.BasicUtil.highlightElement(driver, ls);
		ls = driver.findElement(By.xpath(".//tr[contains(@title,'"+text+"')]"));
		new Actions(driver).moveToElement(ls).click().build().perform();
	}
	public void clickOnRefreshButton() {
		driver.findElement(this.xpathRefreshButton).click();
		new WebDriverWait(driver, 10).until(new ClientSynchedWithServer());
		
	}
	public void clickOnInspectButton(String itemName){
		selectRow(itemName);
		//System.out.println(driver.findElement(this.xpathDeleteIcon).getAttribute("class"));
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver driver){
				WebElement wb = driver.findElement(By.xpath(".//img[contains(@alt,'Inspect')]/.."));
				System.out.println(wb.getAttribute("class"));
				if(wb.getAttribute("class").contains("Disabled"))
					return false;
				return true;
			}
		});
		driver.findElement(this.xpathInspectIcon).click();
	}
	
	public void clickOnColumnNumberForRow(String itemName,int i){
		if(i<1)
			return;
		WebElement wb = driver.findElement(By.xpath(this.s_xpathTable+"//tr/td[contains(.,'"+itemName+"')]/../td["+i+"]//a"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", wb);;
		
		wb.click();
	}
	
}
