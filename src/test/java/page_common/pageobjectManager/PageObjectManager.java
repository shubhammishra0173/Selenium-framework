package page_common.pageobjectManager;

import org.openqa.selenium.WebDriver;
import pageClass.amazon_commonPage;

public class PageObjectManager {
    private WebDriver driver;
    private amazon_commonPage amazoncommonpage;
    private amazon_Functions nxi;
    public PageObjectManager(WebDriver driver){
        this.driver=driver;
    }
    public amazon_Functions getAmazon(){
        if(this.nxi==null){
            this.nxi = new amazon_Functions(this.driver);
        }
        return this.nxi;
    }
    public amazon_commonPage getamazonommonpageObjects(){
        if(this.amazoncommonpage==null){
            this.amazoncommonpage=new amazon_commonPage(this.driver);
        }
        return this.amazoncommonpage;
    }
}
