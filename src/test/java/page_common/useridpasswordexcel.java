package page_common;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class useridpasswordexcel {
    public static int getColumnNumberFromExcel(XSSFSheet sheet , String colId){
        int colNum=0;
        Iterator<Row> rowIterator  = sheet.iterator();
        int rowCount=0;
        int colCount=0;
        boolean rowFound=false;
        while(rowIterator.hasNext() && rowCount<1){
            rowCount=rowCount+1;
            Row row = rowIterator.next();
            Iterator<Cell>cellIterator=row.cellIterator();
            while (cellIterator.hasNext()){
                colCount=colCount+1;
                Cell cell = cellIterator.next();
                switch (cell.getCellType()){
                    case NUMERIC:
                        double valueToCompare = cell.getNumericCellValue();
                        double colIdInNumber = Double.parseDouble(colId);
                        if(valueToCompare==colIdInNumber){
                            colNum=colCount;
                        }
                        break;
                    case STRING:
                        if(cell.getStringCellValue().equalsIgnoreCase(colId)){
                            colNum=colCount;
                        }
                        break;
                    default:
                        break;
                }
                if(colNum>0){
                    break;
                }
            }
        }
        return colNum;
    }
    public static int getRowNumberFomExcel(XSSFSheet sheet , String rowId){
        int rowNum=0;
        boolean rowFound=false;
        Iterator<Row> rowIterator= sheet.iterator();
        int rowCount=0;
        int colCount=0;
        while(rowIterator.hasNext()){
            colCount=0;
            rowCount=rowCount+1;
            Row row = rowIterator.next();
            Iterator<Cell>cellIterator=row.cellIterator();
            while (cellIterator.hasNext() &&colCount<1){
                colCount=colCount+1;
                Cell cell = cellIterator.next();
                switch (cell.getCellType()){
                    case NUMERIC:
                        double valueToCompare = cell.getNumericCellValue();
                        double colIdInNumber = Double.parseDouble(rowId);
                        if(valueToCompare==colIdInNumber){
                            rowNum=rowCount;
                            rowFound=true;
                        }
                        break;
                    case STRING:
                        if(cell.getStringCellValue().equalsIgnoreCase(rowId)){
                            rowNum=colCount;
                            rowFound=true;
                        }

                        break;
                    default:
                        break;
                }
                if(rowFound){
                    break;
                }
            }
        }
        return rowNum;
    }
    public static String getValueByCellNumFromExcel(XSSFSheet sheet ,int rowNum,int colNum){
        String value="";
        Iterator<Row> rowIterator =sheet.iterator();
        int rowCount=0;
        int colCount=0;
        while ((rowIterator.hasNext())){
            Row row = rowIterator.next();
            if(++rowCount<rowNum){
                continue;
            } else if (rowCount>rowNum) {
                break;
            }
            Iterator<Cell>cellIterator= row.cellIterator();
            while(cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                if(++colCount<colNum){
                    continue;
                } else if (colCount>colNum) {
                    break;

                }switch (cell.getCellType()){
                    case NUMERIC:
                        value=cell.getNumericCellValue()+"";
                        break;
                    case STRING:
                        value=cell.getStringCellValue();
                        break;
                    default:
                        break;
                }
            }
        }
        return value;
    }
    public static String getValueFromExcel(String excelName,String sheetName,String columnName,String rowId) throws IOException {
        String value ="";
        int rowNum=0;
        int colNum=0;
        FileInputStream file = new FileInputStream(new File(excelName));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(sheetName);
        colNum=getColumnNumberFromExcel(sheet,columnName);
        rowNum= getRowNumberFomExcel(sheet,rowId);
        value = getValueByCellNumFromExcel(sheet,rowNum,colNum);
        workbook.close();
        file.close();
        return value;

    }
}
