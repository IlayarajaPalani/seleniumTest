
/* 
 Description : This module generates the xml by from the input data  provided in the excel sheet
  
 */

package com.amex.tp.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.LoggerFactory;



public class XMLGen {
	static String Parallelism;
	static boolean parallel = false;
	boolean status = false;
	public static org.slf4j.Logger logger = LoggerFactory.getLogger(XMLGen.class);
	public XMLGen(boolean parallelval) {
		// TODO Auto-generated constructor stub
		Parallelism = "tests" ; 
		parallel = parallelval;
	}
	public static void main(String[] args){
		// TODO Auto-generated method stub
	
		
        XMLGen xml = new XMLGen(false);
        xml.readExcel("./test-data/ExecutionEngine.xls", "TEST.xml","packname");
		
		String testcasename = null;
	
	}

	//  This method generates the xml
	
	public boolean readExcel(String excelfilepath, String xmlfilepath, String packname){
		logger.info("XML generation got started ");
		logger.info(" excelfilepath : "+excelfilepath);
		logger.info("xmlfilepath : "+xmlfilepath);
		
		String testcasename = null;
		String method = null;
		String basefile = null;
		HSSFWorkbook workbook;
		HSSFSheet worksheet = null;
		Map map = new LinkedHashMap();
		ArrayList al = new ArrayList();
		Map datamap = new HashMap();
		String Testcasename = null;
		StringBuilder sb = new StringBuilder();
		
		try{
		FileInputStream inputStream = new FileInputStream(new File(excelfilepath));
		workbook = new HSSFWorkbook(inputStream);
		worksheet = workbook.getSheet("Sheet1");
		}
		catch(IOException e){
			e.printStackTrace();
			logger.info("The exception: "+e.getMessage());
		}
		sb.append("<?xml version="+"\""+"1.0"+"\""+" encoding="+"\""+"UTF-8"+"\""+"?>");
		sb.append("\n");
		sb.append("<!DOCTYPE suite SYSTEM "+"\""+"http://testng.org/testng-1.0.dtd"+"\""+">");
		sb.append("\n");
		if(parallel){
			sb.append("<suite name="+"\""+"SFTTest"+"\""+" "+"parallel="+"\""+Parallelism+"\""+" "+"thread-count="+"\""+"1"+"\"" +">");
			sb.append("\n");
		}
		else{
			sb.append("<suite name="+"\""+"SFTTest"+"\""+" "+"parallel="+"\""+"none"+"\""+">");
			sb.append("\n");
		}
		Iterator<Row> rowiterator = worksheet.rowIterator();
		while(rowiterator.hasNext()){
			Row row = rowiterator.next();
			short cellnum = row.getLastCellNum();
			int cellno = (int) (cellnum);
			//System.out.println(cellno);
			int rownum = row.getRowNum();
			if(rownum>0 && row.getCell(0).getStringCellValue().equalsIgnoreCase("Yes")){
				sb.append("<test name="+"\""+"Test"+rownum+"\""+">");
				sb.append("\n");
			for(int j =0; j<cellno; j++)
			{
				Cell col = row.getCell(j);
				switch(j){
				case 1:
					map.put("TestCaseName",col);
					testcasename = packname+col.getStringCellValue();
					break;
				case 2:
					map.put("Browser",col);
					break;

				}			
			}
			
			
			
			
		    Iterator mapiterator = map.entrySet().iterator();      	 
	        while (mapiterator.hasNext())
	        {
	        	Map.Entry pair = (Map.Entry)mapiterator.next();   
	            sb.append("<parameter name=");
	            sb.append("\""+pair.getKey()+"\"");
	            sb.append(" value=");
	            sb.append("\""+pair.getValue()+"\""+">");
	            sb.append("</parameter>");
	            sb.append("\n");
	            
			}
	        
	        sb.append("<classes>");
	        sb.append("\n");
	        sb.append("<class name="+"\""+testcasename+"\""+"/>");
	        sb.append("\n");
	        sb.append("</classes>");
	        sb.append("\n");
	        sb.append("</test>");
	        sb.append("\n");
	        }     
}	
		
		/*sb.append("<test name=\"CompleteTest\">");
        sb.append("\n");
        sb.append("<parameter name=\"TestCaseName\" value=\"DriverManager\"></parameter>");
        sb.append("\n");
        sb.append("<parameter name=\"Browser\" value=\"Firefox\"></parameter>");
        sb.append("\n");
        sb.append("<classes>");
        sb.append("\n");
        sb.append("<class name=\"com.amex.tp.common.DriverManager\"/>");
        sb.append("\n");
        sb.append("</classes>");
        sb.append("\n");
        sb.append("</test>");
        sb.append("\n");*/
		sb.append("</suite>");
System.out.println(sb);
try{
File xmlfile = new File(xmlfilepath);
FileWriter fwriter = new FileWriter(xmlfile);
BufferedWriter bwriter = new BufferedWriter(fwriter);
bwriter.write(sb.toString());
bwriter.close();
status= true;
}
catch(IOException e){
	e.printStackTrace();
	System.err.println("Problem writing to the file"+xmlfilepath);
	logger.info("Problem writing to the file"+xmlfilepath);
	status= false;
}
System.out.println(status);
logger.info("The XML is generated successfully");
return status;
}
}