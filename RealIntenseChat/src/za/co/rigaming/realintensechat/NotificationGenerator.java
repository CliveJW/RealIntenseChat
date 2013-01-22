package za.co.rigaming.realintensechat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;

public class NotificationGenerator {
	
	public static boolean downloadNotiRunning = false;

	public static void createNotification(Context context, String title,
			String message, int notificationID) {

		Notification noti = new NotificationCompat.Builder(context)
				.setContentTitle(title).setContentText(message)
				.setSmallIcon(R.drawable.ic_launcher).setTicker(message)
				.build();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(notificationID, noti);

	}

	public static void createNotification(Context context, Intent intent,
			String action, String title, String message, int notificationID) {

		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);
		
		Notification noti = new NotificationCompat.Builder(context)
				.setContentTitle(title).setContentText(message)
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent)
				.addAction(R.drawable.ic_launcher, action, pIntent).build();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(notificationID, noti);

	}

	

}
