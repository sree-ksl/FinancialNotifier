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

public class AddVIActivity extends Activity {

	private static final int START_DATE_DIALOG = 1;
	private static final int END_DATE_DIALOG = 2;

	int syear, smonth, sday;
	int eyear, emonth, eday;
	EditText editStartDate, editEndDate, editVehicleNo, editVehicleDetails,
			editVehicleValue, editPolicyNo, editCompany, editAmount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_vi);

		editVehicleNo = (EditText) this.findViewById(R.id.editVehicleNo);
		editVehicleDetails = (EditText) this
				.findViewById(R.id.editVehicleDetails);
		editVehicleValue = (EditText) this.findViewById(R.id.editVehicleValue);
		editPolicyNo = (EditText) this.findViewById(R.id.editPolicyNo);
		editCompany = (EditText) this.findViewById(R.id.editCompany);
		editAmount = (EditText) this.findViewById(R.id.editAmount);

		editStartDate = (EditText) this.findViewById(R.id.editStartDate);
		editEndDate = (EditText) this.findViewById(R.id.editEndDate);

		
		
		// get the current date
		final Calendar c = Calendar.getInstance();
		eyear = syear = c.get(Calendar.YEAR);
		emonth = smonth = c.get(Calendar.MONTH);
		eday = sday = c.get(Calendar.DAY_OF_MONTH);
		updateStartDateDisplay();
		updateEndDateDisplay();
		
		

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
			eyear = pYear;
			emonth = pMonth;
			eday = pDay;
			updateEndDateDisplay();
		}
	};

	public void add(View v) {
		try {
			DBHelper dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			// execute insert command

			ContentValues values = new ContentValues();
			values.put(Database.VI_VEHICLE_NO, editVehicleNo.getText()
					.toString());
			values.put(Database.VI_VEHICLE_DETAILS, editVehicleDetails
					.getText().toString());
			values.put(Database.VI_VEHICLE_VALUE, editVehicleValue.getText()
					.toString());
			values.put(Database.VI_POLICY_NO, editPolicyNo.getText().toString());
			values.put(Database.VI_POLICY_COMPANY, editCompany.getText()
					.toString());
			values.put(Database.VI_START_DATE, editStartDate.getText()
					.toString());
			values.put(Database.VI_END_DATE, editEndDate.getText().toString());
			values.put(Database.VI_AMOUNT, editAmount.getText().toString());

			long rows = db.insert(Database.VI_TABLE_NAME, null, values);
			db.close();

			if (rows > 0) {
				Toast.makeText(this,
						"Added Vehicle Insurance Details Successfully!",
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
		showDialog(END_DATE_DIALOG);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);

		switch (id) {
		case START_DATE_DIALOG:
			return new DatePickerDialog(this, startDateSetListener, syear,
					smonth, sday);
		case END_DATE_DIALOG:
			return new DatePickerDialog(this, endDateSetListener, eyear,
					emonth, eday);
		}
		return null;
	}

	private void updateStartDateDisplay() {
		// Month is 0 based so add 1
		editStartDate.setText(String
				.format("%4d-%02d-%02d", syear, smonth + 1, sday));
	}

	private void updateEndDateDisplay() {
		// Month is 0 based so add 1
		editEndDate.setText(String.format("%4d-%02d-%02d", eyear, emonth + 1, eday));
	}

	public void clearAll(View v) {

		editVehicleNo.setText("");
		editVehicleDetails.setText("");
		editVehicleValue.setText("");
		editPolicyNo.setText("");
		editCompany.setText("");
		editAmount.setText("");

		Calendar c = Calendar.getInstance();
		syear = c.get(Calendar.YEAR);
		smonth = c.get(Calendar.MONTH);
		sday = c.get(Calendar.DAY_OF_MONTH);
		updateStartDateDisplay();

	}
}
