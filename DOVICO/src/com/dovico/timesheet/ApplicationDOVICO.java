package com.dovico.timesheet;

import android.app.Application;
import android.os.Build;

import com.dovico.timesheet.db.DbManager;
import com.dovico.timesheet.db.DbManagerImpl;
import com.dovico.timesheet.rest.methods.GetEmployeeInfo;
import com.dovico.timesheet.utils.Logger;
import com.dovico.timesheet.utils.SharedPrefsUtil;

public class ApplicationDOVICO extends Application {


	public static final String TAG = "Dovico";


	private static ApplicationDOVICO instance;
	private DbManager dbManager;

	public static ApplicationDOVICO getInstance() {
		
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		
		dbManager = DbManagerImpl.getInstance(getApplicationContext());
		dbManager.deleteAllAssignments();
		dbManager.deleteAllClients();
		dbManager.deleteAllProjects();
		dbManager.deleteAllTasks();
		dbManager.deleteAllTimeEntries();
	
		
		// We retrieve employeeInfo if we haven't already
		int employeeID = SharedPrefsUtil.getIntFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.EMPLOYEE_ID);
		
		if (employeeID == SharedPrefsUtil.SHARED_PREFS_INTEGER_DEFAULT_VALUE) {
			String userToken = SharedPrefsUtil.getStringFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.USER_TOKEN);
			GetEmployeeInfo getEmployeeInfo = new GetEmployeeInfo(getApplicationContext(), userToken, "1");
			getEmployeeInfo.execute();
			employeeID = SharedPrefsUtil.getIntFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.EMPLOYEE_ID);
		}
	
		Logger.d(TAG, "employeeID: " + employeeID);
		
		SharedPrefsUtil.putIntToSharedPrefs(getApplicationContext(), SharedPrefsUtil.SDK_VERSION, Build.VERSION.SDK_INT);
		
	}
}