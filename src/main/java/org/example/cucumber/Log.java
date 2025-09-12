package org.example.cucumber;

import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Log {
    public static final Logger Log = LogManager.getLogger(Log.class);

    public static void startTestCase(String sTestCaseName) {
Log.info("********************Start test case**********************");
        Log.info("********************+"+sTestCaseName+"e**********************");

    }
    public static void endTestCase(String sTestCaseName) {
        Log.info("********************End Test case case**********************");
        Log.info("********************+"+sTestCaseName+"e**********************");

    }
    public static void error(String message){
        Log.error(message);
    }
    public static void error(String message,Throwable e){
        Log.error(message+"Error :"+e.getMessage());
    }
    public static void info(String message){
        Log.info(message);
    }
    public static void debug(String message){
        Log.debug(message);
    }
    public static void debug(String message,Throwable e){
        Log.debug(message,e);
    }
    public static void error(Exception e){
        Log.error(e);
    }
    public static void info(List<String> jobGroupNames){
        Log.info(jobGroupNames);
    }
    public static void info(boolean flag){
        Log.info(flag);
    }
}