package utilites;

import com.aventstack.extentreports.ExtentReports;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class ExtentConfiguration {
    private static ExtentReports extent;
    public static final String WORKING_DIR = System.getProperty("user.dir");
    private static final String TIME_STAMP= (new SimpleDateFormat("dd.MM.yyyy.HH.mm")).format(new Date());
    private static final String EXTENT_REPORTS_FOLDER = null;
    private static final String REPORT_NAME = null;
    private static final String EXTENT_REPORTS_PATH = null;
    private static final String EQLOGO="eqlogo.jpg";
    private static Logger logger;
    private  ExtentConfiguration(){

    }
    public static String returnReportName()
    {
        return  EXTENT_REPORTS_PATH;
    }
    public static String getExtentReportsFolder()
    {
        return  EXTENT_REPORTS_PATH;
    }
    public static ExtentReports getInstance(){
        if(extent==null)
        {
            createReportsFolder();
            attachreporters();
        }
        return extent;
    }
    public static void createReportsFolder(){
        File file = new File(EXTENT_REPORTS_FOLDER);
        if(!file.exists()&&!file.mkdir()){
            logger.warning("Failed to create directory");
        }
    }
    public static ExtentReports attachreporters(){
        String extentReportRequired =null;
        extent = new ExtentReports();

        return extent;
    }
}
