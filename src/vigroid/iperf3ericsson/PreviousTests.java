package vigroid.iperf3ericsson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity to show previous iperf tests run on device
 */
public class PreviousTests extends Activity {
	public static ArrayList<String> ArrayofTests = new ArrayList<String>();
	private GridView gridView;
	private Button exportDataButton;
	IPerfDBHelper db;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_history);
		
		db = new IPerfDBHelper(getApplicationContext());
		// Reading all contacts
		
        List<TestResultDetails> trdList = db.getAllTests();       
        Log.w("IPERF", "Size is: "+Integer.toString(trdList.size()));
        for (TestResultDetails tr : trdList) {
            String log = "Timestamp: "+tr.getTimestamp()+" ,IP : " + tr.getIpAddress() + " ,Phone: " + 
        tr.getModelNumber();
                // Writing Contacts to log
        Log.w("IPERF", log);

        }

        
        Log.w("IPERF","Number of tests = "+Integer.toString(db.getNumberOfTestsRun()));
        //Log.w("IPERF",db.getTestResultByID(10000000).toString());
        
		
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ArrayofTests);
		
		gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(adapter);

//        gridView.setOnTouchListener(new setOnTouchListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                int position, long id) {
//               Toast.makeText(getApplicationContext(),
//                ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
		
        exportDataButton = (Button) findViewById(R.id.exportButton);
        exportDataButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (db.getNumberOfTestsRun()>0){
					
					String fileLocation = db.exportDatabase();
					generateEmail(fileLocation);
					
				}else{
					Toast.makeText(getApplicationContext(),R.string.run_tests_first,Toast.LENGTH_LONG).show(); 
				}
				
					
					
			}
		});	
        
        
        
	}
	
	public void generateEmail(String fileLocation){
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.email_address)});
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
		File root = Environment.getExternalStorageDirectory();
		
		File file = new File(root, "FILEEXPORT.csv");
		
		Log.w("IPERF","Exists:"+Boolean.toString(file.exists())+" Canread:"+Boolean.toString(file.canRead()));
		
		if (!file.exists() || !file.canRead()) {
		    Toast.makeText(getBaseContext(), "Attachment Error", Toast.LENGTH_SHORT).show();
		    finish();
		    return;
		}
		Uri uri = Uri.fromFile(file);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(intent, "Send email..."));
		
		
	}
	
}
