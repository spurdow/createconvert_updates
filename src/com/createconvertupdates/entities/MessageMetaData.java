package com.createconvertupdates.entities;

public class MessageMetaData {
	private long id;
	private long message_id;
	private String title;
	private String content;
	private String date;
	private int status;
	public MessageMetaData(long id, long message_id, String title,
			String content, String date, int status) {
		super();
		this.id = id;
		this.message_id = message_id;
		this.title = title;
		this.content = content;
		this.date = date;
		this.status = status;
	}
	public MessageMetaData() {
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMessage_id() {
		return message_id;
	}
	public void setMessage_id(long message_id) {
		this.message_id = message_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
