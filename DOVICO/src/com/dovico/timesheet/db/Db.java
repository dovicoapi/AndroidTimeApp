package com.dovico.timesheet.db;

/**
 * Place for commonly used DB strings.
 * 
 * @author milos.pesic
 * 
 */
public final class Db {

	public static final String DATABASE_NAME = "dovicotimesheet.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_CLIENTS = "clients";
	public static final String TABLE_PROJECTS = "projects";
	public static final String TABLE_TASKS = "tasks";
	public static final String TABLE_TIME_ENTRIES = "time_entries";
	public static final String TABLE_ASSIGNMENTS = "assignments";

	private Db() {
	}
	
	public static final class Assignments {
		

		public static final String ID = "_id";
		public static final String GLOBAL_ASSIGNMENT_ID = "globalAssignmentID";
		public static final String ITEM_ID = "itemID";
		public static final String NAME = "name";
		public static final String ASSIGNMENT_URI = "assignmentURI";
		
		public static final String CREATE_STATEMENT = "CREATE TABLE "
				+ TABLE_ASSIGNMENTS + "(" 
					+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ GLOBAL_ASSIGNMENT_ID + " TEXT UNIQUE, "
					+ NAME + " TEXT, "
					+ ASSIGNMENT_URI + " TEXT, "
					+ ITEM_ID + " INTEGER"
				+ ")";


	}

	public static final class Clients {
		

		public static final String ID = "_id";
		public static final String GLOBAL_CLIENT_ID = "globalClientID";
		public static final String NAME = "name";		
		public static final String CONTACT = "contact";
		public static final String EMAIL = "email";
		public static final String ASSIGNMENT_URI = "assignmentURI";

		public static final String CREATE_STATEMENT = "CREATE TABLE "
				+ TABLE_CLIENTS + "(" 
					+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ GLOBAL_CLIENT_ID + " INTEGER UNIQUE, "
					+ NAME + " TEXT, "
					+ CONTACT + " TEXT, "
					+ ASSIGNMENT_URI + " TEXT, "
					+ EMAIL + " TEXT"
				+ ")";


	}

	public static final class Projects {
		

		public static final String ID = "_id";
		public static final String GLOBAL_PROJECT_ID = "globalProjectID";
		public static final String CLIENT_ID = "clientID";		
		public static final String LEADER_ID = "leaderID";		
		public static final String NAME = "name";		
		public static final String STATUS = "status";		
		public static final String START_DATE = "start_date";		
		public static final String END_DATE = "end_date";		
		public static final String BILLING_BY = "billing_by";		
		public static final String FIXED_COST = "fixed_cost";		
		public static final String CURRENCY = "currencyID";
		public static final String ASSIGNMENT_URI = "assignmentURI";
		
		public static final String CREATE_STATEMENT = "CREATE TABLE "
					+ TABLE_PROJECTS + "(" 
						+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
						+ GLOBAL_PROJECT_ID + " INTEGER, "
						+ CLIENT_ID + " INTEGER, "
						+ LEADER_ID + " INTEGER, "
						+ NAME + " TEXT, "
						+ STATUS + " TEXT, "
						+ START_DATE + " TEXT, "
						+ END_DATE + " TEXT, "
						+ BILLING_BY + " TEXT, "
						+ FIXED_COST + " TEXT, "
						+ ASSIGNMENT_URI + " TEXT, "
						+ CURRENCY + " INTEGER, "
						+ "UNIQUE("+GLOBAL_PROJECT_ID+", " + CLIENT_ID + ") ON CONFLICT REPLACE "
					+ ")";

	}

	public static final class Tasks {
		
		
		public static final String ID = "_id";
		public static final String GLOBAL_TASK_ID = "globalTaskID";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";	
		public static final String PROJECT_ID = "projectID";
		
		public static final String CREATE_STATEMENT = "CREATE TABLE " 
			+ TABLE_TASKS
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ GLOBAL_TASK_ID + " INTEGER, "
				+ NAME + " TEXT, "
				+ PROJECT_ID + " INTEGER, "
				+ DESCRIPTION + " TEXT, "
				+ "UNIQUE("+GLOBAL_TASK_ID+", " + PROJECT_ID + ") ON CONFLICT REPLACE " + 
			")";
		
	}

public static final class TimeEntries {
		
		public static final String ID = "_id";
		public static final String EMPLOYEE_ID = "timeEntryID";
		public static final String PROJECT_ID = "projectID";
		public static final String CLIENT_ID = "clientID";
		public static final String TASK_ID = "taskID";
		public static final String DATE = "date";
		public static final String TOTAL_HOURS = "total_hours";
		public static final String PROJECT_NAME = "projectName";
		public static final String TASK_NAME = "taskName";
		public static final String STATUS = "status";
		public static final String GLOBAL_TIME_ENTRY_ID = "globalTimeEntryID";
		
		public static final String CREATE_STATEMENT = "CREATE TABLE " 
			+ TABLE_TIME_ENTRIES
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ GLOBAL_TIME_ENTRY_ID + " TEXT, " 
				+ EMPLOYEE_ID + " INTEGER, " 
				+ PROJECT_ID + " INTEGER, " 
				+ CLIENT_ID + " INTEGER, " 
				+ TASK_ID + " INTEGER, " 
				+ TOTAL_HOURS + " TEXT, " 
				+ PROJECT_NAME + " TEXT, " 
				+ TASK_NAME + " TEXT, " 
				+ STATUS + " TEXT, " 
				+ DATE + " TEXT)";
							
	}
}
