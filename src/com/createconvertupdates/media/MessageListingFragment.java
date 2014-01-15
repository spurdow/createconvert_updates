package com.createconvertupdates.media;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.createconvertupdates.adapters.MessageListAdapter;
import com.createconvertupdates.dbentities.MessageHelper;
import com.createconvertupdates.entities.Message;
import com.createconvertupdates.entities.MessageMetaData;

public class MessageListingFragment extends SherlockFragment {

	
	private ListView mListView;
	
	private MessageListAdapter adapter;
	
	
	
	
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
		
		if(messages.size() < 1){
			//MessageMetaData mmd = new MessageMetaData(1 , 1 , );
			messages.add(new Message(1 , "test" , 1 ));
			messages.add(new Message(2 , "test" , 1));
			messages.add(new Message(3 , "test" , 1));
		}
		
		adapter = new MessageListAdapter(getActivity() , messages);
		
		mListView.setAdapter(adapter);
		
		messageHelper.close();
		
		
		
		super.onActivityCreated(savedInstanceState);
	}
	
	

	
}
