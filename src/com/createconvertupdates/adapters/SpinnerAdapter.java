package com.createconvertupdates.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.createconvertupdates.commons.BitmapDownloaderTask;
import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.entities.MessageProject;
import com.createconvertupdates.iface.IImageDownload;
import com.createconvertupdates.media.R;

public class SpinnerAdapter extends AbstractListAdapter<MessageProject> implements  IImageDownload {
	
	private LayoutInflater inflater;
	
	public SpinnerAdapter(Context context, List<MessageProject> lists) {
		super(context, lists);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getOverridedView(int position, View child, ViewGroup root) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		final MessageProject mprojects = getList().get(position);
		if(child == null){
			holder = new ViewHolder();
			child = inflater.inflate(R.layout.message_spinner_projects, null , false);
			holder.image = (ImageView) child.findViewById(R.id.mp_image);
			holder.name = (TextView) child.findViewById(R.id.mp_project_name);
			holder.checkBox = (CheckBox) child.findViewById(R.id.mp_cbox);
			
			child.setTag(holder);
		}else{
			holder = (ViewHolder) child.getTag();
		}
		
		holder.name.setText(mprojects.getName());
		holder.checkBox.setChecked(mprojects.isCheck());
		
		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				mprojects.setCheck(arg1);
			}
			
		});
		
		/*
		 *  download the weak reference bitmap image for imageview
		 */
		// remove this when online
		String replace = mprojects.getImagePath().replace("http://localhost/", Utilities.HOST_NAME);
		
		mprojects.setImagePath(replace);
		download(mprojects.getImagePath() , holder.image);
		
		return child;
	}
	
	
	@Override
	public void download(String url, ImageView view) {
		// TODO Auto-generated method stub
		BitmapDownloaderTask task = new BitmapDownloaderTask(view);
		task.execute(url);
		
	}

	private class ViewHolder {
		ImageView image;
		TextView name;
		CheckBox checkBox;
	}
}
