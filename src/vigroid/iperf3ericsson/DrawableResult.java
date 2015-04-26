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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.View;

public class DrawableResult extends TestResult {

	// Chart settings load from preference
	private String displayUnit;
	private String chartFontSize;

	public DrawableResult(TestSubject teSubject) {
		super(teSubject);

	}

	public View DrawChart(Context context, int graphID) throws JSONException {

		if (isEmpty)
			return null;
		// fetch settings
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		this.displayUnit = pref.getString("unit", "KB");
		this.chartFontSize = pref.getString("chartFontSize", "M");

		// Get JSON data
		JSONArray intervalArray = JSONResult.getJSONArray("intervals");
		double[] speed = new double[intervalArray.length()];
		String nodeValue;
		// looping through All nodes in interval array
		for (int i = 0; i < intervalArray.length(); i++) {
			JSONObject speedNode = intervalArray.getJSONObject(i)
					.getJSONObject("sum");
			// get the data we need from JSON file
			nodeValue = speedNode.getString("bits_per_second");
			// convert String to double, and keep two digits
			double temp;
			switch (displayUnit) {
			case "Kb":
				temp = ((double) Double.valueOf(nodeValue).longValue()) / 1024;
				break;
			case "KB":
				temp = ((double) Double.valueOf(nodeValue).longValue())
						/ (8 * 1024);
				break;
			case "Mb":
				temp = ((double) Double.valueOf(nodeValue).longValue())
						/ (1024 * 1024);
				break;
			case "MB":
				temp = ((double) Double.valueOf(nodeValue).longValue())
						/ (8 * 1024 * 1024);
				break;
			default:
				temp = ((double) Double.valueOf(nodeValue).longValue())
						/ (8 * 1024);
				break;
			}
			speed[i] = ((int) (temp * 100)) / 100.0;

		}

		// Show data on the user interface

		String[] mInterval = new String[intervalArray.length()];
		for (int i = 0; i < intervalArray.length(); i++) {

			mInterval[i] = "Time" + Integer.toString(i);
		}

		double maxValue = speed[0];

		for (int i = 1; i < speed.length; i++) {
			if (speed[i] > maxValue)
				maxValue = speed[i];
		}

		// Requires two parts to draw a chart - dataset and renderer
		// Creating an XYseries for speed
		XYSeries speedSeries = new XYSeries("Speed");
		// Adding data to the Series
		for (int i = 0; i < mInterval.length; i++) {
			speedSeries.add(i, speed[i]);
		}
		// Creating a dataset to hold the series
		XYMultipleSeriesDataset speedDataset = new XYMultipleSeriesDataset();

		// add speed data to the data set
		speedDataset.addSeries(speedSeries);

		// Write renderer use mulitpleRender for future extensions
		// TODO Animate the renderer, probably need to rewrite the render
		// methods

		// Creating a XYMultipleSeriesRenderer
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer
				.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Network Speed Chart");
		multiRenderer.setXTitle("Time Intervals");
		// TODO dynamic units needed, useful when need to work on setting etc.
		multiRenderer.setYTitle("Network Speed in " + displayUnit + " /s");
		/***
		 * Customizing graphs
		 */
		// setting text size of the title
		multiRenderer.setChartTitleTextSize(34);
		// setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(28);
		// setting text size of the graph label
		multiRenderer.setLabelsTextSize(28);
		// setting zoom buttons visibility
		multiRenderer.setZoomButtonsVisible(false);
		// setting pan inability which uses graph to move on both axis
		multiRenderer.setPanEnabled(true, false);
		// setting click false on graph
		multiRenderer.setClickEnabled(false);
		// setting zoom to false on both axis
		multiRenderer.setZoomEnabled(false, false);
		// setting lines to display on y axis
		multiRenderer.setShowGridY(false);
		// setting lines to display on x axis
		multiRenderer.setShowGridX(false);
		// setting legend to fit the screen size
		multiRenderer.setFitLegend(true);
		// setting displaying line on grid
		multiRenderer.setShowGrid(true);
		// setting zoom to false
		multiRenderer.setZoomEnabled(false);
		// setting external zoom functions to false
		multiRenderer.setExternalZoomEnabled(false);
		// setting displaying lines on graph to be formatted(like using
		// graphics)
		multiRenderer.setAntialiasing(true);
		// setting to in scroll to false
		multiRenderer.setInScroll(false);
		// setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		// setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		// setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.LEFT);
		// setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		// setting no of values to display in y axis
		multiRenderer.setYLabels(10);
		// setting y axis max value, Since i'm using static values inside the
		// graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMin(0);
		multiRenderer.setYAxisMax(maxValue+(maxValue/10) );
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-0.5);
		// setting max values to be display in x axis
		multiRenderer.setXAxisMax(5);
		// setting bar size or space between two bars
		multiRenderer.setBarSpacing(0.5);
		// Setting background color of the graph to transparent
		multiRenderer.setBackgroundColor(Color.GRAY);
		// Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(context.getResources().getColor(
				R.color.transparent_background));
		multiRenderer.setApplyBackgroundColor(true);

		// setting the margin size for the graph in the order top, left, bottom,
		// right
		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });
		multiRenderer.setPointSize(7f);

		for (int i = 0; i < mInterval.length; i++) {
			multiRenderer.addXTextLabel(i, mInterval[i]);
		}

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		XYSeriesRenderer speedRenderer = new XYSeriesRenderer();
		// customize the speedRenderer
		int chartValuesFontSize;
		switch (chartFontSize) {
		case "S":
			chartValuesFontSize=24;
			break;
		case "M":
			chartValuesFontSize=30;
			break;
		case "L":
			chartValuesFontSize=36;
			break;
		default:
			chartValuesFontSize=30;
			break;
		}
		speedRenderer.setChartValuesTextSize(chartValuesFontSize);
		speedRenderer.setFillPoints(true);
		speedRenderer.setLineWidth(2);
		speedRenderer.setDisplayChartValues(true);
		speedRenderer.setDisplayChartValuesDistance(10);
		speedRenderer.setPointStyle(PointStyle.SQUARE);

		// if it is area chart or just line chart
		if (graphID == 3) {
			FillOutsideLine fill = new FillOutsideLine(
					FillOutsideLine.Type.BOUNDS_ALL);
			fill.setColor(Color.GREEN);
			speedRenderer.addFillOutsideLine(fill);
		}
		multiRenderer.addSeriesRenderer(speedRenderer);

		switch (graphID) {
		case 0:
			return ChartFactory.getBarChartView(context, speedDataset,
					multiRenderer, Type.DEFAULT);
		case 1:
			return ChartFactory.getLineChartView(context, speedDataset,
					multiRenderer);
		case 2:
			return ChartFactory.getCubeLineChartView(context, speedDataset,
					multiRenderer, 0.25f);
		case 3:
			return ChartFactory.getCubeLineChartView(context, speedDataset,
					multiRenderer, 0.25f);
		default:
			return ChartFactory.getBarChartView(context, speedDataset,
					multiRenderer, Type.DEFAULT);
		}
	}
}
