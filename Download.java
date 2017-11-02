/*Description: This program is used to perform download operation using FTP and FTPS protocol.
*/
package amex.fs.commons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;

public class Download {
	Map<String, Object> downloadobj= new HashMap<String, Object>();
	FTPClient ftpclient = null;
	FTPSClient ftpsclient = null;
	static Logger logger;
	Boolean downloadstatus = false;
	File f;
	String filename=null;
	long filesize;

	public Download(org.slf4j.Logger logger )
	{
		this.logger=logger;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Login lg = new Login(logger);
		Map loginobj=lg.logintoSFT("mft.e2-env1.sft.dev.ipc.us.aexp.com", 21, "CATUSER", "amex123", "FTPS");
		Thread.sleep(5000);
		System.out.println("Protocol------------>"+loginobj.get("protocol"));
		System.out.println(loginobj);
		Upload up = new Upload(logger);
		
		Map uploadobj = up.uploadFile(loginobj, "filetest1",  "T",  "/inbox",  "BINARY", "PASSIVE");
		System.out.println(uploadobj);
		Download dw = new Download(logger);
		String Downlaodfilename = (String) uploadobj.get("Filename");
		Map downloadobj = dw.downloadFile(loginobj, Downlaodfilename, "C:\\Users\\US5002313\\Desktop\\"+Downlaodfilename, "/outbox", "ASCII", "PASSIVE");
		System.out.println(downloadobj);

	}
	public Map<String, Object> downloadFile(Map connectionobj, String Remotefilename, String  Localfilename, String Remotedir, String Filetype, String Filetransfermode) throws IOException{
		/*If connection is done using FTP then file is downloaded using below steps. 
		*/
	  try
	  {
		if(connectionobj.get("protocol").equals("FTP")){
			ftpclient = (FTPClient) connectionobj.get("connection");
			if(ftpclient!= null){
				if (Filetype.equalsIgnoreCase("ASCII")){
					ftpclient.setFileType(ftpclient.ASCII_FILE_TYPE);
					logger.info("FileType       ASCII");
				}
				else{
					ftpclient.setFileType(ftpclient.BINARY_FILE_TYPE);
					logger.info("FileType       BINARY");
				}
				if (Filetransfermode.equalsIgnoreCase("ACTIVE")){
					ftpclient.enterLocalActiveMode();
					logger.info("FileTransferMode       ACTIVE");
				}
				else{
					ftpclient.enterLocalPassiveMode();
					logger.info("FileTransferMode       PASSIVE");
				}
				ftpclient.changeWorkingDirectory(Remotedir);
				f = new File(Localfilename);
				OutputStream out = new FileOutputStream(f);
				downloadstatus = ftpclient.retrieveFile(Remotefilename, out);
				
				if(downloadstatus)
				{
					filename = f.getName();
					filesize = f.length();
					downloadobj.put("Filename", filename);
					downloadobj.put("filesize", filesize);
					downloadobj.put("downloadstatus", downloadstatus);
					logger.info(filename+" downloaded Status"+"             "+downloadstatus);
					
				}
				else{
					logger.info(filename+" downloaded Status"+"             "+downloadstatus);
					downloadobj.put("downloadstatus", downloadstatus);
				}
			}
			
		}
		
		/*If connection is done using FTPs then file is downloaded using below steps. 
		*/
		if(connectionobj.get("protocol").equals("FTPS")){
			ftpsclient =  (FTPSClient) connectionobj.get("connection");
			if(ftpsclient!= null){
				if (Filetype.equalsIgnoreCase("ASCII")){
					ftpsclient.setFileType(ftpsclient.ASCII_FILE_TYPE);
					logger.info("FileType       ASCII");
				}
				else{
					ftpsclient.setFileType(ftpsclient.BINARY_FILE_TYPE);
					logger.info("FileType       BINARY");
				}
				if (Filetransfermode.equalsIgnoreCase("ACTIVE")){
					ftpsclient.enterLocalActiveMode();
					logger.info("FileTransferMode       ACTIVE");
				}
				else{
					ftpsclient.enterLocalPassiveMode();
					logger.info("FileTransferMode       PASSIVE");
				}
				ftpsclient.changeWorkingDirectory(Remotedir);
				f = new File(Localfilename);
				OutputStream out = new FileOutputStream(f);
				downloadstatus = ftpsclient.retrieveFile(Remotefilename, out);
				if(downloadstatus)
				{
					filename = f.getName();
					filesize = f.length();
					downloadobj.put("Filename", filename);
					downloadobj.put("filesize", filesize);
					downloadobj.put("downloadstatus", downloadstatus);
					logger.info(filename+" downloaded Status"+"             "+downloadstatus);
				}
				else{
					logger.info(filename+" downloaded Status"+"             "+downloadstatus);
					downloadobj.put("downloadstatus", downloadstatus);
				}
			}
		}
	
	  }catch(Exception e)
	  {
		  logger.info("File Download Failed");
		  downloadobj.put("downloadstatus", downloadstatus);
	  }
		return downloadobj;
		
	}

}
