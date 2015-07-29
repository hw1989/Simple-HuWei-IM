package com.hwant.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	public static String getName(String str) {
		SimpleDateFormat format = new SimpleDateFormat(str);
		return format.format(new Date());
	}
}
