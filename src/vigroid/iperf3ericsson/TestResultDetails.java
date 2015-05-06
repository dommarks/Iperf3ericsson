package vigroid.iperf3ericsson;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class TestResultDetails extends TestResult {

	// database related
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
	private String cpuUtilization;
	private String IpAddress;
	private String testID;

	// UI related
	private Context context;

	public TestResultDetails(String fileLocation, Context context) {
		super(fileLocation);
		this.context = context;
		initialResultDetails();
	}
	
	public TestResultDetails(){
		
	}

	private void initialResultDetails() {
		// TODO Auto-generated method stub
		parseJSONforTestResult();
		cellphoneInfoForTestResult();
		LocationHelper lh= new LocationHelper(context);
		this.latitude=lh.getLatitude();
		this.longtitude= lh.getLongitude();
		this.testID= Long.toString(System.currentTimeMillis())+"-"+this.IMEINumber;
	}

	public void addToDB() {
		IPerfDBHelper db = new IPerfDBHelper(context);
		db.insertRecords(this);
		db.close();
	}

	private void parseJSONforTestResult() {

		try {

			JSONObject jsonStart = JSONResult.getJSONObject("start");

			JSONObject jsonConnectedStart = jsonStart
					.getJSONObject("connecting_to");
			this.ServerName = jsonConnectedStart.getString("host");
			this.portNumber = jsonConnectedStart.getString("port");
			this.pingTime = ping(this.ServerName);

			JSONArray jsonConnected = jsonStart.getJSONArray("connected");
			JSONObject jsonHosts = jsonConnected.getJSONObject(0);
			this.IpAddress = jsonHosts.getString("local_host");

			JSONObject jsonConnectedTime = jsonStart.getJSONObject("timestamp");
			this.timestamp = jsonConnectedTime.getString("time");

			// JSONObject jsonIntervals=JSONResult.getJSONObject("intervals");
			JSONObject jsonEnd = JSONResult.getJSONObject("end");
			JSONObject jsonTotal = jsonEnd.getJSONObject("sum_received");
			this.dataPayloadSize = jsonTotal.getString("bytes");

			double temp = ((double) Double.valueOf(
					jsonTotal.getString("bits_per_second")).longValue()) / 8192;
			this.averageSpeed = Double.toString(temp);

			JSONObject jsonCPU = jsonEnd
					.getJSONObject("cpu_utilization_percent");
			this.cpuUtilization = jsonCPU.getString("host_total");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.w("IPERF", "JSON Error = " + e.toString());
		}

	}

	private String ping(String url) {
		long startTime = 0;
		long endTime = 0;
		String result = "";
		try {
			startTime = System.currentTimeMillis();
			Runtime.getRuntime().exec("/system/bin/ping -c 1 " + url);
			endTime = System.currentTimeMillis();
			result = Long.toString(endTime - startTime);
		} catch (IOException e) {
			result = "PING ERROR";
		}

		return result;
	}

	private void cellphoneInfoForTestResult(){
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		this.carrierName = manager.getNetworkOperatorName();
		this.IMEINumber = manager.getDeviceId();
		this.modelNumber = generateDeviceName();
		this.connectionType = getNetworkClassName(context);
	}
	
	private String getNetworkClassName(Context context){
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Log.w("IPERF","NETWORK INFO = "+connManager.getActiveNetworkInfo().getSubtypeName());
		
		if (mWifi.isConnected()){
			WifiManager WifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			return connManager.getActiveNetworkInfo().getTypeName()+" - "+ WifiManager.getConnectionInfo().getLinkSpeed()+" - "+connManager.getActiveNetworkInfo().getExtraInfo();
		}
		
		else {
			return connManager.getActiveNetworkInfo().getTypeName()+" - "+connManager.getActiveNetworkInfo().getSubtypeName()+" - "+connManager.getActiveNetworkInfo().getExtraInfo();
		}
	}
	
	private String generateDeviceName() {
	    final String manufacturer = Build.MANUFACTURER;
	    final String model = Build.MODEL;
	    if (model.startsWith(manufacturer)) {
	        return capitalize(model);
	    }
	    if (manufacturer.equalsIgnoreCase("HTC")) {
	        // make sure "HTC" is fully capitalized.
	        return "HTC " + model;
	    }
	    return capitalize(manufacturer) + " " + model;
	}
	
	private String capitalize(String str) {
	    if (TextUtils.isEmpty(str)) {
	        return str;
	    }
	    final char[] arr = str.toCharArray();
	    boolean capitalizeNext = true;
	    String phrase = "";
	    for (final char c : arr) {
	        if (capitalizeNext && Character.isLetter(c)) {
	            phrase += Character.toUpperCase(c);
	            capitalizeNext = false;
	            continue;
	        } else if (Character.isWhitespace(c)) {
	            capitalizeNext = true;
	        }
	        phrase += c;
	    }
	    return phrase;
	}
	public String toStringFormatted(){
		return "TestID= "+testID+"\nTimestamp = "+timestamp + "\nConnectionType = "+connectionType+"\nCarrier Name = " + carrierName
				+"\nIMEI Number = " + IMEINumber+  "\nModel Number = " + modelNumber+ "\nLongitude = "+
				longtitude+ "\nLatitude="+ latitude+ "\nServer Name="+ ServerName+
				"\nPort Number =" +portNumber+ "\nAverage speed = "+ averageSpeed+ "\nDataPayloadSize = "+ dataPayloadSize+
				"\nPing ="+ pingTime+ "\nCPU Utilization = "+ cpuUtilization+ "\nIP Address = "+ IpAddress;
		
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getIMEINumber() {
		return IMEINumber;
	}

	public void setIMEINumber(String iMEINumber) {
		IMEINumber = iMEINumber;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getServerName() {
		return ServerName;
	}

	public void setServerName(String serverName) {
		ServerName = serverName;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(String averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public String getDataPayloadSize() {
		return dataPayloadSize;
	}

	public void setDataPayloadSize(String dataPayloadSize) {
		this.dataPayloadSize = dataPayloadSize;
	}

	public String getPingTime() {
		return pingTime;
	}

	public void setPingTime(String pingTime) {
		this.pingTime = pingTime;
	}

	public String getCpuUtilization() {
		return cpuUtilization;
	}

	public void setCpuUtilization(String cpuUtilization) {
		this.cpuUtilization = cpuUtilization;
	}

	public String getIpAddress() {
		return IpAddress;
	}

	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}

	public String getTestID() {
		return testID;
	}

	public void setTestID(String testID) {
		this.testID = testID;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
}
