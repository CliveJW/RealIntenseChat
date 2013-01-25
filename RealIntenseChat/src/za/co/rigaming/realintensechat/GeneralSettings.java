package za.co.rigaming.realintensechat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GeneralSettings {

	public static class Settings {
		boolean pm_switch;
		boolean pvt_switch;
		boolean msg_switch;
		int refresh_rate;
		boolean screen_on;
		boolean refresh;
		int text_size;

		public static Settings getSettings(Context context) {
			
			Settings set = new Settings();

			MySQLiteHelper mysql = new MySQLiteHelper(context);
			SQLiteDatabase db = mysql.getReadableDatabase();
			Cursor c = db.query("settings", new String[] { "notifyPm",
					"notifyMsg", "notifyPvt", "refresh", "refresh_rate",
					"screen_on", "text_size" }, "id = ?", new String[] { "1" }, null, null,
					null);
			
			c.moveToFirst();
			
			if (c.getInt(0) == 1) {
				set.pm_switch = true;
			} else {
				set.pm_switch = false;
			}

			if (c.getInt(1) == 1) {
				set.msg_switch = true;
			} else {
				set.msg_switch = false;
			}

			if (c.getInt(2) == 1) {
				set.pvt_switch = true;
			} else {
				set.pvt_switch = false;
			}

			if (c.getInt(3) == 1) {
				set.refresh = true;
			} else {
				set.refresh = false;
			}

			set.refresh_rate = c.getInt(4);

			if (c.getInt(5) == 1) {
				set.screen_on = true;
			} else {
				set.screen_on = false;
			}
			
			set.text_size = (c.getInt(6));

			db.close();
			return set;

		}

		public boolean saveSettings(Context context) {

			MySQLiteHelper mysql = new MySQLiteHelper(context);
			SQLiteDatabase db = mysql.getWritableDatabase();
			
			ContentValues cv = new ContentValues();
			cv.put("id", 1);
			
			cv.put("notifyMsg", (this.msg_switch) ? 1 : 0);
			Log.i("MSG", String.valueOf((this.msg_switch) ? 1 : 0));
			
			cv.put("notifyPvt", (this.pvt_switch) ? 1 : 0);
			Log.i("PVT", String.valueOf((this.pvt_switch) ? 1 : 0));
			
			cv.put("notifyPm", (this.pm_switch) ? 1 : 0);
			Log.i("PM", String.valueOf((this.pm_switch) ? 1 : 0));
			
			cv.put("refresh", (this.refresh) ? 1 : 0);
			Log.i("refresh", String.valueOf((this.refresh) ? 1 : 0));
			
			cv.put("refresh_rate", this.refresh_rate);
			Log.i("Srefresh_rate", String.valueOf(this.refresh_rate));
			
			cv.put("screen_on", (this.screen_on) ? 1 : 0);
			Log.i("screen_on", String.valueOf((this.screen_on) ? 1 : 0));
			
			cv.put("text_size", this.text_size);
			Log.i("text_size", String.valueOf(this.text_size));

			long success = db.update("settings", cv, "id = ?", new String[] {"1"});
			
			db.close();
			if (success != -1) {
				Log.i("SAVED DATA TO DATABASE", "TRUE");
				return true;
			}
			Log.i("SAVED DATA TO DATABASE", "FALSE");
			return false;

		}

	}

}
