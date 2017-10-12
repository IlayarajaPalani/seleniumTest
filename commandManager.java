package com.amex.tp.common;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;


public class commandManager {
	
	private static commandManager CommandManager;
	DriverManager dManager;
	WebDriver fdriver;
	String parentwindow ="";
	int hits = 0;
	int threshold = 5;
	JavascriptExecutor js;
	String dateTime ="12/07/2014 2:00 PM";
	
	
	public commandManager(WebDriver wbd){
		fdriver = wbd;
		fdriver.manage().window().maximize();
		parentwindow= fdriver.getWindowHandle();
		js = (JavascriptExecutor)fdriver;
	}
	public static commandManager getInstance(String browser) throws Exception{
			System.out.println("Browser called from cmd mgr get instance");
			if(CommandManager!= null)
			{
				return CommandManager;
			}else
			{
				System.out.println("else part of cmd mgr get instance");
				
				CommandManager = new commandManager(DriverManager.getWebDriver(browser));
				return CommandManager;
			}
		
	}
	

	
			public void open(String url) throws Exception {
				fdriver.get(url);
			}
			
			public void type(String target, String type, String value) throws Exception	{
				isPageLoaded();
				WebElement wel = this.getWebElement(target,type);
				wel.clear();
				wel.sendKeys(value);

			
			}
				
			public void isPageLoaded() throws Exception{
				hits=0;
				try{
					while (! js.executeScript("return document.readyState").toString().equals("complete")){
						Thread.currentThread().sleep(100);
						//System.out.println("Waiting to page load");
					}
				}
				catch(WebDriverException e){
					if(hits < threshold){
						Thread.currentThread().sleep(500);
					}
					else{
						throw new Exception(e.fillInStackTrace());
					}
				}
				
			}
			public void datepicker(String target, String type, String selDate) throws Exception {
				isPageLoaded();
				
				WebElement wel = this.getWebElement(target,type);
				try {
					wel.click();
					Thread.sleep(3000);
					//WebElement dataWidget = fdriver.findElement(By.xpath(".//*[@id='innerDiv']/table/tbody/tr/td"));
					//WebElement dataWidget = fdriver.findElement(By.xpath(".//*[@id='innerDiv']/table/tbody/tr/td/table/tbody"));
					
					//WebElement dataWidget = fdriver.findElement(By.xpath(".//*[@id='innerDiv']/table[1]//td"));
					//List <WebElement> rows=dataWidget.findElements(By.tagName("tr"));
					//List <WebElement> columns=dataWidget.findElements(By.tagName("td"));
					
					
					//js.executeScript("$(\"input[Rel='scheduleStartDate']\").removeAttr('readonly').removeAttr('hidden').val('"23 May 2015"')");
/*					js.executeScript ("document.getelementsbyname('scheduleStartDate')[0].removeAttribute('readonly')"); // Enables the from date box
					fdriver.findElement(By.name("scheduleStartDate")).click();
					WebElement DateBox= fdriver.findElement(By.name("scheduleStartDate"));
					DateBox.clear();
					DateBox.sendKeys("08/17/2017");*/
					//fdriver.findElement(By.tagName("href").name("javascript:void(0)")).click();
					//fdriver.findElements(By.tagName("href").equals("javascript:void(0)"))
					//fdriver.findElement(By.xpath("html/body/table/tbody/tr[2]/td/table/tbody/tr[3]/td/div/table/tbody/tr/td/table/tbody/tr[4]/td[7]/div/a")).click();

					//System.out.println(fdriver.findElement(By.className("CellAnchor")).getAttribute("value"));
		/*			String parentWindow = fdriver.getWindowHandle();
					String subWindow = null;
					Set<String> handles = fdriver.getWindowHandles(); // get all window handles
					Iterator<String> iterator = handles.iterator();
					while (iterator.hasNext()){
					    subWindow = iterator.next();
					}
					fdriver.switchTo().window(subWindow);
					int sRow = 2;
					int sCol = 3;
					String data = fdriver.findElement(By.xpath(".//*[@id='innerDiv']/table/tbody/tr/td/table/tbody/tr["+sRow+"]/td["+sCol+"]")).getText();
					System.out.println(data);
					fdriver.switchTo().window(parentWindow);*/
					
				fdriver.switchTo().frame("gToday:contrast:agenda.js");
					//fdriver.findElement(By.xpath("html/body/table/tbody/tr[2]/td/table/tbody/tr[3]/td/div/table/tbody/tr/td/table/tbody/tr[4]/td[5]/div/a")).click();
				List<WebElement> columns=fdriver.findElements(By.xpath(".//*[@id='innerDiv']/table/tbody/tr/td/table/tbody//td"));
				
				//System.out.println(columns.size());
				for (WebElement cell: columns){
					  //Select xx. Date
					//System.out.println(cell.getText());
					  if(cell.getText().equalsIgnoreCase(selDate)){
					    cell.findElement(By.linkText(selDate)).click();
					    break;
					  }
					}
	
				fdriver.switchTo().defaultContent();
				}
				catch(Exception e) {
					System.out.println("Date Picker Failed");
				}
				
			}
			
			public void click(String target, String type) throws Exception {
				isPageLoaded();
				WebElement wel = this.getWebElement(target,type);
				try{
					wel.click();
					}
				catch(Exception e){
					wel.sendKeys(Keys.RETURN);
				}
				
			}
			
			public void sendKeyToElement(String target, int index) throws Exception {
				isPageLoaded();
				List<WebElement> wbEs = getWebElements(target);
				//wbEs.get(index).click();
				wbEs.get(index).sendKeys(Keys.RETURN);
			}
			public void selectByText(String target, String type, String value) throws Exception {
				isPageLoaded();
				WebElement wel = this.getWebElement(target,type);
				int hits = 0;
				
				while(hits < threshold){
					hits++;
					try{
						Select sel = new Select(wel);
						sel.selectByVisibleText(value);
						break;
					}
					catch(NoSuchElementException e){
						sleep(200);
						continue;
					}
					catch(WebDriverException e){
						sleep(200);
						continue;
					}
				}
			}
			
			public void selectByIndex(String target, String type, int value) throws Exception {
				isPageLoaded();
				WebElement wel = this.getWebElement(target,type);
				int hits = 0;
				
				while(hits < threshold){
					hits++;
					try{
						Select sel = new Select(wel);
						sel.selectByIndex(value);
						break;
					}
					catch(NoSuchElementException e){
						sleep(200);
						continue;
					}
					catch(WebDriverException e){
						sleep(200);
						continue;
					}
				}
			}
			
			public void selectByValue(String target, String type, String value) throws Exception {
				isPageLoaded();
				WebElement wel = this.getWebElement(target,type);
				int hits = 0;
				
				while(hits < threshold){
					hits++;
					try{
						Select sel = new Select(wel);
						sel.selectByValue(value);
						break;
					}
					catch(NoSuchElementException e){
						sleep(200);
						continue;
					}
					catch(WebDriverException e){
						sleep(200);
						continue;
					}
				}
			}
			
			public void sleep(int value) throws Exception {
				Thread.currentThread().sleep(value);
			}
			
			public String getContent(String target, String type) throws Exception {
				isPageLoaded();
				WebElement wel = this.getWebElement(target,type);
				return wel.getText();
			}
			
			public String getAttribute(String target, String type, String attribute) throws Exception {
				isPageLoaded();
				WebElement wel = this.getWebElement(target,type);
				return wel.getAttribute(attribute);
			}
			
			public boolean ifExists(String target, String type, int localThreshold, long sleepDuration) throws Exception {
				isPageLoaded();
				try{
				this.getWebElement(target,type,localThreshold,sleepDuration);
				return true;
				}
				catch(Exception e){
					return false;
				}
				
			}
			public boolean ifDisplayed(String target) throws Exception {
				isPageLoaded();
				try{
					List<WebElement> wbEs = getWebElements(target);
					for(WebElement wEl : wbEs){
						if(wEl.isDisplayed())
						return true;
					}
					return false;
				}
				catch(Exception e){
					return false;
				}
			}
			public boolean ifEnabled(String target) throws Exception {
				isPageLoaded();
				try{
					List<WebElement> wbEs = getWebElements(target);
					for(WebElement wEl : wbEs){
						if(wEl.isEnabled())
						return true;
					}
					return false;
				}
				catch(Exception e){
					return false;
				}
			}
			
			public List<String> getContents(String target) throws Exception {
				hits=0;
				List elements = null;
				List<String> elementText = new ArrayList<String>();
				isPageLoaded();
				while(hits < threshold){
						hits++;
						try{
							elements = fdriver.findElements(By.xpath(target));
							break;
						}
						catch(Exception e){
							if(hits < threshold){
								Thread.currentThread().sleep(2000);
							}
							else{
								throw new Exception(e.fillInStackTrace());
							}
						}
					}
					for( Object element : elements){
						elementText.add( ((WebElement)element).getText() );
					}

				return elementText;
			}
			public WebElement getWebElement(String target, String type) throws Exception {
				hits=0;
				isPageLoaded();
				while(hits < threshold){
					hits++;
					try{

						switch(type){

						case "id":
							return fdriver.findElement(By.id(target));
						case "name":
							return fdriver.findElement(By.name(target));
						case "linkText":
							return fdriver.findElement(By.linkText(target));
						case "xpath":
							return fdriver.findElement(By.xpath(target));
						case "className":
							return fdriver.findElement(By.className(target));
						case "cssSelector":
							return fdriver.findElement(By.cssSelector(target));
						case "partialLinkText":
							return fdriver.findElement(By.partialLinkText(target));
						case "tagName":
							return fdriver.findElement(By.tagName(target));

							
						default:
							return null;
						}
					}
					catch(Exception e){
						if(hits < threshold){
							Thread.currentThread().sleep(2000);
						}
						else{
							throw new Exception(e.fillInStackTrace());
						}
					}

				}
				return null;
			}
			
			public WebElement getWebElement(String target, String type, int localThreshold, long sleepDuration) throws Exception {
				hits=0;
				isPageLoaded();
				while(hits < localThreshold){
					hits++;
					try{

						switch(type){

						case "id":
							return fdriver.findElement(By.id(target));
						case "name":
							return fdriver.findElement(By.name(target));
						case "linkText":
							return fdriver.findElement(By.linkText(target));
						case "xpath":
							return fdriver.findElement(By.xpath(target));
						case "className":
							return fdriver.findElement(By.className(target));
						case "cssSelector":
							return fdriver.findElement(By.cssSelector(target));
						case "partialLinkText":
							return fdriver.findElement(By.partialLinkText(target));
						case "tagName":
							return fdriver.findElement(By.tagName(target));
						default:
							return null;
						}
					}
					catch(Exception e){
						if(hits < localThreshold){
							Thread.currentThread().sleep(sleepDuration);
						}
						else{
							throw new Exception(e.fillInStackTrace());
						}
					}

				}
				return null;
			}
			
			public List<WebElement> getWebElements(String target) throws Exception {
				hits=0;
				//target="//img[@id='okBtn' and @src='images/btn_ok.gif' and @onclick='javascript:ajaxFunctionForConfirmRetrievalDP1()']";
				List<WebElement> elements = null;
				isPageLoaded();
				while(hits < threshold){
						hits++;
						try{
							elements = fdriver.findElements(By.xpath(target));
							break;
						}
						catch(Exception e){
							if(hits < threshold){
								//wd.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
								Thread.currentThread().sleep(2000);
							}
							else{
								throw new Exception(e.fillInStackTrace());
							}
						}
					}

				return elements;
			}


}
