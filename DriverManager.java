package com.amex.tp.common;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Listeners;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
import com.saucelabs.testng.SauceOnDemandTestListener;
import com.saucelabs.saucerest.SauceREST;

@Listeners({SauceOnDemandTestListener.class})
public class DriverManager  implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {
	private static WebDriver webDriver = null;
	private static DriverManager driverManager = null;
	
	public String username = System.getenv("SAUCE_USER_NAME") != null ? System.getenv("SAUCE_USER_NAME") : System.getenv("SAUCE_USERNAME");
    public String accesskey = System.getenv("SAUCE_API_KEY") != null ? System.getenv("SAUCE_API_KEY") : System.getenv("SAUCE_ACCESS_KEY");
	
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);
   // private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    private ThreadLocal<String> sessionId = new ThreadLocal<String>();
    
    private WebDriver createDriver(String browser, String version, Platform platform, String methodName, FirefoxProfile firefoxProfile) throws Exception {
    	
    	System.out.println("Running create Driver instance of drivermanager");
		
    	DesiredCapabilities capabilities = new DesiredCapabilities(browser, version, platform);
        capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);

        String jobName = methodName + '_' + platform.getMajorVersion() + '_' + browser + '_' + version;

        capabilities.setCapability("name", jobName);
        webDriver = new RemoteWebDriver(new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),capabilities);
        String id = ((RemoteWebDriver) getWebDriver(browser)).getSessionId().toString();
        
        sessionId.set(id);  	 
        String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", id, jobName);
        System.out.println(message);
        
         return webDriver;
        } 
    
	public DriverManager(String browser) throws Exception{
		super();
		Map<String, Object> cmdconnectionobj = new HashMap<String, Object>();
		commandManager cmd = null;

		FirefoxProfile firefoxProfile = new FirefoxProfile(); 
		firefoxProfile.setPreference("network.proxy.type",4);
		firefoxProfile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		File filedownloadlocation=new File("downloads/");
		firefoxProfile.setPreference("browser.download.dir",filedownloadlocation.getAbsolutePath());
		System.out.println("browser.download.dir:"+filedownloadlocation.getAbsolutePath());
		firefoxProfile.setPreference("browser.download.folderList", 2);
		firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/csv, text/csv,application/octet-stream,application/octet-stream doc xls pdf txt,text/plain");
		firefoxProfile.setPreference("browser.download.manager.focusWhenStarting", false);
		firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
		firefoxProfile.setPreference("browser.download.useDownloadDir", true);
		firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
		firefoxProfile.setPreference("browser.download.manager.closeWhenDone", true);
		firefoxProfile.setPreference("browser.download.manager.showAlertOnComplete", false);
		firefoxProfile.setPreference("browser.download.manager.useWindow", false);
		firefoxProfile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
		firefoxProfile.setPreference("pdfjs.disabled", true); 
		try{
			if(username == null || accesskey == null){
	
					        	switch(browser)
					        	{
									case "IE":
										
											System.out.println("launching IE browser");
											System.out.println("Creating Local driver in DriverManager");
											ClassLoader classLoader = getClass().getClassLoader();
											File file = new File(classLoader.getResource("IEDriverServer.exe").getFile());
											System.out.println(file.getPath());
											DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();  
											ieCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING,
													true);
											ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
											System.setProperty("webdriver.ie.driver", file.getCanonicalPath());
											webDriver = new InternetExplorerDriver(ieCapabilities);
											webDriver.manage().window().maximize();
											webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
											//cmdconnectionobj.put("fdriver",cmd.fdriver);
										break;
									case "Firefox":
										
										System.out.println("launching Firefox browser");
										System.out.println("Creating Local driver in DriverManager");
										DesiredCapabilities capabilities = new DesiredCapabilities("firefox", "47.0.1", Platform.WINDOWS);
								        capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
								        capabilities.setCapability("firefox_binary", "C:\\Users\\ipala2\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
								        webDriver= new FirefoxDriver(capabilities);
										webDriver.manage().window().maximize();
										webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
									break;
					        	}
						 
						}
					else{
						System.out.println("Before Creating SauceLab Driver in DriverManager");
						
								System.out.println("Launcing Firefox browser in SauceLabs");
								webDriver = createDriver("firefox", "41.0", Platform.WINDOWS, "TP_Regression", firefoxProfile);
								System.out.println("After Creating SauceLab Driver in DriverManager");
					}
					
					//TODO
					/** SauceLab initialization 
					objects [] = {browser params like Browsertype, version, OS}
					SampleLabDriverSupplier.createDriver();
					webDriver =  SampleLabDriverSupplier.getWebDriver();
					*/
					}
	catch(NullPointerException e1)
	{   e1.printStackTrace();
	
	//connectionobj.put("sessionid", dManager.getSessionId());
	
	cmdconnectionobj.put("fdriver",cmd.fdriver);
	}
}
	
	/*public static WebDriver getDriver() throws Exception {
		if(webDriver_DriverManager==null) {
			driverManager = new DriverManager();
			return webDriver_DriverManager;
		}
		else{
			return webDriver_DriverManager;
		}
	}*/
	
	

    public static WebDriver getWebDriver(String browser) throws Exception {
    		//webDriver.quit();
    		System.out.println("Driver Manager get WebDriver");
		
    		if(webDriver==null) {
    			driverManager = new DriverManager(browser);
    			return webDriver;
    		}
    		else{
    			return webDriver;
    		}
    		
		
	}
    
    /*public static DriverManager getDriverManager() throws Exception {
    	if(driverManager==null) {
			driverManager = new DriverManager();
			getWebDriver();
			return driverManager;
		}
		else{
			return driverManager;
		}
	}*/


	public String getSessionId() {
        return sessionId.get();
    }
	
	
    
    @Override
    public SauceOnDemandAuthentication getAuthentication() {
        return authentication;
    }
    
}
