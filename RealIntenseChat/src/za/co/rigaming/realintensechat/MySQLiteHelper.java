package za.co.rigaming.realintensechat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_MESSAGES = "messages";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_PAYLOAD = "payload";
  public static final String COLUMN_ENDPOINT = "endpoint";

  private static final String DATABASE_NAME = "messages.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_MESSAGES + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_PAYLOAD
      + " text not null"
      + ", " + COLUMN_ENDPOINT
      + " text not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
  
 

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// TODO Auto-generated method stub

}
StringBuilder sf;

@Override
public void onCreate(SQLiteDatabase db) {
	db.execSQL(
	        "create table if not exists settings (" +
	        "\"id\" INTEGER NOT NULL, " +
	        "\"notifyMsg\" INTEGER NOT NULL, " +
	        "\"notifyPm\" INTEGER NOT NULL, " +
	        "\"notifyPvt\" INTEGER NOT NULL, " +
	        "\"refresh\" INTEGER NOT NULL, " +
	        "\"refresh_rate\" INTEGER NOT NULL, " +
	        "\"screen_on\" INTEGER NOT NULL"
	        + ");"
	      );
	
	ContentValues cv = new ContentValues();
	cv.put("id", 1);
	cv.put("notifyMsg", 1);
	cv.put("notifyPvt", 1);
	cv.put("notifyPm", 1);
	cv.put("refresh", 1);
	cv.put("refresh_rate", 30);
	cv.put("screen_on", 0);
	db.insert("settings", null, cv);

}

  // @Override
  // public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  //   Log.w(MySQLiteHelper.class.getName(),
  //       "Upgrading database from version " + oldVersion + " to "
  //           + newVersion + ", which will destroy all old data");
  //   db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
  //   onCreate(db);
  // }

} 