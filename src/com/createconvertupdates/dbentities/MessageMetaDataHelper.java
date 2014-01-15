package com.createconvertupdates.dbentities;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.createconvertupdates.entities.MessageMetaData;
import com.createconvertupdates.iface.IHelperActions;

public class MessageMetaDataHelper implements IHelperActions<MessageMetaData>{

	
	public final static String TABLE_NAME = "message_metadata_table";
	
	public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	public final static String FIELD_ID = "id";
	public final static String FIELD_MESSAGE_ID = "message_id";
	public final static String FIELD_TITLE = "title";
	public final static String FIELD_CONTENT = "content";
	public final static String FIELD_DATE = "date";
	public final static String FIELD_TYPE = "type";
	public final static String FIELD_STATUS = "status";
	
	public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
			" ( " + 
			FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
			FIELD_MESSAGE_ID + " INTEGER , " + 
			FIELD_TITLE + " TEXT , " + 
			FIELD_CONTENT +  " TEXT , " + 
			FIELD_DATE + " TEXT , " + 
			FIELD_TYPE + " INTEGER , " +
			FIELD_STATUS + " INTEGER  , " + 
			"FOREIGN KEY  ( " +FIELD_MESSAGE_ID + " ) REFERENCES " + MessageHelper.TABLE_NAME + " ( " + MessageHelper.FIELD_ID + " ));";
	
	public final static String ADD_INDEX = "CREATE INDEX " +  TABLE_NAME + "_"+ FIELD_MESSAGE_ID + "_idx ON " + TABLE_NAME + " (" + FIELD_MESSAGE_ID + ")" ;
	
	private DBHelper database;
	
	public MessageMetaDataHelper(Context context){
		database = new DBHelper(context);
	}
	
	
	@Override
	public long add(MessageMetaData object) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = database.getWritableDatabase();
		
		long id = -1;
		
		ContentValues values = new ContentValues();
		values.put(FIELD_MESSAGE_ID, object.getMessage_id());
		values.put(FIELD_TITLE, object.getTitle());
		values.put(FIELD_CONTENT, object.getContent());
		values.put(FIELD_DATE, object.getDate());
		values.put(FIELD_TYPE, object.getType());
		values.put(FIELD_STATUS, object.getStatus());
		
		id = db.insert(TABLE_NAME, null, values);
		
		return id;
	}


	@Override
	public boolean update(long id, MessageMetaData object) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = database.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FIELD_TITLE, object.getTitle());
		values.put(FIELD_CONTENT, object.getContent());
		values.put(FIELD_DATE, object.getDate());
		values.put(FIELD_TYPE, object.getType());
		values.put(FIELD_STATUS, object.getStatus());
		
		int affected_rows = db.update(TABLE_NAME, values, FIELD_ID + " = ? ", new String[]{String.valueOf(id)});
		
		return affected_rows > 0;
	}


	@Override
	public MessageMetaData delete(long id, MessageMetaData object) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db = database.getWritableDatabase();
		
		int affected_rows  = db.delete(TABLE_NAME, FIELD_ID + " = ?", new String[]{String.valueOf(id)});
		
		return (affected_rows > 0 ) ? object : null;
	}

	
	public MessageMetaData get(long id , long message_id ){
		MessageMetaData m_mdata = new MessageMetaData();
		
		SQLiteDatabase db = database.getReadableDatabase();
		
		String select_query = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_ID + " = ? AND " + FIELD_MESSAGE_ID + " = ?" ;
		
		Cursor cursor = db.rawQuery(select_query, new String[]{String.valueOf(id) , String.valueOf(message_id)});
		
		
		if(cursor != null && cursor.moveToFirst()){
			long mid = cursor.getLong(cursor.getColumnIndex(FIELD_ID));
			long mmid = cursor.getLong(cursor.getColumnIndex(FIELD_MESSAGE_ID));
			String title = cursor.getString(cursor.getColumnIndex(FIELD_TITLE));
			String content = cursor.getString(cursor.getColumnIndex(FIELD_CONTENT));
			String date = cursor.getString(cursor.getColumnIndex(FIELD_DATE));
			int type = cursor.getInt(cursor.getColumnIndex(FIELD_TYPE));
			int status = cursor.getInt(cursor.getColumnIndex(FIELD_STATUS));
			
			m_mdata.setId(mid);
			m_mdata.setMessage_id(mmid);
			m_mdata.setTitle(title);
			m_mdata.setContent(content);
			m_mdata.setDate(date);
			m_mdata.setType(type);
			m_mdata.setStatus(status);
		}
		
		return m_mdata;
	}
	
	public List<MessageMetaData> getAll(long message_id){
		List<MessageMetaData> lists = new ArrayList<MessageMetaData>();
		
		SQLiteDatabase db = database.getReadableDatabase();
		
		String q = "SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_MESSAGE_ID + " = ?";
		
		Cursor c = db.rawQuery(q, new String[]{String.valueOf(message_id)});
		
		if(c!=null && c.moveToFirst()){
			do{
				long id = c.getLong(c.getColumnIndex(FIELD_ID));
				long m_id = c.getLong(c.getColumnIndex(FIELD_MESSAGE_ID));
				String title  = c.getString(c.getColumnIndex(FIELD_TITLE));
				String content = c.getString(c.getColumnIndex(FIELD_CONTENT));
				String date = c.getString(c.getColumnIndex(FIELD_DATE));
				int type = c.getInt(c.getColumnIndex(FIELD_TYPE));
				int status = c.getInt(c.getColumnIndex(FIELD_STATUS));
				
				lists.add(new MessageMetaData(id , m_id , title , content , date, type, status));
				
			}while(c.moveToNext());
		}
		
		return lists;
		
	}
	

}
