package za.co.rigaming.realintensechat;

import java.util.Date;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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
	
	
	
	
}
