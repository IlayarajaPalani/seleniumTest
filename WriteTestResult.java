package com.amex.tp.common;

import java.io.*;

import org.testng.annotations.Test;

public class WriteTestResult {

	static FileOutputStream fos;
	File resultfile;
	@Test
	public static void main(String args[]) throws FileNotFoundException
	{
		LoadProperties lp1=new LoadProperties(FrameworkConstants.RunIdFile);
		String runIdFile=(lp1.readProperty("RUNID"));
		WriteTestResult wrt=new WriteTestResult();
		wrt.writeToFile(runIdFile,"Check");
		
	}
	
	public boolean writeToFile(String testresultfilename,String texttowrite)
	{
		try(FileWriter fw = new FileWriter(testresultfilename, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(texttowrite);
			} catch (IOException e) {
			    //exception handling left as an exercise for the reader
			}
	
		return true;
	}
	
	
}
