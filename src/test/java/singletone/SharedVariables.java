package singletone;

public class SharedVariables {
    private static SharedVariables instance;
    private boolean amazonSiteLoggedIn=false;
    private boolean amazonPagenavigated=false;
    private String newUserID =null;
    private String referenceID = null;
    private String browserName =null;
    private String browserVersion=null;
private SharedVariables(){

}
public static SharedVariables getInstance(){
    if(instance==null){
        instance = new SharedVariables();
    }
    return instance;
}
public void setamazonSiteLoggedIn(boolean amazonSiteLoggedIn){
    this.amazonSiteLoggedIn=amazonSiteLoggedIn;
}
public boolean isamazonPageLoggedin(){
    return this.amazonPagenavigated;
}

    public static void setInstance(SharedVariables instance) {
        SharedVariables.instance = instance;
    }

    public boolean isAmazonSiteLoggedIn() {
        return amazonSiteLoggedIn;
    }

    public void setAmazonSiteLoggedIn(boolean amazonSiteLoggedIn) {
        this.amazonSiteLoggedIn = amazonSiteLoggedIn;
    }

    public boolean isAmazonPagenavigated() {
        return amazonPagenavigated;
    }

    public void setAmazonPagenavigated(boolean amazonPagenavigated) {
        this.amazonPagenavigated = amazonPagenavigated;
    }

    public String getNewUserID() {
        return newUserID;
    }

    public void setNewUserID(String newUserID) {
        this.newUserID = newUserID;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }
}
