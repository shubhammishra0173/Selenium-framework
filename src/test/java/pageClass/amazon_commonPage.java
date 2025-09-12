package pageClass;

import org.example.cucumber.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import page_common.CommonActionsForXpath;
import utilites.AssertionLibrary;
import utilites.Screenshots;
import utilites.WaitUtils;

public class amazon_commonPage extends CommonActionsForXpath {
    WebDriver driver;
    protected By enteremailorPhone= By.xpath("//input[contains(@id,'ap_email_login')]");
    protected By hoverMousesiginPopup = By.xpath("//span[contains(text(),'Account & Lists')]");
    protected By signInbutton= By.xpath("//span[contains(text(),'Sign in')]");
    protected By continueBtn = By.xpath("//span[contains(text(),'Continue')]");
    protected By passwordInput=By.xpath("//input[contains(@id,'ap_password')]");
    protected By homePageTab= By.xpath("//a[contains(@aria-label,'Amazon.in Prime')]");
    public amazon_commonPage(WebDriver driver) {
        super(driver);
        this.driver=driver;
    }
    public boolean amazonLogin(String URL, String userName,String password){
        boolean loggedIn=false;
        try{
            if(URL!=null){
                this.navigateTo(URL);
            }
            WaitUtils.waitForPageLoad(this.driver);
           boolean loginPageLoaded=this.loginPage();
           if(loginPageLoaded){
               this.setuserID(userName);

               this.clickElement(continueBtn,"continue button clicked");
               this.setEncryptedPassword(password);
               this.clickElement(signInbutton,"user clicked on signin button");
               WaitUtils.waitForPageLoad(this.driver);
               loggedIn=this.postLoginCheck();
           }else{
               Log.info("Home page not loaded successfully");
               Screenshots.addFailureStepWithScreenshotInRpoert(this.driver,"Home page not loaded ");
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return loggedIn;
    }

    private boolean postLoginCheck() {
        boolean loggedIn =false;
        if(this.checkHomePage()){
            Log.info("Logged in successfully");
            loggedIn=true;
        }
        return loggedIn;
    }

    private boolean checkHomePage() {
   return this.waiforElementPresent(this.homePageTab,60);
    }


    private void setEncryptedPassword(String password) {
   try{
       this.setPassword(this.passwordInput,password);

   }catch (Exception e){
       e.printStackTrace();
   }
    }

    private void setuserID(String userName) {
   this.setInputValue(this.enteremailorPhone,userName, AssertionLibrary.Screenshot.NOT_REQUIRED);
    }

    private boolean loginPage() {
        boolean loaded =false;
        if(this.waitforElementVisible(this.enteremailorPhone,5)){
            loaded=true;
            Screenshots.addStepInReport(true,"login popup available");
        }else if(this.waitforElementVisible(this.hoverMousesiginPopup,5));
        this.clickElement(this.hoverMousesiginPopup,"clicked on mouse hovering");{
            this.clickElement(signInbutton,"signInbutton clicked");
            if(this.waitforElementVisible(this.enteremailorPhone,2)) loaded=true;
        }
        return loaded;
    }
}
