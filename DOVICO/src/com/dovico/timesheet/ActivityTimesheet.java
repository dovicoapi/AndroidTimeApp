package com.dovico.timesheet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.dovico.timesheet.db.Db;
import com.dovico.timesheet.db.DbManager;
import com.dovico.timesheet.db.DbManagerImpl;
import com.dovico.timesheet.rest.methods.GetTimeEntriesForEmployee;
import com.dovico.timesheet.utils.SharedPrefsUtil;
import com.dovico.timesheet.widgets.adapters.DOVICOTimesheetListViewCursorAdapter;

public class ActivityTimesheet extends Activity{
	
	private static final String TAG = "Timesheet";
	
	private ListView timesheetListView;
	private DbManager dbManager;
	private int timeEntryCount;
	
	private static final int DIALOG_GET_TIME_ENTRIES = 0;
	public static final int DIALOG_DELETING_TIME_ENTRY = 1;
	
	private ProgressDialog getTimeEntriesDialog;
	public ProgressDialog deleteTimeEntriesDialog;
	
	private class GetTimeEntries extends AsyncTask {

		
		String employeeID;
		String userToken;

		public GetTimeEntries(String employeeID, String userToken) {
			this.userToken = userToken;
			this.employeeID = employeeID;
		}

		@Override
		protected Object doInBackground(Object... params) {
			GetTimeEntriesForEmployee getTimeEntries = new GetTimeEntriesForEmployee(getApplicationContext(), employeeID, userToken, "1");
			getTimeEntries.execute();
						
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					refreshAdapter();					
				}
			});
			
			
			return null;
			}

		@Override
		protected void onPreExecute() {
			showDialog(DIALOG_GET_TIME_ENTRIES);
		}

		@Override
		protected void onPostExecute(Object result) {
			if (getTimeEntriesDialog.isShowing()) {
				dismissDialog(DIALOG_GET_TIME_ENTRIES);
			}
		}

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timesheet);
		
		dbManager = DbManagerImpl.getInstance(getApplicationContext());
		timeEntryCount = 0;
		timesheetListView = (ListView) findViewById(R.id.listview_timesheet);
		
	}
	
	protected Dialog onCreateDialog(int id) {
    	ProgressDialog dialog = null;
    	switch (id) {
    	case DIALOG_GET_TIME_ENTRIES:

    		getTimeEntriesDialog = new ProgressDialog(
					ActivityTimesheet.this);
    		getTimeEntriesDialog.setMessage("Loading Time Entries");
			return getTimeEntriesDialog;
    	case DIALOG_DELETING_TIME_ENTRY:

    		deleteTimeEntriesDialog = new ProgressDialog(
					ActivityTimesheet.this);
    		deleteTimeEntriesDialog.setMessage("Deleting time entry");
			return deleteTimeEntriesDialog;
    	}
    	return dialog;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		new GetTimeEntries(String.valueOf(SharedPrefsUtil
				.getIntFromSharedPrefs(getApplicationContext(),
						SharedPrefsUtil.EMPLOYEE_ID)),
						SharedPrefsUtil
						.getStringFromSharedPrefs(getApplicationContext(),
								SharedPrefsUtil.USER_TOKEN)).execute((Object[])null);
		
	}

	public void onClickTimeEntry(View v) {
		Intent intent = new Intent(ActivityTimesheet.this, ActivityMain.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickTimesheet(View v) {
//		Intent intent = new Intent(ActivityTimesheet.this, ActivityTimesheet.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
	}
	public void onClickSetup(View v) {
		Intent intent = new Intent(ActivityTimesheet.this, ActivitySetup.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickAbout(View v) {
		Intent intent = new Intent(ActivityTimesheet.this, ActivityAbout.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void refreshAdapter() {
		String[] fields = new String[] { Db.TimeEntries.PROJECT_ID, Db.TimeEntries.TASK_ID,
				Db.TimeEntries.DATE, Db.TimeEntries.TOTAL_HOURS};
		int[] to = new int[] {R.id.project_date, R.id.project_task, R.id.project_date, R.id.project_total_hours};
		DOVICOTimesheetListViewCursorAdapter adapter = new DOVICOTimesheetListViewCursorAdapter(ActivityTimesheet.this,
				R.layout.timesheet_listview_item, dbManager.getTimeEntriesCursor(), fields, to);
		timeEntryCount = adapter.getCursor().getCount();
		if (timeEntryCount == 0) {
			Toast.makeText(getApplicationContext(), "There's no time entries for now",
					Toast.LENGTH_SHORT).show();
		} 
		
		timesheetListView.setAdapter(adapter);
//		timesheetListView.invalidate();
	}
}
