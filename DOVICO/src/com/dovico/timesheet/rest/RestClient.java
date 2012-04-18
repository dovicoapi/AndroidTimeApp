package com.dovico.timesheet.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.dovico.timesheet.rest.methods.enums.RequestMethod;
import com.dovico.timesheet.utils.Logger;


/**
 * Class which has useful methods for communicating with web services
 * 
 * 
 */
public class RestClient {

	private static final String TAG = "RestClient";
	private static final int TIMEOUT = 9000;
	private List<NameValuePair> params;
	private List<NameValuePair> headers;
	private HttpEntity entity;
	private String url;

	private int responseCode;
	private String message;

	private String response;

	/**
	 * 
	 * @param url for RestClient
	 */
	public RestClient(String url) {
		Log.d(TAG, "Initiating RestClient with url: " + url);
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	/**
	 * 
	 * @return String response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * 
	 * @return String error message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @return int Response Code
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * 
	 * @param name of parameter
	 * @param value of parameter
	 */
	public void addParam(String name, String value) {
		Log.d(TAG, "Adding parameter name = " + name + ", value = " + value);
		params.add(new BasicNameValuePair(name, value));
	}

	/**
	 * 
	 * @param name of header
	 * @param value in header
	 */
	public void addHeader(String name, String value) {
		Logger.d(TAG, "headerValue: " + value);
		headers.add(new BasicNameValuePair(name, value));
	}

	/**
	 * 
	 * @param method http
	 * @throws UnsupportedEncodingException
	 *             ...
	 */
	public void execute(RequestMethod method) throws UnsupportedEncodingException {
		
		switch (method) {
		case GET:
			// add parameters
			String combinedParams = "";
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					if (p != null) {
						String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), HTTP.UTF_8);
						if (combinedParams.length() > 1) {
							combinedParams += "&" + paramString;
						} else {
							combinedParams += paramString;
						}
					}
				}
			}
			HttpGet requestGET = new HttpGet(url + combinedParams);

			// add headers
			for (NameValuePair h : headers) {
				requestGET.addHeader(h.getName(), h.getValue());
			}

			executeRequest(requestGET);
			break;

		case POST:
			HttpPost requestPost = new HttpPost(url);

			// add headers
			for (NameValuePair h : headers) {
				requestPost.addHeader(h.getName(), h.getValue());
			}

			if (!params.isEmpty()) {
				requestPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			if (entity != null) {
				requestPost.setEntity(entity);
			}			

			
			executeRequest(requestPost);

			break;
		case DELETE:
			HttpDelete requestDelete = new HttpDelete(url);

			// add headers
			for (NameValuePair h : headers) {
				requestDelete.addHeader(h.getName(), h.getValue());
			}		
			
			executeRequest(requestDelete);

			break;
		default:
			break;
		}
	}

	private void executeRequest(HttpUriRequest request) {
		Logger.d(TAG, "URI: " + request.getURI());
		Logger.d(TAG, "request line: " + request.getRequestLine());
		Logger.d(TAG, "request method: " + request.getMethod());
		
		for (Header header: request.getAllHeaders()) {
			Logger.d(TAG, "header name: " + header.getName() + ", header value: " + header.getValue());
		}
		
				
		HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpParams);		

		HttpResponse httpResponse;

		try {
			httpResponse = httpclient.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);
			
				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e) {
			Logger.e(TAG, "ClientProtocolException: " + e);
		} catch (IOException e) {
			Logger.e(TAG, "IOException: " + e);
		} catch (Exception e) {
			Logger.e(TAG, "Exception: " + e);
		} finally {
			Logger.d(TAG, "finished");
			if (httpclient != null) {
				httpclient.getConnectionManager().shutdown();
			}			
//			messageIntent = communicator
			
		}
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.e(TAG, "background task - end", e);
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}
}
