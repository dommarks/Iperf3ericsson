package vigroid.iperf3ericsson;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONObject;

import android.util.Log;

public class TestResult {
	
	private final static String TAG = "JSONfectingIPerfEricsson";
	
	protected JSONObject JSONResult;
	private File JSONFile;
	public Boolean isEmpty;
	private TestSubject teSubject;

	public TestResult(TestSubject teSubject) {
		super();
		this.teSubject = teSubject;
		this.JSONFile = (this.teSubject.isResultEmpty) ? null : new File(
				"sdcard/iPerfResult.json");
		this.isEmpty = true;
		if (fetchJSONObject())
			this.isEmpty = false;

	}
	
	public TestResult( String fileLocation) {
		super();
		this.JSONFile= new File(fileLocation);
		this.isEmpty=true;
		if(fetchJSONObject())
			this.isEmpty=false;
	}
	
	public TestResult() {
		super();
	}

	//fetch JSON array from JOSN file
	public Boolean fetchJSONObject() {
		if (this.JSONFile == null)
			return false;
		try {
			// TODO consider take a variable instead of a fixed path
			FileInputStream stream = new FileInputStream(JSONFile);
			String jsonStr = null;

			try {
				FileChannel fc = stream.getChannel();
				MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
						fc.size());

				jsonStr = Charset.defaultCharset().decode(bb).toString();
			} finally {
				stream.close();
			}
			if (jsonStr.length() > 1) {
				this.JSONResult = new JSONObject(jsonStr);
				return true;
			}

			else{
				Log.d(TAG,"JOSN file length is too short, check the file again");
			}return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

}
