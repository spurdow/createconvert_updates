package com.createconvertupdates.dbentities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.createconvertupdates.commons.Utilities.*;

public class DBHelper extends SQLiteOpenHelper{

	private final static String TAG = "DatabaseHelper";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(ProjectHelper.CREATE_TABLE);
		db.execSQL(ProjectMetaDataHelper.CREATE_TABLE);
		db.execSQL(ProjectMetaDataHelper.ADD_INDEX);
		db.execSQL(MessageHelper.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.execSQL(ProjectHelper.DROP_TABLE);
		db.execSQL(ProjectMetaDataHelper.DROP_TABLE);
		db.execSQL(MessageHelper.DROP_TABLE);
		
		onCreate(db);
	}

}
