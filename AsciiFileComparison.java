package amex.fs.commons;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

public class AsciiFileComparison {

	public static org.slf4j.Logger logger = LoggerFactory.getLogger(AsciiFileComparison.class);
	
	public static void main(String[] args) {
		try {
			
			AsciiFileComparison fc =new AsciiFileComparison();
			
			System.out.println( fc.contentVerification("CRLF_Win.txt", "CRLF", "CR_Mac.txt", "CR") );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean contentVerification(String srcFileName, String srcFileType, String destFileName, String destFileType) throws IOException{
		
		/*ClassLoader classLoader = getClass().getClassLoader();
		File srcfile = new File(classLoader.getResource(srcFileName).getFile());
		File destfile = new File(classLoader.getResource(destFileName).getFile());*/
		
		File srcfile = new File(srcFileName);
		File destfile = new File(destFileName);
        logger.info("source file name: "+srcfile);
        logger.info("source file name: "+destfile);
        logger.info("srctype "+srcFileType);
        logger.info("desttype "+destFileType);
		String srcFileContent,destFileContent;

		if(srcfile.exists()&&destfile.exists()){

			if (srcfile.isDirectory() || destfile.isDirectory()) {
				logger.info("Source or Destination File is a directory.  Cannot be validated");
				return false;
			}

			/*if (srcfile.length() != destfile.length()) {
				return false;
			}*/

			if (srcfile.getCanonicalFile().equals(destfile.getCanonicalFile())) {
				logger.info("Source and Destination Files are Same. No Validation performed.");
				return true;
			}

			if(!getFileEolType(srcfile).equalsIgnoreCase(srcFileType)){
				System.out.println("Source file "+srcFileName+" mismatches with specified source file type "+srcFileType);
				logger.info("Source file "+srcFileName+" mismatches with specified source file type "+srcFileType);
				return false;
			}
			else if(!getFileEolType(destfile).equalsIgnoreCase(destFileType)){
				System.out.println("Dest file "+destFileName+" mismatches with specified dest file type "+destFileType);
				logger.info("Dest file "+destFileName+" mismatches with specified dest file type "+destFileType);
				return false;
			}

			srcFileContent = getFileContent(srcfile);
			destFileContent = getFileContent(destfile);

			if(getFileEolCount(srcFileContent,srcFileType) != getFileEolCount(destFileContent,destFileType)){
				System.out.println("No. of EOL mismatches between "+srcFileName+" and "+destFileName);
				logger.info("No. of EOL mismatches between "+srcFileName+" and "+destFileName);
				return false;
			}
			
			boolean contentMatch = false;
			FileReader srcInputStream = null, destInputStream = null;
			try{
				srcInputStream = new FileReader(srcfile);
				destInputStream = new FileReader(destfile);
				contentMatch = IOUtils.contentEqualsIgnoreEOL(srcInputStream, destInputStream);
			}
			finally{
				closeQuietly(srcInputStream);
				closeQuietly(destInputStream);
				
			}
			
			return contentMatch;
			

		}

		return false;
	}

	
	public String getFileEolType(File file) throws IOException{
		if(checkForCR(file)){
			return "CR";
		}
		else if(checkForLF(file)){
			return "LF";
		}
		else if(checkForCRLF(file)){
			return "CRLF";
		}

		return null;
	}

	public double getFileEolCount(String fileContent, String fileType) throws IOException{

		double eolCount=-1;
		switch(fileType)
		{
		case "CR":
			eolCount = fileContent.length() - fileContent.replace("\r", "").length();
			break;
		case "LF":
			eolCount = fileContent.length() - fileContent.replace("\n", "").length();
			break;
		case "CRLF":
			eolCount = fileContent.length() - fileContent.replace("\r\n", "").length();
			eolCount/=2;
			break;
		}

		return eolCount;
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
		finally{
			closeQuietly(inputStream);
		}


		return fileContent;
	}

	public boolean checkForCR(File file) throws IOException{
		FileReader inputStream = null;
		try{
			inputStream = new FileReader(file);

			int c;
			String fileContent = "";
			while ((c = inputStream.read()) != -1) {
				char ch = (char) c;
				fileContent+=ch;
			}

			if(fileContent.indexOf("\r") != -1 && fileContent.indexOf("\n") == -1){
				int count = fileContent.length() - fileContent.replace("\r", "").length();
				System.out.println(count);
				return true;
			}

			return false;
		}
		finally{
			closeQuietly(inputStream);
		}


	}

	public boolean checkForCRLF(File file) throws IOException{
		FileReader inputStream = null;
		try{
			inputStream = new FileReader(file);

			int c;
			String fileContent = "";
			while ((c = inputStream.read()) != -1) {
				char ch = (char) c;
				fileContent+=ch;
			}

			if(fileContent.indexOf("\r\n") != -1){
				int count = fileContent.length() - fileContent.replace("\r\n", "").length();
				System.out.println(count);
				return true;
			}

			return false;
		}
		finally{
			closeQuietly(inputStream);
		}
	}

	public boolean checkForLF(File file) throws IOException{
		FileReader inputStream = null;
		try{

			inputStream = new FileReader(file);

			int c;
			String fileContent = "";
			while ((c = inputStream.read()) != -1) {
				char ch = (char) c;
				fileContent+=ch;
			}

			if(fileContent.indexOf("\n") != -1 && fileContent.indexOf("\r") == -1){
				int count = fileContent.length() - fileContent.replace("\n", "").length();
				System.out.println(count);
				return true;
			}

			return false;
		}
		finally{
			closeQuietly(inputStream);
		}
	}

	public void closeQuietly(FileReader input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (Exception ioe) {
			// ignore
		}
	}
}
