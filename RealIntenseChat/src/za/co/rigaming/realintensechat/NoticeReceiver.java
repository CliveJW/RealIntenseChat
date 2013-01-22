package za.co.rigaming.realintensechat;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NoticeReceiver extends BroadcastReceiver {
  private static final int PM_RECEIVE = 484;
  private static final int DIRECT_RECEIVE = 274;

  @Override
  public void onReceive(Context context, Intent intent) {
   
	  String name = null;
	  String time;
	  String appName = "R.I.G Mobile";
	  String pm_message = "You have received A PM";
	  String direct_message = name + " is chatting to you!";
	  String chat_line = null;
	  
	 
	  
  }
}