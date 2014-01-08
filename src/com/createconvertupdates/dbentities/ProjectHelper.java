package com.createconvertupdates.dbentities;

import java.util.ArrayList;
import java.util.List;

import com.createconvertupdates.entities.Customer;
import com.createconvertupdates.entities.MessageProject;
import com.createconvertupdates.entities.Project;
import com.createconvertupdates.iface.IHelperActions;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.createconvertupdates.commons.Utilities.*;

public class ProjectHelper implements IHelperActions<Project>{

	private final static String TAG = "ProjectHelper";
	
	public final static String TABLE_NAME = "project_table";
	
	public final static String FIELD_ID = "id";
	private final static String FIELD_NAME = "name";
	private final static String FIELD_IMAGE = "image";
	private final static String FIELD_SLOGAN = "slogan";
	private final static String FIELD_DATE = "date";
	private final static String FIELD_STATUS = "status";
	
	public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + 
			" ( " + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + 
			FIELD_NAME + " TEXT , " +
			FIELD_IMAGE + " TEXT , " +
			FIELD_SLOGAN + " TEXT , " +
			FIELD_DATE + "  TEXT," +
			FIELD_STATUS + " INTEGER ) ";
	
	private DBHelper databaseHelper;
	
	public ProjectHelper(Context context) {
		databaseHelper = new DBHelper(context);
	}




	@Override
	public long add(Project p) {
		// TODO Auto-generated method stub
		
		/*
		 *  get sqlite database reference
		 */
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		long id = -1;
		ContentValues values = new ContentValues();
		values.put(FIELD_NAME, p.getName() );
		values.put(FIELD_IMAGE, p.getImagePath());
		values.put(FIELD_SLOGAN, p.getSlogan());
		values.put(FIELD_DATE, p.getDate());
		values.put(FIELD_STATUS, p.getStatus());	
		id = db.insert(TABLE_NAME, null, values);
			
		if(id > 0){
			Log.d(TAG, "added new project");
		}
		db.close();
		return id ;
		
	}


	public Project get(long id) {
		// TODO Auto-generated method stub
		Project p = new Project();
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_ID 
				+ " = ?";
		
		Cursor c = db.rawQuery(selectQuery, new String[]{ String.valueOf(id)});
		
		if(c != null && c.moveToFirst()){
			p.setId(c.getInt(c.getColumnIndex(FIELD_ID)));
			p.setName(c.getString(c.getColumnIndex(FIELD_NAME)));
			p.setImagePath(c.getString(c.getColumnIndex(FIELD_IMAGE)));
			p.setDate(c.getString(c.getColumnIndex(FIELD_DATE)));
			p.setSlogan(c.getString(c.getColumnIndex(FIELD_SLOGAN)));
			p.setStatus(c.getShort(c.getColumnIndex(FIELD_STATUS)));
		}
		c.close();
		db.close();
		
		return p;
	}

	@Override
	public boolean update(long id, Project p) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int affected_rows = 0;			
		ContentValues values = new ContentValues();	
		values.put(FIELD_NAME , p.getName());
		values.put(FIELD_IMAGE, p.getImagePath());
		values.put(FIELD_SLOGAN, p.getSlogan());
		values.put(FIELD_STATUS, p.getStatus());
		affected_rows = db.update(TABLE_NAME, values, FIELD_ID + " = ?", new String[]{ String.valueOf(id)});
		db.close();
		return affected_rows > 0;
	}


	public List<Project> getAll() {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		
		List<Project> projects = new ArrayList<Project>();
		
		String selectSql = "SELECT * FROM " + TABLE_NAME ;
		
		Cursor c = db.rawQuery(selectSql, null);
		
		if(c.moveToFirst()){
			do{
				Project project = new Project();
				project.setId(c.getInt(c.getColumnIndex(FIELD_ID)));
				project.setImagePath(c.getString(c.getColumnIndex(FIELD_IMAGE)));
				project.setName(c.getString(c.getColumnIndex(FIELD_NAME)));
				project.setSlogan(c.getString(c.getColumnIndex(FIELD_SLOGAN)));
				project.setStatus(c.getInt(c.getColumnIndex(FIELD_STATUS)));
				projects.add(project);
			}while(c.moveToNext());
		}
		c.close();
		db.close();
		return projects;
	}
	
	public List<MessageProject> getListAsMessage(){
		List<Project> projects = getAll();
		List<MessageProject> mprojects = new ArrayList<MessageProject>();
		for(Project project : projects){
			MessageProject new_m_project = new MessageProject(project , false);
			mprojects.add(0, new_m_project);
		}
		
		return mprojects;
	}

	@Override
	public Project delete(long id  , Project project) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		int r = db.delete(TABLE_NAME, FIELD_ID + " = ?", new String[]{String.valueOf(id)});
		
		
		return (r > 0 )? project : null;
	}

	public void close() {
		// TODO Auto-generated method stub
		databaseHelper.close();
	}



}
