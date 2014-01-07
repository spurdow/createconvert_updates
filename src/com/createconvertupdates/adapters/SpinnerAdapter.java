package com.createconvertupdates.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import static com.createconvertupdates.commons.Utilities.*;

public class SpinnerAdapter extends AbstractListAdapter<MessageProject> implements  IImageDownload {
	
	protected static final String TAG = "SpinnerAdapter";
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
		
		if(position == 0){
			holder.name.setText("Select atleast 1");
			holder.checkBox.setVisibility(View.INVISIBLE);
			holder.image.setVisibility(View.INVISIBLE);
			holder.checkBox.setOnCheckedChangeListener(null);
			
		}else{
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.VISIBLE);
			holder.name.setText(mprojects.getName());
			holder.checkBox.setChecked(mprojects.isCheck());
			holder.checkBox.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d(TAG, arg0.toString());
					Log.v(TAG, "FROM " + mprojects.isCheck() + " TO " + !mprojects.isCheck() );
					mprojects.setCheck(!mprojects.isCheck());
					Log.d(TAG, "Project " + mprojects.getId());
					saveSpinnerData(inflater.getContext() , mprojects);
				}

				
			});
			
			/*
			 *  download the weak reference bitmap image for imageview
			 */
			// remove this when online
			String replace = mprojects.getImagePath().replace("http://localhost/", Utilities.HOST_NAME);
			//
			mprojects.setImagePath(replace);
			download(mprojects.getImagePath() , holder.image);
		}
		

		
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
