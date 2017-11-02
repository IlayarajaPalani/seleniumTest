package amex.fs.commons;


/*Description:
 * project: NGP Automation.
 * author: Viren Tiwari
 * This program is to test CD non secure plus node using CD protocol .
 * A file will be uploaded to SFT server and the file will be verified at the T&M   . 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import sun.net.www.content.audio.x_aiff;

import amex.fs.commons.Download;
import amex.fs.commons.FileComparision;
import amex.fs.commons.FileSizeCheck;
import amex.fs.commons.FrameworkConstants;
import amex.fs.commons.LoadProperties;
import amex.fs.commons.Login;
import amex.fs.commons.Logoff;
import amex.fs.commons.Sentinels;
import amex.fs.commons.TestcaseLookup;
import amex.fs.commons.Upload;
import amex.fs.commons.WriteTestResult;

public class Schedule
{
	Map<String, Object> TnMSchedule=new HashMap<String, Object>();
	int teststatus1=0;
	public static org.slf4j.Logger logger = LoggerFactory.getLogger(Schedule.class);
	String uploadedfilename = null;
	public Schedule(org.slf4j.Logger logger )
	{
		this.logger=logger;
	}

	Map connectionmap,connectionmap1,uplaodmap;
	String servername,TPurl,TPuser,TPpwd;
	String qcurl;
	String qcuname;
	String qcpwd;
	String domain;
	String project;
	String TLpath;
	String TSet;
	String runIdFile;
	List<String> lst;
	WriteTestResult wtr,testlog;
	String TID;
	String tid=null;
	Login lg=new Login(logger);
	String TrackMonitor=null;
	String warningMsge=null;
	String WarningMsge=null;
    
	Logoff loff = null;
  
	boolean TMVerification_status = false;	
	
 
	public Map<String, Object> TNMverify(String TMSearchParam,String Value,String url,String id,String pass) throws Throwable
	//public Map<String, Object> basefileverify(String BaseFileName,String url,String id,String pass) throws Throwable
    {  
		System.out.println("connecting to firefox");
		FirefoxProfile profile1 = new FirefoxProfile();  
		profile1.setPreference("network.proxy.type",4);
		WebDriver wd= new FirefoxDriver(profile1);
		wd.manage().window().maximize();

		wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		logger.info("login to TM");
		System.out.println("connected to TM");
		System.out.println("opening TM");
		wd.get(url);
		wd.findElement(By.id("textboxuid_AD")).click();
		wd.findElement(By.id("textboxuid_AD")).clear();
		wd.findElement(By.id("textboxuid_AD")).sendKeys(id);
		wd.findElement(By.id("textboxpwd_AD")).click();
		wd.findElement(By.id("textboxpwd_AD")).clear();
		wd.findElement(By.id("textboxpwd_AD")).sendKeys(pass);
		wd.findElement(By.id("Login")).click();
		wd.findElement(By.id("NavigationButton2")).click();
		logger.info("login successful");
		System.out.println("login successful");
		if (!wd.findElement(By.id("NavigationButton2")).isSelected())
		{
			wd.findElement(By.id("NavigationButton2")).click();
		}
		Thread.sleep(FrameworkConstants.SleepValue); 
		//test radio button
		if (!wd.findElement(By.xpath("/html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr[2]/td/font/input[2]")).isSelected())
		{
			wd.findElement(By.xpath("/html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr[2]/td/font/input[2]")).click();
		}
		
		switch(TMSearchParam)
		{
		case "Base File Name":
			System.out.println("Parameter Value :"+Value);
			//Base file name option selection
			if (!wd.findElement(By.xpath("//table[@id='Table6']/tbody/tr[3]/td/table/tbody/tr[2]/td[1]/p/font/select//option[6]")).isSelected())
			{
				wd.findElement(By.xpath("//table[@id='Table6']/tbody/tr[3]/td/table/tbody/tr[2]/td[1]/p/font/select//option[6]")).click();
			}
			wd.findElement(By.name("filterByValue")).click();
			wd.findElement(By.name("filterByValue")).clear();
			wd.findElement(By.name("filterByValue")).sendKeys(Value);
			wd.findElement(By.name("search")).click();
			System.out.println(Value+" is submitted as input");
			break;
		case "User Name":
			System.out.println("Parameter Value :"+Value);
			//Base file name option selection
			if (!wd.findElement(By.xpath("/html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[3]/td/table/tbody/tr[2]/td[1]/p/font/select/option[2]")).isSelected())
			{
				wd.findElement(By.xpath("/html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[3]/td/table/tbody/tr[2]/td[1]/p/font/select/option[2]")).click();
			}
			wd.findElement(By.name("filterByValue")).click();
			wd.findElement(By.name("filterByValue")).clear();
			wd.findElement(By.name("filterByValue")).sendKeys(Value);
			wd.findElement(By.name("search")).click();
			System.out.println(Value+" is submitted as input");
			break;
		case "File Tracking ID":
			System.out.println("Parameter Value :"+Value);
			//Base file name option selection
			if (!wd.findElement(By.xpath("/html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[3]/td/table/tbody/tr[2]/td[1]/p/font/select/option[5]")).isSelected())
			{
				wd.findElement(By.xpath("/html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[3]/td/table/tbody/tr[2]/td[1]/p/font/select/option[5]")).click();
			}
			wd.findElement(By.name("filterByValue")).click();
			wd.findElement(By.name("filterByValue")).clear();
			wd.findElement(By.name("filterByValue")).sendKeys(Value);
			wd.findElement(By.name("search")).click();
			System.out.println(Value+" is submitted as input");
			break;
		}
		
		// String WarningSign = wd.findElement(By.xpath("html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[1]/td[1]/ul/table/tbody/tr/td[1]/img")).getText();
		/* WarningMsge =wd.findElement(By.xpath("html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[1]/td[1]/ul/table/tbody/tr/td[2]/div/font")).getText();
		System.out.println("WarningSign:: "+WarningMsge);*/
		 
		/* TrackMonitor = wd.findElement(By.xpath("html/body/table[3]/tbody/tr/td[3]/form/table/tbody/tr[1]/td[1]/table/tbody/tr/td/div/font/b")).getText();
		System.out.println("TrackMonitor:: "+TrackMonitor);*/
		
		try
		{
	        // if (TrackMonitor.equals("Tracking / Monitoring - File Tracking Results"))
			//Assert.assertEquals(wd.findElement(By.xpath("html/body/table[3]/tbody/tr/td[3]/form/table/tbody/tr[1]/td[1]/table/tbody/tr/td/div/font/b")).getText(), "Tracking / Monitoring - File Tracking Results"); 
			if(wd.findElement(By.xpath("html/body/table[3]/tbody/tr/td[3]/form/table/tbody/tr[1]/td[1]/table/tbody/tr/td/div/font/b")).isDisplayed())
			{

				Thread.sleep(FrameworkConstants.SleepValue); 
				String time_SFT= wd.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[3]/form/table/tbody/tr[4]/td[1]/table/tbody/tr[2]/td[5]/p/font")).getText();
				System.out.println("SFT Time:"+time_SFT);
				String splittime1[]= time_SFT.split(" ");
				String SFT_TnMtime= splittime1[0];
				System.out.println("SFT_TnMtime value : "+SFT_TnMtime);
				TnMSchedule.put("SFT_TnMtime", SFT_TnMtime);
				 
				Thread.sleep(FrameworkConstants.SleepValue); 
				
				wd.findElement(By.id("Picture14")).click();
//				Schedule Retrieval from Client - Path
				String Event = wd.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[3]/form/div[2]/layer/table/tbody/tr[3]/td[3]/a/p/font")).getText();
				System.out.println("Event1 :: "+Event);
				String Event2 = wd.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[3]/form/div[2]/layer/table/tbody/tr[5]/td[3]/a/p/font")).getText();
				System.out.println("Event2 :: "+Event2);
				//boolean temp= wd.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[3]/form/div[2]/layer/table/tbody/tr[5]/td[3]/a/p/font")).getText().contains("File Push to Client");
				//System.out.println("temp1:: "+temp);
				//Generating system time
				DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				Date d = new Date();
				df = new SimpleDateFormat("yyyy-MM-dd");
				df.setTimeZone(TimeZone.getTimeZone("IST"));
				String todayDate = df.format(d);
				System.out.println("todayDate :: "+todayDate);
				TnMSchedule.put("SysDate", todayDate);


				if (SFT_TnMtime.equals(todayDate))
				{
					System.out.println("date matched");
					System.out.println("Scheduled to pull the file on ::"+todayDate);

					//if (temp==true)
					System.out.println("Fetching tid..");
					String ScheduleTID=wd.findElement(By.xpath(".//*[@id='FileResult']/tbody/tr[2]/td[1]/p/a/font")).getText();
					System.out.println("ScheduleTID :: "+ScheduleTID);
					TnMSchedule.put("TID", ScheduleTID);
					if (Event.equals("Schedule Retrieval from Client"))
					{
						System.out.println("Event :: "+Event);
						String temp1= wd.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[3]/form/div[2]/layer/table/tbody/tr[3]/td[5]/p/font")).getText().trim();
						System.out.println("status: "+temp1);
						logger.info(" Message: \t"+temp1 );
						System.out.println(" Message: \t "+temp1);
						if (temp1.equals("Failed") )
						{   
							  TnMSchedule.put("TMVerification_status",false);
							
							  System.out.println("Schedule Retrieval Pull Failed");
							  System.out.println("Logout from TP...");

						}
						else if(temp1.equals("Completed")){
							
							  
							  System.out.println("Schedule Retrieval status is Completed in TnM");
							   if (Event2.equals("File Push to Client"))
								{
									System.out.println("Event2 :: "+Event2);
									String temp2= wd.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[3]/form/div[2]/layer/table/tbody/tr[5]/td[5]/p/font")).getText().trim();
									System.out.println("status: "+temp2);
									logger.info(" Message: \t"+temp2 );
									System.out.println(" Message: \t "+temp2);
									if (temp2.equals("Completed"))
									{   
										String Event3 = wd.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[3]/form/div[2]/layer/table/tbody/tr[6]/td[3]/a/p/font")).getText();
										if (Event3.equals("File Mailbox"))
										{
											System.out.println("Event3 :: "+Event3);
											String temp3= wd.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[3]/form/div[2]/layer/table/tbody/tr[6]/td[5]/p/font")).getText().trim();
											System.out.println("status: "+temp3);
											logger.info(" Message: \t"+temp3 );
											System.out.println(" Message: \t "+temp3);
										TnMSchedule.put("TMVerification_status",true);
										
										System.out.println("TnM validation completed successfully");
										System.out.println("Logout from TP...");
										}
										else
										{
											TnMSchedule.put("TMVerification_status",false);
											
											System.out.println("TnM validation completed successfully");
											System.out.println("Logout from TP...");
										}
									}
									else if (temp2.equals("Failed"))
									{
										TnMSchedule.put("TMVerification_status",false);
											
										System.out.println("TnM validation completed successfully");
										System.out.println("Logout from TP...");
									}
									else
									{
										TnMSchedule.put("TMVerification_status",false);
											
										System.out.println("Schedule Retrieval event updation pending...");
										System.out.println("Logout from TP...");
									}
								}

							   else if(Event2.equals("File Mailbox"))
								{
										System.out.println("Event2 :: "+Event2);
										String temp3= wd.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[3]/form/div[2]/layer/table/tbody/tr[5]/td[5]/p/font")).getText().trim();
										System.out.println("status: "+temp3);
										logger.info(" Message: \t"+temp3 );
										System.out.println(" Message: \t "+temp3);
										if (temp3.equals("Completed"))
										{   
											TnMSchedule.put("TMVerification_status", true);
											System.out.println("TnM validation completed successfully");
											System.out.println("Logout from TP...");
										}
										else if (temp3.equals("Failed"))
										{
											TnMSchedule.put("TMVerification_status",false);
											
											System.out.println("TnM validation completed successfully");
											System.out.println("Logout from TP...");
										}
										else
										{
											TnMSchedule.put("TMVerification_status",false);
											
											System.out.println("Schedule Retrieval event updation pending...");
											System.out.println("Logout from TP...");
										}
								}
							   TnMSchedule.put("TMVerification_status",true);
							 
						}
						else
						{
							  TnMSchedule.put("TMVerification_status",false);
							  
							  System.out.println("Schedule Retrieval status is not updated in TnM");
							  System.out.println("Logout from TP...");
						}
  
					}
					
				}
				else
				{
					System.out.println("Scheduled to pull the file on "+SFT_TnMtime);
					System.out.println("File is not pulled on  "+todayDate);
					TnMSchedule.put("TMVerification_status",false);
				}

			}
		}
			
			catch(NoSuchElementException e)
			{    System.out.println("Invalid input..");
				//System.err.println("Msge :: "+e);
			}
			try 
			{
				//else if (wd.findElement(By.xpath("html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[1]/td[1]/ul/table/tbody/tr/td[1]/img")).isDisplayed())
				// else if(WarningMsge.equals("No matching results found. You may refine search criteria and Retry")) 
				//Assert.assertEquals(wd.findElement(By.xpath("html/body/table[3]/tbody/tr[1]/td[3]/form/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[1]/td[1]/ul/table/tbody/tr/td[2]/div/font")).getText(), "No matching results found. You may refine search criteria and Retry");
				 if (wd.findElement(By.xpath(".//*[@id='Table6']/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[1]/td[1]/ul/table/tbody/tr/td[2]/div/font")).isDisplayed())
				{
					System.out.println(" Checking for the warning sign...");
					WebElement eMsge = wd.findElement(By.xpath(".//*[@id='Table6']/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[1]/td[1]/ul/table/tbody/tr/td[2]/div/font"));
					System.out.println("Message ::"+eMsge.getText());
					TnMSchedule.put("TMVerification_status",false);
				}
			}
			catch(NoSuchElementException e)
			{   System.out.println("Valid basefile name");
				//System.err.println("Msge :: "+e);
			}
			logger.info("logoff from TM");
			wd.findElement(By.name("logoutTM")).click();
			Thread.sleep(FrameworkConstants.SleepValue); 
			wd.quit();

		   System.out.println(TnMSchedule.keySet());
		   System.out.println(TnMSchedule.values());
		
	
		return TnMSchedule;
		
		
    }
}
