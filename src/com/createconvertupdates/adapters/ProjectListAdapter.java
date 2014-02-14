package com.createconvertupdates.adapters;

import java.util.ArrayList;
import java.util.List;

import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.dbentities.ProjectHelper;
import com.createconvertupdates.dbentities.ProjectMetaDataHelper;
import com.createconvertupdates.entities.Project;
import com.createconvertupdates.iface.IAdapterActions;
import com.createconvertupdates.iface.IImageDownload;
import com.createconvertupdates.media.R;
import com.createconvertupdates.tasks.BitmapDownloaderTask;
import com.readystatesoftware.viewbadger.BadgeView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


public class ProjectListAdapter extends AbstractListAdapter<Project> implements IAdapterActions<Project> , IImageDownload {

	private static final String TAG = "ProjectListAdapter";
	private LayoutInflater inflater;
	
	public ProjectListAdapter(Context context, List<Project> lists) {
		super(context, lists);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		
	}

	@Override
	public View getOverridedView(int position, View child, ViewGroup root) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		List<Project> projects = getList();
		if(child == null){
			child = inflater.inflate(R.layout.project_per_list_layout,root ,false);
			
			holder = new ViewHolder();
			holder.name = (TextView) child.findViewById(R.id.h_name);
			holder.slogan = (TextView) child.findViewById(R.id.h_slogan);
			
			holder.date = (TextView) child.findViewById(R.id.h_date);
			/*holder.status = new BadgeView(getContext() , holder.date);
			holder.status.setBadgeMargin(15, 10);
			holder.status.setBadgeBackgroundColor(Color.parseColor("#A4C639"));
			*/
			holder.status_items = new BadgeView(getContext() , holder.date);	
			holder.image = (ImageView) child.findViewById(R.id.h_imageView);

			
			child.setTag(holder);
		}else{
			holder = (ViewHolder) child.getTag();
		}
		
		if(projects.get(position).getStatus()==1){
			child.setBackgroundResource(R.drawable.tab_unselected_focused_createconvert);
			
		}
		
		ProjectMetaDataHelper metaDataHelper = new ProjectMetaDataHelper(getContext());
		int count = metaDataHelper.getAll(projects.get(position).getId()).size();
		
		
		holder.name.setText(projects.get(position).getName());
		holder.slogan.setText(projects.get(position).getSlogan());
		holder.date.setText(projects.get(position).getDate());
		
		
		Log.d(TAG, projects.get(position).getDate() + "  = date ");
		
		if(count == 0) count = 20;
		
		holder.status_items.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		holder.status_items.setText(String.valueOf(count));
		holder.status_items.show();
		/*
		holder.status.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
		holder.status.setText("new");
		holder.status.show();
		
		/*
		 *  download the weak reference bitmap image for imageview
		 */
		// remove this when online
		String replace = projects.get(position).getImagePath().replace("http://localhost/", Utilities.HOST_NAME);
		
		projects.get(position).setImagePath(replace);
		download(projects.get(position).getImagePath() , holder.image);
		
		return child;
	}
	/*
	 *  custom view holder
	 *  for project lists 
	 */
	public class ViewHolder{
		TextView name;
		TextView slogan;
		TextView date;
		BadgeView status_items;
		ImageView image;
		
	}

	@Override
	public void add(Project e) {
		// TODO Auto-generated method stub
		getList().add(0 ,e);
		notifyDataSetChanged();
	}

	@Override
	public void delete(int index) {
		// TODO Auto-generated method stub
		getList().remove(index);
		notifyDataSetChanged();
	}
	
	@Override
	public void update(int pos, Project object) {
		// TODO Auto-generated method stub
		getList().set(pos, object);
		notifyDataSetChanged();
	}

	@Override
	public List<Project> getAll() {
		// TODO Auto-generated method stub
		return getList();
	}

	@Override
	public void download(String url, ImageView view) {
		// TODO Auto-generated method stub
		BitmapDownloaderTask downloadImage = new BitmapDownloaderTask(view);
		downloadImage.execute(url);
		
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Filter filter = new Filter(){

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// TODO Auto-generated method stub
				Log.d("FILTERER", "Filtering Results " + constraint);
				FilterResults filterResults = new FilterResults();
				List<Project> filteredList = new ArrayList<Project>();
				
				
				
				if(getList() == null ){
					ProjectHelper helper = new ProjectHelper(getContext());
					setList( helper.getAll() );
				}
				
				
				
				if(constraint == null || constraint.length() == 0){
					ProjectHelper helper = new ProjectHelper(getContext());
					setBackupList(helper.getAll());
					filterResults.count = getBackupList().size();
					filterResults.values = getBackupList();
				}else{
					constraint = constraint.toString().toLowerCase();
					List<Project> l = getList();
					for(int i =0 ; i < l.size() ; i++){
						Project p = l.get(i);
						if(p.getName().toString().toLowerCase().startsWith(constraint.toString()) ||
						   p.getSlogan().toString().toLowerCase().startsWith(constraint.toString()) ||
						   p.getWebsite().toString().toLowerCase().startsWith(constraint.toString())){
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
				setList((List<Project>) results.values);
				notifyDataSetChanged();
				
			}
			
		};
		
		return filter;

	}

	




}
