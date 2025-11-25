package utilites;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.cucumber.Log;
import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.PrinterMakeAndModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Screenshots {

    private static final Logger logger = LogManager.getLogger(Screenshots.class);
    private static final String SCREENSHOT_FOLDER="\\screenshots\\";
    private static final String ACTION_1="action1";
    private static String screenshotsFolderPath;
    private static String screenShotsLevel;
    private Screenshots(){

    }
    public static String getScreenshotFolderPath(){
        return screenshotsFolderPath;
    }
    public static void createDirectory(){
        String drive= getDriveWithFreeSpace();
        if(drive!=null &&drive.contains("C:")){
            String screenshotsFolderPath = FileUtils.getTempDirectoryPath() + "\\screenshots\\";
            System.out.println(screenshotsFolderPath);
        }else{
            screenshotsFolderPath=drive+"\\screenshots\\";
        }
        screenshotsFolderPath="AutomationRepots/screenshots/";
        File file = new File(screenshotsFolderPath);
        if(!file.exists()&&!file.mkdir()){
            logger.warn("Failed to create Directory");
        }
    }
    public static void setScreenShotsLevel(){
        screenShotsLevel=ConfigProvider.getAsString("ScreenShotslevel");
        if(screenShotsLevel==null){
            screenShotsLevel="Detailed";
        }
    }
    public static void addStepWithScreenshotInReport(WebDriver driver,String message){
        ExtentTest extentTest = ExtentTestManager.getTest();
        if(extentTest!=null){
            if(driver!=null){
                String path = captureScreenshot(driver,"action1");
                try{
                    if(!screenShotsLevel.equalsIgnoreCase("FailureOnly")){
                        extentTest.pass(message, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                    }else {
                        extentTest.pass(message);
                    }
                }catch (Exception var5){
                    logger.warn(var5.getMessage());
                }
            }else{
                extentTest.pass(message);
            }
        }

    }
    public static void addStepWithScreenshotInReport(boolean status, WebDriver driver , String message){
        ExtentTest extentTest = ExtentTestManager.getTest();
        try{
            if(extentTest!=null){
                if(driver!=null){
                    String path = captureScreenshot(driver,"secreenshot");
                    if(status){
                        extentTest.pass(message,MediaEntityBuilder.createScreenCaptureFromPath(path).build());

                    }else{
                        extentTest.fail(message,MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                        Assert.assertTrue(status,message);
                    }
                }else if(status){
                    extentTest.pass(message);

                }else {
                    extentTest.fail(message);
                    Assert.assertTrue(status,message);
                }
            }
        }catch (Exception e){
            logger.warn(e.getMessage());
        }finally {
            if(status){
                Log.info("Test passed - "+message);
            }else{
                Log.info("Test Failed -- "+ message);
            }
        }
    }
    public static void addStepInReport(String message){
        ExtentTest extentTest = ExtentTestManager.getTest();
        if(extentTest!=null){
                extentTest.pass(message);
            }
    }
    public static void addStepInReport(boolean conditon , String message){
        ExtentTest extentTest = ExtentTestManager.getTest();
        if(extentTest!=null){
            if(conditon){
                extentTest.pass(message);
            }else{
                extentTest.fail(message);
            }
        }
    }
    public static String getDriveWithFreeSpace(){
        String driveWithFreeSpace = null;
        File[] availableDrivers = File.listRoots();
        if(availableDrivers.length>1){
            File[]var2 = availableDrivers;
            int var3 = availableDrivers.length;
            for(int var4=0;var4<var3;++var4){
                File file = var2[var4];
                if(file.getFreeSpace()>100000000L){
                    driveWithFreeSpace=file.toString();
                }
            }
        }
        return driveWithFreeSpace;
    }
    public static String captureDesktop(String screenshotName) throws AWTException {
        String randomNumber = RandomStringUtils.randomNumeric(5);
        String destinationPath = screenshotsFolderPath+screenshotName+randomNumber+".png";
        Robot r = new Robot();
        Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage image = r.createScreenCapture(capture);
        try{
            ImageIO.write(image,"png",new File(destinationPath));
        }catch (IOException var7){
            logger.warn("Not able to capture desktop");
        }
        return destinationPath;
    }
    public static void addStepWithDesktopScreenInReport(String message){
        String path =null;
        try{
            path = captureDesktop("screenshot");

        }catch (AWTException var3)
        {
            logger.warn(var3.getMessage());
        }if(!screenShotsLevel.equalsIgnoreCase("FailureOnly")){

            ExtentTestManager.getTest().pass(message,MediaEntityBuilder.createScreenCaptureFromPath(path).build());

        }else{
            ExtentTestManager.getTest().pass(message);
        }
    }

    public static void addFailureStepWithScreenshotInRpoert(WebDriver driver , String message){
        ExtentTest extentTest = ExtentTestManager.getTest();
        if(extentTest!=null){
            if(driver!=null){
                String path = captureScreenshot(driver, "screenshot");
                try{
                    extentTest.fail(message,MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                }catch (Exception var5){
                    logger.warn(var5.getMessage());
                }
            }else{
                extentTest.fail(message);
            }
        }
    }
    public static void addJSONOutputToReport(String jsonString ,String status){
        ExtentTest extentTest = ExtentTestManager.getTest();
        Markup m = MarkupHelper.createCodeBlock(jsonString, CodeLanguage.JSON);
        if(status.equals(Screenshots.Status.PASS)){
            extentTest.pass(m);
        }else{
            extentTest.pass(m);
        }
    }
    public static void addXMLOutputToReport(String jsonString ,String status){
        ExtentTest extentTest = ExtentTestManager.getTest();
        Markup m = MarkupHelper.createCodeBlock(jsonString, CodeLanguage.XML);
        if(status.equals(Screenshots.Status.PASS)){
            extentTest.pass(m);
        }else{
            extentTest.pass(m);
        }
    }
    public static void addTableOutputReport(String [][]tableData ,String status){
        ExtentTest extentTest = ExtentTestManager.getTest();
        //ExtentTest extentTest = ExtentTestManager.getTest();
        Markup m = MarkupHelper.createTable(tableData);
        if(status.equals(Screenshots.Status.PASS)){
            extentTest.pass(m);
        }else{
            extentTest.pass(m);
        }
    }
    public static String captureScreenshot(WebDriver driver, String ScreenshotName){
        String RandomNumber = RandomStringUtils.randomNumeric(5);
        String destinationPath = screenshotsFolderPath+ScreenshotName+ RandomNumber+".png";
        TakesScreenshot ts = (TakesScreenshot) driver;
        File srcFile = (File) ts.getScreenshotAs(OutputType.FILE);
        try{
            updatetimeStamp(srcFile,destinationPath);
        }catch(IOException var7){
            logger.warn("Not able to capture screenshot");

        }
        return destinationPath.substring(destinationPath.indexOf("/")+1);
    }
    public static void updatetimeStamp(File sourceFileName,String TargetFileName) throws IOException {
        BufferedImage image = ImageIO.read(sourceFileName);
        SimpleDateFormat formatter = new SimpleDateFormat();
        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(18.0F));
        g.setColor(new Color(255,20,20));
        Date date = new Date(System.currentTimeMillis());
        g.drawString(formatter.format(date),image.getWidth()-250,image.getHeight()-20);
        g.dispose();
        ImageIO.write(image,"png",new File(TargetFileName));
    }
    static {
        createDirectory();
        setScreenShotsLevel();
    }public static enum  Status{
        PASS,
        FAIL;
        private Status(){

        }
    }
}
