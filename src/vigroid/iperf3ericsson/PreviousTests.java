package vigroid.iperf3ericsson;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Activity to show previous iperf tests run on device
 */
public class PreviousTests extends Activity{
	public static ArrayList<String> ArrayofTests = new ArrayList<String>();
	public static ArrayAdapter<String> adapter;
	private List<TestResultDetails> trdList;
	private GridView gridView;
	private Button exportEmailButton;
	private Button exportFTPButton;
	private Button deleteAll;
	
	private IPerfDBHelper db;
	private boolean ftpInProgress;
	private boolean deleteYes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ftpInProgress = false;deleteYes = false;
		db = new IPerfDBHelper(PreviousTests.this);
		//trdList = db.getAllTests(); 
		setContentView(R.layout.activity_test_history);
		adapter = new ArrayAdapter<String>(PreviousTests.this,android.R.layout.simple_list_item_1, ArrayofTests);

		// Reading all contacts
		trdList = db.getAllTests();       
        
        //Log.w("IPERF","Number of tests = "+Integer.toString(db.getNumberOfTestsRun()));
		gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(adapter);
        
		runOnUiThread(new Runnable() {
		    public void run() {
		        adapter.notifyDataSetChanged();
		    }
		});
		adapter.notifyDataSetChanged();
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View v,
	                int position, long id) {
	        	
	        	Toast.makeText(PreviousTests.this, trdList.get(position).toString(), 5).show();
	        	Intent intent = new Intent(getBaseContext(), ShowIndividualTest.class);
	        	intent.putExtra("testID", trdList.get(position).getTestID().toString());
	        	startActivity(intent);
	        }
	    });
    
		
        ///**Button Handlers**///
        
        exportEmailButton = (Button) findViewById(R.id.exportButtonEmail);
        exportEmailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (db.getNumberOfTestsRun()>0){
					generateEmail(db.exportDatabase());
				}else{
					Toast.makeText(getApplicationContext(),R.string.run_tests_first,Toast.LENGTH_LONG).show(); 
				}		
			}
		});	    
        
        exportFTPButton = (Button) findViewById(R.id.exportButtonFTP);
        exportFTPButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!ftpInProgress){
					ftpInProgress = true;
					if (db.getNumberOfTestsRun()>0){
						exportFTPButton.setText(R.string.export_ftp_button_in_progress);
						String fileLocation = db.exportDatabase();
						new FTPExport(fileLocation,getBaseContext(),PreviousTests.this).execute();
						ftpInProgress = false;
						exportFTPButton.setText(R.string.export_ftp_button);
					}else{
						ftpInProgress = false;
						Toast.makeText(getApplicationContext(),R.string.run_tests_first,Toast.LENGTH_LONG).show(); 
					}		
				}	
			}
		});	   
        
        
        deleteAll = (Button) findViewById(R.id.deleteButton);
        deleteAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteDialog();
				if (deleteYes){
					trdList = db.getAllTests();
		            adapter.clear();
			        deleteYes = false;
				}
				}
			});	
        
	}
	
	//Utility methods//
	
	public boolean deleteDialog(){
		deleteYes = false;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            db.deleteAllRecords();
		            deleteYes = true;
//		    		adapter = new ArrayAdapter<String>(this,
//		                    android.R.layout.simple_list_item_1, ArrayofTests);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(PreviousTests.this);
		builder.setMessage(R.string.delete_all_ask).setPositiveButton(R.string.delete_all_yes, dialogClickListener)
		    .setNegativeButton(R.string.cancel, dialogClickListener).show();
		return deleteYes;
	}
	
	public void generateEmail(String fileLocation){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.email_address)});
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
		
		File file = new File(fileLocation);
		if (!file.exists() || !file.canRead()) {
		    Toast.makeText(getBaseContext(), R.string.unable_to_attach, Toast.LENGTH_SHORT).show();
		    finish();
		    return;
		}
		
		Uri uri = Uri.fromFile(file);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(intent, "Send email..."));
	}
	
}
