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
  
  public class table {
	  String name;
	  List<Column> columns;

	  public String generateTableCreate(List<Column> columns) {
		  String base = "create table " + name + "(id INTEGER PRIMARY KEY AUTOINCREMENT, ";
		  for (Column c : columns) {
			  String name = c.name;
			  String type = c.type;
			  String nullable = c.nullable;
			  base = base + ", " + name + " " + type.toUpperCase() + " " +  nullable.toUpperCase();
		  }
		  base = base + ");";
		  return base;
	  }
  }
  
  public class Column {
	  String name;
	  String type;
	  String nullable;

	  public Column getColumn(String n, String t, String nu) {
		  Column.this.name = n;
		  Column.this.type = t;
		  Column.this.nullable = nu;
		  return this;
	  }
  }
  
  public class Type {
	  final String integer = "INTEGER";
	  final String not_null = "NOT NULL";
	  final String can_null = "";

	  public String getVarchar(String num) {
				  return "VARCHAR(" + num + ")";
			  }

	  public String getInt() {
		  return "INTEGER";
	  }
	  public String getNotNull() {
		  return "NOT NULL";
	  }
	  public String getNull() {
		  return "";
	  }
  }
  
  public void test() {

	  table t = new table();
	  t.name = "Test";

	  List<Column> col = new ArrayList<MySQLiteHelper.Column>();

	  Column id = new Column().getColumn(COLUMN_ID, new Type().getInt(), new Type().getNotNull());
	  Column payload = new Column().getColumn(COLUMN_ID, new Type().getVarchar("50"), new Type().getNull());
	  Column endpoint = new Column().getColumn(COLUMN_ENDPOINT, new Type().getVarchar("50"), new Type().getNull());

	  col.add(id);
	  col.add(payload);
	  col.add(endpoint);

    // compiledString.append(")");

    // //db.execSQL(compiledString);



	t.generateTableCreate(col);


  }

  
//  public void createTable( String tableName, List<Array> columns[] ) {
//	  
//	  String colName = null;
//	  String type = null;
//	  boolean canBeNull = false;
//    String nullValue = null;
//    StringBuilder compiledString = new StringBuilder();
//
//    compiledString.append("CREATE TABLE " + tableName + " ( ");
//	  
//	  for ( List l : columns ) {
//		  
//		  colName = (String) l.get(0);
//		  type = (String) l.get(1);
//		  canBeNull = Boolean.valueOf((String) l.get(2));
//      
//		  if ( canBeNull == true ) {
//        nullValue = "null"
//      } else {
//        nullValue = "not null"
//      }
//
//		  compiledString.append(colName + " " + type + " " + nullValue)
//		  
//	  }
//
//    compiledString.append(")");
//
//    .execSQL(compiledString)
//	  
//  }

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// TODO Auto-generated method stub

}
StringBuilder sf;

@Override
public void onCreate(SQLiteDatabase db) {
	db.execSQL(
	        "create table if not exists settings (\"id\" INTEGER NOT NULL, \"notifyMsg\" TEXT NOT NULL, \"notifyPm\" TEXT NOT NULL, \"notifyPvt\" TEXT NOT NULL"
	        + ");"
	      );
	
	ContentValues cv = new ContentValues();
	cv.put("id", 1);
	cv.put("notifyMsg", "true");
	cv.put("notifyPvt", "true");
	cv.put("notifyPm", "true");
	
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