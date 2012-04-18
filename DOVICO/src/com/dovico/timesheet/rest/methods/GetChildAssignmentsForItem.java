package com.dovico.timesheet.rest.methods;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.util.Log;

import com.dovico.timesheet.beans.Assignment;
import com.dovico.timesheet.beans.Project;
import com.dovico.timesheet.beans.Task;
import com.dovico.timesheet.rest.RestClient;
import com.dovico.timesheet.rest.methods.enums.RequestMethod;
import com.dovico.timesheet.utils.Logger;
import com.dovico.timesheet.utils.XmlUtils;

/**
 * 
 * @author milos.pesic
 * 
 */
public class GetChildAssignmentsForItem extends RestMethod {

	private static final String TAG = "GetChildAssignmentsForItem";

	private List<Assignment> childAssignments;
	private String parentAssignmentID;
	private int parentAssignmentItemID;
	private Context context;
	private String userToken;
	/**
	 * 
	 * @param service
	 *            RestService
	 * @param context
	 *            of application
	 */
	public GetChildAssignmentsForItem(Context context, String assignmentID, String assignmentURI, String userToken, String restAPIVersion) {
		super(context,userToken, restAPIVersion);
		methodUrl = assignmentURI;
		this.context = context;
		this.userToken = userToken;
		Logger.d(TAG, "methodURL: " + methodUrl);
		
		parentAssignmentID = assignmentID;
		parentAssignmentItemID = Integer.parseInt(assignmentID.substring(1));
		restClient = new RestClient(methodUrl);
	}

	@Override
	protected void doInBackground() {
		try {
			Log.i(TAG, "GetChildAssignmentsForItem background task started");
			super.doInBackground();
			
			childAssignments = new ArrayList<Assignment>();

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
					childAssignments = XmlUtils.parseAssignments(response);
				} catch (IOException e) {
					Logger.e(TAG, "IOExc: " + e);
				}
				
				Log.d(TAG, "Successfully parsed " + childAssignments.size()
						+ " child childAssignments");
				if (childAssignments.size() > 0) {
					
					Logger.d(TAG, "******************Child Assignments**********");
					for (Assignment childAssignment: childAssignments) {
						Logger.d(TAG, "childAssignmentURI: " + childAssignment.getAssignmentURI() + ", childAssignName: " + childAssignment.getName() + ", childAssign Id: " + childAssignment.getAssignmentID() + ", childAssign itemID: " + childAssignment.getItemID());
						
						if (parentAssignmentID.startsWith("P")) {
							
							if (!childAssignment.getAssignmentID().startsWith("G")) {
								Task task = new Task();
								task.setName(childAssignment.getName());
								task.setId(childAssignment.getItemID());
								task.setProjectID(parentAssignmentItemID);
								dbManager.insertNewTask(task);
							} else {
								GetChildAssignmentsForItem newGetChildAssings = new GetChildAssignmentsForItem(context, parentAssignmentID, childAssignment.getAssignmentURI(), userToken, "1");
								newGetChildAssings.execute();
							}
						} else if (parentAssignmentID.startsWith("C")) {
							
							if (!childAssignment.getAssignmentID().startsWith("G")) {
								Project project = new Project();
								project.setClientID(parentAssignmentItemID);
								project.setName(childAssignment.getName());
								project.setId(childAssignment.getItemID());
								project.setAssignmentURI(childAssignment.getAssignmentURI());
								dbManager.insertNewProject(project);
							} else {
								GetChildAssignmentsForItem newGetChildAssings = new GetChildAssignmentsForItem(context, parentAssignmentID, childAssignment.getAssignmentURI(), userToken, "1");
								newGetChildAssings.execute();
							}
						}
						
					}
					
				}
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage(), e);
		} 
		Log.i(TAG, "GetChildAssignmentsForItem background task - end");
	}

}
