package vigroid.iperf3ericsson;

import java.util.ArrayList;

/**
 * 
 * Helper class for display of previous tests. Holds object containing an array of testID's, and a matching array 
 *of human-readable test timestamps and speeds.
 * the testID of ArrayofTests[x] = testIDArray[x] 
 */
public class PreviousTestLists {

	public ArrayList<String> testIDArray;
	public ArrayList<String> arrayOfTests = new ArrayList<String>();

	PreviousTestLists(ArrayList<String> trdList,	ArrayList<String> testArray){
		testIDArray= trdList;
		arrayOfTests = testArray;
	}
	
	public ArrayList<String> getTestIDArray(){
		return testIDArray;
	}
	
	public ArrayList<String> getTestNameandSpeedArray(){
		return arrayOfTests;
	}
	
}