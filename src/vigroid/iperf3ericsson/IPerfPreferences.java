package vigroid.iperf3ericsson;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class IPerfPreferences extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content, 
				new PrefFragment()).commit();
		
	}
	
	public static class PrefFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			//load the preference from XML
			addPreferencesFromResource(R.xml.prefiperf);
		}
		
	}
	
}
