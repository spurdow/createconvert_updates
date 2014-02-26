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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.createconvertupdates.adapters.MessageMetaDataAdapter;
import com.createconvertupdates.commons.ConnectionDetector;
import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.dbentities.MessageHelper;
import com.createconvertupdates.dbentities.MessageMetaDataHelper;
import com.createconvertupdates.entities.Customer;
import com.createconvertupdates.entities.Message;
import com.createconvertupdates.entities.MessageMetaData;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import static com.createconvertupdates.commons.Utilities.*;

public class SendMessageTask extends AsyncTask<String , String , String>{

	private static final String TAG = "SendMessageTask";
	private Context mContext;
	private AlertDialog alert;
	private String email;

	
	public SendMessageTask(Context context, AlertDialog alert){
		this.mContext = context;
		this.alert = alert;
	}


	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		Customer customer = Utilities.getSavedCustomer(mContext);
		String result = "";
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		Message mess = new Message();
		mess.setHeader(params[0]);
		mess.setStatus(0);
		MessageHelper mHelper = new MessageHelper(this.mContext);
		long cmid = mHelper.add(mess);
		
		//Log.d(TAG, params[0]);
		list.add(new BasicNameValuePair(TAG_EMAIL , customer.getEmail()));
		list.add(new BasicNameValuePair(TAG_MESSAGE_TITLE  ,  params[0]));
		list.add(new BasicNameValuePair(TAG_MESSAGE_CONTENT , params[1]));
		list.add(new BasicNameValuePair("message_type" , params[2]));
		list.add(new BasicNameValuePair("client_message_id"  , String.valueOf(cmid)));
		
		
		
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
			try {
				JSONObject jsonObject = new JSONObject(result);
				
				String tag_result = jsonObject.getString(TAG_MESSAGE_METADATA_RESULT);
				String tag_message = jsonObject.getString(TAG_MESSAGE_RESULT);
				long client_message_id = Long.parseLong(jsonObject.getString("client_message_id"));
				JSONArray jsonArray = jsonObject.getJSONArray(TAG_DATA_RESULT);
				
				
				
				if(tag_result.equals("success")){
					
					//MessageHelper mHelper = new MessageHelper(mContext);
					Message m = new Message();
					MessageMetaData mm = new MessageMetaData();
					
					JSONObject jObject = jsonArray.getJSONObject(0);
						
					JSONObject message = jObject.getJSONObject("message");
					JSONObject message_metadata = jObject.getJSONObject("message_metadata");
					
					
					
					mm.setContent(message_metadata.getString("content"));
					mm.setDate(message_metadata.getString("created_at"));
					mm.setServer_message_id(Long.parseLong(message_metadata.getString("server_message_id")));
					mm.setType(Integer.parseInt(message_metadata.getString("message_type")));
					mm.setStatus(Integer.parseInt(message_metadata.getString("status")));
					
					m.setHeader(message.getString("header"));
					m.setStatus(Integer.parseInt(message.getString("status")));
					
					MessageHelper mHelper = new MessageHelper(mContext);
					mHelper.update(client_message_id, m);
					mm.setMessage_id(client_message_id);
					MessageMetaDataHelper mmHelper = new MessageMetaDataHelper(mContext);
					mmHelper.add(mm);
					
					
					
					
		    		Intent i = new Intent("new_message_");
		    		i.putExtra("id", client_message_id);
		    		LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(i);
					
		    		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setIcon(R.drawable.btn_plus)
					.setTitle("Message Sent!")
					.setMessage(tag_message);
					AlertDialog dialog = builder.create();
					
					dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new Dialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog1, int which) {
							// TODO Auto-generated method stub
							dialog1.dismiss();
							alert.dismiss();
						}
						
					});
					dialog.show();
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setIcon(R.drawable.btn_plus)
					.setTitle("Message Failed!")
					.setMessage(tag_message);
					AlertDialog dialog = builder.create();
					
					dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new Dialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog1, int which) {
							// TODO Auto-generated method stub
							dialog1.dismiss();
						}
						
					});
					dialog.show();
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
