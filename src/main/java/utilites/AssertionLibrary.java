package utilites;



import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class AssertionLibrary {
    private static WebDriver driver;
    private static final String ACTUAL = "<br> Actual: ";
    private static final  String EXPECTED="<br> Expected: ";
    public AssertionLibrary(WebDriver driver){
        AssertionLibrary.driver=driver;
    }
    public static void assertEquals(String actual, String expected, String message,Screenshot screenshot){
        assertEquals((Object) actual,(Object)expected,(String) message,(Screenshot) screenshot);
    }
    public static void assertEquals(String actual, String expected, String message){
        assertEquals((Object) actual,(Object)expected,(String) message,AssertionLibrary.Screenshot.REQUIRED);
    }

    public static void assertEquals(Double actual, Double expected, Double delta ,String message,Screenshot screenshot){
       String reportMessage = message+"<br> Actual: "+actual.toString()+"<br> Expected: "+expected.toString();
        Assert.assertEquals(actual,expected,delta,message);
        attachScreenshotIfRequired(screenshot,reportMessage);
    }
    public static void assertEquals(Object actual,Object expected,String message ,Screenshot screenshot){
        String reportMessage = message+"<br> Actual: "+actual.toString()+"br expected: "+expected.toString();
        Assert.assertEquals(actual,expected,message);
        attachScreenshotIfRequired(screenshot,reportMessage);
    }
    public static void assertEquals(Object actual,Object expected,String message){
        assertEquals(actual,expected,message,AssertionLibrary.Screenshot.REQUIRED);
    }
    public static void assertEquals(boolean condition,String message,Screenshot screenshot){
        String reportMessage = message+"<br> Condition: "+condition;
        Assert.assertTrue(condition,message);
        attachScreenshotIfRequired(screenshot,reportMessage);
    }
    public static void assertTrue(boolean condition,String message,Screenshot screenshot){
        String reportMessage = message+"<br> Condition: "+condition;
        Assert.assertTrue(condition,message);
        attachScreenshotIfRequired(screenshot,reportMessage);
    }
    public static void assertTrue(boolean condition,String message){
        assertTrue(condition,message, AssertionLibrary.Screenshot.REQUIRED);
    }
    public static void assertNotEquals(String actual,String expected,String message,Screenshot screenshot){
        assertNotEquals(actual,expected,message,screenshot);
    }
    public static void assertNotEquals(String actual1,String actual2,String message){
        assertNotEquals(actual1,actual2,message,AssertionLibrary.Screenshot.REQUIRED);
    }
    public static void assertNotEquals(Object actual1,Object actual2,String message, Screenshot screenshot){
        String reportMessage= message+"<br> Actual: "+actual1.toString()+"<br> Expected: "+actual2.toString();
        Assert.assertNotEquals(actual1,actual2,message);
        attachScreenshotIfRequired(screenshot,reportMessage);
    }
    public static void assertNotEquals(Object actual1 , Object actual2 , String message){
        assertNotEquals(actual1,actual2,message,AssertionLibrary.Screenshot.REQUIRED);
    }
    public static void aseertFalse(boolean condition,String message,Screenshot screenshot){
        String reportMessage = message+"<br> Condition: "+condition;
        Assert.assertFalse(condition,message);
        attachScreenshotIfRequired(screenshot,reportMessage);
    }
    public static void assertFalse(boolean condition,String message,Screenshot screenshot){
        String reportMessage = message+"<br> Condition: "+condition;
        Assert.assertFalse(condition,message);
        attachScreenshotIfRequired(screenshot,reportMessage);
    }
    public static void assertFalse(boolean condition,String message){
        assertFalse(condition,message, AssertionLibrary.Screenshot.REQUIRED);
    }
    private static void attachScreenshotIfRequired(Screenshot screenshot,String message){
        if(screenshot.equals(AssertionLibrary.Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,message);
        }else{
            Screenshots.addStepInReport(message);
        }
    }
    public static enum Screenshot {
        REQUIRED, NOT_REQUIRED;

        private Screenshot() {

        }
    }
}
