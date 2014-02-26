package com.createconvertupdates.media;

import java.util.ArrayList;
import java.util.List;

import com.createconvertupdates.commons.AfterTextChanged;
import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.entities.Customer;
import com.createconvertupdates.iface.ISwipeGesture;
import com.createconvertupdates.tasks.LoginTask;
import com.createconvertupdates.tasks.RegisterTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import static com.createconvertupdates.commons.Utilities.*;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;

public class SplashActivity extends Activity implements OnTouchListener{
	
	private final static String TAG = "SplashScreen";

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	private GestureDetector gesture;

	private Point last_point;
	
	private RelativeLayout child;
	
	private boolean error = true;
	
	//public GoogleCloudMessaging gcm;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_splash_screen);
		
		
		
		if(checkPlayServices()){
			Customer customer = getSavedCustomer(SplashActivity.this);
			Bundle extras = this.getIntent().getExtras();
			int state = -1;
			if(extras != null){
				state = extras.getInt("activity_change");
			}
			if(state > -1 ){
				/**
				 *  create alert dialog to post that registration for reg id failed
				 *  
				 */
			}
			else if(customer.getId() > 0){
				Intent i = new Intent(SplashActivity.this , HomeFragmentActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}
			
			gesture = new GestureDetector(new GestureOpenListener());
			
			child = (RelativeLayout) findViewById(R.id.splashscreen_child_layout);
			child.setOnTouchListener(this);
			
			last_point = new Point();
			
			
				
		}
		
	}


	 /*
     *  Fling detection
     */
    private final class GestureOpenListener extends SimpleOnGestureListener implements ISwipeGesture{
    	private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        
        
      
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			boolean result = true;
			
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                	
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            swipeRight();
                        } else {
                            swipeLeft();
                        }
                        
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            swipeDown();
                        } else {
                            swipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
			
			return result;
		}


		@Override
		public void swipeLeft() {
			// TODO Auto-generated method stub
			
			startActivity(new Intent(SplashActivity.this , HomeFragmentActivity.class));
			
			Log.d(TAG, "Swipe Left");
		}


		@Override
		public void swipeRight() {
			// TODO Auto-generated method stub
			Log.d(TAG, "Swipe Right");
		}


		@Override
		public void swipeDown() {
			// TODO Auto-generated method stub
			
			error = false;
			
			LayoutInflater inflater = LayoutInflater.from(SplashActivity.this);
			View dialogview = inflater.inflate(R.layout.register_layout, null);
			 
			final Button register = (Button) dialogview.findViewById(R.id.btn_register);
			Button cancel = (Button) dialogview.findViewById(R.id.btn_cancel_reg);
			
			
			 AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(SplashActivity.this);

			 dialogbuilder.setView(dialogview);
			 final AlertDialog dialog = dialogbuilder.create();
			
			
			/*
			 *  get handle for textView's
			 */
			
			final EditText fname = (EditText) dialogview.findViewById(R.id.txt_fname);
			final EditText lname = (EditText) dialogview.findViewById(R.id.txt_lname);
			final EditText mname = (EditText) dialogview.findViewById(R.id.txt_mname);
			final EditText username = (EditText) dialogview.findViewById(R.id.txt_username);
			final EditText password =  (EditText) dialogview.findViewById(R.id.txt_password);
			final EditText cPassword = (EditText) dialogview.findViewById(R.id.txt_cpassword);
			final EditText email = (EditText) dialogview.findViewById(R.id.txt_email);
			
			final TextView fname_error = (TextView) dialogview.findViewById(R.id.txt_fname_error);
			final TextView lname_error = (TextView) dialogview.findViewById(R.id.txt_lname_error);
			final TextView mname_error = (TextView) dialogview.findViewById(R.id.txt_mname_error);
			final TextView username_error = (TextView) dialogview.findViewById(R.id.txt_username_error);
			final TextView password_error = (TextView) dialogview.findViewById(R.id.txt_password_error);
			final TextView password_c_error = (TextView) dialogview.findViewById(R.id.txt_confirm_password_error);
			final TextView email_error  = (TextView) dialogview.findViewById(R.id.txt_email_error);
			
			/*
			 *  create textchange listener
			 *
			 *  firstname listener
			 */
			fname.addTextChangedListener(new AfterTextChanged(){

				@Override
				public void abstract_afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(!isValidString(s.toString())){
						error = true;
						fname_error.setText("Firstname should be filled up!");
						
					}else{
						error = false;
						fname_error.setText("");
					}
				}
				
			});
			
			
			
			/*
			 *  lastname listener
			 */
			lname.addTextChangedListener(new AfterTextChanged(){

				@Override
				public void abstract_afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(!isValidString(s.toString())){
						error = true;
						lname_error.setText("Last name should be filled up!");
						
					}else{
						error = false;
						lname_error.setText("");
					}
				}
				
			});
			
			/*
			 *  middlename listener
			 */
			mname.addTextChangedListener(new AfterTextChanged(){

				@Override
				public void abstract_afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(!isValidString(s.toString())){
						error = true;
						mname_error.setText("MiddleName should be filled up!");
						
					}else{
						error = false;
						mname_error.setText("");
					}
				}
				
			});
			
			/*
			 *  username listener
			 */
			username.addTextChangedListener(new AfterTextChanged(){

				@Override
				public void abstract_afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(!isValidString(s.toString()) || !isValidUsername(s.toString())){
						error = true;
						username_error.setText("Username should be filled up!");
						
					}else{
						error = false;
						username_error.setText("");
					}
				}
				
			});
			
			/*
			 *  password listener
			 */
			password.addTextChangedListener(new AfterTextChanged(){

				@Override
				public void abstract_afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(!isValidString(s.toString()) || !isValidPassword(s.toString())){
						error = true;
						password_error.setText("Password must be greater than 4 characters");
					}else{
						error = false;
						password_error.setText("");
					}
					
				}
				
			});
			
			/*
			 *  confirm password listener
			 */
			cPassword.addTextChangedListener(new AfterTextChanged(){

				@Override
				public void abstract_afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(!isValidPassword2Password(s.toString() , password.getText().toString())){
						error = true;
						password_c_error.setText("Password should match!");
					}
					else if(!isValidString(s.toString()) || !isValidPassword(s.toString())){
						error = true;
						password_c_error.setText("Password should be greater than 4 characters!");
						
					}else{
						error = false;
						password_c_error.setText("");
					}
				}
				
			});
			
			email.addTextChangedListener(new AfterTextChanged(){

				@Override
				public void abstract_afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(!isValidEmail(s.toString())){
						error = true;
						email_error.setText("Not a valid email address");
					}
					else if(!isValidString(s.toString()) || !isValidUsername(s.toString())){
						error = true;
						email_error.setText("Invalid !");
						
					}else{
						error = false;
						email_error.setText("");
					}
				}
				
			});
			
			   
			register.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/*
					 *  check for null textfields
					 */
					List<String> list = new ArrayList<String>();
					list.add(fname.getText().toString());
					list.add(lname.getText().toString());
					list.add(mname.getText().toString());
					list.add(username.getText().toString());
					list.add(password.getText().toString());
					list.add(cPassword.getText().toString());
					list.add(email.getText().toString());

					for(String str : list){
						if(!isValidString(str)){
							error = true;
							break;
						}
					}
					
					if(!isValidPassword2Password(list.get(4), list.get(5))){
						error = true;
						password_c_error.setText("Password do not match");
					}
					
					
					if(!error){
						//proceed with registration
						Customer customer = new Customer();
						customer.setFirstname(list.get(0));
						customer.setLastname(list.get(1));
						customer.setMiddlename(list.get(2));
						customer.setUsername(list.get(3));
						customer.setPassword(list.get(4).toCharArray());
						customer.setEmail(list.get(6));
						
						
						
						RegisterTask tas = new RegisterTask(SplashActivity.this, customer);
						tas.setDialogParent(dialog);
						tas.execute(null, null, null);
						
					}else{
						Log.d(TAG, "Sorry theres something wrong with the inputs.");
						AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(SplashActivity.this);
						 // dialogbuilder.setTitle("Login");
						 // dialogbuilder.setIcon(R.drawable.ic_launcher);
						 dialogbuilder.setTitle("Error").setMessage("Please fill all fields")
						 .setIcon(android.R.drawable.ic_delete);
						 AlertDialog dialog = dialogbuilder.create();
						 //dialog.set
						 dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
							}
						});
						   
						 dialog.show();
					}
						
				}
				   
			 });
			
			
			
			   

			 cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
				   
			 });
			   
			 dialog.show();
			Log.d(TAG, "Swipe Down");
			
		}


		@SuppressLint("NewApi")
		@Override
		public void swipeUp() {
			// TODO Auto-generated method stub
			
			error = false;
			


			LayoutInflater inflater = LayoutInflater.from(SplashActivity.this);
			View dialogview = inflater.inflate(R.layout.login_layout, null);
			
			 AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(SplashActivity.this);
			 dialogbuilder.setView(dialogview);
			 final AlertDialog dialog = dialogbuilder.create();
			 
			Button login = (Button) dialogview.findViewById(R.id.btn_login);
			Button cancel = (Button) dialogview.findViewById(R.id.btn_cancel);
			
			final EditText username = (EditText) dialogview.findViewById(R.id.txt_uname);
			final EditText password = (EditText) dialogview.findViewById(R.id.txt_password_login);
			
			final TextView username_error = (TextView) dialogview.findViewById(R.id.txt_uname_error);
			final TextView password_error = (TextView) dialogview.findViewById(R.id.txt_password_login_error);
			
			username.addTextChangedListener(new AfterTextChanged(){

				@Override
				public void abstract_afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(!isValidString(s.toString())){
						error = true;
						username_error.setText("Username is empty");
					}else{
						error = false;
						username_error.setText("");
					}
				}
				
			});
			
			password.addTextChangedListener(new AfterTextChanged(){

				@Override
				public void abstract_afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(!isValidString(s.toString()) || !isValidPassword(s.toString())){
						error = true;
						password_error.setText("Password must be greater than 4 characters");
					}else{
						error = false;
						password_error.setText("");
					}
				}
				
				
			});
			   
			login.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					List<String> list = new ArrayList<String>();
					
					list.add(username.getText().toString());
					list.add(password.getText().toString());
					
					for(String str : list){
						if(!isValidString(str)){
							error = true;
							break;
						}
					}
					
					if(!error){
						
						LoginTask login = new LoginTask(SplashActivity.this, list.get(0), list.get(1));
						login.setParentDialog(dialog);
						login.execute(null, null, null);
						
						/*
						 *  get the registration id if no reg id registered on the device
						 */
						
					}else{
						Log.d(TAG, "Sorry theres something wrong with the inputs.");
						AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(SplashActivity.this);
						 // dialogbuilder.setTitle("Login");
						 // dialogbuilder.setIcon(R.drawable.ic_launcher);
						 dialogbuilder.setTitle("Error").setMessage("Please fill all fields")
						 .setIcon(android.R.drawable.ic_delete);
						 AlertDialog dialog = dialogbuilder.create();
						 //dialog.set
						 dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
							}
						});
						   
						 dialog.show();
					}
						
				}
				   
			 });
			
			
			
			 cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
				   
			 });
			

			   
			 dialog.show();
			   
			   
			Log.d(TAG, "Swipe Up");
		}
        
        
    	
    }
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		// pass the motionevent to touchevent of gesture
		gesture.onTouchEvent(arg1);
		//get the last point
		last_point.x = (int) arg1.getX();
		last_point.y = (int) arg1.getY();
		
		Log.d(TAG, last_point.toString());
		return true;
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	            
	           
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            finish();
	        }
	        return false;
	      
	    }
	    return true;
	    
	}

}
