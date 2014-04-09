package com.st.fn;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VehiclesActivity extends Activity {
 
	ListView listVechicles; 
    DBHelper dbhelper;
	Cursor cursor;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicles);
		listVechicles = (ListView) this.findViewById(R.id.listVehicles);
		listVechicles.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView, int arg2,long arg3) {
				TextView  textVehicleNo= (TextView) selectedView.findViewById(R.id.textVechicleNo);
				Log.d( Database.TAG, "Selected Vehicle : " + textVehicleNo.getText().toString());
				
				Intent intent = new Intent(VehiclesActivity.this, UpdateVehicleActivity.class);
				intent.putExtra("vehicleno", textVehicleNo.getText().toString());
				startActivity(intent);
			}
		});
		getVehicles();
    }
    
	public void getVehicles() {
		try {
			dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			cursor = db.query( Database.VI_TABLE_NAME,null,null,null,null,null, Database.VI_END_DATE);
			startManagingCursor(cursor);
			String from [] = {Database.VI_POLICY_NO,Database.VI_VEHICLE_NO, Database.VI_VEHICLE_DETAILS, 
  					          Database.VI_POLICY_COMPANY, 
					          Database.VI_AMOUNT, Database.VI_START_DATE,
					          Database.VI_END_DATE};
			
			int to [] = { R.id.textPolicyNo, R.id.textVechicleNo, R.id.textVehicleDetails, R.id.textCompany, R.id.textAmount,  R.id.textStartDate, R.id.textEndDate};
			
			SimpleCursorAdapter ca  = new SimpleCursorAdapter(this,R.layout.vehicle, cursor,from,to);
		    listVechicles.setAdapter(ca);
		    Log.d(Database.TAG, String.valueOf(cursor.getCount()));
		    
		} catch (Exception ex) {
			Log.d(Database.TAG, ex.getMessage());
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			dbhelper.close();
		}
		catch(Exception ex) {
		}
	}

	public void addVehicle(View v) {
    	 Intent intent = new Intent(this, AddVIActivity.class);
    	 startActivity(intent);
    }

}
