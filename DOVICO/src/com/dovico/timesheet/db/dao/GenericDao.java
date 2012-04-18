package com.dovico.timesheet.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Base class shared by all data access objects
 * 
 * @author petar.vucelja
 *
 */
public abstract class GenericDao {
	
	protected static SQLiteDatabase db;
	
	protected String tableName;
	protected String idName;
	
	/**
	 * GenericDao constructor
	 * 
	 * @param db The database
	 */
	protected GenericDao(SQLiteDatabase db) {
		GenericDao.db = db;
	}
	
	/**
	 * Default insert statement
	 * 
	 * @param values the values to insert
	 * @return ID of inserted row
	 */
	protected long insert(ContentValues values) {
        return db.insert(tableName, null, values);
    }
    
	/**
	 * Get all rows
	 * 
	 * @param columns the columns to return
	 * @return selected columns from all rows from the table
	 */
	protected Cursor getAll(String[] columns) {
        return db.query(tableName, columns, null, null, null, null, null);
    }
    
	/**
	 * Return row by ID
	 * 
	 * @param id the ID of the row
	 * @param columns which columns to return
	 * @return selected columns from a given row
	 */
	protected Cursor getById(Integer id, String[] columns) {
		String[] selectionArgs = {id.toString()};
        Cursor cursor = db.query(true, tableName, columns, idName + "=?", selectionArgs, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    
	/**
	 * Delete all rows from table
	 * 
	 * @param table the table to delete from
	 * @return the number of deleted rows
	 */
	protected int deleteAll(String table) {
        return db.delete(table, null, null);
    }
    
	/**
	 * Delete row by ID
	 * 
	 * @param id the id of the row to delete
	 * @return number of deleted rows
	 */
	protected int deleteById(Integer id) {
        return db.delete(tableName, idName + "=" + id, null);
    }
    
	/**
	 * Update a given row
	 * 
	 * @param id the id of the row to update
	 * @param values the values to update
	 * @return the number of rows affected
	 */
	protected int updateById(Integer id, ContentValues values) {
		String[] whereArgs = {id.toString()};
        return db.update(tableName, values, idName + "=?", whereArgs);
    }

}
