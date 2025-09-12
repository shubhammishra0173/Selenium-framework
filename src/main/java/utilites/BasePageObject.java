package utilites;


import org.example.cucumber.Log;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.IOException;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

import static utilites.AssertionLibrary.Screenshot;

public class BasePageObject {

    public static  final String DESEDE_ENCRYPTION_SCHEME="DEsede";
    public static final Logger logger = Logger.getLogger(BasePageObject.class.getName());
    public static final int DEFAULT_IMPLICIT_WAIT=0;
    public static final String SET_INPUT="Set input: ";
    public static final String CLICK_ACTION="Click Action: ";
    public static final String DROPDOWN="Selected value from Dropdown ";
    public static final String SET_INPUT_COMMAND="arguments[0].value='%s'; ";
    public static final String CLICK_COMMAND="arguments[0].click() ";

    public static final String JS_DISPLAY_COMMAND="arguments[0].style.display='block'; ";
    public static final String CLICK="Click: ";
    public static final String UNICODE_FORMAT="UTF8: ";
    public static final String BEFORE_CLICK="Before Click ";
    private  final JavascriptExecutor javascriptExecutor;
    private final int implicitWaitTime = ConfigProvider.getAsInt("IMPLICIT_WAIT");
    byte[]arrayBytes;
    SecretKey key;
    Actions action;
    Alert alert;
    private FluentWait<WebDriver> fluentWait;
    private WebDriver driver;

    public BasePageObject(WebDriver webdriver){
        this.driver =webdriver;
        Duration pollingInterval = Duration.ofMillis((long) ConfigProvider.getAsInt("POLLING_INTERVAL"));
        Duration fluentWaitDuration = Duration.ofSeconds((long) ConfigProvider.getAsInt("FLUENT_WAIT"));
        this.fluentWait= (new FluentWait(this.driver)).withTimeout(fluentWaitDuration).pollingEvery(pollingInterval).ignoring(StaleElementReferenceException.class).ignoring(ElementNotInteractableException.class).ignoring(NoSuchElementException.class);
        this.javascriptExecutor= (JavascriptExecutor) this.driver;
        PageFactory.initElements(this.driver,this);
        this.action=new Actions(this.driver);

    }
    public BasePageObject(){
        this.javascriptExecutor=null;
    }
    public WebDriver getDriver(){
        return this.driver;
    }
    public void get(String url){
        this.driver.get(url);
        Screenshots.addStepWithScreenshotInReport(this.driver,"Application launched: <a href =\""+url+"\">"+url+"</a>");
    }
    public void navigateTo(String url){
        this.driver.navigate().to(url);
        Screenshots.addStepWithScreenshotInReport(this.driver,"Application launched: <a href =\""+url+"\">"+url+"</a>");
    }
public void setImplicitWait(int duration){
        this.driver.manage().timeouts().implicitlyWait((long) duration, TimeUnit.SECONDS);


}
protected List<WebElement> getElements(String locator) {
    return (List) this.fluentWait.until((driver1) -> {
        return this.driver.findElements(By.xpath(locator));
    });
}
    protected List<WebElement> getElements(By by){
        return (List) this.fluentWait.until((driver1)->{
            return this.driver.findElements(by);
        });
    }
    protected WebElement getElement(String locator) {
        return (WebElement) this.fluentWait.until((driver1) -> {
            return this.driver.findElement(By.xpath(locator));
        });
    }

    protected WebElement getElement(By by) {
        return (WebElement) this.fluentWait.until((driver1) -> {
            return this.driver.findElement(by);
        });
    }
    protected boolean isElementOnPage(String locator){
        this.setImplicitWait(0);
        boolean flag = !this.getElements(locator).isEmpty();
        this.setImplicitWait(ConfigProvider.getAsInt("IMPLICITLY_WAIT"));
        return flag;
    }
    protected boolean isElementOnPage(By by){
        this.setImplicitWait(0);
        boolean flag = !this.getElements(by).isEmpty();
        this.setImplicitWait(ConfigProvider.getAsInt("IMPLICITLY_WAIT"));
        return flag;
    }
    protected boolean isEnabled(String locator){
        List<WebElement> elementList = this.getElements(locator);
        return !elementList.isEmpty()?((WebElement)elementList.get(0)).isEnabled():false;
    }
    protected boolean isEnabled(By by){
        List<WebElement> elementList = this.getElements(by);
        return !elementList.isEmpty()?((WebElement)elementList.get(0)).isEnabled():false;
    }
    protected boolean isDisplayed(By by){
        List<WebElement> elementList = this.getElements(by);
        return !elementList.isEmpty()?((WebElement)elementList.get(0)).isDisplayed():false;
    }
    protected boolean isDisplayed(String locator){
        List<WebElement> elementList = this.getElements(locator);
        return !elementList.isEmpty()?((WebElement)elementList.get(0)).isDisplayed():false;
    }

    protected boolean isSelected(By by){
        List<WebElement> elementList = this.getElements(by);
        return !elementList.isEmpty()?((WebElement)elementList.get(0)).isSelected():false;
    }
    protected boolean isSelected(String locator){
        List<WebElement> elementList = this.getElements(locator);
        return !elementList.isEmpty()?((WebElement)elementList.get(0)).isSelected():false;
    }
    protected int getElementsSize(By by){
        return  this.isElementOnPage(by)?this.getElements(by).size():0;
    }
    protected int getElementsSize(String locator){
        return  this.isElementOnPage(locator)?this.getElements(locator).size():0;
    }
    protected void setInputValue(String locator,String value){
        WebElement element = this.getElement(locator);
        element.clear();
        element.sendKeys(new CharSequence[]{value});
        Screenshots.addStepInReport("Set input: "+value);
    }
    protected void setInputValue(By locator,String value){
        WebElement element = this.getElement(locator);
        element.clear();
        element.sendKeys(new CharSequence[]{value});
        Screenshots.addStepInReport("Set input: "+value);
    }
    protected void setInputValueJS(String locator,String value){
        WebElement element = this.getElement(locator);
        element.clear();
       this.javascriptExecutor.executeScript(String.format("arguments[0].value='%s';",value),new Object[]{element});
        Screenshots.addStepInReport("arguments[0].value='%s';"+value);
    }
    protected void setInputValueJS(By locator,String value){
        WebElement element = this.getElement(locator);
        element.clear();
        this.javascriptExecutor.executeScript(String.format("arguments[0].value='%s';",value),new Object[]{element});
        Screenshots.addStepInReport("arguments[0].value='%s';"+value);
    }
    protected void clearElement(String locator){
        this.getElement(locator).clear();
    }
    protected void clearElement(By locator){
        this.getElement(locator).clear();
    }

protected String getText(String locator){
        return this.getElement(locator).getText();
}
    protected String getText(By locator){
        return this.getElement(locator).getText();
    }
    protected String getAttribute(String locator,String attribute){
        return this.getElement(locator).getAttribute(attribute);
    }
    protected String getAttribute(By locator,String attribute){
        return this.getElement(locator).getAttribute(attribute);
    }
    protected String getCSSValue(String locator,String attribute){
        return this.getElement(locator).getCssValue(attribute);
    }
    protected String getCSSValue(By locator,String attribute){
        return this.getElement(locator).getCssValue(attribute);
    }
    protected void clickElementJS(String loctor){
        Screenshots.addFailureStepWithScreenshotInRpoert(this.driver,"Before click: ");
        this.javascriptExecutor.executeScript("arguments[0].click()",new Object[]{this.getElement(loctor)});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
    }
    protected void clickElementJS(By loctor){
        Screenshots.addFailureStepWithScreenshotInRpoert(this.driver,"Before click: ");
        this.javascriptExecutor.executeScript("arguments[0].click()",new Object[]{this.getElement(loctor)});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
    }
    protected void clickElementJS(String loctor,String description){
        Screenshots.addFailureStepWithScreenshotInRpoert(this.driver,"Before click: ");
        this.javascriptExecutor.executeScript("arguments[0].click()",new Object[]{this.getElement(loctor)});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click :"+description);
    }

    protected void clickElementJS(By loctor,String description){
        Screenshots.addFailureStepWithScreenshotInRpoert(this.driver,"Before click: ");
        this.javascriptExecutor.executeScript("arguments[0].click()",new Object[]{this.getElement(loctor)});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click :"+description);
    }
    protected void makeElementVisibleAndClick(String locator){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before click:");
        WebElement element = this.getElement(locator);
        this.javascriptExecutor.executeScript("arguments[0].style.display='block';",new Object[]{element});
        this.javascriptExecutor.executeScript("arguments[0].click();",new Object[]{element});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");

    }
    protected void makeElementVisibleAndClick(By locator){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before click:");
        WebElement element = this.getElement(locator);
        this.javascriptExecutor.executeScript("arguments[0].style.display='block';",new Object[]{element});
        this.javascriptExecutor.executeScript("arguments[0].click();",new Object[]{element});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");

    }
    protected void makeElementVisibleAndClick(String locator,String descriptions){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before click:");
        WebElement element = this.getElement(locator);
        this.javascriptExecutor.executeScript("arguments[0].style.display='block';",new Object[]{element});
        this.javascriptExecutor.executeScript("arguments[0].click();",new Object[]{element});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click "+descriptions);

    }
    protected void makeElementVisibleAndClick(By locator,String descriptions){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before click:");
        WebElement element = this.getElement(locator);
        this.javascriptExecutor.executeScript("arguments[0].style.display='block';",new Object[]{element});
        this.javascriptExecutor.executeScript("arguments[0].click();",new Object[]{element});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click "+descriptions);

    }
    public void clickElement(String locator){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before click:");
        this.getElement(locator).click();
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
    }
    public void clickElement(By locator){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before click:");
        this.getElement(locator).click();
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
    }
    public void clickElement(String locator,String description){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before click: "+description);
        this.getElement(locator).click();
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click: "+description);
    }
    public void clickElement(By locator,String description){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before click: "+description);
        this.getElement(locator).click();
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click: "+description);
    }
    public void clickElement(String locator,String description,AssertionLibrary.Screenshot screenshot) {
        this.getElement(locator).click();
        if (screenshot.equals(Screenshot.REQUIRED)) {
            Screenshots.addStepWithScreenshotInReport(this.driver, " click: " + description);
        } else {
            Screenshots.addStepInReport("Click: " + description);
        }
    }

    public void clickElement(By locator,String description,AssertionLibrary.Screenshot screenshot) {
        this.getElement(locator).click();
        if (screenshot.equals(Screenshot.REQUIRED)) {
            Screenshots.addStepWithScreenshotInReport(this.driver, " click: " + description);
        } else {
            Screenshots.addStepInReport("Click: " + description);
        }
    }
    protected void shiftFocusAway(String locator) {
        this.getElement(locator).sendKeys(new CharSequence[]{Keys.TAB});


    }
    protected void shiftFocusAway(By locator) {
        this.getElement(locator).sendKeys(new CharSequence[]{Keys.TAB});


    }
    protected String getPageSource(){
        return this.driver.getPageSource();

    }
    protected boolean closeWindow(){
        return this.closeWindow(this.driver.getWindowHandle());
    }
    protected boolean closeWindow(String windowsId){
        boolean ret=false;
        String before =null;
        Set<String> handles = this.driver.getWindowHandles();
        Iterator var6  = handles.iterator();
        while (var6.hasNext()){
            String handle = (String) var6.next();
            if(handle.trim().equalsIgnoreCase(windowsId))
            {
                try{
                    this.driver.switchTo().window(windowsId);
                    this.driver.close();
                    if(before!=null){
                        this.driver.switchTo().window(before);
                    }
                    ret =true;
                    break;
                }catch (Exception var9){
                    logger.info(var9.toString());
                }
            }else{
                before =handle;
            }
        }
        return ret;
    }
    protected void scrollintoview(String locator){
        this.javascriptExecutor.executeScript("arguments[0].scrollintoView();",new Object[]{this.getElement(locator)});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Scroll page: ");
    }
    protected String switchWindow(){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(10L));
        String current = this.driver.getWindowHandle();
        try{
            wait.until((d)->{
                assert  d !=null;
                return d.getWindowHandles().size()!=1;
            });
        }catch (TimeoutException var5){
            return this.driver.getWindowHandle();
        }
        Set<String>handles = this.driver.getWindowHandles();
        handles.remove(current);
        String newTab= (String) handles.iterator().next();
        this.driver.switchTo().window(newTab);
        return newTab;
    }
    public String switchWindow(String node){
        String winID = this.driver.getWindowHandle();
        if(node!=null &&!node.trim().isEmpty()){
            if (node.trim().equalsIgnoreCase("root") || node.trim().equalsIgnoreCase("parent")) {
            this.driver.switchTo().window(winID);
            return winID;
            }if(node.trim().equalsIgnoreCase("child")){
                return this.switchWindow();
            }
            }else{
            Screenshots.addFailureStepWithScreenshotInRpoert(this.driver,"A window node must be passed");
        }if(!this.isWindowExist(node)){
            Screenshots.addFailureStepWithScreenshotInRpoert(this.driver,"A window node must be passed the current "+node+"is not passed");
        }this.driver.switchTo().window(node);
        return node;

        }
        public boolean isWindowExist(String windowsId){
        boolean found =false;
        Set<String> handles = this.driver.getWindowHandles();
        Iterator var4 = handles.iterator();
        while(var4.hasNext()){
            String win =(String) var4.next();
            if(win.trim().equalsIgnoreCase(windowsId)){
                found=true;
                break;
            }
        }
        return found;
        }
        protected void setPassword(By by ,String encryptedString){
        String decryptString = this.getDecryptedText(encryptedString);
        WebElement element = this.getElement(by);
        element.clear();
        element.sendKeys(new CharSequence[]{decryptString});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Set input: "+encryptedString);


        }
    protected void setPassword(String by ,String encryptedString){
        String decryptString = this.getDecryptedText(encryptedString);
        WebElement element = this.getElement(by);
        element.clear();
        element.sendKeys(new CharSequence[]{decryptString});
        Screenshots.addStepWithScreenshotInReport(this.driver,"Set input: "+encryptedString);


    }
    protected String setPassword(String encryptedSetting){
        return this.getDecryptedText(encryptedSetting);
    }
    protected  String getDecryptedText(String enryptedString) {
        String decryptText = null;
        try {
String myEncriptKey= "WelcomeToMyAcademy";
String myEncryptionScheme= "DESede";
this.arrayBytes=myEncriptKey.getBytes("UTF8");
            KeySpec ks = new DESedeKeySpec(this.arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(myEncryptionScheme);
            Cipher cipher= Cipher.getInstance(myEncryptionScheme);
            this.key=skf.generateSecret(ks);
            cipher.init(2,this.key);
            byte[]encryptedText = Base64.decodeBase64(enryptedString);
            byte[]plainText= cipher.doFinal(encryptedText);
            decryptText=new String(plainText);
        }catch (Exception var10){
            var10.printStackTrace();
        }
        return decryptText;
    }

    public void dropdDownValue(String xpath, String value){
        Select oSelection = new Select(this.driver.findElement(By.xpath(xpath)));
        oSelection.selectByVisibleText(value);
    }
    public void dropdDownValue(String xpath, String value,AssertionLibrary.Screenshot screenshot){
        Select oSelection = new Select(this.driver.findElement(By.xpath(xpath)));
        oSelection.selectByVisibleText(value);
        if(screenshot.equals(Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(this.driver,"Selected the value from dropdown "+value);
        logger.info("Dropdown value selected");
        }
    }
    protected String identity(By by){
        WebElement element = this.getElement(by);
        String idv= element.getText();
        String before = idv.substring(idv.lastIndexOf(" "));
        String trimmedString=before.substring(0,before.length()-1).trim();
        Screenshots.addStepWithScreenshotInReport(this.driver,"Set input: "+trimmedString);
        return trimmedString;
    }
    public void setExplicitWaitVisible(String locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));

    }
    public void setExplicitWaitVisible(By locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

    }
    public void setExplicitWaitclickable(String locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));

    }

    public void setExplicitWaitclickable(By locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.elementToBeClickable(locator));

    }public void setExplicitWaitPresence(String locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));

    }
    public void setExplicitWaitPresence(By locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));

    }
    public void setExplicitWaitSelected(String locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.elementToBeSelected(By.xpath(locator)));

    }
    public void setExplicitWaitSelected(By locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.elementToBeSelected(locator));

    }
    public void setExplicitWaitTextPresent(String locator,long seconds,String text){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(locator),text));

    }
    public void setExplicitWaitTextPresent(By locator,long seconds,String text){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.textToBePresentInElementLocated((locator),text));

    }
    public void waitForAbsence(String locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));

    }
    public void waitForAbsence(By locator,long seconds){
        WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));

    }
    public void switchTOiFrame(int index){
        this.driver.switchTo().frame(index);
    }
    public void switchTOiFrame(String name){
        this.driver.switchTo().frame(name);
    }
    public void switchTOiFrame(WebElement locator){
        this.driver.switchTo().frame(locator);
    }
    public void switchToDefaultFrame(){
        this.driver.switchTo().defaultContent();
    }
    public void switchToParentFrame(){
        this.driver.switchTo().parentFrame();
    }
    public void acceptAlert(){
        this.alert=this.driver.switchTo().alert();
        this.alert.accept();
    }
    public void dismisAlert(){
        this.alert=this.driver.switchTo().alert();
        this.alert.dismiss();
    }
    public String getAlertText(){
        this.alert=this.driver.switchTo().alert();
       return this.alert.getText();
    }
    public void sendAlertText(String textinput){
        this.alert=this.driver.switchTo().alert();
        this.alert.sendKeys(textinput);
    }
    public void clickAndHold(){
        this.action.clickAndHold().build().perform();
    }
    public void clickAndHold(WebElement locator){
        this.action.clickAndHold(locator).build().perform();
    }

    public void release(){
        this.action.release().build().perform();
    }
    public void release(WebElement locator){
        this.action.release(locator).build().perform();
    }
    public void rightclick(){
        this.action.contextClick().build().perform();
    }
    public void rightclick(WebElement locator){
        this.action.contextClick(locator).build().perform();
    }
    public void doubleClick(){
        this.action.doubleClick().build().perform();
    }
    public void doubleClick(WebElement locator){
        this.action.doubleClick(locator).build().perform();
    }

public void dragAnddropElement(WebElement source,WebElement target){
        this.action.dragAndDrop(source,target).build().perform();
}
    public void dragAnddropElement(WebElement source,int xoffset,int yoffset){
        this.action.dragAndDropBy(source,xoffset,yoffset).build().perform();
    }
    public void moveToElement(WebElement element){
        this.action.moveToElement(element).build().perform();
    }
    public void moveToElement(WebElement source,int xoffset,int yoffset){
        this.action.moveToElement(source,xoffset,yoffset).build().perform();
    }
    public void pageRefresh(){
        this.driver.navigate().refresh();
    }
    public static void clearExeDrivers(){
        if(System.getProperty("os.name").contains("Window")){
            if(ConfigProvider.getAsString("browser").equalsIgnoreCase("chrome")){
                executeOnCMD("taskkill/F/IM chromedriver.exe /T");
            }
            if(ConfigProvider.getAsString("browser").equalsIgnoreCase("edge")){
                executeOnCMD("taskkill/F/IM msedgedriver.exe /T");
            }
            logger.info("driver.exe process killed in background");
        }
    }
    public static void executeOnCMD(String command){
        if(System.getProperty("os.name").contains("windows")){
            try{
                Runtime.getRuntime().exec(command);
                logger.info(command+" executed ");
            }catch (IOException var2){
                throw new RuntimeException(var2);
            }
        }
    }
    public void clickShadowElement(String cssHost,String cssValue,AssertionLibrary.Screenshot screenshot){
        try{
            SearchContext shadow = this.shadowElementInitialize(cssHost);
            this.action.click(shadow.findElement(By.cssSelector(cssValue))).click().build().perform();
            this.setImplicitWait(1);
            if(screenshot.equals(screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
                logger.info("logger action performed");
            }
        }catch (Exception var5){
            logger.info("exception message for click shadow element"+var5);
        }
    }

    public void clickShadowElement(AssertionLibrary.Screenshot screenshot,String cssValue){
        try{
            SearchContext shadow = this.shadowElementInitialize("nxn-shell");
            this.action.click(shadow.findElement(By.cssSelector(cssValue))).click().build().perform();
            this.setImplicitWait(1);
            if(screenshot.equals(screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
                logger.info("logger action performed");
            }
        }catch (Exception var4){
            logger.info("exception message for click shadow element"+var4);
        }
    }
    public void moveToShadowElement(String cssHost,AssertionLibrary.Screenshot screenshot,String cssValue){
        try{
            SearchContext shadow = this.shadowElementInitialize(cssHost);
            this.action.moveToElement(shadow.findElement(By.cssSelector(cssValue))).click().build().perform();
            this.setImplicitWait(1);
            if(screenshot.equals(screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
                logger.info("logger action performed");
            }
        }catch (Exception var5){
            logger.info("exception message for click shadow element"+var5);
        }
    }
    public void moveToShadowElement(String cssValue,AssertionLibrary.Screenshot screenshot){
        try{
            SearchContext shadow = this.shadowElementInitialize("nxn-shell");
            this.action.moveToElement(shadow.findElement(By.cssSelector(cssValue))).click().build().perform();
            this.setImplicitWait(1);
            if(screenshot.equals(Screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
                logger.info("navigate tot element");
            }
        }catch (Exception var5){
            logger.info("exception message for move shadow element"+var5);
        }
    }

    public void setinputValueShadowElement(String cssValue,String cssHost,String text,AssertionLibrary.Screenshot screenshot){
        try{
            SearchContext shadow = this.shadowElementInitialize(cssHost);
            this.action.click(shadow.findElement(By.cssSelector(cssValue))).sendKeys(new CharSequence[]{text}).build().perform();
            this.setImplicitWait(1);
            if(screenshot.equals(Screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
                logger.info("logger action performed");
            }
        }catch (Exception var5){
            logger.info("exception message for click shadow element"+var5);
        }
    }
    public void setinputValueShadowElement(String cssValue,String text,AssertionLibrary.Screenshot screenshot){
        try{
            SearchContext shadow = this.shadowElementInitialize("nxn-shell");
            this.action.click(shadow.findElement(By.cssSelector(cssValue))).sendKeys(new CharSequence[]{text}).build().perform();
            this.setImplicitWait(1);
            if(screenshot.equals(Screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Set input");
                logger.info("Text entered for :"+text);
            }
        }catch (Exception var5){
            logger.info("exception message for click shadow element"+var5);
        }
    }
    public String  getTexShadowElement(String cssHost,String cssValue,AssertionLibrary.Screenshot screenshot){
       String textValue ="";
        try{
           SearchContext shadow=   this.shadowElementInitialize(cssHost);
            textValue=shadow.findElement(By.cssSelector(cssValue)).getText();
            this.setImplicitWait(1);
            if(screenshot.equals(Screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
                logger.info("logger action performed");
            }
        }catch (Exception var5){
            logger.info("exception message for click shadow element"+var5);
        }
        return textValue;
    }
    public String  getTexShadowElement(String cssValue,AssertionLibrary.Screenshot screenshot){
        String textValue ="";
        try{
            SearchContext shadow  = this.shadowElementInitialize("nxn-shell");
            textValue=shadow.findElement(By.cssSelector(cssValue)).getText();
            this.setImplicitWait(1);
            if(screenshot.equals(Screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
                logger.info("logger action performed");
            }
        }catch (Exception var5){
            logger.info("exception message for click shadow element"+var5);
        }
        return textValue;
    }

    public boolean  isShadowElementDisplayed(String cssHost,String cssValue,AssertionLibrary.Screenshot screenshot){
        boolean status =false;
        try{
            SearchContext shadow  = this.shadowElementInitialize(cssHost);
            if(shadow.findElement(By.cssSelector(cssValue)).isDisplayed()){
                status=true;
            }
            this.setImplicitWait(1);
            if(screenshot.equals(Screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
                logger.info("Eelement is visible");
            }
        }catch (Exception var5){
            logger.info("exception message shadow element not displayed"+var5);
        }
        return status;
    }

    public boolean  isShadowElementDisplayed(String cssValue,AssertionLibrary.Screenshot screenshot){
        boolean status =false;
        try{
            SearchContext shadow  = this.shadowElementInitialize("nxn-shell");
            if(shadow.findElement(By.cssSelector(cssValue)).isDisplayed()){
                status=true;
            }
            this.setImplicitWait(1);
            if(screenshot.equals(Screenshot.REQUIRED)){
                Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
                logger.info("Eelement is visible");
            }
        }catch (Exception var5){
            logger.info("exception message shadow element not displayed"+var5);
        }
        return status;
    }
    private SearchContext shadowElementInitialize(String cssHost){
        SearchContext shadow =null;
       try {
           this.setImplicitWait(2);
           shadow = this.driver.findElement(By.cssSelector(cssHost)).getShadowRoot();
           this.setImplicitWait(2);
       }catch(Exception var4){
           logger.info("Exception Message for shadow root: "+var4);
       }
       return shadow;
    }
    public boolean waitforElementVisible(By loacator,int waittimeSeconds){
        this.setImplicitWait(1);
        boolean ele=false;
        try{
            String var1000=String.valueOf(loacator);
            Log.info("check for element visible: "+var1000 +"seconds: "+waittimeSeconds);
            (new WebDriverWait(this.driver,Duration.ofSeconds((long)waittimeSeconds))).until(ExpectedConditions.visibilityOfElementLocated(loacator));
            ele =true;
        }catch (Exception e){
            Log.info(e.getMessage());
        }
        Log.info("element visible ? "+ele);
        this.setImplicitWait(this.implicitWaitTime);
        return ele;
    }

    protected void setInputValue(By by, String value, Screenshot screenshot) {
  WebElement element = this.getElement(by);
  element.clear();
  element.sendKeys(new CharSequence[]{value});
  if(screenshot.equals(Screenshot.REQUIRED)){
      Screenshots.addStepWithScreenshotInReport(this.driver,"set input:"+value);
  }else{
      Screenshots.addStepInReport("Set input: "+value);
  }
    }

    protected boolean waiforElementPresent(By locator, int waittimeinSeconds) {
        this.setCustomImplicitlyWait(1);
        boolean ele =false;
        try{
            String var1000=String.valueOf(locator);
            Log.info("Check the element present "+var1000+" in seconds "+waittimeinSeconds);
            (new WebDriverWait(this.driver, Duration.ofSeconds((long)waittimeinSeconds))).until(ExpectedConditions.presenceOfElementLocated(locator));
            ele =true;

        }catch (Exception e){
            Log.info(e.getMessage());
        }
        Log.info("elements present ? "+ele);
        this.setCustomImplicitlyWait(this.implicitWaitTime);
        return ele;
    }
    public void  setCustomImplicitlyWait(int seconds){
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds((long)seconds));
    }
}
    //522



