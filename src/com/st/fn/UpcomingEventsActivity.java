package com.st.fn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class UpcomingEventsActivity extends Activity {

	ListView listEvents;
	Cursor cursor;
	int nodays = 7;
	EditText editNodays;
	TextView textMessage;
	LinearLayout layoutEvents;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upcoming_events);

		editNodays = (EditText) this.findViewById(R.id.editNodays);
		textMessage = (TextView) this.findViewById(R.id.textMessage);
		listEvents = (ListView) this.findViewById(R.id.listEvents);
		layoutEvents = (LinearLayout) this.findViewById(R.id.layoutEvents);
		

		listEvents.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView,
					int arg2, long arg3) {
				String key = ((TextView) selectedView
						.findViewById(R.id.textKey)).getText().toString();
				TextView textType = (TextView) selectedView
						.findViewById(R.id.textType);

				String type = textType.getText().toString();

				Intent intent;
				if (type.equals("LI")) {
					intent = new Intent(UpcomingEventsActivity.this,
							UpdateLifeInsuranceActivity.class);
					intent.putExtra("policyno", key);
				} else if (type.equals("VI")) {
					intent = new Intent(UpcomingEventsActivity.this,
							UpdateVehicleActivity.class);
					intent.putExtra("vehicleno", key);
				} else {
					intent = new Intent(UpcomingEventsActivity.this,
							UpdateDepositActivity.class);
					intent.putExtra("depositno", key);
				}

				startActivity(intent);
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void getEvents(View v) {
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			ArrayList<Map<String, String>> events = new ArrayList<Map<String, String>>();
			nodays = Integer.parseInt(editNodays.getText().toString());

			Database.getUpcomingVehicleInsurances(db, events, nodays);
			Database.getUpcomingLifeInsurances(db, events, nodays);
			Database.getUpcomingDeposits(db, events, nodays);
			
			Collections.sort(events, new SortByDueDate());
			

			if (events.size() > 0) {
				int to[] = { R.id.textKey, R.id.textDetails, R.id.textType,
						R.id.textDuedate };
				String from[] = { "key", "details", "type", "duedate" };
				SimpleAdapter ca = new SimpleAdapter(this, events,
						R.layout.event, from, to);
				listEvents.setAdapter(ca);
				layoutEvents.setVisibility(View.VISIBLE);
			}
			else{
				textMessage.setVisibility(View.VISIBLE);
			}
				

			dbhelper.close();
		} catch (Exception ex) {
			Log.d(Database.TAG, ex.getMessage());
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	
}
