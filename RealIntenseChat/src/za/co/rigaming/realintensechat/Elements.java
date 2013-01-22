package za.co.rigaming.realintensechat;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class Elements {

	public class User {
		String id;
		String name;
		String rank;
		String unreadPM;
		String rankColour;
		String directColor;
	}

	class Message {
		String Text;
		String time;
		int userid;
		int from;
		boolean pvt;
		String sender;
		String sendercolor;

	}

	
	
	
}
