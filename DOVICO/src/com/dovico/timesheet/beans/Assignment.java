package com.dovico.timesheet.beans;

public class Assignment {
	
	private String assignmentID;
	private int itemID;
	private String name;
	private String assignmentURI;

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getAssignmentID() {
		return assignmentID;
	}

	public void setAssignmentID(String assignmentID) {
		this.assignmentID = assignmentID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssignmentURI() {
		return assignmentURI;
	}

	public void setAssignmentURI(String assignmentURI) {
		this.assignmentURI = assignmentURI;
	} 
	
}
