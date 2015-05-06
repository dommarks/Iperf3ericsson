package vigroid.iperf3ericsson;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class FetchingTask extends AsyncTask<Object, Void, String> {

	protected ProgressDialog progressDialog;
	private final static String TAG = "AsyncIPerfEricsson";
	private Context mContext;
	private TestSubject test;

	public DrawableResult iPerfResultMain;
	private Spinner graphSpinner;
	private View mChartView;
	private LinearLayout chartContainer;
	
	private Runnable runnable;
	private Handler handler;
	

	public FetchingTask(Context context, TestSubject test,
			Spinner graphSpinner, View mChartView, LinearLayout chartContainer) {
		this.mContext = context;
		this.test = test;
		this.graphSpinner = graphSpinner;
		this.chartContainer = chartContainer;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		progressDialog = ProgressDialog.show(mContext, "Running iPerf test", "Please wait....",
				true, true);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG, "cancel");
				cancel(true);
			}
		});
		Log.d(TAG, "start progress bar");
	}

	@Override
	protected String doInBackground(Object... params) {
		Log.d(TAG, "running the iperf test!");
		return test.runTest();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.d(TAG, "done");
		progressDialog.dismiss();

		iPerfResultMain = new DrawableResult(test);
		if (!iPerfResultMain.isEmpty){
			TestResultDetails tr =  new TestResultDetails("sdcard/iPerfResult.json",mContext);
			tr.addToDB();
			chartUpdate(graphSpinner.getSelectedItemId());
		}

		Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
	}

	public void chartUpdate(long graphID) {
		if (!iPerfResultMain.isEmpty) {
			try {
				mChartView = iPerfResultMain.DrawChart(mContext, (int) graphID);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// remove any views before u paint the chart
			chartContainer.removeAllViews();
			// adding the view to the linearLayout
			chartContainer.addView(mChartView);
		}

	}
}
