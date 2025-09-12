package utilites;

import utilites.exeptions.PropertyFileNotFoundException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigProvider {
    private static final String PROPERTIES_PATH = "/proeprties";
    private static Properties props;
    private static final Map<String,Properties> configMap= new HashMap<>();
    private static final Logger logger = Logger.getLogger(ConfigProvider.class.getName());
    private ConfigProvider(){

    }
    private static Properties getInstance(String properrtyFileName){
        Properties prop;
        if(configMap.isEmpty()){
            props = loadProperties(properrtyFileName);
            configMap.put(properrtyFileName,props);
            return props;
        }else{
            Iterator var2= configMap.entrySet().iterator();
            while(var2.hasNext()){
                Map.Entry entry = (Map.Entry) var2.next();
                if(entry.getKey().equals(properrtyFileName)){
                    return (Properties) entry.getValue();
                }
            }
            props = loadProperties(properrtyFileName);
            configMap.put(properrtyFileName,props);
            return props;
        }
    }
    public static Properties getInstance(){
        if(props ==null){
            props =loadProperties();
            return props;
        } else return props;
    }
    public static Properties loadProperties(){
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream in;
        try{
            in = loader.getClass().getResourceAsStream("/properties/NetxGen.properties");
            props.load(in);
        }catch (NullPointerException var7){
            logger.info("File not found search again");
       try{
           InputStream inputStreamNetxGen = ConfigProvider.class.getResourceAsStream("/properties/NetxGen.properties");
           props.load(inputStreamNetxGen);
           logger.info("File found");
       }catch (Exception var6){
           var6.printStackTrace();
       }
        }catch (Exception var8){
            var8.printStackTrace();
        }try{
            in =ConfigProvider.class.getResourceAsStream("/properties");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String resources;
            while((resources=br.readLine())!=null){
                logger.info("properties file found"+resources);
                InputStream is = ConfigProvider.class.getResourceAsStream("/properties"+resources);
                props.load(is);
            }
            br.close();

        }catch (NullPointerException var9){
            throw new PropertyFileNotFoundException("No property file found nder the folder please check");
        }catch (IOException var10){
            var10.printStackTrace();
        }
        return props;
    }
    private static Properties loadProperties(String propertyFile){
        Properties prop = new Properties();
        InputStream is = ConfigProvider.class.getResourceAsStream("/properties"+propertyFile+".properties");
        try{
            prop.load(is);
        }catch (NullPointerException var4){
            throw new PropertyFileNotFoundException("No property file found nder the folder please check");
        }catch (IOException var5){
            logger.warning("Not able to load the properties");
        }
return props;
    }
    public static String getAsString(String key){
        return getInstance().getProperty(key);
    }
    public static int getAsInt(String key){
        return Integer.parseInt(getInstance().getProperty(key));

    }

    public static String getAsString(String fileName,String key){
        return getInstance(fileName).getProperty(key);
    }
    public static int getAsInt(String fileName,String key){
        return Integer.parseInt(getInstance(fileName).getProperty(key));

    }
    public static String getAsString(String environment,String propertyFile,String key){
        Properties props = new Properties();
        InputStream inputStream = ConfigProvider.class.getResourceAsStream("/properties"+ File.separator+environment+"/"+propertyFile+".properties");
    String value =null;
    try{
        props.load(inputStream);
        value = props.getProperty(key);
        props.clear();
        inputStream.close();
    }catch (IOException var7){
        logger.log(Level.SEVERE,"an exception was thrown", var7);
    }
    return value;
    }
}
