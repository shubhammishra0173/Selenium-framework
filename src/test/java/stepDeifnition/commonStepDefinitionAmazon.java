package stepDeifnition;

import io.cucumber.java.en.Given;
import pageClass.amazon_commonPage;
import page_common.pageobjectManager.amazon_Functions;
import singletone.SharedVariables;
import stepDefinition_common.AbstractSteps;
import utilites.Screenshots;

public class commonStepDefinitionAmazon extends AbstractSteps {

private boolean amazonLogin=false;
private final SharedVariables sharedVariables = SharedVariables.getInstance();
public amazon_commonPage getCommonObjects(){
return this.getPageObjectManager().getamazonommonpageObjects();
}
public amazon_Functions getAmazon(){return  this.getPageObjectManager().getAmazon();}

    @Given("user login into the amazon site {} with {},{}")
    public void userLoginIntoTheAmazonSiteUrlWithUserIDPassword(String url , String userName, String encryptdPassword) {
this.sharedVariables.setAmazonSiteLoggedIn(false);
this.startDriver();
if(this.getAmazon().amazonLogin(this.getPropertyValue(url),this.getPropertyValue(userName),this.getPropertyValue(encryptdPassword))){

        Screenshots.addStepInReport(true , "login successful");
        this.sharedVariables.setAmazonSiteLoggedIn(true);
    }else{
        Screenshots.addStepWithScreenshotInReport(false,this.getDriver(),"Login unsuccessful ");

    }
}


}
