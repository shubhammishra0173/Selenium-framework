package page_common;

import org.openqa.selenium.By;

public class Locator {
    public By by;
    public String details;
    public Locator(By by, String locatordetails){
        this.by=by;
        this.details=locatordetails;
    }

}
