package com.createconvertupdates.entities;

public class MessageMetaData {
	private long id;
	private long message_id;
	private String title;
	private String content;
	private String date;
	private String status;
	public MessageMetaData(long id, long message_id, String title,
			String content, String date, String status) {
		super();
		this.id = id;
		this.message_id = message_id;
		this.title = title;
		this.content = content;
		this.date = date;
		this.status = status;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
