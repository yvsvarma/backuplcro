package poms;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.BasicUtil;

public class ConsoleJobLibPage {
	WebDriver driver;
	By refreshButton = By.xpath(".//img[@title='Refresh']");
	By filterAppName = By.xpath(".//table/tbody/tr[2]/th[@_d_index='2']/span/input");
	By filterJobType = By.xpath(".//table/tbody/tr[2]/th[@_d_index='8']/span/input");
	By jobRows = By.xpath(".//table[@summary='Job Library Data']/tbody/tr[1]//td[7]");
	By jobDetailsRows = By.xpath(".//table[@summary='Job Library Data']//tr[1]");
	public ConsoleJobLibPage(WebDriver driver){
		this.driver = driver;
	}
	public ConsoleJobLibPage refreshJobLib(){
		driver.findElement(refreshButton).click();
		return this;
	}
	public ConsoleJobLibPage filterByAppname(String appName)
	{
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(filterAppName).clear();
		driver.findElement(filterAppName).sendKeys(appName);
		driver.findElement(filterAppName).sendKeys(Keys.ENTER);
		return this;
	}
	public ConsoleJobLibPage filterByJobType(String jobType)
	{
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(filterJobType).clear();
		driver.findElement(filterJobType).sendKeys(jobType);
		driver.findElement(filterJobType).sendKeys(Keys.ENTER);
		return this;
	}
	public ConsoleJobLibPage selectFirstJob(String appName,String jobType){
		//filterByJobType(jobType);
		//filterByAppname(appName);
		BasicUtil.waitADF(driver, 10);
		driver.findElement(jobRows).click();
		BasicUtil.waitADF(driver, 10);
		return this;
	}
	public ConsoleJobLibPage waitForjobToFinish(String appName,String jobType, int timeOut) throws Exception{
/*		filterByJobType(jobType);
		filterByAppname(appName);*/
		int tryCount =0;
		int maxtries = 100;
		int waitForEachTry = timeOut/maxtries;
		this.selectFirstJob("", "");
		this.printJobProps();
		for(;tryCount<maxtries;tryCount++){
			refreshJobLib();
			BasicUtil.wait(waitForEachTry);
			if(!isFirstJobListedIsSuccess())
				continue;
			else
				break;
			
		}
		System.out.println("job Finished successfully.");
		return this;
	}
	public void printJobProps() throws Exception{
		
		BasicUtil.waitADF(driver, 10);
		List<WebElement> listOfElements = driver.findElements(jobRows);
		for(WebElement wb : listOfElements)
			BasicUtil.log(wb.getText());
	}
	public boolean isFirstJobListedIsSuccess() throws Exception{
		
		BasicUtil.wait(2);
		List<WebElement> listOfElements = driver.findElements(jobRows);
		
		System.out.println("Status of the job is : "+listOfElements.get(0).getText());

		if(listOfElements.get(0).getText().contains("Failure"))
			throw new Exception("Import Job Failed.");
		if(listOfElements.get(0).getText().contains("Success"))
			return true;
		else
			return false;
	}
}
