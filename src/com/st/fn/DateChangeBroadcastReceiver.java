package com.st.fn;

import java.util.ArrayList;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DateChangeBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent arg1) {
		Log.d(Database.TAG, "DateChangeBroadcastReceiver");
		// send notifications 

		try {
			DBHelper dbhelper = new DBHelper(ctx);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			ArrayList<Map<String, String>> events = new ArrayList<Map<String, String>>();

			Database.getUpcomingVehicleInsurances(db, events, Database.getNotifyDays());
			Database.getUpcomingLifeInsurances(db, events, Database.getNotifyDays());
			Database.getUpcomingDeposits(db, events, Database.getNotifyDays());

			if (events.size() > 0) {
				NotificationManager nm = (NotificationManager) ctx
						.getSystemService(Context.NOTIFICATION_SERVICE);

				for (Map<String, String> event : events) {
					// notify user about events
					Notification notification = new Notification(
							R.drawable.icon, "Financial Notifier",
							System.currentTimeMillis());
					String type = event.get("type");
					String key = event.get("key");
					String details = event.get("details") + " is due on " + event.get("duedate");
					// id of the event 
					int  eventid = Integer.parseInt(event.get("id"));
					

					Intent intent;
					if (type.equals("LI")) {
						intent = new Intent(ctx,
								UpdateLifeInsuranceActivity.class);
						intent.putExtra("policyno", key);
						eventid += 10000; // make it unique by adding 1000 for vehicle
						
					} else if (type.equals("VI")) {
						intent = new Intent(ctx, UpdateVehicleActivity.class);
						intent.putExtra("vehicleno", key);
						eventid += 20000; // make it unique by adding 2000 for life insurance
					} else {
						intent = new Intent(ctx, UpdateDepositActivity.class);
						intent.putExtra("depositno", key);
						eventid += 30000; // make it unique by adding 3000 for Deposit
					}
					PendingIntent contentIntent = PendingIntent.getActivity(
							ctx, 1, intent, Notification.DEFAULT_SOUND
									| Notification.DEFAULT_VIBRATE);
					notification.setLatestEventInfo(ctx, "Financial Notifier",details, contentIntent);
					nm.notify(eventid, notification);
					Log.d( Database.TAG, "Notified for id " + eventid);
				}
			}
			dbhelper.close();
		} catch (Exception ex) {
			Log.d(Database.TAG, ex.getMessage());
		}
	}

}
