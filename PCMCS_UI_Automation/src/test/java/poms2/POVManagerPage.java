package poms2;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BasicUtil;

public class POVManagerPage {

    private WebDriver driver;
    private WebDriverWait wait;
    String xpathForPovRow = ".//div[@title='Available Points of View']/div[2]/table//tr";
    By povTableRows = By.xpath(xpathForPovRow);
    By nextButton = By.xpath(".//button[text()='Next']");
    By cancelButton = By.xpath(".//button[text()='Cancel']");
    By yesButton = By.xpath(".//button[text()='Yes']");
    By noButton = By.xpath(".//button[text()='No']");
    By okButton = By.xpath(".//button[contains(.,'OK')]");
    By filterQueryAppnameInput = By.xpath(".//th[1]//input");
    By filterQueryByQueryNameInput = By.xpath(".//th[2]//input");
    By clearPOVButton = By.xpath("//img[@title='Clear Point of View Data']");
    By copyPOVButton = By.xpath("//img[@title='Copy Point of View Data']");
    By deletePOVButton = By.xpath("//img[@title='Delete Point of View']");
    By createPOVButton = By.xpath("//img[@title='Create Point of View']");
    By changePOVButton = By.xpath("//img[@title='Change Point of View State']");
    By refreshButton = By.xpath("//a/span[text()='Refresh']");

    public By createPOVConfirm = By.xpath("//div[text()='Create Point of View']");
    public By changePOVConfirm = By.xpath("//div[text()='Change Point Of View State']");
    public By deletePOVConfirm = By.xpath("//div[text()='Confirm Delete']");
    public By clearPOVConfirm = By.xpath("//div[text()='Clear Point of View']");
    public By copyPOVConfirm = By.xpath("//div[text()='Copy Point of View']");
    By yearSelect = By.xpath(".//label[text()='Year']/../..//select");
    By periodSelect = By.xpath(".//label[text()='Period']/../..//select");
    By scenarioSelect = By.xpath(".//label[text()='Scenario']/../..//select");
    By stateSelect = By.xpath(".//label[text()='Status']/../..//select");

    By manageRuleCheckBox = By.xpath(".//label[text()='Manage Rules']/..//input");
    By inputDataCheckBox = By.xpath(".//label[text()='Input Data']/..//input");
    By alloatedDataCheckBox = By.xpath(".//label[text()='Allocated Values']/..//input");
    By adjustmentDataCheckBox = By.xpath(".//label[text()='Adjustment Values']/..//input");
    //

    public POVManagerPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10000);
    }

    public boolean doesThisPOVExists(String povtext) {
        for (String pov : getAllPOVs()) {
            if (pov.equals(povtext)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getAllPOVs() {
        BasicUtil.waitADF(driver, 100);
        List<WebElement> allRows = driver.findElements(povTableRows);
        ArrayList<String> povRows = new ArrayList<String>();
        for (WebElement wb : allRows) {
            String povElement1 = wb.findElement(By.xpath("./td[1]")).getText();
            String povElement2 = wb.findElement(By.xpath("./td[2]")).getText();
            String povElement3 = wb.findElement(By.xpath("./td[3]")).getText();
            String povElement4 = wb.findElement(By.xpath("./td[4]")).getText();
            String totalPOVText = povElement1 + "_" + povElement2 + "_" + povElement3 + "_" + povElement4;
            povRows.add(totalPOVText);
        }
        return povRows;
    }

    public POVManagerPage seletRow(String povText) {
        int i = 1;
        for (String pov : getAllPOVs()) {
            if (pov.equals(povText)) {
                driver.findElement(By.xpath(xpathForPovRow + "[" + i + "]")).click();
                return this;
            } else {
                i++;
            }
        }
        return this;
    }

    public POVManagerPage clickYesButton() {
        BasicUtil.waitADF(driver, 100);
        driver.findElement(yesButton).click();
        return this;
    }

    public POVManagerPage clickRefreshButton() {
        BasicUtil.waitADF(driver, 100);
        driver.findElement(refreshButton).click();
        return this;
    }

    public POVManagerPage clickOKButton() {
        BasicUtil.waitADF(driver, 100);
        try {
            BasicUtil.findVisibleElement(driver, okButton).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //driver.findElement(okButton).click();
        return this;
    }

    public POVManagerPage clickNoButton() {
        BasicUtil.waitADF(driver, 100);
        driver.findElement(noButton).click();
        return this;
    }

    public POVManagerPage clickCreatePOVButton() {
        driver.findElement(createPOVButton).click();
        return this;
    }

    public POVManagerPage clickDeletePOVButton() {
        driver.findElement(deletePOVButton).click();
        return this;
    }

    public POVManagerPage clickCopyPOVButton() {
        driver.findElement(copyPOVButton).click();
        return this;
    }

    public POVManagerPage clickClearPOVButton() {
        driver.findElement(clearPOVButton).click();
        return this;
    }

    public POVManagerPage clickChangePOVButton() {
        driver.findElement(changePOVButton).click();
        return this;
    }

    public POVManagerPage selectYear(String year) {
        Select yearSelectDropDown = new Select(driver.findElement(yearSelect));
        yearSelectDropDown.selectByVisibleText(year);
        return this;
    }

    public POVManagerPage selectPeriod(String period) {
        Select periodSelectDropDown = new Select(driver.findElement(periodSelect));
        periodSelectDropDown.selectByVisibleText(period);
        return this;
    }

    public POVManagerPage selectScenario(String scenario) {
        Select scenarioSelectDropDown = new Select(driver.findElement(scenarioSelect));
        scenarioSelectDropDown.selectByVisibleText(scenario);
        return this;
    }

    public POVManagerPage selectState(String state) {
        Select stateSelectDropDown = new Select(driver.findElement(stateSelect));
        stateSelectDropDown.selectByVisibleText(state);
        return this;
    }

    public POVManagerPage clickManageRuleCheckBox() {
        driver.findElement(manageRuleCheckBox).click();
        return this;
    }

    public POVManagerPage clickInputDataCheckBox() {
        driver.findElement(inputDataCheckBox).click();
        return this;
    }

    public POVManagerPage clickAdjustmentDataCheckBox() {
        driver.findElement(adjustmentDataCheckBox).click();
        return this;
    }

    public POVManagerPage clickAllocatedDataCheckBox() {
        driver.findElement(alloatedDataCheckBox).click();
        return this;
    }
}
