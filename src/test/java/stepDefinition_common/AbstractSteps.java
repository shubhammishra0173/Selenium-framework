package stepDefinition_common;

import drivers.DriverManager;
import drivers.DriverManagerFactory;
import org.example.cucumber.CucumberRuntime;
import org.example.cucumber.Log;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import page_common.pageobjectManager.PageObjectManager;
import utilites.ConfigProvider;

import java.time.Duration;

public abstract  class AbstractSteps {
private static ThreadLocal<drivers.DriverManager> driverManager = new ThreadLocal<>();
private static ThreadLocal <WebDriver> driver = new ThreadLocal<>();
private static ThreadLocal <PageObjectManager> pageObjectManager = new ThreadLocal<>();
private static final String BROWSER = System.getProperty(ConfigProvider.getAsString("browser").trim(),"chrome");
String Config_Browser = System.getProperty("browerName");
String comApp_Flag = ConfigProvider.getAsString("companionApp");
private static final int IMPLICIT_WAIT = ConfigProvider.getAsInt("IMPLICIT_WAIT");
public void  startDriver(){
    this.initializeDriverManager(BROWSER,false);
}
private void initializeDriverManager(String browser , boolean setCustomUserAgent){
    if(driverManager.get()==null){
        driverManager.set(DriverManagerFactory.getManager(browser));
    }
    if(setCustomUserAgent){
        ((DriverManager)driverManager.get()).setAdditionalChromeHeadlessArguments("--user-agent="+ConfigProvider.getAsString("userAgent"));
    }
    driver.set(((DriverManager)driverManager.get()).getDriver());
    ((WebDriver) driver.get()).manage().timeouts().implicitlyWait(Duration.ofSeconds((long) IMPLICIT_WAIT));
if(!browser.equalsIgnoreCase("chrome")){
    ((WebDriver)driver.get()).manage().window().maximize();
}

}
public void setDriver(){
    setBrowser();
    if(driverManager.get()==null)
        driverManager.set(DriverManagerFactory.getManager(Config_Browser));
        driver.set(driverManager.get().getDriver());
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigProvider.getAsInt("IMPLICIT_WAIT")));
        if(comApp_Flag.equalsIgnoreCase("false")&&!Config_Browser.equalsIgnoreCase("safari")){
            driver.get().manage().deleteAllCookies();
        }else{
            driver.get().manage().window().setSize(new Dimension(1920,1080));
        }
        pageObjectManager.set(new PageObjectManager(driver.get()));
        CucumberRuntime.set(driver.get());
        //ZephyrScaleDriver.set(driver.get());

}
public void stopDriver(){
    if(driverManager.get()!=null){
        driverManager.get().stopService();
        driverManager.set(null);
        driver.set(null);
        pageObjectManager.set(null);
    }
}
public WebDriver getDriver(){
    return driver.get();
}
public PageObjectManager getPageObjectManager(){
    return pageObjectManager.get();
}
public void setFirefoxDriver(){
    driverManager.set(DriverManagerFactory.getManager("firefox"));
    driver.set(driverManager.get().getDriver());
    driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigProvider.getAsInt("IMPLICIT_WAIT")));
if(!ConfigProvider.getAsString("browser").equalsIgnoreCase("chrome"))
    driver.get().manage().window().maximize();
pageObjectManager.set(new PageObjectManager(driver.get()));
CucumberRuntime.set(driver.get());
}
        public void setBrowser(){
    if(Config_Browser==null){
        Config_Browser=ConfigProvider.getAsString("browser");
    }
        }
        public  String getPropertyValue(String key){
    String value =key;
    try{
      value=System.getProperty(key);
    }catch (Exception e){
        Log.error("Exception in data File reading "+e.getMessage());
    }
    if(value==null) value=key;
Log.info("Value for key "+key+" = "+value);
return value;
}

}
