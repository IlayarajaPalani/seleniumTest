/*Description: This module is having constant values used in the framework
*/
package amex.fs.commons;

public class FrameworkConstants {
	
	public static String HTTPS_Props = "./properties/SFT_HTTPS.properties";
	public static String SFT = "./properties/SFT.properties";
	public static int ThreadCount=2;
	public static String RunLog="RunLog.log";
	public static String ExecutionSheet="./test-data/ExecutionSheet.xls";
	public static String ExecutionSheet_HTTPS="./test-data/ExecutionSheet_HTTPS.xls";
	public static String HTTPSinbox="inbox";
	public static String Platformchk="POD";
    public static String TCLookup="./test-data/TCLookup.txt";
	public static String DownloadDirectory="./downloads/";
	public static String RemoteOutbox="/outbox";
	public static String RemoteSent="/sent";
	public static int SleepValue=40000;
	public static String Pack_https = "amex.fs.sft.https."; 
	public static String Pack = "amex.fs.sft.";
	public static String AcceptHostKey = "YES"; 
	public static String KnownHostRepositoryPath="./known_host_file/Known_Host_SFT";
	public static String Key = "./keys/id_rsa";
    public static int uploadcount=5;
    public static String ScreenShots="./screenshots";
    public static String RunIdFile="RunId.txt";
    public static String DefaultSFTPWD="amex123";
    public static int Maxloopvalue=1000;
    public static int Minloopvalue=7;
    public static String WinEOL= "CRLF";
    public static String UnixEOL= "LF";
    public static String MacEOL= "CR";
    public static String ExecutionSheet_ASCIIBIN= "./test-data/ExecutionSheet_ASCIIBIN.xls";
    public static String ExecutionSheet_PGP= "./test-data/ExecutionSheet_PGP.xls";
    public static String ExecutionSheet_ASCIIBIN_HTTPS="./test-data/ExecutionSheet_ASCIIBIN_HTTPS.xls";
    public static String ExecutionSheet_ASCIIBIN_OUTBOUND="./test-data/ExecutionSheet_ASCIIBIN_OUTBOUND.xls";
    public static String RETRIEVAL_QMANAGER="LPQMA725";
    public static String RETRIEVAL_QMANAGERNAME="SUBMITSECUREFILEFLOW.SFT.RETRIEVAL.REQUEST";
    public static String RETRIEVAL_HOSTNAME="10.16.157.54";
    public static int RETRIEVAL_PORT=1414;
    public static String RETRIEVAL_CHANNEL="SYSTEM.DEF.SVRCONN";
    public static String ExecutionSheet_RETRIEVAL="./test-data/ExecutionSheet_RETRIEVAL.xls";
    public static String TMProperties="./properties/TM.properties";
    public static final String TMTrackingID="File Tracking ID";
    public static final String TMBaseFileName="Base File Name";
    public static String RetrievalBaseFileName="SFT_SSH_RET";
    public static String RetrievalUserID="SFT_DELUSER";
    
    //public static String HTTPS_Props1="./properties/SFT_HTTPS.properties";

}
