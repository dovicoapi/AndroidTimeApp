package com.dovico.timesheet.rest.methods;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import android.content.Context;
import android.util.Log;

import com.dovico.timesheet.common.DOVICORestAPI;
import com.dovico.timesheet.rest.RestClient;
import com.dovico.timesheet.rest.methods.enums.RequestMethod;
import com.dovico.timesheet.utils.Logger;
import com.dovico.timesheet.utils.SharedPrefsUtil;
import com.dovico.timesheet.utils.XmlUtils;
import com.dovico.timesheet.utils.XmlUtils.XMLTags;

public class Authenticate extends RestMethod {
	private static final String TAG = "Authenticate";
	private Context m_Context;
	
	private String m_sAuthenticateXml;
	/**
	 * 
	 * @param service
	 *            RestService
	 * @param context
	 *            of application
	 */
	public Authenticate(Context context, String sCompany, String sUserId, String sPassword) {
		super(context, "", "2");
		
		m_Context = context;
		methodUrl = DOVICORestAPI.RestURL.AUTHENTICATE + this.restAPIVersion;
		
		m_sAuthenticateXml = "<UserInfo><CompanyName>" + this.encodeTextForElement(sCompany) + "</CompanyName>";
		m_sAuthenticateXml += "<UserName>" + this.encodeTextForElement(sUserId) + "</UserName>";
		m_sAuthenticateXml += "<Password>" + this.encodeTextForElement(sPassword) + "</Password></UserInfo>";
		
		Logger.d(TAG, "Authenticate final url:" + methodUrl);
		restClient = new RestClient(methodUrl);
	}

	@Override
	protected void doInBackground() {
		try {
			Logger.i(TAG, "Authenticate background task started");
			super.doInBackground();

			
			
			restClient.setEntity(new StringEntity(m_sAuthenticateXml));
			restClient.addHeader("Content-Type", "text/xml");
			restClient.execute(RequestMethod.POST);

			if (restClient.getResponseCode() != HttpStatus.SC_OK) {
				Logger.d(TAG, "Response code: " + restClient.getResponseCode());
				Logger.d(TAG, "Response message: " + restClient.getMessage());

				return;
			}

			String sResponse = restClient.getResponse();
			Logger.d(TAG, "Response code: " + restClient.getResponseCode() + ", response: " + sResponse);
			
			userToken = XmlUtils.extractTagValue(sResponse, "</DataAccessToken>", sResponse.indexOf("<DataAccessToken>"));
			
			SharedPrefsUtil.putStringToSharedPrefs(m_Context, SharedPrefsUtil.USER_TOKEN, userToken);

		} catch (UnsupportedEncodingException e) {
			Logger.e(TAG, e.getMessage());
		} 
		Logger.i(TAG, "Authenticate background task - end");
	}

}
