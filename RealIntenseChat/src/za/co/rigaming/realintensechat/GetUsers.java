package za.co.rigaming.realintensechat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;

public class GetUsers extends Activity {
	
	Button post;
	EditText input;
	
	DefaultHttpClient dhc = AppSettings.getClient();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.postmessage);
		
		post = (Button) findViewById(R.id.button1);
		input = (EditText) findViewById(R.id.chat_input);
		
		post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			
				new doHttpPost().execute();
				
			}
		});
		
		
		super.onCreate(savedInstanceState);
		
	}
	
	class doHttpPost extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			HttpPost httppost = new HttpPost("http://www.rigaming.co.za/getUsers.php?users");

			try {
		        

		        // Execute HTTP Post Request
				
		       // HttpResponse response = dhc.execute(httppost);
		        ResponseHandler<String> responseHandler=new BasicResponseHandler();
		        String responseBody = dhc.execute(httppost, responseHandler);
		        JSONObject response=new JSONObject(responseBody);
		       Log.i("GetMessage", responseBody);
		        
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
		
	}
	
}
