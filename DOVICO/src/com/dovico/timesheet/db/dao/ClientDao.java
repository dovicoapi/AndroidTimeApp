package com.dovico.timesheet.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dovico.timesheet.beans.Client;
import com.dovico.timesheet.db.Db;
import com.dovico.timesheet.utils.Logger;

/**
 * Holds methods for activating, deactivating server-side app recommender.
 * 
 * @author milos.pesic
 * 
 */
public class ClientDao extends GenericDao {

	private static final String TAG = "ClientDao";

	/**
	 * AppDao constructor
	 * 
	 * @param context
	 *            The application context
	 * @param db
	 *            database
	 */
	public ClientDao(Context context, SQLiteDatabase db) {
		super(db);
		tableName = Db.TABLE_CLIENTS;
		idName = Db.Clients.ID;
	}

	public Cursor getClientsCursor() {
		String[] columns = { Db.Clients.ID, Db.Clients.GLOBAL_CLIENT_ID, Db.Clients.NAME,
				Db.Clients.CONTACT, Db.Clients.EMAIL, Db.Clients.ASSIGNMENT_URI};
		Cursor cursor = db.query(tableName, columns,
				null, null, null,
				null, Db.Clients.NAME);
				
		return cursor;
	}

	
	/**
	 * Delete all clients.
	 * 
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */
	public int deleteAllClients() {

		Log.d(TAG, "deleteAllClients called");

		int result = db.delete(tableName, null, null);

		Log.d(TAG, "deleteAllClients ends, returns: " + result);

		return result;
	}


	public long insertNewClient(Client client) {
		Log.d(TAG, "insertNewClient called");
		long returnValue = -1;

		Logger.d(TAG, "clientID: " + client.getId());
		Logger.d(TAG, "clientName: " + client.getName());
		Logger.d(TAG, "clientMail: " + client.getEmail());
		Logger.d(TAG, "getContact: " + client.getContact());
		Logger.d(TAG, "getAssignmentURI: " + client.getAssignmentURI());
		
		ContentValues contentValues = new ContentValues(5);
		
		contentValues.put(Db.Clients.GLOBAL_CLIENT_ID, client.getId());
		contentValues.put(Db.Clients.NAME, client.getName());
		contentValues.put(Db.Clients.EMAIL, client.getEmail());
		contentValues.put(Db.Clients.ASSIGNMENT_URI, client.getAssignmentURI());
		contentValues.put(Db.Clients.CONTACT, client.getContact());

		returnValue = db.insert(tableName, null, contentValues);
		Log.d(TAG, "insertNewClient ends, returns " + returnValue);
		
		return returnValue;
	}

	public String getClientName(int clientID) {
		Log.d(TAG, "getClientName called");
		String clientName ="";
		Cursor cursor = null;
		try {
			String[] selectionArgs = { String.valueOf(clientID) };
			String[] columns = { Db.Clients.NAME};
			cursor = db.query(tableName, columns, Db.Clients.GLOBAL_CLIENT_ID + "=?",
					selectionArgs, null, null, null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					clientName = cursor.getString(0);
					Log.d(TAG, "retreived taskName" + clientName);
				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		Log.d(TAG, "getClientName ends");
		return clientName;
	}
	
}
