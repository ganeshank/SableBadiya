package com.cigital.integration.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringGeneration {
	public static String getRandomString(int length){
	    boolean useLetters = true;
	    boolean useNumbers = false;
	    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
	 
	    System.out.println(generatedString);
	    return generatedString;
	}
	
	public static String generateOrderNumber(){
		SimpleDateFormat dsf =  new SimpleDateFormat("ddMMyyHHmmss");
		Date date = new Date();
		String s = dsf.format(date);
		return "ORD"+s;
	}
}
