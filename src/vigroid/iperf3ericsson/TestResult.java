package vigroid.iperf3ericsson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class TestResult {
	//TODO could change to private, depends
	protected JSONObject JSONResult;
	private File JSONFile;
	public Boolean isEmpty;
	private TestSubject teSubject;
	
	private Context context;
	
	private String timestamp; // or long type
	private String ServerName;
	private String portNumber;
	private String averageSpeed;
	private String cpuUtiliztation;
	private String pingTime;
	private String payloadSize;
	private String ipAddress;
	
	public TestResult(TestSubject teSubject) {
		super();
		this.teSubject=teSubject;
		this.JSONFile= (this.teSubject.isResultEmpty)?null:new File("sdcard/iPerfResult.json");
		this.isEmpty=true;
		if(fetchJSONData())
			this.isEmpty=false;
		
	}
	
	
	public TestResult(String defaultunit, String fileLocation, Context context) {
//		super();
		this.context = context;
		//this.unit = defaultunit;
		this.JSONFile= new File(fileLocation);
		this.isEmpty=true;
		if(fetchJSONData()){
			this.isEmpty=false;}
		
	}


	public Boolean fetchJSONData(){
		Log.w("IPERF","fetchJASONData()");
		try	{
			//TODO consider take a variable instead of a fixed path
            FileInputStream stream = new FileInputStream(JSONFile);
            String jsonStr = null;
		
            try {
            	FileChannel fc = stream.getChannel();
            	MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            	jsonStr = Charset.defaultCharset().decode(bb).toString();
            }
            finally {
            	stream.close();
            }
            if (jsonStr.length()>1) {
            	this.JSONResult = new JSONObject(jsonStr);
            	//Log.w("IPERF","JSON string = "+jsonStr);
            	return true;
			}
           
			else
				return false;
          
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
		
	}

	/**
	 * Creates DetailResult object from JSON object, adds to database
	 * @param jobj
	 */
	public void createDetailResult(){
		TestResultDetails dr = new TestResultDetails();
		parseJSONforTestResult();
		
		dr.setContext(context);
		dr.setServerName(ServerName);
		dr.setPortNumber(portNumber);
		dr.setTimestamp(timestamp);
		dr.setAverageSpeed(averageSpeed);
		dr.setPingTime(pingTime);
		dr.setCpuUtilization(cpuUtiliztation);
		dr.setDataPayloadSize(payloadSize);
		dr.setIpAddress(ipAddress);
		
		LocationHelper lh = new LocationHelper(context);
		
		dr.setConnectionType(lh.getNetworkClassName(context));
		dr.setCarrierName(LocationHelper.CarrierName);
		dr.setIMEINumber(LocationHelper.IMEINumber);
		dr.setModelNumber(LocationHelper.DEVICE_NAME);
		
		dr.setTestID(Long.toString(System.currentTimeMillis())+"."+dr.getIMEINumber());
		
		//Setting location
		dr.setLatitude(lh.getLatitude());
		dr.setLongtitude(lh.getLongitude());
		
		//Add to database
		dr.addToDB();
		
	}
	
	
	private void parseJSONforTestResult(){

        try {
        	
        	JSONObject jsonStart=JSONResult.getJSONObject("start");
			
			JSONObject jsonConnectedStart = jsonStart.getJSONObject("connecting_to");
			this.ServerName=jsonConnectedStart.getString("host");
			this.portNumber=jsonConnectedStart.getString("port");
			this.pingTime = ping(this.ServerName); 
			
			JSONArray jsonConnected = jsonStart.getJSONArray("connected");
			JSONObject jsonHosts = jsonConnected.getJSONObject(0);
			this.ipAddress = jsonHosts.getString("local_host");
			
			JSONObject jsonConnectedTime = jsonStart.getJSONObject("timestamp");
			this.timestamp = jsonConnectedTime.getString("time");
			
			//JSONObject jsonIntervals=JSONResult.getJSONObject("intervals");
			JSONObject jsonEnd=JSONResult.getJSONObject("end");
			JSONObject jsonTotal = jsonEnd.getJSONObject("sum_received");
			this.payloadSize = jsonTotal.getString("bytes");
			this.payloadSize = jsonTotal.getString("bytes");
			
			double temp=((double) Double.valueOf(jsonTotal.getString("bits_per_second")).longValue())/8192;
			this.averageSpeed= Double.toString(temp);
			
			JSONObject jsonCPU = jsonEnd.getJSONObject("cpu_utilization_percent");
			this.cpuUtiliztation = jsonCPU.getString("host_total");
	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.w("IPERF","JSON Error = "+e.toString());
		}
		
		
	}
	
	public JSONObject getJSONResult() {
		return JSONResult;
	}
	
	private String ping(String url) {
	    	long startTime=0;
	    	long endTime = 0;
	    	String result = "";
	        try {
	        	startTime = System.currentTimeMillis();
				Runtime.getRuntime().exec("/system/bin/ping -c 1 " + url);
				endTime = System.currentTimeMillis();
				result = Long.toString(endTime-startTime);
			} catch (IOException e) {
				result = "PING ERROR";
			}
	        
	        return result;
	}
	
	
}
