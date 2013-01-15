package za.co.rigaming.realintensechat;

import java.util.Date;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewForLogin extends Activity {

	WebView webview;
	CookieManager cookieManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.webview);

		webview = (WebView) findViewById(R.id.webview);
		
		WebViewClient wvc = new WebViewClient();
		
		
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
					Intent startGetingMessage = new Intent(WebviewForLogin.this, GetMessages.class);
					startActivity(startGetingMessage);
					finish();
				}
				
				super.onPageFinished(view, url);
			}
		});
		
		webview.loadUrl("http://www.rigaming.co.za/facebook_login.php");
		

		super.onCreate(savedInstanceState);
	}
}
