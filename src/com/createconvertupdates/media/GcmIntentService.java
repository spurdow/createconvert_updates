package com.createconvertupdates.media;

import java.util.List;

import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.dbentities.ProjectHelper;
import com.createconvertupdates.dbentities.ProjectMetaDataHelper;
import com.createconvertupdates.entities.Project;
import com.createconvertupdates.entities.ProjectMetaData;
import com.createconvertupdates.media.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static com.createconvertupdates.commons.Utilities.*;

public class GcmIntentService extends IntentService {
	private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    
    

    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}



	@Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        Log.d("INTENT SERVICE", "Handled Intent CALLED");

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	/**
            	 *  generate notification only if notify is not null 
            	 *  and notify is true
            	 */
            	if(extras.getString("notify") != null && Boolean.valueOf(extras.getString("notify"))){
	            	if(extras.getString("notification_id") != null){
	            		generateNotification(Integer.valueOf(extras.getString("notification_id")) , extras);
	            	}
            	}

                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        Log.d("INTENT SERVICE", "CoMplete WAKEFUL");
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    
	/**
	 * the generality of generating notification
	 * @param notification_id
	 * @param extras
	 */
	private void generateNotification(int notification_id , Bundle extras){
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		if(extras == null) return;
		if(extras.getString("notify") == null) return;
		
		switch(notification_id){
		case Utilities.CREATECONVERT_NOTIFICATION: break;
		case Utilities.PROJECT_TABLE_NOTIFICATION: 
				generateProjectTableNotification(extras , notification_id);
			break;
		case Utilities.MESSAGE_NOTIFICATION: break;
		case Utilities.PROJECT_METADATA_NOTIFICATION: 
				generateProjectUpdatesNotification(extras , notification_id);
			break;
		default: break;
		}
	}

	/*
	 *  generate project table notifications
	 */
	@SuppressLint("DefaultLocale")
	private void generateProjectTableNotification(Bundle extras , int notification_id){
   		if(Boolean.valueOf(extras.getString("notify"))){
    		ProjectHelper helper = new ProjectHelper(this.getApplicationContext());
    		List<Project> projects = helper.getAll();
    		helper.close();
    		String name = extras.getString("name").trim();
    		
    		for(Project p : projects){
    			/**
    			 *  project already added 
    			 *  no insertion of database and no notification
    			 */
    			if(p.getName().toLowerCase().equals(name.toLowerCase())){
    				Log.d(TAG, "has been inserted already " + p.getName());
    				return;
    			}
    		}
    		
    		Project project = new Project();
    		project.setId(Integer.parseInt(extras.getString("id")));
    		project.setName(extras.getString("name"));
    		
    		project.setWebsite(extras.getString("website"));
    		project.setImagePath(extras.getString("image"));
    		project.setSlogan(extras.getString("slogan"));
    		project.setDate(extras.getString("date_created"));
    		project.setStatus(Integer.parseInt(extras.getString("status")));
    		long id = helper.add(project);
    		Log.d(TAG, project.getWebsite() + " = WEBSITE ");
    		
    		/*
    		 *  send broadcast to project updates
    		 */
    		Intent i = new Intent("add_project");
    		i.putExtra("id", id);
    		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    		
    		Intent intent = new Intent(this , HomeFragmentActivity.class);
    		intent.putExtra(TAG_PROJECTS,notification_id);
    		intent.putExtra("state", 1);
    		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
    		
    		/*
    		 *  save and get the count of notification 
    		 */
    		int count = saveNotificationCount(getApplicationContext() , notification_id , TAG_PROJECTS);
    		String contentText = "";
    		if(count > 0){
    			contentText = "New Projects Added!";
    		}else{
    			contentText = "Project " + project.getName().toUpperCase() + " added successfully!";
    		}
    		/*
    		 *  notification side
    		 */
            final NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
            //.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher))
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Create Convert Media ltd.")
            .setNumber(count)
            .setContentText(contentText);
            
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(notification_id, mBuilder.build());
   		}
	}
	
	private void generateProjectUpdatesNotification(Bundle extras , int notification_id){
		if(Boolean.valueOf(extras.getString("notify"))){
			/**
			 *  add the project meta data to database 
			 */
			ProjectMetaDataHelper helper = new ProjectMetaDataHelper(getApplicationContext());
						
			ProjectMetaData pmd = new ProjectMetaData();
			pmd.setProject_id(Integer.parseInt(extras.getString("project_id")));
			pmd.setUpdate_message(extras.getString("update_message"));
			pmd.setDate_received(extras.getString("date_received"));
			pmd.setStatus(Integer.parseInt(extras.getString("status")));
			
			long id = helper.add(pmd);
			long project_id = pmd.getProject_id();
			helper.close();
			
			/*
    		 *  send broadcast to project meta data message updates
    		 */
    		Intent i = new Intent("new_project_message");
    		i.putExtra("id", id);
    		i.putExtra("project_id", project_id);
    		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
			
    		Intent intent = new Intent(this , ProjectFragment.class);
    		intent.putExtra("project_id", project_id);
    		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT);
    		
    		/*
    		 *  get the project
    		 */
    		ProjectHelper projectHelper = new ProjectHelper(getApplicationContext());
    		Project getProject = projectHelper.get(pmd.getProject_id());
    		projectHelper.close();
    		/*
    		 *  notification side
    		 */
    		
    		
            final NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher))
            .setSmallIcon(android.R.drawable.btn_star)
            .setContentTitle("Create Convert Media ltd.")
            .setContentText(getProject.getName() + " updated!");
            
            
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(notification_id, mBuilder.build());
		}
	}
	
    


}