package com.createconvertupdates.entities;

import android.graphics.Bitmap;

public class Project {
	private long id;
	private String name;
	private String website;
	private String imagePath;
	private Bitmap image;
	private String slogan;
	private String date;
	private int status;
	
	
	public Project(){
		
	}
	
	public Project(long id, String name,String website, String imagePath, Bitmap image, String slogan,
			String date,
			int status) {
		super();
		this.id = id;
		this.name = name;
		this.website= website;
		this.imagePath = imagePath;
		this.image = image;
		this.slogan = slogan;
		this.status = status;
	}
	public Project(long id2, String name2, String imagePath2, Bitmap image2,
			String slogan2, String date2, int status2) {
		// TODO Auto-generated constructor stub
		this(id2, name2, "", imagePath2, image2, slogan2, date2, status2);
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
		
	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
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
	
	public String toString(){
		return "date = "+ date;
	}
	

}
