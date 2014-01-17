package com.createconvertupdates.tasks;

import com.createconvertupdates.adapters.MessageMetaDataAdapter;
import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.entities.Customer;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ListView;

public class SendMessageTask extends AsyncTask<String , Void , String>{

	private Context mContext;
	private MessageMetaDataAdapter mAdapter;
	private ListView mListView;
	private EditText mEditTxt;
	
	public SendMessageTask(Context context , MessageMetaDataAdapter adapter , ListView listView, EditText e_txt){
		this.mContext = context;
		this.mAdapter = adapter;
		this.mListView = listView;
		this.mEditTxt = e_txt;
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		Customer customer = Utilities.getSavedCustomer(mContext);
		
		String result = "";
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	
	
}
