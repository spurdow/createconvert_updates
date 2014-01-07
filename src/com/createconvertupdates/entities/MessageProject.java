package com.createconvertupdates.entities;

import android.graphics.Bitmap;

public class MessageProject extends Project{
	private boolean isCheck;

	public MessageProject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MessageProject(long id, String name, String imagePath, Bitmap image,
			String slogan, String date, int status , boolean isCheck) {
		super(id, name, imagePath, image, slogan, date, status);
		// TODO Auto-generated constructor stub
		this.isCheck = isCheck;
	}
	
	public MessageProject(Project project , boolean isCheck){
		this(project.getId() , project.getName()  , project.getImagePath(), project.getImage(),
				project.getSlogan(),  project.getDate() , project.getStatus() , isCheck);
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	
	
	
}
