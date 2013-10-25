package com.dovico.timesheet;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dovico.timesheet.beans.Assignment;
import com.dovico.timesheet.beans.Client;
import com.dovico.timesheet.beans.Project;
import com.dovico.timesheet.beans.TimeEntry;
import com.dovico.timesheet.common.IOUtil;
import com.dovico.timesheet.db.Db;
import com.dovico.timesheet.db.DbManager;
import com.dovico.timesheet.db.DbManagerImpl;
import com.dovico.timesheet.rest.methods.GetAssignmentsForEmployee;
import com.dovico.timesheet.rest.methods.GetChildAssignmentsForItem;
import com.dovico.timesheet.rest.methods.InsertTimeEntry;
import com.dovico.timesheet.utils.Logger;
import com.dovico.timesheet.utils.SharedPrefsUtil;
import com.dovico.timesheet.widgets.CalendarView;
import com.dovico.timesheet.widgets.CalendarView.OnDateSelectedListener;
import com.dovico.timesheet.widgets.DOVICOSpinner;
import com.dovico.timesheet.widgets.HorizontalPager;
import com.dovico.timesheet.widgets.HorizontalPager.OnScreenSwitchListener;
import com.dovico.timesheet.widgets.adapters.DOVICOSpinnerCursorAdapter;

public class ActivityMain extends Activity implements OnDateSelectedListener {
	
	private static final int TIME_ENTRY_NUMBER_OF_PAGES = 2;

	private static final String TAG = "ActivityMain";
	
	protected static final int DIALOG_AUTHORIZATION_FAILED_ID = 0;
	protected static final int DIALOG_NO_INTERNET_CONNECTION_ID = 1;
	protected static final int DIALOG_RETRIEVING_PROJECTS_ID = 2;
	protected static final int DIALOG_RETRIEVING_TASKS_ID = 3;
	protected static final int DIALOG_RETRIEVING_CLIENTS_ID = 4;
	protected static final int DIALOG_INSERT_TIME_ENTRY = 5;
	
	private HorizontalPager horizontalPager;
	private LayoutInflater inflater;
	
	private DbManager dbManager;
	private String currentSelectedDate;
	private Integer currentProjectID;
	private Integer currentTaskID;
	private Integer currentEmployeeID;
	private Integer currentClientID;
	private Double currentTotalHours;
	
	private ProgressDialog getProjectsDialog;
	private ProgressDialog getTasksDialog;
	private ProgressDialog getClientsDialog;
	private ProgressDialog insertTimeEntryDialog;
	private OnItemSelectedListener projectsSpinnerListener;
	
private class GetAssignmentsAsync extends AsyncTask {

	
	String accessToken, userToken;

	public GetAssignmentsAsync(String accessToken, String userToken) {
		this.accessToken = accessToken;
		this.userToken = userToken;
	}

	@Override
	protected Object doInBackground(Object... params) {
				
		List<Assignment> assignments = dbManager.getAllAssignments();
		
		if (assignments.size() == 0) {
		
		dbManager.deleteAllProjects();
		dbManager.deleteAllClients();
		dbManager.deleteAllTasks();
		dbManager.deleteAllAssignments();
		dbManager.deleteAllTimeEntries();
		
			GetAssignmentsForEmployee getAssignments = new GetAssignmentsForEmployee(getApplicationContext(), 
					Integer.toString(SharedPrefsUtil.getIntFromSharedPrefs(getBaseContext(), SharedPrefsUtil.EMPLOYEE_ID)), userToken, "1");
			getAssignments.execute();
			if (getAssignments.getResponseCode() == 401) {
				runOnUiThread(new Runnable() {

					public void run() {
						showDialog(DIALOG_AUTHORIZATION_FAILED_ID);
					}
				});
			}
		assignments = dbManager.getAllAssignments();
		}
		
		
		for (Assignment assignment: assignments) {			
			String assignmentID = assignment.getAssignmentID();
			int itemID = assignment.getItemID();
			String name = assignment.getName();
			
			if (assignmentID.startsWith("C")) {
				Client client = new Client();
				client.setName(assignment.getName());
				client.setId(itemID);
				client.setAssignmentURI(assignment.getAssignmentURI());
				if (dbManager.getClientName(itemID).equals("")) {
					dbManager.insertNewClient(client);
				}
				
				if (hideClientName == 0) {
					GetChildAssignmentsForItem getChildAssignments = new GetChildAssignmentsForItem(getApplicationContext(), assignmentID, assignment.getAssignmentURI(), userToken, "1");
					getChildAssignments.execute();
				}
				
			} else if (assignmentID.startsWith("P")){
				Project project = new Project();
				project.setClientID(0);
				project.setName(name);
				project.setId(itemID);
				project.setAssignmentURI(assignment.getAssignmentURI());
				dbManager.insertNewProject(project);
				
				
				if (dbManager.getClientName(0).equals("")) {
					Client dummYClient = new Client();
					dummYClient.setId(0);
					dummYClient.setName("Other Assignments");
					dbManager.insertNewClient(dummYClient);
				}
				
			}													
				
		}		
		
		if (hideClientName == 1) {
			Cursor clientsCursor = dbManager.getClientsCursor();
			//startManagingCursor(clientsCursor);

			final String[] columns = new String[] { Db.Clients.NAME};
			final int[] to = new int[] { R.id.client_name};

			setAdapterToSpinner((DOVICOSpinner)findViewById(R.id.client_spinner), clientsCursor, getString(R.string.client_spinner_prompt), columns, to);
		} else {
			
			Cursor projectsCursor = dbManager.getProjectsCursor();

			String[] columns = new String[] { Db.Projects.NAME};
			int[] to = new int[] { R.id.client_name};

			setAdapterToSpinner((DOVICOSpinner)findViewById(R.id.project_spinner), projectsCursor, getString(R.string.project_spinner_prompt), columns, to);
		}
		return null;
	}



	@Override
	protected void onPreExecute() {
		showDialog(DIALOG_RETRIEVING_CLIENTS_ID);
	}

	@Override
	protected void onPostExecute(Object result) {
		if (getClientsDialog.isShowing()) {
			dismissDialog(DIALOG_RETRIEVING_CLIENTS_ID);
		}
	}

}
	
private class GetChildAssignmentsAsync extends AsyncTask {

	
	String accessToken, userToken, parentAssignmentID, assignmentURI;
	boolean clientChildAssignments; 

	public GetChildAssignmentsAsync(String accessToken, String userToken, String parenAssigmentID, String assignmentURI) {
		this.accessToken = accessToken;
		this.userToken = userToken;
		this.parentAssignmentID = parenAssigmentID;
		Logger.d(TAG, "assignmentURI passed to AsyncTask: " + assignmentURI);
		this.assignmentURI = assignmentURI;
		if (parentAssignmentID.startsWith("C")) {
			clientChildAssignments = true;
		} else if (parentAssignmentID.startsWith("P")) {
			clientChildAssignments = false;
		}
	}

	@Override
	protected Object doInBackground(Object... params) {
		Cursor cursor = null;
		if (clientChildAssignments) {
			cursor = dbManager.getProjectsCursorFilteredByClient(currentClientID);
		} else {
			cursor = dbManager.getTasksCursorFilteredByProject(currentProjectID);
		}
		
		if (cursor.getCount() == 0) {
		
		Logger.d(TAG, "assignmentURI sent for REST method: " + assignmentURI);
		GetChildAssignmentsForItem getChildAssignments = new GetChildAssignmentsForItem(getApplicationContext(), parentAssignmentID, assignmentURI, userToken, "1");
		getChildAssignments.execute();
			if (getChildAssignments.getResponseCode() == 401) {
				runOnUiThread(new Runnable() {

					public void run() {
						showDialog(DIALOG_AUTHORIZATION_FAILED_ID);
					}
				});
			}
			
			if (clientChildAssignments) {
				cursor = dbManager.getProjectsCursorFilteredByClient(currentClientID);
			} else {
				cursor = dbManager.getTasksCursorFilteredByProject(currentProjectID);
			}

		}
			
			if (clientChildAssignments) {

            	String[] columns = new String[] { Db.Projects.NAME};
            	int[] to = new int[] { R.id.client_name};

            	setAdapterToSpinner((DOVICOSpinner)findViewById(R.id.project_spinner), cursor, getString(R.string.project_spinner_prompt), columns, to);
			} else {

				String[] columns = new String[] { Db.Tasks.NAME};
				int[] to = new int[] { R.id.client_name};

				setAdapterToSpinner((DOVICOSpinner)findViewById(R.id.task_spinner), cursor, getString(R.string.task_spinner_prompt), columns, to);
			}
		return null;
	}



	@Override
	protected void onPreExecute() {
		showDialog(DIALOG_RETRIEVING_CLIENTS_ID);
	}

	@Override
	protected void onPostExecute(Object result) {
		if (getClientsDialog.isShowing()) {
			dismissDialog(DIALOG_RETRIEVING_CLIENTS_ID);
		}
	}

}

private class InsertTimeEntryTask extends AsyncTask {

	TimeEntry newTimeEntry;
	String userToken;

	public InsertTimeEntryTask(String userToken, TimeEntry newTimeEntry) {
		this.userToken = userToken;
		this.newTimeEntry = newTimeEntry;
	}

	@Override
	protected Object doInBackground(Object... params) {
		InsertTimeEntry insertTimeEntry = new InsertTimeEntry(getApplicationContext(), newTimeEntry, userToken, "1");
		insertTimeEntry.execute();
			if (insertTimeEntry.getResponseCode() == 400) {
				runOnUiThread(new Runnable() {	

					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "Time entry wasn't inserted. Please make sure your day has under 24 hours and the day has not been locked due to a time lockout rule.", Toast.LENGTH_LONG).show();
					}
					});
				} else {
					runOnUiThread(new Runnable() {	

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "Time Entry added successfully!", Toast.LENGTH_LONG).show();
							((EditText)findViewById(R.id.totalHoursEditText)).setText("");
							((EditText)findViewById(R.id.descriptionEditText)).setText("");
							onClickPrevious(null);
						}
						});			
					
				}
			return null;
		}

	@Override
	protected void onPreExecute() {
		showDialog(DIALOG_INSERT_TIME_ENTRY);
	}

	@Override
	protected void onPostExecute(Object result) {
		if (insertTimeEntryDialog.isShowing()) {
			dismissDialog(DIALOG_INSERT_TIME_ENTRY);
		}
	}

}
		
	private GetAssignmentsAsync getAssignmentsAsync;
	private GetChildAssignmentsAsync getChildAssignAsync;

	private int timeEntryMode;
	private int sdkVersion;
	private int hideClientName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sdkVersion = SharedPrefsUtil.getIntFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.SDK_VERSION);
		
		dbManager = DbManagerImpl.getInstance(getApplicationContext());
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		horizontalPager = (HorizontalPager) findViewById(R.id.flip_item_1);
		horizontalPager
				.setOnScreenSwitchListener(new OnScreenSwitchListener() {
					
					public void onScreenSwitched(int screen) {
						Logger.d(TAG, "Screen changed!");
						initializeNavigationControls();
					}
				});
		horizontalPager.setPageCount(TIME_ENTRY_NUMBER_OF_PAGES);
		
		horizontalPager.addView(inflater.inflate(R.layout.page_calendar_entry, null));
		
		ViewGroup pageClientSelection = (ViewGroup) inflater.inflate(R.layout.page_client_selection, null);
		pageClientSelection.findViewById(R.id.project_spinner).setEnabled(false);
		pageClientSelection.findViewById(R.id.task_spinner).setEnabled(false);
		
		horizontalPager.addView(pageClientSelection);
		
		((EditText)findViewById(R.id.totalHoursEditText)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() != 0 && currentTaskID != null) {
					enableSaveTimeEntryOption(null);
				} else {
					disableSaveTimeEntry(null);
				}
			}
		});
		
		//horizontalPager.addView(totalHoursEntry);
		
		((CalendarView)findViewById(R.id.calendarview_calendar)).setOnDateSelectedListener(this);
		
		Calendar today = Calendar.getInstance();
		currentSelectedDate = today.get(Calendar.YEAR) + "-" + String.format("%02d", today.get(Calendar.MONTH)+1) + "-" + today.get(Calendar.DAY_OF_MONTH);
		Logger.d(TAG, "currentDate: " + currentSelectedDate);
		
	}
	
    
 


	@Override
    public void onResume() {
    	super.onResume();
    	
    	// james says: i put this code in to help with pushing to the setup screen first
    	String sFirstTimeInit = SharedPrefsUtil.getStringFromSharedPrefs(getApplicationContext(), "FirstTimeInit");
    	if (sFirstTimeInit.equals(""))
    	{
    		SharedPrefsUtil.putStringToSharedPrefs(getApplicationContext(), "FirstTimeInit", "T");
	    	Intent intent = new Intent(this, ActivitySetup.class);
	    	startActivity(intent);
	    	
	    	return;
    	}
    	
    	timeEntryMode = SharedPrefsUtil.getIntFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.TIME_ENTRY_MODE);
    	if (timeEntryMode == 1) { 
    		((EditText)findViewById(R.id.totalHoursEditText)).setHint("e.g. 1.5");
    		((EditText)findViewById(R.id.totalHoursEditText)).setRawInputType(InputType.TYPE_CLASS_NUMBER);
    	} else {
    		((EditText)findViewById(R.id.totalHoursEditText)).setHint("e.g. 01:30");
    		((EditText)findViewById(R.id.totalHoursEditText)).setRawInputType(InputType.TYPE_CLASS_TEXT);
    	}
    	
    	hideClientName = SharedPrefsUtil.getIntFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.HIDE_CLIENT_NAME);
    	
    	
    	// Grab a reference to the description text box
    	View vChild = horizontalPager.getChildAt(1);
    	EditText txtDescription = (EditText)vChild.findViewById(R.id.descriptionEditText);
    	
    	if (hideClientName == 0) {
    		vChild.findViewById(R.id.client_spinner).setVisibility(View.GONE);
    		vChild.findViewById(R.id.textView1).setVisibility(View.GONE);
    		
    		// Limit the number of lines shown before scrolling starts to 3    		
    		txtDescription.setMaxLines(3);
    	} else {
    		vChild.findViewById(R.id.client_spinner).setVisibility(View.VISIBLE);
    		vChild.findViewById(R.id.textView1).setVisibility(View.VISIBLE);
    		
    		// Limit the number of lines shown before scrolling starts to 1    		
    		txtDescription.setMaxLines(1);
    	}
    	
    	List<Assignment> assigns = dbManager.getAllAssignments();
    	
    	if ( (hideClientName == 1 && (currentClientID == null || assigns.size() == 0)) 
    			|| hideClientName == 0){
    	
    		new Thread() {

    			private boolean firstTimeClients;
    			private boolean firstTimeProjects;
    			private boolean firstTimeTasks;

    			@Override
    			public void run() {
    				if (IOUtil.isOnline(getApplicationContext())) {
    					final String accessToken = SharedPrefsUtil.getStringFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.ACCESS_TOKEN);
    					final String userToken = SharedPrefsUtil.getStringFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.USER_TOKEN);


    					runOnUiThread(new Runnable() {

    						@Override
    						public void run() {
    							getAssignmentsAsync = new GetAssignmentsAsync(accessToken, userToken);
    							getAssignmentsAsync.execute((Object[])null);
    						}
    					});

    					Logger.i(TAG, "Populating spinners from the start");
    					final DOVICOSpinner clientSpinner = (DOVICOSpinner) horizontalPager.getChildAt(1).findViewById(R.id.client_spinner);
    					final DOVICOSpinner projectsSpinner = (DOVICOSpinner) horizontalPager.getChildAt(1).findViewById(R.id.project_spinner);
    					final DOVICOSpinner tasksSpinner = (DOVICOSpinner) horizontalPager.getChildAt(1).findViewById(R.id.task_spinner);
    					
    					

    					if (hideClientName == 1) {
    						projectsSpinner.setEnabled(false);
    						tasksSpinner.setEnabled(false);
    						
    						firstTimeClients = true;
    						
							try {
								DOVICOSpinnerCursorAdapter adapter = (DOVICOSpinnerCursorAdapter) projectsSpinner
										.getAdapter();
								if (adapter != null) {
									adapter.notifyDataSetInvalidated();
								}

								adapter = (DOVICOSpinnerCursorAdapter) tasksSpinner
										.getAdapter();
								if (adapter != null) {
									adapter.notifyDataSetInvalidated();
								}
							} catch (Exception ex) {

							}

        					clientSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {


        						@Override
        						public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


        							Cursor c = (Cursor)parent.getItemAtPosition(pos);
        							int selectedClientId = c.getInt(c.getColumnIndexOrThrow(Db.Clients.GLOBAL_CLIENT_ID));
        							Logger.d(TAG, "currentClientID: " + currentClientID + ", selectedClientId: " + selectedClientId + ", selectedClientAssignmentURI: " + c.getString(c.getColumnIndexOrThrow(Db.Clients.ASSIGNMENT_URI)));


        							if (sdkVersion <= 10 && (firstTimeClients || ((currentClientID != null) && (currentClientID == selectedClientId)))) {
        								firstTimeClients = false;
        								Logger.d(TAG, "firstTimeClient is true, so returning without selecting projects");
        								return;
        							}

        							currentClientID = selectedClientId;



        							if (currentClientID != 0) {
        								getChildAssignAsync = new GetChildAssignmentsAsync(accessToken, userToken, "C" + Integer.toString(currentClientID), c.getString(c.getColumnIndexOrThrow(Db.Clients.ASSIGNMENT_URI)));
        								getChildAssignAsync.execute((Object[])null);

        							} else {
        								Cursor projectsCursor = dbManager.getProjectsCursorFilteredByClient(currentClientID);

        								String[] columns = new String[] { Db.Projects.NAME};
        								int[] to = new int[] { R.id.client_name};

        								setAdapterToSpinner((DOVICOSpinner)findViewById(R.id.project_spinner), projectsCursor, getString(R.string.project_spinner_prompt), columns, to);

        							}

        							((DOVICOSpinnerCursorAdapter)clientSpinner.getAdapter()).notifyDataSetChanged();

        							firstTimeProjects = true;
        							firstTimeTasks = true;

        							tasksSpinner.setEnabled(false);
        							DOVICOSpinnerCursorAdapter adapter = (DOVICOSpinnerCursorAdapter) tasksSpinner.getAdapter();
        							if (adapter != null) {
        								adapter.notifyDataSetInvalidated();
        							}
        							projectsSpinner.setEnabled(true);


        							projectsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        								@Override
        								public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        									Cursor c = (Cursor)parent.getItemAtPosition(pos);
        									int selectedProjectID = c.getInt(c.getColumnIndexOrThrow(Db.Projects.GLOBAL_PROJECT_ID));
        									Logger.d(TAG, "pos: " + pos +", currentProjectID: " + currentProjectID + ", selectedProjectID: " + selectedProjectID + ", selectedProjectAssignmentURI: " + c.getString(c.getColumnIndexOrThrow(Db.Projects.ASSIGNMENT_URI)));

        									if (sdkVersion <= 10 && (firstTimeProjects || ((currentProjectID != null) && (currentProjectID == selectedProjectID)))) {
        										firstTimeProjects = false;
        										Logger.d(TAG, "firstTimeProjects is true, so returning without selecting tasks");
        										return;
        									} 


        									currentProjectID = selectedProjectID;

        									getChildAssignAsync = new GetChildAssignmentsAsync(accessToken, userToken, "P" + Integer.toString(currentProjectID), c.getString(c.getColumnIndexOrThrow(Db.Projects.ASSIGNMENT_URI)));
        									getChildAssignAsync.execute((Object[])null);

        									((DOVICOSpinnerCursorAdapter)projectsSpinner.getAdapter()).notifyDataSetChanged();
        									tasksSpinner.setEnabled(true);


        									firstTimeTasks = true;


        									tasksSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        										@Override
        										public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        											Cursor c = (Cursor)parent.getItemAtPosition(pos);
        											int selectedTaskId = c.getInt(c.getColumnIndexOrThrow(Db.Tasks.GLOBAL_TASK_ID));
        											Logger.d(TAG, "selectedTaskId: " + selectedTaskId);

        											if (sdkVersion <= 10 && firstTimeTasks) {
        												firstTimeTasks = false;
        												return;
        											} 

        											((DOVICOSpinnerCursorAdapter)tasksSpinner.getAdapter()).notifyDataSetChanged();

        											currentTaskID = selectedTaskId;
        											Editable totalHoursText = ((EditText)findViewById(R.id.totalHoursEditText)).getEditableText();
        											if (totalHoursText.length() != 0) {
        												enableSaveTimeEntryOption(null);
        											}

        										}

        										@Override
        										public void onNothingSelected(AdapterView<?> parent) {
        										}
        									});


        								}
        								@Override
        								public void onNothingSelected(AdapterView<?> parent) {
        								}
        							});


        						}
        						@Override
        						public void onNothingSelected(AdapterView<?> parent) {
        							Logger.i (TAG, "Nothing is selected");
        						}
        					});
    						
    					} else {
    						projectsSpinner.setEnabled(true);
    						tasksSpinner.setEnabled(false);
    						

							firstTimeProjects = true;
							firstTimeTasks = true;

							try {
								DOVICOSpinnerCursorAdapter adapter = (DOVICOSpinnerCursorAdapter) tasksSpinner.getAdapter();
								if (adapter != null) {
									adapter.notifyDataSetInvalidated();
								}
							} catch (Exception e) {
								
							}


							projectsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
								Cursor c = (Cursor)parent.getItemAtPosition(pos);
								int selectedProjectID = c.getInt(c.getColumnIndexOrThrow(Db.Projects.GLOBAL_PROJECT_ID));
								Logger.d(TAG, "pos: " + pos +", currentProjectID: " + currentProjectID + ", selectedProjectID: " + selectedProjectID + ", selectedProjectAssignmentURI: " + c.getString(c.getColumnIndexOrThrow(Db.Projects.ASSIGNMENT_URI)));

								if (sdkVersion <= 10 && (firstTimeProjects || ((currentProjectID != null) && (currentProjectID == selectedProjectID)))) {
									firstTimeProjects = false;
									Logger.d(TAG, "firstTimeProjects is true, so returning without selecting tasks");
									return;
								} 


								currentProjectID = selectedProjectID;

								getChildAssignAsync = new GetChildAssignmentsAsync(accessToken, userToken, "P" + Integer.toString(currentProjectID), c.getString(c.getColumnIndexOrThrow(Db.Projects.ASSIGNMENT_URI)));
								getChildAssignAsync.execute((Object[])null);

								((DOVICOSpinnerCursorAdapter)projectsSpinner.getAdapter()).notifyDataSetChanged();
								tasksSpinner.setEnabled(true);


								firstTimeTasks = true;


								tasksSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
									@Override
									public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
										Cursor c = (Cursor)parent.getItemAtPosition(pos);
										int selectedTaskId = c.getInt(c.getColumnIndexOrThrow(Db.Tasks.GLOBAL_TASK_ID));
										Logger.d(TAG, "selectedTaskId: " + selectedTaskId);

										if (sdkVersion <= 10 && firstTimeTasks) {
											firstTimeTasks = false;
											return;
										} 

										((DOVICOSpinnerCursorAdapter)tasksSpinner.getAdapter()).notifyDataSetChanged();

										currentTaskID = selectedTaskId;
										Editable totalHoursText = ((EditText)findViewById(R.id.totalHoursEditText)).getEditableText();
										if (totalHoursText.length() != 0) {
											enableSaveTimeEntryOption(null);
										}

									}

									@Override
									public void onNothingSelected(AdapterView<?> parent) {
									}
								});


							}
							@Override
							public void onNothingSelected(AdapterView<?> parent) {
							}
						});


					}

    					

    					

    				} else {
    					runOnUiThread(new Runnable() {

    						public void run() {
    							showDialog(DIALOG_NO_INTERNET_CONNECTION_ID);
    						}
    					});
    				}
    			}


    		}.start();	
    	} 
    	
    	initializeNavigationControls();
    	
    }
    
    protected void setAdapterToSpinner(
			final DOVICOSpinner spinner,
			final Cursor cursor, final String cursorPrompt, final String[] columns,
			final int[] to) {
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				final DOVICOSpinnerCursorAdapter mAdapter = new DOVICOSpinnerCursorAdapter(getApplicationContext(), R.layout.client_spinner_item, cursor, columns, to, cursorPrompt);		
				mAdapter.setDropDownViewResource(R.layout.client_spinner_item);
				spinner.setAdapter(mAdapter);
			}
		});
		
		
	}
    
    private void enableSaveTimeEntryOption(View saveView) {
    	if (saveView == null) {
    		saveView = (Button) findViewById(R.id.button_save);
    	}		
    	saveView.setEnabled(true);
    	saveView.setClickable(true);
    	saveView.setBackgroundResource(R.drawable.btn_orange);
    	((Button)saveView).setTextColor(0xffffffff);
	}
    
    private void disableSaveTimeEntry(View saveView) {
    	if (saveView == null) {
    		saveView = (Button) findViewById(R.id.button_save);
    	}
    	saveView.setEnabled(false);
    	saveView.setClickable(false);
    	saveView.setBackgroundResource(R.drawable.btn_orange_light);
    	((Button)saveView).setTextColor(0xffcdc8b1);
	}
    
    private void initializeNavigationControls() {
		
		Button previousArrow = (Button) findViewById(R.id.button_previous);
		Button nextArrow = (Button) findViewById(R.id.button_next);
		Button buttonSave = (Button) findViewById(R.id.button_save);
		
		int currScreen = horizontalPager.getCurrentScreen();

		if (currScreen > 0) {
			previousArrow.bringToFront();
			previousArrow.setClickable(true);
			previousArrow.setVisibility(View.VISIBLE);
		} else {
			previousArrow.setClickable(false);
			previousArrow.setVisibility(View.INVISIBLE);
		}
		
		if (currScreen == (horizontalPager.getPageCount() - 1)) {
						
			((EditText)findViewById(R.id.totalHoursEditText)).clearFocus();
			
			nextArrow.setVisibility(View.GONE);
			nextArrow.setClickable(false);
			buttonSave.bringToFront();			
			buttonSave.setVisibility(View.VISIBLE);
						
			if (currentClientID == null || currentProjectID == null || currentSelectedDate.equals("") || currentTaskID == null || ((EditText)findViewById(R.id.totalHoursEditText)).getEditableText().length() == 0) {
				disableSaveTimeEntry(buttonSave);
			} else {
				enableSaveTimeEntryOption(buttonSave);
			}
			
		} else {
			buttonSave.setVisibility(View.INVISIBLE);
			buttonSave.setClickable(false);
			nextArrow.bringToFront();
			nextArrow.setClickable(true);
			nextArrow.setVisibility(View.VISIBLE);
		}
		
//		findViewById(R.id.client_spinner).bringToFront();

	}
    
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog = null;
    	AlertDialog.Builder builder;
    	LayoutInflater inflater;
    	View layout;
    	switch (id) {
    	case DIALOG_AUTHORIZATION_FAILED_ID:

    		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    		layout = inflater.inflate(
    				R.layout.dialog_authorization_failed,
    				(ViewGroup) findViewById(R.id.layout_root));

    		builder = new AlertDialog.Builder(this);
    		builder.setView(layout);
    		dialog = builder.create();

    		dialog.setCancelable(false);

    		break;
    	case DIALOG_NO_INTERNET_CONNECTION_ID:

    		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    		layout = inflater.inflate(
    				R.layout.dialog_no_internet_connection,
    				(ViewGroup) findViewById(R.id.layout_root));

    		builder = new AlertDialog.Builder(this);
    		builder.setView(layout);
    		dialog = builder.create();

    		dialog.setCancelable(false);

    		break;
    	case DIALOG_RETRIEVING_PROJECTS_ID:

    		getProjectsDialog = new ProgressDialog(
					ActivityMain.this);
    		getProjectsDialog.setMessage("Loading Projects");
			return getProjectsDialog;
    	case DIALOG_RETRIEVING_TASKS_ID:

    		getTasksDialog = new ProgressDialog(
					ActivityMain.this);
    		getTasksDialog.setMessage("Loading Tasks");
			return getTasksDialog;
    	case DIALOG_RETRIEVING_CLIENTS_ID:

    		getClientsDialog = new ProgressDialog(
					ActivityMain.this);
    		getClientsDialog.setMessage("Loading Info");
			return getClientsDialog;
    	case DIALOG_INSERT_TIME_ENTRY:

        	insertTimeEntryDialog = new ProgressDialog(
        			ActivityMain.this);
        	insertTimeEntryDialog.setMessage("Adding time entry...");
        	return insertTimeEntryDialog;

        }

    	return dialog;
    }
    
    public void onClickDialogAuthorizationFailedOk(View v) {
		dismissDialog(DIALOG_AUTHORIZATION_FAILED_ID);
		Intent intent = new Intent(this, ActivitySetup.class);
		startActivity(intent);
	}
    
    public void onClickDialogNoInternetConnectionOk(View v) {
		dismissDialog(DIALOG_NO_INTERNET_CONNECTION_ID);
		finish();
	}
	
	public void onClickTimeEntry(View v) {
//		Intent intent = new Intent(ActivityMain.this, ActivityMain.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
	}
	public void onClickTimesheet(View v) {
		Intent intent = new Intent(ActivityMain.this, ActivityTimesheet.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickSetup(View v) {
		Intent intent = new Intent(ActivityMain.this, ActivitySetup.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickAbout(View v) {
		Intent intent = new Intent(ActivityMain.this, ActivityAbout.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickSave(View v){
		currentEmployeeID = SharedPrefsUtil.getIntFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.EMPLOYEE_ID);
		
		Logger.d(TAG, "User clicked save time entry, current info:");
		Logger.d(TAG,"clientID: " + currentClientID + ", employeeID: " + currentEmployeeID + " , projectID: " + currentProjectID
				+ ", date: " + currentSelectedDate + ", task: " + currentTaskID + ", totalHours: " + currentTotalHours);
		
		Editable totalHoursText = ((EditText)findViewById(R.id.totalHoursEditText)).getEditableText();
		if (timeEntryMode == 1) {
			try {
				double totalHours = Double.parseDouble(totalHoursText.toString());				
				currentTotalHours = totalHours;
			} catch (NumberFormatException nfe) {
				Toast.makeText(getApplicationContext(), "Please enter hours in decimal format (e.g. 1.5)", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			String totalHoursString = totalHoursText.toString();
			int idenxOfColon = totalHoursString.indexOf(":");
			if (!totalHoursString.contains(":") || 
					totalHoursString.substring(idenxOfColon).length() > 3) {												
				Toast.makeText(getApplicationContext(), "Please enter hours in format HH:mm (e.g. 01:30) or :mm (e.g. :30)", Toast.LENGTH_SHORT).show();
				return;
			} else {
				int hours = 0;
				String hoursString = totalHoursString.substring(0, idenxOfColon);
				if (hoursString.length() > 0) {
					hours = Integer.parseInt(hoursString);
				}
				int minutes = Integer.parseInt(totalHoursString.substring(idenxOfColon + 1,totalHoursString.length()));
				if (hours >= 24 && minutes > 0) {
					Toast.makeText(getApplicationContext(), "No more than 24 hours could be entered for a single day", Toast.LENGTH_SHORT).show();
					return;
				}				
				
				currentTotalHours = (double)hours + (double) minutes/60.0;
				DecimalFormat fmt = new DecimalFormat("0.00");  
				currentTotalHours = Double.parseDouble(fmt.format(currentTotalHours));
			}							
		}
		
		if (hideClientName == 0) {
			currentClientID = dbManager.getProjectClientID(currentProjectID);
		}
		
		
		if (currentClientID == null || currentEmployeeID == null || currentProjectID == null || currentSelectedDate.equals("") || currentTaskID == null || currentTotalHours == null) {
			Toast.makeText(getApplicationContext(), "You need to fill in all information for time entry (date, task and total hours)", Toast.LENGTH_LONG).show();
		} else {
			// Get a reference to our Description text box
			Editable txtDescription = ((EditText)findViewById(R.id.descriptionEditText)).getEditableText();
						
			TimeEntry newTimeEntry = new TimeEntry();
			newTimeEntry.setProjectID(currentProjectID);
			newTimeEntry.setTaskID(currentTaskID);
			newTimeEntry.setTotalHours(currentTotalHours);
			newTimeEntry.setDate(currentSelectedDate);
			newTimeEntry.setEmployeeID(currentEmployeeID);
			newTimeEntry.setClientID(currentClientID);
			newTimeEntry.setDescription(txtDescription.toString());
			
			final String userToken = SharedPrefsUtil.getStringFromSharedPrefs(getApplicationContext(), SharedPrefsUtil.USER_TOKEN);

			new InsertTimeEntryTask(userToken, newTimeEntry).execute((Object[])null);

		}
	}
	public void onClickPrevious(View v) {
		Logger.d(TAG, "clicked previous");
		horizontalPager.setCurrentScreen(
				horizontalPager.getCurrentScreen() - 1, true);
	}

	public void onClickNext(View v) {
		Logger.d(TAG, "clicked next");
		horizontalPager.setCurrentScreen(
				horizontalPager.getCurrentScreen() + 1, true);
	}


	public void onDateSelected(RelativeLayout cell, String yyyymmdd) {
		Logger.d(TAG, "Date Selected: " + yyyymmdd);
		currentSelectedDate = yyyymmdd;
		onClickNext(null);
	}

}
