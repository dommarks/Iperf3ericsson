package vigroid.iperf3ericsson;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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

	private View mChartView;
	LinearLayout chartContainer;

	private Spinner graphSpinner;

	DrawableResult iPerfResultMain;
	SharedPreferences pref;
	TestSubject test;
	private Runnable runnable;
	private Handler handler;
	private boolean runTests = false;
	private LocationHelper lh;
	
	private Button runMultipleTestsButton;

	@Override
	protected void onRestart() {
		super.onRestart();
		graphSpinner.setSelection(Integer.parseInt(pref.getString("chartDefault", "0")));
		if (iPerfResultMain != null)
			chartUpdate(graphSpinner.getSelectedItemId());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		lh = new LocationHelper(getBaseContext());
		
		setContentView(R.layout.activity_main);

		graphSpinner = (Spinner) findViewById(R.id.graph_select_spinner);
		
		pref= PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		
		graphSpinner.setSelection(Integer.parseInt(pref.getString("chartDefault", "0")));

		Button draw_Graph_button = (Button) findViewById(R.id.drawGraphButton);

		chartContainer = (LinearLayout) findViewById(R.id.chart);

		draw_Graph_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// initial test and run iPerf3
				test = new TestSubject(getApplicationContext());
				Toast.makeText(getApplicationContext(), test.runTest(),
						Toast.LENGTH_LONG).show();

				// initialize DrawableResult
				iPerfResultMain = new DrawableResult(test);
				if (iPerfResultMain != null && !iPerfResultMain.isEmpty)
					chartUpdate(graphSpinner.getSelectedItemId());
				TestResult tr = new TestResult("KB/s", "sdcard/iPerfResult.json",getBaseContext());
    	    	tr.createDetailResult();
    	    	
			}
		});

		runMultipleTestsButton = (Button) findViewById(R.id.runMultipleTests);
		runMultipleTestsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				runTests = !runTests;
				
				if (runTests){
					lh.startLocationListening();
					runMultipleTestsButton.setText(getResources().getString(R.string.stop_tests));
					handler = new Handler();
					runnable = new Runnable() {
					    public void run() {
					    	lh.startLocationListening();
							test = new TestSubject(getApplicationContext());
							Toast.makeText(getApplicationContext(), test.runTest(),
									Toast.LENGTH_LONG).show();

							// initialize DrawableResult
							iPerfResultMain = new DrawableResult(test);
							if (iPerfResultMain != null && !iPerfResultMain.isEmpty)
								chartUpdate(graphSpinner.getSelectedItemId());
							TestResult tr = new TestResult("KB/s", "sdcard/iPerfResult.json",getBaseContext());
			    	    	tr.createDetailResult();
					        handler.postDelayed(this, 1000);
					    }
					};
					handler.postDelayed(runnable, 1);	
				}else{
					handler.removeCallbacks(runnable);
					lh.stopLocationListening();
					runMultipleTestsButton.setText(getResources().getString(R.string.run_continuously));
				}
			}
		});
		
		graphSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (iPerfResultMain != null && !iPerfResultMain.isEmpty)
					chartUpdate(graphSpinner.getSelectedItemId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		
		Button previous_tests_button = (Button) findViewById(R.id.previousTestButton);
		previous_tests_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
					Intent myIntent = new Intent(MainActivity.this, PreviousTests.class);
					//myIntent.putExtra("key", value); //Optional parameters
					MainActivity.this.startActivity(myIntent);
			}});
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
			startActivity(new Intent(this, IPerfPreferences.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// draw the graph again if orientation changed
		super.onConfigurationChanged(newConfig);
		if (iPerfResultMain != null)
			chartUpdate(graphSpinner.getSelectedItemId());
	}


	public void chartUpdate(long graphID) {
		if (!iPerfResultMain.isEmpty) {
			try {
				mChartView = iPerfResultMain.DrawChart(MainActivity.this,
						(int) graphID);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// this part is used to display graph on the XML

			// remove any views before u paint the chart
			chartContainer.removeAllViews();
			// adding the view to the linearLayout
			chartContainer.addView(mChartView);
		}
	}
}
