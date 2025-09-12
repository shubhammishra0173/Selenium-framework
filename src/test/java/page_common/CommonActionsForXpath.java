package page_common;

import com.aventstack.extentreports.model.Log;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import stepDefinition_common.DefaultStepDefinition;
import utilites.BasePageObject;
import utilites.ConfigProvider;
import utilites.Screenshots;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CommonActionsForXpath extends BasePageObject {

   protected WebDriver myDriver;
   public CommonActionsForXpath(WebDriver driver){
       super(driver);
       myDriver =driver;
   }
   public void click(String xpath){
       clickElement(By.xpath(xpath),xpath+"is clicked");
   }
    public void click(WebElement element,String elementDetails){
        Screenshots.addStepWithScreenshotInReport(myDriver,"Before click: "+elementDetails);
        element.click();
        Screenshots.addStepWithScreenshotInReport(myDriver,"Clicked "+elementDetails);
    }
    public boolean clickIfTheElementisPresent(String xpath){
       boolean elementFound=false;
       if(isElementPresent(xpath)){
           elementFound =true;
           click(xpath);
       }return elementFound;
    }
    public void switchToFrame(String xpathString){
       Locator obj = new page_common.Locator(By.xpath(xpathString),"");
           switchToFrame(obj);
    }
    public void quitBrowser(){
       myDriver.quit();}

    public void switchToDefaultContent(){
       myDriver.switchTo().defaultContent();

    }
    public void waitForSeconds(int seconds){
       try{
           Thread.sleep(seconds*1000);
       }catch (InterruptedException e){
           e.printStackTrace();
       }
    }
    public boolean waitForTheElementToAppear(String xpath,int seconds){
       boolean isElementFound=false;
       for(int i =0;i<seconds;i++){
           if(isElementPresent(xpath)){
               isElementFound =true;
               break;
           }
           waitForSeconds(1);
       }
       return isElementFound;
    }
    public boolean waitForTheElementToAppearAndEnabled(String xpath, int seconds){
       boolean isElementFound=false;
       for(int i=0;i<seconds;i++)
       {
           if(isElementEnabled(xpath)){
               System.out.println("appear loop enabled founddd "+i);
               isElementFound=true;
               break;
           }
           waitForSeconds(1);
       }
       return isElementFound;
    }
    public boolean waitForTheElementToDisappear(String xpath,int seconds){
       boolean isElementVisible =true;
       for(int i =0;i<seconds;i++){
           System.out.println("disappear loop "+i);
           if(!isElementPresent(xpath)){
               System.out.println("disappear loop trueee "+i);
               isElementVisible=false;
               break;
           }
           waitForSeconds(1);
       }
       return isElementVisible;
    }
    public boolean isElementPresent(String xpath){
       boolean isElementFound=false;
       try{
           myDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
           if(myDriver.findElements(By.xpath(xpath)).size()>0){
               isElementFound=true;
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       finally {
           myDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigProvider.getAsInt("IMPLICIT_WAIT")));
       }
       return isElementFound;
    }
    public boolean isElementEnabled(String xpath){
        boolean isElementFound=false;
        try{
            myDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            if(myDriver.findElement(By.xpath(xpath)).isEnabled()){
                isElementFound=true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            myDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigProvider.getAsInt("IMPLICIT_WAIT")));
        }
        return isElementFound;
    }
    public List<WebElement>getAllElements(String xpath){
       return getElements(xpath);
    }
    public void type(String xpath ,String value){
       setInputValue(xpath,value);
    }
    public void typeWithoutClearing(String xpath ,String value){
      myDriver.findElement(By.xpath(xpath)).sendKeys(value);
    }
    public void typeWithKey(String xpath , Keys key){
       WebElement element = getElement(xpath);
       element.sendKeys(key);
       Screenshots.addStepInReport("Set input: "+key);
    }
    public String getText(String xpath){
       return getElement(xpath).getText();

    }
    public void scroll(String xpath){
       Actions clickAction= new Actions(myDriver);
       WebElement scrollablePane=myDriver.findElement(By.xpath(xpath));
       clickAction.moveToElement(scrollablePane).click().build().perform();
       Actions scrollAction= new Actions(myDriver);
       scrollAction.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
       waitForSeconds(2);
    }
    public void clickMultipleElements(String xpath,int count){
       Screenshots.addStepWithScreenshotInReport(myDriver,"Before click");
       List<WebElement> elements = getAllElements(xpath);
       int i =0;
       for(WebElement x : elements){
           x.click();
           i++;
           if(i==count){
               break;
           }
       }
    }
    public static String getValueFromExcel(String columnName){
       return DefaultStepDefinition.currentIterationMap.get().get(columnName);
    }
    public String getUrlForRegion(String application,String region){
       String url="";
       if(application.equalsIgnoreCase("Amazon")){
           if(region.equalsIgnoreCase("QA")){
               url="https://www.amazon.com/";
           }
         else  if(region.equalsIgnoreCase("PROD")){
               url="https://www.amazon.com/";
           }
       }
       return url;
    }
    public void  login(String application,String region , String userName,String passwrod){
       myDriver.get(getUrlForRegion(application,region));
       if(application.equalsIgnoreCase("WEALTH")){
           new loginPagefacebook(myDriver).login(userName,passwrod);
       } else if (application.equalsIgnoreCase("Amazon")) {
           new AmazonLoginPage(myDriver).login(userName,passwrod);
       }
       Screenshots.addStepInReport(true,"Login completed");
    }
    public void login(String application,String region,LoginDetails user){
       myDriver.get(getUrlForRegion(application,region));
       String userName = user.userId;
       String password = getPasswordForUserName(userName);
       login(application,region,userName,password);
    }
    public String getPasswordForUserName(String userName){
       String excelDocName="\\\\Wc08a10014qv00\\nxp\\Test Bed ids\\Muthulogin.xlsx";
       String sheetName= "login";
       String colId="Pasword";
       String rowId=userName;
       String password="";
       try{
           password=useridpasswordexcel.getValueFromExcel(excelDocName,sheetName,colId,rowId);
       }catch (Exception e){
           e.printStackTrace();
       }
       return password;
    }

    public static void writeExcel(){
       XSSFWorkbook workbook = new XSSFWorkbook();
       XSSFSheet sheet = workbook.createSheet("Employee Data");
        Map<String,Object[]>data= new TreeMap<>();
        data.put("1", new Object[]{"ID","NAME","LASTNAME"});
        data.put("2", new Object[]{1,"TEST","TEST"});
        data.put("3", new Object[]{2,"Divya","Dwivedi"});
        data.put("4", new Object[]{3,"Shubham","Mishra"});
        Set<String> keySet = data.keySet();
        int rownum=0;
        for(String key: keySet){
            Row row = sheet.createRow(rownum++);
            Object[]objArr = data.get(key);
            int cellnum=0;
            for(Object obj :objArr){
                Cell cell = row.createCell(cellnum++);
                if(obj instanceof String){
                    cell.setCellValue((String)obj);
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);

                }
            }
        }try{
            FileOutputStream out = new FileOutputStream(new File("C:\\admin\\demo.xlsx"));
            workbook.write(out);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public boolean isElementVisible(String xpath){
       boolean isvisible =false;
       try{
           myDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
           if(myDriver.findElement(By.xpath(xpath)).isDisplayed()){
               isvisible=true;
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       finally {
           myDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigProvider.getAsInt("IMPLICIT_WAIT")));
       }
       return isvisible;
    }
    public boolean clickElementisVisible(String xpath){
       boolean elementVisible=false;
       if(isElementVisible(xpath)){
           elementVisible=true;
           click(xpath);
       }
       return elementVisible;
    }
    public boolean isElementSelected(String xpath){
       boolean isSelected = false ;
       try{
           myDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
           if(myDriver.findElement(By.xpath(xpath)).isSelected()){
               isSelected=true;
           }
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           myDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigProvider.getAsInt("IMPLICIT_WAIT")));
           return  isSelected;
       }
    }


}
