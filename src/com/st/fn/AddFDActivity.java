package com.st.fn;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddFDActivity extends Activity {
	private static final int DEPOSIT_DATE_DIALOG = 1;
	private static final int MATURITY_DATE_DIALOG = 2;

	int myear, mmonth, mday;
	int dyear, dmonth, dday;
	EditText editDepositDate, editMaturityDate, editAccountHolder, editDepositAmount, editMaturityAmount, editDepositNo, editBank, editIntRate;
	CheckBox chkAutoRenewal;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_fd);

		editDepositNo = (EditText) this.findViewById(R.id.editDepositNo);
		editAccountHolder = (EditText) this.findViewById(R.id.editAccountHolder);
		editBank = (EditText) this.findViewById(R.id.editBank);
		editDepositAmount = (EditText) this.findViewById(R.id.editDepositAmount);
		editMaturityAmount = (EditText) this.findViewById(R.id.editMaturityAmount);
		editBank = (EditText) this.findViewById(R.id.editBank);
		editIntRate = (EditText) this.findViewById(R.id.editIntRate);
		editDepositDate = (EditText) this.findViewById(R.id.editDepositDate);
		editMaturityDate = (EditText) this.findViewById(R.id.editMaturityDate);
		chkAutoRenewal  = (CheckBox) this.findViewById(R.id.checkAutoRenewal);

		// get the current date
		final Calendar c = Calendar.getInstance();
		dyear = myear = c.get(Calendar.YEAR);
		dmonth = mmonth = c.get(Calendar.MONTH);
		dday = mday = c.get(Calendar.DAY_OF_MONTH);
		updateDepositDateDisplay();
		updateMaturityDateDisplay();

	}

	private DatePickerDialog.OnDateSetListener maturityDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int pYear, int pMonth, int pDay) {
			myear = pYear;
			mmonth = pMonth;
			mday = pDay;
			updateMaturityDateDisplay();
		}
	};

	private DatePickerDialog.OnDateSetListener depositDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int pYear, int pMonth, int pDay) {
			dyear = pYear;
			dmonth = pMonth;
			dday = pDay;
			updateDepositDateDisplay();
		}
	};

	public void add(View v) {
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// execute insert command

			ContentValues values = new ContentValues();
			values.put(Database.FD_DEPOIST_NO, editDepositNo.getText().toString());
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

			long rows = db.insert(Database.FD_TABLE_NAME, null, values);
			db.close();

			if (rows > 0) {
				Toast.makeText(this,						"Added Deposit Details Successfully!",
						Toast.LENGTH_LONG).show();
				clearAll(v);
			} else
				Toast.makeText(this, "Sorry! Error occurred while adding!",
						Toast.LENGTH_LONG).show();

		} catch (Exception ex) {
			Log.d(Database.TAG, "Error in add() " + ex.getMessage());
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void showDepositDateDialog(View v) {
		showDialog(DEPOSIT_DATE_DIALOG);
	}

	public void showMaturityDateDialog(View v) {
		showDialog(MATURITY_DATE_DIALOG);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);

		switch (id) {
		case DEPOSIT_DATE_DIALOG:
			return new DatePickerDialog(this, depositDateSetListener, dyear,
					dmonth, dday);
		case MATURITY_DATE_DIALOG:
			return new DatePickerDialog(this, maturityDateSetListener, myear,
					mmonth, mday);
		}
		return null;
	}

	private void updateDepositDateDisplay() {
		// Month is 0 based so add 1
		editDepositDate.setText(String
				.format("%4d-%02d-%02d", dyear, dmonth + 1, dday));
	}

	private void updateMaturityDateDisplay() {
		// Month is 0 based so add 1
		editMaturityDate.setText(String.format("%4d-%02d-%02d", myear, mmonth + 1, mday));
	}

	public void clearAll(View v) {

	    editAccountHolder.setText("");
		editDepositAmount.setText("");
		editMaturityAmount.setText("");
		editAccountHolder.setText("");
		editBank.setText("");
		editIntRate.setText("");
		editDepositDate.setText("");
		editMaturityDate.setText("");
		editDepositNo.setText("");
		editDepositNo.requestFocus();
   }
}
