package vigroid.iperf3ericsson;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class IperfLocationListener implements LocationListener {

    public static double latitude;
    public static double longitude;

    @Override
	public void onLocationChanged(Location loc)
	{
		loc.getLatitude();
		loc.getLongitude();
		latitude=loc.getLatitude();
		longitude=loc.getLongitude();
		
		LocationHelper.latitude=loc.getLatitude();
		LocationHelper.longitude=loc.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider)
	{
		latitude = -1;
		longitude = -1;
	}
	@Override
	public void onProviderEnabled(String provider)
	{
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}
}