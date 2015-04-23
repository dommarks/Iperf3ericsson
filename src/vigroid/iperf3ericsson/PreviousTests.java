package vigroid.iperf3ericsson;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity to show previous iperf tests run on device
 */
public class PreviousTests extends Activity {
	public static ArrayList<String> ArrayofTests = new ArrayList<String>();
	private GridView gridView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_history);
		
		IPerfDBHelper db = new IPerfDBHelper(getApplicationContext());
		// Reading all contacts
		
        List<TestResultDetails> trdList = db.getAllTests();       
        Log.w("IPERF", "Size is: "+Integer.toString(trdList.size()));
        for (TestResultDetails tr : trdList) {
            String log = "Timestamp: "+tr.getTimestamp()+" ,IP : " + tr.getIpAddress() + " ,Phone: " + 
        tr.getModelNumber();
                // Writing Contacts to log
        Log.w("IPERF", log);

        }

        //db.getAllTests();
        
        Log.w("IPERF","Number of tests = "+Integer.toString(db.getNumberOfTestsRun()));
        //Log.w("IPERF",db.getTestResultByID(10000000).toString());
        
		
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ArrayofTests);
		
		gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(adapter);
//
//        gridView.setOnTouchListener(new setOnTouchListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                int position, long id) {
//               Toast.makeText(getApplicationContext(),
//                ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
		
	}
	
	
}
