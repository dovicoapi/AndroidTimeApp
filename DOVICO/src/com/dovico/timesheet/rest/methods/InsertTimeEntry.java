package com.dovico.timesheet.rest.methods;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.util.Log;

import com.dovico.timesheet.beans.TimeEntry;
import com.dovico.timesheet.common.DOVICORestAPI;
import com.dovico.timesheet.rest.RestClient;
import com.dovico.timesheet.rest.methods.enums.RequestMethod;
import com.dovico.timesheet.utils.Logger;

/**
 * 
 * @author milos.pesic
 * 
 */
public class InsertTimeEntry extends RestMethod {

	
	private static final String TAG = "InsertTimeEntry";

	private TimeEntry timeEntry;
	/**
	 * 
	 * @param service
	 *            RestService
	 * @param context
	 *            of application
	 */
	public InsertTimeEntry(Context context, TimeEntry timeEntry, String userToken, String restAPIVersion) {
		super(context,userToken, restAPIVersion);
		this.timeEntry = timeEntry;
		methodUrl = DOVICORestAPI.RestURL.INSERT_TIME_ENTRY + restAPIVersion;
		Logger.d(TAG, "get client final url:" + methodUrl);
		restClient = new RestClient(methodUrl);
	}

	@Override
	protected void doInBackground() {
		try {
			Log.i(TAG, "InsertTimeEntry background task started");
			super.doInBackground();

			StringEntity stringEntity = new StringEntity(prepareTimeEntryForSubmitting(timeEntry));
			restClient.setEntity(stringEntity);
			restClient.addHeader("Content-Type", "text/xml");

			restClient.execute(RequestMethod.POST);

			if (restClient.getResponseCode() != HttpStatus.SC_OK) {
				Logger.d(TAG, "Response code: " + restClient.getResponseCode());
				Logger.d(TAG, "Response message: " + restClient.getMessage());

				return;
			}

			String response = restClient.getResponse();
			Logger.d(TAG, response);

		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage(), e);
		} 
		Log.i(TAG, "InsertTimeEntry background task - end");
	}

	/**
	 * @param timeEntry
	 * @return 	//<TimeEntries><TimeEntry><ProjectID>1297</ProjectID><TaskID>4917</TaskID><EmployeeID>111</EmployeeID>
	//<Date>2011-06-01</Date><StartTime>0800</StartTime><StopTime>0900</StopTime><TotalHours>1</TotalHours><Description>a description</Description></TimeEntry></TimeEntries>
	 */
	private String prepareTimeEntryForSubmitting(TimeEntry timeEntry) {
		
		StringBuilder sb = new StringBuilder(300);
		sb.append("<TimeEntries>").append("<TimeEntry>");
		sb.append("<ProjectID>" + timeEntry.getProjectID() + "</ProjectID>");
		sb.append("<TaskID>" + timeEntry.getTaskID() + "</TaskID>");
		sb.append("<EmployeeID>" + timeEntry.getEmployeeID() + "</EmployeeID>");
		sb.append("<Date>" + timeEntry.getDate() + "</Date>");
		sb.append("<TotalHours>" + timeEntry.getTotalHours() + "</TotalHours>");
		sb.append("<Description>" + encodeTextForElement(timeEntry.getDescription()) + "</Description>");
		sb.append("</TimeEntry>").append("</TimeEntries>");
		
		String timeEntryAsString = sb.toString();
		Logger.d(TAG, "timeEntryAsString: " + timeEntryAsString);
		
		return timeEntryAsString;
	}

}
