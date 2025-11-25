package drivers;

import io.restassured.RestAssured;
import io.restassured.internal.RestAssuredResponseImpl;
import lombok.Generated;
import org.example.cucumber.Log;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import utilites.ConfigProvider;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;


public abstract class DriverManager {
    private static final Logger logger = Logger.getLogger(DriverManager.class.getName());
    private static final String BROWSERSTACK_MODE = System.getProperty("browserStackMode", ConfigProvider.getAsString("browserstackMode"));
    private static final boolean BROWSERSTACK_MODE_VALUE;

    protected WebDriver driver;
    protected String additionalChromeHeadlessArguments;

    public DriverManager() {

    }

    protected static String getHubUrl() throws IOException {
        Log.info("getHubUrl - retrieving hub_url from SMS");
        InputStream inputStream = DriverManager.class.getClassLoader().getResourceAsStream("bsStack.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) Objects.requireNonNull(inputStream)));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String jsonBody = jsonBuilder.toString();
        RestAssured.baseURI = System.getProperty("");
        RestAssured.useRelaxedHTTPSValidation();
        RestAssuredResponseImpl response = (RestAssuredResponseImpl) RestAssured.given()
                .header("Accept", "application/json", new Object[0]).header("Content-Tyoe", "application/json", new Object[0]).body(jsonBody).when().post("secrets/download", new Object[0]);

Log.info("getHubUrl - status is "+response.getStatusLine());
Log.info("getHubUrl - Status Code is "+response.getStatusCode());
        Properties prop= new Properties();
        prop.load(new StringReader(response.asPrettyString()));
        String hubUrl;
        if(BROWSERSTACK_MODE_VALUE){
            hubUrl= BROWSERSTACK_MODE.equalsIgnoreCase("external")?prop.getProperty("hub_url_external"):prop.getProperty("hub_url_internal");
        }else{
            hubUrl = prop.getProperty("hub_url_internal");
        }
            return hubUrl;
    }
    protected abstract void startService();
    protected abstract void createDriver();
    public void stopService(){}
    public WebDriver getDriver(){
        if(null==this.driver){
            if(!this.isSeleniumGridRequired()){
                DriverExecutables.setBrowserExe();
                this.startService();
            }
            this.createDriver();
        }
        return this.driver;
    }
    boolean isSeleniumGridRequired(){
        String value= System.getProperty("SeleniumGrid",ConfigProvider.getAsString("SeleniumGrid"));
        return Boolean.parseBoolean(value);
    }
    Platform getPlatform(){
        String platforValue = System.getProperty("Platform",ConfigProvider.getAsString("platform"));
        if(!platforValue.equalsIgnoreCase("windows7")&& !platforValue.equalsIgnoreCase("windows")&& !platforValue.equalsIgnoreCase("7")){
            if(!platforValue.equalsIgnoreCase("windows8")&& !platforValue.equalsIgnoreCase("8")){
                if(!platforValue.equalsIgnoreCase("windows8.1")&& !platforValue.equalsIgnoreCase("8.1")){
                    if(!platforValue.equalsIgnoreCase("windows10")&& !platforValue.equalsIgnoreCase("10")) {
                        if(!platforValue.equalsIgnoreCase("windowsXP")&& !platforValue.equalsIgnoreCase("xp")) {

                            if (platforValue.equalsIgnoreCase("mac")) {
                                return Platform.MAC;
                            } else if (platforValue.equalsIgnoreCase("linux")) {
                                return Platform.LINUX;
                            }else{
                                return platforValue.equalsIgnoreCase("unix") ? Platform.UNIX:Platform.ANY;
                            }
                        }else{
                            return Platform.XP;
                        }
                    }else{
                        return Platform.WIN10;
                    }
                    }else {
                    return Platform.WIN8_1;
                }
                }   else {
                return  Platform.WIN8;
            }
        }else{
            return Platform.WINDOWS;
        }
    }
    URL getremoteServerUrl(){
        URL url=null;
        String urlString="";
        try{
            urlString=System.getProperty("hub_url",getHubUrl().trim());
            url=this.toURL(urlString);
        }catch (IOException|NullPointerException var4){
            logger.warning("hub_url property is not defined");
        }
        if(urlString.isEmpty()){
            logger.warning("hub_url value is not defend");
        }
        return url;
    }
    private URL toURL(String urlString){
        URL url = null;
        try{
            url=new URL(urlString);
        }catch (MalformedURLException var4){
            logger.warning("url may not be correct "+urlString);
        }
        return url;
    }

    @Generated
    public String getAdditionalChromeHeadlessArguments(){
        return  this.additionalChromeHeadlessArguments;
    }
    @Generated
    public String setAdditionalChromeHeadlessArguments(String additionalChromeHeadlessArguments){
        return  this.additionalChromeHeadlessArguments=additionalChromeHeadlessArguments;
    }

    static {
        BROWSERSTACK_MODE_VALUE=BROWSERSTACK_MODE!=null &&(!BROWSERSTACK_MODE.isEmpty()||!BROWSERSTACK_MODE.isBlank());
    }
}