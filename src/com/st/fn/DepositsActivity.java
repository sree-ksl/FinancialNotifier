package com.st.fn;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DepositsActivity extends Activity {
	DBHelper dbhelper; 
	ListView listDeposits;
	Cursor cursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposits);
        
		listDeposits = (ListView) this.findViewById(R.id.listDeposits);
		listDeposits.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView, int arg2,long arg3) {
				TextView  textDepositNo = (TextView) selectedView.findViewById(R.id.textDepositNo);
				Intent intent = new Intent(DepositsActivity.this, UpdateDepositActivity.class);
				intent.putExtra("depositno", textDepositNo.getText().toString());
				startActivity(intent);
			}
		});
		
		try {
			dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			cursor = db.query( Database.FD_TABLE_NAME,null,null,null,null,null, Database.FD_MATURITY_DATE);
			startManagingCursor(cursor);
			String from [] = {Database.FD_DEPOIST_NO,Database.FD_BANK, 
  					          Database.FD_ACCOUNT_HOLDER, 
					          Database.FD_MATURITY_DATE, Database.FD_MATURITY_AMOUNT };
			
			int to [] = { R.id.textDepositNo, R.id.textBank, R.id.textAccountHolder, R.id.textMaturityDate, R.id.textMaturityAmount};
			
			SimpleCursorAdapter ca  = new SimpleCursorAdapter(this,R.layout.deposit, cursor,from,to);
		    listDeposits.setAdapter(ca);
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
    
    public void addDeposit(View v) {
    	 Intent intent = new Intent(this, AddFDActivity.class);
    	 startActivity(intent);
    }

}
