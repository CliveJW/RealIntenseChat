package za.co.rigaming.realintensechat;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class Automation {

	public static void startAutomaticRefresh(Context context) {

		Intent i = new Intent(context, RefreshService.class);

		PendingIntent pendingIntent = PendingIntent
				.getService(context, 134234, i, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 30);
		alarmManager.setRepeating(AlarmManager.RTC,
				calendar.getTimeInMillis(), 30000, pendingIntent);
	}
	
	public static void stopAutomaticRefresh(Context context) {
		
		Intent i = new Intent(context, RefreshService.class);

		PendingIntent pendingIntent = PendingIntent
				.getService(context, 1, i, 0);
		
		
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

}
