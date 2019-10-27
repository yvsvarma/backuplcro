package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import poms2.DataLoadPage;
import utils.BasicUtil;

public class FuseNavigatorPage {

    WebDriver driver;
    WebDriverWait wait;
    public By navigateIconBy = By.xpath(".//a[@title='Navigator']");
    By modelViewLink = By.xpath(".//span[text()='Model Views']/..");
    By povManagerLink = By.xpath(".//span[text()='Points of View']/..");
    By ruleBalancingLink = By.xpath(".//span[text()='Rule Balancing']/..");
    By manageDBLink = By.xpath(".//span[text()='Database']/..");
    By manageQueriesLink = By.xpath(".//span[text()='Manage Queries']/..");
    By dashboardLink = By.xpath(".//span[text()='Dashboards']/..");
    By consoleLink = By.xpath(".//span[text()='Application']/..");
    By intelligenceLink = By.xpath(".//span[text()='Intelligence']/..");
    By signOutLink = By.xpath(".//a[contains(.,'Sign Out')]");
    By OKButton = By.xpath(".//button[contains(.,'OK')]");
    By signOutOKButton = By.xpath(".//div[contains(@id,'logout')]//button[text()='OK']");
    By jobLibLink = By.xpath(".//span[text()='Job Library']/..");
    By sysReportsLink = By.xpath(".//span[text()='System Reports']/..");
    By dataloadLink = By.xpath(".//a[contains(.,'Data Load')]");
    By manageCalculationLink = By.xpath(".//span[text()='Calculation']/..");
    By manageRulesLink = By.xpath(".//span[text()='Rules']/..");
    By reportsLink = By.xpath(".//span[text()='Reports']/..");
    By modelValidationLink = By.xpath(".//span[text()='Model Validation']/..");
    By dimensionManagementLink = By.xpath(".//span[text()='Dimension Management']/..");
    By homePagelink = By.xpath("//img[@title='Home']");
    //.//a[contains(.,'Sign Out')]
    //.//tr/td[contains(.,'Do you want to end your session?')]

    public FuseNavigatorPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 100);
    }

    public FuseNavigatorPage clickNavigateButton() {
        BasicUtil.waitADF(driver, 200);
        WebElement element = driver.findElement(navigateIconBy);
        Actions action = new Actions(driver);
        action.moveToElement(element).build().perform();
        driver.findElement(navigateIconBy).click();
        BasicUtil.wait(5);
        return this;
    }

    public FuseNavigatorPage ClickSignOutDropDown(String userName) {
        By logoutImage = By.xpath(".//a[@id='cil12']/img");
        driver.findElement(logoutImage).click();
        return this;
    }

    public FuseNavigatorPage clickSignOutLink() throws InterruptedException {

        //BasicUtil.waitADF(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(signOutLink));
        driver.findElement(signOutLink).click();
        return this;
    }

    public FuseLoginPage clickOKButton() throws InterruptedException {
        BasicUtil.waitADF(driver, 20);
        driver.findElement(OKButton).click();
        return new FuseLoginPage(driver);
    }

    public FuseLoginPage clickSignoutOKButton() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(signOutOKButton));
        BasicUtil.wait(5);
        BasicUtil.highlightElement(driver, driver.findElement(signOutOKButton));
        driver.findElement(signOutOKButton).click();
        BasicUtil.wait(5);
        return new FuseLoginPage(driver);
    }

    
    public DataLoadPage navigateToDataLoad() {
        clickNavigateButton();
        driver.findElement(manageDBLink).click();
        //validations needs to be removed.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h1[contains(text(),'Database')]")));
        driver.findElement(dataloadLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//h2[contains(text(),'Essbase Load Options')]")));
        return new DataLoadPage(driver);
    }

   
    public JobLibPage navigateToJobLibrary() {
        clickNavigateButton();
        driver.findElement(jobLibLink).click();
        //validations needs to be removed.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//span[contains(text(),'Job Library')]")));
        return new JobLibPage(driver);
    }

    public ConsolePage navigateToConsole() throws InterruptedException {
        clickNavigateButton();
        driver.findElement(consoleLink).click();

        //validations needs to be removed.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//span[contains(text(),'Applications')]")));
        return new ConsolePage(driver);
    }

    
    public FuseLoginPage signout() throws InterruptedException {
        this.ClickSignOutDropDown(System.getProperty("id"));
        BasicUtil.log("Clicking signout button.");
        this.clickSignOutLink();
        BasicUtil.log("Signed out.");
        return this.clickSignoutOKButton();
    }

    public DimensionManagementPage navigateToDiemnsionManagement() {
        clickNavigateButton();
        driver.findElement(dimensionManagementLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//span[contains(text(),'Dimension')]")));
        return new DimensionManagementPage(driver);
    }

    public FuseNavigatorPage clickHomeButton() {
        BasicUtil.highlightElement(driver, driver.findElement(this.homePagelink));
        driver.findElement(this.homePagelink).click();
        BasicUtil.waitADF(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='Current user image']")));
        return this;

    }

}
