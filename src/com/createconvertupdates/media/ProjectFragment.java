package com.createconvertupdates.media;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.createconvertupdates.adapters.ProjectListAdapter;
import com.createconvertupdates.adapters.ProjectMetaDataAdapter;
import com.createconvertupdates.dbentities.ProjectHelper;
import com.createconvertupdates.dbentities.ProjectMetaDataHelper;
import com.createconvertupdates.entities.Project;
import com.createconvertupdates.entities.ProjectMetaData;
import com.createconvertupdates.iface.IImageDownload;
import com.createconvertupdates.tasks.BitmapDownloaderTask;

public class ProjectFragment extends SherlockFragmentActivity implements IImageDownload{

	
	public static final String TAG = "ProjectFragment";
	private ListView listView;
	private UpdateReceiver mreceiver;
	private ActionBar mBar;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.list_layout);
		
		listView = (ListView) findViewById(R.id.id_listview);
		
		Bundle extras = getIntent().getExtras();

		long p_id = extras.getLong("project_id");
		
		ProjectHelper pHelper = new ProjectHelper(this);
		
		Project project = pHelper.get(p_id);
		
		/*
		 *  add header
		 */
		ViewGroup header = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.project_header_layout, null);
		
		ImageView image = (ImageView) header.findViewById(R.id.project_header_image);
		TextView slogan = (TextView) header.findViewById(R.id.project_header_slogan);
		TextView website = (TextView) header.findViewById(R.id.project_link);
		
		
		website.setText(Html.fromHtml("<a href=\'" + project.getWebsite() + "\' > View Website </a>"));
		
		slogan.setText(project.getSlogan());
		
		/**
		 *  download the image
		 */
		download(project.getImagePath() , image);
		
		listView.addHeaderView(header);
		
		pHelper.close();
		
		ProjectMetaDataHelper pmdHelper = new ProjectMetaDataHelper(this);
		
		List<ProjectMetaData> pmdLists = pmdHelper.getAll(p_id);

		
		ProjectMetaDataAdapter adapter= new ProjectMetaDataAdapter(this , pmdLists);
		
		mreceiver = new UpdateReceiver(adapter);
		
		listView.setAdapter(adapter);
		
		
		mBar = getSupportActionBar();
		
		mBar.setTitle(project.getName());
		mBar.setHomeButtonEnabled(true);
		mBar.setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home: 
				finish();
			break;
		}
		return super.onOptionsItemSelected(item);
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
			final long project_id = intent.getLongExtra("project_id", -1);
			/*
			 *  if there is a record then update the adapter
			 */
			Log.d(TAG, id + " Updating... Projects Messages");
			ProjectMetaDataHelper helper = new ProjectMetaDataHelper(context);
			adapter.add(helper.get(id , project_id));
			
		}
		
	}

	@Override
	public void download(String url, ImageView view) {
		// TODO Auto-generated method stub
		BitmapDownloaderTask task = new BitmapDownloaderTask(view);
		task.execute(url);
		
	}
	
}
