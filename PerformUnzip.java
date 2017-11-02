package amex.fs.commons;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.LoggerFactory;

public class PerformUnzip
{
    List<String> fileList;
   // private static final String INPUT_ZIP_FILE = "C:\\Users\\AS5026013\\Desktop\\FILES\\Automation_testdata\\Zippedfile.zip";
    //private static final String OUTPUT_FOLDER = "C:\\Users\\AS5026013\\Desktop\\FILES\\Automation_testdata\\Outputfile\\";

    public static org.slf4j.Logger logger = LoggerFactory.getLogger(PerformUnzip.class);
    
    public PerformUnzip(org.slf4j.Logger logger )
	{
		this.logger=logger;
	}

	public static void main( String[] args )
    {
    	PerformUnzip unZip = new PerformUnzip(logger);
    	//unZip.unZipIt(INPUT_ZIP_FILE,OUTPUT_FOLDER);
    }

    /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     * @throws IOException 
     */
    public boolean unZipIt(String zipFile, String outputFolder) throws IOException{
    	boolean b=false;

     byte[] buffer = new byte[4096];

    
System.out.println("the file to be unzipped is "+zipFile);
    	//get the zip file content
try{

	//create output directory is not exists
	File folder = new File(outputFolder);
	if(!folder.exists()){
		folder.mkdir();
	}

	//get the zip file content
	ZipInputStream zis =
		new ZipInputStream(new FileInputStream(zipFile));
	//get the zipped file list entry
	ZipEntry ze = zis.getNextEntry();
	//long s= ze.getCompressedSize();
	//System.out.println("the size is "+s);
System.out.println("ze : "+ze);
	while(ze!=null){


		
       System.out.println("the value is not null;");
	   String fileName = ze.getName();
       File newFile = new File(outputFolder + File.separator + fileName+"unzipped");

       System.out.println("file unzip : "+ newFile.getAbsoluteFile());

        //create all non exists folders
        //else you will hit FileNotFoundException for compressed folder
        new File(newFile.getParent()).mkdirs();

        FileOutputStream fos = new FileOutputStream(newFile);

        int len;
        while ((len = zis.read(buffer)) > 0) {
   		fos.write(buffer, 0, len);
        }

        fos.close();
        ze = zis.getNextEntry();
        b=true;
	}

    zis.closeEntry();
	zis.close();
	

	System.out.println("Done");

}catch(IOException ex){
   ex.printStackTrace();
}
   return b;  
   }
}
