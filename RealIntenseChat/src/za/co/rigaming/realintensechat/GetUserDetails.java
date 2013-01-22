package za.co.rigaming.realintensechat;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

public class GetUserDetails extends AsyncTask<Object, String, String> {
	
	public class User {
		String id;
		String name;
		String rank;
		String unreadPM;
		String rankColour;
		String directColor;
	}
	
	Context context = ChatView.context;
	@Override
	protected String doInBackground(Object... params) {
		
		if (!ChatView.input.isInputMethodTarget()) {
		
		HttpPost poster = new HttpPost(
				"http://www.rigaming.co.za/getCurrentUser.php");

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = null;

		try {
			responseBody = Http.createClient(Stickies.getSession(context, "RIGSESS")).execute(poster,
					responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			User user = new User();
			JSONObject response = new JSONObject(responseBody);
			Log.i("UserDetails", response.toString());
			user.id = response.getString("id");
			user.name = response.getString("name");
			user.rank = response.getString("rank");
			user.directColor = response.getString("nameHilite");
			user.rankColour = response.getString("color");
			user.unreadPM = response.getString("unreadPms");
			Stickies.setUser(user);
			publishProgress();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		ChatView.username.setText(Stickies.user.name);
		ChatView.username.setTextColor(Color
				.parseColor(Stickies.user.rankColour));
		ChatView.unread.setText(Stickies.user.unreadPM);
		new GetChatMessages().execute();
		super.onProgressUpdate(values);
	}

}
