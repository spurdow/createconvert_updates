package com.createconvertupdates.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.entities.Customer;
import com.createconvertupdates.media.HomeFragmentActivity;
import com.createconvertupdates.media.SplashActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.util.Log;

import static com.createconvertupdates.commons.Utilities.*;

public class GCMRegIDTask extends AsyncTask<Void , Void , String>{

	private static final String TAG = "GCMRegIDTask";
	private Context context;
	private Customer customer;
	private GoogleCloudMessaging gcm;
	private String regid;
	private ProgressDialog dialog ;
	public GCMRegIDTask(Context context , Customer customer){
		this.context = context;
		this.customer = customer;
		this.gcm  = GoogleCloudMessaging.getInstance(context);
	}
	
	
	/**
	 *  register regid to server side and store it to preferences
	 */
	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String msg  ="";
		
		try {
			regid = gcm.register(SENDER_ID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = "not sent";
		}
		try {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Log.d(TAG, customer.getId()+ "");
			list.add(new BasicNameValuePair("id" , customer.getId()+""));
			list.add(new BasicNameValuePair("regid" , regid));
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(REGISTER_REGID_URL);
			
			post.setEntity(new UrlEncodedFormEntity(list));
			
			HttpResponse response = client.execute(post);
			
			msg = getContent(response);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.d(TAG, msg);
		return msg;
	}


	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		try {
			JSONObject jsonObject = new JSONObject(result);
			
			String tag_result = jsonObject.getString(TAG_REGISTER_RESULT);
			
			if(tag_result.trim().equals("success")){
				customer.setRegid(regid);
				/*
				 *  save the customer preferences
				 */
				Utilities.savePreferences(context, customer);
			}else if(tag_result.trim().equals("error")){
				HomeFragmentActivity a = (HomeFragmentActivity) context;
				removeSavedCustomer(context);
				Intent i = new Intent(a , SplashActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("activity_change", 0 );
				a.startActivity(i);
				
			}
			Log.d(TAG, tag_result + " result after regid");
		}catch(JSONException e){
			e.printStackTrace();
		}
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(context);
		dialog.setMessage("Please wait while we configure your device.");
		dialog.setIndeterminate(true);
		dialog.show();
	}
	
	

}
