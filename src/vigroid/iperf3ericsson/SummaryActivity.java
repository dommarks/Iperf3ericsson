package vigroid.iperf3ericsson;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;



@SuppressWarnings("deprecation")
public class SummaryActivity extends Activity {

	ActionBar.Tab detailTab, historyTab;
	Fragment detailFragmentTab =new DetailFragmentTab();
	Fragment historyFragmentTab = new HistoryFragmentTab();
	
	ActionBar actionBar;
	TestResultDetails currentTest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		detailTab = actionBar.newTab().setText("Detail");
		historyTab = actionBar.newTab().setText("History");
		
		detailTab.setTabListener(new TabListener(detailFragmentTab));
		historyTab.setTabListener(new TabListener(historyFragmentTab));
		
		actionBar.addTab(detailTab);
		actionBar.addTab(historyTab);
		
	}
}
