//package utilites;
//
//
//
//import com.aventstack.extentreports.*;
//import com.aventstack.extentreports.gherkin.model.IGherkinFormatterModel;
//import com.aventstack.extentreports.observer.ExtentObserver;
//
//public class ExtentReports extends AbstractProcessor implements Writable, AnalysisTypeConfigurable {
//    public ExtentReports(){
//
//    }
//    public void attachReporter(ExtentObserver... observer){
//        super.attachReporter(observer);
//    }
//    public ExtentTest createTest(Class<? extends IGherkinFormatterModel>type,String name,String description){
//        ExtentTest t = new ExtentTest(this,type,name,description);
//        this.onTestCreated(t.getModel());
//        return t;
//    }
//    public ExtentTest createTest(Class<? extends IGherkinFormatterModel>type,String name){
//        return this.createTest((Class) type,name,(String) null);
//    }
//    public ExtentTest createTest(GherkinKeyword gherkinKeyword, String name , String description){
//        Class<? extends IGherkinFormatterModel> clazz = gherkinKeyword.getKeyword().getClass();
//        return this.createTest(clazz,name,description);
//
//    }
//    public ExtentTest createTest(GherkinKeyword gherkinKeyword , String testName){
//        return this.createTest((GherkinKeyword) gherkinKeyword,testName,(String) null);
//    }
//    public  ExtentTest createTest(String name , String description){
//        ExtentTest t = new ExtentTest(this,name,description);
//        this.onTestCreated(t.getModel());
//        return t;
//    }
//    public ExtentTest createTest(String name){ return  this.createTest((String) name, (String) null);}
//public void removeTest(ExtentTest test){this.onTestRemoved(test.getModel());}
//    public void removeTest(String name){this.getReport().findTest(this.getReport().getTestList(),name).ifPresent(this::onTestRemoved);}
//
//
//    @Override
//    public void setAnalysisStrategy(AnalysisStrategy analysisStrategy) {
//
//    }
//
//    @Override
//    public void flush() {
//
//    }
//}
