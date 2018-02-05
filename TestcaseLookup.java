/*
 * Description:
 * This moduleis to get the testcase associated with the particular group, before uploading to ALM
 */

package com.amex.tp.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestcaseLookup {
	String line =null;
	boolean status = false;
	static Logger logger=null;
	List<String> lineList = null;
	
	public TestcaseLookup(org.slf4j.Logger logger )
	{
		this.logger=logger;
	}
	
	

	public static void main(String[] args) {
		TestcaseLookup tc = new TestcaseLookup(logger);
		try {
			List lt = tc.lookupTestcase(FrameworkConstants.TCLookup, "G1");
			System.out.println("list"+lt);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/* Test case lookup module, finds the test case by group name
	 * 
	 */
	public List<String> lookupTestcase(String filename, String groupname) throws IOException{
		logger.info(" The test case lookup module begins..");	
		try {
				File f = new File(filename);
				if(f.exists()){
					System.out.println("Lookup File"+filename);				
					FileReader fr = new FileReader(f);
					BufferedReader br = new BufferedReader(fr);
					lineList = new ArrayList<String>();
					while((line= br.readLine()) != null){
						String[] str = line.split(";");
						if(str[1].equals(groupname)){
							lineList.add(str[0]);
							System.out.println(str[0]);
							//logger.info("Test cases associated with the "+groupname+"-"+str[0]);
							System.out.println("Test cases associated with the "+groupname+"-"+str[0]);
						}
						}
					br.close();
					}
				}catch (FileNotFoundException e) {
					
					e.printStackTrace();
					System.err.println(e.getMessage());
					logger.info("The exception is: "+e.getMessage());
	
					}
					System.out.println(lineList+"Chk this");
					System.out.println(" The test case lookup is completed..");
					return lineList;
					}
	}
