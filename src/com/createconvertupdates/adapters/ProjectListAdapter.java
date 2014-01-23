package com.createconvertupdates.adapters;

import java.util.List;

import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.entities.Project;
import com.createconvertupdates.iface.IAdapterActions;
import com.createconvertupdates.iface.IImageDownload;
import com.createconvertupdates.media.R;
import com.createconvertupdates.tasks.BitmapDownloaderTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectListAdapter extends AbstractListAdapter<Project> implements IAdapterActions<Project> , IImageDownload{

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
			holder.status = (TextView) child.findViewById(R.id.h_status);
			holder.image = (ImageView) child.findViewById(R.id.h_imageView);

			
			child.setTag(holder);
		}else{
			holder = (ViewHolder) child.getTag();
		}
		
		if(projects.get(position).getStatus()==1){
			child.getBackground().setColorFilter(Color.parseColor("gray"), PorterDuff.Mode.LIGHTEN);
		}
		
		holder.name.setText(projects.get(position).getName());
		holder.slogan.setText(projects.get(position).getSlogan());
		holder.date.setText(projects.get(position).getDate());
		
		Log.d(TAG, projects.get(position).getDate() + "");
		
		if(projects.get(position).getStatus() == 1){
			holder.status.setText("new");
			holder.status.setTextColor(Color.GREEN);
		}
		
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
		TextView status;
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




}
