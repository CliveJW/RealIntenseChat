package za.co.rigaming.realintensechat;

import java.util.concurrent.TimeUnit;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.apache.http.client.CookieStore;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;

import android.app.Application;

@ReportsCrashes(formKey = "dFJNcVdBTm4tQzUzbncwb3FUbUQzUWc6MQ")
public class AppSettings extends Application {
	private static final DefaultHttpClient client = createClient();
	
	
		 @Override
		  public void onCreate() {
		      super.onCreate();

		      // The following line triggers the initialization of ACRA
		      ACRA.init(this);
		  }
	
	
	static DefaultHttpClient getClient() {
		return client;
	}
	
	private static DefaultHttpClient createClient(){
		BasicHttpParams params = new BasicHttpParams();
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
		schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		cm.closeIdleConnections(3, TimeUnit.DAYS);
		DefaultHttpClient httpclient = new DefaultHttpClient(cm, params);
		httpclient.getCookieStore().getCookies();
		
		return httpclient;
	}
	
	public static void setCookie(String c) {
		CookieStore cookieStore = new BasicCookieStore(); 
		BasicClientCookie bcc = new BasicClientCookie("RIGSESS", 
				c);
		bcc.setDomain("www.rigaming.co.za");
		cookieStore.addCookie(bcc);
		client.setCookieStore(cookieStore);
	}
	
}