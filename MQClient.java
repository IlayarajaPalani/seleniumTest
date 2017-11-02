
// Description - 

package amex.fs.commons;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class MQClient {
	
	public static void main(String[] args) {
		
		try {
			MQClient mqc = new MQClient();
			String fileName = "G600RETXML.xml";
			String qMngr = "LPQMA725";
			String qName = "SUBMITSECUREFILEFLOW.SFT.RETRIEVAL.REQUEST";
			String hostName = "10.16.157.54"; //"10.66.2.102"; 
			int port = 1414;
			String channel = "SYSTEM.DEF.SVRCONN";
			
			mqc.push(qMngr, qName, hostName, port, channel, fileName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean push(String qMngr, String qName, String hostName, int port, String channel, String localFileName) throws Exception{

		MQQueue defaultLocalQueue = null;
		MQQueueManager qManager = null;


		try{
			
			MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
			MQEnvironment.hostname = "10.16.157.54";
			MQEnvironment.channel = channel;
			MQEnvironment.port = port;
			System.out.println("Loaded Properties for MQ...");

			qManager = new MQQueueManager(qMngr);
			int openOptions = MQC.MQOO_OUTPUT + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INQUIRE;
			defaultLocalQueue = qManager.accessQueue(qName, openOptions);


			MQMessage putMessage = new MQMessage();
			String msg = getFileContent(new File(localFileName));
			putMessage.writeString(msg);
			MQPutMessageOptions pmo = new MQPutMessageOptions(); 
			System.out.println(defaultLocalQueue.getDescription());
			System.out.println(defaultLocalQueue.isOpen());
			defaultLocalQueue.put(putMessage, pmo);
			System.out.println("Message is put on MQ.");
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			if(defaultLocalQueue!=null)defaultLocalQueue.close();
			if(qManager!=null)qManager.close();
		}
		return true;
	}
	
	public boolean pull(String qMngr, String qName, String hostName, int port, String channel, String localFileName) throws Exception{

		MQQueue defaultLocalQueue = null;
		MQQueueManager qManager = null;

		try{
			MQEnvironment.hostname = hostName;
			MQEnvironment.channel = channel;
			MQEnvironment.port = port;
			MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);

			qManager = new MQQueueManager(qMngr);
			int openOptions = MQC.MQOO_FAIL_IF_QUIESCING | MQC.MQOO_INQUIRE | MQC.MQOO_INPUT_SHARED | MQC.MQOO_BROWSE;
			defaultLocalQueue = qManager.accessQueue(qName, openOptions);


			MQMessage getMessage = new MQMessage();
			MQGetMessageOptions gmo = new MQGetMessageOptions();

			System.out.println(defaultLocalQueue.getDescription());
			System.out.println(defaultLocalQueue.isOpen());
			if(defaultLocalQueue.getCurrentDepth() > 0){
				defaultLocalQueue.get(getMessage, gmo);
			}
			else{
				throw new Exception("No message available");
			}
			
			String message = getMessage.readString(getMessage.getMessageLength());
			System.out.println("Message retrieved from MQ:"+message);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			if(defaultLocalQueue!=null)defaultLocalQueue.close();
			if(qManager!=null)qManager.close();
		}
		return true;
	}

	public String getFileContent(File file) throws IOException{
		FileReader inputStream = null;
		String fileContent = "";
		try{
			inputStream = new FileReader(file);
			int c;
			while ((c = inputStream.read()) != -1) {
				char ch = (char) c;
				fileContent+=ch;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		finally{
			if(inputStream!=null)inputStream.close();
		}


		return fileContent;
	}


}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~POD XML Reading~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 
try {
	
	MQClient mqc = new MQClient();
	String fileName = "PODXML.txt";
	String qMngr = "LPDMA620";
	String qName = "SFTDELIVERYFUNCTION.ST.INPUT";
	String hostName = "148.171.30.200"; 
	int port = 1414;
	String channel = "SYSTEM.DEF.SVRCONN";
	
	mqc.push(qMngr, qName, hostName, port, channel, fileName);
	mqc.pull(qMngr, qName, hostName, port, channel, fileName);

} catch (Exception e) {
	e.printStackTrace();
}

*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~POA XML Posting~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * POA E2 Alias queue 
 
try {
	
	MQClient mqc = new MQClient();
	String fileName = "POAXML.txt";
	String qMngr = "LPQMA618M";
	String qName = "SUBMITSECUREFILEFLOW.NU.REQUEST.E2NGPS1";
	String hostName = "10.66.2.102"; 
	int port = 1414;
	String channel = "SYSTEM.DEF.SVRCONN";
	
	mqc.push(qMngr, qName, hostName, port, channel, fileName);
	
} catch (Exception e) {
	e.printStackTrace();
}

*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~POD XML Posting~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		MQClient mqc = new MQClient();

		LoadProperties lp=new LoadProperties(FrameworkConstants.SFT);
		String unixServerUrl=lp.readProperty("unixServerUrl");
		String unixId =lp.readProperty("unixId");
		String unixPwd =lp.readProperty("unixPwd");
		String ftpServer = lp.readProperty("server");

		String uName1 = "GK_ST_01"; 
		String folName1 = "/sent/";
		String fName1 = "GK_STF_01#P24HFCL4930B6L";

		String uName2 = "GK_ST_01";
		String folName2 = "/outbox/";
		String fName2 = "GK_RET_01#RETFILEJUNE016";

		String fileName = "PODXML.txt";
		String qMngr = "LPQMA618M";
		String qName = "SUBMITSECUREFILEFLOW.INPUT";
		String hostName = "10.66.2.102"; 
		int port = 1414;
		String channel = "SYSTEM.DEF.SVRCONN";

		LinuxBoxFileTransfer lbft = null;
		try {
			lbft = new LinuxBoxFileTransfer();
			boolean bootStatus = lbft.bootLinuxBox(unixServerUrl, 446, unixId, null, unixPwd);
			if(!bootStatus) throw new Exception("Unable to Boot linux box");

			if(! lbft.sendCommand("ftp -inv "+ftpServer).contains("Connected to "+ftpServer)) 
				throw new Exception("Unable to connect to "+ftpServer);
			if(! lbft.sendCommand("user "+uName1+" "+FrameworkConstants.DefaultSFTPWD).contains("230 User logged in")) 
				throw new Exception("Unable to login");
			lbft.sendCommand("bin");
			lbft.sendCommand("cd "+folName1);
			if(! lbft.sendCommand("get "+fName1).contains("226 Transfer complete")) 
				throw new Exception("Unable to Transfer");
			lbft.sendCommand("bye");
			lbft.downloadFromBoxToLocal(fName1, FrameworkConstants.DownloadDirectory+fName1);

			mqc.push(qMngr, qName, hostName, port, channel, fileName);

			if(! lbft.sendCommand("ftp -inv "+ftpServer).contains("Connected to "+ftpServer)) 
				throw new Exception("Unable to connect to "+ftpServer);
			if(! lbft.sendCommand("user "+uName2+" "+FrameworkConstants.DefaultSFTPWD).contains("230 User logged in")) 
				throw new Exception("Unable to login");
			lbft.sendCommand("bin");
			lbft.sendCommand("cd "+folName2);
			if(! lbft.sendCommand("get "+fName2).contains("226 Transfer complete")) 
				throw new Exception("Unable to Transfer");
			lbft.sendCommand("bye");
			lbft.downloadFromBoxToLocal(fName2, FrameworkConstants.DownloadDirectory+fName2);


			FileComparision fc =new FileComparision(LoggerFactory.getLogger(SCPClient.class));
			boolean contentVervalue = false;
			contentVervalue= fc.contentVerification(FrameworkConstants.DownloadDirectory+"/"+fName1,FrameworkConstants.DownloadDirectory+"/"+fName2);
			System.out.println("contentVerificationvalue :"+contentVervalue);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(lbft!=null) lbft.destroy();
		}*/
