package com.dovico.timesheet.db;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import android.util.Log;

import com.dovico.timesheet.beans.Assignment;
import com.dovico.timesheet.beans.Client;
import com.dovico.timesheet.beans.Project;
import com.dovico.timesheet.beans.Task;
import com.dovico.timesheet.beans.TimeEntry;
import com.dovico.timesheet.db.dao.AssignmentsDao;
import com.dovico.timesheet.db.dao.ClientDao;
import com.dovico.timesheet.db.dao.ProjectDao;
import com.dovico.timesheet.db.dao.TaskDao;
import com.dovico.timesheet.db.dao.TimeEntryDao;
import com.dovico.timesheet.utils.Logger;

/**
 * Manager to be used for all DB related operations.
 * 
 * @author milos.pesic
 * 
 */
public class DbManagerImpl implements DbManager {

	public static final int DUMMY_CLIENT_ID = -1;

	private static final String TAG = "DbManagerImpl";

	private static DbManagerImpl dovicoTimeSheetDBManager;
	private static Object dovicoTimeSheetDBManagerLock = new Object();
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

	private static final String DOVICO_TIMESHEET_DATABASE_PATH = "/data/data/com.dovico.timesheet/databases/";

	// flag to determine if it is first time after install (to pre-populate the
	// DB)
	private static boolean wasOnCreateCalled;

	private Context context;
	private SQLiteDatabase db;
	private ClientDao clientDao;
	private ProjectDao projectDao;
	private TaskDao taskDao;
	private TimeEntryDao timeEntryDao;
	private AssignmentsDao assignmentDao;

	@SuppressWarnings("unused")
	private boolean okToInsertDefaultData;

	private AppTransactionListener txListener = new AppTransactionListener();

	private DbManagerImpl(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);

		db = openHelper.getWritableDatabase();
		clientDao = new ClientDao(context, db);
		projectDao = new ProjectDao(context, db);
		taskDao = new TaskDao(context, db);
		timeEntryDao = new TimeEntryDao(context, db);
		assignmentDao = new AssignmentsDao(context, db);

//		if (wasOnCreateCalled) {
//			Log.d(TAG, "Inserting applications");
//			List<App> apps = getApplicationsOnPhone();
//			insertInstalledApps(apps);
//			Log.d(TAG, "Inserting applications Ok.");
//			wasOnCreateCalled = false;
//		}
	}

	/**
	 * Get Singleton instance
	 * 
	 * @param context
	 *            the application context
	 * @return Instance of DbManagerImpl
	 */
	public static DbManagerImpl getInstance(Context context) {

		Log.i(TAG, "DbManagerImpl getInstance");
		synchronized (dovicoTimeSheetDBManagerLock) {
			if (dovicoTimeSheetDBManager == null) {
				dovicoTimeSheetDBManager = new DbManagerImpl(context);
			}
		}
		return dovicoTimeSheetDBManager;
	}
	
	@Override
	public String getClientName(int clientID) {
		Logger.d(TAG, "getClientName called");
		String clientName = "";
		db.beginTransaction();
		try {
			clientName = clientDao.getClientName(clientID);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Logger.d(TAG, "getClientName ends, clientName: " + clientName);

		return clientName;
	}
	
	@Override
	public List<Assignment> getAllAssignments() {
		Logger.d(TAG, "getAssignments called");
		List<Assignment> assignments;
		db.beginTransaction();
		try {
			assignments = assignmentDao.getAssignments();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Logger.d(TAG, "getAssignments ends");

		return assignments;
	}
	
	@Override
	public List<TimeEntry> getAllTimeEntries() {
		Logger.d(TAG, "getAllTimeEntries called");
		List<TimeEntry> assignments;
		db.beginTransaction();
		try {
			assignments = timeEntryDao.getAllTimeEntries();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Logger.d(TAG, "getAllTimeEntries ends");

		return assignments;
	}
	
	@Override
	public String getTaskName(int taskID) {
		Logger.d(TAG, "getTaskName called");
		String taskName = "";
		db.beginTransaction();
		try {
			taskName = taskDao.getTaskNameById(taskID);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Logger.d(TAG, "getTaskName ends, taskName: " + taskName);

		return taskName;
	}
	
	@Override
	public Cursor getTasksCursorFilteredByProject(int projectID) {
		Log.d(TAG, "getTasksCursorFilteredByProject called");
		Cursor cursor = taskDao.getTasksCursorFilteredByProject(projectID);
		Log.d(TAG, "getTasksCursorFilteredByProject ends");
		return cursor;
	}
	
	@Override
	public Cursor getTasksCursor() {
		Log.d(TAG, "getTasksCursor called");
		Cursor cursor = taskDao.getTasksCursor();
		Log.d(TAG, "getTasksCursor ends");
		return cursor;
	}
	
	@Override
	public List<Project> getAllProjects() {
		Logger.d(TAG, "getAllProjects called");
		List<Project> projects;
		db.beginTransaction();
		try {
			projects = projectDao.getAllProjects();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Logger.d(TAG, "getAllProjects ends");

		return projects;
	}
	
	@Override
	public String getProjectName(int projectID) {
		Logger.d(TAG, "getProjectName called");
		String taskName = "";
		db.beginTransaction();
		try {
			taskName = projectDao.getProjectNameById(projectID);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Logger.d(TAG, "getProjectName ends, projectName: " + taskName);

		return taskName;
	}
	
	@Override
	public int getProjectClientID(Integer currentProjectID) {
		Logger.d(TAG, "getProjectClientID called");
		int clientID = -1;
		db.beginTransaction();
		try {
			clientID = projectDao.getProjectClientID(currentProjectID);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Logger.d(TAG, "getProjectClientID ends, projectClientID: " + clientID);

		return clientID;
	}
	
	@Override
	public Cursor getClientsCursor() {
		Log.d(TAG, "getClientsCursor called");
		Cursor cursor = clientDao.getClientsCursor();
		Log.d(TAG, "getClientsCursor ends");
		return cursor;
	}

	@Override
	public Cursor getProjectsCursor() {
		Log.d(TAG, "getProjectsCursor called");
		Cursor cursor = projectDao.getProjectsCursor();
		Log.d(TAG, "getProjectsCursor ends");
		return cursor;
	}
	
	@Override
	public Cursor getAssignmentsCursor() {
		Log.d(TAG, "getAssignmentsCursor called");
		Cursor cursor = assignmentDao.getAssignmentsCursor();
		Log.d(TAG, "getAssignmentsCursor ends");
		return cursor;
	}
	
	@Override
	public Cursor getProjectsCursorFilteredByClient(int clientID) {
		Log.d(TAG, "getProjectsCursorFilteredByClient called");
		Cursor cursor = projectDao.getProjectsCursorFilteredByClient(clientID);
		Log.d(TAG, "getProjectsCursorFilteredByClient ends");
		return cursor;
	}
	
	@Override
	public Cursor getTimeEntriesCursor() {
		Log.d(TAG, "getTimeEntriesCursorFilteredByEmployee called");
		Cursor cursor = timeEntryDao.getTimeEntriesCursor();
		Log.d(TAG, "getTimeEntriesCursorFilteredByEmployee ends");
		return cursor;
	}


	@Override
	public int insertClients(List<Client> clients) {
		Log.d(TAG, "insertClients called");
		int countSuccessfullInserts = 0;

//		deleteAllClients();
		
//		Client dummyClient = new Client();
//		dummyClient.setName("Choose Client");
//		dummyClient.setId(DUMMY_CLIENT_ID);
//		insertNewClient(dummyClient);

		for (Client client : clients) {
			long rowid;
			rowid = insertNewClient(client);
				if (rowid != DUMMY_CLIENT_ID) {
				countSuccessfullInserts++;
			}
		}

		Log.d(TAG, "inserted " + countSuccessfullInserts
				+ " clients in the db");
		Log.d(TAG, "insertClients ends");
		return countSuccessfullInserts;
	}

	@Override
	public void deleteAllClients() {
		Log.d(TAG, "deleteAllClients called");

		int result = 0;
		db.beginTransactionWithListener(txListener);
		try {
			result = clientDao.deleteAllClients();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		Log.d(TAG, "deleteAllClients ends, result: " + result);


	}
	
	@Override
	public void deleteAllAssignments() {
		Log.d(TAG, "deleteAllAssignments called");

		int result = 0;
		db.beginTransactionWithListener(txListener);
		try {
			result = assignmentDao.deleteAllAssignments();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		Log.d(TAG, "deleteAllAssignments ends, result: " + result);


	}

	public long insertNewClient(Client client) {
		Log.d(TAG, "insertNewClient called");

		long rowid;
		db.beginTransactionWithListener(txListener);
		try {
			rowid = clientDao.insertNewClient(client);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Log.d(TAG, "insertNewClient ends");
		return rowid;
	}

	@Override
	public int insertProjects(List<Project> projects) {
		Log.d(TAG, "insertProjects called");
		int countSuccessfullInserts = 0;

//		deleteAllProjects();

		for (Project project : projects) {
			long rowid;
			rowid = insertNewProject(project);
				if (rowid != DUMMY_CLIENT_ID) {
				countSuccessfullInserts++;
			}
		}

		Log.d(TAG, "inserted " + countSuccessfullInserts
				+ " Projects in the db");
		Log.d(TAG, "insertProjects ends");
		return countSuccessfullInserts;
	}
	

	@Override
	public int insertTimeEntries(List<TimeEntry> timeEntries) {
		Log.d(TAG, "insertTimeEntries called");
		int countSuccessfullInserts = 0;
		
		deleteAllTimeEntries();

		for (TimeEntry timeEntry : timeEntries) {
			long rowid;
			rowid = insertNewTimeEntry(timeEntry);
				if (rowid != DUMMY_CLIENT_ID) {
				countSuccessfullInserts++;
			}
		}

		Log.d(TAG, "inserted " + countSuccessfullInserts
				+ " timeEntries in the db");
		Log.d(TAG, "insertTimeEntries ends");
		return countSuccessfullInserts;
	}
	
	public long insertNewProject(Project project) {
		Log.d(TAG, "insertNewProject called");

		long rowid;
		db.beginTransactionWithListener(txListener);
		try {
			rowid = projectDao.insertNewProject(project);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Log.d(TAG, "insertNewProject ends");
		return rowid;
	}
	
	@Override
	public int insertAssignments(List<Assignment> assignments) {
		Log.d(TAG, "insertAssignments called");
		int countSuccessfullInserts = 0;

		for (Assignment assignment : assignments) {
			long rowid;
			rowid = insertNewAssignment(assignment);
				if (rowid != -1) {
				countSuccessfullInserts++;
			}
		}

		Log.d(TAG, "inserted " + countSuccessfullInserts
				+ " assignments in the db");
		Log.d(TAG, "insertAssignments ends");
		return countSuccessfullInserts;
	}
	
	private long insertNewAssignment(Assignment assignment) {
		Log.d(TAG, "insertNewAssignment called");

		long rowid;
		db.beginTransactionWithListener(txListener);
		try {
			rowid = assignmentDao.insertNewAssigment(assignment);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Log.d(TAG, "insertNewAssignment ends");
		return rowid;
	}

	@Override
	public void deleteAllProjects() {
		Log.d(TAG, "deleteAllProjects called");

		int result = 0;
		db.beginTransactionWithListener(txListener);
		try {
			result = projectDao.deleteAllProjects();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		Log.d(TAG, "deleteAllProjects ends, result: " + result);
	}
	
	@Override
	public void deleteAllTimeEntries() {
		Log.d(TAG, "deleteAlTimeEntries called");

		int result = 0;
		db.beginTransactionWithListener(txListener);
		try {
			result = timeEntryDao.deleteAllTimeEntries();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		Log.d(TAG, "deleteAlTimeEntries ends, result: " + result);
	}

	@Override
	public int insertTasks(List<Task> tasks) {
		Log.d(TAG, "insertTasks called");
		int countSuccessfullInserts = 0;

//		deleteAllTasks();

		for (Task task : tasks) {
			long rowid;
			rowid = insertNewTask(task);
				if (rowid != DUMMY_CLIENT_ID) {
				countSuccessfullInserts++;
			}
		}

		Log.d(TAG, "inserted " + countSuccessfullInserts
				+ " tasks in the db");
		Log.d(TAG, "insertTasks ends");
		return countSuccessfullInserts;
	}
	
	@Override
	public void deleteAllTasks() {
		Log.d(TAG, "deleteAllTasks called");

		int result = 0;
		db.beginTransactionWithListener(txListener);
		try {
			result = taskDao.deleteAllTasks();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		Log.d(TAG, "deleteAllTasks ends, result: " + result);
	}

	@Override
	public long insertNewTask(Task task) {
		Log.d(TAG, "insertNewTask called");

		long rowid;
		db.beginTransactionWithListener(txListener);
		try {
			rowid = taskDao.insertNewTask(task);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		Log.d(TAG, "insertNewTask ends");
		return rowid;
	}

	@Override
	public long insertNewTimeEntry(TimeEntry timeEntry) {
		Log.d(TAG, "insertTimeEntry called");

		long rowid;
		db.beginTransactionWithListener(txListener);
		try {
			rowid = timeEntryDao.insertNewTimeEntry(timeEntry);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		Log.d(TAG, "insertTimeEntry ends");
		return rowid;
	}


	@Override
	public int deleteTimeEntry(long timeEntryID) {
		Log.d(TAG, "deleteTimeEntry called");
		
		int result = 0;
		db.beginTransactionWithListener(txListener);
		try {
			result = timeEntryDao.deleteTimeEntry(timeEntryID);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		Log.d(TAG, "deleteTimeEntry ends, return value: " + result);

		return result;
	}



	class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, Db.DATABASE_NAME, null, Db.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "Creating database for AppRecommender... ");
			db.execSQL(Db.Clients.CREATE_STATEMENT);
			db.execSQL(Db.Projects.CREATE_STATEMENT);
			db.execSQL(Db.Tasks.CREATE_STATEMENT);
			db.execSQL(Db.TimeEntries.CREATE_STATEMENT);
			db.execSQL(Db.Assignments.CREATE_STATEMENT);
			Log.d(TAG, "Database created Ok.");
			wasOnCreateCalled = true;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i(TAG,
					"Upgrading database, this will drop tables and recreate.");
			db.execSQL(DROP_TABLE + Db.TABLE_CLIENTS);
			db.execSQL(DROP_TABLE + Db.TABLE_PROJECTS);
			db.execSQL(DROP_TABLE + Db.TABLE_TASKS);
			db.execSQL(DROP_TABLE + Db.TABLE_TIME_ENTRIES);
			db.execSQL(DROP_TABLE + Db.TABLE_ASSIGNMENTS);
			onCreate(db);
		}

		public void openDatabase() {
			Log.i(TAG, "openDataBase() called!");
			okToInsertDefaultData = true;
			String myPath = DOVICO_TIMESHEET_DATABASE_PATH
					+ Db.DATABASE_NAME;
			SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		}

		@Override
		public synchronized void close() {
			Log.i(TAG, "closing database..");
			if (db != null) {
				db.close();
			}
			super.close();

		}
	}

	private final class AppTransactionListener implements
			SQLiteTransactionListener {

		public boolean isLastTransactionSuccessful;

		public void onBegin() {
			isLastTransactionSuccessful = false;
		}

		public void onCommit() {
			isLastTransactionSuccessful = true;
		}

		public void onRollback() {
			isLastTransactionSuccessful = false;
		}
	}




}