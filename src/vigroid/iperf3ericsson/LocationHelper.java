package vigroid.iperf3ericsson;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/* Gets device location.
 * @TODO add handling for GPS turned off, network location, and delayed updates.
 */
public class LocationHelper {

	// Initialized on class start
	public static double longitude;
	public static double latitude;
	private IperfLocationListener mlocListener;
	private LocationManager mlocManager;
	private static long locationTimestamp = 0;
	private boolean isListening;
	private Context context;
	public static String IMEINumber;
	public static String CarrierName;
	public static String DEVICE_NAME;

	public LocationHelper(Context context) {
		longitude = 0;
		latitude = 0;
		this.context = context;
		// LocationManager mlocManager=null;
		// LocationListener mlocListener;
		mlocManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new IperfLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0,
				mlocListener);
		isListening = true;
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		CarrierName = manager.getNetworkOperatorName();
		IMEINumber = manager.getDeviceId();
		DEVICE_NAME = generateDeviceName();

		if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			if (IperfLocationListener.latitude > 0) {
				Toast.makeText(context, "fetching", Toast.LENGTH_LONG).show();
				latitude = IperfLocationListener.latitude;
				longitude = IperfLocationListener.longitude;
			} else {
				latitude = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
				longitude = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
			}
		} else {
			// et_field_name.setText("GPS is not turned on...");
			latitude = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
			longitude = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
		}
	}
	
	public void startLocationListening(){
		if (!isListening){
			mlocManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	        mlocListener = new IperfLocationListener();
	       mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	       isListening = true;
		}
	}
	
	public void stopLocationListening(){
		if(isListening){
		mlocManager.removeUpdates(mlocListener);
		mlocManager = null;
		mlocListener = null;
		isListening = false;}
	}
	
	public String getLatitude(){
		return Double.toString(latitude);
	}
	public String getLongitude(){
		return Double.toString(longitude);
	}
	public String getLocationTimestamp(){
		return Double.toString(locationTimestamp);
	}
	public String getIMEI(){
		return IMEINumber;
	}
	public String getCarrierName(){
		return CarrierName;
	}
	
	public String getDeviceName(){
		return DEVICE_NAME;
	}
	
	public static String getNetworkClassName(Context context){
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if (mWifi.isConnected()){
			WifiManager WifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			return connManager.getActiveNetworkInfo().getTypeName()+" - "+ WifiManager.getConnectionInfo().getLinkSpeed()+" - "+connManager.getActiveNetworkInfo().getExtraInfo();
		}
		return connManager.getActiveNetworkInfo().getTypeName()+" - "+connManager.getActiveNetworkInfo().getSubtypeName()+" - "+connManager.getActiveNetworkInfo().getExtraInfo();
	}
	public static String generateDeviceName() {
	    final String manufacturer = Build.MANUFACTURER;
	    final String model = Build.MODEL;
	    if (model.startsWith(manufacturer)) {
	        return capitalize(model);
	    }
	    if (manufacturer.equalsIgnoreCase("HTC")) {
	        // make sure "HTC" is fully capitalized.
	        return "HTC " + model;
	    }
	    return capitalize(manufacturer) + " " + model;
	}
	
	private static String capitalize(String str) {
	    if (TextUtils.isEmpty(str)) {
	        return str;
	    }
	    final char[] arr = str.toCharArray();
	    boolean capitalizeNext = true;
	    String phrase = "";
	    for (final char c : arr) {
	        if (capitalizeNext && Character.isLetter(c)) {
	            phrase += Character.toUpperCase(c);
	            capitalizeNext = false;
	            continue;
	        } else if (Character.isWhitespace(c)) {
	            capitalizeNext = true;
	        }
	        phrase += c;
	    }
	    return phrase;
	}
	
}
