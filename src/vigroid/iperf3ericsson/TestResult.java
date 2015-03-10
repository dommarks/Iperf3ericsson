package vigroid.iperf3ericsson;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONObject;

public class TestResult {
	protected Boolean isResultSDcard;
	protected JSONObject JSONResult;
	private File JsonFile;
	//TODO units switch
	protected String unit;
	
	public TestResult(String defaultunit) {
		super();
		this.unit = defaultunit;
		this.isResultSDcard=false;
		this.JsonFile= new File("sdcard/iPerfResult.json");
		CheckIfInSDcard();
	}

	public void FetchJSONData(){
		if(isResultSDcard)
		// Read JSON file from SDcard and Display it
		try	{
			//TODO consider take a variable instead of a fixed path
            FileInputStream stream = new FileInputStream(JsonFile);
            String jsonStr = null;
		
            try {
            	FileChannel fc = stream.getChannel();
            	MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            	jsonStr = Charset.defaultCharset().decode(bb).toString();
            }
            finally {
            	stream.close();
            }
            
            this.JSONResult = new JSONObject(jsonStr);

          
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void CheckIfInSDcard(){
		if(this.JsonFile!=null){
			isResultSDcard=true;
		}
		else
			isResultSDcard=false;
		
	}
	
	
}
