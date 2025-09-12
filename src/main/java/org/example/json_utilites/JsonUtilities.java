package org.example.json_utilites;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.cucumber.Log;
import org.json.JSONException;
import org.openqa.selenium.json.JsonException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;



public class JsonUtilities {
    public static void main(String[] args) throws IOException, JSONException {
        String expectedJsonPath = ".\\src\\test\\resources\\data\\test.json";
        String actualJsonPath = ".\\src\\test\\resources\\data\\test1.json";
        CompareTwoJson(expectedJsonPath,actualJsonPath);
        //=========================
        String jsonPath = ".\\account-tolerence.json";
        String keyPath = "data[0].toleranceValue";
        String keyValue = GetKeyValueFromJson(jsonPath,keyPath);
        Log.info("Key path is "+keyPath +"&& "+ "key value is "+keyValue);
        //==============================================================
        jsonPath = ".\\src\\test\\resources\\data\test.json";
        keyPath="sequence[0].models[0].latestModelVersion";
        keyValue="121212";

        try{
            String updateJSON = updateJSONathValue(jsonPath,keyPath,keyValue);
            Log.info(updateJSON);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static String GetKeyValueFromJson(String jsonPath , String keyPath){
        String jsonString;
        String keyValue = "Error: key not found in JSON";
        try{
            jsonString = new String(Files.readAllBytes(Paths.get(jsonPath)), StandardCharsets.UTF_8);
            keyValue= JsonPath.read(jsonString,"$."+keyPath).toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return keyValue;
    }
    public static String updateJSONathValue(String jsonPath ,String keyPath ,String keyValue) throws IOException {
        String strJson = new String(Files.readAllBytes(Paths.get(jsonPath)), StandardCharsets.UTF_8);
        String path = keyPath;
        String value =keyValue;
        String updateJson =null;
        Configuration configuration = Configuration.builder().build();
        if(NumberUtils.isNumber(value)){
            int value1 = Integer.parseInt(value);
            updateJson= JsonPath.using(configuration).parse(strJson).set(path,value1).jsonString();
        }else{
            updateJson= JsonPath.using(configuration).parse(strJson).set(path,value).jsonString();
        }
        Log.info("Updated Json="+updateJson);
        return updateJson;
    }
    private static void CompareTwoJson(String expectedJsonpath,String actualJsonPath) throws IOException, JSONException {
        String diffJsonRow = null;
        ObjectMapper mapper = new ObjectMapper();
        String failureMessage= "Error: ";
        File exposedFile = new File(expectedJsonpath);
        File actualFile = new File(actualJsonPath);
        try(FileWriter writer = new FileWriter("JsonDiff.txt",false)){
            try{
                JsonNode expectedNode,actualNode;
            expectedNode = mapper.readTree(exposedFile);
            actualNode = mapper.readTree(actualFile);
            boolean statusAfterCompare = actualNode.equals(expectedNode);
            if(statusAfterCompare)
            {
                Log.info("Both Json Are equal");
                writer.write("Both actual and expected JSON are equal");
                writer.close();
            }else{
                Log.info("Actual and expected JSons are not equal");
                JSONAssert.assertEquals(failureMessage, expectedNode.toString(), actualNode.toString(), JSONCompareMode.STRICT);

            }
            }catch (AssertionError e){
                diffJsonRow =e.getMessage();
                String replaceText = diffJsonRow.replace("Error: ","");
                String [] diffRaw = replaceText.split(" ; ");
                int i =0;
                writer.write("Total number of difference are: "+diffRaw.length+"\n");
                while(i<diffRaw.length){
                    writer.write("=============================\n");
                    writer.write("key("+i+"): "+diffRaw[i]);
                    writer.write("\n");
                    i++;
                }
            }catch (JsonException e){
                Log.error(e.getMessage());
            }catch (JsonProcessingException e){
                Log.error(e.getMessage());
            }catch (IOException e){
                Log.error(e.getMessage());
            }
        }
    }
}
