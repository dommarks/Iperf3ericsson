package vigroid.iperf3ericsson;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONObject;

public class TestResult {
	//TODO could change to private, depends
	protected JSONObject JSONResult;
	private File JSONFile;
	//TODO units switch
	protected String unit;
	public Boolean isEmpty;
	
	
	
	public TestResult(String defaultunit, String fileLocation) {
		super();
		this.unit = defaultunit;
		this.JSONFile= new File(fileLocation);
		this.isEmpty=true;
		if(fetchJSONData())
			this.isEmpty=false;
		
	}

	public Boolean fetchJSONData(){
		if(this.JSONFile==null)
			return false;
		// Read JSON file from SDcard and Display it
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
            
            this.JSONResult = new JSONObject(jsonStr);

          
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
		
	}

	public JSONObject getJSONResult() {
		return JSONResult;
	}
	
	
}
