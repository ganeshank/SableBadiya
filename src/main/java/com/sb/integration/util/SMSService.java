package com.sb.integration.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

@SuppressWarnings("deprecation")
public class SMSService {

	final static Logger logger = Logger.getLogger(SMSService.class);
	
	@SuppressWarnings("resource")
	public void sendMessage(String[] number, String message)throws Exception{
		HttpClient client = null;
		Properties prop = null;
		InputStream is = null;
		try{
			  logger.debug("sendMessage method has started.");
			  prop = new Properties();
			  is = this.getClass().getResourceAsStream("/sms.properties");
			  prop.load(is);
			  
			  String mobileNumbers = null;
			  for (String string : number) {
				if(mobileNumbers==null)
					mobileNumbers = string;
				else
					mobileNumbers = mobileNumbers + "," + string;
			  }
			  message = message.replace(" ", "+");
			  
			  String restApiUrl = "http://sms.hspsms.com/sendSMS?";
			  
			  restApiUrl = restApiUrl + "username=" + prop.getProperty("username");
			  restApiUrl = restApiUrl + "&message=" + message;
			  restApiUrl = restApiUrl + "&sendername=" + prop.getProperty("sendername");
			  restApiUrl = restApiUrl + "&smstype=" + prop.getProperty("smstype");
			  restApiUrl = restApiUrl + "&numbers=" + mobileNumbers;
			  restApiUrl = restApiUrl + "&apikey=" + prop.getProperty("apikey");
			  
			  System.out.println(restApiUrl);
			  logger.debug("restApiUrl---"+ restApiUrl);
			
			  client = new DefaultHttpClient();
			  HttpGet request = new HttpGet(restApiUrl);
			  
			  HttpResponse response = client.execute(request);
			  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
			  String line = "";
			  while ((line = rd.readLine()) != null) {
			    System.out.println(line);
			    logger.debug("LINE---"+ line);
			  }
			  logger.debug("Message sent successfully.");
			  
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception();
		}finally {
			if(prop!=null)
				prop.clone();
			if(is!=null)
				is.close();
		}
	}
}
