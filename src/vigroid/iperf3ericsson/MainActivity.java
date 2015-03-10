package vigroid.iperf3ericsson;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.LineChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	private View  mChartView;

	public native void runIperf();
	
	private void DrawCharts(double[] speed){

		String[] mInterval = new String[] {
				"Time1","Time2","Time3","Time4","Time5"
		};
		double maxValue=speed[0];

		for (int i=1;i<speed.length;i++){
			if(speed[i]>maxValue)
				maxValue=speed[i];
		}
		
		//Requires two parts to draw a chart - dataset and renderer
		//Creating an XYseries for speed
		XYSeries speedSeries = new XYSeries("Speed");
		//Adding data to the Series
		for(int i=0;i<mInterval.length;i++){
			speedSeries.add(i, speed[i]);
		}
		//Creating a dataset to hold the series
		XYMultipleSeriesDataset speedDataset = new XYMultipleSeriesDataset();
		
		//add speed data to the data set
		speedDataset.addSeries(speedSeries);
		
		//Write renderer use mulitpleRender for future extensions
		//TODO Animate the renderer, probably need to rewrite the render methods

		
		//Creating a XYMultipleSeriesRenderer
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		 multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		 multiRenderer.setXLabels(0);
		 multiRenderer.setChartTitle("Network Speed Chart");
		 multiRenderer.setXTitle("Time Intervals");
		 //TODO dynamic units needed, useful when need to work on setting etc.
		 multiRenderer.setYTitle("Network Speed in KB/s");
		 /***
		  * Customizing graphs
		  */
		 //setting text size of the title
		  multiRenderer.setChartTitleTextSize(28);
		  //setting text size of the axis title
		  multiRenderer.setAxisTitleTextSize(24);
		  //setting text size of the graph label
		  multiRenderer.setLabelsTextSize(24);
		  //setting zoom buttons visibility
		  multiRenderer.setZoomButtonsVisible(false);
		  //setting pan inability which uses graph to move on both axis
		  multiRenderer.setPanEnabled(false, false);
		  //setting click false on graph
		  multiRenderer.setClickEnabled(false);
		  //setting zoom to false on both axis
		  multiRenderer.setZoomEnabled(false, false);
		  //setting lines to display on y axis
		  multiRenderer.setShowGridY(false);
		  //setting lines to display on x axis
		  multiRenderer.setShowGridX(false);
		  //setting legend to fit the screen size
		  multiRenderer.setFitLegend(true);
		  //setting displaying line on grid
		  multiRenderer.setShowGrid(false);
		  //setting zoom to false
		  multiRenderer.setZoomEnabled(false);
		  //setting external zoom functions to false
		  multiRenderer.setExternalZoomEnabled(false);
		  //setting displaying lines on graph to be formatted(like using graphics)
		  multiRenderer.setAntialiasing(true);
		  //setting to in scroll to false
		  multiRenderer.setInScroll(false);
		  //setting to set legend height of the graph
		  multiRenderer.setLegendHeight(30);
		  //setting x axis label align
		  multiRenderer.setXLabelsAlign(Align.CENTER);
		  //setting y axis label to align
		  multiRenderer.setYLabelsAlign(Align.LEFT);
		  //setting text style
		  multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		  //setting no of values to display in y axis
		  multiRenderer.setYLabels(10);
		  // setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
		  // if you use dynamic values then get the max y value and set here
		  multiRenderer.setYAxisMin(0);
		  multiRenderer.setYAxisMax(maxValue);
		  //setting used to move the graph on xaxiz to .5 to the right
		  multiRenderer.setXAxisMin(-0.5);
		 //setting max values to be display in x axis
		  multiRenderer.setXAxisMax(5);
		  //setting bar size or space between two bars
		  multiRenderer.setBarSpacing(0.5);
		  //Setting background color of the graph to transparent
		  multiRenderer.setBackgroundColor(Color.TRANSPARENT);
		  //Setting margin color of the graph to transparent
		  multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent_background));
		  multiRenderer.setApplyBackgroundColor(true);

		  //setting the margin size for the graph in the order top, left, bottom, right
		  multiRenderer.setMargins(new int[]{30, 30, 30, 30});

		  for(int i=0; i< mInterval.length;i++){
			  multiRenderer.addXTextLabel(i, mInterval[i]);
		  }
		  
		  // Adding incomeRenderer and expenseRenderer to multipleRenderer
		  // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		  // should be same
		  XYSeriesRenderer speedRenderer = new 
					  XYSeriesRenderer();
		  //customize the speedRenderer
		  speedRenderer.setFillPoints(true);
		  speedRenderer.setLineWidth(2);
		  speedRenderer.setDisplayChartValues(true);
		  speedRenderer.setDisplayChartValuesDistance(10);
		  multiRenderer.addSeriesRenderer(speedRenderer);
		  
		  //this part is used to display graph on the xml
		  LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
		  //remove any views before u paint the chart
		  chartContainer.removeAllViews();
		  String[] types = new String[] { LineChart.TYPE, BarChart.TYPE };
		  
	      // Creating a combined chart with the chart types specified in types array
		  //mChartView = (GraphicalView)ChartFactory.getCombinedXYChartView(MainActivity.this, speedDataset, multiRenderer, types);
		 
		  mChartView = ChartFactory.getBarChartView(MainActivity.this, speedDataset, multiRenderer,Type.DEFAULT);
		  //adding the view to the linearlayout
		  chartContainer.addView(mChartView);
	}
	

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
            }
        });
		
		Button draw_Graph_button = (Button) findViewById(R.id.drawGraphButton);
		draw_Graph_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Read JSON file from SDcard and Display it
				try{
					//TODO consider take a variable instead of a fixed path
					File JsonFile = new File("sdcard/iPerfResult.json");
		            FileInputStream stream = new FileInputStream(JsonFile);
		            String jsonStr = null;
		            double[] speed;
				
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
		            JSONArray intervalArray  = jsonObj.getJSONArray("intervals");
		            speed = new double[intervalArray.length()];
		            String nodeValue;
		            //looping through All nodes in interval array
		            for (int i = 0; i < intervalArray.length(); i++) {
		            	JSONObject speedNode = intervalArray.getJSONObject(i).getJSONObject("sum");
                    	// get the data we need from json file
		            	nodeValue = speedNode.getString("bits_per_second");
		            	//convert String to double
		            	speed[i]=((double) Double.valueOf(nodeValue).longValue())/8000;
		            }


                    // Show data on the user interface
                    //tv.setText(Double.toString(speed[1]/8000)+ "KB/s");
		            DrawCharts(speed);
                    

				
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
