package com.kimbrelk.da.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Util {
	
	public final static Date stringToDate(String str) {
		try {
			final DateFormat format = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss-SSSS");
			return format.parse(str);
		}
		catch (ParseException e) {
			return null;
		}
	}
}