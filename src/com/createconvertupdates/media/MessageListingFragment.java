package com.createconvertupdates.media;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.createconvertupdates.adapters.MessageListAdapter;
import com.createconvertupdates.adapters.MessageMetaDataAdapter;
import com.createconvertupdates.dbentities.MessageHelper;
import com.createconvertupdates.dbentities.MessageMetaDataHelper;
import com.createconvertupdates.entities.Message;
import com.createconvertupdates.entities.MessageMetaData;

public class MessageListingFragment extends SherlockFragment implements OnItemClickListener {

	
	public static final String TAG = "MessageListingFragment" ;

	private ListView mListView;
	
	private MessageListAdapter adapter;
	
	private UpdateReceiver mreceiver;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.list_layout , container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mListView = (ListView) getView().findViewById(R.id.id_listview);
		
		MessageHelper messageHelper = new MessageHelper(getActivity());
		
		List<Message> messages = messageHelper.getAll();
	
		adapter = new MessageListAdapter(getActivity() , messages);
		
		mreceiver = new UpdateReceiver(adapter);
		
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(this);
		
		messageHelper.close();
				
		super.onActivityCreated(savedInstanceState);
	}




	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Message m = adapter.getList().get(arg2);
		
		
		Intent i = new Intent(this.getActivity() , MessageFragment.class);
		i.putExtra("message_id", m.getId());
		i.putExtra("message_title", m.getHeader());
		
		startActivity(i);
		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		/*
		 *  register a update reciever
		 */
		LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(mreceiver);
		super.onPause();
		
	}
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		/*
		 *  register a update reciever
		 */
		LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(mreceiver,
				new IntentFilter("new_message_"));
		super.onResume();
	}
	
	private class UpdateReceiver extends BroadcastReceiver{
		
		private MessageListAdapter adapter;
		
		public UpdateReceiver(MessageListAdapter adapter){
			this.adapter = adapter;
		}
		
		/*
		 *  local broadcast reciever
		 */
		@Override
		public void onReceive(final Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			
			final long id = intent.getLongExtra("id", -1);
			
			/*
			 *  if there is a record then update the adapter
			 */
	
			MessageHelper mHelper = new MessageHelper(context);
			adapter.add(mHelper.getMessage(id));
			
		}
		
	}
	

	
}
