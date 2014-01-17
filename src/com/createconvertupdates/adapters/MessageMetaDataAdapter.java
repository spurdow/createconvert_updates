package com.createconvertupdates.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.createconvertupdates.entities.MessageMetaData;
import com.createconvertupdates.iface.IAdapterActions;

public class MessageMetaDataAdapter extends AbstractListAdapter<MessageMetaData> implements IAdapterActions<MessageMetaData>{

	private Context mContext;
	private List<MessageMetaData> mList;
	
	public MessageMetaDataAdapter(Context context, List<MessageMetaData> lists) {
		super(context, lists);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(MessageMetaData e) {
		// TODO Auto-generated method stub
		mList.add(e);
		notifyDataSetChanged();
	}

	@Override
	public void delete(int index) {
		// TODO Auto-generated method stub
		mList.remove(index);
		notifyDataSetChanged();
	}

	@Override
	public void update(int pos, MessageMetaData object) {
		// TODO Auto-generated method stub
		mList.set(pos, object);
		notifyDataSetChanged();
	}

	@Override
	public List<MessageMetaData> getAll() {
		// TODO Auto-generated method stub
		return mList;
	}

	@Override
	public View getOverridedView(int position, View child, ViewGroup root) {
		// TODO Auto-generated method stub
		return null;
	}

}
