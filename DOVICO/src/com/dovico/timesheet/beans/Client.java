package com.dovico.timesheet.beans;

public class Client {
	
	private int id;
	private String name;	
	private String contact;	
	private String email;
	private String assignmentURI;
	
	public String getAssignmentURI() {
		return assignmentURI;
	}
	public void setAssignmentURI(String assignmentURI) {
		this.assignmentURI = assignmentURI;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
