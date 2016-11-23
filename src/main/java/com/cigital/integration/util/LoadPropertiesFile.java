package com.cigital.integration.util;

import java.io.InputStream;
import java.util.Properties;

public class LoadPropertiesFile {
	public Properties loadProperties(String fileName)throws Exception{
		Properties prop = null;
		InputStream is = null;
        try {
            prop = new Properties();
            is = this.getClass().getResourceAsStream(fileName);
            prop.load(is);
            
            return prop;
        }catch(Exception e){
        	e.printStackTrace();
        	throw new Exception("file is not loaded successfully.");
        }finally{
        	if(is!=null){
        		is.close();
        	}
        }
        
	}
}
