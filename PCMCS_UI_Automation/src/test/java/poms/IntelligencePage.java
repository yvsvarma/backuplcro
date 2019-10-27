package poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BasicUtil;

public class IntelligencePage {

    private By analysisViewSubPageLink = By.xpath(".//img[@title='Analysis Views']/..");
    private By scatterAnalysisSubPageLink = By.xpath(".//img[@title='Scatter Analysis']/..");
    private By profitCurvesSubPageLink = By.xpath(".//img[@title='Profit Curves']/..");
    private By KPISubPageLink = By.xpath(".//img[@title='Key Performance Indicators']/..");
    public WebDriver driver;
    public Actions builder;
    public WebDriverWait wait;

    public IntelligencePage(WebDriver driver) {
        this.driver = driver;
        builder = new Actions(driver);
        wait = new WebDriverWait(driver, 30);
    }

    public AnalysisViewPage clickAVSubPageLink() {
        BasicUtil.waitADF(driver, 100);
        driver.findElement(analysisViewSubPageLink).click();
        return new AnalysisViewPage(driver);
    }

    public ProfitCurvesPage clickProfitCurvesSubPageLink() {
        BasicUtil.waitADF(driver, 100);
        driver.findElement(profitCurvesSubPageLink).click();
        return new ProfitCurvesPage(driver);
    }

    public KPIPage clickKPISubPageLink() {
        BasicUtil.waitADF(driver, 100);
        driver.findElement(KPISubPageLink).click();
        return new KPIPage(driver);
    }

    public ScatterGraphPage clickScatterSubPageLink() {
        BasicUtil.waitADF(driver, 100);
        driver.findElement(scatterAnalysisSubPageLink).click();
        return new ScatterGraphPage(driver);
    }
}
