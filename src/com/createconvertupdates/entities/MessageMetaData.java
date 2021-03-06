package com.createconvertupdates.entities;

public class MessageMetaData {
	
	public final static int MINE = 0x0;
	public final static int ADMIN = 0x01;
	
	private long id;
	private long message_id;
	private long server_message_id;
	private String content;
	private String date;
	private int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private int status;
	public MessageMetaData(long id, long message_id, long server_message_id , 
			String content, String date,int type, int status) {
		super();
		this.id = id;
		this.message_id = message_id;
		this.server_message_id = server_message_id;
		this.content = content;
		this.date = date;
		this.type = type;
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
	public long getServer_message_id() {
		return server_message_id;
	}
	public void setServer_message_id(long server_message_id) {
		this.server_message_id = server_message_id;
	}
	
	
}
