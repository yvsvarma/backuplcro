package pagefragments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

/**
 * @author
 *
 */
public class UpdateDimensionPageFragment {
	private WebDriver driver;
	private WebDriverWait wait;
	private By addRowXPath = By.xpath(".//img[@title='Add Row']");
	private By deleteRowXPath = By.xpath(".//img[@title='Delete Row']");
	private By selectFileLink = By.xpath(".//a[text()='Select File']");
	private By updateDimensionCheckBox = By.xpath(".//label[text()='Update Dimensions']/..//input");
	private By validateDimensionsCheckBox = By.xpath(".//label[text()='Validate Dimensions']/..//input");
	private By impactAnalysisCheckBox = By.xpath(".//label[text()='Impact Analysis']/..//input");
	private By preUpdateCheckBox = By.xpath(".//label[text()='Pre Update Analysis']/..//input");
	//
	public UpdateDimensionPageFragment(WebDriver driver){
		this.driver=driver;
		wait = new WebDriverWait(driver,120);
	}
	/**
	 * @param isUpdateAnalysis
	 * @param isPreUpdateAnalysis
	 * @param isValidateDimension
	 * @param isImpactAnalysis
	 */
	public void setupUpdateOptions(boolean isUpdateAnalysis , boolean isPreUpdateAnalysis, boolean isValidateDimension, boolean isImpactAnalysis )
	{
		if(isUpdateAnalysis){
			driver.findElement(updateDimensionCheckBox).click();
		}else{
			if(isPreUpdateAnalysis){
				driver.findElement(preUpdateCheckBox).click();
				if(isValidateDimension)
					driver.findElement(validateDimensionsCheckBox).click();
				if(isImpactAnalysis)
					driver.findElement(impactAnalysisCheckBox).click();
			}
		}
	}
	
	public void clickAddRow(){
		driver.findElement(addRowXPath).click();
	}
	public void clickDeleteRow(){
		driver.findElement(deleteRowXPath).click();
	}
	public void clickSelectFileLink(){
		driver.findElement(selectFileLink).click();
	}
	public void clickOKButton(){
		try {
			BasicUtil.findVisibleElement(driver, By.xpath(".//button[text()='OK']")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//.//div[text()='The Update Dimensions has been submitted.']
}
