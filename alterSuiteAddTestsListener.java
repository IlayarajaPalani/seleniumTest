package com.amex.tp.common;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class alterSuiteAddTestsListener implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> suites) {
        XmlSuite suite = suites.get(0);
       
        
        addTestsFromExcel(FrameworkConstants.ExecutionSheet, FrameworkConstants.Pack, suite);
        addTestsFromExcel(FrameworkConstants.ExecutionSheet_maintainer, FrameworkConstants.Pack1, suite);
        addTestsFromExcel(FrameworkConstants.ExecutionSheet_Profmaintainer, FrameworkConstants.Pack2, suite);
        addTestsFromExcel(FrameworkConstants.ExecutionSheet_wgadmin, FrameworkConstants.Pack3, suite);
    }

    private void addTestsFromExcel (String excelSheet, String packageValue, XmlSuite suite) {
        try (
                FileInputStream inputStream = new FileInputStream(new File(excelSheet));
                HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        ) {
            HSSFSheet worksheet = workbook.getSheet("Sheet1");
            Iterator<Row> rowIterator = worksheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell boolCell = row.getCell(0);
                Cell tcNameCell = row.getCell(1);
                Cell browserCell = row.getCell(2);

                if (boolCell.getStringCellValue().equalsIgnoreCase("yes")) {
                    XmlTest test = new XmlTest(suite);

                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("TestCaseName", tcNameCell.getStringCellValue());
                    parameters.put("Browser", browserCell.getStringCellValue());
                    test.setParameters(parameters);

                    XmlClass testClass = new XmlClass(Class.forName(packageValue + tcNameCell.getStringCellValue()));
                    List<XmlClass> testClassList = new ArrayList<>();
                    testClassList.add(testClass);
                    test.setClasses(testClassList);

                    //suite.addTest(test);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
