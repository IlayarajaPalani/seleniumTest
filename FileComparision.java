/*Description: This program is used to compare two files.
*/
package amex.fs.commons;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class FileComparision {
	boolean compstatus = false;
	static Logger logger;
	
	
	public FileComparision(Logger logger)
	{
		this.logger=logger;
	}
	

	public static void main(String[] args) throws IOException {
		FileComparision fc = new FileComparision(logger);
		fc.contentVerification("C:\\Users\\US5002313\\Desktop\\test.html", "C:\\Users\\US5002313\\Desktop\\test.html");
		// TODO Auto-generated method stub

	}
	public boolean contentVerification(String srcFilename, String destFilename) throws IOException{
		
		File srcfile = new File(srcFilename);
		File destfile = new File(destFilename);
		if(srcfile.exists()&&destfile.exists()){
		logger.info("source file taken for comparision "+srcFilename);
		logger.info("Destination file taken for comparision "+destFilename);
		compstatus = FileUtils.contentEquals(srcfile, destfile);
		logger.info("Comparision status "+"              "+compstatus);
		System.out.println("result "+compstatus);	
		}
		else
		{
			logger.info("source file Presence "+" "+srcfile.exists()+";"+" Destination File Presence "+" "+destfile.exists());
			System.out.println("source file Presence "+" "+srcfile.exists()+";"+" Destination File Presence "+" "+destfile.exists());	
		}
		return compstatus;
	}

}
