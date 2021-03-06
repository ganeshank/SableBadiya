package com.sb.integration.service.impl;

import java.security.MessageDigest;

import com.sb.integration.service.SecurityService;

public class SecurityServiceImpl implements SecurityService {

	public String getEncryptedPassword(String password) {
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update(password.getBytes());
	        
	        byte byteData[] = md.digest();
	 
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	     
	        System.out.println("Hex format : " + sb.toString());
	        
	        return sb.toString();
		}catch(Exception ex){
			System.out.println("Password encryption is done properly...."+ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
}
