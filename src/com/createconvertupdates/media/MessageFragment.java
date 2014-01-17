package com.createconvertupdates.media;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.createconvertupdates.adapters.MessageMetaDataAdapter;
import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.dbentities.MessageMetaDataHelper;
import com.createconvertupdates.entities.MessageMetaData;

public class MessageFragment extends SherlockFragmentActivity {

	
	private ListView mListView;
	private ActionBar mBar;
	private EditText mContent;
	private MessageMetaDataAdapter mAdapter;
	private long message_id;
	private String title;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.send_message_layout);
		
		mListView = (ListView) findViewById(R.id.id_listview_send);
		
		mContent = (EditText) findViewById(R.id.id_txt_message);
		
		Bundle extras = getIntent().getExtras();
		
		message_id  = extras.getLong("message_id");
		
		title = extras.getString("message_title");
		
		MessageMetaDataHelper mm_dh = new MessageMetaDataHelper(this);
		
		List<MessageMetaData> message_metadata = mm_dh.getAll(message_id);
		
		if(message_metadata.size() <= 0){
			message_metadata.add(new MessageMetaData(1 , message_id, title, "hello" , "12-20-2014" , 0 , 1));
			message_metadata.add(new MessageMetaData(1 , message_id, title, "asdsadas" , "12-20-2014" , 0 , 1));
			message_metadata.add(new MessageMetaData(1 , message_id, title, "asdsada" , "12-20-2014" , 0 , 1));
			message_metadata.add(new MessageMetaData(1 , message_id, title, "hasds" , "12-20-2014" , 0 , 1));
			message_metadata.add(new MessageMetaData(1 , message_id, title, "22222222" , "12-20-2014" , 0 , 1));
			message_metadata.add(new MessageMetaData(1 , message_id, title, "33333333" , "12-20-2014" , 0 , 1));
		}
		
		mAdapter = new MessageMetaDataAdapter(this , message_metadata);
		
		
		mListView.setAdapter(mAdapter);
		
		mm_dh.close();
		
		mBar = getSupportActionBar();
		
		mBar.setTitle(title);
		mBar.setHomeButtonEnabled(true);
		mBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	public void sendMessage(View v){
		String content = mContent.getText().toString().trim();
		if(Utilities.isValidString(content)){
			mAdapter.add(new MessageMetaData(1, message_id, title  , content , "12-20-2014" , (Math.random() > 0.5f)? 0: 1 , 1 ) );
			mListView.setSelection(mAdapter.getCount() - 1);
		}
	}

	
}
