package com.st.fn;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

public class Database {
	public static final String TAG = "Financial Notifier";
	public static final String VI_TABLE_NAME = "vehicle_insurance";
	public static final String VI_ID = "_id";
	public static final String VI_VEHICLE_NO = "vehicle_no";
	public static final String VI_VEHICLE_DETAILS = "vehicle_details";
	public static final String VI_VEHICLE_VALUE = "vehicle_value";
	public static final String VI_POLICY_NO = "policy_no";
	public static final String VI_POLICY_COMPANY = "policy_company";
	public static final String VI_START_DATE = "start_date";
	public static final String VI_END_DATE = "end_date";
	public static final String VI_AMOUNT = "amount";
	
	public static final String FD_TABLE_NAME = "FIXED_DEPOSITS";
	public static final String FD_ID = "_id";
	public static final String FD_DEPOIST_NO = "deposit_no";
	public static final String FD_ACCOUNT_HOLDER = "account_holder";
	public static final String FD_BANK = "bank";
	public static final String FD_DEPOSIT_AMOUNT = "deposit_amount";
	public static final String FD_DEPOSIT_DATE = "deposit_date";
	public static final String FD_MATURITY_AMOUNT = "maturity_amount";
	public static final String FD_MATURITY_DATE = "maturity_date";
	public static final String FD_INTEREST_RATE = "interest_rate";
	public static final String FD_AUTO_RENEWAL = "auto_renewal";

	
	public static final String LI_TABLE_NAME = "life_insurance";
	public static final String LI_ID = "_id";
	public static final String LI_POLICY_NO = "policy_no";
	public static final String LI_POLICY_HOLDER = "policy_holder";
	public static final String LI_POLICY_NAME = "policy_name";
	public static final String LI_COMPANY = "company";
	public static final String LI_PREMIUM_AMOUNT = "premium_amount";
	public static final String LI_START_DATE = "start_date";
	public static final String LI_PERIOD = "period";
	public static final String LI_SUM_ASSURED = "sum_assured";
	public static final String LI_DUE_DATE = "due_date";
	
	public static int notify_days= 7;
	public static int hour_of_day = 6;
	
	public static String getDatabasePath(Context context) {
		DBHelper dbhelper = new DBHelper(context);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String path = db.getPath();
		db.close();
		dbhelper.close();
		return path;
	}
	
	public static void getPreferences(Context ctx) {
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(ctx);
		notify_days = Integer.parseInt(sp.getString("nodays","7"));
		hour_of_day  = Integer.parseInt(sp.getString("hour","6"));
		Log.d(Database.TAG,"Preferences Set to  " + notify_days + ":" + hour_of_day);
	}
	
	public static int getHourOfDay() {
		return hour_of_day;
	}
	
	public static int getNotifyDays() {
		return notify_days;
	}
	
	public static void getUpcomingVehicleInsurances(SQLiteDatabase db,
			ArrayList<Map<String, String>> events, int nodays) {
		Cursor cursor = db.query(Database.VI_TABLE_NAME, null,
				" julianday(end_date) - julianday(date('now'))  <= " + nodays,
				null, null, null, Database.VI_END_DATE);
		while (cursor.moveToNext()) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("key", cursor.getString(cursor
					.getColumnIndex(Database.VI_VEHICLE_NO)));
			map.put("duedate", cursor.getString(cursor
					.getColumnIndex(Database.VI_END_DATE)));
			map.put("details", cursor.getString(cursor
					.getColumnIndex(Database.VI_VEHICLE_DETAILS)));
			map.put("id", cursor.getString(cursor
					.getColumnIndex(Database.VI_ID)));
			map.put("type", "VI");
			events.add(map);
		}
		cursor.close();
	}

	public static void getUpcomingLifeInsurances(SQLiteDatabase db,
			ArrayList<Map<String, String>> events, int nodays) {
		Cursor cursor = db.query(Database.LI_TABLE_NAME, null,
				" julianday(due_date) - julianday(date('now'))  <= " + nodays,
				null, null, null, Database.LI_DUE_DATE);
		while (cursor.moveToNext()) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("key", cursor.getString(cursor
					.getColumnIndex(Database.LI_POLICY_NO)));
			map.put("duedate", cursor.getString(cursor
					.getColumnIndex(Database.LI_DUE_DATE)));
			map.put("details", cursor.getString(cursor
					.getColumnIndex(Database.LI_POLICY_NAME)));
			map.put("id", cursor.getString(cursor
					.getColumnIndex(Database.LI_ID)));
			
			map.put("type", "LI");
			events.add(map);
		}
		cursor.close();
	}
	
	
	public static void getUpcomingDeposits(SQLiteDatabase db,
			ArrayList<Map<String, String>> events, int nodays) {
		Cursor cursor = db.query(Database.FD_TABLE_NAME, null,
				" julianday(maturity_date) - julianday(date('now'))  <= " + nodays,
				null, null, null, Database.FD_MATURITY_DATE);
		while (cursor.moveToNext()) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("key", cursor.getString(cursor
					.getColumnIndex(Database.FD_DEPOIST_NO)));
			map.put("duedate", cursor.getString(cursor
					.getColumnIndex(Database.FD_MATURITY_DATE)));
			map.put("details", cursor.getString(cursor.getColumnIndex(Database.FD_BANK)) + " - " + 
					           cursor.getString(cursor.getColumnIndex(Database.FD_ACCOUNT_HOLDER)) + " - " + 
					           cursor.getString(cursor.getColumnIndex(Database.FD_MATURITY_AMOUNT)) );
			map.put("id", cursor.getString(cursor
					.getColumnIndex(Database.FD_ID)));
			
			map.put("type", "FD");
			events.add(map);
		}
		cursor.close();
	}

}
