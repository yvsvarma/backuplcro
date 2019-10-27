package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BasicUtil;

public class DimensionManagementPage {
	
	public WebDriver driver;
	public  Actions builder ;
	public WebDriverWait wait;
	private FuseTablePO tablePO;
	 
	public DimensionManagementPage(WebDriver driver){
		this.driver = driver;
		builder  = new Actions(driver);
		wait = new WebDriverWait(driver,30);
		tablePO = new FuseTablePO(driver,".//div[contains(@title,'Dimensions')]//table[boolean(@_rowcount)]");
	}
	
	public DimensionEditorPage openDimEditorByDimensionName(String dimensionName){
                BasicUtil.waitADF(driver, 100);
		tablePO.clickOnColumnNumberForRow(dimensionName, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h1[text()='"+dimensionName+"']")));
		return new DimensionEditorPage(driver);
	}
	
	
}
