package com.amex.tp.common;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.testng.TestNG;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import org.testng.collections.Lists;



public class TPController {
  @Test
  public void main() throws IOException, JAXBException {
	  
	 // TestListenerAdapter tla = new TestListenerAdapter();
	 // BasicConfigurator.configure();
	  System.out.println("Automated testng xml creation starts here");
	  TestNG tng = new TestNG();
	  List<String> suites = Lists.newArrayList();
	  RunId rd= new RunId();
	  rd.generateRunId();
	  XMLGen dynamicxml=new XMLGen(true);
	  String packname = FrameworkConstants.Pack;
	  dynamicxml.readExcel(FrameworkConstants.ExecutionSheet, "testngTP.xml",packname);
	  
	  suites.add("testngTP.xml");
	   
	 tng.setTestSuites(suites);
	 tng.run();
  }
 
}

