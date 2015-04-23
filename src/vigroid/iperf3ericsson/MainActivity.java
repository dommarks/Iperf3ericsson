package vigroid.iperf3ericsson;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
private View  mChartView;
	
	private Spinner graphSpinner;
	DrawableResult iPerfResultMain;
	public native String runIperf(String host,int portNum);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//android.os.Debug.waitForDebugger();

		setContentView(R.layout.activity_main);
		
		graphSpinner = (Spinner) findViewById(R.id.graph_select_spinner);
		Button previous_tests_button = (Button) findViewById(R.id.previousTestButton);
		previous_tests_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
					Intent myIntent = new Intent(MainActivity.this, PreviousTests.class);
					//myIntent.putExtra("key", value); //Optional parameters
					MainActivity.this.startActivity(myIntent);
			}});
		
		Button draw_Graph_button = (Button) findViewById(R.id.drawGraphButton);
		draw_Graph_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), runIperf("iperf.scottlinux.com",5201),
						   Toast.LENGTH_LONG).show(); 
				////TESTING DB
//					TestResultDetails tr = new TestResultDetails(MainActivity.this);
//					tr.addToDB();
					
					
					//initialize DrawableResult
	        	    iPerfResultMain = new DrawableResult("KB/s", "sdcard/iPerfResult.json",getBaseContext());
	        	    if(iPerfResultMain != null && !iPerfResultMain.isEmpty ){
	        	    	chartUpdate(graphSpinner.getSelectedItemId());
	        	    	TestResult tr = new TestResult("KB/s", "sdcard/iPerfResult.json",getBaseContext());
	        	    	tr.createDetailResult();}
			}
		});	
		
		graphSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(iPerfResultMain != null && !iPerfResultMain.isEmpty )
					chartUpdate(graphSpinner.getSelectedItemId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void chartUpdate(long graphID) {
	    if(!iPerfResultMain.isEmpty){
	    	try {
				mChartView = iPerfResultMain.DrawChart(MainActivity.this, (int)graphID);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	//this part is used to display graph on the xml
	    	LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
	    	//remove any views before u paint the chart
	    	chartContainer.removeAllViews();
	    	//adding the view to the linearlayout
	    	chartContainer.addView(mChartView);
	    }
	}
	static {
		System.loadLibrary("ndklib");
	}
}
