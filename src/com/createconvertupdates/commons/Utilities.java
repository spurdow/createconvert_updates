package com.createconvertupdates.commons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.createconvertupdates.entities.Customer;
import com.createconvertupdates.entities.MessageProject;
import com.createconvertupdates.media.SplashActivity;

public class Utilities {
	
	/**
	 *  SQLite DB name
	 */
	public final static String DB_NAME = "createconvert_updates";
	public final static int DB_VERSION = 1;
	
	/**
	 *  Host name or the server base url
	 */
	public final static String HOST_NAME = "http://192.168.1.6/";
	/**
	 *  sender id of the gcm 
	 */
	public final static String SENDER_ID = "601886556185";
	/**
	 *  user name and password for login task
	 */
	public final static String TAG_USERNAME = "username";
	public final static String TAG_PASSWORD = "password";
	
	
	
	/**
	 *  php folder for the current site
	 */
	public final static String PHP_FOLDER = "createconvert_updates_server/";
	
	/**
	 *  PROPERTY REGISTRATION ID
	 */
	public final static String PROPERTY_REGISTRATION_ID = "registration_id";
	
	/**
	 *  URLS
	 */
	
	public final static String REGISTER_URL = HOST_NAME + PHP_FOLDER + "register_customer";
	public final static String LOGIN_URL = HOST_NAME + PHP_FOLDER + "login_customer";
	public final static String REGISTER_REGID_URL = HOST_NAME + PHP_FOLDER + "register_regid";
	
	
	/**
	 *  <for testing purposes>
	 *  URL Keys
	 */
	public final static String REGISTER_KEY_URL = HOST_NAME + PHP_FOLDER + "reg_key";
	public final static String LOGIN_KEY_URL = HOST_NAME + PHP_FOLDER + "login_key";
	public final static String USER_KEY_URL = HOST_NAME + PHP_FOLDER + "user_key";
	
	/**
	 *  Notification Tags
	 */
	public final static String TAG_UPDATES = "Updates";
	public final static String TAG_PROJECTS = "Projects";
	public final static String TAG_PROJECT_UPDATES = "Projects_Updates";
	public final static String TAG_MESSAGES = "Messages";
	
	
	/**
	 *  Notification ID's
	 */
	public final static int CREATECONVERT_NOTIFICATION = 0x0;
	public final static int PROJECT_TABLE_NOTIFICATION = 0x01;
	public final static int MESSAGE_NOTIFICATION = 0x02;
	public final static int PROJECT_METADATA_NOTIFICATION = 0x03;
	
	/**
	 *  Other tags
	 */
	public final static String TAG_REGISTER_RESULT = "register_result";
	public final static String TAG_MESSAGE_RESULT = "message";
	public final static String TAG_DATA_RESULT = "data";
	private static final String TAG = "Utilities";
	

	
	/** 
	 * static bitmap downloader
	 */
	public static Bitmap downloadBitmap(String url) {
	    final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	    final HttpGet getRequest = new HttpGet(url);

	    try {
	        HttpResponse response = client.execute(getRequest);
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) { 
	            Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 
	            return null;
	        }
	        
	        final HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream inputStream = null;
	            try {
	                inputStream = entity.getContent(); 
	                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	                return bitmap;
	            } finally {
	                if (inputStream != null) {
	                    inputStream.close();  
	                }
	                entity.consumeContent();
	            }
	        }
	    } catch (Exception e) {
	        // Could provide a more explicit error message for IOException or IllegalStateException
	        getRequest.abort();
	        Log.w("ImageDownloader", "Error while retrieving bitmap from " + url + " " + e.toString());
	    } finally {
	        if (client != null) {
	            client.close();
	        }
	    }
	    return null;
	}
	/**
	 *  get the spinner selected projects if there is
	 */
	public static List<MessageProject> getSpinnerData(Context context  , List<MessageProject> data){
		SharedPreferences shared = getPreferences(context);
		for(MessageProject mproject: data){
			
			Log.d(TAG, "PROEJCT " + mproject.getId() + " = " + shared.getBoolean("message_is_checked_" + mproject.getId(), false));
			mproject.setCheck(shared.getBoolean("message_is_checked_" + mproject.getId(), false));
			
		}
		
		return data;
		
	}
	/**
	 *  save the spinner selected projects
	 */
	public static void saveSpinnerData(Context context, MessageProject data){
		SharedPreferences shared = getPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		
		if(shared.getLong("project_id_" + data.getId(), -1) != -1){
			editor.putBoolean("message_is_checked_"+data.getId() ,data.isCheck() );
		}else{
			editor.putLong("project_id_" + data.getId(), data.getId());
		}
		editor.commit();
	}
	
	/**
	 *  get no. of notifications
	 */
	public static void removeNotificationCount(Context context , int notification_id , String notif_tags){
		SharedPreferences shared = getPreferences(context);		
		SharedPreferences.Editor editor = shared.edit();
		editor.remove(notif_tags + "" + notification_id);
		editor.commit();
		
	}
	
	/**
	 *  save no. of notifications
	 */
	public static int saveNotificationCount(Context context , int notification_id , String notif_tags){
		SharedPreferences shared = getPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		int count = shared.getInt(notif_tags + "" + notification_id, 0);

		editor.putInt(notif_tags+""+notification_id, ++count);
		
		editor.commit();
		
		return count;
	}
	
	/**
	 *  remove customer logging out 
	 */
	public static void removeSavedCustomer(Context context){
		SharedPreferences shared = getPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		
		editor.remove("id").remove("username")
		.remove("password").remove("email");
		editor.commit();
	}
	
	
	/**
	 *  get customer using preferences
	 */
	public static Customer getSavedCustomer(Context context){
		Customer customer = new Customer();
		SharedPreferences shared = getPreferences(context);
		customer.setId(shared.getInt("id", -1));
		customer.setEmail(shared.getString("email", "no value"));
		customer.setUsername(shared.getString("username", "no value"));
		customer.setPassword(shared.getString("email", "no value").toCharArray());
		customer.setRegid(shared.getString("regid", "no value"));
		
		return customer;
	}
	
	
	/**
	 *  saving shared preferences
	 *  using id , username and password
	 */
	public static void savePreferences(Context context, Customer customer){
		SharedPreferences pref = getPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		if(pref.getInt("id", -1) == -1)
			editor.putInt("id", customer.getId());
		if(pref.getString("email", "no value").equals("no value"))
			editor.putString("email", customer.getEmail());
		if(pref.getString("username", "no value").equals("no value"))
			editor.putString("username", customer.getUsername());
		if(pref.getString("password", "no value").equals("no value"))
			editor.putString("password", customer.getPassword());
		if(isValidString(customer.getRegid())){
			editor.putString("regid", customer.getRegid());
		}
		editor.commit();
	}
	
	/**
	 *  getting shared preferences
	 */
	
	public static SharedPreferences getPreferences(Context context){
		return context.getSharedPreferences(SplashActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}
	
	/**
	 *  result settings
	 */
	public final static String NO_RESULT = "none";
	
	enum Keys{
		Register("REGISTER"),
		Login("LOGIN"),
		;
		
		private String key;
		private Keys(String key){
			this.key = key;
		}
		public String getKey(){return key;}
	}
	/**
	 *  getting the login key
	 */
	public static String getLoginKey(Context context){
		String result = "";
		ConnectionDetector detector = new ConnectionDetector();
		if(!detector.isConnectedToInternet(context)){
			result = NO_RESULT;
		}else{
			result = getKey(Keys.Login);
		}
		
		return result;
	}
	
	/**
	 *  getting the registration key from server side 
	 */
	public static String getRegisterKey(Context context){	
		String result = "";
		ConnectionDetector detector = new ConnectionDetector();
		if(!detector.isConnectedToInternet(context)){
			result = NO_RESULT;
		}else{
			result = getKey(Keys.Register);
		}
		
		return result;
	}
	/**
	 *  getting keys dependant to @param Keys key
	 */
	private static String getKey(Keys key){
		String lkey = "";
		String url = "";
		
		if(key == Keys.Register){
			url = REGISTER_KEY_URL;
		}else if(key == Keys.Login){
			url = LOGIN_KEY_URL;
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
		
	
			HttpResponse response = client.execute(post);
			
			lkey = getContent(response);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			lkey = NO_RESULT;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			lkey = NO_RESULT;
		}
		
		
		return lkey;
	}
	
	/**
	 *  getting http-response as string
	 */
	public static String getContent(HttpResponse response) throws ParseException, IOException{
		String result = "";
		InputStream is = null;
		final HttpEntity entity = response.getEntity();
		is = entity.getContent();
		try {
			
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
            
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        } finally{
        	/**
        	 *  safe removal of inputstream
        	 */
        	if(is != null){
        		is.close();
        	}
        	/**
        	 *  release all allocated resources
        	 */
        	entity.consumeContent();
        }
		
		
		return result;
	}
	

	
	/**
	 *  validate password against confirm password
	 */
	public static boolean isValidPassword2Password(String p1, String p2){
		return p1.equals(p2);
	}
	
	/**
	 *  validate username 
	 */
	public static boolean isValidUsername(String str_sequence){
		return str_sequence.length() > 0 && str_sequence.length() < 100;
	}
	
	/**
	 *  validate password
	 */
	public static boolean isValidPassword(String str_sequence){
		return str_sequence.length() > 4 && str_sequence.length() < 50;
	}
	
	/**
	 *  validate empty strings
	 */
	@SuppressLint("NewApi")
	public static boolean isValidString(String str_sequence){
		if(str_sequence == null || str_sequence.equals("")  || str_sequence.isEmpty()){
			return false;
		}
		
		return true;
	}
	
	/**
	 *  validate email address
	 */
	public static boolean isValidEmail(String email){
		Pattern pattern;
		Matcher matcher;
	         String regExpn =
	             "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
	                 +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
	                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
	                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
	                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
	                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
	
	     CharSequence inputStr = email;
	
	     pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
	     matcher = pattern.matcher(inputStr);
	
	     if(matcher.matches())
	        return true;
	     else
	        return false;
	}
}
