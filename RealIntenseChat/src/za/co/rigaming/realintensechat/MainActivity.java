package za.co.rigaming.realintensechat;

import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CookieSyncManager.createInstance(this);
		
		Button login, getUsers, getMessages;
		
		login = (Button) findViewById(R.id.login);
		getUsers = (Button) findViewById(R.id.users);
		getMessages = (Button) findViewById(R.id.messages);
		
		
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent startLogin = new Intent(MainActivity.this, WebviewForLogin.class);
				startActivity(startLogin);
				
			}
		});
		
		getUsers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startGettingUsers = new Intent(MainActivity.this, GetUsers.class);
				startActivity(startGettingUsers);
			}
		});
		
		getMessages.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startGettingMessages = new Intent(MainActivity.this, GetMessages.class);
				startActivity(startGettingMessages);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
