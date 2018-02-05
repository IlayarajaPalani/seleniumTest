package com.amex.tp.common;

//Description - Search a transmitter in Maintain Transmitter Screen by providing a Transmitter name

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import bsh.ParseException;

import com.amex.tp.common.CreateZip;
import com.amex.tp.common.FrameworkConstants;
import com.amex.tp.common.LoadProperties;
import com.amex.tp.common.ScreenShot;
import com.amex.tp.common.TP_Login;
import com.amex.tp.common.TestcaseLookup;
import com.amex.tp.common.WriteTestResult;
import com.amex.tp.common.commandManager;

@Test
public class TestComplete{

	public static org.slf4j.Logger logger = LoggerFactory.getLogger(TestComplete.class);

	Map<String, Object> connectionmap;
	ScreenShot screenPrint;
	String runIdFile;
	List<String> testCaseList;
	WriteTestResult wtr;
	WebDriver fdriver;
	String filePath = FrameworkConstants.ScreenShots
			+ "/G14SearchTransmitterTC001/G14SearchTransmitterTC001.zip";
	String screenshotname;
	int screenshotnumber = 1;
	String loginscreenshot;
	File folder;
	CreateZip zipfile = null;
	ZipOutputStream zos = null;
	FileOutputStream fos = null;
	boolean testCasesucessFlag=false;
	commandManager cmd;

	@Parameters({ "TestCaseName", "Browser" })
	public void teardown(String tcname, String browser) throws InterruptedException,ParseException, Throwable {
		String tcid = "";
		LoadProperties lp = new LoadProperties(FrameworkConstants.G14_Props);
		cmd = commandManager.getInstance(browser, tcid);

		try {
			TP_Login tp = new TP_Login(logger,browser,cmd);
			connectionmap = tp.HlogintoTP(browser, zos);
			fdriver = (WebDriver) connectionmap.get("fdriver");
			fdriver.quit();
			} catch (Exception e) {
				e.printStackTrace();
				fdriver.quit();
				Assert.fail(e.getMessage());

		}

	}	

	
}
