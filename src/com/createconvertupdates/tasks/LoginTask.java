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
import com.createconvertupdates.entities.Customer;
import com.createconvertupdates.media.HomeFragmentActivity;



import static com.createconvertupdates.commons.Utilities.*;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<Void , Void , String>{

	private static final String TAG = "LoginTask";

	private ProgressDialog progress;
		
	private Context context;
	private String username;
	private String password;
	
	public LoginTask(Context context , String username, String password){
		this.context = context;
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		progress = new ProgressDialog(context);
		progress.setIndeterminate(true);
		progress.setMessage("Please wait while we check your credentials");
		progress.show();
		if(!ConnectionDetector.isConnectedToInternet(context)){
			dialog.hide();
			this.cancel(true);
			Log.d(TAG, "cancelled");
		}
		
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		String result = "";
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(TAG_USERNAME , username));
		list.add(new BasicNameValuePair(TAG_PASSWORD , password));
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(LOGIN_URL);
			post.setEntity(new UrlEncodedFormEntity(list));
			HttpResponse response = client.execute(post);
			
			result = getContent(response);
			
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

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		progress.dismiss();
		
		if(isCancelled()){
			return;
		}
		
		try {
			JSONObject jsonObject = new JSONObject(result);
			
			String tag_result = jsonObject.getString(TAG_REGISTER_RESULT);
			String tag_message = jsonObject.getString(TAG_MESSAGE_RESULT);
			JSONArray jsonArray = jsonObject.getJSONArray(TAG_DATA_RESULT);
			
		
			if(tag_result.trim().equals("error")){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setIcon(android.R.drawable.ic_delete)
				.setTitle("Login Failed")
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
				dialog.dismiss();
				
				
				/*
				 *  save customer preferences
				 */
				Customer customer = new Customer();
				// get the json array exclusively for customer
				JSONObject object = jsonArray.getJSONObject(0);
				customer.setId(Integer.parseInt(object.getString("id")));
				customer.setEmail(object.getString("email"));
				customer.setUsername(object.getString("username"));
				customer.setPassword(object.getString("password").toCharArray());
				/*
				 * save the customer preferences
				 */
				savePreferences(context , customer);
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setIcon(R.drawable.btn_plus)
				.setTitle("Login Success")
				.setMessage(tag_message);
				AlertDialog dialog = builder.create();
				
				dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new Dialog.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog1, int which) {
						// TODO Auto-generated method stub
						dialog1.dismiss();
						Intent i = new Intent(context , HomeFragmentActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
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
		
	}
	private AlertDialog dialog;
	public void setParentDialog(AlertDialog dialog) {
		// TODO Auto-generated method stub
		this.dialog = dialog;
	}

	
	
}
