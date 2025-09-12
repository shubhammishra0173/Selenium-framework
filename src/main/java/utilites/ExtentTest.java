//package utilites;
//
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.GherkinKeyword;
//import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.gherkin.GherkinDialect;
//import com.aventstack.extentreports.gherkin.model.IGherkinFormatterModel;
//import com.aventstack.extentreports.markuputils.Markup;
//import com.aventstack.extentreports.model.Author;
//import com.aventstack.extentreports.model.Category;
//import com.aventstack.extentreports.model.Device;
//import com.aventstack.extentreports.model.ExceptionInfo;
//import com.aventstack.extentreports.model.Log;
////import com.aventstack.extentreports.model.Video;
//import com.aventstack.extentreports.model.Media;
//import com.aventstack.extentreports.model.RunResult;
//import com.aventstack.extentreports.model.ScreenCapture;
//import com.aventstack.extentreports.model.Test;
//
//import com.aventstack.extentreports.model.service.ExceptionInfoService;
//import com.aventstack.extentreports.util.Assert;
//
//import lombok.Generated;
//
//import java.io.Serializable;
//import java.util.Arrays;
//
//public class ExtentTest implements RunResult, Serializable {
//
// private static final long serialVersionUID=584603178630591993L;
//    private transient utilites.ExtentReports extent;
// private Test model;
//
//ExtentTest(utilites.ExtentReports extent, Class<? extends IGherkinFormatterModel>type , String name, String description){
//    Assert.notEmpty(name,"Test name must not be null or emtpy");
//    this.model = Test.builder().bddType(type).name(name).description(description).useNaturalConf(extent.isUsingNaturalConf()).build();
//    this.extent=extent;
//}
//ExtentTest(utilites.ExtentReports extent, String testName , String description){
//    this(extent,(Class) null,testName,description);
//
//
//}
//public synchronized ExtentTest createNode(Class<? extends IGherkinFormatterModel>type, String name,String description){
//    utilites.ExtentTest t = new utilites.ExtentTest(this.extent,type,name,description);
//    this.model.addChild(t.getModel());
//    this.extent.onNodeCreated(t.getModel());
//    return t;
//}
//    public synchronized ExtentTest createNode( String name,String description){
//        ExtentTest t = new ExtentTest(this.extent,name,description);
//        this.model.addChild(t.getModel());
//        this.extent.onNodeCreated(t.getModel());
//        return t;
//    }
//    public ExtentTest createNode(Class<? extends IGherkinFormatterModel>type,String name){
//    return this.createNode((Class) type, name ,(String) null);
//    }
//    public ExtentTest createNode(String name){
//        return this.createNode((String) name ,(String) null);
//    }
//public ExtentTest createNode(GherkinKeyword gherkinKeyword,String name,String description){
//    return  gherkinKeyword ==null ?this.createNode(name,description):this.createNode(gherkinKeyword.getKeyword().getClass(),name,description);
//}
//public ExtentTest createNode(GherkinKeyword gherkinKeyword , String name){
//    return this.createNode((GherkinKeyword) gherkinKeyword,name,(String) null);
//}
//public ExtentTest  generateLog(Status status,String details){
//    Log log = Log.builder().status(status).details(details).build();
//    this.model.addGeneratedLog(log);
//    this.extent.onLogCreated(log,this.model);
//    return this;
//}
//public ExtentTest generateLog(Status status , Markup markup){
//    return this.generateLog(status,markup.getMarkup());
//}
//public ExtentTest log(Status status,String details , Throwable t,Media media) {
//    Assert.notNull(status, "Status must not be null");
//    Log log = Log.builder().status(status).details(details == null ? "" : details).build();
//    ExceptionInfo exceptionInfo = ExceptionInfoService.createExceptionInfo(t);
//    log.setException(exceptionInfo);
//    if (exceptionInfo != null) {
//        this.model.getExceptions().add(exceptionInfo);
//    }
//    this.model.addLog(log);
//    this.extent.onLogCreated(log, this.model);
//    if (media != null) {
//        log.addMedia(media);
//        this.extent.onMediaAdded(media, log, this.model);
//    }
//    return this;
//}
//    //100 line done
//    public ExtentTest log (Status status , String details, Media media){
//        return this.log(status,details,(Throwable) null,media);
//    }
//    public ExtentTest log (Status status ,  Media media){
//        return this.log(status,(String) null,(Throwable) null,media);
//    }
//    public ExtentTest log (Status status , String details){
//        return this.log(status,(String) details,(Media) null);
//    }
//public ExtentTest log(Status status , Markup markup, Media media){
//        String details = markup.getMarkup();
//        return this.log(status,details);
//    }
//    public ExtentTest log(Status status , Markup markup){
//        String details = markup.getMarkup();
//        return this.log(status,details);
//    }
//    public ExtentTest log(Status status ,Throwable t, Media media){
//        return this.log(status,(String)null,t ,media);
//    }
//    public ExtentTest log(Status status ,Throwable t){
//        return this.log(status,(Throwable) t ,(Media) null);
//    }
//    public ExtentTest info(String details , Media media){
//         this.log(Status.INFO,details ,media);
//         return this;
//    }
//    public ExtentTest info (String details){return this.info((String)details,(Media) null);
//    }
//    public ExtentTest info(Throwable t , Media media){
//        this.log(Status.INFO,t ,media);
//        return this;
//    }
//    public ExtentTest info(Throwable t){
//        return this.info((Throwable)t , (Media) null);
//    }
//    public ExtentTest info(Markup m){
//       this.log(Status.INFO,m);
//       return this;
//    }
//    public ExtentTest info(Media media){
//        this.log(Status.INFO,media);
//        return this;
//    }
//    public ExtentTest pass(String details,Media media){
//        this.log(Status.PASS,details,media);
//        return this;
//    }
//    public ExtentTest pass(String details){
//        return this.pass((String) details ,(Media) null);
//    }
//    public ExtentTest pass(Throwable t, Media media){
//        this.log(Status.PASS,t,media);
//        return this;
//    }
//    public ExtentTest pass(Throwable t){
//        return this.pass((Throwable) t,(Media) null);
//    }
//    public ExtentTest pass(Markup m){
//      this.log(Status.PASS,m);
//      return this;
//    }
//    public ExtentTest pass(Media media){
//        this.log(Status.PASS,media);
//        return this;
//    }
//
//
//
//    public ExtentTest fail(String details,Media media){
//        this.log(Status.FAIL,details,media);
//        return this;
//    }
//    public ExtentTest fail(String details){
//        return this.fail((String) details ,(Media) null);
//    }
//    public ExtentTest fail(Throwable t, Media media){
//        this.log(Status.FAIL,t,media);
//        return this;
//    }
//    public ExtentTest fail(Throwable t){
//        return this.fail((Throwable) t,(Media) null);
//    }
//    public ExtentTest fail(Markup m){
//        this.log(Status.FAIL,m);
//        return this;
//    }
//    public ExtentTest fail(Media media){
//        this.log(Status.FAIL,media);
//        return this;
//    }
//    public ExtentTest warning(String details,Media media){
//        this.log(Status.WARNING,details,media);
//        return this;
//    }
//    public ExtentTest warning(String details){
//        return this.warning((String) details ,(Media) null);
//    }
//    public ExtentTest warning(Throwable t, Media media){
//        this.log(Status.WARNING,t,media);
//        return this;
//    }
//    public ExtentTest warning(Throwable t){
//        return this.warning((Throwable) t,(Media) null);
//    }
//    public ExtentTest warning(Markup m){
//        this.log(Status.WARNING,m);
//        return this;
//    }
//    public ExtentTest warning(Media media){
//        this.log(Status.WARNING,media);
//        return this;
//    }
//    public ExtentTest skip(String details,Media media){
//        this.log(Status.SKIP,details,media);
//        return this;
//    }
//    public ExtentTest skip(String details){
//        return this.skip((String) details ,(Media) null);
//    }
//    public ExtentTest skip(Throwable t, Media media){
//        this.log(Status.SKIP,t,media);
//        return this;
//    }
//    public ExtentTest skip(Throwable t){
//        return this.skip((Throwable) t,(Media) null);
//    }
//    public ExtentTest skip(Markup m) {
//        this.log(Status.SKIP, m);
//        return this;
//    }
//
//    public ExtentTest skip(Media media){
//        this.log(Status.SKIP,media);
//        return this;
//    }
//    public ExtentTest assignCategory(String... category){
//        if(category!=null && category.length !=0){
//            Arrays.stream(category).forEach((x)->{
//                Category c = new Category(x.replaceAll("\\s+",""));
//                this.model.getCategorySet().add(c);
//                this.extent.onCategoryAdded(c,this.model);
//            });
//            return this;
//        }else{
//            return  this;
//        }
//    }
//    public ExtentTest assignCategory(String... author){
//        if(author!=null && author.length !=0){
//            Arrays.stream(author).forEach((x)->{
//                Author a  = new Author(x.replaceAll("\\s+",""));
//                this.model.getAuthorSet().add(a);
//                this.extent.onAuthorAdded(a,this.model);
//            });
//            return this;
//        }else{
//            return  this;
//        }
//    }
//    public ExtentTest assignCategory(String... device){
//        if(device!=null && device.length !=0){
//            Arrays.stream(device).forEach((x)->{
//                Device d  = new Device(x.replaceAll("\\s+",""));
//                this.model.getDeviceSet().add(d);
//                this.extent.onDeviceAdded(d,this.model);
//            });
//            return this;
//        }else{
//            return  this;
//        }
//    }
//    public Status getStatus(){
//        return this.model.getStatus();
//    }
//    public ExtentTest addScreenCaptureFromPath(String path , String title){
//    Assert.notEmpty(path,"Screen capture path must not be null or empty");
//    Media m = ScreenCapture.builder().path(path).title(title).build();
//    this.model.addMedia(m);
//    this.extent.onMediaAdded(m,this.model);
//    return this;
//    }public ExtentTest addScreenCaptureFromPath(String path){
//
//        return this.addScreenCaptureFromPath(path,(String) null);
//    }
//    public ExtentTest addScreenCaptureFromBase64String(String base64 , String title){
//        Assert.notEmpty(base64,"Screen capture path must not be null or empty");
//        if(!base64.startsWith("data:")){
//            base64= "data:image/png;base64,"+base64;
//        }
//        Media m = ScreenCapture.builder().base64(base64).title(title).build();
//        this.model.addMedia(m);
//        this.extent.onMediaAdded(m,this.model);
//        return this;
//    }
//    public ExtentTest addScreenCaptureFromBase64String(String base64){
//
//        return this.addScreenCaptureFromPath(base64,(String) null);
//    }
//
//
//
//   /* public ExtentTest addVideoFromPath(String path , String title){
//        Assert.notEmpty(path,"Screen capture path must not be null or empty");
//        Media m = Video.builder().path(path).title(title).build();
//        this.model.addMedia(m);
//        this.extent.onMediaAdded(m,this.model);
//        return this;
//    }public ExtentTest addVideoFromPath(String path){
//
//        return this.addVideoFromPath(path,(String) null);
//    }
//    public ExtentTest addVideoFromBase64String(String base64 , String title){
//        Assert.notEmpty(base64,"Screen capture path must not be null or empty");
//        if(!base64.startsWith("data:")){
//            base64= "data:image/png;base64,"+base64;
//        }
//        Media m = Video.builder().base64(base64).title(title).build();
//        this.model.addMedia(m);
//        this.extent.onMediaAdded(m,this.model);
//        return this;
//    }
//    public ExtentTest addVideoFromBase64String(String base64){
//
//        return this.addVideoFromBase64String(base64,(String) null);
//    }
//    public ExtentTest addMedia(Media m){
//    this.model.addMedia(m);
//    this.extent.onMediaAdded(m,this.model);
//    return this;
//    }*/
//    @Generated
//    public ExtentReports getExtent(){
//    return this.extent;
//    }
//    @Generated
//    public Test getModel(){
//    return  this.model;
//    }
//}
//
//
//
//
//
