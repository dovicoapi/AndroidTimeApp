package com.dovico.timesheet.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dovico.timesheet.beans.TimeEntry;
import com.dovico.timesheet.db.Db;
import com.dovico.timesheet.utils.Logger;

/**
 * @author milos.pesic
 * 
 */
public class TimeEntryDao extends GenericDao {

	private static final String TAG = "TimeEntryDao";

	private Context context;

	/**
	 * UserDao constructor
	 * 
	 * @param context
	 *            The application context
	 * @param db
	 *            database
	 */
	public TimeEntryDao(Context context, SQLiteDatabase db) {
		super(db);
		this.context = context;
		tableName = Db.TABLE_TIME_ENTRIES;
		idName = Db.TimeEntries.ID;
	}


	public List<TimeEntry> getAllTimeEntries() {
		Log.d(TAG, "getAllTimeEntries called");
		List<TimeEntry> timeEntries = null;
		Cursor cursor = null;
		try {
			cursor = getTimeEntriesCursor();
			if (cursor != null) {
				timeEntries = new ArrayList<TimeEntry>();
				Log.d(TAG,
						"number of timeEntrie in database; "
								+ cursor.getCount());
				if (cursor.moveToFirst()) {
					do {
						timeEntries
								.add(getTimeEntryFromCursor(cursor));
					} while (cursor.moveToNext());
				}
			} else {
				Log.d(TAG, "cursor is null");
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		Log.d(TAG, "getAllTimeEntries ends");
		return timeEntries;
	}
	
	public TimeEntry getTimeEntryFromCursor(Cursor cursor) {
		Log.d(TAG, "getTimeEntryFromCursor called");
		//Db.TimeEntries.ID, Db.TimeEntries.DATE, Db.TimeEntries.EMPLOYEE_ID, Db.TimeEntries.PROJECT_ID, Db.TimeEntries.TASK_ID, Db.TimeEntries.TOTAL_HOURS, 
		//Db.TimeEntries.CLIENT_ID, Db.TimeEntries.PROJECT_NAME, Db.TimeEntries.TASK_NAME, Db.TimeEntries.STATUS, Db.TimeEntries.GLOBAL_TIME_ENTRY_ID
		TimeEntry timeEntry = new TimeEntry();

		timeEntry.setId(cursor.getString(10));
		timeEntry.setDate(cursor.getString(1));
		timeEntry.setEmployeeID(cursor.getInt(2));
		timeEntry.setProjectID(cursor.getInt(3));
		timeEntry.setTaskID(cursor.getInt(4));
		timeEntry.setTotalHours(cursor.getDouble(5));
		timeEntry.setClientID(cursor.getInt(6));
		timeEntry.setProjectName(cursor.getString(7));
		timeEntry.setTaskName(cursor.getString(8));
		timeEntry.setStatus(cursor.getString(9));

		Log.d(TAG, "getAssignmentFromCursor ends");
		return timeEntry;
	}

	public long insertNewTimeEntry(TimeEntry timeEntry) {
		Log.d(TAG, "insertNewTimeEntry called");

//		Logger.d(TAG, "task globalID: " + timeEntry.get);
//		Logger.d(TAG, "taskName: " + task.getName());
//		Logger.d(TAG, "taskDesc: " + task.getDescription());
//		Logger.d(TAG, "task projectId: " + task.getProjectID());
//		Logger.d(TAG, "getContact: " + client.getContact());
		
		ContentValues contentValues = new ContentValues(6);
		
		contentValues.put(Db.TimeEntries.DATE, timeEntry.getDate());
		contentValues.put(Db.TimeEntries.EMPLOYEE_ID, timeEntry.getEmployeeID());
		contentValues.put(Db.TimeEntries.PROJECT_ID, timeEntry.getProjectID());
		contentValues.put(Db.TimeEntries.TASK_ID, timeEntry.getTaskID());
		contentValues.put(Db.TimeEntries.TOTAL_HOURS, timeEntry.getTotalHours());
		contentValues.put(Db.TimeEntries.CLIENT_ID, timeEntry.getClientID());
		contentValues.put(Db.TimeEntries.PROJECT_NAME, timeEntry.getProjectName());
		contentValues.put(Db.TimeEntries.TASK_NAME, timeEntry.getTaskName());
		contentValues.put(Db.TimeEntries.STATUS, timeEntry.getStatus());
		contentValues.put(Db.TimeEntries.GLOBAL_TIME_ENTRY_ID, timeEntry.getId());

		// StringBuffer strBuf = new StringBuffer();
		// for (String currString : tags) {
		// strBuf.append(currString + "||");
		// }
		// strBuf.delete(strBuf.length() - 2, strBuf.length());
		//
//		contentValues.put(Db.Clients.CONTACT, client.getContact());

		long returnValue = db.insert(tableName, null, contentValues);
		Log.d(TAG, "insertNewTimeEntry ends, returns " + returnValue);

		return returnValue;
	}

	public int deleteTimeEntry(long timeEntryID) {
		Logger.d(TAG, "deleteTimeEntry called, deleting time entry with ID: " + timeEntryID);
		String[] whereArgs = { String.valueOf(timeEntryID) };
		int returnValue = db.delete(tableName, Db.TimeEntries.ID + "=?", whereArgs);
		Logger.d(TAG, "deleteTimeEntry ends, returns: " + returnValue);
		return returnValue;
	}


	public Cursor getTimeEntriesCursor() {
//		String[] selectionArgs = { Integer.toString(timeEntryID) };
		String[] columns = { Db.TimeEntries.ID, Db.TimeEntries.DATE, Db.TimeEntries.EMPLOYEE_ID, Db.TimeEntries.PROJECT_ID, Db.TimeEntries.TASK_ID, Db.TimeEntries.TOTAL_HOURS, 
				Db.TimeEntries.CLIENT_ID, Db.TimeEntries.PROJECT_NAME, Db.TimeEntries.TASK_NAME, Db.TimeEntries.STATUS, Db.TimeEntries.GLOBAL_TIME_ENTRY_ID};
		Cursor cursor = db.query(tableName, columns,
				null, null, null,
				null, null);
		return cursor;
	}


	public int deleteAllTimeEntries() {
		Logger.d(TAG, "deleteAllTimeEntries called");

		int result = db.delete(tableName, null, null);

		Logger.d(TAG, "deleteAllTimeEntries ends, returns: " + result);

		return result;
	}


}
