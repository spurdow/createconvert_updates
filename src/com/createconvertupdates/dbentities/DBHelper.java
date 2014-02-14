package com.createconvertupdates.dbentities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.createconvertupdates.commons.Utilities.*;

public class DBHelper extends SQLiteOpenHelper{

	private final static String TAG = "DatabaseHelper";
	
	private Context context;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		/**
		 *  project_table
		 */
		db.execSQL(ProjectHelper.CREATE_TABLE);
		/**
		 *  project_metadata_table 
		 *  with index to project_id
		 */
		db.execSQL(ProjectMetaDataHelper.CREATE_TABLE);
		db.execSQL(ProjectMetaDataHelper.ADD_INDEX);
		
		
		/**
		 *  message_table
		 *  with index to project_id
		 */
		db.execSQL(MessageHelper.CREATE_TABLE);
		
		/**
		 *  message_metadata_table
		 *  with index to message_id
		 */
		db.execSQL(MessageMetaDataHelper.CREATE_TABLE);
		db.execSQL(MessageMetaDataHelper.ADD_INDEX);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.execSQL(ProjectHelper.DROP_TABLE);
		db.execSQL(ProjectMetaDataHelper.DROP_TABLE);
		db.execSQL(MessageHelper.DROP_TABLE);
		db.execSQL(MessageMetaDataHelper.DROP_TABLE);
		
		onCreate(db);
	}
	
	public Context appContext(){
		return context;
	}

}
