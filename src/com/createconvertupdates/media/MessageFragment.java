package com.createconvertupdates.media;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.createconvertupdates.adapters.MessageMetaDataAdapter;
import com.createconvertupdates.adapters.ProjectMetaDataAdapter;
import com.createconvertupdates.commons.AfterTextChanged;
import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.dbentities.MessageMetaDataHelper;
import com.createconvertupdates.dbentities.ProjectMetaDataHelper;
import com.createconvertupdates.entities.MessageMetaData;
import com.createconvertupdates.tasks.SendMessageMetaDataTask;

public class MessageFragment extends SherlockFragmentActivity {

	
	private static final String TAG = MessageFragment.class.getName();
	private ListView mListView;
	private ActionBar mBar;
	private EditText mContent;
	private Button mSend;
	private MessageMetaDataAdapter mAdapter;
	private long message_id;
	private long server_message_id;
	private String title;
	private UpdateReceiver mreceiver;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.send_message_layout);
		
		mListView = (ListView) findViewById(R.id.id_listview_send);
		
		mContent = (EditText) findViewById(R.id.id_txt_message);
		
		mSend = (Button) findViewById(R.id.id_btn_send_message); 
		mSend.setEnabled(false);
		
		mContent.addTextChangedListener(new AfterTextChanged(){

			@Override
			public void abstract_afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(Utilities.isValidString(s.toString())){
					mSend.setEnabled(true);
				}else if(s.toString().trim().equals("")){
					mSend.setEnabled(false);
				}
			}
			
		});
		
		
		Bundle extras = getIntent().getExtras();
		
		message_id  = extras.getLong("message_id");
		
		title = extras.getString("message_title");
		
		MessageMetaDataHelper mm_dh = new MessageMetaDataHelper(this);
		
		List<MessageMetaData> message_metadata = mm_dh.getAll(message_id);
		
		server_message_id = message_metadata.get(0).getServer_message_id();
		
		Log.d(TAG, message_metadata.size() + " ");

		mAdapter = new MessageMetaDataAdapter(this , message_metadata);
		
		mreceiver = new UpdateReceiver(mAdapter);
		
		mListView.setAdapter(mAdapter);
		
		mm_dh.close();
		
		mBar = getSupportActionBar();
		
		mBar.setTitle(title);
		mBar.setHomeButtonEnabled(true);
		mBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private SendMessageMetaDataTask sendMessage = null;
	public void sendMessage(View v){
		String content = mContent.getText().toString().trim();
		if(Utilities.isValidString(content)){
			//mAdapter.add(new MessageMetaData(1, message_id , content , "12-20-2014" , (Math.random() > 0.5f)? 0: 1 , 1 ) );
			
			sendMessage = new SendMessageMetaDataTask(this, mAdapter);
			sendMessage.execute(content, String.valueOf(server_message_id) , String.valueOf(MessageMetaData.MINE) , String.valueOf(message_id));
			mListView.setSelection(mAdapter.getCount() - 1);
			mContent.setText("");
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		/*
		 *  register a update reciever
		 */
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mreceiver);
		super.onPause();
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		/*
		 *  register a update reciever
		 */
		LocalBroadcastManager.getInstance(this).registerReceiver(mreceiver,
				new IntentFilter("new_message"));
		super.onResume();
	}
	
	private class UpdateReceiver extends BroadcastReceiver{
		
		private MessageMetaDataAdapter adapter;
		
		public UpdateReceiver(MessageMetaDataAdapter adapter){
			this.adapter = adapter;
		}
		
		/*
		 *  local broadcast reciever
		 */
		@Override
		public void onReceive(final Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			
			final long id = intent.getLongExtra("id", -1);
			final long m_id = intent.getLongExtra("message_id", -1);
			/*
			 *  if there is a record then update the adapter
			 */
			Log.d(TAG, id + " Updating... Projects Messages");
			MessageMetaDataHelper mmHelper = new MessageMetaDataHelper(context);
			adapter.add(mmHelper.get(id, m_id));
			
		}
		
	}
	
}
