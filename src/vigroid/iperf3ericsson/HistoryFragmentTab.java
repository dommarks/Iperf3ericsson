package vigroid.iperf3ericsson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract.Root;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HistoryFragmentTab extends Fragment {

	private static ArrayList<String> arrayOfTests = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private List<TestResultDetails> trdList;

	private GridView gridView;
	private Button exportEmailButton;
	private Button exportFTPButton;
	private Button deleteAll;

	private IPerfDBHelper db;

	private boolean ftpInProgress;
	private boolean deleteYes;

	Context currentContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.history_layout, container,
				false);
		currentContext = getActivity().getApplicationContext();
		db = new IPerfDBHelper(currentContext);
		// Reading in all Tests run
		ResultListAndObjects rl = db.getAllTests();
		trdList = rl.getTestResultObjectArray();
		arrayOfTests = rl.getTestNameandSpeedArray();
		adapter = new ArrayAdapter<String>(currentContext,
				android.R.layout.simple_list_item_1, arrayOfTests);

		gridView = (GridView) rootView.findViewById(R.id.gridView1);
		gridView.setAdapter(adapter);
		// / *** Gridview Handler *** ///
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Intent intent = new Intent(currentContext,
						ShowIndividualTest.class);
				intent.putExtra("testID", trdList.get(position).getTestID()
						.toString());
				startActivity(intent);
			}
		});

		// /**Button Handlers**///
		exportEmailButton = (Button) rootView.findViewById(R.id.exportButtonEmail);
		exportEmailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (db.getNumberOfTestsRun() > 0) {
					generateEmail(db.exportDatabase());
				} else {
					Toast.makeText(currentContext,
							R.string.run_tests_first, Toast.LENGTH_LONG).show();
				}
			}
		});

		exportFTPButton = (Button) rootView.findViewById(R.id.exportButtonFTP);
		exportFTPButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!ftpInProgress) {
					ftpInProgress = true;
					if (db.getNumberOfTestsRun() > 0) {
						exportFTPButton
								.setText(R.string.export_ftp_button_in_progress);
						String fileLocation = db.exportDatabase();
						new FTPExport(fileLocation, currentContext,
								getActivity()).execute();
						ftpInProgress = false;
						exportFTPButton.setText(R.string.export_ftp_button);
					} else {
						ftpInProgress = false;
						Toast.makeText(currentContext,
								R.string.run_tests_first, Toast.LENGTH_LONG)
								.show();
					}
				}
			}
		});

		deleteAll = (Button) rootView.findViewById(R.id.deleteButton);
		deleteAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteDialog();
				if (deleteYes) {
					ResultListAndObjects rl = db.getAllTests();
					trdList = rl.getTestResultObjectArray();
					arrayOfTests = rl.getTestNameandSpeedArray();
					adapter.clear();
					deleteYes = false;
				}
			}
		});

		return rootView;
	}

	public boolean deleteDialog() {
		deleteYes = false;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					db.deleteAllRecords();
					deleteYes = true;
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		builder.setMessage(R.string.delete_all_ask)
				.setPositiveButton(R.string.delete_all_yes, dialogClickListener)
				.setNegativeButton(R.string.cancel, dialogClickListener).show();
		return deleteYes;
	}

	public void generateEmail(String fileLocation) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { getString(R.string.email_address) });
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));

		File file = new File(fileLocation);
		if (!file.exists() || !file.canRead()) {
			Toast.makeText(currentContext, R.string.unable_to_attach,
					Toast.LENGTH_SHORT).show();
			getActivity().finish();
			return;
		}

		Uri uri = Uri.fromFile(file);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(intent, "Send email..."));
	}
}
