package za.co.rigaming.realintensechat;

import java.util.concurrent.TimeUnit;

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

public class Http {
	
	

	 static DefaultHttpClient createClient(String c){
		BasicHttpParams params = new BasicHttpParams();
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		DefaultHttpClient httpclient = new DefaultHttpClient(cm, params);
		httpclient.getCookieStore().getCookies();
		CookieStore cookieStore = new BasicCookieStore(); 
		BasicClientCookie bcc = new BasicClientCookie("RIGSESS", 
				c);
		bcc.setDomain("www.rigaming.co.za");
		cookieStore.addCookie(bcc);
		httpclient.setCookieStore(cookieStore);
		
		return httpclient;
	}
	
	
}
