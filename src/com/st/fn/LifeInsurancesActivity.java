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

public class LifeInsurancesActivity extends Activity {
 
	DBHelper dbhelper; 
	ListView listLifeInsurances;
	Cursor cursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lifeinsurances);
        
		listLifeInsurances = (ListView) this.findViewById(R.id.listLifeInsurances);
		listLifeInsurances.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView, int arg2,long arg3) {
				TextView  textPolicyNo= (TextView) selectedView.findViewById(R.id.textPolicyNo);
				Intent intent = new Intent(LifeInsurancesActivity.this, UpdateLifeInsuranceActivity.class);
				intent.putExtra("policyno", textPolicyNo.getText().toString());
				startActivity(intent);
			}
		});
   
   
		try {
			dbhelper = new DBHelper(this);
			SQLiteDatabase db = dbhelper.getReadableDatabase();
			cursor = db.query( Database.LI_TABLE_NAME,null,null,null,null,null, Database.LI_DUE_DATE);
			startManagingCursor(cursor);
			String from [] = {Database.LI_POLICY_NO,Database.LI_COMPANY, Database.LI_POLICY_NAME, 
  					          Database.LI_POLICY_HOLDER, 
					          Database.LI_DUE_DATE, Database.LI_PREMIUM_AMOUNT };
			
			int to [] = { R.id.textPolicyNo, R.id.textCompany, R.id.textPolicyName, R.id.textPolicyHolder, R.id.textDueDate,  R.id.textPremiumAmount};
			
			SimpleCursorAdapter ca  = new SimpleCursorAdapter(this,R.layout.lifesinsurance, cursor,from,to);
		    listLifeInsurances.setAdapter(ca);
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
    
    
    public void addLifeInsurance(View v) {
    	 Intent intent = new Intent(this, AddLIActivity.class);
    	 startActivity(intent);
    }

}
