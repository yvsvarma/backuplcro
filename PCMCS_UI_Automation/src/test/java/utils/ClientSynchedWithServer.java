package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ClientSynchedWithServer implements ExpectedCondition<Boolean> {
    // return false if AdfPage object and functions do not exist
    // if they do exist, return true if page is fully loaded and ready or reason why this is not completed yet
    String js =
        "return typeof AdfPage !== 'undefined' && " + 
        "typeof AdfPage.PAGE !== 'undefined' && " +
        "typeof AdfPage.PAGE.isSynchronizedWithServer === 'function' && " +
        "(AdfPage.PAGE.isSynchronizedWithServer() || " +
        "(typeof AdfPage.PAGE.whyIsNotSynchronizedWithServer === 'function' && " +
        "AdfPage.PAGE.whyIsNotSynchronizedWithServer()))";
 
    @Override
    public Boolean apply(WebDriver driver) {
        JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
        Object result = jsDriver.executeScript(js);
        //BasicUtil.log("[ADFJavaSriptClientStatus]: client ready? : " + result);
        return Boolean.TRUE.equals(result);
    }
}