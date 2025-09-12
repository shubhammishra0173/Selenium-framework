package utilites;

import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.ExtentTest;
import lombok.Synchronized;


import java.util.*;
import java.util.logging.Logger;

public class ExtentTestManager {
    private static final Map<Integer,ExtentTest> extentTestMap = new HashMap<>();
    private static final Set<Integer> extentThreadList = new HashSet<>();
    private static ExtentReports extent;
    private static final Logger logger = Logger.getLogger(ExtentTestManager.class.getName());
    private ExtentTestManager(){

    }
    public static synchronized ExtentTest getTest(){
        return (ExtentTest) extentTestMap.get(getCurrentThread());
    }
    public static synchronized void  endTest()
    {
        logger.info("end test start: "+Thread.currentThread().getId());
        extentThreadList.remove(getCurrentThread());
        if(!extentTestMap.isEmpty()&& extentThreadList.isEmpty()){
            removeTest();
            logger.info("ExtentTestMap is:"+extentTestMap);

        }
    }
    public static synchronized void removeTest() {
        Iterator<Integer> iterator = extentTestMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            ExtentTest test = extentTestMap.get(i);
            extent.removeTest(test);
        }
        extentTestMap.clear();
    }

    public static synchronized ExtentTest startTest(String testName, String desc){
        logger.info("Start test "+Thread.currentThread().getId());
        extent = ExtentConfiguration.getInstance();
       ExtentTest test = extent.createTest(testName,desc);
       extentTestMap.put(getCurrentThread(),test);
       extentThreadList.add(getCurrentThread());
       return test;
    }
    public  static synchronized int getCurrentThread(){
        return (int) Thread.currentThread().getId();
    }
}
