package excelreader;

import annotation.ExcelDetails;
import com.fasterxml.jackson.databind.introspect.Annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ExcelDataProvider {
    private final ThreadLocal<String> classExcelName= new ThreadLocal<>();
    private final ThreadLocal<String> classSheetName= new ThreadLocal<>();
    private final ThreadLocal<String> methodExcelName= new ThreadLocal<>();
    private final ThreadLocal<String> methodSheetName= new ThreadLocal<>();
    private final ThreadLocal<Class<?>>testClass= new ThreadLocal<>();
    public  ExcelDataProvider(Class<?> testClass){this.testClass.set(testClass);}
public void getExcelDetailsFromClass(){
        if(((Class)this.testClass.get()).isAnnotationPresent(ExcelDetails.class)){
            Annotation annotation =((Class)this.testClass.get()).getAnnotation(ExcelDetails.class);
            ExcelDetails excelDetails = (ExcelDetails) annotation;

            if(excelDetails.excelName().isEmpty()){
                this.classExcelName.set(((Class)this.testClass.get()).getSimpleName());
            }else{
                this.classExcelName.set(excelDetails.excelName());
            }this.classSheetName.set(excelDetails.sheetName());
        }
}
private void getDetailsFromMethod(Method method){
        if(method.isAnnotationPresent(ExcelDetails.class)){
            Annotation annotation =method.getAnnotation(ExcelDetails.class);
            ExcelDetails excelDeatils = (ExcelDetails) annotation;
            if(excelDeatils.excelName().isEmpty()){
                this.methodExcelName.set(method.getName());
            }else{
                this.methodExcelName.set(excelDeatils.excelName());
            }
            this.methodSheetName.set(excelDeatils.sheetName());
        }
}
public Object[][] data(Method method){
        this.getDetailsFromMethod(method);
        if(this.methodExcelName.get()!=null && this.methodSheetName.get()!=null){
            return ReadExcel.getData((String) this.methodExcelName.get(),(String) this.methodSheetName.get());
        }else{
            this.getExcelDetailsFromClass();;
            return ReadExcel.getData((String) this.classExcelName.get(),(String) this.classExcelName.get());
        }
}
}
