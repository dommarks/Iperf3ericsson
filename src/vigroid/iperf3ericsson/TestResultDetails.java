package vigroid.iperf3ericsson;

import java.security.Timestamp;

import android.content.Context;
import android.view.View;

public class TestResultDetails {

	private Context context;
	
	/*
	 * Test result variables
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
	private String testID;

	
	/**
	 * Constructor for DetailResult object.
	 * This holds the data for a specific iperf test.
	 */

	public TestResultDetails(Context context, String connectionType, String carrierName,
			String iMEINumber, String modelNumber, String timestamp,
			String longtitude, String latitude, String serverName,
			String portNumber, String averageSpeed, String dataPayloadSize,
			String pingTime, String cpuUtilization, String ipAddress,String testID) {
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
		this.testID = testID;
	}
	
	
	public TestResultDetails() {
		// TODO Auto-generated constructor stub
	}

	public TestResultDetails(Context context){
		this.context = context;
		
	}

	/**
	 * Adds this object to the database
	 * TODO add handling to prevent duplicate records from being entered
	 */
	public void addToDB(){
		IPerfDBHelper db = new IPerfDBHelper(context);	
		db.insertRecords(this);
		db.close();
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
	
	public TestResultDetails DBFetchEntrie(Timestamp timestamp){
		TestResultDetails currentResult = null;
		return currentResult;
	}
	
	//Getters and Setter
	
	public void setContext(Context context){
		this.context = context;
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
		return CpuUtilization;
	}

	public void setCpuUtilization(String cpuUtilization) {
		CpuUtilization = cpuUtilization;
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
	public void setTestID(String testId){
		testID = testId;
	}

	@Override
	public String toString(){
		return "TestID="+testID+" Timestamp="+timestamp + " ConnectionType= "+connectionType+" Carriername= " + carrierName
				+" IMEINumber=" + IMEINumber+  " modelNumber=" + modelNumber+ " Longitude="+
				longtitude+ " Latitude="+ latitude+ " Servername="+ ServerName+
				" PortNumber=" +portNumber+ " Average speed="+ averageSpeed+ " DataPayloadSize="+ dataPayloadSize+
				" Ping="+ pingTime+ " CPU Util="+ CpuUtilization+ "IP Address="+ IpAddress;
		
	}
	
	public String toStringFormatted(){
		return "TestID= "+testID+"\nTimestamp = "+timestamp + "\nConnectionType = "+connectionType+"\nCarrier Name = " + carrierName
				+"\nIMEI Number = " + IMEINumber+  "\nModel Number = " + modelNumber+ "\nLongitude = "+
				longtitude+ "\nLatitude="+ latitude+ "\nServer Name="+ ServerName+
				"\nPort Number =" +portNumber+ "\nAverage speed = "+ averageSpeed+ "\nDataPayloadSize = "+ dataPayloadSize+
				"\nPing ="+ pingTime+ "\nCPU Utilization = "+ CpuUtilization+ "\nIP Address = "+ IpAddress;
		
	}



	
	
	
}

