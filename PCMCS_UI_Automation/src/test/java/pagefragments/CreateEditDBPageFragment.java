package pagefragments;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;
/**
 * @author      Mohnish Banchhor <mohnish.banchhor@oracle.com>
 * @version     1.0
 * @since       1.0          
 */
public class CreateEditDBPageFragment {
	private WebDriver driver;
	private WebDriverWait wait; 
	Actions builder;
	 
	By saveCloseButton = By.xpath(".//button[text()='Save and Close']");
	By nameInputField = By.xpath(".//label[text()='Name']/../following-sibling::td//input");
	By descInputField = By.xpath(".//label[text()='Description']/../following-sibling::td//textarea");
	By enabledCheckBox = By.xpath(".//label[text()='Enabled']/../following-sibling::td//input");
	By headerInputField = By.xpath(".//label[text()='Header']/../following-sibling::td//input");
	By graphTab = By.xpath(".//div[@title='Graphs']/a");
	By URLTab = By.xpath(".//div[@title='URL']/a");
	By yesButton = By.xpath(".//button[text()='Yes']");
	By createButton = By.xpath(".//button[text()='Create']");
	By OKButton = By.xpath(".//button[text()='OK']");
	By cancelButton = By.xpath(".//button[text()='Close']");
	By noButton = By.xpath(".//button[text()='No']");
	public CreateEditDBPageFragment(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,10000);
		builder =  new Actions(driver);
	}
	
	public void clickOKButton() throws Exception{
		BasicUtil.findVisibleElement(driver, OKButton).click();
	}
	public WebElement findDBCell(int cellID){
		return driver.findElement(By.xpath(".//div[contains(@id,'cell"+cellID+"::content')]/../div"));
	}
	public void clickOnCellPreference(int cellID){
		driver.findElement(By.xpath(".//div[contains(@id,'cell"+cellID+"::content')]//a[@title='Cell Preferences']")).click();
	}
	public void clickOnDeleteVisualization(int cellID){
		driver.findElement(By.xpath(".//div[contains(@id,'cell"+cellID+"::content')]//a[@title='Delete Visualization'])")).click();
	}
	public void clickCancelButton() throws Exception{
		BasicUtil.findVisibleElement(driver, cancelButton).click();

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
	public String getErrorsFromPopup(){
		return driver.findElement(By.xpath("//td[contains(@id,'error::content')]")).getText();
	}
	public void clickSaveAndClose(){
		driver.findElement(saveCloseButton).click();
	}
	public void dragAndDropVisualization(String visualizatioName, int cellID){
		WebElement source = driver.findElement(By.xpath("//span[@title='"+visualizatioName+"']"));
		WebElement destination = findDBCell(cellID);
		BasicUtil.dragAndDrop(driver,source,destination);
	}
	public void populateChartPrefrences(String analysisViewName, String analysisViewType, String header, String...membersDelimited) throws Exception{
		if(analysisViewType.equals("Key Performance Indicator")){
			driver.findElement(By.xpath(".//a[contains(@id,'lovIcon')]")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//a[text()='More...']")));
			driver.findElement(By.xpath(".//span[contains(.,'"+analysisViewName+"')]")).click();
		}
		if(analysisViewType.equals("Analysis View")){
			selectAV(analysisViewName);
			BasicUtil.waitADF(driver, 100);
		}
		if(header!=null){
			driver.findElement(By.xpath(".//tr[@title='Header']//input")).sendKeys(header);
		}
	    for(int i = 0; i < membersDelimited.length; i++){
	        // walk through array of arguments
	    	String memberString = membersDelimited[i];
	    	String memberType = memberString.split("_")[0];
	    	String memberName = memberString.split("_")[1];
	    	driver.findElement(By.xpath(".//tr[contains(@title,'"+memberType+"')]//table//a")).click();
	    	BasicUtil.waitADF(driver, 100);
	    	driver.findElement(By.xpath(".//label[text()='"+memberName+"']")).click();
	    }
	}
	public void populateLineChartPrefrences(String analysisViewName, String header, String...members) throws Exception{

		if(analysisViewName!=null){
			selectAV(analysisViewName);
			BasicUtil.waitADF(driver, 100);
		}
		if(header!=null){
			driver.findElement(By.xpath(".//tr[@title='Header']//input")).sendKeys(header);
		}
		driver.findElement(By.xpath(".//tr[contains(@title,'Lines')]//table//a")).click();
	    for(int i = 0; i < members.length; i++){
	        // walk through array of arguments

	    	
	    	BasicUtil.waitADF(driver, 100);
	    	driver.findElement(By.xpath(".//label[text()='"+members[i]+"']")).click();
	    }
	}
	public void populateBarChartPrefrences(String analysisViewName, String header, String...members) throws Exception{

		if(analysisViewName!=null){
			selectAV(analysisViewName);
			BasicUtil.waitADF(driver, 100);
		}
		if(header!=null){
			driver.findElement(By.xpath(".//tr[@title='Header']//input")).sendKeys(header);
		}
		driver.findElement(By.xpath(".//tr[contains(@title,'Bars')]//table//a")).click();
	    for(int i = 0; i < members.length; i++){
	        // walk through array of arguments
	    	BasicUtil.waitADF(driver, 100);
	    	driver.findElement(By.xpath(".//label[text()='"+members[i]+"']")).click();
	    }
	}
	public void populateStackedChartPrefrences(String analysisViewName, String header, String...members) throws Exception{

		if(analysisViewName!=null){
			selectAV(analysisViewName);
			BasicUtil.waitADF(driver, 100);
		}
		if(header!=null){
			driver.findElement(By.xpath(".//tr[@title='Header']//input")).sendKeys(header);
		}
		driver.findElement(By.xpath(".//tr[contains(@title,'Area')]//table//a")).click();
	    for(int i = 0; i < members.length; i++){
	        // walk through array of arguments
	    	BasicUtil.waitADF(driver, 100);
	    	driver.findElement(By.xpath(".//label[text()='"+members[i]+"']")).click();
	    }
	}
	public void populateColumnChartPrefrences(String analysisViewName, String header, String...members) throws Exception{

		if(analysisViewName!=null){
			selectAV(analysisViewName);
			BasicUtil.waitADF(driver, 100);
		}
		if(header!=null){
			driver.findElement(By.xpath(".//tr[@title='Header']//input")).sendKeys(header);
		}
		driver.findElement(By.xpath(".//tr[contains(@title,'Columns')]//table//a")).click();
	    for(int i = 0; i < members.length; i++){
	        // walk through array of arguments
	    	BasicUtil.waitADF(driver, 100);
	    	driver.findElement(By.xpath(".//label[text()='"+members[i]+"']")).click();
	    }
	}
	public void populateKPIChartPrefrences(String kpiName, String header) throws Exception{


		if(header!=null){
			driver.findElement(By.xpath(".//tr[@title='Header']//input")).sendKeys(header);
		}
		driver.findElement(By.xpath(".//tr[contains(@title,'Key Performance Indicator')]//table//a")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//a[text()='More...']")));
		driver.findElement(By.xpath(".//td[contains(@title,'"+kpiName+"')]")).click();
		//Sdriver.findElement(By.xpath(".//button[contains(@id,'lovDialogId::ok')]")).click();
		

	}
	public void populatePieChartPrefrences(String analysisViewName, String header, String member) throws Exception{

		if(analysisViewName!=null){
			selectAV(analysisViewName);
			BasicUtil.waitADF(driver, 100);
		}
		if(header!=null){
			driver.findElement(By.xpath(".//tr[@title='Header']//input")).sendKeys(header);
		}
	    if(member != null){
	       
	    	Select pieMember  = new Select(driver.findElement(By.xpath(".//tr[@title='Pie Slices']//select")));
	    	BasicUtil.waitADF(driver, 100);
	    	pieMember.selectByVisibleText(member);
	    }
	}
	public void populateScatterChartPrefrences(String analysisViewName, String header, String memberXAxis, String memberYAxis) throws Exception{

		if(analysisViewName!=null){
			selectAV(analysisViewName);
			BasicUtil.waitADF(driver, 100);
		}
		if(header!=null){
			driver.findElement(By.xpath(".//tr[@title='Header']//input")).sendKeys(header);
		}
	    if(memberXAxis != null){
	       
	    	Select pieMember  = new Select(driver.findElement(By.xpath(".//tr[@title='X-Axis']//select")));
	    	BasicUtil.waitADF(driver, 100);
	    	pieMember.selectByVisibleText(memberXAxis);
	    }
	    if(memberYAxis != null){
		       
	    	Select pieMember  = new Select(driver.findElement(By.xpath(".//tr[@title='Y-Axis']//select")));
	    	BasicUtil.waitADF(driver, 100);
	    	pieMember.selectByVisibleText(memberYAxis);
	    }
	}
	public void populateBubbleChartPrefrences(String analysisViewName, String header, String memberXAxis, String memberYAxis,String bubbleSizeMember) throws Exception{

		if(analysisViewName!=null){
			selectAV(analysisViewName);
			BasicUtil.waitADF(driver, 100);
		}
		if(header!=null){
			driver.findElement(By.xpath(".//tr[@title='Header']//input")).sendKeys(header);
		}
	    if(memberXAxis != null){
	       
	    	Select pieMember  = new Select(driver.findElement(By.xpath(".//tr[@title='X-Axis']//select")));
	    	BasicUtil.waitADF(driver, 100);
	    	pieMember.selectByVisibleText(memberXAxis);
	    }
	    if(memberYAxis != null){
		       
	    	Select pieMember  = new Select(driver.findElement(By.xpath(".//tr[@title='Y-Axis']//select")));
	    	BasicUtil.waitADF(driver, 100);
	    	pieMember.selectByVisibleText(memberYAxis);
	    }
	    if(bubbleSizeMember != null){
		       
	    	Select pieMember  = new Select(driver.findElement(By.xpath(".//tr[@title='Bubble Size']//select")));
	    	BasicUtil.waitADF(driver, 100);
	    	pieMember.selectByVisibleText(bubbleSizeMember);
	    }
	}
	public void selectAV(String avName) throws Exception {
		By analViewSel = By.xpath(".//tr[@title='Analysis View']/td[2]/a[contains(.,'Select')]");
		driver.findElement(analViewSel).click();
		BasicUtil.wait(2);
		List<WebElement> elements = driver.findElements(By.xpath(".//td[contains(.,'"+avName+"') and @class='x10t']"));
		//.//td[contains(.,'Test AV 1') and @class='x10t']
		for(WebElement each : elements)
			{
				//System.out.println(each.getText() +" "+each.getTagName());
				if(each.isDisplayed())
					each.click();
			}
		
		driver.findElement(By.xpath(".//div[contains(@title,'Analysis')]//button[text()='OK']")).click();
	}
}
