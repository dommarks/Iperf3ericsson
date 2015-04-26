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
	public Boolean isEmpty;
	private TestSubject teSubject;
	
	
	public TestResult(TestSubject teSubject) {
		super();
		this.teSubject=teSubject;
		this.JSONFile= (this.teSubject.isResultEmpty)?null:new File("sdcard/iPerfResult.json");
		this.isEmpty=true;
		if(fetchJSONData())
			this.isEmpty=false;
		
	}

	public Boolean fetchJSONData(){
		if(this.JSONFile==null)
			return false;
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
	
	
}
