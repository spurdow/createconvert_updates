package com.createconvertupdates.entities;

public class ProjectMetaData {
	private int id;
	private int project_id;
	private String update_message;
	private String date_received;
	private int status;
	
	public ProjectMetaData(int id, int project_id, String update_message,
			String date_received, int status) {
		super();
		this.id = id;
		this.project_id = project_id;
		this.update_message = update_message;
		this.date_received = date_received;
		this.status = status;
	}
	
	public ProjectMetaData(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getUpdate_message() {
		return update_message;
	}

	public void setUpdate_message(String update_message) {
		this.update_message = update_message;
	}

	public String getDate_received() {
		return date_received;
	}

	public void setDate_received(String date_received) {
		this.date_received = date_received;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
