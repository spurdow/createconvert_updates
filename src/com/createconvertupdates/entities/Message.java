package com.createconvertupdates.entities;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private long id;
	private String header;
	private int status;
	private List<MessageMetaData> list;
	
	public Message(){}

	public Message(long id, String header, int status,
			List<MessageMetaData> list) {
		super();
		this.id = id;
		this.header = header;
		this.status = status;
		this.list = list;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<MessageMetaData> getList() {
		return list;
	}

	public void setList(List<MessageMetaData> list) {
		this.list = list;
	}
	
	
}
