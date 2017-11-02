package amex.fs.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class SCPClient {

	public static void main(String[] arg) throws Exception{
		/*String ftpServer = "mft.e2-env1.sft.dev.ipc.us.aexp.com";
		String sftuser = "MBXUSER";
		String inputFileName = "Mail_File";
		String rPath1 = "outbox/";
		String rPath2 = "sent/";
		String expRmtFileName = "Mail_File$S$";*/

		String ftpServer = "fsgatewaytest.intra.aexp.com";
		String sftuser = "GK_User_04";
		String inputFileName = "Mail_File";
		String rPath1 = "outbox/";
		String rPath2 = "sent/";
		String expRmtFileName = "Mail_File";

		LinuxBoxFileTransfer lbft = null;
		SCPClient scpc = null;
		try{
			lbft = new LinuxBoxFileTransfer();
			scpc = new SCPClient();
			LoadProperties lp=new LoadProperties(FrameworkConstants.SFT);
			String unixServerUrl=lp.readProperty("unixServerUrl");
			String unixId =lp.readProperty("unixId");
			String unixPwd =lp.readProperty("unixPwd");

			boolean bootStatus = lbft.bootLinuxBox(unixServerUrl, 446, unixId, null, unixPwd);
			if(!bootStatus) throw new Exception("Unable to Boot linux box");

			String insertTime = scpc.getMSTTime();

			if(! scpc.push(ftpServer, sftuser, FrameworkConstants.DefaultSFTPWD, "/inbox/", inputFileName)){
				throw new Exception("Unable to transfer file to "+ftpServer+" in SCP mode");
			}
			else{
				System.out.println("SCP Push successfull");
			}

			if(! lbft.sendCommand("ftp -inv "+ftpServer).contains("Connected to "+ftpServer)) 
				throw new Exception("Unable to connect to "+ftpServer);

			if(! lbft.sendCommand("user "+sftuser+" "+FrameworkConstants.DefaultSFTPWD).contains("230 User logged in")) 
				throw new Exception("Unable to login");

			lbft.sendCommand("cd "+rPath1);

			String output = lbft.sendCommand("ls -R");
			String temp = "";
			do{
				temp = lbft.getOutput();
				if(temp != null && !temp.isEmpty()) { System.out.println(temp); output+=temp;}
			}
			while(!temp.isEmpty());

			String outputFileName = scpc.getFileNamefromOutput(output, expRmtFileName, insertTime);
			System.out.println("SCP Push successfull with file: "+outputFileName);

			lbft.sendCommand("bye");

			if(! scpc.pull(ftpServer, sftuser, FrameworkConstants.DefaultSFTPWD, rPath1, outputFileName)){
				throw new Exception("Unable to transfer file from "+ftpServer+" in SCP mode");
			}
			else{
				System.out.println("SCP Pull1 successfull");
			}

			FileComparision fc =new FileComparision(LoggerFactory.getLogger(SCPClient.class));
			try{
				System.out.println("Verifying the content of "+inputFileName+" against "+outputFileName);
				boolean contentVerificationvalue= fc.contentVerification(inputFileName,FrameworkConstants.DownloadDirectory+"/"+outputFileName+"_"+rPath1.replaceAll("/", ""));
				System.out.println("contentVerificationvalue of "+rPath1+" :"+contentVerificationvalue);

			}catch(Exception exc)
			{
				System.out.println("Exception in file type verification is: ");
				exc.printStackTrace();
				throw exc;
			}

			if(! scpc.pull(ftpServer, sftuser, FrameworkConstants.DefaultSFTPWD, rPath2, outputFileName)){
				throw new Exception("Unable to transfer file from "+ftpServer+" in SCP mode");
			}
			else{
				System.out.println("SCP Pull2 successfull");
			}

			fc =new FileComparision(LoggerFactory.getLogger(SCPClient.class));
			try{
				System.out.println("Verifying the content of "+inputFileName+" against "+outputFileName);
				boolean contentVerificationvalue= fc.contentVerification(inputFileName,FrameworkConstants.DownloadDirectory+"/"+outputFileName+"_"+rPath2.replaceAll("/", ""));
				System.out.println("contentVerificationvalue of "+rPath2+" :"+contentVerificationvalue);

			}catch(Exception exc)
			{
				System.out.println("Exception in file type verification is: ");
				exc.printStackTrace();
				throw exc;
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			lbft.destroy();
		}
	}

	public boolean push(String host, String user, String pass, String remotedirectory, String lFile) throws Exception{

		FileInputStream fis=null;

		try{

			JSch jsch=new JSch();
			Session session=jsch.getSession(user, host, 22);
			UserInfo ui=new MyUserInfo(pass);
			session.setUserInfo(ui);
			session.connect();
			String command="scp -t "+remotedirectory+lFile;
			Channel channel=session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			OutputStream out=channel.getOutputStream();
			InputStream in=channel.getInputStream();
			channel.connect();
			if(checkAck(in)!=0){
				return false;
			}
			File lf = new File(lFile);
			long filesize=lf.length();
			command="C0644 "+filesize+" "+lFile+"\n";
			out.write(command.getBytes()); out.flush();
			if(checkAck(in)!=0){
				return false;
			}
			fis=new FileInputStream(lFile);
			byte[] buf=new byte[1024];
			while(true){
				int len=fis.read(buf, 0, buf.length);
				if(len<=0) break;
				out.write(buf, 0, len);
			}
			fis.close();
			fis=null;
			buf[0]=0; out.write(buf, 0, 1); out.flush();
			if(checkAck(in)!=0){
				return false;
			}
			out.close();
			channel.disconnect();
			session.disconnect();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}

	public boolean pull(String host, String user, String pass, String rPath, String rFile) throws Exception{
		Session session = null;
		try{
			FileOutputStream fs=null;
			JSch jsch=new JSch();
			session=jsch.getSession(user, host, 22);
			UserInfo ui=new MyUserInfo(pass);
			session.setUserInfo(ui);
			session.connect();
			String command="scp -f "+rPath+rFile;
			Channel channel=session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			OutputStream out=channel.getOutputStream();
			InputStream in=channel.getInputStream();
			channel.connect();
			byte[] buf=new byte[1024];
			buf[0]=0; out.write(buf, 0, 1); out.flush();
			while(true){
				int c=checkAck(in);
				if(c!='C'){
					break;
				}
				in.read(buf, 0, 5);
				long filesize=0L;
				while(true){
					if(in.read(buf, 0, 1)<0){
						break; 
					}
					if(buf[0]==' ')break;
					filesize=filesize*10L+(long)(buf[0]-'0');
				}
				String file=null;
				for(int i=0;;i++){
					in.read(buf, i, 1);
					if(buf[i]==(byte)0x0a){
						file=new String(buf, 0, i);
						break;
					}
				}
				System.out.println("filesize="+filesize+", file="+file);
				if(filesize <= 0){
					System.out.println("Failing transfer due to zero filesize");
				}
				buf[0]=0; out.write(buf, 0, 1); out.flush();
				fs=new FileOutputStream(FrameworkConstants.DownloadDirectory+rFile+"_"+rPath.replaceAll("/", ""));
				int index;
				while(true){
					if(buf.length<filesize) index=buf.length;
					else index=(int)filesize;
					index=in.read(buf, 0, index);
					if(index<0){
						break;
					}
					fs.write(buf, 0, index);
					filesize-=index;
					if(filesize==0L) break;
				}
				fs.close();
				fs=null;
				if(checkAck(in)!=0){
					return false;
				}
				buf[0]=0; out.write(buf, 0, 1); out.flush();
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			session.disconnect();
		}
	}

	int checkAck(InputStream in) throws IOException{
		int b=in.read();
		if(b==0) return b;
		if(b==-1) return b;
		if(b==1 || b==2){
			StringBuffer sb=new StringBuffer();
			int c;
			do{c=in.read();sb.append((char)c);}
			while(c!='\n');
			if(b==1 || b == 2){
				System.out.print(sb.toString());
			}
		}
		return b;
	}

	public String getFileNamefromOutput(String output, String expRmtFileName, String insertTimeStr) throws Exception{

		List<String> fileList = null; String outputFileName = "";
		if(output.indexOf("\r") != -1 && output.indexOf("\n") == -1) fileList = Arrays.asList(output.split("\r"));
		else if(output.indexOf("\n") != -1 && output.indexOf("\r") == -1) fileList = Arrays.asList(output.split("\n"));
		else if(output.indexOf("\r\n") != -1) fileList = Arrays.asList(output.split("\r\n"));

		if(fileList != null){

			Iterator<String> fileListIterator = fileList.iterator();

			while(fileListIterator.hasNext()){
				String fileListNode = fileListIterator.next();
				if(fileListNode == null || fileListNode.isEmpty()) continue;

				if(fileListNode.indexOf(expRmtFileName) != -1){
					outputFileName = fileListNode;
				}

			}

			if(outputFileName.isEmpty()) throw new Exception("No files matching the given name: "+expRmtFileName);

			System.out.println("outputFileName:"+outputFileName);

			String[] outputArr = outputFileName.split("\\s+");

			if(outputArr.length < 9) throw new Exception("Unable to split the output file name: "+outputFileName);

			outputFileName = outputArr[8].trim();
			String outputFileTimeStr = outputArr[5] +" "+ outputArr[6] +" "+ outputArr[7];

			TimeZone mst = TimeZone.getTimeZone("MST");
			DateFormat formatter = new SimpleDateFormat("MMM dd HH:mm");
			formatter.setTimeZone(mst);

			Calendar insertTime = Calendar.getInstance();
			insertTime.setTime(formatter.parse(insertTimeStr));

			Calendar outputFileTime = Calendar.getInstance();
			outputFileTime.setTime(formatter.parse(outputFileTimeStr));
			System.out.println("Insert time:"+insertTimeStr+" Output time:"+outputFileTimeStr);

			if(insertTime.compareTo(outputFileTime) <= 0){
				return outputFileName;
			}
			else{
				throw new Exception("Insert time is mismatching. Might be ref. old file");
			}

		}
		else{
			throw new Exception("Error.");
		}
	}

	public String getMSTTime() throws Exception{
		TimeZone mst = TimeZone.getTimeZone("MST");
		DateFormat formatter = new SimpleDateFormat("MMM dd HH:mm");
		formatter.setTimeZone(mst);

		Calendar currentdate = Calendar.getInstance(mst);
		currentdate.setTime(formatter.parse(formatter.format(currentdate.getTime())));
		return formatter.format(currentdate.getTime()) ;
	}

}


class MyUserInfo implements UserInfo{
	String passwd;
	public MyUserInfo(String passwd) { this.passwd = passwd; };

	public String getPassword(){ return passwd; }
	public boolean promptYesNo(String str){ return true;   }
	public String getPassphrase(){ return null; }
	public boolean promptPassphrase(String message){ return true; }
	public boolean promptPassword(String message){ return true;}
	public void showMessage(String message){}
}