package vigroid.iperf3ericsson;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	
	double latitude = 0;
	double longitude = 0;
	

	public native void runIperf();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//android.os.Debug.waitForDebugger();
		final LocationHelper loc = new LocationHelper(getApplicationContext());
		loc.getLocation();
		
		setContentView(R.layout.activity_main);
		
		final TextView tv = (TextView) findViewById(R.id.mainTV);
		Button run_iPerf_button = (Button) findViewById(R.id.runTestButton);
		run_iPerf_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	loc.getLocation();
        	       runIperf();
        	       latitude = loc.getLatitude();
        	       longitude = loc.getLongitude();
        	       tv.setText("success");
            }
        });
		
		Button draw_Graph_button = (Button) findViewById(R.id.drawGraphButton);
		draw_Graph_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//tv.setText("HEiehi");
				// Read Json from SDcard and Display it
				try{
					//TODO consider take a variable instead of a fixed path
					File JsonFile = new File("sdcard/iPerfResult.json");
		            FileInputStream stream = new FileInputStream(JsonFile);
		            String jsonStr = null;
				
		            try {
		            	FileChannel fc = stream.getChannel();
		            	MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

		            	jsonStr = Charset.defaultCharset().decode(bb).toString();
		            }
		            finally {
		            	stream.close();
		            }
		            
		            JSONObject jsonObj = new JSONObject(jsonStr);

		            // Getting data JSON Object nodes
		            JSONObject data  = jsonObj.getJSONObject("end").getJSONObject("sum_received");

		            /* looping through All nodes
		            for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);*/
                    // get the data we need from json file
                    String id = data.getString("bits_per_second");
                    Double netWorkSpeed = (double) Double.valueOf(id).longValue();
                    
                    //use >  int id = c.getInt("duration"); if you want get an int


                    // tmp hashmap for single node
                    HashMap<String, String> parsedData = new HashMap<String, String>();

                    // TODO change to meat our requirement
                    //parsedData.put("id", id);
                    //parsedData.put("title", title);
                    //parsedData.put("duration", duration);


                    // do what do you want on your interface
                    tv.setText(Double.toString(netWorkSpeed/8000)+ "KB/s");
                  

				
				}
				catch (Exception e) {
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
