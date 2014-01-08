package com.createconvertupdates.entities;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private long id;
	private long customer_id;
	private int status;
	private List<MessageMetaData> lists;

	public Message(long id , long customer_id , int status){
		this(id , customer_id , status , new ArrayList<MessageMetaData>());
	}
	
	public Message(long id, long customer_id, int status , List<MessageMetaData> lists) {
		super();
		this.id = id;
		this.customer_id = customer_id;
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
	public long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
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
	
}
