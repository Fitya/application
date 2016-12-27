package com.example.uch_te.database;

import java.io.File;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBHelper extends SQLiteOpenHelper{

    public static String DB_PATH = Environment.getExternalStorageDirectory().getPath() +"/uch_te/";
    private static String DB_NAME = "uch_te.db";
    public static SQLiteDatabase mDB; 
    private final Context myContext;
    DBHelper openHelper = null;
    
    public DBHelper(Context context) { 	 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
    public void openDataBase() throws SQLException{
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
		File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/uch_te/uch_te.db");
		if (!folder.exists()) {

			File folder2 = new File(Environment.getExternalStorageDirectory().getPath() + "/uch_te");
			boolean success = true;
			if (!folder2.exists()) {
			    success = folder2.mkdir();
			}
			
			mDB = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);

			//mDB.execSQL("CREATE TABLE if not exists spravochnik (_id VARCHAR, tip_spr VARCHAR, naimen VARCHAR, tip_obo VARCHAR, naimen_like VARCHAR, tel VARCHAR);");
			mDB.execSQL("CREATE TABLE if not exists reestr (_id VARCHAR, kol VARCHAR, naimen VARCHAR, tip_obo VARCHAR, mar_mod VARCHAR);");
			mDB.execSQL("CREATE TABLE if not exists act (_id VARCHAR, osnovanie VARCHAR, naimen VARCHAR, kol VARCHAR);");
		}
        mDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
 
    @Override
	public synchronized void close() {
    	if(mDB != null)
    	mDB.close();
   	    super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
}
