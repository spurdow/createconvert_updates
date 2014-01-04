package com.createconvertupdates.entities;

public class Customer {
	
	public final static String FIRSTNAME = "firstname";
	public final static String LASTNAME = "lastname";
	public final static String MIDDLENAME = "middlename";
	public final static String REGID  = "regid";
	public final static String USERNAME = "username";
	public final static String PASSWORD = "password";
	public final static String EMAIL = "email";
	
	private int id;
	private String firstname;
	private String lastname;
	private String middlename;
	private String regid;
	private String username;
	private char[] password;
	private String email;
	private String date_last_login;
	private String date_created;
	private int status;
	
	public Customer(){}
	
	public Customer(int id, String firstname, String lastname,
			String middlename, String regid, String username, char[] password,
			String email, String date_last_login, String date_created,
			int status) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.middlename = middlename;
		this.regid = regid;
		this.username = username;
		this.password = password;
		this.email = email;
		this.date_last_login = date_last_login;
		this.date_created = date_created;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public String getRegid() {
		return regid;
	}
	public void setRegid(String regid) {
		this.regid = regid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		String pass = "";
		for(char i : password){
			pass+=i;
		}
		return pass;
		
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDate_last_login() {
		return date_last_login;
	}
	public void setDate_last_login(String date_last_login) {
		this.date_last_login = date_last_login;
	}
	public String getDate_created() {
		return date_created;
	}
	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
