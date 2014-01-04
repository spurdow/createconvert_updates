package com.createconvertupdates.media;

import com.createconvertupdates.commons.Utilities;
import com.createconvertupdates.dbentities.ProjectHelper;
import com.createconvertupdates.entities.Project;
import com.createconvertupdates.media.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

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
    public static final int NOTIFICATION_ID = 1;
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
		case Utilities.PROJECT_METADATA_NOTIFICATION: break;
		default: break;
		}
	}

	/*
	 *  generate project table notifications
	 */
	private void generateProjectTableNotification(Bundle extras , int notification_id){
   		if(Boolean.valueOf(extras.getString("notify"))){
    		ProjectHelper helper = new ProjectHelper(this.getApplicationContext());
    		Project project = new Project();
    		project.setId(Integer.parseInt(extras.getString("id")));
    		project.setName(extras.getString("name"));
    		project.setImagePath(extras.getString("image"));
    		project.setSlogan(extras.getString("slogan"));
    		project.setDate(extras.getString("date"));
    		project.setStatus(Integer.parseInt(extras.getString("status")));
    		long id = helper.add(project);
    		Log.d(TAG, helper.getAll().size() + "");
    		
    		/*
    		 *  send broadcast to project updates
    		 */
    		Intent i = new Intent("add_project");
    		i.putExtra("id", id);
    		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    		
    		Intent intent = new Intent(this , HomeFragmentActivity.class);
    		intent.putExtra("state", 1);
    		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
    		
    		/*
    		 *  notification side
    		 */
            final NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher))
            .setContentTitle("Create Convert Media ltd.")
            .setContentText("Project " + project.getName() + " added successfully!");
            
            
            
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(notification_id, mBuilder.build());
   		}
	}
	
    


}