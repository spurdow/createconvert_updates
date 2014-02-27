package com.createconvertupdates.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.createconvertupdates.dbentities.ProjectHelper;
import com.createconvertupdates.dbentities.ProjectMetaDataHelper;
import com.createconvertupdates.entities.Project;
import com.createconvertupdates.entities.ProjectMetaData;
import com.createconvertupdates.iface.IAdapterActions;
import com.createconvertupdates.medialtd.R;

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
			
			child.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) child.getTag();
		}
		
		viewHolder.message.setText(list.get(position).getUpdate_message());
		viewHolder.date.setText(list.get(position).getDate_received());
		
		if(list.get(position).getStatus() == 1){
			child.setBackgroundResource(R.drawable.tab_unselected_focused_createconvert);
		}
		return child;
	}
	
	public class ViewHolder{
		TextView message;
		TextView date;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		Filter filter = new Filter(){

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// TODO Auto-generated method stub
				Log.d("FILTERER", "Filtering Results " + constraint);
				FilterResults filterResults = new FilterResults();
				List<ProjectMetaData> filteredList = new ArrayList<ProjectMetaData>();
				
				if(getList() == null ){
					ProjectMetaDataHelper helper = new ProjectMetaDataHelper(getContext());
					setList( helper.getAll(getList().get(0).getProject_id()) );
				}
				
				if(constraint == null || constraint.length() == 0){
					ProjectMetaDataHelper helper = new ProjectMetaDataHelper(getContext());
					setBackupList( helper.getAll(getList().get(0).getProject_id()) );
					filterResults.count = getBackupList().size();
					filterResults.values = getBackupList();
				}else{
					constraint = constraint.toString().toLowerCase();
					List<ProjectMetaData> l = getList();
					for(int i =0 ; i < l.size() ; i++){
						ProjectMetaData p = l.get(i);
						if(p.getUpdate_message().toString().toLowerCase().startsWith(constraint.toString()) ||
						   p.getDate_received().toString().toLowerCase().startsWith(constraint.toString()) ){
							//Spannable highlighted = new SpannableString(p.getUpdate_message());
							
							//highlighted.setSpan(new BackgroundColorSpan(), start, end, flags)
							
							filteredList.add(p);
						}
						filterResults.count = filteredList.size();
						filterResults.values  = filteredList;
					}
				}
				return filterResults;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// TODO Auto-generated method stub
				setList((List<ProjectMetaData>) results.values);
				notifyDataSetChanged();
			}
		};
		
		return filter;
	}

}
