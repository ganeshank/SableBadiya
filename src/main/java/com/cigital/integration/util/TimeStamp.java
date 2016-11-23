package com.cigital.integration.util;

import java.text.SimpleDateFormat;
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
}
