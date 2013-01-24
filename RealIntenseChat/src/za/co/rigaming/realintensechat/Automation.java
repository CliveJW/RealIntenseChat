package za.co.rigaming.realintensechat;

import java.util.Calendar;

import za.co.rigaming.realintensechat.GeneralSettings.Settings;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Automation {
	
	

	public static void startAutomaticRefresh(Context context) {
		Settings set = Settings.getSettings(context);
		Intent i = new Intent(context, RefreshService.class);

		PendingIntent pendingIntent = PendingIntent
				.getService(context, 134234, i, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 30);
		alarmManager.setRepeating(AlarmManager.RTC,
				calendar.getTimeInMillis(), set.refresh_rate * 1000, pendingIntent);
	}
	
	public static void stopAutomaticRefresh(Context context) {
		
		Intent i = new Intent(context, RefreshService.class);

		PendingIntent pendingIntent = PendingIntent
				.getService(context, 134234, i, PendingIntent.FLAG_CANCEL_CURRENT);
		
		
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

}
