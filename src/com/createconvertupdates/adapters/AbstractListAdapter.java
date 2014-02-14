package com.createconvertupdates.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 *  An abstract implementatio of list adapter to be used by all adapters
 *  Author : David Montecillo 
 */
public abstract class AbstractListAdapter<E> extends BaseAdapter implements Filterable{

	
	private Context context;
	private List<E> lists ;
	private List<E> backupList;
	public AbstractListAdapter(Context context , List<E> lists){
		this.context = context;
		this.lists = lists;
	}
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(lists == null) return 0;
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return getOverridedView(arg0 , arg1, arg2);
	}
	/**
	 *  returns the Override view provided by its subclass
	 */
	public abstract View getOverridedView(int position , View child, ViewGroup root);
	
	
	public Context getContext(){
		return context;
	}
	
	public void setList(List<E> list){
		this.lists = list;
	}

	public void setBackupList(List<E> fList){
		this.backupList = fList;
	}
	
	public List<E> getList(){
		return lists;
	}
	
	public List<E> getBackupList(){
		return backupList;
	}
	


}
