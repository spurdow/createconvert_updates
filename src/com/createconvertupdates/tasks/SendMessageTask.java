package com.createconvertupdates.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.createconvertupdates.adapters.MessageMetaDataAdapter;
import com.createconvertupdates.commons.ConnectionDetector;
import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.entities.Customer;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import static com.createconvertupdates.commons.Utilities.*;

public class SendMessageTask extends AsyncTask<String , String , String>{

	private static final String TAG = "SendMessageTask";
	private Context mContext;

	
	public SendMessageTask(Context context){
		this.mContext = context;

	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		Customer customer = Utilities.getSavedCustomer(mContext);
		String result = "";
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		Log.d(TAG, params[0]);
		list.add(new BasicNameValuePair(TAG_EMAIL , customer.getEmail()));
		list.add(new BasicNameValuePair(TAG_PROJECT_ID , params[0]) );
		list.add(new BasicNameValuePair(TAG_MESSAGE_TITLE  ,  params[1]));
		list.add(new BasicNameValuePair(TAG_MESSAGE_CONTENT , params[2]));
		
		
		
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
		
		
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if(result != null){
			Log.d(TAG, result);
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
