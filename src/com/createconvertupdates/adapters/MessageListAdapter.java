package com.createconvertupdates.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.createconvertupdates.dbentities.MessageHelper;
import com.createconvertupdates.entities.Message;
import com.createconvertupdates.entities.MessageMetaData;
import com.createconvertupdates.iface.IAdapterActions;
import com.createconvertupdates.medialtd.R;
import com.readystatesoftware.viewbadger.BadgeView;

public class MessageListAdapter extends AbstractListAdapter<Message> implements IAdapterActions<Message>{

	private LayoutInflater inflater;
	
	public MessageListAdapter(Context context, List<Message> lists) {
		super(context, lists);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getOverridedView(int position, View child, ViewGroup root) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		List<Message> lists = getList();
		if(child == null){
			holder = new ViewHolder();
			child = inflater.inflate(R.layout.message_as_list_row, root , false);
			holder.image  = (ImageView) child.findViewById(R.id.mal_image);
			holder.title = (TextView) child.findViewById(R.id.mal_title);
			holder.count = (TextView) child.findViewById(R.id.mal_count);
			holder.bCount = new BadgeView(getContext()  , holder.count);
			child.setTag(holder);
		}else{
			holder = (ViewHolder) child.getTag();
		}
		holder.count.setVisibility(View.INVISIBLE);
		
		if(lists.get(position).getStatus() == 1){
			child.setBackgroundResource(R.drawable.tab_unselected_focused_createconvert);
		}
		
		int count = 0;
		if(lists.get(position).getList() != null){
			for(MessageMetaData mm : lists.get(position).getList()){
				if(mm.getStatus() == 1){
					count++;
				}
			}
		}
		
		
		holder.title.setText(lists.get(position).getHeader());
		
		if(count > 0){
			holder.bCount.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
			holder.bCount.setText(String.valueOf(count));
			holder.bCount.show();
			holder.count.setVisibility(View.INVISIBLE);
		}
		return child;
	}
	
	class ViewHolder{
		ImageView image;
		TextView title;
		TextView count;
		BadgeView bCount;
	}

	@Override
	public void add(Message e) {
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
	public void update(int pos, Message object) {
		// TODO Auto-generated method stub
		getList().set(pos, object);
		notifyDataSetChanged();
	}

	@Override
	public List<Message> getAll() {
		// TODO Auto-generated method stub
		return getList();
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
				List<Message> filteredList = new ArrayList<Message>();
				
				
				if(getList() == null ){
					MessageHelper helper = new MessageHelper(getContext());
					setList( helper.getAll() );
				}
								
				if(constraint == null || constraint.length() == 0){
					MessageHelper helper = new MessageHelper(getContext());
					setBackupList(helper.getAll());
					filterResults.count = getBackupList().size();
					filterResults.values = getBackupList();
				}else{
					constraint = constraint.toString().toLowerCase();
					List<Message> l = getList();
					for(int i =0 ; i < l.size() ; i++){
						Message m = l.get(i);
						if(m.getHeader().toString().toLowerCase().startsWith(constraint.toString())){
							filteredList.add(m);
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
				setList((List<Message>) results.values);
				notifyDataSetChanged();
				
			}
			
		};
		
		return filter;
	}

}
