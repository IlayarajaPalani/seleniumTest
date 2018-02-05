
/*Description:This module captures the screenshot whenever its called.
 * 
 */

package com.amex.tp.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


public class ScreenShot {
private org.slf4j.Logger logger=LoggerFactory.getLogger(ScreenShot.class);
  
  public void takeScreenShot(String screenshotpath, String screenshotname, WebDriver wd) {
	  
	//	logger.info("***Placed screen shot in "+screenshotpath+"\\"+screenshotname+".png");
        // screenshotpath=FrameworkConstants.ScreenShots;

	  	 File scrFile = ((TakesScreenshot)wd).getScreenshotAs(OutputType.FILE);
	       //The below method will save the screen shot in d drive with test method name 
	  	 File screenshotfile = new File(screenshotpath+"\\"+screenshotname+".png");
	  	 
	  	 
	          try {
					FileUtils.copyFile(scrFile,screenshotfile);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	  }	
}
