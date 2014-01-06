package com.createconvertupdates.media;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.createconvertupdates.adapters.ViewPagerAdapter;
import com.createconvertupdates.entities.Customer;
import com.createconvertupdates.tasks.GCMRegIDTask;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import static com.createconvertupdates.commons.Utilities.*;

public class HomeFragmentActivity extends SherlockFragmentActivity {

	protected static final String TAG = "HomeFragmentActivity";
	private ActionBar mActionBar;
	private ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.home_fragment_activity);
		
		
		
		Customer customer = getSavedCustomer(HomeFragmentActivity.this);
		Log.d(TAG, "Register ID = " + customer.getRegid());
		if(customer.getRegid().equals("no value")){
			/*
			 *  register a new gcm registration id
			 */
			GCMRegIDTask task = new GCMRegIDTask(HomeFragmentActivity.this, customer);
			task.execute(null, null, null);
		}
		
		/*
		 * creating custom action bar layout
		 */
		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar_layout, null);
		
		/*
		 *  get the sherlock action bar support
		 */
		mActionBar = this.getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setTitle("Welcome to CreateConvert");
		//mActionBar.setDisplayShowTitleEnabled(true);
		
	
		
		
		/*
		 *  set the actionBar navigation mode to tabs
		 */
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		
		/*
		 *  get the pager from xml
		 */
		mViewPager = (ViewPager) findViewById(R.id.pager);
		
		/*
		 *  if our instance state is not null
		 *  then retrieve our last position from the viewpager
		 */
		if(arg0 != null){
			mViewPager.setCurrentItem(arg0.getInt("state"));
			/*
			 *  if tag projects extras exists 
			 *  then we remove the notification count
			 */
			if(arg0.getInt(TAG_PROJECTS, -1) != -1){
				removeNotificationCount(this, arg0.getInt(TAG_PROJECTS), TAG_PROJECTS);
			}
		}
		
		/*
		 * instantiate fragment manager for viewpager adapter to use
		 */
		FragmentManager fm = this.getSupportFragmentManager();
		
		/*
		 * set page listener the onPageSelected position
		 */
		ViewPager.SimpleOnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener(){

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mActionBar.setSelectedNavigationItem(position);
				Log.d(TAG, position + "");
			}
			
		};
		
		/*
		 *  set the page listener
		 */
		mViewPager.setOnPageChangeListener(listener);
		
		/*
		 *  instantiate the adapter 
		 */
		ViewPagerAdapter adapter = new ViewPagerAdapter(fm);
		/*
		 *  use the adapter for the view pager
		 */
		mViewPager.setAdapter(adapter);
		
		
		
		/*
		 *  set the tabs and tab listener for each tabs
		 *  
		 */
		
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(tab.getPosition() );
				Log.d(TAG, tab.getPosition() + "");
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
		};
		
		mActionBar.addTab(mActionBar.newTab().setText(TAG_UPDATES).setTabListener(tabListener));
		mActionBar.addTab(mActionBar.newTab().setText(TAG_PROJECTS).setTabListener(tabListener));
		mActionBar.addTab(mActionBar.newTab().setText(TAG_MESSAGES).setTabListener(tabListener));
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		/*
		 *  save the tabs current position/state 
		 *  this is invoke when screen orientation changes
		 */
		outState.putInt("state", mViewPager.getCurrentItem());
		super.onSaveInstanceState(outState);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = this.getSupportMenuInflater();
		
		inflater.inflate(R.menu.home_page_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.id_logout: removeSavedCustomer(this);
				Intent i = new Intent(this , SplashActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("activity_change", 0 );
				startActivity(i);
			break;
		case R.id.id_compose: 
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.new_message, null);
			
			Spinner select = (Spinner) view.findViewById(R.id.messaging_spinner);
			EditText m_title = (EditText) view.findViewById(R.id.messaging_title);
			EditText m_content = (EditText) view.findViewById(R.id.messaging_content);
			
			Button message = (Button) view.findViewById(R.id.m_btn_message);
			Button cancel = (Button) view.findViewById(R.id.m_btn_cancel);
			
			builder.setView(view);
			
			final AlertDialog alert = builder.create();
			
			message.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/**
					 *  messaging action
					 */
				}
				
			});
			
			cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alert.dismiss();
				}
				
			});
			alert.show();
			break;
		
		
		}
		
		return super.onOptionsItemSelected(item);
	}



	
	

	
}
