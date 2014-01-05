package com.createconvertupdates.media;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.createconvertupdates.adapters.ProjectListAdapter;
import com.createconvertupdates.adapters.ProjectMetaDataAdapter;
import com.createconvertupdates.dbentities.ProjectHelper;
import com.createconvertupdates.dbentities.ProjectMetaDataHelper;
import com.createconvertupdates.entities.Project;
import com.createconvertupdates.entities.ProjectMetaData;

public class ProjectFragment extends SherlockFragmentActivity{

	
	public static final String TAG = "ProjectFragment";
	private ListView listView;
	private UpdateReceiver mreceiver;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.list_layout);
		
		listView = (ListView) findViewById(R.id.id_listview);
		
		Bundle extras = getIntent().getExtras();
		
		
		
		int id = extras.getInt("project_id");
		
		ProjectHelper pHelper = new ProjectHelper(this);
		
		Project project = pHelper.get(id);
		
		pHelper.close();
		
		ProjectMetaDataHelper pmdHelper = new ProjectMetaDataHelper(this);
		
		List<ProjectMetaData> pmdLists = pmdHelper.getAll();
		
		ProjectMetaDataAdapter adapter= new ProjectMetaDataAdapter(this , pmdLists);
		
		mreceiver = new UpdateReceiver(adapter);
		
		listView.setAdapter(adapter);
		
		
		
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
				new IntentFilter("new_project_message"));
		super.onResume();
	}
	
	private class UpdateReceiver extends BroadcastReceiver{
		
		private ProjectMetaDataAdapter adapter;
		
		public UpdateReceiver(ProjectMetaDataAdapter adapter){
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
			Log.d(TAG, id + " Updating... Projects Messages");
			ProjectMetaDataHelper helper = new ProjectMetaDataHelper(context);
			adapter.add(helper.get(id));
			
		}
		
	}
	
}
