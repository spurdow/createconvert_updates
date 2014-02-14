package com.createconvertupdates.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.createconvertupdates.commons.ConnectionDetector;
import com.createconvertupdates.dbentities.MessageHelper;
import com.createconvertupdates.entities.Customer;

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
	
	public SendMessageMetaDataTask(Context context){
		this.mContext = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String result = "";
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(TAG_MESSAGE_CONTENT ,params[0] ));
		list.add(new BasicNameValuePair(TAG_MESSAGE_METADATA_ID , params[1]));
		list.add(new BasicNameValuePair(TAG_MESSAGE_METADATA_STATUS , params[2]));

		
		AndroidHttpClient http = AndroidHttpClient.newInstance("Android");
		HttpPost post = new HttpPost(SEND_MESSAGE_URL);
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
		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if(result != null){
			Log.d(TAG, result);
			this.publishProgress("Message Sending Success...");
			//MessageHelper helper = new MessageHelper(mContext);
			
			//helper.add(object)
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
