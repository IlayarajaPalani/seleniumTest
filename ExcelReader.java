package com.amex.tp.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader
{

    public static HashMap loadExcelLines(String fileName, String dataneed)
    {
        // Used the LinkedHashMap and LikedList to maintain the order
        HashMap<String, String> outerMap = new LinkedHashMap<String, String>();

        //LinkedHashMap<Integer, List> hashMap = new LinkedHashMap<Integer, List>();

        String sheetName = null;
        // Create an ArrayList to store the data read from excel sheet.
        // List sheetData = new ArrayList();
        FileInputStream fis = null;
        
        try
        {
        	
            fis = new FileInputStream(fileName);
            // Create an excel workbook from the file system
            HSSFWorkbook  workBook = new HSSFWorkbook(fis);
            // Get the first sheet on the workbook.
            HSSFSheet sheet = workBook.getSheetAt(0);
                // XSSFSheet sheet = workBook.getSheetAt(0);
                sheetName = workBook.getSheetName(0);
                
                Iterator rows = sheet.rowIterator();
                for ( int i = 0 ; i <= sheet.getLastRowNum() ; i++ )
                {
                    Row row = sheet.getRow(i);

                    if (i > 0) //skip first row
                    {

                    	Cell testname = row.getCell(1);
                    	String Testname = testname.toString();
                    	
                         
                    	switch(dataneed) {
                    	case "PUBLICKEY":
                    		Cell value	   = row.getCell(3);
                    		//System.out.println(value);
                            String Value      = value.toString();
                            //System.out.println(testname+": "+" "+Value);
                            
                            
                            outerMap.put(Testname, Value);
                            break;
                    	case "PRIVKEY":
                    		Cell privvalue	   = row.getCell(4);
                            String PrivValue      = privvalue.toString();
                            //System.out.println(PrivValue);
                            //System.out.println(testname);
                            outerMap.put(Testname, PrivValue);
                            break;
                    	case "SIGN":
                    		Cell signvalue	   = row.getCell(5);
                            String SignValue      = signvalue.toString();
                            //System.out.println(SignValue);
                            //System.out.println(testname);
                            outerMap.put(Testname, SignValue);
                            break;
                    	case "CERT":
                    		Cell certvalue	   = row.getCell(6);
                            String CertValue      = certvalue.toString();
                            //System.out.println(CertValue);
                            //System.out.println(testname);
                            outerMap.put(Testname, CertValue);
                            break;            	
                    	
                    	
                    	}
                    	}
                        //outerMap.put(sheet.getRow(i), Key);
                        

                }
                                   

                    // sheetData.add(data);
               
                /*outerMap.put(sheetName, hashMap);
                hashMap = new LinkedHashMap<Integer, List>();
*/
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return outerMap;

    }
}
