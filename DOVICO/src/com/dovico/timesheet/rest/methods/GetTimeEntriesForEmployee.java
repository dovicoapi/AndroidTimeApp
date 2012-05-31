package com.dovico.timesheet.rest.methods;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.util.Log;

import com.dovico.timesheet.beans.TimeEntry;
import com.dovico.timesheet.common.DOVICOConst;
import com.dovico.timesheet.common.DOVICORestAPI;
import com.dovico.timesheet.common.TimeEntryComparatorByDate;
import com.dovico.timesheet.rest.RestClient;
import com.dovico.timesheet.rest.methods.enums.RequestMethod;
import com.dovico.timesheet.utils.Logger;
import com.dovico.timesheet.utils.XmlUtils;

/**
 * 
 * @author milos.pesic
 * 
 */
public class GetTimeEntriesForEmployee extends RestMethod {

	private static final String TAG = "GetTimeEntriesForEmployee";

	private List<TimeEntry> timeEntries;
	/**
	 * 
	 * @param service
	 *            RestService
	 * @param context
	 *            of application
	 */
	/// <history>
	/// <modified author="C. Gerard Gallant" date="2012-05-31" reason="Fixed a bug where the URI was requesting ALL time entries for the person rather than just the time entries within the expected date range (depending on how long the person has been tracking their time with DOVICO software, the list of time entries could be huge)"/>
	/// </history>
	public GetTimeEntriesForEmployee(Context context, String employeeID, String userToken, String restAPIVersion) {
		super(context, userToken, restAPIVersion);

		// The end date for the time entries we want is today. The Start date is 6 days before today
		Date dtEndDate = new Date();
		Date dtStartDate = new Date();
		dtStartDate.setTime(dtEndDate.getTime() - (1000 * 60 * 60 *24 * 6)); // 1000 milliseconds per second x 60 seconds per minute x 60 minutes per hour x 24 hours per day x 6 days  
		
		// Build the Date range query string
		String sDateRangeQueryString = buildDateRangeQueryString(dtStartDate, dtEndDate); 

		// Build up the URI needed to request the user's time entries
		methodUrl = (DOVICORestAPI.RestURL.GET_TIME_ENTRIES + employeeID + DOVICORestAPI.RestURL.DOVICO_API_VERSION + restAPIVersion + "&" + sDateRangeQueryString);
		restClient = new RestClient(methodUrl);
	}
	
	
	/// <history>
	/// <modified author="C. Gerard Gallant" date="2012-05-31" reason="Copied from the common library"/>
	/// </history>
	// Helper to return a Date Range query string
	private String buildDateRangeQueryString(Date dtDateRangeStart, Date dtDateRangeEnd){
		// Create a Date formatter object that will turn a date into the XML Date Format string expected by the API
		SimpleDateFormat fFormatter = new SimpleDateFormat(DOVICOConst.XML_DATE_FORMAT);
		
		// Return the date range query string with an encoded space between both dates
		return ("daterange=" + fFormatter.format(dtDateRangeStart) + "%20" + fFormatter.format(dtDateRangeEnd));
	}

	
	@Override
	protected void doInBackground() {
		try {
			Log.i(TAG, "GetTimeEntriesForEmployee background task started");
			super.doInBackground();
			
			timeEntries = new ArrayList<TimeEntry>();

			restClient.execute(RequestMethod.GET);

			if (restClient.getResponseCode() != HttpStatus.SC_OK) {
				Logger.d(TAG, "Response code: " + restClient.getResponseCode());
				Logger.d(TAG, "Response message: " + restClient.getMessage());

				return;
			}

			String response = restClient.getResponse();
				Logger.d(TAG, response);

			if (response != null) {
				try {
					timeEntries = XmlUtils.parseTimeEntries(response, new Date());
				} catch (IOException e) {
					Logger.e(TAG, "IOExc: " + e);
				}
				
				Logger.d(TAG, "Successfully parsed " + timeEntries.size()
						+ " timeEntries");
				if (timeEntries.size() > 0) {	
					
					Collections.sort(timeEntries, new TimeEntryComparatorByDate());
					
					dbManager.insertTimeEntries(timeEntries);
				}
			}
		} catch (UnsupportedEncodingException e) {
			Logger.e(TAG, "Exception: " + e);
		} 
		Logger.i(TAG, "GetTimeEntriesForEmployee background task - end");
	}


}
