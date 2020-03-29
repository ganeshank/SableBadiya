package com.sb.integration.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class TimeStamp {
	final static Logger logger = Logger.getLogger(TimeStamp.class);
	
	public static String getAsiaTimeStamp(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date toPrint = new Date();
		format.setTimeZone(TimeZone.getTimeZone("IST"));
		
		String dateStr = format.format(toPrint);
	    logger.debug("date time:::"+dateStr.toString());
		return dateStr;
	}
	
	public static String getAsiaOnlyTime(){
		SimpleDateFormat format = new SimpleDateFormat("HH a");
		Date toPrint = new Date();
		format.setTimeZone(TimeZone.getTimeZone("IST"));
		
		String dateStr = format.format(toPrint);
	    logger.debug("getAsiaOnlyTime time:::"+dateStr.toString());
		return dateStr;
	}
	
	public static Timestamp getTimeStampForString(String dateStr){
		try{
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		    Date parsedDate = dateFormat.parse(dateStr);
		    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		    return timestamp;
		}catch(Exception e){//this generic but you can control another types of exception
		 e.printStackTrace();
		}
		return null;
	}
	
	public static String getAsiaDay(){
		SimpleDateFormat format = new SimpleDateFormat("EEE");
		Date toPrint = new Date();
		format.setTimeZone(TimeZone.getTimeZone("IST"));
		
		String dateStr = format.format(toPrint);
	    logger.debug("getAsiaMonth :::"+dateStr.toString());
		return dateStr;
	}
	
	public static String getTodaysDate(){
		SimpleDateFormat sdf =  new SimpleDateFormat("EEE, d MMM yyyy");
		Date today = new Date();
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		
		return sdf.format(today);
	}
	
	public static String getTomorrowsDate(){
		SimpleDateFormat sdf =  new SimpleDateFormat("EEE, d MMM yyyy");
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		
		return sdf.format(tomorrow);
	}
	
	public static String getAddupDate(int increaseDate){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, increaseDate);  // number of days to add
		return sdf.format(c.getTime());
	}
	
	public static Timestamp convertStringToTimestamp(String str_date) {
	    try {
	      DateFormat formatter;
	      formatter = new SimpleDateFormat("EEE, d MMM yyyy");
	       // you can change format of date
	      Date date = formatter.parse(str_date);
	      java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());

	      return timeStampDate;
	    } catch (ParseException e) {
	      System.out.println("Exception :" + e);
	      return null;
	    }
	  }
}
