package com.createconvertupdates.adapters;

import com.createconvertupdates.media.MessageFragment;
import com.createconvertupdates.media.ProjectFragment;
import com.createconvertupdates.media.UpdateFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	
	public final static int PAGES = 3;
	
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0){
		case 0  : return new UpdateFragment();
		case 1 	: return new ProjectFragment();
		case 2 	: return new MessageFragment();
			default: assert false;
		};
		
		return null;
	}

	
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGES;
	}

}