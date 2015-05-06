package vigroid.iperf3ericsson;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

@SuppressWarnings("deprecation")
public class TabListener implements ActionBar.TabListener{
	
	private Fragment fragment;
	
	public TabListener(Fragment fragment) {
		// TODO Auto-generated constructor stub
		this.fragment=fragment;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		ft.replace(R.id.activity_summary, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		ft.remove(fragment);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	
}
