package com.sb.integration.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class A {
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        Date d1 = sdf.parse("2018-01-12 19:33:44");
		System.out.println(sdf1.format(d1));
	}
}
