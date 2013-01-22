package za.co.rigaming.realintensechat;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GetUsers extends AsyncTask<Object, Object, Object> {

	public static JSONArray userResults;

	public static ArrayAdapter<String> userListAdapter;
	public static ArrayList<String> userList;
	public static ListView mainListView;
	public static String color = new String();
	Context context = ChatView.context;

	@Override
	protected void onProgressUpdate(Object... values) {

		final Context context = ChatView.context;

		userListAdapter = new ArrayAdapter<String>(context,
				R.layout.simple_view, userList);

		ChatView.mainListView.setAdapter(userListAdapter);
		super.onProgressUpdate(values);
	}

	@Override
	protected Object doInBackground(Object... params) {

		try {

			HttpPost poster = new HttpPost(
					"http://www.rigaming.co.za/getUsers.php?users");

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = Http.createClient(
					Stickies.getSession(context, "RIGSESS")).execute(poster,
					responseHandler);
			JSONObject response = new JSONObject(responseBody);
			Log.i("Users Response", response.toString());

			userResults = response.getJSONArray("users");
			userList = new ArrayList<String>();

			for (int i = 0; i < userResults.length(); i++) { // **line 2**

				JSONObject childJSONObject = userResults.getJSONObject(i);
				String name = childJSONObject.getString("name");
				Log.i("USERNAME", name);

				userList.add(name);

			}

			publishProgress();

		} catch (JSONException e) {
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