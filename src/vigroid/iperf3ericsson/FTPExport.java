package vigroid.iperf3ericsson;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class FTPExport extends AsyncTask<String, Void, FTPExport> {

	private String fileLocation;
	private Context context;
	private Activity activity;

	private String ftpServer;
	private String ftpUser;
	private String ftpPsw;
	private int ftpPort;
	private SharedPreferences prefs;

	public FTPExport(String fileName, Context context, Activity activity) {
		this.fileLocation = fileName;
		this.context = context;
		this.activity = activity;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		this.ftpServer = prefs.getString("ftpServer", "ftp.barracudasalad.com");
		this.ftpUser = prefs.getString("ftpUserName",
				"iperftest@barracudasalad.com");
		this.ftpPsw = prefs.getString("ftpPassword", "P@$$W0rd");
		try {
			this.ftpPort = Integer.parseInt(prefs.getString("ftpPort", "21"));
		} catch (NumberFormatException e) {

			this.ftpPort = 21;
		}
	}

	/*
	 * FTP Upload. Utililzes ftp4j - FOSS license/GNU 2.0 -
	 * http://sourceforge.net/projects/ftp4j/
	 */
	@Override
	protected FTPExport doInBackground(String... fileName) {
		FTPClient client = new FTPClient();
		File uploadFile = new File(fileLocation);
		try {
			client.connect(ftpServer, ftpPort);
			client.login(ftpUser, ftpPsw);
			client.setType(FTPClient.TYPE_BINARY);
			client.setPassive(true);
			client.noop();
			client.upload(uploadFile, new FTPTransferListener());
		} catch (Exception e) {
			toastMessage(context.getString(R.string.ftp_failed) + " "
					+ e.toString());
			try {
				client.disconnect(true);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

	public class FTPTransferListener implements FTPDataTransferListener {

		public void started() {
			toastMessage(context.getString(R.string.ftp_start));
		}

		public void transferred(int length) {
		}

		public void completed() {
			toastMessage(context.getString(R.string.ftp_success));
		}

		public void aborted() {
			toastMessage(context.getString(R.string.ftp_aborted));
		}

		public void failed() {
			toastMessage(context.getString(R.string.ftp_failed));
		}
	}

	public void toastMessage(String message) {
		final String toastMsg = message;
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(activity, toastMsg, Toast.LENGTH_SHORT).show();
			}
		});
	}
}