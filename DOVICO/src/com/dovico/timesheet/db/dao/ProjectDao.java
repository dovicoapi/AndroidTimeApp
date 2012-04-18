package com.dovico.timesheet.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dovico.timesheet.beans.Project;
import com.dovico.timesheet.db.Db;
import com.dovico.timesheet.utils.Logger;

/**
 * @author milos.pesic
 * 
 */
public class ProjectDao extends GenericDao {

	private static final String TAG = "ProjectDao";

	/**
	 * UserDao constructor
	 * 
	 * @param context
	 *            The application context
	 * @param db
	 *            database
	 */
	public ProjectDao(Context context, SQLiteDatabase db) {
		super(db);
		tableName = Db.TABLE_PROJECTS;
		idName = Db.Projects.ID;
	}


	public Cursor getProjectsCursor() {
		String[] columns = { Db.Projects.ID, Db.Projects.NAME, Db.Projects.GLOBAL_PROJECT_ID, Db.Projects.FIXED_COST, Db.Projects.ASSIGNMENT_URI};
		Cursor cursor = db.query(tableName, columns,
				null, null, null,
				null, Db.Projects.NAME);
		return cursor;
	}

	public long insertNewProject(Project project) {
		Log.d(TAG, "insertNewProject called");

		Logger.d(TAG, "projectID: " + project.getId());
		Logger.d(TAG, "projectName: " + project.getName());
		
		ContentValues contentValues = new ContentValues(5);
		
		contentValues.put(Db.Projects.GLOBAL_PROJECT_ID, project.getId());
		contentValues.put(Db.Projects.NAME, project.getName());
		contentValues.put(Db.Projects.CLIENT_ID, project.getClientID());
		contentValues.put(Db.Projects.FIXED_COST, project.getFixed_cost());
		contentValues.put(Db.Projects.ASSIGNMENT_URI, project.getAssignmentURI());

		long returnValue = db.insert(tableName, null, contentValues);
		Log.d(TAG, "insertNewProject ends, returns " + returnValue);

		return returnValue;
	}

	public int deleteAllProjects() {
		Log.d(TAG, "deleteAllProjects called");

		int result = db.delete(tableName, null, null);

		Log.d(TAG, "deleteAllProjects ends, returns: " + result);

		return result;
	}

	public Cursor getProjectsCursorFilteredByClient(int clientID) {
		String[] selectionArgs = { Integer.toString(clientID) };
		String[] columns = { Db.Projects.ID, Db.Projects.NAME, Db.Projects.GLOBAL_PROJECT_ID, Db.Projects.FIXED_COST, Db.Projects.ASSIGNMENT_URI};
		Cursor cursor = db.query(tableName, columns,
				Db.Projects.CLIENT_ID + "=?", selectionArgs, null,
				null, Db.Projects.NAME);
		return cursor;
	}
	
	public Project getProjectFromCursor(Cursor cursor) {
		Log.d(TAG, "getProjectFromCursor called");
		Project project = new Project();

		project.setName(cursor.getString(1));
		project.setId(cursor.getInt(2));
		project.setFixed_cost(cursor.getString(3));

		Log.d(TAG, "getProjectFromCursor ends");
		return project;
	}

	public List<Project> getAllProjects() {
		Log.d(TAG, "getAllProjects called");
		List<Project> projects = null;
		Cursor cursor = null;
		try {
			cursor = getProjectsCursor();
			if (cursor != null) {
				projects = new ArrayList<Project>();
				Log.d(TAG,
						"number of projects in database; "
								+ cursor.getCount());
				if (cursor.moveToFirst()) {
					do {
						projects
								.add(getProjectFromCursor(cursor));
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
		Log.d(TAG, "getAllProjects ends");
		return projects;
	}


	public String getProjectNameById(int projectID) {
		Log.d(TAG, "getProjectNameById called");
		String projectName ="";
		Cursor cursor = null;
		try {
			String[] selectionArgs = { String.valueOf(projectID) };
			String[] columns = { Db.Projects.NAME};
			cursor = db.query(tableName, columns, Db.Projects.GLOBAL_PROJECT_ID + "=?",
					selectionArgs, null, null, null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					projectName = cursor.getString(0);
					Log.d(TAG, "retreived projectName" + projectName);
				}

			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		Log.d(TAG, "getProjectNameById ends");
		return projectName;
	}


	public int getProjectClientID(Integer currentProjectID) {
		Log.d(TAG, "getProjectClientID called");
		int projectName = -1;
		Cursor cursor = null;
		try {
			String[] selectionArgs = { String.valueOf(currentProjectID) };
			String[] columns = { Db.Projects.CLIENT_ID};
			cursor = db.query(tableName, columns, Db.Projects.GLOBAL_PROJECT_ID + "=?",
					selectionArgs, null, null, null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					projectName = cursor.getInt(0);
					Log.d(TAG, "retreived projectClientID" + projectName);
				}

			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		Log.d(TAG, "getProjectClientID ends");
		return projectName;
	}

}
