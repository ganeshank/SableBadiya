package com.sb.integration.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
 
public class SendOtp {
	public String sendSms(String messageStr, String number) {
		Properties prop = null;
		InputStream is = null;
		
		try {
			prop = new Properties();
			is = this.getClass().getResourceAsStream("/sms.properties");
			prop.load(is);
			  
			// Construct data
			String user = "username=" + prop.getProperty("otp.username");
			String hash = "&hash=" + prop.getProperty("otp.hash");
			String message = "&message=" + messageStr;
			String sender = "";
			if(prop.getProperty("sender.required").equals("true")){
				sender = "&sender=" + prop.getProperty("otp.sender");
			}
			
			String numbers = "&numbers=" + number;
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = user + hash + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			
			return stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
			return "Error "+e;
		}
	}
}
