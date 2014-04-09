package com.st.fn;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddLIActivity extends Activity {

	private static final int START_DATE_DIALOG = 1;
	private static final int DUE_DATE_DIALOG = 2;

	int syear, smonth, sday;
	int dyear, dmonth, dday;
	EditText editStartDate, editDueDate, editPolicyHolder, editPolicyName,
			editPreimumAmount, editPolicyNo, editCompany, editPeriod,editSumAssured;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_li);

		editPolicyNo = (EditText) this.findViewById(R.id.editPolicyNo);
		editPolicyHolder = (EditText) this.findViewById(R.id.editPolicyHolder);
		editPolicyName = (EditText) this
				.findViewById(R.id.editPolicyName);
		editPreimumAmount = (EditText) this.findViewById(R.id.editPremiumAmount);
		editCompany = (EditText) this.findViewById(R.id.editCompany);
		editPeriod = (EditText) this.findViewById(R.id.editPeriod);
		editSumAssured = (EditText) this.findViewById(R.id.editSumAssured);
		editStartDate = (EditText) this.findViewById(R.id.editStartDate);
		editDueDate = (EditText) this.findViewById(R.id.editDueDate);

		// get the current date
		final Calendar c = Calendar.getInstance();
		dyear = syear = c.get(Calendar.YEAR);
		dmonth = smonth = c.get(Calendar.MONTH);
		dday = sday = c.get(Calendar.DAY_OF_MONTH);
		updateStartDateDisplay();
		updateDueDateDisplay();
		
		

	}

	private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int pYear, int pMonth, int pDay) {
			syear = pYear;
			smonth = pMonth;
			sday = pDay;
			updateStartDateDisplay();
		}
	};

	private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int pYear, int pMonth, int pDay) {
			dyear = pYear;
			dmonth = pMonth;
			dday = pDay;
			updateDueDateDisplay();
		}
	};

	public void add(View v) {
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// execute insert command

			ContentValues values = new ContentValues();
			values.put(Database.LI_POLICY_HOLDER, editPolicyHolder.getText()
					.toString());
			values.put(Database.LI_POLICY_NAME, editPolicyName
					.getText().toString());
			values.put(Database.LI_PREMIUM_AMOUNT, editPreimumAmount.getText()
					.toString());
			values.put(Database.LI_POLICY_NO, editPolicyNo.getText().toString());
			values.put(Database.LI_COMPANY, editCompany.getText()
					.toString());
			values.put(Database.LI_START_DATE, editStartDate.getText()
					.toString());
			values.put(Database.LI_DUE_DATE, editDueDate.getText().toString());
			values.put(Database.LI_PERIOD, editPeriod.getText().toString());
			values.put(Database.LI_SUM_ASSURED, editSumAssured.getText().toString());

			long rows = db.insert(Database.LI_TABLE_NAME, null, values);
			db.close();

			if (rows > 0) {
				Toast.makeText(this,
						"Added Life Insurance Details Successfully!",
						Toast.LENGTH_LONG).show();
				this.finish();
			} else
				Toast.makeText(this, "Sorry! Error occurred while adding!",
						Toast.LENGTH_LONG).show();

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void showStartDateDialog(View v) {
		showDialog(START_DATE_DIALOG);
	}

	public void showEndDateDialog(View v) {
		showDialog(DUE_DATE_DIALOG);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);

		switch (id) {
		case START_DATE_DIALOG:
			return new DatePickerDialog(this, startDateSetListener, syear,
					smonth, sday);
		case DUE_DATE_DIALOG:
			return new DatePickerDialog(this, endDateSetListener, dyear,
					dmonth, dday);
		}
		return null;
	}

	private void updateStartDateDisplay() {
		// Month is 0 based so add 1
		editStartDate.setText(String
				.format("%4d-%02d-%02d", syear, smonth + 1, sday));
	}

	private void updateDueDateDisplay() {
		// Month is 0 based so add 1
		editDueDate.setText(String.format("%4d-%02d-%02d", dyear, dmonth + 1, dday));
	}

	public void clearAll(View v) {

		editPolicyHolder.setText("");
		editPolicyName.setText("");
		editPreimumAmount.setText("");
		editPolicyHolder.setText("");
		editCompany.setText("");
		editPeriod.setText("");

		Calendar c = Calendar.getInstance();
		syear = c.get(Calendar.YEAR);
		smonth = c.get(Calendar.MONTH);
		sday = c.get(Calendar.DAY_OF_MONTH);
		updateStartDateDisplay();

	}
}
