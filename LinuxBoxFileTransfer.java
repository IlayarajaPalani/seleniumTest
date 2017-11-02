package amex.fs.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class LinuxBoxFileTransfer {

	Session unixSession;
	Channel     channel;
	ChannelShell shellChannel;
	ChannelSftp sftpChannel;
	InputStreamReader commandOutput;
	PrintWriter printWriter    = null;

	public static void main(String[] args) {
		LinuxBoxFileTransfer lbft = null;
		try {
			LoadProperties lp=new LoadProperties(FrameworkConstants.SFT);
			String unixServerUrl=lp.readProperty("unixServerUrl");
			String unixId =lp.readProperty("unixId");
			String unixPwd =lp.readProperty("unixPwd");
			String unixHomeDir =lp.readProperty("unixHomeDir");
			String ftpServer = lp.readProperty("server");
			//FTP
			/*String localFileName = "Mail_File.txt";
			String remoteFileName  = "Mail_File.txt";
			String sftUser = "MBXUSER";
			String uploadMode = "binary", downloadMode = "binary";
			String upLoadDir ="/inbox", downloadDir = "/outbox";
			String transferMode = "ftp";*/
		
			//SFTP
			String server = "fsgatewaytest.intra.aexp.com";
			String localFileName = "REMOTEFILE";
			String remoteFileName  = "REMOTEFILE";
			String sftUser = "REMOTEDELUSER";
		String uploadMode = "binary", downloadMode = "binary";
		String upLoadDir ="/inbox", downloadDir = "/outbox";
			String transferMode = "sftp";
		
			lbft = new LinuxBoxFileTransfer();
			boolean bootStatus = lbft.bootLinuxBox(unixServerUrl, 446, unixId, null, unixPwd);
			if(!bootStatus) throw new Exception("Unable to Boot linux box");

			boolean uploadStatus = lbft.uploadFromLocalToBox(unixHomeDir, localFileName, remoteFileName, "777");
			if(!uploadStatus) throw new Exception("Unable to upload local file");

			lbft.sendCommand("cd "+unixHomeDir);
			lbft.sendCommand("ls");

			String inputFileName = remoteFileName;
			
			if(transferMode.equals("ftp")){
				lbft.sendCommand("ftp -inv "+ftpServer);
				lbft.sendCommand("user "+sftUser+" "+FrameworkConstants.DefaultSFTPWD);
				lbft.sendCommand(uploadMode);
				lbft.sendCommand("put "+inputFileName);
				lbft.sendCommand("cd "+upLoadDir);
				String output = lbft.sendCommand("ls -R");
				String outputFileName = lbft.getFileNamefromOutput(output, inputFileName);
				lbft.sendCommand("cd ..");
				lbft.sendCommand("cd "+downloadDir);
				lbft.sendCommand("ls -R");
				lbft.sendCommand(downloadMode);
				lbft.sendCommand("get "+outputFileName);
				lbft.sendCommand("bye");


			lbft.sendCommand("cd "+unixHomeDir);

				String inputFile = lbft.sendCommand("cat -v "+inputFileName);

				System.out.println(lbft.checkFileEolType(inputFile));

				String outputFile2 = lbft.sendCommand("cat -v "+outputFileName);

				System.out.println(lbft.checkFileEolType(outputFile2));
			}
			else if(transferMode.equals("sftp")){
			lbft.sendCommand("lftp");
			lbft.sendCommand("set ftp:ssl-force true");
			lbft.sendCommand("set ftp:ssl-protect-data true");
			lbft.sendCommand("connect "+server);
			lbft.sendCommand("login "+sftUser+" "+FrameworkConstants.DefaultSFTPWD);
				lbft.sendCommand("put "+inputFileName);
			lbft.sendCommand("cd "+upLoadDir);
			String output = lbft.sendCommand("ls -R");
			String outputFileName = lbft.getFileNamefromOutput(output, inputFileName);
			lbft.sendCommand("cd ..");
			lbft.sendCommand("cd "+downloadDir);
			lbft.sendCommand("ls -R");
			lbft.sendCommand("get "+outputFileName);
			lbft.sendCommand("bye");
			
			lbft.sendCommand("cd "+unixHomeDir);

			String inputFile = lbft.sendCommand("cat -v "+inputFileName);

			System.out.println(lbft.checkFileEolType(inputFile));

			String outputFile2 = lbft.sendCommand("cat -v "+outputFileName);

				System.out.println(lbft.checkFileEolType(outputFile2));
			}
		
			

		} catch (Exception e) {
			e.printStackTrace();
	}
		finally{
			if(lbft != null){
				lbft.destroy();
			}

		}
	}


	public String sendCommand(String command) throws Exception{
		if(command == null || command.isEmpty()) throw new Exception("Given command is null or empty");
		printWriter.println(command);
		String output = getOutput();
		System.out.println(output);
		if(output.contains("Invalid command") || output.contains("Unknown command")) throw new Exception("Invalid/Unknown Command");
		return output;
	}

	public boolean bootLinuxBox(String host, int port, String uName, String pvtKey, String pass){

		try{
			System.out.println("Connecting...");

			if(host == null || uName == null || pass == null
					|| host.isEmpty() || uName.isEmpty() || pass.isEmpty()){
				System.out.println("Unable to connect. Input parameters empty.");
				return false;
			}
				
			
			JSch jsch=new JSch();

			unixSession=jsch.getSession(uName, host, port);

			if (pvtKey!=null)
			{
				System.out.println("Key Based Authentication!!!");
				jsch.addIdentity(pvtKey);
			}
			else
			{
				System.out.println("Password authentication");
				System.out.println(uName);
				System.out.println(pass);
				
				unixSession.setPassword(pass);
			}

			jsch.setKnownHosts(FrameworkConstants.KnownHostRepositoryPath);

			unixSession.setConfig("StrictHostKeyChecking", "YES");

			unixSession.connect();   
			channel = unixSession.openChannel("shell");
			shellChannel = ((ChannelShell)channel);
			shellChannel.setPty(true);
			shellChannel.setPtyType("dumb");
			sftpChannel = (ChannelSftp) unixSession.openChannel("sftp");

			channel.connect();
			sftpChannel.connect();

			System.out.println("Connection Established.");

			Thread.sleep(5000);

			InputStream inStream = shellChannel.getInputStream();
			commandOutput = new InputStreamReader(inStream);

			OutputStream outStream = shellChannel.getOutputStream();
			printWriter = new PrintWriter(new OutputStreamWriter(outStream), true); 
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}

		if(unixSession.isConnected())

			return true;
		else
			
			return false;
	}

	public boolean uploadFromLocalToBox(String remotePath, String localFileName, String remoteFileName, String permission){

		try {
			if(sftpChannel.isConnected()){
			sftpChannel.cd(remotePath);
			System.out.println(sftpChannel.pwd());
			System.out.println(remotePath);
			File mailFile = new File(localFileName);
			System.out.println(localFileName);
			System.out.println(remoteFileName);
			FileInputStream fis = new FileInputStream(mailFile);
			//System.out.println("File is available");
			sftpChannel.put(fis, remoteFileName, ChannelSftp.OVERWRITE);
			sftpChannel.chmod(Integer.parseInt(permission,8), remotePath+"/"+remoteFileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public boolean downloadFromBoxToLocal(String remoteFileName, String localFileName){

		try {
			
			sftpChannel.get(remoteFileName, localFileName);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public String getOutput() throws IOException, InterruptedException{

		int hits = 0, threshold = 5;
		StringBuilder line = new StringBuilder();

		while(hits < threshold){
			if(!commandOutput.ready()){
				Thread.sleep(2000);
				hits++;
				continue;
			}
			else{
				hits = threshold;
				while (commandOutput.ready()) {
					char toAppend = (char) commandOutput.read();
					line.append(toAppend);
				}
			}
		}

		return line.toString();
	}

	public String getFileNamefromOutput(String output, String inputFileName) throws Exception{

		List<String> fileList = null; String outputFileName = "";
		if(output.indexOf("\r") != -1 && output.indexOf("\n") == -1) fileList = Arrays.asList(output.split("\r"));
		else if(output.indexOf("\n") != -1 && output.indexOf("\r") == -1) fileList = Arrays.asList(output.split("\n"));
		else if(output.indexOf("\r\n") != -1) fileList = Arrays.asList(output.split("\r\n"));

		if(fileList != null){

			Iterator<String> fileListIterator = fileList.iterator();

			while(fileListIterator.hasNext()){
				String fileListNode = fileListIterator.next();
				if(fileListNode.indexOf(inputFileName) != -1)
					outputFileName = fileListNode;
			}

			if(outputFileName.isEmpty()) throw new Exception("No files matching the given name: "+inputFileName);

			System.out.println("outputFileName:"+outputFileName);

			String[] outputArr = outputFileName.split("\\s+");

			if(outputArr.length < 2) throw new Exception("Unable to split the output file name: "+outputFileName);

			outputFileName = outputArr[outputArr.length-1].trim();

			return outputFileName;
		}
		else{
			throw new Exception("Error.");
		}
	}

	public String checkFileEolType(String fileContent) throws Exception{
		if(fileContent != null && !fileContent.isEmpty()){
			if(fileContent.indexOf("^M") != -1)
				return FrameworkConstants.WinEOL;
			else
				return FrameworkConstants.UnixEOL;
		}
		else{
			throw new Exception("");
		}
	}
	
	public String getServerTime() throws Exception{

		printWriter.println("date \"+%b %d %k:%M\"");
		String output = getOutput();
		
		if(output.contains("Invalid command") || output.contains("Unknown command")) throw new Exception("Invalid/Unknown Command");
		
		List<String> lineList = null;
		if(output.indexOf("\r") != -1 && output.indexOf("\n") == -1) lineList = Arrays.asList(output.split("\r"));
		else if(output.indexOf("\n") != -1 && output.indexOf("\r") == -1) lineList = Arrays.asList(output.split("\n"));
		else if(output.indexOf("\r\n") != -1) lineList = Arrays.asList(output.split("\r\n"));
		
		String temp = "";
		for(String line : lineList){
			if(line.indexOf(":") != -1 && line.length()==12){
				temp = line;
				break;
			}
		}
		if(temp.isEmpty()) throw new Exception("Unable to fetch date");
		else output = temp;
		return output ;
	
	}
	
	public void destroy() {
		System.out.println("Disconnecting...");
		try {
			if (printWriter != null) {
				printWriter.close();
			}
		} catch (Exception ioe) {
			// ignore
		}

		try {
			if (commandOutput != null) {
				commandOutput.close();
			}
		} catch (Exception ioe) {
			// ignore
		}

		try {
			if (channel != null) {
				channel.disconnect();
			}
		} catch (Exception ioe) {
			// ignore
		}

		try {
			if (unixSession != null) {
				unixSession.disconnect();
			}
		} catch (Exception ioe) {
			// ignore
		}
		System.out.println("Disconnected");
	}
}
