package za.co.rigaming.realintensechat;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import za.co.rigaming.realintensechat.GeneralSettings.Settings;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GetUserDetails extends AsyncTask<Object, String, String> {
	
	public class User {
		String id;
		String name;
		String rank;
		String unreadPM;
		String rankColour;
		String directColor;
	}
	
	Context context = ChatView.context;
	Settings set = Settings.getSettings(context);
	@Override
	protected String doInBackground(Object... params) {
		
		if (!ChatView.input.isInputMethodTarget()) {
		
		HttpPost poster = new HttpPost(
				"http://www.rigaming.co.za/getCurrentUser.php");

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = null;

		try {
			responseBody = Http.createClient(Stickies.getSession(context, "RIGSESS")).execute(poster,
					responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			User user = new User();
			JSONObject response = new JSONObject(responseBody);
			Log.i("UserDetails", response.toString());
			user.id = response.getString("id");
			user.name = response.getString("name");
			user.rank = response.getString("rank");
			user.directColor = response.getString("nameHilite");
			user.rankColour = response.getString("color");
			user.unreadPM = response.getString("unreadPms");
			Stickies.setUser(user);
			
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		    List<RunningTaskInfo> services = activityManager
		            .getRunningTasks(Integer.MAX_VALUE);
		    boolean isActivityFound = false;

		    if (services.get(0).topActivity.getPackageName().toString()
		            .equalsIgnoreCase(context.getPackageName().toString())) {
		        isActivityFound = true;
		    }

		    if (isActivityFound) {
		    	publishProgress();
		    } else {
		    	if (set.pvt_switch){
		    	Intent pop_msg = new Intent(context, ChatView.class);
		    	pop_msg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				createNotification(context, pop_msg, null, "R.I.G Mobile", "You have " + user.unreadPM.toString() + " unread PM's", 99945);
				
					doNotify();
				}
				publishProgress();
		    }

			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		ChatView.username.setText(Stickies.user.name);
		ChatView.username.setTextColor(Color
				.parseColor(Stickies.user.rankColour));
		ChatView.unread.setText(Stickies.user.unreadPM);
		
		new GetChatMessages().execute();
		super.onProgressUpdate(values);
	}
	public static void createNotification(Context context, Intent intent,
			String action, String title, String message, int notificationID) {

		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);
		
		Notification noti = new NotificationCompat.Builder(context)
				.setContentTitle(title).setContentText(message)
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent)
				.build();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(notificationID, noti);

	}
	
	public static void doNotify() {
		try {
			Ringtone r = RingtoneManager.getRingtone(ChatView.context,
					RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			Vibrator v = (Vibrator) ChatView.context
					.getSystemService(Context.VIBRATOR_SERVICE);

			r.play();
			v.vibrate(1000);
		} catch (Exception e) {
		}
	}


}
