package drivers;

import io.cucumber.java.hu.Ha;
import org.apache.commons.lang3.StringUtils;
import org.example.cucumber.Log;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utilites.ConfigProvider;

import java.util.*;
import java.util.regex.Pattern;

public class EdgeDriverManager extends DriverManager{
    public static String headlessFlag = System.getProperty("headless", ConfigProvider.getAsString("headless"));
    private ChromeDriverService chService;
    private String edgecaps = System.getProperty("edge.caps.list.of.strings", ConfigProvider.getAsString("chrome.caps.list.of.strings"));
    private boolean bsFlag = Boolean.valueOf(System.getProperty("BrowserStack", ConfigProvider.getAsString("BrowserStack")));
    private boolean seleniumGridFlag = Boolean.valueOf(System.getProperty("SeleniumGrid", ConfigProvider.getAsString("SeleniumGrid")));
    private String downloadPath = System.getProperty("edge.file.download.path", ConfigProvider.getAsString("edge.file.download.path"));
    private String autoFileDownload = System.getProperty("auto.file.download", ConfigProvider.getAsString("auto.file.download"));
    private EdgeDriverService edgeDriverService;

    private static void fnEdgeCapabilities(HashMap<String,Object> browserStackOptions, String arr){

        String[] caps = arr.split(",");
        if(!caps[1].equalsIgnoreCase("true") &&!caps[1].equalsIgnoreCase("false")){
            browserStackOptions.put(caps[0],caps[1]);
        }else{
            boolean flag = Boolean.parseBoolean(caps[1]);
            browserStackOptions.put(caps[0],flag);
        }
    }
    protected void startService() {
        if (!this.isServiceInitialized()) {
            this.edgeDriverService = EdgeDriverService.createDefaultService();
        }
    }
    private boolean isServiceInitialized(){
        return  null!=this.edgeDriverService;
    }
    public void stopService() {
        try {
            if (this.isServiceInitialized() && this.edgeDriverService.isRunning()) {
                this.edgeDriverService.stop();

            } else if (this.driver != null) {
                this.driver.quit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createDriver(){
        Log.startTestCase("Launching Edge Driver");
        MutableCapabilities capabilities = new MutableCapabilities();
        EdgeOptions options = new EdgeOptions();
        HashMap<String, Object> browserstackOptions = new HashMap<>();
        this.fnEdgeBSSCapabilities(capabilities, browserstackOptions);
        if (headlessFlag.equalsIgnoreCase("true")) {
            this.fnInitiateEdgeHeadlessDriver(capabilities,options);
        }else{
            String[]values = ConfigProvider.getAsString("options.list.of.strings").split(",");
            options.addArguments(values);
            HashMap<String, Object>edgePrefs = new HashMap<>();
            if(!this.autoFileDownload.isEmpty() && this.autoFileDownload.equalsIgnoreCase("true")){
                edgePrefs.put("profile.default_content_setting_values.automatic_downloads",1);
            }else{
                edgePrefs.put("profile.default_content_settings.popups",0);
            }edgePrefs.put("safebrowsing.enabled","true");
            if(StringUtils.isNoneEmpty(new CharSequence[]{this.downloadPath})){
                edgePrefs.put("download.default_directory",this.downloadPath);
            }
            options.setExperimentalOption("prefs",edgePrefs);
            options.setCapability("platformName",this.getPlatform());
            if(this.isSeleniumGridRequired()){
                capabilities.setCapability("ggog:chromeOptions",options);
                this.driver = new RemoteWebDriver(this.getremoteServerUrl(),capabilities,true);
            }else{
                this.driver=new EdgeDriver(this.edgeDriverService,options);
            }
            Log.endTestCase("Launching edge driver");
        }

    }
    private void fnInitiateEdgeHeadlessDriver(MutableCapabilities capabilities, EdgeOptions options){
        String[]values =ConfigProvider.getAsString("edgeHeadless.options.list.of.strings").split(",");
        options.addArguments(values);
        options.setCapability("platformName",this.getPlatform());
       if(StringUtils.isNoneEmpty(new CharSequence[]{this.downloadPath})){
           HashMap<String,Object> edgerefs = new HashMap<>();
           edgerefs.put("downloa.default_directory",this.downloadPath);
       }
       if(this.isSeleniumGridRequired()){
           capabilities.setCapability("ms:edgeOptions",options);
           this.driver=new RemoteWebDriver(this.getremoteServerUrl(),capabilities,true);
       }else{
           Log.info("Selenium Grid Flag "+this.isSeleniumGridRequired());
           this.driver=new EdgeDriver(options);
       }
    }
    private void fnEdgeBSSCapabilities(MutableCapabilities capabilities, HashMap<String,Object>browserStackOptions)
    {
        if(this.bsFlag&& this.seleniumGridFlag){
            if(!edgecaps.isEmpty() && edgecaps.contains("||")){
                String[]arr=edgecaps.split(Pattern.quote("||"));
                for(int i =0;i<arr.length;++i) {
                    fnEdgeCapabilities(browserStackOptions, arr[i]);
                }
                }else if(!edgecaps.isEmpty()){
                    fnEdgeCapabilities(browserStackOptions,edgecaps);
                }
            capabilities.setCapability("bstack:options",browserStackOptions);

        }
    }}
