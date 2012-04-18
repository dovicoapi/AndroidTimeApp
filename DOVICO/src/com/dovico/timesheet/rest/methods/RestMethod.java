package com.dovico.timesheet.rest.methods;

import java.net.URLEncoder;

import android.content.Context;

import com.dovico.timesheet.common.DOVICOConst;
import com.dovico.timesheet.db.DbManager;
import com.dovico.timesheet.db.DbManagerImpl;
import com.dovico.timesheet.rest.RestClient;

/**
 * Base abstract REST method which all other methods need to extend runs in a
 * separate thread
 * 
 * @author milos.pesic
 * 
 */
public abstract class RestMethod {
	
	private static final String TAG = "RestMethod";
	
	protected static final String ACCESS_TOKEN = "access_token";
	protected static final String USER_TOKEN = "user_token";

	protected static final String MESSAGE = "message";
	protected static final String STATUS_CODE = "statusCode";
	protected static final String WEBSERVICE_RESPONSE = "webserviceResponse";

	protected static String methodUrl;

	protected static final String MESSAGE_NOT_RECEIVED = "Message not received";

	protected DbManager dbManager;

	protected int responseCode;
	protected String message;
	
	protected String userToken;
	protected String restAPIVersion;
	protected RestClient restClient;

	/**
	 * 
	 * @param accessToken
	 *            accesToken
	 * @param context
	 *            of application
	 * @param restAPIVersion
	 */
	public RestMethod(Context context, String userToken, String restAPIVersion) {
		this.userToken = userToken;
		this.restAPIVersion = restAPIVersion;
		dbManager = DbManagerImpl.getInstance(context);
	}
	

	/**
	 * executes REST on a new tread method
	 */
	public void execute() {
		doInBackground();
	}

	/**
	 * executing REST method
	 */
	protected void doInBackground() {
		restClient.addHeader("Authorization", "WRAP access_token=\"client=" + URLEncoder.encode(DOVICOConst.DEVELOPERS_CONSUMER_SECRET_API_TOKEN) +
                "&user_token=" + URLEncoder.encode(userToken) + "\"");
	}
	
	public int getResponseCode() {
		return restClient.getResponseCode();
	
	}
	
	public String getMessage() {
		return restClient.getMessage();
	
	}

}
