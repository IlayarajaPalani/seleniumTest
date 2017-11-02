package amex.fs.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CompareExcel {

	public static org.slf4j.Logger logger=LoggerFactory.getLogger(CompareExcel.class);
	
	String file1, file2;
	FileInputStream excellFile1, excellFile2;
	
	/*CompareExcel(String file1, String file2) throws FileNotFoundException
	{
		this.file1=file1;
		this.file2=file2;
	}*/
	public CompareExcel(Logger logger)
	{
		this.logger=logger;
	}
	
	public static void main(String[] args) throws IOException {
    	
    	CompareExcel comp=new CompareExcel(logger);
    	boolean check=comp.compareTwoExcels("./One.xlsx", "./Two.xlsx");
    	System.out.println(check);
    	logger.info("check  "+check);
    	
    
    }
	
	public boolean compareTwoExcels(String file1, String file2) throws IOException
	{
       
	
        try
		{
        	 excellFile1 = new FileInputStream(new File(file1));
             excellFile2 = new FileInputStream(new File(file2));
            
             XSSFWorkbook workbook1 = new XSSFWorkbook(excellFile1);
             XSSFWorkbook workbook2 = new XSSFWorkbook(excellFile2);

             int file1sheets=workbook1.getNumberOfSheets();
             int file2sheets=workbook2.getNumberOfSheets();
             
             int maxsheetcount;
             
             if(file1sheets<file2sheets)
             {
            	 maxsheetcount=file2sheets;
             }else
             {
            	 maxsheetcount=file1sheets;
             }
             
             if(file1sheets==file2sheets)
             {
            	 for(int i=0;i<maxsheetcount;i++)
            	 {
            		
            		XSSFSheet sheet1 = workbook1.getSheetAt(i);
         			XSSFSheet sheet2 = workbook2.getSheetAt(i);

         			// Compare sheets
         			if(compareTwoSheets(sheet1, sheet2)) {
         				logger.info("Excel Comparison is success - Both the excel contents are same ");
         				return true;
         				//System.out.println("\n\nThe two excel sheets are Equal");
         			} else {
         				logger.info("Excel Comparison Failed - Excels content does not match ");
         				return false;
         				//System.out.println("\n\nThe two excel sheets are Not Equal");
         			}

            		 
            	 }
            	 
             }else
             {
            	 return false;
             }
             
             
			// Get first/desired sheet from the workbook
					}
        catch(Exception e)
        {
			System.out.println("Check-1"+e);
		}

        excellFile1.close();
        excellFile2.close();
		return false;
        
	}
	
    

    
    // Compare Two Sheets
    public static boolean compareTwoSheets(XSSFSheet sheet1, XSSFSheet sheet2) {
        int firstRow1 = sheet1.getFirstRowNum();
        int lastRow1 = sheet1.getLastRowNum();
        int secondsheetrow1=sheet2.getFirstRowNum();
        
        int secondsheetlastrow=sheet2.getLastRowNum();
        
        int lastRow;
        
        if(lastRow1>secondsheetlastrow)
        {
        	lastRow=lastRow1;
        }else
        {
        	lastRow=secondsheetlastrow;
        }
        
        
        boolean equalSheets = true;
        
        for(int i=firstRow1; i <= lastRow; i++) {
            
//            System.out.println("\n\nComparing Row "+i);
            
            XSSFRow row1 = sheet1.getRow(i);
            XSSFRow row2 = sheet2.getRow(i);
            if(!compareTwoRows(row1, row2)) {
                equalSheets = false;
              
               //System.out.println("Row "+i+" - Not Equal");
                break;
            } else {
               // System.out.println("Row "+i+" - Equal");
            }
        }
        return equalSheets;
    }

    // Compare Two Rows
    public static boolean compareTwoRows(XSSFRow row1, XSSFRow row2) {
        if((row1 == null) && (row2 == null)) {
            return true;
        } else if((row1 == null) || (row2 == null)) {
            return false;
        }
        
        int firstCell1 = row1.getFirstCellNum();
        int lastCell1 = row1.getLastCellNum();
        boolean equalRows = true;
        
        // Compare all cells in a row
        for(int i=firstCell1; i <= lastCell1; i++) {
            XSSFCell cell1 = row1.getCell(i);
            XSSFCell cell2 = row2.getCell(i);
            if(!compareTwoCells(cell1, cell2)) {
                equalRows = false;
                System.err.println("       Cell "+i+" - NOt Equal");
                break;
            } else {
                System.out.println("       Cell "+i+" - Equal");
            }
        }
        return equalRows;
    }

    // Compare Two Cells
    public static boolean compareTwoCells(XSSFCell cell1, XSSFCell cell2) {
        if((cell1 == null) && (cell2 == null)) {
            return true;
        } else if((cell1 == null) || (cell2 == null)) {
            return false;
        }
        
        boolean equalCells = false;
        int type1 = cell1.getCellType();
        int type2 = cell2.getCellType();
        if (type1 == type2) {
            if (cell1.getCellStyle().equals(cell2.getCellStyle())) {
                // Compare cells based on its type
                switch (cell1.getCellType()) {
                case HSSFCell.CELL_TYPE_FORMULA:
                    if (cell1.getCellFormula().equals(cell2.getCellFormula())) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    if (cell1.getNumericCellValue() == cell2.getNumericCellValue()) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    if (cell1.getStringCellValue().equals(cell2.getStringCellValue())) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    if (cell2.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    if (cell1.getBooleanCellValue() == cell2.getBooleanCellValue()) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    if (cell1.getErrorCellValue() == cell2.getErrorCellValue()) {
                        equalCells = true;
                    }
                    break;
                default:
                    if (cell1.getStringCellValue().equals(
                            cell2.getStringCellValue())) {
                        equalCells = true;
                    }
                    break;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return equalCells;
    }
}