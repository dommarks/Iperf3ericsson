package vigroid.iperf3ericsson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowIndividualTest extends Activity {
	private TestResultDetails trd;
	private IPerfDBHelper db;
	private TextView tv;
	private Button goBackButton;
	private Button deleteTestButton;
	private boolean deleteYes = false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_individual_test);
	tv = (TextView) findViewById(R.id.individualTestView);
	
	db = new IPerfDBHelper(ShowIndividualTest.this);
	
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
	    String testID = extras.getString("testID");
	    trd = db.getTestResultByID(testID);
	}
	
	if (trd.getTestID()!=null){
		tv.setText(trd.toStringFormatted());
	}
	goBackButton = (Button) findViewById(R.id.go_back);
    goBackButton.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	});	    
    
	deleteTestButton = (Button) findViewById(R.id.delete_test);
    deleteTestButton.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			deleteYes = false;
			deleteDialog(trd.getTestID());
			if(deleteYes){
			finish();
			}
		}
	});	 
	
}
	
	public boolean deleteDialog(String testID){
		deleteYes = false;
		final String deleteTest = testID;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	db.deleteRecord(deleteTest);
		            deleteYes = true;
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(ShowIndividualTest.this);
		builder.setMessage(R.string.delete_all_ask).setPositiveButton(R.string.delete_all_yes, dialogClickListener)
		    .setNegativeButton(R.string.cancel, dialogClickListener).show();
		return deleteYes;
	}	
	
}
