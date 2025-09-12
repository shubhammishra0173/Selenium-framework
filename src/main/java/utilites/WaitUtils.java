package utilites;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {
    private WaitUtils(){

    }
    public static void waitForPageLoad(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30L));
        wait.until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver driver){

            return ((JavascriptExecutor) driver).executeScript("return dcument.readystate", new Object[0]).equals("complete");
        }
            });
    }
}
