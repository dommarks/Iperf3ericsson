package vigroid.iperf3ericsson;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/* Gets device location.
 * @TODO add handling for GPS turned off, network location, and delayed updates.
 */
public class LocationHelper extends Activity{
	
	//Initialized on class start
	private static LocationManager lm; 
	private static Location location;
	private static double longitude= 0;
	private static double latitude = 0;
	private static long locationTimestamp = 0;
	
	
	public LocationHelper(Context context){
		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);	
	}
	
	public void getLocation(){
	longitude = location.getLongitude();
	latitude = location.getLatitude();
	//Timestamp is to determine if the location is stale or not
	locationTimestamp = System.currentTimeMillis();
	}
	
	public double getLatitude(){
		return latitude;
	}
	public double getLongitude(){
		return longitude;
	}
	public double getLocationTimestamp(){
		return locationTimestamp;
	}
	
	
}
