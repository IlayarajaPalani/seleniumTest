/* Description :
 * This module generates the sentinal output as per the input sentinal name
 * The sentinal input are:
 * TID, DATE, UTC_DATE, DATE_CCYYDDMM, SHORT_DATE, DOW_TEXT,DOW, DATE_TIME, UTC_TIME. 
 * 
 */

package amex.fs.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.LoggerFactory;



@SuppressWarnings("unused")
public class Sentinels {
	String sentinelValue= null;
	DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	boolean status = false;
	public static org.slf4j.Logger logger = LoggerFactory.getLogger(Sentinels.class);
	static Map uploadobj;
	String uploadTime;
	
	public static void main(String[] args) throws IOException, ParseException{
		
	/*	Login lg = new Login(logger);
		
		Map loginobj = lg.logintoSFT("fsgatewaytest.intra.aexp.com", 21, "REMOTEDELUSER", "amex123", "FTP");
		System.out.println("Protocol------------>"+loginobj.get("protocol"));
		//System.out.println(loginobj);
		Upload up = new Upload(logger);
		
		 uploadobj = up.uploadFile(loginobj, "DATE_TIME",  "TESTFILE.txt",  "/inbox",  "BINARY", "PASSIVE");
		System.out.println(uploadobj);*/
		Sentinels sent = new Sentinels();
		String sentinel= sent.sentinelCheck("DATE");
		System.out.println("MON"+sentinel);
		
	
	}
	
	
	// This method is used to validate Tracking id sentinal
	public boolean TIDValidation(String TID){
		if(TID.length()==14){
			if (FrameworkConstants.Platformchk== "POD")
			{
				status = TID.equals(TID.toUpperCase());
			}
			else if(TID.startsWith("A03")|| TID.startsWith("A04") || TID.startsWith("A05") || TID.startsWith("A06")){
			status = TID.equals(TID.toUpperCase());
			}
			else{
				status = false;
			}
	}
		else{
	  status = false;
		}
		return status;

	}
	
	

// This method generates the the sentinal value according to the input sentinal name
public String sentinelCheck(String sentinel) throws ParseException 
{
	logger.info("The sentinal module begins !!");
	Date d = new Date();
	DayOfWeek dayOfWeek = null;
	
	LocalDate localDate = LocalDate.now(ZoneId.of("UTC-07:00"));
	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
	
	
	int day;
	Date dat= null;
	
	switch(sentinel){
	case "DATE" :
		
		df = new SimpleDateFormat("yyyyMMdd");
		df.setTimeZone(TimeZone.getTimeZone("MST"));
		sentinelValue = df.format(d);
		break;
		
	case "UTC_DATE" :
		df = new SimpleDateFormat("yyyyMMdd");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		sentinelValue = df.format(d);
		break;
		
	case "DATE_CCYYDDMM" :
		df = new SimpleDateFormat("yyyyddMM");
		df.setTimeZone(TimeZone.getTimeZone("MST"));
		sentinelValue=df.format(d);
		break;
		
	case "SHORT_DATE" :
		df = new SimpleDateFormat("yyMMdd");
		df.setTimeZone(TimeZone.getTimeZone("MST"));
		sentinelValue=df.format(d);
		break;
		
	case "DOW" :
		dayOfWeek = localDate.getDayOfWeek(); 
		day = dayOfWeek.getValue();
		sentinelValue = String.valueOf(day);
		break;
		
	case "PREV_MON_CCYYMMDD":
		dayOfWeek = localDate.getDayOfWeek(); 
		day = dayOfWeek.getValue();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyyMMdd" );
		
	switch(day){
		
	case 1:
		sentinelValue = formatter.format(localDate.minusDays(7));
		break;
	case 2:
		sentinelValue = formatter.format(localDate.minusDays(1));
		break;
	case 3:
		sentinelValue = formatter.format(localDate.minusDays(2));
		break;
	case 4:
		sentinelValue = formatter.format(localDate.minusDays(3));
		break;
	case 5:
		sentinelValue = formatter.format(localDate.minusDays(4));
		break;
	case 6: 
		sentinelValue = formatter.format(localDate.minusDays(5));
		break;
	case 7:
		sentinelValue =formatter.format(localDate.minusDays(6));
		break;
		}
		break;
		
	case "DOW_TEXT":
		df = new SimpleDateFormat("E");
		df.setTimeZone(TimeZone.getTimeZone("MST"));
		sentinelValue = df.format(d);
		break;
		
	case "DATE_TIME":
		df = new SimpleDateFormat("yyMMddHms");
		df.setTimeZone(TimeZone.getTimeZone("MST"));
		System.out.println(sentinelValue);
		
	case "UTC_TIME":
		df = new SimpleDateFormat("Hms");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
				
}
logger.info("The input sentinal is: "+sentinel);
logger.info("The generated sentinal value is: "+sentinelValue);
return sentinelValue;
}

}
  

  
  
  
  
  
  
  
  
  
  
