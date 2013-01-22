package za.co.rigaming.realintensechat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RefreshService extends Service {

	
	
	public void onCreate() {
		
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new AsyncRefresh().execute();
		stopSelf();// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
