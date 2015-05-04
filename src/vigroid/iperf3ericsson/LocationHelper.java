package vigroid.iperf3ericsson;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/* Gets device location.
 * @TODO add handling for GPS turned off, network location, and delayed updates.
 */
public class LocationHelper{
	
	//Initialized on class start
	private  LocationManager lm; 
	private Location location;
	private Location locationNetwork;
	public static double longitude;
	public static double latitude;
	private static long locationTimestamp = 0;
	public static String IMEINumber;
	public static String CarrierName;
	public static String DEVICE_NAME;
	private IperfLocationListener mlocListener;
	private LocationManager mlocManager;
	private boolean isListening;
	private Context context;
	
	public LocationHelper(Context context){
		longitude = 0; latitude = 0;
		this.context = context;
		//LocationManager mlocManager=null;
        //LocationListener mlocListener;
        mlocManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new IperfLocationListener();
       mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
       isListening = true;

       if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           if(IperfLocationListener.latitude>0)
           {
                latitude= IperfLocationListener.latitude;
                longitude = IperfLocationListener.longitude;
            }
            else
            {
            	latitude = -1;
            	longitude = -1;
             }
         } else {
            // et_field_name.setText("GPS is not turned on...");
         }

		
		//Setting carrier name
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		CarrierName = manager.getNetworkOperatorName();
		IMEINumber = manager.getDeviceId();
		DEVICE_NAME = generateDeviceName(); 
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
	
	public String getNetworkClassName(Context context){
		//Getting network connection type
		if (ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_WIFI)){
			return "WIFI";
		}else if (ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_MOBILE)){
			return getNetworkClass(context);
		}else {
			return "UNKNOWN";
		}	
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
	
	public String getNetworkClass(Context context) {
	    TelephonyManager mTelephonyManager = (TelephonyManager)
	            context.getSystemService(Context.TELEPHONY_SERVICE);
	    int networkType = mTelephonyManager.getNetworkType();
	    switch (networkType) {
	        case TelephonyManager.NETWORK_TYPE_GPRS:
	        case TelephonyManager.NETWORK_TYPE_EDGE:
	        case TelephonyManager.NETWORK_TYPE_CDMA:
	        case TelephonyManager.NETWORK_TYPE_1xRTT:
	        case TelephonyManager.NETWORK_TYPE_IDEN:
	            return "2G";
	        case TelephonyManager.NETWORK_TYPE_UMTS:
	        case TelephonyManager.NETWORK_TYPE_EVDO_0:
	        case TelephonyManager.NETWORK_TYPE_EVDO_A:
	        case TelephonyManager.NETWORK_TYPE_HSDPA:
	        case TelephonyManager.NETWORK_TYPE_HSUPA:
	        case TelephonyManager.NETWORK_TYPE_HSPA:
	        case TelephonyManager.NETWORK_TYPE_EVDO_B:
	        case TelephonyManager.NETWORK_TYPE_EHRPD:
	        case TelephonyManager.NETWORK_TYPE_HSPAP:
	            return "3G";
	        case TelephonyManager.NETWORK_TYPE_LTE:
	            return "4G";
	        default:
	            return "Unknown";
	    }
	}
	
}
