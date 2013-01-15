package za.co.rigaming.realintensechat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class GetMessages extends Activity {

	Button post;
	EditText input;
	TextView chatView;
	static DefaultHttpClient dhc = AppSettings.getClient();
	ScrollView chat_ScrollView;
	String lastID;
	User user; 
	TextView username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		user = new User();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.postmessage);

		post = (Button) findViewById(R.id.button1);
		input = (EditText) findViewById(R.id.chat_input);
		chatView = (TextView) findViewById(R.id.chatView);
		chatView.setTextSize(11);
		chatView.setMovementMethod(new ScrollingMovementMethod());
		chatView.setBackgroundResource(R.layout.border);
		username = (TextView) findViewById(R.id.user);
		
		post.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				new GetChatMessages().execute();
				input.setText("");

			}
		});
		new getUserDetails().execute();
		new GetChatMessages().execute();
		super.onCreate(savedInstanceState);

	}
	
	private class User {
		String id;
		String name;
		String rank;
		String chatColor;
		String unreadPM;
	}

	private class Message {
		String Text;
		String time;
		int userid;
		int from;
		boolean pvt;
		String rankColour;
		String pvtColour;
		String sender;

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
					msgList.add(msg);
					msg.sender = childJSONObject.getString("user");
					lastID = childJSONObject.getString("messageid");
					Log.i("UserMessage", childJSONObject.toString() + "\n");
				}
				Log.i("Msg List Size", String.valueOf(msgList.size()));
				for (Message i : msgList) {
					Log.i("MessageText",
							i.Text.replaceAll("(<[^>]+>)", "{No Smiley}"));
							
							String spanToBe = ": " + i.sender + " : " + i.Text;
					publishProgress(spanToBe.replaceAll("(<[^>]+>)",
							"{No Smiley}"));
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
				user.chatColor = response.getString("nameHilite");
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
			super.onProgressUpdate(values);
		}
		
	}

	private void appendTextAndScroll(String text) {
		final int MAX_LINE = 50;
		int excessLineNumber = chatView.getLineCount() - MAX_LINE;
		if (chatView != null) {
			chatView.append(text + "\n");
			final Layout layout = chatView.getLayout();
			if (layout != null) {
				int scrollDelta = layout
						.getLineBottom(chatView.getLineCount() - 1)
						- chatView.getScrollY() - chatView.getHeight();
				if (scrollDelta > 0)
					chatView.scrollBy(0, scrollDelta);
			}
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
	
	

}
