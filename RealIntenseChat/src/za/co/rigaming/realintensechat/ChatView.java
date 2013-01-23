package za.co.rigaming.realintensechat;

import java.util.Calendar;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import za.co.rigaming.realintensechat.Automation;
import za.co.rigaming.realintensechat.GeneralSettings.Settings;

import com.slidingmenu.*;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class ChatView extends SlidingActivity {

	public class User {
		String id;
		String name;
		String rank;
		String unreadPM;
		String rankColour;
		String directColor;
	}

	static Button post;
	static EditText input;

	static User user;
	static TextView username;
	static TextView pvtResip;
	static TextView unread;
	static ListView lv;
	static Handler mHandler;
	static ListView mainListView;
	static ViewGroup vg;
	static LayoutInflater mInflater;
	static ScrollView sv;
	static JSONArray jsonMainArr;
	static String userNamePvt;
	static Context context;
	static DefaultHttpClient dhc;
	static ImageView imgV;
	// static String lastID;
	static ProgressBar pg;
	static Switch pm;
	static Switch pvt;
	static Switch msg;
	static Settings settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		View v = getWindow().getDecorView();
		this.setSlidingActionBarEnabled(true);
		setContentView(R.layout.postmessage);
		setBehindContentView(R.layout.list);
		
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.actionbar_home_width);
		
		v = getWindow().getDecorView();
	
		CookieSyncManager.createInstance(this);
		settings = Settings.getSettings(getApplicationContext());
		
		Log.i("APP SETTINGS", settings.msg_switch);
		Log.i("APP SETTINGS", settings.pm_switch);
		Log.i("APP SETTINGS", settings.pvt_switch);
		

		mHandler = new Handler();

		user = new User();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		pg = (ProgressBar) findViewById(R.id.progressBarjkjkj1);
		imgV = (ImageView) findViewById(R.id.imageView1);
		float f = (float) 0.1;
		imgV.setAlpha(f);
		post = (Button) findViewById(R.id.button1);
		input = (EditText) findViewById(R.id.chat_input);
		Stickies.chatView = (TextView) findViewById(R.id.chatView);
		Stickies.chatView.setTextSize(11);
		Stickies.chatView.setMovementMethod(new ScrollingMovementMethod());

		username = (TextView) findViewById(R.id.user);
		unread = (TextView) findViewById(R.id.unread);

		mainListView = (ListView) findViewById(R.id.userlist);
		sv = (ScrollView) findViewById(R.id.scrollView1);
		
		pvt = (Switch) findViewById(R.id.pvt_switch);
		msg = (Switch) findViewById(R.id.msg_switch);
		pm = (Switch) findViewById(R.id.pms_switch);
		
		setSettings();
		
		registerForContextMenu(mainListView);

		// Set the ArrayAdapter as the ListView's adapter.
		mainListView.setAdapter(GetUsers.userListAdapter);

		mainListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String userName = (String) mainListView
						.getItemAtPosition(position);
				if (input.getText().length() == 0) {
					input.setText("[" + userName + "] ");
					input.setSelection(input.getText().length());
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				} else {
					input.setText(input.getText() + " " + "[" + userName + "] ");
					input.setSelection(input.getText().length());
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				}

			}

		});

		post.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new GetChatMessages().execute();
				// input.setText("");
				// mHandler.postDelayed(chatRefresh, 5000);

			}
		});

		input.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					new GetChatMessages().execute();
					// input.setInputType(InputType.TYPE_NULL);
					getWindow().setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

				}
				return false;
			}
		});
		
		pm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					settings.pm_switch = "true";
					settings.saveSettings(context);
				} else {
					settings.pm_switch = "false";
					settings.saveSettings(context);
				}
				
				
			}
		});
		
		pvt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					settings.pvt_switch = "true";
					settings.saveSettings(context);
				} else {
					settings.pvt_switch = "false";
					settings.saveSettings(context);
				}
				
				
			}
		});
		
		msg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					settings.msg_switch = "true";
					settings.saveSettings(context);
				} else {
					settings.msg_switch = "false";
					settings.saveSettings(context);
				}
				
				
			}
		});

		// mHandler.post(GetChatMessages.chatRefresh);

		Automation.startAutomaticRefresh(context);
		new GetUserDetails().execute();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// Get the info on which item was selected
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

		// Get the Adapter behind your ListView (this assumes you're using
		// a ListActivity; if you're not, you'll have to store the Adapter
		// yourself
		// in some way that can be accessed here.)

		// Retrieve the item that was clicked on
		userNamePvt = (String) mainListView.getItemAtPosition(info.position);
		Log.i("PVT NAME!!!!", userNamePvt);
		menu.add(0, 987, 0, "Send Whisper To: " + userNamePvt);
	};

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// Here's how you can get the correct item in onContextItemSelected()
		if (item.getItemId() == 987) {
			ShowWhisperScreen();
		}
		return false;
	};

	public void ShowWhisperScreen() {

		LayoutInflater factory = LayoutInflater.from(this);

		final View textEntryView = factory.inflate(R.layout.whisper, null);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setInverseBackgroundForced(true);

		alert.setView(textEntryView);

		pvtResip = (EditText) textEntryView.findViewById(R.id.whisp_text);

		alert.setPositiveButton("Submit", new DialogInterface.OnClickListener()

		{

			public void onClick(DialogInterface dialog, int whichButton) {

				String pvt_txt = (String) pvtResip.getText().toString();

				for (int i = 0; i < GetUsers.userResults.length(); i++) { // **line
																			// 2**
					try {
						JSONObject childJSONObject = GetUsers.userResults
								.getJSONObject(i);
						String name = childJSONObject.getString("name");
						Log.i("FOUNDNAME", name);
						String id = childJSONObject.getString("id");
						Log.i("GOT ID", id);

						if (name.equals(userNamePvt)) {
							input.setText(pvt_txt);
							new GetChatMessages().execute(id);
							// input.setText("");
							return;
						}
					} catch (JSONException je) {
						je.printStackTrace();
					}
				}

			}

		});

		alert.show();

	};

	public static void doMsg() {

		new GetUserDetails().execute();

	}

	@Override
	protected void onResume() {
		CookieSyncManager.createInstance(this);
		mHandler.post(GetChatMessages.run);
		super.onResume();
	}

	@Override
	protected void onPause() {
		CookieSyncManager.getInstance().stopSync();
		super.onPause();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	protected void onRestart() {

		super.onRestart();
	}
	
	public void setSettings() {
		Settings set = Settings.getSettings(context);
		
		if (set.msg_switch.equals("true")) {
			msg.setChecked(true);
		} else {
			msg.setChecked(false);
		}
		
		if (set.pvt_switch.equals("true")) {
			pvt.setChecked(true);
		} else {
			pvt.setChecked(false);
		}
		
		if (set.pm_switch.equals("true")) {
			pm.setChecked(true);
		} else {
			pm.setChecked(false);
		}
	}

}