package poms2;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import poms.FuseNavigatorPage;
import poms.IntelligencePage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;

import utils.BasicUtil;

public class ManageDBPage {

    private WebDriver driver;
    private WebDriverWait wait;
    //By createNewDBRadio = By.xpath(".//label[contains(.,'Create')]/../span/input");
    By createNewDBRadio = By.xpath(".//input[@id='f2:0:sbr3::content']");
    By updateDBRadio = By.xpath(".//label[contains(.,'Update')]/../span/input");
    By preserveDataCheckbox = By.xpath(".//label[contains(.,'Preserve')]/../span/input");
    By deplyNowButton = By.xpath(".//button[contains(.,'Deploy Now')]");
    By warningMessageDlg = By.xpath(".//div[text()='Warning']");
    By warningMessagetext = By.xpath(".//div[text()='The existing database and all its data will be deleted.']");
    By okButton = By.xpath(".//button[contains(.,'OK')]");
    By OKDeployDialogButton = By.xpath(".//div[contains(.,'Dimension changes')]/table//button[contains(.,'OK')]");
    By manageDBTab = By.xpath(".//a[contains(.,'Essbase Deploy')]");
    By dataLoadTab = By.xpath(".//a[contains(.,'Data Load')]");
    By administrationTab = By.xpath(".//a[contains(.,'Administration')]");
    //

    public ManageDBPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10000);
    }

    public ManageDBPage clickOnCreateNewDBRadio() {
        driver.findElement(createNewDBRadio).click();
        return this;
    }

    public ManageDBPage clickOnUpdateDBRadio(boolean preserveData) {
        driver.findElement(this.updateDBRadio).click();
        if (!preserveData) {
            driver.findElement(preserveDataCheckbox).click();
        }
        return this;
    }

    public ManageDBPage clickDeployNowButton() {
        driver.findElement(deplyNowButton).click();
        return this;
    }

    public ManageDBPage clickOK() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(warningMessageDlg));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(warningMessagetext));
        driver.findElement(okButton).click();
        return this;
    }

    public ManageDBPage clickDeployDlgOKButton() {
        BasicUtil.waitADF(driver, 100);
        driver.findElement(OKDeployDialogButton).click();
        return this;
    }

    public ManageDBPage navigateToManageDBTab() {
        driver.findElement(manageDBTab).click();
        return this;
    }
}
