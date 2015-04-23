package vigroid.iperf3ericsson;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

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
	private long timestamp; // or long type
	private double longtitude;
	private double latitude; // or Location type
	private String ServerName;
	private String portNumber;
	private double averageSpeed;
	private double dataPayloadSize;
	private long pingTime;
	private double CpuUtilization;
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
            	Log.w("IPERF","JSON string = "+jsonStr);
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
		
		
		
		//Setting timestamp
		dr.setTimestamp(System.currentTimeMillis());
		
		LocationHelper lh = new LocationHelper(context);
//		lh.getLocation();
		
		//Getting network connection type
//		dr.setConnectionType(lh.getNetworkClassName(context));
		dr.setConnectionType("4G");
		
		//Setting carrier name
//		dr.setCarrierName(LocationHelper.CarrierName);
		dr.setCarrierName("CARRIER NAME");
		
		//Setting IMEI number
//		dr.setIMEINumber(LocationHelper.IMEINumber);
		dr.setIMEINumber("IMEI NUMBER");
		
		//Setting device name
//		dr.setModelNumber(LocationHelper.DEVICE_NAME);
		dr.setModelNumber("MODEL NUMBER");
		
		//Setting location
//		dr.setLatitude(lh.getLatitude());
//		dr.setLongtitude(lh.getLongitude());
		
		dr.setLatitude(30000);
		dr.setLongtitude(20304);
		
		///Adding data from iperf test
//			dr.setServerName(jo.getString(ServerName));
//			dr.setDataPayloadSize(jo.getInt(name));
//			dr.setPingTime(jo.optLong(pingTime));
			dr.setServerName("SERVER NAME HERE");
			dr.setDataPayloadSize(25);
			dr.setPingTime(30202020);
			dr.setCpuUtilization(30040302);
			dr.setIpAddress("THIS IS AN IP ADDRESS");
			dr.setPortNumber("3040");
			dr.setAverageSpeed(4000);

		dr.setContext(context);
		dr.addToDB();
		
	}
	
	
	public JSONObject getJSONResult() {
		return JSONResult;
	}
	
	
}
