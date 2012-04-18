package com.dovico.timesheet.widgets.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dovico.timesheet.ActivityTimesheet;
import com.dovico.timesheet.R;
import com.dovico.timesheet.db.DbManager;
import com.dovico.timesheet.db.DbManagerImpl;
import com.dovico.timesheet.rest.methods.DeleteTimeEntry;
import com.dovico.timesheet.utils.Logger;
import com.dovico.timesheet.utils.SharedPrefsUtil;

public class DOVICOTimesheetListViewCursorAdapter extends SimpleCursorAdapter{

	private static final String TAG = "DOVICOTimesheetListViewCursorAdapter";
	private Activity context;
	private DbManager dbManager; 
	private int layout;
	private LayoutInflater inflater;
	
	private class DeleteButtoninfo {
		
		public DeleteButtoninfo(long timeEntryLocalID, String timeEntryID) {
			this.timeEntryLocalID = timeEntryLocalID;
			this.timeEntryID = timeEntryID;
		}
		
		private long timeEntryLocalID;
		private String timeEntryID;
		
		public long getTimeEntryLocalID() {
			return timeEntryLocalID;
		}
		public void setTimeEntryLocalID(long timeEntryLocalID) {
			this.timeEntryLocalID = timeEntryLocalID;
		}
		public String getTimeEntryID() {
			return timeEntryID;
		}
		public void setTimeEntryID(String timeEntryID) {
			this.timeEntryID = timeEntryID;
		}
	}
	
private class DeleteTimeEntryAsync extends AsyncTask {

	
		long timeEntryLocalID;
		String timeEntryID;
		String userToken;

		public DeleteTimeEntryAsync(String timeEntryID, String userToken, long timeEntryLocalID) {
			this.userToken = userToken;
			this.timeEntryID = timeEntryID;
			this.timeEntryLocalID = timeEntryLocalID;
		}

		@Override
		protected Object doInBackground(Object... params) {
			DeleteTimeEntry getTimeEntries = new DeleteTimeEntry(context, timeEntryID, userToken, "1");
			getTimeEntries.execute();	
			
			dbManager.deleteTimeEntry(timeEntryLocalID);
			context.runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					((ActivityTimesheet)context).refreshAdapter();
				}
			  });
			
			return null;
			}

		@Override
		protected void onPreExecute() {
			context.showDialog(ActivityTimesheet.DIALOG_DELETING_TIME_ENTRY);
		}

		@Override
		protected void onPostExecute(Object result) {
			if ( ((ActivityTimesheet)context).deleteTimeEntriesDialog.isShowing()) {
				context.dismissDialog(ActivityTimesheet.DIALOG_DELETING_TIME_ENTRY);
			}
		}

	}
	
	public DOVICOTimesheetListViewCursorAdapter(Activity context, int layout, Cursor c,
			String[] from, int[] to) {		
		
		super(context, layout, c, from, to);
		this.context = context;
		this.dbManager = DbManagerImpl.getInstance(context);
		this.layout = layout;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	    @Override
	    public int getCount() {
	        return super.getCount();
	    }
	    
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	if (convertView == null) {
	    		 convertView = inflater.inflate(
	 	        		layout, parent, false);
	    	}
	    	final Cursor c = getCursor();
	    	c.moveToPosition(position);
	       
	        ((TextView)convertView.findViewById(R.id.project_name)).setText(c.getString(7));
	        ((TextView)convertView.findViewById(R.id.project_date)).setText(c.getString(1));
	        ((TextView)convertView.findViewById(R.id.project_task)).setText(c.getString(8));
	        ((TextView)convertView.findViewById(R.id.project_total_hours)).setText(c.getString(5));
	        
	        final String timeEntryID = c.getString(10);
	        String status = c.getString(9);
	        
	        Button deleteTimeEntry = ((Button)convertView.findViewById(R.id.button1));
	        if (status.equals("A") || status.equals("U")) {	        	
	        	deleteTimeEntry.setVisibility(View.INVISIBLE);
	        	deleteTimeEntry.setClickable(false);
	        } else {
	        	Logger.d(TAG, "Button setting, timeEntryLocalId: " + c.getInt(0) + ", timeEntryId: " + timeEntryID);
	        	
	        	// In the event the Status was A or U previously, we need to make sure the button is visible and clickable again
	        	deleteTimeEntry.setVisibility(View.VISIBLE);
	        	deleteTimeEntry.setClickable(true);
	        	
	        	deleteTimeEntry.setTag(new DeleteButtoninfo(c.getInt(0), timeEntryID));
	        	deleteTimeEntry.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						DeleteButtoninfo deleteButtonInfo = (DeleteButtoninfo) v.getTag();
						Logger.d(TAG, "Button setting, actual deletion: timeEntryID: " + deleteButtonInfo.getTimeEntryID() + ", timeEntryLocalID: " + deleteButtonInfo.getTimeEntryLocalID());
						new DeleteTimeEntryAsync(deleteButtonInfo.getTimeEntryID(), SharedPrefsUtil.getStringFromSharedPrefs(context, SharedPrefsUtil.USER_TOKEN), deleteButtonInfo.getTimeEntryLocalID()).execute(null);
					}
				});
	        }
	        
	        return convertView;
	    }
	    

}
