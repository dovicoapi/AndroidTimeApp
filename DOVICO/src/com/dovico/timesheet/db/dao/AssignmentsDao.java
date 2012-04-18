package com.dovico.timesheet.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dovico.timesheet.beans.Assignment;
import com.dovico.timesheet.db.Db;
import com.dovico.timesheet.utils.Logger;

/**
 * @author milos.pesic
 * 
 */
public class AssignmentsDao extends GenericDao {

	private static final String TAG = "AssignmentsDao";

	/**
	 * UserDao constructor
	 * 
	 * @param context
	 *            The application context
	 * @param db
	 *            database
	 */
	public AssignmentsDao(Context context, SQLiteDatabase db) {
		super(db);
		tableName = Db.TABLE_ASSIGNMENTS;
		idName = Db.Assignments.ID;
	}

	public List<Assignment> getAssignments() {
		Log.d(TAG, "getAssignments called");
		List<Assignment> assignments = null;
		Cursor cursor = null;
		try {
			cursor = getAssignmentsCursor();
			if (cursor != null) {
				assignments = new ArrayList<Assignment>();
				Log.d(TAG,
						"number of assignments in database; "
								+ cursor.getCount());
				if (cursor.moveToFirst()) {
					do {
						assignments
								.add(getAssignmentFromCursor(cursor));
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
		Log.d(TAG, "getAssignments ends");
		return assignments;
	}
	
	public Assignment getAssignmentFromCursor(Cursor cursor) {
		Log.d(TAG, "getAssignmentFromCursor called");
		Assignment assignment = new Assignment();

		assignment.setAssignmentID(cursor.getString(0));
		assignment.setItemID(cursor.getInt(1));
		assignment.setName(cursor.getString(2));
		assignment.setAssignmentURI(cursor.getString(3));

		Log.d(TAG, "getAssignmentFromCursor ends");
		return assignment;
	}

	public Cursor getAssignmentsCursor() {
		String[] columns = { Db.Assignments.GLOBAL_ASSIGNMENT_ID, Db.Assignments.ITEM_ID, Db.Assignments.NAME, Db.Assignments.ASSIGNMENT_URI};
		Cursor cursor = db.query(tableName, columns,
				null, null, null,
				null, Db.Assignments.GLOBAL_ASSIGNMENT_ID + " DESC");
		return cursor;
	}

	public long insertNewAssigment(Assignment assignment) {
		Log.d(TAG, "insertNewProject called");

		Logger.d(TAG, "assignmentID: " + assignment.getAssignmentID());
		Logger.d(TAG, "itemID: " + assignment.getItemID());
		Logger.d(TAG, "name: " + assignment.getName());
		Logger.d(TAG, "assignmentURI: " + assignment.getAssignmentURI());
		
		ContentValues contentValues = new ContentValues(4);
		
		contentValues.put(Db.Assignments.GLOBAL_ASSIGNMENT_ID, assignment.getAssignmentID());
		contentValues.put(Db.Assignments.ITEM_ID, assignment.getItemID());
		contentValues.put(Db.Assignments.NAME, assignment.getName());
		contentValues.put(Db.Assignments.ASSIGNMENT_URI, assignment.getAssignmentURI());

		long returnValue = db.insert(tableName, null, contentValues);
		Log.d(TAG, "insertNewAssigment ends, returns " + returnValue);

		return returnValue;
	}

	public int deleteAllAssignments() {
		Log.d(TAG, "deleteAllAssignments called");

		int result = db.delete(tableName, null, null);

		Log.d(TAG, "deleteAllAssignments ends, returns: " + result);

		return result;
	}

}
