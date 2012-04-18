package com.dovico.timesheet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.dovico.timesheet.db.DbManager;
import com.dovico.timesheet.db.DbManagerImpl;
import com.dovico.timesheet.rest.methods.GetEmployeeInfo;
import com.dovico.timesheet.utils.Logger;
import com.dovico.timesheet.utils.SharedPrefsUtil;

public class ActivitySetup extends Activity {
	
	private static final String TAG = "Setup";
	private int currentEntryTimeMode;
	private int currentHideClientNameValue;
	private String userToken;
	private DbManager dbManager;
	
	private ProgressDialog getEmployeeDialog;
	
	private static final int DIALOG_SAVING_SETTINGS = 0;
	
	private class GetEmployeeAsync extends AsyncTask {

		
		String userToken;

		public GetEmployeeAsync(String userToken) {
			this.userToken = userToken;
		}

		@Override
		protected Object doInBackground(Object... params) {
			
			GetEmployeeInfo getEmployeeInfo = new GetEmployeeInfo(getApplicationContext(), userToken, "1");
			getEmployeeInfo.execute();
			Logger.d(TAG, "employeeID: " + SharedPrefsUtil.getIntFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.EMPLOYEE_ID));
            
            return null;
		}

		@Override
		protected void onPreExecute() {
			showDialog(DIALOG_SAVING_SETTINGS);
		}

		@Override
		protected void onPostExecute(Object result) {
			if (getEmployeeDialog.isShowing()) {
				dismissDialog(DIALOG_SAVING_SETTINGS);
			}
			
			SharedPrefsUtil.putStringToSharedPrefs(getApplicationContext(), SharedPrefsUtil.USER_TOKEN, userToken);		
			SharedPrefsUtil.putIntToSharedPrefs(getApplicationContext(), SharedPrefsUtil.TIME_ENTRY_MODE, currentEntryTimeMode);
			SharedPrefsUtil.putIntToSharedPrefs(getApplicationContext(), SharedPrefsUtil.HIDE_CLIENT_NAME, currentHideClientNameValue);
			finish();
			
		}

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		dbManager = DbManagerImpl.getInstance(getApplicationContext());
		
		userToken = SharedPrefsUtil.getStringFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.USER_TOKEN);
		
		((EditText)findViewById(R.id.edittext_access)).setText(userToken);
//		((EditText)findViewById(R.id.edittext_access)).setText("0ce8d2d46ab9434b8737faeae86ba14e.2019");		
		
		Spinner minutesDecimalSpinner = (Spinner)findViewById(R.id.minutes_decimal_spinner);
		minutesDecimalSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				currentEntryTimeMode = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		Spinner hideClientNameSpinner = (Spinner)findViewById(R.id.hide_client_name_spinner);
		hideClientNameSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				currentHideClientNameValue = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		currentEntryTimeMode = SharedPrefsUtil.getIntFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.TIME_ENTRY_MODE);
		if (currentEntryTimeMode == SharedPrefsUtil.SHARED_PREFS_INTEGER_DEFAULT_VALUE) {
			currentEntryTimeMode = 0;
		}
		minutesDecimalSpinner.setSelection(currentEntryTimeMode);
		
		currentHideClientNameValue = SharedPrefsUtil.getIntFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.HIDE_CLIENT_NAME);
		if (currentHideClientNameValue == SharedPrefsUtil.SHARED_PREFS_INTEGER_DEFAULT_VALUE) {
			currentHideClientNameValue = 0;
		}
		hideClientNameSpinner.setSelection(currentHideClientNameValue);	
		
	}
	
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = null;

		switch (id) {
		case DIALOG_SAVING_SETTINGS:

			getEmployeeDialog = new ProgressDialog(
					ActivitySetup.this);
			getEmployeeDialog.setMessage("Saving your settings");
			return getEmployeeDialog;
		}

		return dialog;
	}
	public void onClickTimeEntry(View v) {
		Intent intent = new Intent(ActivitySetup.this, ActivityMain.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickTimesheet(View v) {
		Intent intent = new Intent(ActivitySetup.this, ActivityTimesheet.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickSetup(View v) {
//		Intent intent = new Intent(ActivitySetup.this, ActivitySetup.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
	}
	public void onClickAbout(View v) {
		Intent intent = new Intent(ActivitySetup.this, ActivityAbout.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void onClickApply(View v){
		String newUserToken = ((EditText)findViewById(R.id.edittext_access)).getText().toString();
		newUserToken = newUserToken.replaceAll(System.getProperty("line.separator"), "");
		newUserToken = newUserToken.replaceAll(" ", "");
		
		
		if (!newUserToken.equals(userToken)) {
			dbManager.deleteAllAssignments();
			dbManager.deleteAllClients();
			dbManager.deleteAllProjects();
			dbManager.deleteAllTasks();
			dbManager.deleteAllTimeEntries();		
			
			new GetEmployeeAsync(newUserToken).execute(null);
			
		} else {
		
		SharedPrefsUtil.putStringToSharedPrefs(getApplicationContext(), SharedPrefsUtil.USER_TOKEN, newUserToken);		
		SharedPrefsUtil.putIntToSharedPrefs(getApplicationContext(), SharedPrefsUtil.TIME_ENTRY_MODE, currentEntryTimeMode);
		SharedPrefsUtil.putIntToSharedPrefs(getApplicationContext(), SharedPrefsUtil.HIDE_CLIENT_NAME, currentHideClientNameValue);
		finish();
		}
		
	}

	
}
