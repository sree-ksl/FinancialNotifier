package com.st.fn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateVehicleActivity extends Activity {
	private String vehicleNo;
	
	EditText editStartDate, editEndDate, editVehicleNo, editVehicleDetails,
	editVehicleValue, editPolicyNo, editCompany, editAmount;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_vi);
		
		Intent intent = getIntent();
		vehicleNo =  intent.getStringExtra("vehicleno").toString();
		
		editVehicleNo = (EditText) this.findViewById(R.id.editVehicleNo);
		editVehicleDetails = (EditText) this
				.findViewById(R.id.editVehicleDetails);
		editVehicleValue = (EditText) this.findViewById(R.id.editVehicleValue);
		editPolicyNo = (EditText) this.findViewById(R.id.editPolicyNo);
		editCompany = (EditText) this.findViewById(R.id.editCompany);
		editAmount = (EditText) this.findViewById(R.id.editAmount);

		editStartDate = (EditText) this.findViewById(R.id.editStartDate);
		editEndDate = (EditText) this.findViewById(R.id.editEndDate);
		
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor vehicle = db.query(Database.VI_TABLE_NAME, null,
				Database.VI_VEHICLE_NO + " = ?", new String[] { vehicleNo }, null, null, null);
		
		vehicle.moveToFirst();
		
		this.editVehicleNo.setText(vehicleNo);
		this.editVehicleDetails.setText(  vehicle.getString( vehicle.getColumnIndex( Database.VI_VEHICLE_DETAILS)));
		this.editVehicleValue.setText(  vehicle.getString( vehicle.getColumnIndex( Database.VI_VEHICLE_VALUE)));
		this.editPolicyNo.setText(  vehicle.getString( vehicle.getColumnIndex( Database.VI_POLICY_NO)));
		this.editCompany.setText(  vehicle.getString( vehicle.getColumnIndex( Database.VI_POLICY_COMPANY)));
		this.editStartDate.setText(  vehicle.getString( vehicle.getColumnIndex( Database.VI_START_DATE)));
		this.editEndDate.setText(  vehicle.getString( vehicle.getColumnIndex( Database.VI_END_DATE)));
		this.editAmount.setText(  vehicle.getString( vehicle.getColumnIndex( Database.VI_AMOUNT)));
        vehicle.close();
        dbhelper.close();
	}
	
	public void updateVehicle(View v) {
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// execute UPDATE command
			ContentValues values = new ContentValues();
			values.put( Database.VI_VEHICLE_DETAILS, editVehicleDetails.getText().toString());
			values.put( Database.VI_VEHICLE_VALUE, editVehicleValue.getText().toString());
			values.put( Database.VI_POLICY_COMPANY, editCompany.getText().toString());
			values.put( Database.VI_AMOUNT, editAmount.getText().toString());
			values.put( Database.VI_POLICY_NO, editPolicyNo.getText().toString());
			values.put( Database.VI_START_DATE, editStartDate.getText().toString());
			values.put( Database.VI_END_DATE, editEndDate.getText().toString());
		
			long rows = db.update(Database.VI_TABLE_NAME, values, Database.VI_VEHICLE_NO + " = ?", new String[] { vehicleNo });

			db.close();
			if (rows > 0)
				Toast.makeText(this, "Updated Vehicle Details Successfully!",Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "Sorry! Could not update vechicle details!",Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void deleteVehicle(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to delete this vehicle?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                deleteCurrentVehicle();
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
	}
         			
    public void deleteCurrentVehicle() {
    	try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			int rows = db.delete(Database.VI_TABLE_NAME, Database.VI_VEHICLE_NO + "=?", 
					 new String[] { vehicleNo});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "Vehicle Deleted Successfully!", Toast.LENGTH_LONG).show();
				this.finish();
			}
			else
				Toast.makeText(this, "Could not delete Vehicle!", Toast.LENGTH_LONG).show();

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
	
    
}
