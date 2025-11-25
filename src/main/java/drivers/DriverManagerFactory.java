package drivers;

public class DriverManagerFactory {
    public static DriverManager getManager(String browserName){
        DriverManager driveManager =null;
        if(browserName.equalsIgnoreCase("chrome")){
            driveManager =new ChromeDriverManager();

        }
        /*else if(browserName.equalsIgnoreCase("firefox")){
            driveManager =new FirefoxDriverManager();

        }*/
      else if(browserName.equalsIgnoreCase("edge")){
            driveManager =new EdgeDriverManager();

        }
        else if(browserName.equalsIgnoreCase("safari")){
            driveManager =new SafariDriverManager();

        }
    return driveManager;
    }
}
