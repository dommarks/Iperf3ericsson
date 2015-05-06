package vigroid.iperf3ericsson;


import java.io.File;
import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class TestSubject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final int MAX_TEST_DURATION = 100;
	
	public boolean isEmpty;
	public boolean isInputInvalid;
	public boolean isResultEmpty;
	
	//iPerf test configuration
	private String serverAddress;
	private int port;
	private int duration;
	private int reverse;
	
	//GPS related
	private boolean isGetLocation;
	public String[] location = new String[2];
	
	//others
	private Context context;
	private File testResultJson;
	
	public TestSubject(Context context) {
		//Test constructor
		this.context=context;
		this.isEmpty=true;
		this.isInputInvalid=false;
		this.isResultEmpty=true;
		initTest();
	}
	
	//get our configurations from setting
	private void initTest(){
		//load from prefiperf.XML
		SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(context);
		this.serverAddress = pref.getString("serverAddress", "coneye.myqnapcloud.com");
		try {
			this.port = Integer.parseInt(pref.getString("port", "5201"));
		} catch (NumberFormatException e) {
			
			this.port=5201;
			isInputInvalid=true;
		}		
		
		if(this.port<0 || this.port>65536){
			this.port=5201;
			isInputInvalid=true;
			
		}
		
		try {
			this.duration = Integer.parseInt(pref.getString("duration", "5"));
		} catch (NumberFormatException e) {
			this.duration=5;
			isInputInvalid=true;
		}
		
		if(this.duration<=0 || this.duration>MAX_TEST_DURATION){
			this.duration=5;
			
		}
		
		String testType = pref.getString("testType", "download");
		
		this.reverse=(testType.matches("download")?1:0);

		this.isGetLocation = pref.getBoolean("isGetLocation", true);		
		
		isEmpty=false;
	}
	
	public String runTest() {
		
		String testState = (isInputInvalid)?(runIPerfTest(serverAddress, port, duration, reverse)+" settings have invalid value(s)"):runIPerfTest(serverAddress, port, duration, reverse);
		isResultEmpty = (testState.contains("Successfully"))?false:true;
		return testState;
		
	}
	
	private native String runIPerfTest(String serverAddress, int port, int duration, int reverse);
	
	static {
		System.loadLibrary("ndklib");
	}
}
