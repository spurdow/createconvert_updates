package com.createconvertupdates.media;

import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.createconvertupdates.adapters.ProjectListAdapter;
import com.createconvertupdates.dbentities.ProjectHelper;
import com.createconvertupdates.entities.Project;
import com.createconvertupdates.medialtd.R;

public class ProjectListingFragment extends SherlockFragment implements OnItemClickListener, OnQueryTextListener {

	private static final String TAG = "ProjectListingFragment";

	private ProjectListAdapter adapter;

	private UpdateReceiver mreceiver;

	private ListView listView;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		this.setHasOptionsMenu(true);
		return inflater.inflate(R.layout.list_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		listView = (ListView) getView().findViewById(R.id.id_listview);
		
		ProjectHelper projectHelper = new ProjectHelper(getActivity());		
		
		List<Project> projects = projectHelper.getAll();
		
		adapter = new ProjectListAdapter(getActivity() , projects);
		
		listView.setAdapter(adapter);
		
		listView.setTextFilterEnabled(true);
		
		mreceiver = new UpdateReceiver(adapter);
		
		projectHelper.close();
		
		listView.setOnItemClickListener(this);
		
		
				
		super.onActivityCreated(savedInstanceState);
	}
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		/*
		 *  register and update reciever
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
				new IntentFilter("add_project"));
		super.onResume();
	}




	private class UpdateReceiver extends BroadcastReceiver{
		
		private ProjectListAdapter adapter;
		
		public UpdateReceiver(ProjectListAdapter adapter){
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
			Log.d(TAG, id + " Updating... Projects");
			ProjectHelper helper = new ProjectHelper(context);
			adapter.add(helper.get(id));
			
		}
		
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
		// TODO Auto-generated method stub
		Project p  = adapter.getList().get(pos);
		p.setStatus(0);
		arg1.setBackgroundColor(Color.WHITE);
		long p_id  = p.getId();
		ProjectHelper projectHelper = new ProjectHelper(this.getActivity());
		projectHelper.update(p_id, p);
		adapter.update(pos, p);
		Intent i = new Intent(this.getActivity() , ProjectFragment.class);
		i.putExtra("project_id", p_id);
		startActivity(i);
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		//MenuInflater inflater = this.getSupportMenuInflater();
		
		inflater.inflate(R.menu.home_page_menu, menu);
		
		MenuItem searchItem = menu.findItem(R.id.id_search);
		SearchView searchView = (SearchView) searchItem.getActionView();
		
		searchView.setOnQueryTextListener(this);
		

		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub

	    adapter.getFilter().filter(query);
	    
	    return true;
	        
	        
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		adapter.getFilter().filter(newText);
		return true;
	}



	
	
}
