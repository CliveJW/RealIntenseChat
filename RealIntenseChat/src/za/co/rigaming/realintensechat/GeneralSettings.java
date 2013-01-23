package za.co.rigaming.realintensechat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GeneralSettings {
	
	static class Settings {
		String pm_switch;
		String pvt_switch;
		String msg_switch;
		
		public static Settings getSettings(Context context) {
			Settings set = new Settings();
			
			MySQLiteHelper mysql = new MySQLiteHelper(context);
			SQLiteDatabase db = mysql.getWritableDatabase();
			Cursor c = db.query("settings", new String[] {"notifyPm","notifyMsg","notifyPvt"}, "id = ?", new String[] {"1"}, null, null, null);
			c.moveToFirst();
			set.pm_switch = c.getString(0);
			set.msg_switch = c.getString(1);
			set.pvt_switch = c.getString(2);
			
			
			db.close();
			return set;
			
		}
		
		public boolean saveSettings(Context context) {
			
			MySQLiteHelper mysql = new MySQLiteHelper(context);
			SQLiteDatabase db = mysql.getWritableDatabase();
			
			ContentValues cv = new ContentValues();
			cv.put("id", 1);
			cv.put("notifyMsg", this.msg_switch);
			cv.put("notifyPvt", this.pvt_switch);
			cv.put("notifyPm", this.pm_switch);
			
			long success = db.update("settings", cv, "id = 1", null);
			db.close();
			if (success != -1) {
				return true;
			}
			return false;
			
		}
		
	}
	
}
