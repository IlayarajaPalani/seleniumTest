/* Description :
 * This module uploads the file in the specified directory
 * The file is uploaded as per the Filetransfermode and filetype specified in the input
 */

package amex.fs.commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Upload {
	Map<String, Object> uploadobj = new HashMap<String, Object>();
	//static Logger logger;
	public static org.slf4j.Logger logger = LoggerFactory.getLogger(Upload.class);
	Map loginobj = null;
	FTPSClient ftpsclient = null;
	FTPClient ftpclient = null;
	String filename;
	boolean result = false;
	long filesize;
	SimpleDateFormat df;
	String[] TID=null;

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public Upload(org.slf4j.Logger logger )
	{
		this.logger=logger;
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Login lg = new Login(logger);
		
		Map loginobj = lg.logintoSFT("fsgatewaytest.intra.aexp.com", 21, "REMOTEDELUSERPOA", "amex123", "FTP");
		System.out.println("Protocol------------>"+loginobj.get("protocol"));
		System.out.println(loginobj);
		Upload up = new Upload(logger);
		
		Map uploadobj = null;
		
		uploadobj = up.uploadFile(loginobj, "REMOTEDELFILE",  "TESTFILE.txt",  "/inbox",  "BINARY", "PASSIVE");
		
		System.out.println(uploadobj);

	}
	
		
	public Map<String, Object> uploadFile(Map connectionobj, String Remotefilename, String  Localfilename, String Remotedir, String Filetype, String Filetransfermode) throws IOException
	{
	logger.info("The file upload started");
	
	/*
	 * The input protocol type is determined.
	 * If the protocol is FTP then FTP connection is established.
	 * If the protocol is FTPS then FTPS connection is established.
	 */
	/*
	 * The FTP module 
	 * 
	 */
		try
		{
			if(connectionobj.get("protocol").equals("FTP"))
			{
				logger.info("The upload protocol is FTP");
				ftpclient = (FTPClient) connectionobj.get("connection");
				if (ftpclient!= null)
					{
						if (Filetype.equalsIgnoreCase("ASCII"))
						{
							ftpclient.setFileType(ftpclient.ASCII_FILE_TYPE);
							logger.info("FileType       ASCII");
						}
						else
						{
							ftpclient.setFileType(ftpclient.BINARY_FILE_TYPE);
							logger.info("FileType       BINARY");
						}
						if (Filetransfermode.equalsIgnoreCase("ACTIVE"))
							{
								ftpclient.enterLocalActiveMode();
								logger.info("FileTransferMode       ACTIVE");
							}
							else
							{
								ftpclient.enterLocalPassiveMode();
								logger.info("FileTransferMode       PASSIVE");
							}
				
						ftpclient.changeWorkingDirectory(Remotedir);
						logger.info("The directory changed to: "+Remotedir);
						InputStream inputStream = new FileInputStream(Localfilename);
						result = ftpclient.storeFile(Remotefilename, inputStream);
						logger.info("Fileuploadstatus       "+result);
						System.out.println("Fileuploadstatus       "+result);
						inputStream.close();
						if(result){
						boolean dir = ftpclient.changeWorkingDirectory("/inbox");
					
						
						Thread.sleep(8000);
						FTPFile[] files= ftpclient.listFiles();
				/*
				 * Getting the uploaded  file details
				 */
						
						
						if((files == null) || (files.length == 0))
						{
							result = false;
							uploadobj.put("uploadstatus", result);
							logger.info("The upload result: "+ result);
						}
						else{
							for (FTPFile file : files)
							{
								df = new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
								String filename = file.getName();
								logger.info("uploaded File name            "+filename);
								filesize = file.getSize();
								Calendar modifiedtime = file.getTimestamp();
								System.out.println(filesize);
								System.out.println(filename);
								logger.info("The file name is: "+filename);
								logger.info("The file size is: "+filesize);
								uploadobj.put("Filename", filename);
								uploadobj.put("Filesize", filesize);
								uploadobj.put("uploadstatus", result);
								TID=filename.split("#");
								uploadobj.put("TID", TID[1]);
								String formatted = df.format(modifiedtime.getTime());
								try 
								{
									Date date =df.parse(formatted);
								}
								catch (ParseException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
									logger.info("The exception is: "+e.getMessage());
								}
						//System.out.println("formatteddate "+formatted);
								uploadobj.put("Time", formatted);
								logger.info("The upload time is: "+formatted);
								logger.info("The upload result: "+ result);
							}
						}
						
				}
						
				else
				{
					uploadobj.put("uploadstatus", result);
					logger.info("The upload result: "+ result);
				}
			}
		}
			
	else{
			
			/*
			 * The FTPS module 
			 * 
			 */
			
			
				ftpsclient = (FTPSClient) connectionobj.get("connection");
				logger.info("The upload protocol is FTPS");
				if(ftpsclient!= null)
				{
					if (Filetype.equalsIgnoreCase("ASCII"))
						{
							ftpsclient.setFileType(ftpsclient.ASCII_FILE_TYPE);
							logger.info("FileType       ASCII");
						}
					else
						{
							ftpsclient.setFileType(ftpsclient.BINARY_FILE_TYPE);
							logger.info("FileType       BINARY");
						}
					if (Filetransfermode.equalsIgnoreCase("ACTIVE"))
						{
							ftpsclient.enterLocalActiveMode();
							logger.info("FileTransferMode       ACTIVE");
						}
					else{
							ftpsclient.enterLocalPassiveMode();
							logger.info("FileTransferMode       PASSIVE");
						}
					ftpsclient.changeWorkingDirectory(Remotedir);
					InputStream inputStream = new FileInputStream(Localfilename);
					result = ftpsclient.storeFile(Remotefilename, inputStream);
					logger.info("Fileuploadstatus       result");
					inputStream.close();
					if(result){
					boolean dir = ftpsclient.changeWorkingDirectory("/inbox");
					Thread.sleep(8000);
					FTPFile[] files= ftpsclient.listFiles();
				
				/*
				 * Getting the uploaded  file details
				 */
					
					if((files == null) || (files.length == 0))
					{
						result = false;
						uploadobj.put("uploadstatus", result);
						logger.info("The upload result: "+ result);
					}
					else{
					for (FTPFile file : files)
					{
						df = new SimpleDateFormat("M/dd/yyyy HH:mm:ss");
						String filename = file.getName();
						logger.info("uploaded File name: "+filename);
						filesize = file.getSize();
						logger.info(" The uploaded file size: "+filesize);
						Calendar modifiedtime = file.getTimestamp();
						System.out.println(filesize);
						System.out.println(filename);
						uploadobj.put("Filename", filename);
						uploadobj.put("Filesize", filesize);
						uploadobj.put("uploadstatus", result);
					
						TID=filename.split("#");
						uploadobj.put("TID", TID[1]);
					
						String formatted = df.format(modifiedtime.getTime());
						try 
						{
							Date date =df.parse(formatted);
						} catch (ParseException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					//System.out.println("formatteddate "+formatted);
						uploadobj.put("Time", formatted);
						logger.info("The upload time: "+formatted);
						logger.info("The upload result: "+ result);
					}
				}
					}
				else
				{
					uploadobj.put("uploadstatus", result);
					uploadobj.put("TID","No File Uploaded");
					logger.info("The upload result: "+ result);
					
				}
			}
		}
	}
	catch(Exception e)
	{
		uploadobj.put("uploadstatus", result);
		uploadobj.put("TID", "No File Uploaded");
		logger.info("The upload result: "+ result);
	}	
		
		return uploadobj;
	}
 
}
