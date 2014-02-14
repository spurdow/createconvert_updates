package com.createconvertupdates.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.createconvertupdates.entities.MessageMetaData;
import com.createconvertupdates.iface.IAdapterActions;
import com.createconvertupdates.media.R;

public class MessageMetaDataAdapter extends AbstractListAdapter<MessageMetaData> implements IAdapterActions<MessageMetaData>{
	
	private LayoutInflater inflater;
	
	public MessageMetaDataAdapter(Context context, List<MessageMetaData> lists) {
		super(context, lists);
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public void add(MessageMetaData e) {
		// TODO Auto-generated method stub
		getList().add(e);
		notifyDataSetChanged();
	}

	@Override
	public void delete(int index) {
		// TODO Auto-generated method stub
		getList().remove(index);
		notifyDataSetChanged();
	}

	@Override
	public void update(int pos, MessageMetaData object) {
		// TODO Auto-generated method stub
		getList().set(pos, object);
		notifyDataSetChanged();
	}

	@Override
	public List<MessageMetaData> getAll() {
		// TODO Auto-generated method stub
		return getList();
	}

	@Override
	public View getOverridedView(int position, View child, ViewGroup root) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(child == null){
			child = inflater.inflate(R.layout.messaging_row, root, false);
			holder = new ViewHolder();
			holder.content = (TextView) child.findViewById(R.id.id_send_content);
			holder.date = (TextView) child.findViewById(R.id.id_send_date);
			child.setTag(holder);
		}else{
			holder = (ViewHolder) child.getTag();
		}
		
		//LayoutParams lp = (LayoutParams) child.getLayoutParams();
		
		MessageMetaData m_md = getList().get(position);
		if(m_md.getType() == MessageMetaData.MINE){
			child.setBackgroundResource(R.drawable.speech_bubble_green);
			
		}else{
			child.setBackgroundResource(R.drawable.speech_bubble_orange);
		}
		
		holder.content.setText(m_md.getContent());
		holder.date.setText(m_md.getDate());
		
		return child;
	}
	
	private class ViewHolder{
		TextView content;
		TextView date;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
