package za.co.rigaming.realintensechat;

import org.json.JSONArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import za.co.rigaming.realintensechat.MySQLiteHelper;

public class Stickies {
	
	class User {
		String id;
		String name;
		String rank;
		String unreadPM;
		String rankColour;
		String directColor;
	}

	 static Button post;
	 static EditText input;
	 static TextView chatView;
	 static ScrollView chat_ScrollView;
	 static za.co.rigaming.realintensechat.GetUserDetails.User user;
	 static TextView username;
	 static TextView pvtResip;
	 static TextView unread;
	 static ListView lv;
	 static Handler mHandler;
	 static ListView mainListView;
	 static ViewGroup vg;
	 static LayoutInflater mInflater;
	 static ScrollView sv;
	 static JSONArray jsonMainArr;
	 static String userNamePvt;
	 
	 static String lastID;
	 
	 
	 public static String getSession(Context context, String name) {
		MySQLiteHelper mslh = new MySQLiteHelper(context);
		SQLiteDatabase db = mslh.getWritableDatabase();
		Cursor c = db.query("sessions", new String[] {"session"}, "name = ?", new String[] {name}, null, null, null);
		c.moveToFirst();
		db.close();
		return c.getString(0);
	 }
	 
	 public static void saveSession(Context context, String session, String name) {
		MySQLiteHelper mslh = new MySQLiteHelper(context);
		SQLiteDatabase db = mslh.getWritableDatabase();
		db.execSQL("CREATE TABLE IF NOT EXISTS sessions (\"_id\" INTEGER PRIMARY KEY, \"name\" TEXT NOT NULL, \"session\" TEXT NOT NULL );");
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("session", session);
		db.insert("sessions", null, cv);
		db.close();
	 }
	 
	 public static void setLastId(String id) {
		
		 lastID = id;
	 }
	 
	 
	 public static void setUser(za.co.rigaming.realintensechat.GetUserDetails.User userness) {
		 user = userness;
	 }
}
