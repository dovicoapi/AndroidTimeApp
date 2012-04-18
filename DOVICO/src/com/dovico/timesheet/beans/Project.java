package com.dovico.timesheet.beans;

public class Project {
	
	private int id;
	private int clientID;
	private int leaderID;
	private String name;
	private String status;
	private String start_date;
	private String end_date;
	private String billing_by;
	private String fixed_cost;
	private int currencyID;
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
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public int getLeaderID() {
		return leaderID;
	}
	public void setLeaderID(int leaderID) {
		this.leaderID = leaderID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getBilling_by() {
		return billing_by;
	}
	public void setBilling_by(String billing_by) {
		this.billing_by = billing_by;
	}
	public String getFixed_cost() {
		return fixed_cost;
	}
	public void setFixed_cost(String fixed_cost) {
		this.fixed_cost = fixed_cost;
	}
	public int getCurrencyID() {
		return currencyID;
	}
	public void setCurrencyID(int currencyID) {
		this.currencyID = currencyID;
	}

}
