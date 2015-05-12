package vigroid.iperf3ericsson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowIndividualTest extends Activity {
	private TestResultDetails trd;
	private IPerfDBHelper db;
	private TextView tv;
	private View v;
	private Button goBackButton;
	private Button deleteTestButton;
	private String testID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_individual_test);
	tv = (TextView) findViewById(R.id.individualTestView);
	
	
	db = new IPerfDBHelper(ShowIndividualTest.this);
	
	
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
	    testID = extras.getString("testID");
	    trd = db.getTestResultByID(testID);
	}
	
	if (trd.getTestID()!=null){
		tv.setText(trd.toStringFormatted());
	}
	
	//Goes back to previous activity
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
			deleteDialog(testID);	
		}
	});	 
	
}
	/*
	 * Displays "are you sure?" dialog
	 */
	public void deleteDialog(String testID){
		final String testToDelete = testID;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
					db.deleteRecord(testToDelete);
					finish();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(ShowIndividualTest.this);
		builder.setMessage(R.string.delete_individual_test_ask).setPositiveButton(R.string.delete_all_yes, dialogClickListener)
		    .setNegativeButton(R.string.cancel, dialogClickListener).show();
	}	
	
}