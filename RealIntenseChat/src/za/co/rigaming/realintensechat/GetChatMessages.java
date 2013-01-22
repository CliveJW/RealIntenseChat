package za.co.rigaming.realintensechat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.co.rigaming.realintensechat.Elements.*;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.View;

public class GetChatMessages extends AsyncTask<String, Object, Object> {

	Context context = ChatView.context;
	public static boolean mustNotify = false;
	String lastID = null;

	JSONArray messageResults;

	class Message {
		String Text;
		String time;
		int userid;
		int from;
		boolean pvt;
		String sender;
		String sendercolor;
		String privColor;
		String privName;

	}

	class PostMessage {
		String message;
		String chat = "1";
		String last = Stickies.lastID;
		String userid = Stickies.user.id;
		String pvtid = null;

		public UrlEncodedFormEntity createPostFormData() {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("chat", chat));
			nameValuePairs.add(new BasicNameValuePair("message", message));
			nameValuePairs.add(new BasicNameValuePair("userid", userid));
			nameValuePairs.add(new BasicNameValuePair("last", last));
			Log.i("Form Entities", chat);
			Log.i("Form Entities", message);
			Log.i("Form Entities", userid);
			Log.i("Form Entities", chat);
			if (pvtid != null) {
				nameValuePairs.add(new BasicNameValuePair("touserid", pvtid));
			}
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
	
	

	@Override
	protected Object doInBackground(String... params) {
		ChatView.pg.setIndeterminate(true);
		if (ChatView.input.isSelected()){
			return params;
		}
		if (Stickies.chatView.getText().length() == 0) {
				Stickies.setLastId("0");
			}
		PostMessage pm = new PostMessage();
		try {
			
			

			pm.message = ChatView.input.getText().toString();

			Log.i("CHATVIEWMESSAGE", pm.message);

			if (params.length != 0) {
				pm.pvtid = params[0];
				pm.chat = "1";
			}

			HttpPost poster = new HttpPost(
					"http://www.rigaming.co.za/getChat.php?");

			poster.setEntity(pm.createPostFormData());

			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String responseBody = Http.createClient(Stickies.getSession(context, "RIGSESS")).execute(poster, responseHandler);

			JSONObject response = new JSONObject(responseBody);

			messageResults = response.getJSONArray("messages");

			List<Message> msgList = new ArrayList<Message>();

			for (int i = 0; i < messageResults.length(); i++) {

				JSONObject childJSONObject = messageResults.getJSONObject(i);

				Message msg = new Message();
				msg.Text = childJSONObject.getString("text");
				msg.time = childJSONObject.getString("time");
				msg.userid = childJSONObject.getInt("userid");
				msg.sender = childJSONObject.getString("user");
				msg.time = childJSONObject.getString("time");
				msg.sendercolor = childJSONObject.getString("color");
				if (childJSONObject.getString("touserid").equalsIgnoreCase(
						Stickies.user.id)) {
					msg.pvt = true;
					msg.privColor = childJSONObject
							.getString("privhighlightcolor");
					msg.privName = childJSONObject.getString("tousername");
				}
				msgList.add(msg);
				Stickies.setLastId(childJSONObject.getString("messageid"));

				// Log.i("UserMessage", childJSONObject.toString() + "\n");
			}

			Log.i("Msg List Size", String.valueOf(msgList.size()));

			for (Message i : msgList) {

				i.Text = i.Text.replaceAll("(<[^>]+>)", "{No Smiley}");

				Log.i("MessageText", i.Text);

				String name = "<font color=" + i.sendercolor + ">:" + i.sender
						+ ": " + "</font><br>";

				String spanToBe = null;
				
				if (i.pvt
						&& i.privName.equalsIgnoreCase(Stickies.user.name)) {
					spanToBe = i.Text = name
							+ "*PVT* <font color=" + i.privColor + ">"
							+ i.Text + "</font>";
					publishProgress(spanToBe);
					mustNotify = true;
					continue;
				}

				if (i.Text.contains("[" + Stickies.user.name + "]")) {
					
					
					i.Text = "<font color=" + Stickies.user.directColor
							+ ">" + i.Text + "</font><br>";
					spanToBe = name + i.Text;
					publishProgress(spanToBe);
					//mustNotify = true;
					continue;

				} 
				
					 
						spanToBe = name + i.Text;
						publishProgress(spanToBe);
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
		ChatView.input.setText("");
		new GetUsers().execute();
		ChatView.pg.setIndeterminate(false);
		
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
	}

	
	private void appendTextAndScroll(String text) {
	
		

	
			Stickies.chatView.append(Html.fromHtml("<p>" + text + "</p>"));
			ChatView.mHandler.post(run);

	
		
	}

	public static void doNotify() {
		try {
			Ringtone r = RingtoneManager.getRingtone(ChatView.context,
					RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			Vibrator v = (Vibrator) ChatView.context
					.getSystemService(Context.VIBRATOR_SERVICE);

			r.play();
		} catch (Exception e) {
		}
	}
	public Object printIt(Object it) {
		System.out.print(it.toString());
		return it;
	}
	public static Runnable run = new Runnable() {
		public void run() {
			ChatView.sv.fullScroll(View.FOCUS_DOWN);
			Stickies.chatView.invalidate();
			return;
		}
	};
}