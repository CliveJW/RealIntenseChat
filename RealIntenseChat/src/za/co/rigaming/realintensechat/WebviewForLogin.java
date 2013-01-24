package za.co.rigaming.realintensechat;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebviewForLogin extends Activity {

	WebView webview;
	CookieManager cookieManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.webview);

		webview = (WebView) findViewById(R.id.webview);
		final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar2);
		pb.setIndeterminate(true);
		WebViewClient wvc = new WebViewClient();
		webview.setVisibility(View.INVISIBLE);
		final TextView bro = (TextView) findViewById(R.id.bro);
		bro.setText(getString());
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				String urlGrab = webview.getUrl();
				Log.i("WebviewForLogin", urlGrab);
				
				if ( urlGrab.contains("chat.php") ) {
					
					
					String c = CookieManager.getInstance().getCookie("www.rigaming.co.za");
					Log.i("SESSION!!!!", c.replace("RIGSESS=", ""));
					AppSettings.setCookie(c.replace("RIGSESS=", ""));
					Stickies.saveSession(getApplicationContext(), c.replace("RIGSESS=", ""), "RIGSESS");
					Intent startGetingMessage = new Intent(WebviewForLogin.this, ChatView.class);
					startActivity(startGetingMessage);
					finish();
				} else if (urlGrab.contains("m.facebook.com")) {
					webview.setVisibility(View.VISIBLE);
					pb.setVisibility(View.INVISIBLE);
					bro.setVisibility(View.INVISIBLE);
				}
				
				super.onPageFinished(view, url);
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				webview.setVisibility(View.INVISIBLE);
				pb.setVisibility(View.VISIBLE);
				bro.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}
		});
		
		
		
		webview.loadUrl("http://www.rigaming.co.za/facebook_login.php");
		

		super.onCreate(savedInstanceState);
	}
	
	
	public static String getString() {
		Random rand = new Random();
		
		List<String> my_words = new LinkedList<String>();
		my_words.add("Finding the droids you are looking for....");
		my_words.add("Squeeking the beaver");
		my_words.add("Reticulating splines");
		my_words.add("Making http data babies with Facebook");
		my_words.add("Stealing your photos, all up in yo business");
		my_words.add("Wikki Wikki");
		my_words.add("Don't be evil!!");
		my_words.add(":-) 	:-| 	:-( 	:-0 	:-> 	:-D 	d(0-o)b 	(x-x) 	 (o)(o)		(0-0) 	:-P");
		my_words.add("Loggin you into bAs3F0oK");
		my_words.add("All your base are belong to us");
		my_words.add("RIG.. Aint is just grand");
		my_words.add("Up Up Down Down Left Right Left Right B A SELECT START");
		my_words.add("You are not reading this.. ");
		my_words.add("All hail Krylik...");
		
		
		int choice = rand.nextInt(my_words.size());
		
		return my_words.get(choice);
	}
	
}
