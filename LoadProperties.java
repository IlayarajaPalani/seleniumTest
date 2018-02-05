/*Description : This module loads the properties from SFT.properties
*/

package com.amex.tp.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {
	public Properties prop = new Properties();
	public InputStream input = null;
	
	public LoadProperties(String filename)
	{
		try {
 
			input = new FileInputStream(filename);

			// load a properties file
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	  
	}
	
	
  public String readProperty(String property)
  {
	  String value="";
	  value=prop.getProperty(property);
	  return value;
  }
  
	

}