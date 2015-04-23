package vigroid.iperf3ericsson;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONObject;

import android.content.Context;
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
		if (carrierName!=null){
		createDetailResult();
		}
		
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
	 * Creates DetailResult object from JSON object
	 * @param jobj
	 */
	public void createDetailResult(){
		
		///Code to create a DetailResult object and add it to the database
		TestResultDetails dr = new TestResultDetails(context, connectionType, carrierName,IMEINumber, modelNumber, timestamp,
				longtitude, latitude,ServerName,portNumber, averageSpeed, dataPayloadSize,pingTime, CpuUtilization, IpAddress);
		
		dr.addToDB();
		
	}
	public JSONObject getJSONResult() {
		return JSONResult;
	}
	
	
}
