package vigroid.iperf3ericsson;

import java.security.Timestamp;

import android.content.Context;
import android.view.View;

public class DetailResult {

	private Context context;
	
	/*
	 * Test result variables
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

	
	/**
	 * Constructor for DetailResult object.
	 * This holds the data for a specific iperf test.
	 */

	public DetailResult(Context context, String connectionType, String carrierName,
			String iMEINumber, String modelNumber, long timestamp,
			double longtitude, double latitude, String serverName,
			String portNumber, double averageSpeed, double dataPayloadSize,
			long pingTime, double cpuUtilization, String ipAddress) {
		super();
		this.context = context;
		this.connectionType = connectionType;
		this.carrierName = carrierName;
		this.IMEINumber = iMEINumber;
		this.modelNumber = modelNumber;
		this.timestamp = timestamp;
		this.longtitude = longtitude;
		this.latitude = latitude;
		this.ServerName = serverName;
		this.portNumber = portNumber;
		this.averageSpeed = averageSpeed;
		this.dataPayloadSize = dataPayloadSize;
		this.pingTime = pingTime;
		this.CpuUtilization = cpuUtilization;
		this.IpAddress = ipAddress;
	}
	
	
	/**
	 * Adds this object to the database
	 * TODO add handling to prevent duplicate records from being entered
	 */
	public void addToDB(){
		IPerfDB db = new IPerfDB(context);
		db.insertTestRecord(this);		
	}
	
	
	//TODO add upload method which upload database entries to Ericsson server, when click the upload button on DetailActivity
	public void DBUploadEntries(){
		
	}
	
	//show the detail when DetailActivity is created, invoke this method in DetailActivity->onCreate
	public View ShowDetail(){
		
		return null;
	}
	//TODO (write)insert the element to locale database
	public void DBInsertEntrie(){
		
	}
	
	//TODO (read)read the element with the specific timestamp from locale database, choose the latest one  if no Internet connection available
	public DetailResult DBFetchEntrie(Timestamp timestamp){
		DetailResult currentResult = null;
		
		return currentResult;
	}
	
	//Getters and Setter
	
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

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
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

	public double getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public double getDataPayloadSize() {
		return dataPayloadSize;
	}

	public void setDataPayloadSize(double dataPayloadSize) {
		this.dataPayloadSize = dataPayloadSize;
	}

	public long getPingTime() {
		return pingTime;
	}

	public void setPingTime(long pingTime) {
		this.pingTime = pingTime;
	}

	public double getCpuUtilization() {
		return CpuUtilization;
	}

	public void setCpuUtilization(double cpuUtilization) {
		CpuUtilization = cpuUtilization;
	}

	public String getIpAddress() {
		return IpAddress;
	}

	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}

	
	
	
	
}

