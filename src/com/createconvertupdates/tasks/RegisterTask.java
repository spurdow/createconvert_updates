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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.createconvertupdates.commons.ConnectionDetector;
import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.entities.Customer;


import com.createconvertupdates.medialtd.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

import static com.createconvertupdates.entities.Customer.*;
import static com.createconvertupdates.commons.Utilities.*;

public class RegisterTask extends AsyncTask<String , String, String> {

	private static final String TAG = "RegisterTask";
	private Context context;
	private Customer customer;
	
	private ProgressDialog progress;
	
	public RegisterTask(Context context, Customer customer){
		this.context = context;
		this.customer = customer;
	}
		
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		progress.dismiss();
		try {
			JSONObject jsonObject = new JSONObject(result);
			
			String tag_result = jsonObject.getString(TAG_REGISTER_RESULT);
			String tag_message = jsonObject.getString(TAG_MESSAGE_RESULT);
			JSONArray jsonArray = jsonObject.getJSONArray(TAG_DATA_RESULT);
			
			//String register_result = jsonData.getString("result");
			
			if(tag_result.trim().equals("error")){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setIcon(android.R.drawable.ic_delete)
				.setTitle("Registration Failed")
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

			}else if(tag_result.trim().equals("success")){
				parent.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setIcon(android.R.drawable.btn_plus)
				.setTitle("Registration Success")
				.setMessage("We have sent you an email to fully register to our Project Updates.");
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
			
			Log.d(TAG, "\n" + tag_result + " " + tag_result.equals("error") + " " + tag_result.trim().equals("error"));
			Log.d(TAG, "\n" +  jsonArray.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_delete)
			.setMessage("Something went wrong with our server. Please retry later. Or Contact the admin.");
			AlertDialog dialog = builder.create();
			
			dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new Dialog.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog1, int which) {
					// TODO Auto-generated method stub
					dialog1.dismiss();
				}
				
			});
			
			e.printStackTrace();
		}
		
		//super.onPostExecute(result);
	}




	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		progress = new ProgressDialog(context);
		progress.setMessage("Please wait while we register you...");
		progress.setIndeterminate(true);
		progress.show();
		if(!ConnectionDetector.isConnectedToInternet(context)){
			progress.hide();
			this.cancel(true);
		}
		super.onPreExecute();
	}




	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub		
		
		//Log.d(TAG, Utilities.getRegisterKey(context));
		String result = "";
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(FIRSTNAME,customer.getFirstname()));
		list.add(new BasicNameValuePair(LASTNAME , customer.getLastname()));
		list.add(new BasicNameValuePair(MIDDLENAME , customer.getMiddlename()));
		list.add(new BasicNameValuePair(USERNAME , customer.getUsername()));
		list.add(new BasicNameValuePair(PASSWORD , customer.getPassword().toString()));
		list.add(new BasicNameValuePair(EMAIL , customer.getEmail()));
		
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(REGISTER_URL);
			post.setEntity(new UrlEncodedFormEntity(list));
			HttpResponse response = client.execute(post);
		
			result = Utilities.getContent(response);
			
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
		
		Log.d(TAG, result);
		
		return result;
	}

	/*
	 *  the dialog parent view holder
	 */
	private AlertDialog parent;

	public void setDialogParent(AlertDialog dialog) {
		// TODO Auto-generated method stub
		this.parent = dialog;
	}


}
