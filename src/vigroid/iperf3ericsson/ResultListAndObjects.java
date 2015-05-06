package vigroid.iperf3ericsson;

import java.util.ArrayList;
import java.util.List;

public class ResultListAndObjects {

	public List<TestResultDetails> trdArray;
	public ArrayList<String> ArrayofTests = new ArrayList<String>();

	ResultListAndObjects(List<TestResultDetails> trdList,	ArrayList<String> testArray){
		trdArray= trdList;
		ArrayofTests = testArray;
	}
	
	public List<TestResultDetails> getTestResultObjectArray(){
		
		return trdArray;
	}
	
	public ArrayList<String> getTestNameandSpeedArray(){
		return ArrayofTests;
	}
	
}
