package amex.fs.sft;

/*Description:
 * project: NGP Automation.
 * author: 
 * This program is to test  Push file to SFT using C:D protocol remote initiated_Non Secure  .
 * A file will be uploaded to SFT server and  transfered  to the client , after download   from client the file  need to verify the content and size . 
 */ 
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import amex.fs.commons.Download;
import amex.fs.commons.FileComparision;
import amex.fs.commons.FileSizeCheck;
import amex.fs.commons.FrameworkConstants;
import amex.fs.commons.LoadProperties;
import amex.fs.commons.Login;
import amex.fs.commons.Logoff;
import amex.fs.commons.TestcaseLookup;
import amex.fs.commons.Upload;
import amex.fs.commons.WriteTestResult;

public class G401CDDelTextCheck {
	
	int teststatus=0;
	public static org.slf4j.Logger logger = LoggerFactory.getLogger(G401CDDelTextCheck.class);
	 String uploadedfilename = null;
	  Map connectionmap, uplaodmap;
	  String servername;
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
	  String Host_cd,Password_cd,Username_cd;
	  String TID;
	 
	 String decryptedfile = null;
	 FTPClient ftpclient_cd = null; // for CD connection
	 boolean loginstatus_cd = false;  // for CD connection
	 Map<String, Object> connectionobj_cd = new HashMap<String, Object>(); //for CD connection
	 //parameters for CD
	 //String Host_cd="148.171.100.1";
	 //int Port_cd=21; 
	// String Username_cd="IL4106A";
	 //String  Password_cd="Jan@2017";
	 //String Protocol_cd;
	 //String Delivery_cd_file_name="'S22.CDTST.file0001'";
	  File file_cd;
	  String filename_cd;
	  long filesize_cd;
	 
	 
	public static void main(String[] args)
	{
		G401CDDelTextCheck fg = new G401CDDelTextCheck();
		
			// basefile2 is referred to Delivery File name for CD
			
			fg.f("G401CDDelTextCheck", "DELUSER01", "no", "21", "FTP", "CD_TEXT_CHECK", "TESTFILE.txt","/inbox", "UD", "BINARY", "PASSIVE", "'S22.CDTST.POATXTUN'");
	}
	 @Test
	 @Parameters({"TestCaseName","SFTUser","ALMUpdate","Port","Protocol","BaseFileName","PhysicalFile","RemoteDirectory","Action","FileType","FileTransferMode","Basefile2"})
	 public void f(String tcname, String sftuser, String almupdate, String port, String protocol, String basefilename, String physicalfile, String remotedirectory, String action, String filetype, String filetransfermode,String basefile2){
		try { logger.info("G401CDDelTextCheck Execution Started");
		  logger.info("Loading Properties");
		  LoadProperties lp=new LoadProperties(FrameworkConstants.SFT);
		  servername=lp.readProperty("server");
		  qcurl=lp.readProperty("almurl");
		  qcuname=lp.readProperty("almuser");
		  qcpwd=lp.readProperty("almpwd");
		  domain=lp.readProperty("almdomain");
		  project=lp.readProperty("almproject");
		  TLpath=lp.readProperty("almTLPath");
		  TSet=lp.readProperty("almTSet");
		  Host_cd=lp.readProperty("MFurl");
	      Username_cd=lp.readProperty("MFRacfID");
		  Password_cd=lp.readProperty("MFpwd");
		  int intport=Integer.parseInt(port);
		  
		  
		  Map dwnld = new HashMap();
		 // Map dwnld1 = new HashMap();
		  boolean constatus= false;
		  boolean sizestatus = false;
		  
		  /*
		   * Host_cd=148.171.100.1;
             Port_cd=21; 
             racfid=IL4106A;
             racfidpwd=Dec@2016;
		   */
		
			/*// String decryptedfile = null;
			 FTPClient ftpclient_cd = null; // for CD connection
			 boolean loginstatus_cd = false;  // for CD connection
			 Map<String, Object> connectionobj_cd = new HashMap<String, Object>(); //for CD connection
			 //parameters for CD
			 String Host_cd=lp.readProperty("host_cd");
			
			 
			//String port_cd=lp.readProperty("Port_cd");
			 int intport_cd=Integer.parseInt(lp.readProperty("Port_cd"));
			 String Username_cd=lp.readProperty("racfid");
			 String  Password_cd=lp.readProperty("racfidpwd");
			 String Protocol_cd;
			 //String Delivery_cd_file_name="'S22.CDTST.file0001'";
			 File file_cd;
				String filename_cd;
				long filesize_cd;
			 
			 Map connectionmap;
		  //
*/			  
		  Login lg=new Login(logger);
		  connectionmap= lg.logintoSFT(servername, intport, sftuser, FrameworkConstants.DefaultSFTPWD, protocol);
		  if((boolean) connectionmap.get("loginstatus")){
			  logger.info(sftuser+" logged into "+servername+" successfully ");
			  Upload up=new Upload(logger);
			  uplaodmap = up.uploadFile(connectionmap, basefilename, physicalfile, remotedirectory, filetype, filetransfermode);
			  if((boolean) uplaodmap.get("uploadstatus")){
				  uploadedfilename = (String) uplaodmap.get("Filename");
				  logger.info(sftuser+" uploaded "+uploadedfilename+" successfully ");
				  
				  //To check physical file name and size
				  file_cd = new File(physicalfile);
					
					filename_cd = file_cd.getName();
					System.out.println("Physical filename :" +filename_cd);
					filesize_cd = file_cd.length();
					System.out.println("Physical filesize :" +filesize_cd);
				  
				  System.out.println("file uploaded for CD delivery");
				  logger.info("file uploaded for CD Delivery");
				  
				 // System.out.println(" download of file started from Mainframe environment");
				  //logger.info("download of file started from Mainframe environment");
				  
				  Logoff loff=new Logoff(logger);
				  loff.logofffromSFT(connectionmap);
				  Download downloadmap = new Download(logger);
			 
			      Thread.sleep(300000);
				    // Mainframe login using FTP protocol
				  
				
				  
				 
			      		ftpclient_cd = new FTPClient();
				  ftpclient_cd.addProtocolCommandListener((ProtocolCommandListener) new PrintCommandListener(new PrintWriter(System.out)));
				  
					
						logger.info("connecting to the Host");
						ftpclient_cd.connect(Host_cd, intport);	
						logger.info("Logging to the FTP server to connect to Mainframe");
						System.out.println("Logging to the serverto connect to Mainframe");
						loginstatus_cd = ftpclient_cd.login(Username_cd, Password_cd);
						logger.info("loginstatus for CD-------"+loginstatus_cd);
						
						connectionobj_cd.put("loginstatus", loginstatus_cd);
						//System.out.println(ftpclient.getReplyString());
						connectionobj_cd.put("Message", ftpclient_cd.getReplyString());
						System.out.println("connectionobj for CD->"+connectionobj_cd);
						connectionobj_cd.put("loginstatus", loginstatus_cd);
						if(loginstatus_cd){
							connectionobj_cd.put("connection", ftpclient_cd);
							connectionobj_cd.put("protocol", "FTP");
							System.out.println(" Mainframe connection is successful");
							
						}
						
					
				  
				  
				  
				  switch(action)
				  {
				  case "UD" :
					    //ftpclient_cd.setFileType(ftpclient_cd.BINARY_FILE_TYPE);
						//logger.info("FileType       BINARY");
						
		  				dwnld= downloadmap.downloadFile(connectionobj_cd, basefile2, FrameworkConstants.DownloadDirectory+""+basefile2, FrameworkConstants.RemoteOutbox, filetype, filetransfermode);
		  				if((boolean) dwnld.get("downloadstatus"))
		  				{
		  					logger.info(FrameworkConstants.DownloadDirectory+basefile2+" Downloaded Successfully");
		  					System.out.println("the download is successful");
		  				}else
		  				{
		  					logger.info(FrameworkConstants.DownloadDirectory+basefile2+" Download Failed");
		  					System.out.println("the download failed");
		  				}
		  				break;
		  	
				  case "UDD"  :
					  Thread.sleep(FrameworkConstants.SleepValue);
 						dwnld= downloadmap.downloadFile(connectionmap, uploadedfilename, FrameworkConstants.DownloadDirectory+""+uploadedfilename, FrameworkConstants.RemoteOutbox, filetype, filetransfermode);
 						
 						if((boolean) dwnld.get("downloadstatus"))
						{
							logger.info(FrameworkConstants.DownloadDirectory+uploadedfilename+" Downloaded Successfully from Outbox");
							Thread.sleep(FrameworkConstants.SleepValue);
	 						dwnld= downloadmap.downloadFile(connectionmap, uploadedfilename, FrameworkConstants.DownloadDirectory+""+uploadedfilename, FrameworkConstants.RemoteSent, filetype, filetransfermode);
	 						if((boolean) dwnld.get("downloadstatus"))
							{
								logger.info(FrameworkConstants.DownloadDirectory+uploadedfilename+" Downloaded Successfully from Sent");
							}else
							{
								logger.info(FrameworkConstants.DownloadDirectory+uploadedfilename+" Download Failed from Sent");
							} 
						}else
						{
							logger.info(FrameworkConstants.DownloadDirectory+uploadedfilename+" Download Failed from Outbox");
						} 
 						break;
		  				
		  		default:
		  			 	teststatus=0;
		  				break;
			  }
				
				
				
					  
					  
					  
					  System.out.println("the file comparission is starting:");
					  logger.info("the file comparision is starting:");
					  
					    FileComparision fc = new FileComparision(logger);
						constatus = fc.contentVerification(physicalfile, FrameworkConstants.DownloadDirectory+"/"+basefile2);
						
						//System.out.println(ftpclient_cd.getReplyString());
						
						String delfilename =FrameworkConstants.DownloadDirectory+"/"+basefile2;
						
						
					
						file_cd = new File(delfilename);
						
						filename_cd = file_cd.getName();
						System.out.println("filename_cd:" +filename_cd);
						filesize_cd = file_cd.length();
						System.out.println("filesize_cd :" +filesize_cd);
						
						//System.out.println("File size  ");
						logger.info("File content verification status:"+constatus);
						System.out.println("File content verification status:"+constatus);
						
						System.out.println("fc "+fc);
						FileSizeCheck fs = new FileSizeCheck(logger);
						
						
						sizestatus = fs.fileSizeVerification(physicalfile, FrameworkConstants.DownloadDirectory+"/"+basefile2);
						System.out.println(ftpclient_cd.getReplyString());
						logger.info("File size verification status:"+sizestatus);
						System.out.println("File size verification status:"+sizestatus);
						System.out.println("fs "+fs);
						if(constatus&&sizestatus){
							//if(constatus){
							teststatus=1; //if file same 
							System.out.println("the file before upload and file after download are same");
							logger.info("the file before before upload and file after download are same");
							}else{
						    teststatus=0; // if different
						    System.out.println("the file before upload and file after download are different");
						    logger.info("the file before upload and file after download are different");
								 }
						System.out.println("the comparision status is: "+teststatus);
						logger.info("the comparision status is: "+teststatus);
					  
						
				  TID=(String)uplaodmap.get("TID");
				  }
						  
					 
			  else{
				  teststatus=0;
				  logger.info(sftuser+" failed to upload "+basefilename);
				  TID="Upload Failed";
			  }
		  }else{
			  logger.info(sftuser+" unable to login to "+servername);
			  teststatus=0;
			  TID="Login Failed";
		  }
		  
		  
	   System.out.println("the QC upload is started");
	   logger.info("the QC upload is started");
		  
		  TestcaseLookup tl =new TestcaseLookup(logger);
		  
		  lst = tl.lookupTestcase(FrameworkConstants.TCLookup, "G401");
		  LoadProperties lp1=new LoadProperties(FrameworkConstants.RunIdFile);
		  runIdFile=(lp1.readProperty("RUNID"));
		  wtr=new WriteTestResult();
		  testlog=new WriteTestResult();
		  
		  switch(almupdate)
		  {
		  case "No":
			  if(teststatus==1)
			  {
			  for(int i=0;i<lst.size();i++)
			  {
				  logger.info("Updating"+lst.get(i)+"status as Passed");
				  wtr.writeToFile(runIdFile,"G401,"+ lst.get(i)+","+TID+",Passed");
			  }
			  }else
			  {
				  for(int i=0;i<lst.size();i++)
				  {
					  logger.info("Updating"+lst.get(i)+"status as Failed");
					  wtr.writeToFile(runIdFile,"G401,"+ lst.get(i)+","+TID+",Failed");
				  }
				  
			  }
			  break;
		  case "no":
			  if(teststatus==1)
			  {
			  for(int i=0;i<lst.size();i++)
			  {
				  logger.info("Updating"+lst.get(i)+"status as Passed");
				  wtr.writeToFile(runIdFile,"G401,"+ lst.get(i)+","+TID+",Passed");
			  }
			  }else
			  {
				  for(int i=0;i<lst.size();i++)
				  {
					  logger.info("Updating"+lst.get(i)+"status as Failed");
					  wtr.writeToFile(runIdFile,"G401,"+ lst.get(i)+","+TID+",Failed");
				  }
				  
			  }
			  break;
		  case "yes":
			  updateALM();
			  break;
		  case "Yes":
			  updateALM();
			  break;
		  }
		  
		  logger.info("G401CDDelTextCheck Execution completed");
		  Logoff loff=new Logoff(logger);
		  loff.logofffromSFT(connectionobj_cd);
	 }
	 catch(NullPointerException e1)
     {   e1.printStackTrace();
     	logger.info("unable to proceed:\t"+e1);
     	
     	
     }
		
		catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			logger.info("unable to proceed:\t"+e2);
		}
	 catch (InterruptedException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			logger.info("unable to proceed:\t"+e3);
		}
	 
	 }
	 public void updateALM()
	 {
		  /*ALMConnect alm = new ALMConnect();
		  boolean qcstatus = alm.connectALM(qcurl, qcuname, qcpwd, domain, project );
		  if(qcstatus){
			  if(teststatus==1){
				  String strStatus="Passed";
				  String filePath=FrameworkConstants.RunLog;
				  String workdir=System.getProperty("user.dir");
		          String fileName=workdir+"\\"+FrameworkConstants.RunLog;
		          System.out.println("workdir"+workdir);
				  for(int i=0;i<lst.size();i++)
				  {
					  logger.info("Updating"+lst.get(i)+"status as Passed");
					  alm.updateTestCase(TLpath, TSet, lst.get(i), strStatus, filePath);
					  wtr.writeToFile(runIdFile,"G401,"+ lst.get(i)+","+TID+",Passed");
				  }
			  }else{
					  String strStatus="Failed";
					  String filePath=FrameworkConstants.RunLog;
					  String workdir=System.getProperty("user.dir");
			          String fileName=workdir+"\\"+FrameworkConstants.RunLog;
			          System.out.println("workdir"+workdir);
					  for(int i=0;i<lst.size();i++)
					  {
						  logger.info("Updating"+lst.get(i)+"status as Failed");
						  alm.updateTestCase(TLpath, TSet, lst.get(i), strStatus, filePath);
						  wtr.writeToFile(runIdFile,"G401,"+ lst.get(i)+","+TID+",Failed");
					  }
				  }
				  
			  }else{
			  System.out.println("Unable to login to ALM");
			  }*/

	 
	 }	 

}
