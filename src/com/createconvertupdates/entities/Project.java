package com.createconvertupdates.entities;

import android.graphics.Bitmap;

public class Project {
	private long id;
	private String name;
	private String imagePath;
	private Bitmap image;
	private String slogan;
	private String date;
	private int status;
	
	
	public Project(){
		
	}
	
	public Project(long id, String name, String imagePath, Bitmap image, String slogan,
			String date,
			int status) {
		super();
		this.id = id;
		this.name = name;
		this.imagePath = imagePath;
		this.image = image;
		this.slogan = slogan;
		this.status = status;
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
	
	

}
