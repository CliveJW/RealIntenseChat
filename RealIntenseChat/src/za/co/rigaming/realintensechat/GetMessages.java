package za.co.rigaming.realintensechat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.text.InputType;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.TextView.OnEditorActionListener;

public class GetMessages extends ListActivity {

	Button post;
	EditText input;
	TextView chatView;
	static DefaultHttpClient dhc = AppSettings.getClient();
	ScrollView chat_ScrollView;
	String lastID;
	User user; 
	TextView username;
	TextView unread;
	ListView lv;
	Handler mHandler;
	ListView mainListView;
	ArrayAdapter<String> listAdapter; 
	ArrayList<String> userList;
	ViewGroup vg;
	LayoutInflater mInflater;
	String color;
	ScrollView sv ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		mHandler = new Handler();
		
		user = new User();
		userList = new ArrayList<String>();  
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.postmessage);

		post = (Button) findViewById(R.id.button1);
		input = (EditText) findViewById(R.id.chat_input);
		chatView = (TextView) findViewById(R.id.chatView);
		chatView.setTextSize(11);
		chatView.setMovementMethod(new ScrollingMovementMethod());
		
		username = (TextView) findViewById(R.id.user);
		unread = (TextView) findViewById(R.id.unread);
		
		mainListView = (ListView) findViewById( R.id.userlist );
		sv = (ScrollView) findViewById(R.id.scrollView1);
		
// Set the ArrayAdapter as the ListView's adapter.  
		mainListView.setAdapter( listAdapter );  

		mainListView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
			      String userName = (String) mainListView.getItemAtPosition(position);
			      if (input.getText().length() == 0) {
			    	  input.setText("[" + userName + "] ");
			    	  input.setSelection(input.getText().length());
			      } else {
			    	  input.setText(input.getText() + " " + "[" + userName + "] ");  
			    	  input.setSelection(input.getText().length());
			      }
			      
			      
			    }
		});
				
		post.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mHandler.removeCallbacks(chatRefresh);
				new GetChatMessages().execute();
				input.setText("");
				//mHandler.postDelayed(chatRefresh, 5000);
				

			}
		});
		

		input.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		        	mHandler.removeCallbacks(chatRefresh);
		            doMsg();
		           // input.setInputType(InputType.TYPE_NULL);
		            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		            input.setText("");
		            
		        }
		        return false;
		    }
		});
		
		input.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (input.isInputMethodTarget()) {
					mHandler.removeCallbacks(chatRefresh);
				} 
				
			}
		});
		
		mHandler.post(chatRefresh);
		new getUserDetails().execute();
		super.onCreate(savedInstanceState);

	}
	
	private class User {
		String id;
		String name;
		String rank;
		String unreadPM;
		String rankColour;
		String directColor;
	}

	private class Message {
		String Text;
		String time;
		int userid;
		int from;
		boolean pvt;
		String sender;
		String sendercolor;

	}

	private class postMessage {
		String message;
		String chat = "2";
		String last = lastID;
		String userid = user.id;

		private UrlEncodedFormEntity createPostFormData() {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("chat", chat));
			nameValuePairs.add(new BasicNameValuePair("message", message));
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
			nameValuePairs.add(new BasicNameValuePair("last", last));
			UrlEncodedFormEntity form = null;
			try {
				form = new UrlEncodedFormEntity(nameValuePairs);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return form;

		}

	}

	class GetChatMessages extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			
			new getUserDetails().execute();

			try {

				// Execute HTTP Post Request
				postMessage pm = new postMessage();
				pm.message = input.getText().toString();
				HttpPost poster = new HttpPost(
						"http://www.rigaming.co.za/getChat.php?");
				poster.setEntity(pm.createPostFormData());

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = dhc.execute(poster, responseHandler);
				JSONObject response = new JSONObject(responseBody);

				ArrayList<String> list = new ArrayList<String>();
				JSONArray jsonMainArr = response.getJSONArray("messages");
				Construct construct = new Construct();
				List<Message> msgList = new ArrayList<GetMessages.Message>();
				for (int i = 0; i < jsonMainArr.length(); i++) { // **line 2**
					JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
					Message msg = new Message();
					msg.Text = childJSONObject.getString("text");
					msg.time = childJSONObject.getString("time");
					msg.userid = childJSONObject.getInt("userid");
					msg.sender = childJSONObject.getString("user");
					msg.time = childJSONObject.getString("time");
					msg.sendercolor = childJSONObject.getString("color");
					msgList.add(msg);
					
					lastID = childJSONObject.getString("messageid");
					Log.i("UserMessage", childJSONObject.toString() + "\n");
				}
				Log.i("Msg List Size", String.valueOf(msgList.size()));
				for (Message i : msgList) {
					i.Text = i.Text.replaceAll("(<[^>]+>)", "{No Smiley}");
					Log.i("MessageText",i.Text
							 );
					
						String name = "<font color=" + i.sendercolor + ">:" + i.sender + ": " + "</font><br></a>";
							
							String spanToBe = null;
						
							if (i.Text.contains(user.name))  {
								 
								i.Text = "<font color=" + user.directColor + ">" + i.Text + "</font><br>";
								spanToBe = name + i.Text;
							publishProgress(spanToBe);
							
							} else {
								spanToBe = name + i.Text;
								publishProgress(spanToBe);
							}
							
							
							
					
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			appendTextAndScroll(values[0].toString());
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Object result) {
			mHandler.removeCallbacks(chatRefresh);
			mHandler.postDelayed(chatRefresh, 30000);
			super.onPostExecute(result);
		}

	}
	
	public class getUserDetails extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {
			HttpPost poster = new HttpPost(
					"http://www.rigaming.co.za/getCurrentUser.php");
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = null;
		
				try {
					responseBody = dhc.execute(poster, responseHandler);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			try {
				JSONObject response = new JSONObject(responseBody);
				Log.i("UserDetails", response.toString());
				user.id = (String) response.get("id");
				user.name = (String) response.get("name");
				user.rank = response.getString("rank");
				user.directColor = response.getString("nameHilite");
				user.rankColour = response.getString("color");
				user.unreadPM = response.getString("unreadPms");
				publishProgress();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			username.setText(user.name);
			username.setTextColor(Color.parseColor(user.rankColour));
			unread.setText(user.unreadPM);
			
			super.onProgressUpdate(values);
		}
		
	}

	private void appendTextAndScroll(String text) {
		
		
		if (chatView != null) {
			chatView.append(Html.fromHtml("<p>" + text + "</p>"));
			
			sv.fullScroll(View.FOCUS_DOWN);
		}
		
		
	}
	
	public static CharSequence setSpanBetweenTokens(CharSequence text,
		    String token, CharacterStyle... cs)
		{
		    // Start and end refer to the points where the span will apply
		    int tokenLen = token.length();
		    int start = text.toString().indexOf(token) + tokenLen;
		    int end = text.toString().indexOf(token, start);

		    if (start > -1 && end > -1)
		    {
		        // Copy the spannable string to a mutable spannable string
		        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
		        for (CharacterStyle c : cs)
		            ssb.setSpan(c, start, end, 0);

		        // Delete the tokens before and after the span
		        ssb.delete(end, end + tokenLen);
		        ssb.delete(start - tokenLen, start);

		        text = ssb;
		    }

		    return text;
		}
	
	private Runnable chatRefresh = new Runnable() {
        public void run() {
        	
    		new GetChatMessages().execute();
    		new PopulateUsers().execute();
            mHandler.postDelayed(chatRefresh, 30000);
        }
    };
    
    class PopulateUsers extends AsyncTask<Object, Object, Object> {
    	
    	
    	
    	@Override
    	protected void onProgressUpdate(Object... values) {
    		
    		
        	
        	Context context = getApplicationContext();
        	
    		listAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_view, userList) {
    			
    			
    			
    			@Override
    	        public View getView(int position, View convertView, ViewGroup parent) {
    				
    	            View view =super.getView(position, convertView, parent);

    	            TextView textView= (TextView) view.findViewById(android.R.id.text1);
    	            try {
    	            /*YOUR CHOICE OF COLOR*/
    	            textView.setTextColor(Color.parseColor(color));

    	            return view;
    	            }  catch (NullPointerException npe) {
    	            	
    	            }
    	            return view;
    	        }
    		}; 
    		mainListView.setAdapter( listAdapter ); 
    		super.onProgressUpdate(values);
    	}

		@Override
		protected Object doInBackground(Object... params) {
			
			try {
				
				
			
			HttpPost poster = new HttpPost(
					"http://www.rigaming.co.za/getUsers.php?users");
			//poster.setEntity(pm.createPostFormData());

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = dhc.execute(poster, responseHandler);
			JSONObject response = new JSONObject(responseBody);
			Log.i("Users Response", response.toString());

			JSONArray jsonMainArr = response.getJSONArray("users");
			userList = new ArrayList<String>();
			for (int i = 0; i < jsonMainArr.length(); i++) { // **line 2**
				JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
				String name = childJSONObject.getString("name");
				Log.i("USERNAME", name);
				
				userList.add(name);
				
				color = childJSONObject.getString("color");
				
	
			}
			
			publishProgress();
			
			
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}
    	
    }
    
    public void doMsg() {
    	mHandler.removeCallbacks(chatRefresh);
    	new GetChatMessages().execute();
    }
    
    @Override
    protected void onResume() {
    	//mHandler.removeCallbacks(chatRefresh);
    	//mHandler.post(chatRefresh);
    	super.onResume();
    }

    @Override
    protected void onPause() {
    	mHandler.removeCallbacks(chatRefresh);
    	super.onPause();
    }

}
