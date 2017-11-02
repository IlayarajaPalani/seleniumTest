/*Description: This program is used to create a zip file
*/

package amex.fs.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.LoggerFactory;

public class CreateZip {
	
	static File filename=null;
	ZipOutputStream zos;
	public static org.slf4j.Logger logger = LoggerFactory.getLogger(CreateZip.class);
	public  void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		System.out.println("Writing '" + fileName + "' to zip file");
		logger.info("Writing '" + fileName + "' to zip file");
		this.zos=zos;
		//File file = new File(fileName);
		FileInputStream fis = new FileInputStream(fileName);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry); 

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
	    	 
	 	
	}
	public void closeZipFile()
	{
		try
		{
		zos.close();
		}catch(Exception e){System.out.println(e);}
		
	}
	
}
