package com.dovico.timesheet.rest.methods;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.util.Log;

import com.dovico.timesheet.beans.Assignment;
import com.dovico.timesheet.common.DOVICORestAPI;
import com.dovico.timesheet.rest.RestClient;
import com.dovico.timesheet.rest.methods.enums.RequestMethod;
import com.dovico.timesheet.utils.Logger;
import com.dovico.timesheet.utils.XmlUtils;

/**
 * 
 * @author milos.pesic
 * 
 */
public class GetAssignmentsForEmployee extends RestMethod {

	private static final String TAG = "GetAssignmentsForEmployee";

	private List<Assignment> assignments;
	/**
	 * 
	 * @param service
	 *            RestService
	 * @param context
	 *            of application
	 */
	public GetAssignmentsForEmployee(Context context, String employeeID, String userToken, String restAPIVersion) {
		super(context,userToken, restAPIVersion);
		methodUrl = DOVICORestAPI.RestURL.GET_ASSIGNMENTS_FOR_EMPLOYEE + employeeID + DOVICORestAPI.RestURL.DOVICO_API_VERSION + restAPIVersion;
		Logger.d(TAG, "finalURL: " + methodUrl);
		restClient = new RestClient(methodUrl);
	}

	@Override
	protected void doInBackground() {
		try {
			Log.i(TAG, "GetAssignmentsForEmployee background task started");
			super.doInBackground();
			
			assignments = new ArrayList<Assignment>();

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
					assignments = XmlUtils.parseAssignments(response);
				} catch (IOException e) {
					Logger.e(TAG, "IOExc: " + e);
				}
				
				Log.d(TAG, "Successfully parsed " + assignments.size()
						+ "assignments");
				if (assignments.size() > 0) {
					dbManager.insertAssignments(assignments);
				}
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage(), e);
		} 
		Log.i(TAG, "GetAssignmentsForEmployee background task - end");
	}

}
