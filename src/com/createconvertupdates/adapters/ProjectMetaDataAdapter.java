package com.createconvertupdates.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.createconvertupdates.entities.ProjectMetaData;
import com.createconvertupdates.iface.IAdapterActions;
import com.createconvertupdates.media.R;

public class ProjectMetaDataAdapter extends AbstractListAdapter<ProjectMetaData> implements IAdapterActions<ProjectMetaData> {

	private LayoutInflater inflater;
	
	public ProjectMetaDataAdapter(Context context, List<ProjectMetaData> lists) {
		super(context, lists);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void add(ProjectMetaData e) {
		// TODO Auto-generated method stub
		getList().add(0, e);
		notifyDataSetChanged();
	}

	@Override
	public void delete(int index) {
		// TODO Auto-generated method stub
		getList().remove(index);
		notifyDataSetChanged();
	}

	@Override
	public void update(int pos, ProjectMetaData object) {
		// TODO Auto-generated method stub
		getList().set(pos, object);
		notifyDataSetChanged();
	}

	@Override
	public List<ProjectMetaData> getAll() {
		// TODO Auto-generated method stub
		return getList();
	}

	@Override
	public View getOverridedView(int position, View child, ViewGroup root) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		List<ProjectMetaData> list = getList();
		if(child == null){
			child = inflater.inflate(R.layout.project_metadata_per_list_layout, root, false);
			viewHolder = new ViewHolder();
			viewHolder.message = (TextView) child.findViewById(R.id.md_message);
			viewHolder.date = (TextView) child.findViewById(R.id.md_date);
			viewHolder.status = (TextView) child.findViewById(R.id.md_status);
			
			child.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) child.getTag();
		}
		
		viewHolder.message.setText(list.get(position).getUpdate_message());
		viewHolder.date.setText(list.get(position).getDate_received());
		
		if(list.get(position).getStatus() == 1){
			viewHolder.status.setText("new");
			viewHolder.status.setTextColor(Color.GREEN);
		}
		
		return child;
	}
	
	public class ViewHolder{
		TextView message;
		TextView date;
		TextView status;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

}
