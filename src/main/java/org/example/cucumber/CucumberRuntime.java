package org.example.cucumber;
import org.openqa.selenium.WebDriver;
public class CucumberRuntime {
    private ThreadLocal<WebDriver> driver;
private WebDriver driver1;
public CucumberRuntime(){

}
public CucumberRuntime(ThreadLocal<WebDriver>driver){
    this.driver=driver;
}
public CucumberRuntime(WebDriver driver2){
    driver1=driver2;
}
private static ThreadLocal<CucumberRuntime> instances = new ThreadLocal<>();
public static synchronized CucumberRuntime get(){
    return instances.get();
}
public void setDriver(ThreadLocal<WebDriver>driver){
    this.driver=driver;
}
public ThreadLocal<WebDriver>getDriver(){return driver;}
    public static synchronized void set(ThreadLocal<WebDriver>driver){
    instances.set(new CucumberRuntime(driver));
    }
    public static synchronized void setPom(ThreadLocal<WebDriver>driver){
        instances.set(new CucumberRuntime(driver));
    }
    public void setDriver(WebDriver driver ){
    driver1=driver;
    }
    public WebDriver getDriver1(){
    return driver1;
    }
    public static synchronized void set(WebDriver driver){
    instances.set(new CucumberRuntime(driver));
    }
    public static synchronized void setPom(WebDriver driver){
        instances.set(new CucumberRuntime(driver));
    }
    public static synchronized void close(){
        instances.remove();
    }
}
