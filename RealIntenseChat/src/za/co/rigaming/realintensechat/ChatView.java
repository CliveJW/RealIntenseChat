package za.co.rigaming.realintensechat;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.co.rigaming.realintensechat.GeneralSettings.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

@SuppressLint("NewApi")
public class ChatView extends SlidingActivity {
	
	private boolean doubleBackToExitPressedOnce = false;
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
	static Spinner spinner;
	static Spinner textSizeSpinner;
	static Switch refresh;
	static Settings settings;
	static CheckBox screen_on;
	static CheckBox comp_pm;
	static CheckBox comp_pvt;
	static CheckBox comp_msg;
	static CheckBox comp_refresh;
	static ArrayAdapter<CharSequence> adapter;
	static ArrayAdapter<CharSequence> textSizeAdapter;
	static SlidingMenu sm;
	static SlidingMenu sm2;
	int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		this.setSlidingActionBarEnabled(true);
		setContentView(R.layout.postmessage);
		setBehindContentView(R.layout.list);

		sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.actionbar_home_width);		
		sm.setFadeDegree(0.9f);
		sm.setBehindScrollScale(0.7f);
		
//		SlidingMenu menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.RIGHT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setBehindOffsetRes(R.dimen.actionbar_home_width);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
//        //menu.setMenu(R.layout.list);
		
		CookieSyncManager.createInstance(this);
		settings = Settings.getSettings(getApplicationContext());
		
		Log.i("APP SETTINGS", String.valueOf(settings.msg_switch));
		Log.i("APP SETTINGS", String.valueOf(settings.pm_switch));
		Log.i("APP SETTINGS", String.valueOf(settings.pvt_switch));
		

		mHandler = new Handler();

		user = new User();
		if (ICSOrNewer()) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		}

		pg = (ProgressBar) findViewById(R.id.progressBarjkjkj1);
		imgV = (ImageView) findViewById(R.id.imageView1);
		float f = (float) 0.1;
		if (ICSOrNewer()) {
		imgV.setAlpha(f);
		} else {
			imgV.setAlpha(10);
		}
		post = (Button) findViewById(R.id.button1);
		input = (EditText) findViewById(R.id.chat_input);
		Stickies.chatView = (TextView) findViewById(R.id.chatView);
		
		Stickies.chatView.setMovementMethod(new ScrollingMovementMethod());

		username = (TextView) findViewById(R.id.user);
		unread = (TextView) findViewById(R.id.unread);

		mainListView = (ListView) findViewById(R.id.userlist);
		sv = (ScrollView) findViewById(R.id.scrollView1);
		
		if (ICSOrNewer()) {
			pvt = (Switch) findViewById(R.id.pvt_switch);
			msg = (Switch) findViewById(R.id.msg_switch);
			pm = (Switch) findViewById(R.id.pms_switch);
			refresh = (Switch) findViewById(R.id.refresh_switch);
		} else {
			comp_pvt = (CheckBox) findViewById(R.id.pvt_switch);
			comp_msg = (CheckBox) findViewById(R.id.msg_switch);
			comp_pm = (CheckBox) findViewById(R.id.pms_switch);
			comp_refresh = (CheckBox) findViewById(R.id.refresh_switch);
		}
		
		spinner = (Spinner) findViewById(R.id.refresh_time_spinner);
		adapter = ArrayAdapter.createFromResource(this,
		        R.array.refresh_time_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		textSizeSpinner = (Spinner) findViewById(R.id.text_size);
		textSizeAdapter = ArrayAdapter.createFromResource(this,
		        R.array.text_size_array, android.R.layout.simple_spinner_item);
		textSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		textSizeSpinner.setAdapter(textSizeAdapter);
		
		screen_on = (CheckBox) findViewById(R.id.screen_on_check);
		
		
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
		if (ICSOrNewer()) {
		pm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				settings.pm_switch = isChecked;
				settings.saveSettings(context);
				setSettings();
			
				
				
			}
		});
		
		pvt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
					settings.pvt_switch = isChecked;
					settings.saveSettings(context);
					setSettings();
				
				
				
			}
		});
		
		msg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				settings.msg_switch = isChecked;
				settings.saveSettings(context);
				setSettings();
			
				
			}
		});
		
		refresh.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				settings.refresh = isChecked;
				settings.saveSettings(context);
				setSettings();
			
				
				
			}
		});
		
		} else {
			comp_pm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					settings.pm_switch = isChecked;
					settings.saveSettings(context);
					setSettings();
				
					
					
				}
			});
			
			comp_pvt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
						settings.pvt_switch = isChecked;
						settings.saveSettings(context);
						setSettings();
					
					
					
				}
			});
			
			comp_msg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					settings.msg_switch = isChecked;
					settings.saveSettings(context);
					setSettings();
				
					
				}
			});
			
			comp_refresh.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					settings.refresh = isChecked;
					settings.saveSettings(context);
					setSettings();
				
					
					
				}
			});
		}
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        Object item = parent.getItemAtPosition(pos);
		        String selection = (String) item;
		        settings.refresh_rate = Integer.valueOf(selection);
		        settings.saveSettings(context);
		        setSettings();
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
		
		textSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        Object item = parent.getItemAtPosition(pos);
		        String selection = (String) item;
		        settings.text_size = Integer.valueOf(selection);
		        settings.saveSettings(context);
		        setSettings();
		        Stickies.chatView.setTextSize(settings.text_size);
		        Stickies.chatView.invalidate();
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
		
		screen_on.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
					settings.screen_on = isChecked;
					settings.saveSettings(context);
					setSettings();
				
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
	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (sm.isMenuShowing()) {
				sm.showContent(true);
			} else {
				sm.showMenu(true);
			}
	    } else if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	 if (doubleBackToExitPressedOnce) {
	             super.onBackPressed();
	             return true;
	         }
	         this.doubleBackToExitPressedOnce = true;
	         Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
	         new Handler().postDelayed(new Runnable() {

	             @Override
	             public void run() {
	              doubleBackToExitPressedOnce=false;   

	             }
	         }, 2000);
	    }
	    return true;
	}
		
	
	
	
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
		
		if (ICSOrNewer()) {
			msg.setChecked(set.msg_switch);

			pvt.setChecked(set.pvt_switch);

			pm.setChecked(set.pm_switch);
		} else {
			comp_msg.setChecked(set.msg_switch);

			comp_pvt.setChecked(set.pvt_switch);

			comp_pm.setChecked(set.pm_switch);
		}
		
		int spinnerPosition = adapter.getPosition(String.valueOf(set.refresh_rate));
		spinner.setSelection(spinnerPosition);
		
		screen_on.setChecked(set.screen_on);
		setScreenOn(set.screen_on);
		
		int textSize = textSizeAdapter.getPosition(String.valueOf(set.text_size));
		textSizeSpinner.setSelection(textSize);

	if (ICSOrNewer()) {	
		if (set.refresh) {
			Automation.stopAutomaticRefresh(context);
			Automation.startAutomaticRefresh(context);
			refresh.setChecked(set.refresh);
		} else {
			Automation.stopAutomaticRefresh(context);
			refresh.setChecked(set.refresh);
		}
	} else {
		if (set.refresh) {
			Automation.stopAutomaticRefresh(context);
			Automation.startAutomaticRefresh(context);
			comp_refresh.setChecked(set.refresh);
		} else {
			Automation.stopAutomaticRefresh(context);
			comp_refresh.setChecked(set.refresh);
		}
	}
	}
	
	public void setScreenOn(boolean on) {
		if (on) {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
			params.screenBrightness = 1;
			getWindow().setAttributes(params);
		} else {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.screenBrightness = -1;
			getWindow().setAttributes(params);
		}
	}
	
	public static boolean ICSOrNewer() {
	    // SDK_INT is introduced in 1.6 (API Level 4) so code referencing that would fail
	    // Also we can't use SDK_INT since some modified ROMs play around with this value, RELEASE is most versatile variable
	    if (android.os.Build.VERSION.RELEASE.startsWith("4."))
	        return true;

	    return false;
	}
	
	

}