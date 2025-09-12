package drivers;

import utilites.ConfigProvider;

public class DriverExecutables {

protected static void setBrowserExe(){
 String jdkVersion=System.getProperty("sun.arch.data.model");
 String browserName = System.getProperty("browser", ConfigProvider.getAsString("browser"));
 if(browserName.equalsIgnoreCase("chrome")){
     executeChrome();
 }
  else if(browserName.equalsIgnoreCase("firefox")){
        executeFirefox(jdkVersion);
    }
 else if(browserName.equalsIgnoreCase("edge")){
     executeEdge();
 }

 else if(browserName.equalsIgnoreCase("safari")){
     executeSafari();
 }


}
protected static void executeSafari(){

}
    protected static void executeEdge(){

    }
    protected static void executeFirefox(String jdkVersion){

    }
    protected static void executeChrome(){

    }
}
