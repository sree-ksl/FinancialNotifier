package com.st.fn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BackupRestoreActivity  extends Activity {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.backuprestore);
	    }

	 public String getBackupFilePath() {
		 String state = Environment.getExternalStorageState();
		 if (state.equals(Environment.MEDIA_MOUNTED))
		 {
			 
			 File card =  Environment.getExternalStorageDirectory();
			 File dir =  new File(card.getAbsolutePath() + "/com.st.fn");
			 
			 if ( !dir.exists()) {
				 Log.d(Database.TAG,"Backup directory created on SDCARD");
				 dir.mkdir();
			 }
			 return  dir.getAbsolutePath() + "/database.backup";
		 }
		 else
			 return null;
	 }
	 
	 public void backupDatabase()  {
		 
		 // open database file and copy data into backup file 
		 String backup_path = getBackupFilePath();
		 if ( backup_path != null ) {
			 // procced to backup
			 String database_path = Database.getDatabasePath(this);
			 // open database file and copy to backup file
			 try {
			 FileInputStream input = new FileInputStream(database_path); 
			 FileOutputStream output = new FileOutputStream(backup_path);
			 
  		     // transfer bytes from the Input File to the Output File
			 byte[] buffer = new byte[1024];
  		     int length;
			 while ((length = input.read(buffer)) > 0) {
			        output.write(buffer, 0, length);
  		     }
 		     output.flush();
			 output.close();
			 input.close();
			 Log.d(Database.TAG,"Backup database to " + backup_path);
			 Toast.makeText(this, "Database Backup Completed!", Toast.LENGTH_LONG).show();
			 this.finish();
			 }
			 catch(Exception ex) {
				 Log.d(Database.TAG,"Backup database error: " + ex.getMessage());
				 Toast.makeText(this, "Could not backup Database!", Toast.LENGTH_LONG).show();
			 }
		 }
		 else
			 Toast.makeText(this, "Sorry. No backup is possible! ",Toast.LENGTH_LONG).show();
			 
	 }
	 
	 public void restore(View v) {
	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to restore old database from SDCARD to current database?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                restoreDatabase();
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
	 
	 public void backup(View v) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to backup database to SDCARD?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                backupDatabase();
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
	 
     public void restoreDatabase()  {
    	 
		 
		 // open database file and copy data into backup file 
		 String backup_path = getBackupFilePath();
		 if ( backup_path != null ) {
			 
			 // check whether file is physically present 
			 File backupfile = new File(backup_path);
			 if ( !backupfile.exists()) {
				 Log.d( Database.TAG,"Backup file [ " +  backup_path + " ] is missing");
				 Toast.makeText(this, "Database backup is missing!", Toast.LENGTH_LONG).show();
				 return;
			 }
			 
			 
			 // Proceed to restore backup database to current database
			 String database_path = Database.getDatabasePath(this);
			 // open database file and copy to backup file
			 try {
			 FileInputStream input = new FileInputStream(backup_path); 
			 FileOutputStream output = new FileOutputStream(database_path);
			 
  		     // transfer bytes from the Input File to the Output File
			 byte[] buffer = new byte[1024];
  		     int length;
			 while ((length = input.read(buffer)) > 0) {
			         output.write(buffer, 0, length);
  		     }
 		     output.flush();
			 output.close();
			 input.close();
			 Log.d(Database.TAG,"Restored database to " + database_path + " from " + backup_path);
			 Toast.makeText(this, "Database Restore Completed!", Toast.LENGTH_LONG).show();
			 this.finish();
			 }
			 catch(Exception ex) {
				 Log.d( Database.TAG,"Restore database error: " + ex.getMessage());
				 Toast.makeText(this, "Could not restore Database!", Toast.LENGTH_LONG).show();
			 }
		 }
		 else
			 Toast.makeText(this, "Sorry. No backup is found, so restore is not possible! ",Toast.LENGTH_LONG).show();
			 
	 }

}
