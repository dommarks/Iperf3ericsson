package vigroid.iperf3ericsson;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragmentTab extends Fragment{
	
	TextView textView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		IPerfDBHelper db = new IPerfDBHelper(getActivity());
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.detail_layout, container,false);
		textView=(TextView) rootView.findViewById(R.id.summary);
		TestResultDetails trd= db.getMostRecentTest();
		textView.setText(trd.toStringFormatted());
		
		return rootView;
	}
	
}
