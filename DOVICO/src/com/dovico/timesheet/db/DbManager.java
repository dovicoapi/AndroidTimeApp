package com.dovico.timesheet.db;

import java.util.List;

import android.database.Cursor;

import com.dovico.timesheet.beans.Assignment;
import com.dovico.timesheet.beans.Client;
import com.dovico.timesheet.beans.Project;
import com.dovico.timesheet.beans.Task;
import com.dovico.timesheet.beans.TimeEntry;

/**
 * Manager to be used for all DB related operations.
 * 
 * @author milos.pesic
 * 
 */
public interface DbManager {
	
	/**
	 * Returns client name of a client.
	 * 
	 *  @param clientID
	 *            clientID
	 * @return client name of a client, null if some problem occurred
	 */
	String getClientName(int clientID);
	
	/**
	 * Returns all projects from db.
	 * 
	 * @return list of all projects, null if some problem occurred
	 */
	List<Project> getAllProjects();
	
	/**
	 * Returns project name of a project.
	 * 
	 *  @param projectID
	 *            projectID
	 * @return project name of a project, null if some problem occurred
	 */
	String getProjectName(int projectID);
	
	int getProjectClientID(Integer currentProjectID);
	
	/**
	 * Returns assignments.
	 * 
	 * @return list of assigments for a logged in employee, null if some problem occurred
	 */
	List<Assignment> getAllAssignments();
	
	/**
	 * Returns timeEntries.
	 * 
	 * @return list of timeEntries for a logged in employee, null if some problem occurred
	 */
	List<TimeEntry> getAllTimeEntries();
	
	/**
	 * Returns task name of a task.
	 * 
	 *  @param taskID
	 *            taskID
	 * @return task name of a task, null if some problem occurred
	 */
	String getTaskName(int taskID);
	
	/**
	 * Returns Cursor for tasks filtered by projectID.
	 * 
	 *   @param projectID
	 *            projectID
	 * @return Cursor of tasks to iterate, null if some problem
	 *         occurred
	 */
	Cursor getTasksCursorFilteredByProject(int projectID);
	
	/**
	 * Returns Cursor for tasks filtered by projectID.
	 * 
	 *   @return Cursor of tasks to iterate, null if some problem
	 *         occurred
	 */
	Cursor getTasksCursor();

	/**
	 * Returns Cursor for clients.
	 * 
	 * @return Cursor of clients to iterate, null if some problem
	 *         occurred
	 */
	Cursor getClientsCursor();

	/**
	 * Returns Cursor for projects.
	 * 
	 * @return Cursor of projects to iterate, null if some problem
	 *         occurred
	 */
	Cursor getProjectsCursor();
	
	/**
	 * Returns Cursor for assignments.
	 * 
	 * @return Cursor of assignments to iterate, null if some problem
	 *         occurred
	 */
	Cursor getAssignmentsCursor();
	
	/**
	 * Returns Cursor for projects filtered by clientID.
	 * 
	 *   @param clientID
	 *            clientID
	 * @return Cursor of projects to iterate, null if some problem
	 *         occurred
	 */
	Cursor getProjectsCursorFilteredByClient(int clientID);
	
	
	/**
	 * Returns Cursor for all time entries.
	 * 
	 * @return Cursor of time entries to iterate, null if some problem
	 *         occurred
	 */
	Cursor getTimeEntriesCursor();

	/**
	 * Insert list of clients
	 * 
	 * @param clients
	 *            .
	 * @return count of successfully inserted clients
	 */
	int insertClients(List<Client> clients);
	
	/**
	 * Insert client.
	 * 
	 * @param client
	 *            client to insert
	 * @return row affected, -1 if failed
	 */
	long insertNewClient(Client client);

	/**
	 * Insert list of projects.
	 * 
	 * @param projects
	 *            list of projects to insert
	 * @return count of successfully inserted projects
	 */
	int insertProjects(List<Project> projects);
	
	/**
	 * Insert project.
	 * 
	 * @param project
	 *            project to insert
	 * @return row affected, -1 if failed
	 */
	long insertNewProject(Project project);
	
	/**
	 * Insert list of assignments.
	 * 
	 * @param assignments
	 *            list of assignments to insert
	 * @return count of successfully inserted assignments
	 */
	int insertAssignments(List<Assignment> assignments);
	
	/**
	 * Insert list of installed apps.
	 * 
	 * @param tasks
	 *            list of tasks to insert
	 * @return count of successfully inserted tasks
	 */
	int insertTasks(List<Task> tasks);
	
	/**
	 * Insert list of timeEntries.
	 * 
	 * @param timeEntries
	 *            list of timeEntries to insert
	 * @return count of successfully inserted timeEntries
	 */
	int insertTimeEntries(List<TimeEntry> timeEntries);

	/**
	 * Insert timeEntry
	 * 
	 * @param timeEntry
	 *            timeEntry to insert
	 * 
	 * @return row affected, -1 if failed
	 */
	long insertNewTimeEntry(TimeEntry timeEntry);
	
	/**
	 * Insert task
	 * 
	 * @param task
	 *            task to insert
	 * 
	 * @return row affected, -1 if failed
	 */
	long insertNewTask(Task task);

	/**
	 * delete timeEntry
	 * 
	 * @param timeEntryID
	 *            timeEntryID to delete
	 * 
	 * @return row affected, -1 if failed
	 */
	int deleteTimeEntry(long timeEntryID);

	void deleteAllClients();
	
	void deleteAllAssignments();
	
	void deleteAllProjects();

	void deleteAllTasks();
	
	void deleteAllTimeEntries();

	

}
