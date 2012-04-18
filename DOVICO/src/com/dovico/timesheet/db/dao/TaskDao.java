package com.dovico.timesheet.db.dao;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dovico.timesheet.beans.Task;
import com.dovico.timesheet.db.Db;
import com.dovico.timesheet.utils.Logger;

/**
 * @author milos.pesic
 * 
 */
public class TaskDao extends GenericDao {

	private static final String TAG = "TaskDao";

	/**
	 * UserDao constructor
	 * 
	 * @param context
	 *            The application context
	 * @param db
	 *            database
	 */
	public TaskDao(Context context, SQLiteDatabase db) {
		super(db);
		tableName = Db.TABLE_TASKS;
		idName = Db.Tasks.ID;
	}
	
	public String getTaskNameById(int taskID) {
		Log.d(TAG, "getTaskNameById called");
		String taskName ="";
		Cursor cursor = null;
		try {
			String[] selectionArgs = { String.valueOf(taskID) };
			String[] columns = { Db.Tasks.NAME};
			cursor = db.query(tableName, columns, Db.Tasks.GLOBAL_TASK_ID + "=?",
					selectionArgs, null, null, null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					taskName = cursor.getString(0);
					Log.d(TAG, "retreived taskName" + taskName);
				}

			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		Log.d(TAG, "getTaskNameById ends");
		return taskName;
	}

	public int deleteAllTasks() {
		Log.d(TAG, "deleteAllTasks called");

		int result = db.delete(tableName, null, null);

		Log.d(TAG, "deleteAllTasks ends, returns: " + result);

		return result;
	}

	public long insertNewTask(Task task) {
		Log.d(TAG, "insertNewTask called");

		Logger.d(TAG, "task globalID: " + task.getId());
		Logger.d(TAG, "taskName: " + task.getName());
		Logger.d(TAG, "taskDesc: " + task.getDescription());
		Logger.d(TAG, "task projectId: " + task.getProjectID());
		
		ContentValues contentValues = new ContentValues(4);
		
		contentValues.put(Db.Tasks.GLOBAL_TASK_ID, task.getId());
		contentValues.put(Db.Tasks.NAME, task.getName());
		contentValues.put(Db.Tasks.DESCRIPTION, task.getDescription());
		contentValues.put(Db.Tasks.PROJECT_ID, task.getProjectID());

		long returnValue = db.insert(tableName, null, contentValues);
		Log.d(TAG, "insertNewTask ends, returns " + returnValue);

		return returnValue;
	}
	
	public Cursor getTasksCursor() {
		String[] columns = { Db.Tasks.ID, Db.Tasks.NAME, Db.Tasks.DESCRIPTION, Db.Tasks.GLOBAL_TASK_ID};
		Cursor cursor = db.query(tableName, columns,
				null, null, null,
				null, Db.Tasks.NAME);
		return cursor;
	}
	
	public Cursor getTasksCursorFilteredByProject(int projectID) {
		String[] selectionArgs = { Integer.toString(projectID) };
		String[] columns = { Db.Tasks.ID, Db.Tasks.NAME, Db.Tasks.DESCRIPTION, Db.Tasks.GLOBAL_TASK_ID};
		Cursor cursor = db.query(tableName, columns,
				Db.Tasks.PROJECT_ID + "=?", selectionArgs, null,
				null, Db.Tasks.NAME);
		return cursor;
	}
	

}
