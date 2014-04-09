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

public class UpdateLifeInsuranceActivity extends Activity {
	private String policyNo;
	
	EditText editStartDate, editDueDate, editPolicyHolder, editPolicyName, editPremiumAmount, editPolicyNo, editCompany, editPeriod, editSumAssured;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_li);
		
		Intent intent = getIntent();
		policyNo =  intent.getStringExtra("policyno").toString();
		
		editPolicyNo = (EditText) this.findViewById(R.id.editPolicyNo);
		editPolicyHolder = (EditText) this.findViewById(R.id.editPolicyHolder);
		editPolicyName = (EditText) this
				.findViewById(R.id.editPolicyName);
		editPremiumAmount = (EditText) this.findViewById(R.id.editPremiumAmount);
		editCompany = (EditText) this.findViewById(R.id.editCompany);
		editPeriod = (EditText) this.findViewById(R.id.editPeriod);
		editSumAssured = (EditText) this.findViewById(R.id.editSumAssured);
		editStartDate = (EditText) this.findViewById(R.id.editStartDate);
		editDueDate = (EditText) this.findViewById(R.id.editDueDate);
		
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query(Database.LI_TABLE_NAME, null,
				Database.LI_POLICY_NO + " = ?", new String[] { policyNo }, null, null, null);
		
		cursor.moveToFirst();
		
		this.editPolicyNo.setText(policyNo);
		this.editPolicyName.setText(  cursor.getString( cursor.getColumnIndex( Database.LI_POLICY_NAME)));
		this.editPremiumAmount.setText(  cursor.getString( cursor.getColumnIndex( Database.LI_PREMIUM_AMOUNT)));
		this.editPolicyHolder.setText(  cursor.getString( cursor.getColumnIndex( Database.LI_POLICY_HOLDER)));
		this.editCompany.setText(  cursor.getString( cursor.getColumnIndex( Database.LI_COMPANY)));
		this.editStartDate.setText(  cursor.getString( cursor.getColumnIndex( Database.LI_START_DATE)));
		this.editDueDate.setText(  cursor.getString( cursor.getColumnIndex( Database.LI_DUE_DATE)));
		this.editSumAssured.setText(  cursor.getString( cursor.getColumnIndex( Database.LI_SUM_ASSURED)));
		this.editPeriod.setText(  cursor.getString( cursor.getColumnIndex( Database.LI_PERIOD)));
        cursor.close();
        dbhelper.close();
	}
	
	public void update(View v) {
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// execute UPDATE command
			ContentValues values = new ContentValues();
			values.put( Database.LI_POLICY_NAME, editPolicyName.getText().toString());
			values.put( Database.LI_PREMIUM_AMOUNT, editPremiumAmount.getText().toString());
			values.put( Database.LI_COMPANY, editCompany.getText().toString());
			values.put( Database.LI_PERIOD, editPeriod.getText().toString());
			values.put( Database.LI_SUM_ASSURED, editSumAssured.getText().toString());
			values.put( Database.LI_POLICY_HOLDER, editPolicyHolder.getText().toString());
			values.put( Database.LI_START_DATE, editStartDate.getText().toString());
			values.put( Database.LI_DUE_DATE, editDueDate.getText().toString());
		
			long rows = db.update(Database.LI_TABLE_NAME, values, Database.LI_POLICY_NO + " = ?", new String[] { policyNo });

			db.close();
			if (rows > 0)
				Toast.makeText(this, "Updated Life Insurance Policy Details Successfully!",Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "Sorry! Could not update life insurance policy details!",Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void delete(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to delete this life insurance policy?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                deleteLifeInsurance();
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
         			
    public void deleteLifeInsurance() {
    	try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			int rows = db.delete(Database.LI_TABLE_NAME, Database.LI_POLICY_NO + "=?", 
					 new String[] { policyNo});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "Life Insurance Deleted Successfully!", Toast.LENGTH_LONG).show();
				this.finish();
			}
			else
				Toast.makeText(this, "Could not delete life insurance!", Toast.LENGTH_LONG).show();

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
	
    
}
