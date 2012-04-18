package com.dovico.timesheet.utils;

import android.util.Log;

public class Logger {
	
	private static final int LOG_LEVEL = Log.DEBUG;
	
	public static void d(String tag, String message) {
		if (LOG_LEVEL <= Log.DEBUG) {
			Log.d(tag, message);
		}
	}
	
	public static void i(String tag, String message) {
		if (LOG_LEVEL <= Log.INFO) {
			Log.i(tag, message);
		}
	}
	
	public static void w(String tag, String message) {
		if (LOG_LEVEL <= Log.WARN) {
			Log.i(tag, message);
		}
	}
	
	public static void e(String tag, String message) {
		if (LOG_LEVEL <= Log.ERROR) {
			Log.i(tag, message);
		}
	}
	

}
