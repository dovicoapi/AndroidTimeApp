package com.dovico.timesheet.common;


public class DOVICORestAPI {
	
	public static class RestURL {
		
		public static final String DOVICO_API_BASE_URL = "https://api.dovico.com/";	
		public static final String DOVICO_API_VERSION = "/?version=";
		
		public static final String GET_ALL_TIME_ENTRIES = DOVICO_API_BASE_URL + "TimeEntries" + DOVICO_API_VERSION;
		public static final String GET_CLIENTS = DOVICO_API_BASE_URL + "Clients" + DOVICO_API_VERSION;
		public static final String GET_PROJECTS = DOVICO_API_BASE_URL + "Projects" + DOVICO_API_VERSION;
		public static final String GET_PROJECT = DOVICO_API_BASE_URL + "Projects/";
		public static final String GET_TASKS = DOVICO_API_BASE_URL + "Tasks" + DOVICO_API_VERSION;
		public static final String GET_EMPLOYEE_INFO = DOVICO_API_BASE_URL + "Employees/Me" + DOVICO_API_VERSION;
		public static final String GET_ASSIGNMENTS_FOR_EMPLOYEE = DOVICO_API_BASE_URL + "Assignments/Employee/";
		public static final String GET_CHILD_ASSIGNMENTS_FOR_ITEM = DOVICO_API_BASE_URL + "Assignments/";
		public static final String GET_CLIENT = DOVICO_API_BASE_URL + "Clients/";		
		public static final String INSERT_TIME_ENTRY = DOVICO_API_BASE_URL + "TimeEntries" + DOVICO_API_VERSION;
		public static final String GET_TIME_ENTRIES = DOVICO_API_BASE_URL + "TimeEntries/Employee/";
		public static final String DELETE_TIME_ENTRY = DOVICO_API_BASE_URL + "TimeEntries/";
		public static final String AUTHENTICATE = DOVICO_API_BASE_URL + "Authenticate" + DOVICO_API_VERSION;
		
	}
	
	public static class APIResponseCodes {
		public static final int AUTHORIZATION_FAILED = 401;	
	}
	
}
