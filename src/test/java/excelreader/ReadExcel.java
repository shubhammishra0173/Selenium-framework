package excelreader;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class ReadExcel {
    private static final ThreadLocal<XSSFWorkbook> xssfWorkbook=new ThreadLocal<>();
    private static final DataFormatter dataFormatter=new DataFormatter();
    private static final Logger logger=Logger.getLogger(ReadExcel.class.getName());
    private static final ThreadLocal<XSSFSheet> xssfSheet=new ThreadLocal<>();
    private static final ThreadLocal<InputStream> fis=new ThreadLocal<>();

    private ReadExcel(){

    }
    public static void setup(String fileName,String sheetName) throws IOException {
        InputStream inputStreamXls= ReadExcel.class.getResourceAsStream("/data/"+fileName+ ".xls");
        InputStream inputStreamXlsx= ReadExcel.class.getResourceAsStream("/data/"+fileName+ ".xlsx");
    if(inputStreamXls==null){
        fis.set(inputStreamXls);
    }else{
        if(inputStreamXlsx==null){
            throw new IOException("ExcelDetails annotation may be missing or excel sheet doesnt Exist");

        }
        fis.set(inputStreamXlsx);
    }

XSSFWorkbook xssfWorkbook= new XSSFWorkbook((InputStream)fis.get());
    xssfSheet.set(xssfWorkbook.getSheet(sheetName));
    xssfWorkbook.close();
    }
    public static Object[][] readData(String[]excelInfo){
        String excelName = excelInfo[0];
        String sheetName = excelInfo[1];
        List<Object> results = new ArrayList<>();
        try{
            setup(excelName,sheetName);
            int numRows = ((XSSFSheet)xssfSheet.get()).getLastRowNum();
            for(int i =1;i<=numRows;++i){
                Map<String,String> inputvalues = getHashMapDataFromRow((Sheet)xssfSheet.get(),i);
                  results.add(new Object[]{inputvalues});
            }
        }catch (IOException e){
            logger.warning(e.getMessage());
        }finally {
            IOUtils.closeQuietly((Closeable) fis.get());
        }
        return results.toArray(new Object[0][]);
    }
    public static List<HashMap<String,String >> readData(String excelName, String sheetName){
        logger.info("Before Read data: "+Thread.currentThread().getId());
        List<HashMap<String,String>> excelData = new ArrayList<>();
        try{
            setup(excelName,sheetName);

        }catch (IOException e){
            logger.warning(e.getMessage());
        }finally {
            IOUtils.closeQuietly((Closeable) fis.get());
        }
        return excelData;
    }
    private static HashMap<String,String > getHashMapDataFromRow(Sheet sheet,int rowIndex){
        HashMap<String,String> results = new HashMap<>();
        String[]columnHeaders=getDataFromRow(sheet,0);
        String[]valueFromRow=getDataFromRow(sheet,rowIndex);
    for(int i =0;i<columnHeaders.length;++i){
        if(i>=valueFromRow.length){
            results.put(columnHeaders[i],"");
        }else {
            results.put(columnHeaders[i],valueFromRow[i]);
        }
    }
    return results;
    }
    private static String[]getDataFromRow(Sheet sheet, int rowIndex){
        FormulaEvaluator formulaEvaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
        Row row = sheet.getRow(rowIndex);
        short numCells= row.getLastCellNum();
        String[]result = new String[numCells];
        for(int i =0;i<numCells;++i){
            result[i]= getValueAsString(row.getCell(i),formulaEvaluator);
        }
        return result;
    }
    private static String getValueAsString(Cell cell , FormulaEvaluator formulaEvaluator){
        if(cell!=null){
            CellType cellType = cell.getCellType();
            if(cellType.equals(CellType.BOOLEAN)){
                return String.valueOf(cell.getBooleanCellValue());

            }
            if(cellType.equals(CellType.NUMERIC)){
                return dataFormatter.formatCellValue(cell);

            }
            if(cellType.equals(CellType.STRING)){
                return cell.getRichStringCellValue().getString();

            }
            if(cellType.equals(CellType.FORMULA)){
                return formulaEvaluator.evaluate(cell).getStringValue();

            }
        }
        return "";
    }
    public static Object[][]getData(String excelName,String sheetName)
    {
        List<Object[]>results= new ArrayList<>();
        try{
            setup(excelName,sheetName);
            int numRows=((XSSFSheet)xssfSheet.get()).getLastRowNum();
            for(int i=1;i<=numRows;++i){
                Map<String,String>inputValues = getMapDataFromRow((Sheet)xssfSheet.get(),i);
                results.add(new Object[]{inputValues});
            }
        }catch (IOException e){
            logger.warning(e.getMessage());
        }finally {
            IOUtils.closeQuietly((Closeable) fis.get());
        }
        return results.toArray(new Object[0][]);
    }
public static int getRowCount(File file , String sheetName){
    int numRows =0;
    try{
        if(file.exists()){
            fis.set(new FileInputStream(file));
            xssfWorkbook.set(new XSSFWorkbook((InputStream)fis.get()));
            xssfSheet.set(((XSSFWorkbook)xssfWorkbook.get()).getSheet(sheetName));
            Logger var10000=logger;
            String var10001=file.getName();
            var10000.info("Number of Rows in the excel sheet"+var10001+ "is"+numRows);
            ((XSSFWorkbook)xssfWorkbook.get()).close();
        }
    }catch (Exception e){
        logger.warning(e.getMessage());
    }
    return numRows;
}
private static Map<String ,String>getMapDataFromRow(Sheet sheet , int rowIndex){
        Map<String,String> results = new LinkedHashMap();
        String[]columnHeaders = getDataFromRow(sheet,0);
        String []valueFromRow = getDataFromRow(sheet,rowIndex);
        for(int i =0;i<columnHeaders.length;++i){
            if(i>=valueFromRow.length){
                results.put(columnHeaders[i],"");

            }else{
                results.put(columnHeaders[i],valueFromRow[i]);
            }
        }
        return results;
}
}
