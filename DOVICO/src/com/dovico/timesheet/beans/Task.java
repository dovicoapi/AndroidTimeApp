package com.dovico.timesheet.beans;

public class Task {
	
	private int id;
	private String name;
	private String description;	
	private int projectID;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectId) {
		this.projectID = projectId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
