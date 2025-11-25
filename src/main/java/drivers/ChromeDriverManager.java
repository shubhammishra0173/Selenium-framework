package drivers;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.example.cucumber.Log;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utilites.ConfigProvider;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ChromeDriverManager extends DriverManager {
    public static String headlessFlag = System.getProperty("headless", ConfigProvider.getAsString("headless"));
    HashMap<String, Object> chromeprefs;
    ChromeOptions options;
    private ChromeDriverService chService;
    private String chromecaps = System.getProperty("chrome.cas.list.of.strings", ConfigProvider.getAsString("chrome.cas.list.of.strings"));
    private boolean bsFlag = Boolean.valueOf(System.getProperty("BrowserStack", ConfigProvider.getAsString("BrowserStack")));
    private boolean seleniumGridFlag = Boolean.valueOf(System.getProperty("SeleniumGrid", ConfigProvider.getAsString("SeleniumGrid")));
    private String downloadPath = System.getProperty("chrome.file.download.path", ConfigProvider.getAsString("chrome.file.download.path"));
    private String autoFileDownload = System.getProperty("auto.file.download", ConfigProvider.getAsString("auto.file.download"));
    private String flagApp = System.getProperty("companionApp", ConfigProvider.getAsString("companionApp"));
    private static final Logger log = Logger.getLogger(ChromeDriverManager.class.getName());


    @Override
    protected void startService() {
        if (!this.isServiceInitialized()) {
            this.chService = ChromeDriverService.createDefaultService();
        }
    }

    private boolean isServiceInitialized() {
        return null != this.chService;
    }

    public void stopService() {
        if (this.isServiceInitialized() && this.chService.isRunning()) {
            this.chService.stop();

        } else {
            this.driver.quit();
        }
    }

    @Override
    protected void createDriver() {
        Log.startTestCase("Launching chrome Driver");
        MutableCapabilities capabilities = new MutableCapabilities();
        this.options = new ChromeOptions();
        HashMap<String, Object> browserstackOptions = new HashMap<>();
        this.fnAddCapabilities(capabilities, browserstackOptions);
        if (headlessFlag.equalsIgnoreCase("true")) {
            String[] configValues = ConfigProvider.getAsString("headless.options.lis.of.Strings").split(",");
            Set<String> arguments = new LinkedHashSet(Arrays.asList(configValues));
            arguments.add("--window-size=1920,1080");
            arguments.add("--remote-allow-origins=*");
            if (StringUtils.isNotEmpty(this.additionalChromeHeadlessArguments)) {
                arguments.addAll(Arrays.asList(this.additionalChromeHeadlessArguments.split(",")));
            }
            this.options.addArguments((String[]) arguments.toArray(new String[0]));
            log.info("All chrome Arguments: " + String.join(", ", arguments));
            this.options.setCapability("platformName", this.getPlatform());
            if (StringUtils.isNoneEmpty(new CharSequence[]{this.downloadPath})) {
                this.chromeprefs=new HashMap<>();
                this.chromeprefs.put("download.default.directory",this.downloadPath);
                this.options.setExperimentalOption("prefs",this.chromeprefs);
            }
            this.fnInitiateDriver(capabilities,this.options);

        }else{
            if(this.flagApp!=null && !this.flagApp.equalsIgnoreCase("false")){
                if(this.flagApp.equalsIgnoreCase("true")){
                    String binary = ConfigProvider.getAsString("companionApp.binary.string");
                    String[]values = ConfigProvider.getAsString("companionApp.region.string").split(",");
                    String[]valuesAdd = ConfigProvider.getAsString("companionApp.list.of.string").split(",");
                    this.options.setBinary(binary.replace("user",System.getProperty("user.name")));
                    this.options.addArguments(values);
                    this.options.addArguments(valuesAdd);
                    HashMap<String,Object> chromePref = new HashMap<>();
                    List<String>experimentalFlags= new ArrayList<>();
                    experimentalFlags.add("calculate-native-win-occlusion@2");
                    chromePref.put("borwser.enabled_labs_experiments",experimentalFlags);
                    this.options.setExperimentalOption("localState", chromePref);

                }else{
                    String[] values =ConfigProvider.getAsString("options.list.of.Strings").split(",");
                    this.options.addArguments(values);
                    this.options.addArguments(new String[]{"--remote-allow-origins=*"});
                }
                this.driverFileDownload();
                if(this.isSeleniumGridRequired()){
                    capabilities.setCapability("goog:chromeOptions",this.options);
                    this.driver=new RemoteWebDriver(this.getremoteServerUrl(),capabilities,true);

                }else{
                    this.driver = new ChromeDriver(this.chService,this.options);
                }
            }
            Log.endTestCase("Launching chrome Driver");
        }

    }
    private void fnInitiateDriver(MutableCapabilities capabilities, ChromeOptions options){
        if(this.isSeleniumGridRequired()){
            capabilities.setCapability("ggo:chromeOptions",options);
            this.driver=new RemoteWebDriver(this.getremoteServerUrl(),capabilities,true);
        }else{
            Log.info("Selenium Grid Flag "+this.isSeleniumGridRequired());
            this.driver=new ChromeDriver(options);
        }
    }
    private void driverFileDownload(){
        HashMap<String,Object> chromePrefsVal = new HashMap<>();
        if(!this.autoFileDownload.isEmpty() && this.autoFileDownload.equalsIgnoreCase("true")){
            chromePrefsVal.put("profile.default_content_settings_values.automatic_downloads",1);

        }else{
            chromePrefsVal.put("profile.default_content_settings.popup",0);

        }
        chromePrefsVal.put("safebrowsing.enabled","true");
        if(StringUtils.isNoneEmpty(new CharSequence[]{this.downloadPath})){
            chromePrefsVal.put("download.default_directory",this.downloadPath);
        }
        this.options.setExperimentalOption("prefs",chromePrefsVal);
        this.options.setCapability("platformName",this.getPlatform());
    }
    private void fnAddCapabilities(MutableCapabilities capabilities,HashMap<String,Object>browserStackOptions){
        if(this.bsFlag && this.seleniumGridFlag){
            if(!this.chromecaps.isEmpty()&& this.chromecaps.contains("||")){
                this.fnAddChromeCaps(browserStackOptions);
            } else if (!this.chromecaps.isEmpty()) {
                this.fnAddChromeCaps(this.chromecaps,browserStackOptions);
            }
            capabilities.setCapability("bstack:options",browserStackOptions);
        }
    }
    private void fnAddChromeCaps(String chromecaps,HashMap<String,Object> browserStackOptions){
String[] caps = chromecaps.split(",");
if(!caps[1].equalsIgnoreCase("true") &&!caps[1].equalsIgnoreCase("false")){
    browserStackOptions.put(caps[0],caps[1]);
}else{
    boolean flag = Boolean.parseBoolean(caps[1]);
    browserStackOptions.put(caps[0],flag);
}
    }
    private void fnAddChromeCaps(HashMap<String,Object>browserStackOptions){
        String[]arr= this.chromecaps.split(Pattern.quote("||"));
        for(int i =0;i<arr.length;++i){
            this.fnAddChromeCaps(arr[i],browserStackOptions);
        }
    }
}
