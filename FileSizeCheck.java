/*Description: This program is used to verify file size.
 * 
 */

package amex.fs.commons;

import java.io.File;

import org.slf4j.Logger;

public class FileSizeCheck {
	boolean SizeVerification_status = false;
	static Logger logger;
	
	public FileSizeCheck(Logger logger)
	{
		this.logger=logger;
	}

	public static void main(String[] args) {
		FileSizeCheck fs = new FileSizeCheck(logger);
		fs.fileSizeVerification("C:\\Users\\US5002313\\Desktop\\test.html", "C:\\Users\\US5002313\\Desktop\\test.html");
		// TODO Auto-generated method stub

	}
	public boolean fileSizeVerification(String srcFile, String destFile){
		File src = new File(srcFile);
		File dest = new File(destFile);
		if(src.exists()&&dest.exists()){
			if(src.length() == dest.length()){
				SizeVerification_status = true;
			}
			else{
				SizeVerification_status = false;
			}
			logger.info("File size comparision status"+"       "+SizeVerification_status);
			System.out.println("SizeVerification_status---->"+SizeVerification_status);
		}
		else{
			logger.info("source file Presence"+" "+src.exists()+";"+"Destination File Presence"+" "+dest.exists());
			System.out.println("source file Presence"+" "+src.exists()+";"+"Destination File Presence"+" "+dest.exists());
		}
		return SizeVerification_status;
		
	}

}
