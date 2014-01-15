package com.createconvertupdates.dbentities;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.createconvertupdates.entities.Message;
import com.createconvertupdates.entities.MessageMetaData;
import com.createconvertupdates.iface.IHelperActions;

public class MessageHelper implements IHelperActions<Message> {

	public final static String TABLE_NAME = "message_table";
	
	public final static String FIELD_ID = "id";
	public final static String FIELD_HEADER = "header";
	public final static String FIELD_STATUS = "status";
	
	public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
			 FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
			 FIELD_HEADER + " TEXT " +
			 FIELD_STATUS + " INTEGER )";
	
	private DBHelper database;
	
	public MessageHelper(Context context){
		this.database = new DBHelper(context);
	}
	
	
	@Override
	public long add(Message object) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = database.getWritableDatabase();

		long id = -1;
		ContentValues values = new ContentValues();
		values.put(FIELD_HEADER, object.getHeader());
		values.put(FIELD_STATUS, object.getStatus());
		
		id = db.insert(TABLE_NAME, null, values);
		db.close();
		
		return id;
	}

	@Override
	public boolean update(long id, Message object) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = database.getWritableDatabase();
		
		int affected_rows = 0;			
		ContentValues values = new ContentValues();	
		
		values.put(FIELD_HEADER, object.getHeader());
		values.put(FIELD_STATUS, object.getStatus());
		affected_rows = db.update(TABLE_NAME, values, FIELD_ID + " = ?", new String[]{String.valueOf(id)});
		
		db.close();
		
		return affected_rows > 0;
	}

	@Override
	public Message delete(long id, Message object) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = database.getWritableDatabase();

		int r = db.delete(TABLE_NAME, FIELD_ID + " = ?", new String[]{String.valueOf(id)});
		
		
		return (r > 0 )? object: null;
	}
	
	public List<Message> getAll(){
		List<Message> messages = new ArrayList<Message>();
		
		SQLiteDatabase db = database.getReadableDatabase();
		
		MessageMetaDataHelper mm_dh = new MessageMetaDataHelper(database.appContext());
		
		
		
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
		
		if(c!= null && c.moveToFirst()){
			do{
				long id = c.getLong(c.getColumnIndex(FIELD_ID));
				String header = c.getString(c.getColumnIndex(FIELD_HEADER));
				int status = c.getInt(c.getColumnIndex(FIELD_STATUS));
				
				List<MessageMetaData> list_mm = mm_dh.getAll( id );
				
				messages.add(new Message(id , header , status, list_mm ));
				
			}while(c.moveToNext());
			
		}
		return messages;
	}
	
	public Message getMessage(long id){
		SQLiteDatabase db = database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_ID + " = ?", new String[]{String.valueOf(id)} );
		Message message = new Message();
		if(c!=null && c.moveToFirst()){
			message.setId(c.getLong(c.getColumnIndex(FIELD_ID)));
			message.setHeader(c.getString(c.getColumnIndex(FIELD_HEADER)));
			message.setStatus(c.getInt(c.getColumnIndex(FIELD_STATUS)));
		}
		return message;
	}
	
	
	public void close(){
		database.close();
	}

}
