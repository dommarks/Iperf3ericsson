package vigroid.iperf3ericsson;


import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.View;



public class DrawableResult extends TestResult{

	public DrawableResult(String defaultunit, String fileLocation, Context context) {
		super(defaultunit, fileLocation, context);
		// TODO Auto-generated constructor stub
		
	}
	
	public View DrawChart(Context context, int graphID) throws JSONException{
		
		if(isEmpty)
			return null;
		
        JSONArray intervalArray  = JSONResult.getJSONArray("intervals");
        double[] speed = new double[intervalArray.length()];
        String nodeValue;
        //looping through All nodes in interval array
        for (int i = 0; i < intervalArray.length(); i++) {
        	JSONObject speedNode = intervalArray.getJSONObject(i).getJSONObject("sum");
        	// get the data we need from JSON file
        	nodeValue = speedNode.getString("bits_per_second");
        	//convert String to double, and keep two digits
        	double temp=((double) Double.valueOf(nodeValue).longValue())/8192;
        	speed[i]=((int)(temp*100))/100.0;
        	
        }
        
        // Show data on the user interface

		String[] mInterval = new String[intervalArray.length()];
		for(int i=0;i<intervalArray.length();i++){
			
			mInterval[i] = "Time" + Integer.toString(i);
		}
		
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
		  multiRenderer.setPanEnabled(true, false);
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
		  multiRenderer.setShowGrid(true);
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
		  multiRenderer.setYAxisMax(maxValue+50);
		  //setting used to move the graph on xaxiz to .5 to the right
		  multiRenderer.setXAxisMin(-0.5);
		 //setting max values to be display in x axis
		  multiRenderer.setXAxisMax(5);
		  //setting bar size or space between two bars
		  multiRenderer.setBarSpacing(0.5);
		  //Setting background color of the graph to transparent
		  multiRenderer.setBackgroundColor(Color.GRAY);
		  //Setting margin color of the graph to transparent
		  multiRenderer.setMarginsColor(context.getResources().getColor(R.color.transparent_background));
		  multiRenderer.setApplyBackgroundColor(true);

		  //setting the margin size for the graph in the order top, left, bottom, right
		  multiRenderer.setMargins(new int[]{30, 30, 30, 30});
		  multiRenderer.setPointSize(7f);



		  for(int i=0; i< mInterval.length;i++){
			  multiRenderer.addXTextLabel(i, mInterval[i]);
		  }
		  
		  // Adding incomeRenderer and expenseRenderer to multipleRenderer
		  // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		  // should be same
		  XYSeriesRenderer speedRenderer = new 
					  XYSeriesRenderer();
		  //customize the speedRenderer
		  speedRenderer.setChartValuesTextSize(24);
		  speedRenderer.setFillPoints(true);
		  speedRenderer.setLineWidth(2);
		  speedRenderer.setDisplayChartValues(true);
		  speedRenderer.setDisplayChartValuesDistance(10);
		  speedRenderer.setPointStyle(PointStyle.SQUARE);
		  
		//setting if it is area chart or just line chart
		  if(graphID==3){
			  FillOutsideLine fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ALL);
		  	  fill.setColor(Color.GREEN);
		  	  speedRenderer.addFillOutsideLine(fill);
		  }
		  multiRenderer.addSeriesRenderer(speedRenderer);
		  
		  switch (graphID) {
		  	case 0:
		  		return ChartFactory.getBarChartView(context, speedDataset, multiRenderer,Type.DEFAULT);
		  	case 1:
		  		return ChartFactory.getLineChartView(context, speedDataset, multiRenderer);
		  	case 2:
		  		return ChartFactory.getCubeLineChartView(context, speedDataset, multiRenderer, 0.25f);
		  	case 3:
		  		return ChartFactory.getCubeLineChartView(context, speedDataset, multiRenderer, 0.25f);
		  	case 4:
		  		//TODO delete
		  		return ChartFactory.getTimeChartView(context, speedDataset, multiRenderer, "dd-MMM-yyyy");
		  	case 5:
		  		//TODO dialchart
		  		return ChartFactory.getTimeChartView(context, speedDataset, multiRenderer, "dd-MMM-yyyy");
		  	default:
		  		return  ChartFactory.getBarChartView(context, speedDataset, multiRenderer,Type.DEFAULT);
			}
	}

}