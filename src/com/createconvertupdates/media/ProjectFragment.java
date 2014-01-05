package com.createconvertupdates.media;

import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.createconvertupdates.dbentities.ProjectHelper;
import com.createconvertupdates.entities.Project;

public class ProjectFragment extends SherlockFragmentActivity{

	
	private ListView listView;
	
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
		
		
		
	}
	
}
