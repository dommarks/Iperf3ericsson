package vigroid.iperf3ericsson;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends Activity {

	private View mChartView;
	private LinearLayout chartContainer;
	private Spinner graphSpinner;
	FetchingTask fetchingTask;

	SharedPreferences pref;

	TestSubject test;

	private Runnable runnable;
	private Handler handler;
	private Button runMultipleTestsButton;
	private boolean runTests = false;
	private LocationHelper lh;

	@Override
	protected void onRestart() {
		super.onRestart();
		graphSpinner.setSelection(Integer.parseInt(pref.getString(
				"chartDefaultType", "0")));
		if (fetchingTask != null && fetchingTask.iPerfResultMain != null)
			fetchingTask.chartUpdate(graphSpinner.getSelectedItemId());

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		//lh = new LocationHelper(getBaseContext());

		graphSpinner = (Spinner) findViewById(R.id.graph_select_spinner);

		pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		graphSpinner.setSelection(Integer.parseInt(pref.getString(
				"chartDefaultType", "0")));

		chartContainer = (LinearLayout) findViewById(R.id.chart);

		Button draw_Graph_button = (Button) findViewById(R.id.drawGraphButton);
		// Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)

		draw_Graph_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				test = new TestSubject(getApplicationContext());

				fetchingTask = new FetchingTask(MainActivity.this, test,
						graphSpinner, mChartView, chartContainer);
				fetchingTask.execute();

			}
		});
		
		runMultipleTestsButton = (Button) findViewById(R.id.runMultipleTests);
		runMultipleTestsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fetchingTask.cancel(true);
			}
		});

		graphSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (fetchingTask != null
						&& fetchingTask.iPerfResultMain != null)
					fetchingTask.chartUpdate(graphSpinner.getSelectedItemId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		Button summary_Button = (Button) findViewById(R.id.summaryButton);

		summary_Button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// start new activity
				startActivity(new Intent(MainActivity.this,
						SummaryActivity.class));

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
			startActivity(new Intent(this, IPerfPreferences.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
