package stepDefinition_common;

import com.aventstack.extentreports.gherkin.model.Scenario;
import excelreader.ReadExcel;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultStepDefinition {
    private static ThreadLocal<String> testDataId =new ThreadLocal<>();
    private static ThreadLocal<String> scenarioName = new ThreadLocal<>();
    private static ThreadLocal<String> excel = new ThreadLocal<>();
    private static ThreadLocal<String> sheet = new ThreadLocal<>();
    private static ThreadLocal<ArrayList<Map<String,String>>> excelData= new ThreadLocal<ArrayList<Map<String,String>>>()
    {
        @Override
        protected ArrayList<Map<String,String>> initialValue(){
            return  new ArrayList<>();
        }
    };
    public static  ThreadLocal<Map<String,String>> currentIterationMap = new ThreadLocal<>();
    @Before
    public void readScenario(Scenario scenario){
        testDataId.set(scenario.getGherkinName());
    }
    @Given("a workbook named {string} and sheet named {string} is read")
    public synchronized void a_workbook_named_sheet_name_is_read(String excelName , String sheetName){
        if(scenarioName.get()==null || !sheetName.equals(sheet.get())||!excelName.equals(excel.get())||!((String)testDataId.get()).equals(scenarioName.get())){
            excelData.get().addAll(ReadExcel.readData(excelName,sheetName));
        }
        List<Map<String,String>> removeData = new ArrayList<>();
        for(Map<String,String>map: excelData.get()){
            if(map.get("testDataID").equals(testDataId.get())){
                currentIterationMap.set(map);
                removeData.add(map);
                break;
            }
        }
        if(!removeData.isEmpty()){
            excelData.get().remove(removeData.get(0));
        }
        scenarioName.set(testDataId.get());
        sheet.set(sheetName);
        excel.set(excelName);
    }
    public String returnScenarioName(){
        return  scenarioName.get();
    }
}
