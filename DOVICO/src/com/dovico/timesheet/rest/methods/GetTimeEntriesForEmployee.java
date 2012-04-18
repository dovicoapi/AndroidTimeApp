package com.dovico.timesheet.rest.methods;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.util.Log;

import com.dovico.timesheet.beans.TimeEntry;
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
	public GetTimeEntriesForEmployee(Context context, String employeeID, String userToken, String restAPIVersion) {
		super(context, userToken, restAPIVersion);
		methodUrl = DOVICORestAPI.RestURL.GET_TIME_ENTRIES + employeeID + DOVICORestAPI.RestURL.DOVICO_API_VERSION + restAPIVersion;
		restClient = new RestClient(methodUrl);
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
