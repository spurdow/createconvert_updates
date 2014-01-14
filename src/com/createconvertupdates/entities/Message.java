package com.createconvertupdates.entities;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private long id;
	private String header;
	private int status;
	private List<MessageMetaData> lists;

	public Message(long id , String header , int status){
		this(id ,header , status , new ArrayList<MessageMetaData>());
	}
	
	public Message(long id, String header, int status , List<MessageMetaData> lists) {
		super();
		this.id = id;
		this.setHeader(header);
		this.status = status;
		this.lists = lists;
	}
	public Message() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<MessageMetaData> getLists() {
		return lists;
	}
	public void setLists(List<MessageMetaData> lists) {
		this.lists = lists;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
}
