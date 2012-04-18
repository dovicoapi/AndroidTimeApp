package com.dovico.timesheet.rest.methods;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.util.Log;

import com.dovico.timesheet.common.DOVICORestAPI;
import com.dovico.timesheet.rest.RestClient;
import com.dovico.timesheet.rest.methods.enums.RequestMethod;
import com.dovico.timesheet.utils.Logger;

/**
 * 
 * @author milos.pesic
 * 
 */
public class DeleteTimeEntry extends RestMethod {

	private static final String TAG = "DeleteTimeEntry";

	/**
	 * 
	 * @param service
	 *            RestService
	 * @param context
	 *            of application
	 */
	public DeleteTimeEntry(Context context, String timeEntryID, String userToken, String restAPIVersion) {
		super(context,userToken, restAPIVersion);
		methodUrl = DOVICORestAPI.RestURL.DELETE_TIME_ENTRY + timeEntryID + DOVICORestAPI.RestURL.DOVICO_API_VERSION + restAPIVersion;
		Logger.d(TAG, "deleteTimeEntry final url:" + methodUrl);
		restClient = new RestClient(methodUrl);
	}

	@Override
	protected void doInBackground() {
		try {
			Logger.i(TAG, "DeleteTimeEntry background task started");
			super.doInBackground();

			//restClient.addHeader("Content-Type", "text/xml");
			restClient.execute(RequestMethod.DELETE);

			if (restClient.getResponseCode() != HttpStatus.SC_OK) {
				Logger.d(TAG, "Response code: " + restClient.getResponseCode());
				Logger.d(TAG, "Response message: " + restClient.getMessage());

				return;
			}

			String response = restClient.getResponse();
			Logger.d(TAG, "Response code: " + restClient.getResponseCode() + ", response: " + response);

		} catch (UnsupportedEncodingException e) {
			Logger.e(TAG, e.getMessage());
		} 
		Logger.i(TAG, "DeleteTimeEntry background task - end");
	}

}
