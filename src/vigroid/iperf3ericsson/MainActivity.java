package vigroid.iperf3ericsson;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	private View  mChartView;
	DrawableResult iPerfResultMain;

	public native void runIperf();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//android.os.Debug.waitForDebugger();

		setContentView(R.layout.activity_main);
		
		//final TextView tv = (TextView) findViewById(R.id.mainTV);
		Button run_iPerf_button = (Button) findViewById(R.id.runTestButton);
		run_iPerf_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
        	       runIperf();
        	       //tv.setText("success");
        	       
        	       //After IPerf runs, a TestResult object should be created
        	       //   if (CODE INDICATING IPERF RAN SUCCESSFULLY){
        	       TestResult tr = new TestResult("KB/s", "sdcard/iPerfResult.json",MainActivity.this);
        	       tr.createDetailResult();
        	       
            }
        });
		
		Button draw_Graph_button = (Button) findViewById(R.id.drawGraphButton);
		draw_Graph_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {

					//initialize DrawableResult
	        	    iPerfResultMain = new DrawableResult("KB/s", "sdcard/iPerfResult.json", MainActivity.this);
					mChartView = iPerfResultMain.DrawChart(MainActivity.this);
					//this part is used to display graph on the xml
					LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
					//remove any views before u paint the chart
					chartContainer.removeAllViews();
					//adding the view to the linearlayout
					chartContainer.addView(mChartView);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	static {
		System.loadLibrary("ndklib");
	}
}
