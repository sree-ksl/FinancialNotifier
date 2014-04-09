package com.st.fn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static final int DB_VERSION = 2; 
	public static final String DB_NAME = "fn.db";
   
	public DBHelper(Context ctx) {
		super(ctx, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
          createTables(db);
	} 

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}
	
	public void createTables(SQLiteDatabase database) {
		String vi_table_sql = "create table " + Database.VI_TABLE_NAME + " ( " +
				Database.VI_ID 	+ " integer  primary key autoincrement, " + 
				Database.VI_VEHICLE_NO + " TEXT," +
				Database.VI_VEHICLE_DETAILS + " TEXT," +
				Database.VI_VEHICLE_VALUE  + " FLOAT," +
				Database.VI_POLICY_NO + " TEXT," +
				Database.VI_POLICY_COMPANY + " TEXT," + 
				Database.VI_START_DATE + " TEXT," +
				Database.VI_END_DATE + " TEXT," +
				Database.VI_AMOUNT  + " FLOAT)";

		
		String fd_table_sql = "create table " + Database.FD_TABLE_NAME + " ( " +
				Database.FD_ID 	+ " integer  primary key autoincrement, " + 
				Database.FD_DEPOIST_NO + " TEXT," +
				Database.FD_ACCOUNT_HOLDER + " TEXT," +
				Database.FD_BANK  + " TEXT," +
				Database.FD_DEPOSIT_AMOUNT + " FLOAT," +
				Database.FD_DEPOSIT_DATE + " TEXT," + 
				Database.FD_MATURITY_AMOUNT + " FLOAT," +
				Database.FD_MATURITY_DATE + " TEXT," +
				Database.FD_INTEREST_RATE  + " FLOAT, " +
				Database.FD_AUTO_RENEWAL  + " TEXT)";
		
		
		String li_table_sql = "create table " + Database.LI_TABLE_NAME + " ( " +
				Database.LI_ID 	+ " integer  primary key autoincrement, " + 
				Database.LI_POLICY_NO + " TEXT," +
				Database.LI_POLICY_HOLDER + " TEXT," +
				Database.LI_COMPANY + " TEXT," +
				Database.LI_POLICY_NAME  + " TEXT," +
				Database.LI_PREMIUM_AMOUNT + " FLOAT," +
				Database.LI_START_DATE+ " TEXT," + 
				Database.LI_DUE_DATE + " FLOAT," +
				Database.LI_PERIOD + " FLOAT," + 
				Database.LI_SUM_ASSURED + " FLOAT)";

		
        try {
		   database.execSQL(vi_table_sql);
		   database.execSQL(li_table_sql);
		   database.execSQL(fd_table_sql);
		   Log.d(Database.TAG,"Tables created!");
        }
        catch(Exception ex) {
        	Log.d(Database.TAG, "Error in DBHelper.onCreate() : " + ex.getMessage());
        }
	}

}
