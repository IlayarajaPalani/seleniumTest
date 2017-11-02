/*Description: This module perform LOGIN( password and keybased), UPLOAD and DOWNLOAD of file using SFTP protocol 
 */
package amex.fs.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SFTP_Module {
	
	Map<String, Object> Loginmap = new HashMap<String, Object>();
	Map<String, Object> uploadmap = new HashMap<String, Object>();
	Map<String, Object> downloadmap = new HashMap<String, Object>();
	ChannelSftp channelSftp= null;
	Channel channel = null;
	String knownHostRepositoryPath=FrameworkConstants.KnownHostRepositoryPath;
	String fingerPrint= "";
	boolean passwordAuthentication = false;
	boolean hostKeyAlredyPresent= false;
	Session session = null;
	boolean status = false;
	String acceptHostKey = FrameworkConstants.AcceptHostKey;
	static String pvt = FrameworkConstants.Key;
	private static org.slf4j.Logger logger;
	SimpleDateFormat df;
	public SFTP_Module(org.slf4j.Logger logger )
	{
		SFTP_Module.logger=logger;
	}
	

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		SFTP_Module tst = new SFTP_Module(logger);
		
		Map Login =tst.logintoSFT("mft.e2-env1.sft.dev.ipc.us.aexp.com", 22, "G83SFTPDOW_TEXT", "amex123", null, "YES");
		System.out.println(Login);
		Map uploadmap = tst.uploadFile(Login,"TESTFILE.txt", "LoginFile", "/inbox");
		tst.logofffromSFT(Login);
	}
	
	/*
	 * The login module begins
	 */
	public Map<String, Object> logintoSFT(String Host, int Port, String Username, String  Password,String PvtKey,String acceptHostKey)
	{
		JSch jsch=new JSch();
		java.util.Properties config = new java.util.Properties();
		try 
		{
				
				jsch.setKnownHosts(knownHostRepositoryPath);
			
				/*
				* Conneting to the host with the specified authentication type
				*/
				session=jsch.getSession(Username, Host, Port);
				if (PvtKey!=null)
				{
					System.out.println("Key Based Authentication!!!");
					logger.info("Key Based Authentication!!!");
					jsch.addIdentity(PvtKey);
				}
				else
				{
					System.out.println("Password authentication");
					logger.info("Password authentication");
					passwordAuthentication = true;
					try
					{
						System.out.println("Hello1");	
						session.setPassword(Password);
						System.out.println("Hello2");
					}catch(Exception e)
					{
						System.out.println("Hello3");
						logger.info(e.getMessage());
						Loginmap.put("result", false);
						Loginmap.put("TID","Login Failed");
					}
        		}
				if (knownHostRepositoryPath != null) 
				{
					/*
					* Getting the known host details, if the host is new then save the key in in the known host file 
					*/
					System.out.println("Hello4");
					jsch.setKnownHosts(knownHostRepositoryPath);
					System.out.println("Hello5");
					hostKeyAlredyPresent=checkHostKey(jsch, fingerPrint, Host);
					System.out.println("Hello6");
	        		if(!hostKeyAlredyPresent)
	        		{
	        			System.out.println("Hello7");
						boolean bAcceptHostKey = false;
						if (acceptHostKey.equalsIgnoreCase("YES"))
						{
							bAcceptHostKey = true;
						}
						if (bAcceptHostKey) {
							config.put("StrictHostKeyChecking", "NO");

						} else {
							config.put("StrictHostKeyChecking", "YES");

						}
	        		}
	        			
	        	}
	        /*
	         * Establishing the session and channel connection for password based authentication
	         */
	            session.setConfig(config);	                     
	 	        session.connect();	           
	            channel = session.openChannel("sftp");
	            channel.connect();
	            //logger.info("sftp channel opened and connected.");
	            channelSftp = (ChannelSftp) channel;
	            Loginmap.put("result", true);
	            Loginmap.put("connection",channelSftp );
	            Loginmap.put("session", session);
	            Loginmap.put("channel", channel);
	            
		}
		catch (JSchException ex) 
		{
			// TODO Auto-generated catch block
		  try{
			
			/*
	         * Establishing the session and channel connection for key based authentication
	         */
				Loginmap.put("result", false);
				System.out.println("Hello8 "+ ex);
			if (ex.getMessage().contains(PvtKey) || (ex.getMessage().contains("Auth fail") && !passwordAuthentication && Password != null))
			{
					System.out.println("Hello9");
					try {					
						session = jsch.getSession(Username, Host, Port);
						java.util.Properties config1 = new java.util.Properties();
						if (knownHostRepositoryPath != null) 
							{
								jsch.setKnownHosts(knownHostRepositoryPath);
								hostKeyAlredyPresent = checkHostKey(jsch, fingerPrint, Host);
						if (!hostKeyAlredyPresent) 
						{
										boolean bAcceptHostKey = false;
										if (acceptHostKey.equalsIgnoreCase("YES"))
											{
												bAcceptHostKey = true;
											}
										if (bAcceptHostKey)
											{
												config.put("StrictHostKeyChecking", "NO");
												logger.info("The host is present in the known host list");
											}
										else 
											{
												config.put("StrictHostKeyChecking", "YES");
												logger.info("Adding the host to the known host list");
											}
						}
					}
					session.setConfig(config1);
					System.out.println("Hello10");
					session.setPassword(Password);
					System.out.println("Hello11");
					session.connect();
					System.out.println("Hello12");
					channel = session.openChannel("sftp");
					channel.connect();
					channelSftp = (ChannelSftp) channel;
					Loginmap.put("result", true);
			        Loginmap.put("connection",channelSftp );
			        Loginmap.put("session", session);
			        Loginmap.put("channel", channel);
				} catch (Exception ex1) {

					System.out.println("Hello13");
					Loginmap.put("result", false);
			        Loginmap.put("Exception",ex );
			        logger.info("The result is : ", false);
			        logger.info("The Exception is "+ex);
					Loginmap.put("TID","Login Failed");

				}

			} else
			{
				System.out.println("Hello14");
				Loginmap.put("result", false);
		        Loginmap.put("Exception",ex );
		        logger.info("The result : ", false);
		        logger.info("The Exception is "+ex);
				Loginmap.put("TID","Login Failed");

			}
		 }catch(Exception e1)	
		 {
			Loginmap.put("TID","Login Failed");  
			Loginmap.put("result", false);
		 }
		}
		
		
		return Loginmap;
	}

	/*
	 * This module adds the host finger print to the known host file 
	 */
	private boolean checkHostKey(JSch jsch, String fingerPrint2, String Host) {
		// TODO Auto-generated method stub
		HostKeyRepository hsk = jsch.getHostKeyRepository();
		HostKey[] host = hsk.getHostKey();
		List<String> hostList = new ArrayList<String>();
		List<String> hostKeyList = new ArrayList<String>();
		if (host!=null)
		{
		for (int j = 0; j < host.length; j++) {
			hostList.add(host[j].getHost());
			hostKeyList.add(host[j].getKey());
		}
		}
		boolean presentHost = hostList.contains(Host);
		boolean already = false;
		if (presentHost) {
			already = hostKeyList.contains(fingerPrint);
		}
		return already;
	}
	
	
	/*
	 * The below method performs file upload using SFTP protocol
	 */
	
	public Map<String, Object> uploadFile(Map Login, String Physicalfile, String Basefile, String Remotedir) throws FileNotFoundException, ParseException{
		
		if(Login.get("connection")!= null)
		{
			try 
			{
				channelSftp = (ChannelSftp) Login.get("connection");
				channelSftp.cd(Remotedir);
				File f = new File(Physicalfile);
				FileInputStream fis = new FileInputStream(f);
				try
				{
					channelSftp.put(fis, Basefile);
					logger.info("The file "+Basefile+"  is uploaded successfully");
					System.out.println("The file "+Basefile+"  is uploaded successfully");
					System.out.println("The file is uploaded successfully");
				}
				catch(Exception ex)
				{
					System.out.println("msg2"+ex.getMessage());
					logger.info("fileexception : "+ex.getMessage());
					uploadmap.put("TID","Upload Failed");
					uploadmap.put("Status",false);
					
					return uploadmap;
				}
			
				Vector<ChannelSftp.LsEntry> list = channelSftp.ls("/inbox");
				if(!list.isEmpty())
				{
					for(ChannelSftp.LsEntry entry : list) 
					{
							if(".".equals(entry.getFilename())||"..".equals(entry.getFilename()))
							{
							}
							else
							{
								df = new SimpleDateFormat("E MMM d hh:mm:ss z yyyy");
								uploadmap.put("Status", true);
								uploadmap.put("Filename", entry.getFilename());
								SftpATTRS a = entry.getAttrs();
								uploadmap.put("Size", a.getSize());
								String filename=entry.getFilename();
								String TID[]=filename.split("#");
								uploadmap.put("TID",TID[1]);
					
								//uploadmap.put("Time", a.getMtimeString());
								//System.out.println(a.getMtimeString());
								Date date =df.parse(a.getMtimeString());
								String formatted = df.format(date);
								System.out.println(date);
								uploadmap.put("Time", date);
								logger.info("The file name with TID is: "+entry.getFilename());
								logger.info("The file size is: "+a.getSize());
								logger.info("The upload time is: "+formatted);
								logger.info("The upload status is: "+true);
							}
					}
				}
				else
				{
					System.out.println("directory is empty!");
					logger.info("The upload status is: "+false);
					logger.info("Directory is empty!");
					uploadmap.put("Status",false);
					uploadmap.put("TID","Inbox Directory is Empty");
				}	
			
			} 
			catch (SftpException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("The exception is "+e.getMessage());
				uploadmap.put("Status",false);
				uploadmap.put("TID","File Upload Failed");
			}
		
		}else{
			System.out.println("connection failed!!!!");
			logger.info("connection failed!!!!");
			uploadmap.put("Status", false);
			uploadmap.put("TID","Login Connection Failed");
		}
		return uploadmap;
		}
	
	
	/*
	 * The below method performs file download using SFTP protocol
	 */
	public Map<String, Object> downloadFile(Map connection, String Remotefilename, String  Localfilename, String Remotedir) throws IOException{
		if(connection.get("connection")!= null){
			try 
			{
				channelSftp = (ChannelSftp) connection.get("connection");
				channelSftp.cd(Remotedir);
				File f = new File(Localfilename);
				FileOutputStream fout = new FileOutputStream(f);
				Thread.sleep(3000);
				channelSftp.get(Remotefilename, fout);
				if(f.exists()&&f.length()>0){
					downloadmap.put("Filename", f.getName());
					downloadmap.put("Filesize", f.length());
				
					
					//downloadmap.put("Filesize", f.length());
					downloadmap.put("Status", true);
					logger.info("The file name is: "+f.getName());
					logger.info("The file size is: "+f.length());
					logger.info("The download status is:"+true);
					
				}
				else{
					downloadmap.put("Status", false);
					logger.info("The download status is:"+false);
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
				downloadmap.put("Status", false);
				logger.info("The exception is : "+e.getMessage());
			}
		
		}
		return downloadmap;
	}
		
	
	/*
	 * Below method logs off from the the SFT server by disconnecting the channel and session connection
	 */
	public boolean logofffromSFT(Map Loginmap)
	{
		if(Loginmap.get("connection")!=null && Loginmap.get("session")!= null && Loginmap.get("channel")!= null){
			try{
				channelSftp.exit();
				channel.disconnect();
				session.disconnect();
				status = true;
				logger.info("Successfully logged off from SFT server");
			}
			catch(Exception e){
				//logger.info("Exception:""+e);
				logger.info("Failed to log off from SFT server");
				logger.info("The exception is: "+e.getMessage() );
				status = false;
			}
		}
		return status;
	}

}
