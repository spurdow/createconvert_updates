package com.createconvertupdates.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.createconvertupdates.adapters.MessageMetaDataAdapter;
import com.createconvertupdates.commons.ConnectionDetector;
import com.createconvertupdates.dbentities.MessageHelper;
import com.createconvertupdates.dbentities.MessageMetaDataHelper;
import com.createconvertupdates.entities.Customer;
import com.createconvertupdates.entities.MessageMetaData;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import static com.createconvertupdates.commons.Utilities.*;

public class SendMessageMetaDataTask extends AsyncTask<String , String , String>{

	private static final String TAG = "SendMessageMetaData";
	private Context mContext;
	
	private String mContent ; 
	private long mMetadata_id;
	private int mStatus;
	private MessageMetaDataAdapter mAdapter;
	
	public SendMessageMetaDataTask(Context context, MessageMetaDataAdapter mAdapter){
		this.mContext = context;
		this.mAdapter = mAdapter;
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String result = "";
		this.publishProgress("Sending...");
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("content" ,params[0] ));
		list.add(new BasicNameValuePair("server_message_id", params[1]));
		list.add(new BasicNameValuePair("message_type" , params[2]));
		list.add(new BasicNameValuePair("message_id" , params[3]));
		

		Log.d(TAG, params[0] + " " +  params[1]  + " " + params[2]);
		
		AndroidHttpClient http = AndroidHttpClient.newInstance("Android");
		HttpPost post = new HttpPost(SEND_MESSAGE_METADATA_URL);
		try {
			post.setEntity(new UrlEncodedFormEntity(list));
			
			HttpResponse response = http.execute(post);
			
			final int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK){
				Log.w(TAG, "Sending Message failed");
				this.publishProgress("Message Sending Failed");
				return null;
			}
			result = getContent(response);
						
			
		}catch(Exception ex){
			post.abort();
			Log.w(TAG, "Failed to send message");
		} finally{
			if(http != null){
				http.close();
			}
		}
		
		if(isCancelled()){
			result = null;
		}
		Log.d(TAG, result + " = result");
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if(result != null && !result.trim().equals("")){
			Log.d(TAG, result);
			this.publishProgress("Message Sending Success...");
			
			try {
				JSONObject jsonObject = new JSONObject(result);
	
				String tag_result = jsonObject.getString(TAG_MESSAGE_METADATA_RESULT);
				String tag_message = jsonObject.getString(TAG_MESSAGE_RESULT);
				long message_id = Long.parseLong(jsonObject.getString("message_id"));
				JSONArray jsonArray = jsonObject.getJSONArray(TAG_DATA_RESULT);
				
				if(tag_result.trim().equals("success")){
					

					JSONObject jObject = jsonArray.getJSONObject(0);
					
					MessageMetaData metaData = new MessageMetaData();
					metaData.setContent(jObject.getString("content"));
					metaData.setDate(jObject.getString("created_at"));
					metaData.setServer_message_id(Long.parseLong(jObject.getString("server_message_id")));
					metaData.setMessage_id(message_id);
					metaData.setType(Integer.parseInt(jObject.getString("message_type")));
					metaData.setStatus(Integer.parseInt(jObject.getString("status")));
					
					MessageMetaDataHelper h = new MessageMetaDataHelper(mContext);
					Log.d(TAG, "id = " + h.add(metaData));
					h.close();
					mAdapter.add(metaData);
					
				}else{
					
				}
				
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(!ConnectionDetector.isConnectedToInternet(mContext)){
			this.cancel(true);
		}
		
	}

}
