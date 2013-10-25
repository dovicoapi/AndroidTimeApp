package com.dovico.timesheet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedPrefsUtil {

	private static final String TAG = "SharedPrefsUtil";
	
	private static final String SHARED_PREFS_NAME = "DOVICO";
	
	public static final boolean SHARED_PREFS_BOOLEAN_DEFAULT_VALUE = false;
	public static final int SHARED_PREFS_INTEGER_DEFAULT_VALUE = -1;
	public static final String SHARED_PREFS_STRING_DEFAULT_VALUE = "";
	
	// SharedPrefs content
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String USER_TOKEN = "userToken";
	public static final String COMPANY_NAME = "companyName";
	public static final String USER_NAME = "userName";
	public static final String HIDE_CLIENT_NAME = "hideClientName";
	public static final String TIME_ENTRY_MODE = "timeEntryMode";
	public static final String EMPLOYEE_ID = "employeeID";
	public static final String SDK_VERSION = "sdkVersion";
	
	private static Editor getSharedPreferenceEditor(Context context) {
		Editor editor = context.getSharedPreferences(SHARED_PREFS_NAME,
				Context.MODE_PRIVATE).edit();
		return editor;
	}
	
	private static SharedPreferences getSharedPrefences(Context context) {
		SharedPreferences sharedPrefs = context.getSharedPreferences(
				SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		return sharedPrefs;
	}

	public static void putBooleanToSharedPrefs(Context context, String tag,
			boolean value) {
		Editor editor = getSharedPreferenceEditor(context);
		editor.putBoolean(tag, value);
		editor.commit();

		Log.d(TAG, "Put in shared prefs  tag: " + tag + ", value: "
				+ value);

	}



	public static boolean getBooleanFromSharedPrefs(Context context, String tag) {
				
		SharedPreferences sharedPrefs = getSharedPrefences(context);
		boolean valueFromSharedPrefs = sharedPrefs.getBoolean(tag,
				SHARED_PREFS_BOOLEAN_DEFAULT_VALUE);
		
		Log.d(TAG, "Retrieved from shared prefs  tag: " + tag + ", value: "
				+ valueFromSharedPrefs);
		
		return valueFromSharedPrefs;
	}


	
	
	public static void putIntToSharedPrefs(Context context, String tag,
			int value) {
		Editor editor = getSharedPreferenceEditor(context);
		editor.putInt(tag, value);
		editor.commit();

		Log.d(TAG, "Put in shared prefs  tag: " + tag + ", value: "
				+ value);

	}

	public static int getIntFromSharedPrefs(Context context, String tag) {
				
		SharedPreferences sharedPrefs = getSharedPrefences(context);
		int valueFromSharedPrefs = sharedPrefs.getInt(tag,
				SHARED_PREFS_INTEGER_DEFAULT_VALUE);
		
		Log.d(TAG, "Retrieved from shared prefs  tag: " + tag + ", value: "
				+ valueFromSharedPrefs);
		
		return valueFromSharedPrefs;
	}

	public static String getStringFromSharedPrefs(Context context, String tag) {
		SharedPreferences sharedPrefs = getSharedPrefences(context);
		String valueFromSharedPrefs = sharedPrefs.getString(tag,
				SHARED_PREFS_STRING_DEFAULT_VALUE);
		
		Log.d(TAG, "Retrieved from shared prefs  tag: " + tag + ", value: "
				+ valueFromSharedPrefs);
		
		return valueFromSharedPrefs;
	}
	
	public static void putStringToSharedPrefs(Context context, String tag,
			String value) {
		Editor editor = getSharedPreferenceEditor(context);
		editor.putString(tag, value);
		editor.commit();

		Log.d(TAG, "Put in shared prefs  tag: " + tag + ", value: "
				+ value);

	}
	
	
}
