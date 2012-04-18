package com.dovico.timesheet.rest.methods;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.util.Log;

import com.dovico.timesheet.beans.Client;
import com.dovico.timesheet.beans.Employee;
import com.dovico.timesheet.common.DOVICORestAPI;
import com.dovico.timesheet.rest.RestClient;
import com.dovico.timesheet.rest.methods.enums.RequestMethod;
import com.dovico.timesheet.utils.Logger;
import com.dovico.timesheet.utils.SharedPrefsUtil;
import com.dovico.timesheet.utils.XmlUtils;

/**
 * 
 * @author milos.pesic
 * 
 */
public class GetEmployeeInfo extends RestMethod {

	private static final String TAG = "GetEmployeeInfo";

	private Employee employee;

	private Context context;
	/**
	 * 
	 * @param service
	 *            RestService
	 * @param context
	 *            of application
	 */
	public GetEmployeeInfo(Context context, String userToken, String restAPIVersion) {
		super(context,userToken, restAPIVersion);
		this.context = context;
		Logger.d(TAG, "Retrieveing employeeInfo for token: " + userToken);
		methodUrl = DOVICORestAPI.RestURL.GET_EMPLOYEE_INFO + restAPIVersion;
		restClient = new RestClient(methodUrl);
	}

	@Override
	protected void doInBackground() {
		try {
			Log.i(TAG, "GetEmployeeInfo background task started");
			super.doInBackground();
			
			employee = new Employee();

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
					employee = XmlUtils.parseEmployee(response);
					SharedPrefsUtil.putIntToSharedPrefs(context, SharedPrefsUtil.EMPLOYEE_ID, employee.getId());
				} catch (IOException e) {
					Logger.e(TAG, "IOExc: " + e);
				}
				
				Log.d(TAG, "Successfully parsed employee with id: " + employee.getId());
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage(), e);
		} 
		Log.i(TAG, "GetEmployeeInfo background task - end");
	}

}
