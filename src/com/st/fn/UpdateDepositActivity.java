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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateDepositActivity extends Activity {
	private String depositNo;
	
	EditText editDepositDate, editMaturityDate, editAccountHolder, editDepositAmount, editMaturityAmount, editDepositNo, editBank, editIntRate;
	CheckBox chkAutoRenewal;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_fd);
		
		Intent intent = getIntent();
		depositNo =  intent.getStringExtra("depositno").toString();
		
		editDepositNo = (EditText) this.findViewById(R.id.editDepositNo);
		editAccountHolder = (EditText) this.findViewById(R.id.editAccountHolder);
		editBank= (EditText) this.findViewById(R.id.editBank);
		editIntRate = (EditText) this.findViewById(R.id.editIntRate);
		
		editDepositDate = (EditText) this.findViewById(R.id.editDepositDate);
		editDepositAmount = (EditText) this.findViewById(R.id.editDepositAmount);
		
		editMaturityDate = (EditText) this.findViewById(R.id.editMaturityDate);
		editMaturityAmount = (EditText) this.findViewById(R.id.editMaturityAmount);
		
		chkAutoRenewal = (CheckBox)  this.findViewById(R.id.checkAutoRenewal);
		
		DBHelper dbhelper = new DBHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query(Database.FD_TABLE_NAME, null,
				Database.FD_DEPOIST_NO + " = ?", new String[] { depositNo }, null, null, null);
		
		cursor.moveToFirst();
		
		this.editDepositNo.setText(depositNo);
		this.editAccountHolder.setText(  cursor.getString( cursor.getColumnIndex( Database.FD_ACCOUNT_HOLDER)));
		this.editBank.setText(  cursor.getString( cursor.getColumnIndex( Database.FD_BANK)));
		this.editDepositAmount.setText(  cursor.getString( cursor.getColumnIndex( Database.FD_DEPOSIT_AMOUNT)));
		this.editDepositDate.setText(  cursor.getString( cursor.getColumnIndex( Database.FD_DEPOSIT_DATE)));
		
		this.editMaturityAmount.setText(  cursor.getString( cursor.getColumnIndex( Database.FD_MATURITY_AMOUNT)));
		this.editMaturityDate.setText(  cursor.getString( cursor.getColumnIndex( Database.FD_MATURITY_DATE)));
				
		this.editIntRate.setText(  cursor.getString( cursor.getColumnIndex( Database.FD_INTEREST_RATE)));
		
		String autoRenewal = cursor.getString( cursor.getColumnIndex( Database.FD_AUTO_RENEWAL));
		
		if ( autoRenewal.equals("y") )
			  chkAutoRenewal.setChecked(true);
		else
			  chkAutoRenewal.setChecked(false);
        cursor.close();
        dbhelper.close();
	}
	
	public void update(View v) {
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// execute UPDATE command
			ContentValues values = new ContentValues();
			
			values.put(Database.FD_ACCOUNT_HOLDER, editAccountHolder.getText().toString());
			values.put(Database.FD_BANK, editBank.getText().toString());
			values.put(Database.FD_DEPOSIT_AMOUNT, editDepositAmount.getText()
					.toString());
			values.put(Database.FD_DEPOSIT_DATE, editDepositDate.getText()
					.toString());
			values.put(Database.FD_MATURITY_DATE, editMaturityDate.getText().toString());
			values.put(Database.FD_MATURITY_AMOUNT, editMaturityAmount.getText().toString());
			values.put(Database.FD_INTEREST_RATE, editIntRate.getText().toString());
			values.put(Database.FD_AUTO_RENEWAL,  chkAutoRenewal.isChecked() ? "y" : "n");
					
			long rows = db.update(Database.FD_TABLE_NAME, values, Database.FD_DEPOIST_NO + " = ?", new String[] { depositNo });

			db.close();
			if (rows > 0)
				Toast.makeText(this, "Updated Deposit Details Successfully!",Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "Sorry! Could not update deposit details!",Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void delete(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to delete this deposit?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                deleteDeposit();
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
         			
    public void deleteDeposit() {
    	try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			int rows = db.delete(Database.FD_TABLE_NAME, Database.FD_DEPOIST_NO + "=?",
					 new String[] { depositNo});
			dbhelper.close();
			if ( rows == 1) {
				Toast.makeText(this, "Deposit Deleted Successfully!", Toast.LENGTH_LONG).show();
				this.finish();
			}
			else
				Toast.makeText(this, "Could not delete deposit!", Toast.LENGTH_LONG).show();

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
	
    
}
