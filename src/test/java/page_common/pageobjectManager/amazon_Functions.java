package page_common.pageobjectManager;

import org.openqa.selenium.WebDriver;
import pageClass.amazon_commonPage;

public class amazon_Functions {
WebDriver driver;
amazon_commonPage common;
public amazon_Functions(WebDriver driver){
    this.driver=driver;
    this.common=new amazon_commonPage(driver);

}
public amazon_Functions(){

}
public boolean amazonLogin(String url , String emailID,String EncryptedPssword){
    return this.common.amazonLogin(url,emailID,EncryptedPssword,false);
}
}
