package vigroid.iperf3ericsson;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class TestResult {
	//TODO could change to private, depends
	protected JSONObject JSONResult;
	private File JSONFile;
	//TODO units switch
	protected String unit;
	public Boolean isEmpty;
	
	private Context context;
	
	/*
	 * Variables for the test result
	 */
	private String connectionType;
	private String carrierName;
	private String IMEINumber;
	private String modelNumber;
	private String timestamp; // or long type
	private String longtitude;
	private String latitude; // or Location type
	private String ServerName;
	private String portNumber;
	private String averageSpeed;
	private String dataPayloadSize;
	private String pingTime;
	private String CpuUtilization;
	private String IpAddress;

	
	
	public TestResult(String defaultunit, String fileLocation, Context context) {
//		super();
		this.context = context;
		this.unit = defaultunit;
		this.JSONFile= new File(fileLocation);
		this.isEmpty=true;
		if(fetchJSONData()){
			this.isEmpty=false;}
		
		/*
		 * 
		 * Code goes here to set the above variables such as carrierName...IMEINumber...and so on
		 * 
		 */
		
		//Creates DetailResult and adds to DB if the variables have been populated (carrierName is used as an example)
//		if (carrierName!=null){
//		createDetailResult();
//		}
		
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
		
		dr.setServerName(ServerName);
		dr.setPortNumber(portNumber);
		//Setting timestamp
		dr.setTimestamp(timestamp);
		dr.setAverageSpeed(averageSpeed);
		
		LocationHelper lh = new LocationHelper(context);
		lh.getLocation();
		
		//Getting network connection type
		dr.setConnectionType(lh.getNetworkClassName(context));
//		dr.setConnectionType("4G");
		
		//Setting carrier name
		dr.setCarrierName(LocationHelper.CarrierName);
//		dr.setCarrierName("CARRIER NAME");
		
		//Setting IMEI number
		dr.setIMEINumber(LocationHelper.IMEINumber);
//		dr.setIMEINumber("IMEI NUMBER");
		
		//Setting device name
		dr.setModelNumber(LocationHelper.DEVICE_NAME);
//		dr.setModelNumber("MODEL NUMBER");
		
		//Setting location
		dr.setLatitude(Double.toString(lh.getLatitude()));
		dr.setLongtitude(Double.toString(lh.getLongitude()));
		
//		dr.setLatitude(30000);
//		dr.setLongtitude(20304);
		
		
		/*******SAMPLE JSON OUTPUT********
		04-23 20:40:34.635: W/IPERF(19052): JSON DATA: {"start":{"connected":[{"socket":45,"local_host":"192.168.1.71","local_port":42901,
		"remote_host":"78.72.178.64","remote_port":5201}],"version":"iperf 3.0a11","system_info":"Linux localhost 3.4.0-3846539 #1 SMP PREEMPT Wed Mar 18 13:27:28 KST 2015 armv7l",
		"timestamp":{"time":"Fri, 24 Apr 2015 01:40:26 GMT","timesecs":1429839626},"connecting_to":{"host":"coneye.myqnapcloud.com","port":5201},"cookie":"localhost.1429839625.637065.5d17fe6e","tcp_mss_default":1448,"test_start":{"protocol":"TCP","num_streams":1,"blksize":131072,"omit":0,"duration":7,"bytes":0,"blocks":0,"reverse":1}},"intervals":[{"streams":[{"socket":45,"start":0,"end":1.00027,"seconds":1.00027,"bytes":386616,"bits_per_second":3092090,"omitted":false}],
		"sum":{"start":0,"end":1.00027,"seconds":1.00027,"bytes":386616,"bits_per_second":3092090,"omitted":false}},
		{"streams":[{"socket":45,"start":1.00027,"end":2.00013,"seconds":0.99986,"bytes":1542872,"bits_per_second":12344700,
		"omitted":false}],
		"sum":{"start":1.00027,"end":2.00013,"seconds":0.99986,"bytes":1542872,"bits_per_second":12344700,"omitted":false}},
		{"streams":[{"socket":45,"start":2.00013,"end":3.00011,"seconds":0.999983,"bytes":2018512,"bits_per_second":16148400,"omitted":false}],
		"sum":{"start":2.00013,"end":3.00011,"seconds":0.999983,"bytes":2018512,"bits_per_second":16148400,"omitted":false}},
		{"streams":[{"socket":45,"start":3.00011,"end":4.00027,"seconds":1.00015,"bytes":3013288,"bits_per_second":24102600,"omitted":false}],
		"sum":{"start":3.00011,"end":4.00027,"seconds":1.00015,"bytes":3013288,"bits_per_second":24102600,"omitted":false}},
		{"streams":[{"socket":45,"start":4.00027,"end":5.0001,"seconds":0.999833,"bytes":3249368,"bits_per_second":25999300,"omitted":false}],
		"sum":{"start":4.00027,"end":5.0001,"seconds":0.999833,"bytes":3249368,"bits_per_second":25999300,"omitted":false}},
		{"streams":[{"socket":45,"start":5.0001,"end":6.00012,"seconds":1.00002,"bytes":3149400,"bits_per_second":25194800,"omitted":false}],
		"sum":{"start":5.0001,"end":6.00012,"seconds":1.00002,"bytes":3149400,"bits_per_second":25194800,"omitted":false}},
		{"streams":[{"socket":45,"start":6.00012,"end":7.00016,"seconds":1.00004,"bytes":3492576,"bits_per_second":27939500,"omitted":false}],
		"sum":{"start":6.00012,"end":7.00016,"seconds":1.00004,"bytes":3492576,"bits_per_second":27939500,"omitted":false}}],"end":
		{"streams":[{"sender":{"socket":45,"start":0,"end":7.00016,"seconds":7.00016,"bytes":17657720,"bits_per_second":20179800,"retransmits":17,"max_snd_cwnd":0,"max_rtt":0,"min_rtt":0,"mean_rtt":0},
		"receiver":{"socket":45,"start":0,"end":7.00016,"seconds":7.00016,"bytes":17507992,"bits_per_second":20008700}}],
		"sum_sent":{"start":0,"end":7.00016,"seconds":7.00016,"bytes":17657720,"bits_per_second":20179800,"retransmits":17},
		"sum_received":{"start":0,"end":7.00016,"seconds":7.00016,"bytes":17507992,"bits_per_second":20008700},
		"cpu_utilization_percent":{"host_total":3.76416,"host_user":0.626119,"host_system":3.25582,"remote_total":0.0623147,
		"remote_user":0.00553909,"remote_system":0.0567756}}}
		Log.w("IPERF","JSON DATA: "+JSONResult.toString());
		*/
		
		///Adding data from iperf test
//		try {
//			dr.setServerName(jo.getString(ServerName));
//			dr.setDataPayloadSize(jo.getInt(name));
//			dr.setPingTime(jo.optLong(pingTime));
//			dr.setServerName("SERVER NAME HERE");
//			dr.setDataPayloadSize(25);
//			dr.setPingTime(30202020);
//			dr.setCpuUtilization(30040302);
//			
//			dr.setIpAddress(JSONResult.getString("remote_host"));
//			
//			dr.setPortNumber("3040");
//			//String nameValue = JSONResult.getJSONObject("receiver").getString("bits_per_second");
//			//String nameValue = speedObject.getString("bits_per_second");
//			Log.w("IPERF","SPEED ="+JSONResult.getJSONObject("receiver").getString("bits_per_second"));
//			dr.setAverageSpeed(Double.parseDouble(JSONResult.getJSONObject("receiver").getString("bits_per_second")));
//			}catch (JSONException e) {
//				// TODO Auto-generated catch block
//				Log.w("IPERF","JSON ERRORED OUT");
//			}

		dr.setContext(context);
		dr.addToDB();
		
	}
	
	
	private void parseJSONforTestResult(){
		
		//JSONObject jsonObject=new JSONObject(JSONResult);

        try {
        	
        	
//            JSONArray intervalArray  = JSONResult.getJSONArray("start");
//            double[] speed = new double[intervalArray.length()];
//            
//            int x = 0;
//            while (x<intervalArray.length()-1){
//            	Log.w("IPERF","JSON = "+intervalArray.get(x).toString());
//            	x++;
//            }
            String nodeValue;
            //looping through All nodes in interval array
            //	JSONObject speedNode = intervalArray.getJSONObject(i).getJSONObject("sum");
        	
        	
        	
			JSONObject jsonStart=JSONResult.getJSONObject("start");
			Log.w("IPERF","Start = "+jsonStart.toString());
			
			Log.w("IPERF","Host = "+ jsonStart.getString("connecting_to"));
			JSONObject jsonConnectedStart = jsonStart.getJSONObject("connecting_to");
			Log.w("IPERF","Host = "+ jsonConnectedStart.getString("host"));
			this.ServerName=jsonConnectedStart.getString("host");
			this.portNumber=jsonConnectedStart.getString("port");
			
			JSONObject jsonConnectedTime = jsonStart.getJSONObject("timestamp");
			this.timestamp = jsonConnectedTime.getString("time");
			Log.w("IPERF","TIMESTAMP + "+jsonConnectedTime.getString("timesecs"));
			
			//JSONObject jsonIntervals=JSONResult.getJSONObject("intervals");
			JSONObject jsonEnd=JSONResult.getJSONObject("end");
			JSONObject jsonTotal = jsonEnd.getJSONObject("sum_received");
			
			double temp=((double) Double.valueOf(jsonTotal.getString("bits_per_second")).longValue())/8192;
			this.averageSpeed= Double.toString(temp);
			
			
			Log.w("IPERF","SPEED = "+ jsonTotal.getString("bits_per_second"));
			
			
			
			Log.w("IPERF","End = "+jsonEnd.toString());
			Log.w("IPERF","JSON PARSED");
			
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.w("IPERF","JSON ERROR");
			Log.w("IPERF","Error = "+e.toString());
		}
		
		
	}
	
	public JSONObject getJSONResult() {
		return JSONResult;
	}
	
	
}
