package com.createconvertupdates.dbentities;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.createconvertupdates.entities.ProjectMetaData;
import com.createconvertupdates.iface.IHelperActions;
import static com.createconvertupdates.commons.Utilities.*;


public class ProjectMetaDataHelper extends SQLiteOpenHelper implements IHelperActions<ProjectMetaData>{

	public final static String TABLE_NAME = "project_metadata_table";	
	
	private final static String FIELD_ID = "id";
	private final static String FIELD_PROJECT_ID = "project_id";
	private final static String FIELD_UPDATE_MESSAGE = "update_message";
	private final static String FIELD_DATE = "date_received";
	private final static String FIELD_STATUS = "status";
	
	private final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	private final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + 
			" ( " + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
			FIELD_PROJECT_ID + " INTEGER ," +
			FIELD_UPDATE_MESSAGE + " TEXT ," +
			FIELD_DATE + " TEXT ," +
			FIELD_STATUS + " INTEGER , " +
			"FOREIGN KEY  ( " + FIELD_PROJECT_ID + " ) REFERENCES " + ProjectHelper.TABLE_NAME + " ( " + ProjectHelper.FIELD_ID +" ));";
	
	
	public ProjectMetaDataHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}
	
	

	@Override
	public long add(ProjectMetaData object) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db = getWritableDatabase();
		
		long id = -1;
		
		ContentValues values = new ContentValues();
		values.put(FIELD_PROJECT_ID, object.getProject_id());
		values.put(FIELD_UPDATE_MESSAGE, object.getUpdate_message());
		values.put(FIELD_DATE, object.getDate_received());
		values.put(FIELD_STATUS, object.getStatus());
			
		id = db.insert(TABLE_NAME, null, values);
		db.close();
		return id;
	}

	@Override
	public ProjectMetaData get(long id) {
		// TODO Auto-generated method stub
		ProjectMetaData projectmetadata = new ProjectMetaData();
		
		SQLiteDatabase db = getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " +TABLE_NAME +" WHERE " + FIELD_ID + " = ?" ;
		
		Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
		
		if(c!=null && c.moveToFirst()){
			projectmetadata.setId(c.getInt(c.getColumnIndex(FIELD_ID)));
			projectmetadata.setProject_id(c.getInt(c.getColumnIndex(FIELD_PROJECT_ID)));
			projectmetadata.setUpdate_message(c.getString(c.getColumnIndex(FIELD_UPDATE_MESSAGE)));
			projectmetadata.setDate_received(c.getString(c.getColumnIndex(FIELD_DATE)));
			projectmetadata.setStatus(c.getInt(c.getColumnIndex(FIELD_STATUS)));
		}
		c.close();
		db.close();
		
		return projectmetadata;
	}

	@Override
	public boolean update(long id, ProjectMetaData object) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		int affected_rows = 0;
		
		ContentValues values = new ContentValues();
		
		values.put(FIELD_UPDATE_MESSAGE, object.getUpdate_message());
		values.put(FIELD_DATE, object.getDate_received());
		values.put(FIELD_STATUS, object.getStatus());
		
		affected_rows = db.update(TABLE_NAME, values, FIELD_ID + " = ?", new String[]{String.valueOf(id)});
		
		db.close();
		
		return affected_rows > 0;
		
	}

	@Override
	public ProjectMetaData delete(long id, ProjectMetaData object) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getWritableDatabase();
		
		int affected_rows = db.delete(TABLE_NAME, FIELD_ID + " = ?", new String[]{String.valueOf(id)});
		
		return (affected_rows > 0)? object : null;
	}

	@Override
	public List<ProjectMetaData> getAll() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getReadableDatabase();
		List<ProjectMetaData> list = new ArrayList<ProjectMetaData>();
		
		String sql = "SELECT * FROM " + TABLE_NAME;
		
		Cursor c = db.rawQuery(sql, null);
		
		if(c.moveToFirst()){
			do{
				ProjectMetaData pmd = new ProjectMetaData();
				pmd.setId(c.getInt(c.getColumnIndex(FIELD_ID)));
				pmd.setProject_id(c.getInt(c.getColumnIndex(FIELD_PROJECT_ID)));
				pmd.setUpdate_message(c.getString(c.getColumnIndex(FIELD_UPDATE_MESSAGE)));
				pmd.setDate_received(c.getString(c.getColumnIndex(FIELD_DATE)));
				pmd.setStatus(c.getInt(c.getColumnIndex(FIELD_STATUS)));
				list.add(pmd);
			}while(c.moveToNext());
		}
		c.close();
		db.close();
		
		
		return list;
		
	}


}
