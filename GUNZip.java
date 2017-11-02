/*Description: This program is used to unzip a ziped file.
*/

package amex.fs.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GUNZip
{
	
	public static void main(String args[]) throws IOException
	{
		File dwnzipfile = new File(FrameworkConstants.DownloadDirectory+"G33CompSFTDecompWinzip_fileP33GKHA13005QA.gz");
		
		File outdir=new File(FrameworkConstants.DownloadDirectory);
		//outdir.mkdir();
		File extfile=new File(outdir.getName());
		GUNZip gzip=new GUNZip();
		
		File extractedfile=gzip.unGzip(dwnzipfile, false);
	}
	
	public static File unGzip(File infile, boolean deleteGzipfileOnSuccess) throws IOException {
	    GZIPInputStream gin = new GZIPInputStream(new FileInputStream(infile));
	    FileOutputStream fos = null;
	    try {
	        File outFile = new File(infile.getParent(), infile.getName().replaceAll("\\.gz$", ""));
	        fos = new FileOutputStream(outFile);
	        byte[] buf = new byte[100000];
	        int len;
	        while ((len = gin.read(buf)) > 0) {
	            fos.write(buf, 0, len);
	        }

	        fos.close();
	        if (deleteGzipfileOnSuccess) {
	            infile.delete();
	        }
	        return outFile; 
	    } finally {
	        if (gin != null) {
	            gin.close();    
	        }
	        if (fos != null) {
	            fos.close();    
	        }
	    }       
	}

	 
}