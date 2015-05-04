package vigroid.iperf3ericsson;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class FTPExport extends AsyncTask<String, Void, FTPExport> {
	
	private String fileLocation;
	private Context context;
	private Activity activity;
	
	FTPExport(String fileName,Context context, Activity activity){
		this.fileLocation = fileName;
		this.context = context;
		this.activity = activity;
	}
	
	/*
	 * FTP Upload. Utililzes ftp4j - FOSS license/GNU 2.0 - http://sourceforge.net/projects/ftp4j/
	 */
	@Override
	protected FTPExport doInBackground(String... fileName) {
		Log.w("IPERF","EXPORT STARTED");
		 FTPClient client = new FTPClient();
		 File uploadFile = new File(fileLocation);
		try {
			client.connect("ftp.barracudasalad.com",21);
			client.login("iperftest@barracudasalad.com", "P@$$W0rd");
			client.setType(FTPClient.TYPE_BINARY);
			client.setPassive(true); 
			client.noop();
			client.upload(uploadFile, new MyTransferListener());
			Log.w("IPERF","EXPORT TRY");
		} catch (Exception e) {
			toastMessage(context.getString(R.string.ftp_failed)+" "+ e.toString());
			Log.w("IPERF",e.toString());
			try {
				client.disconnect(true);	
			} catch (Exception e2) {
				Log.w("IPERF",e2.toString());
			}
		}
		
  
		return null;
	}
	
//	/*
//	 * FTP Upload. Utililzes ftp4j - FOSS license/GNU 2.0 - http://sourceforge.net/projects/ftp4j/
//	 */
//	public void exportViaFTP(){
//		Log.w("IPERF","EXPORT STARTED");
//		 FTPClient client = new FTPClient();
//		 File uploadFile = new File(fileLocation);
//		try {
//			client.connect("lab00.cs.ndsu.nodak.edu",22);
//			client.login("u552", "8D@ys/wk");
//			client.setType(FTPClient.TYPE_BINARY);
//			client.setPassive(true); 
//			client.noop();
//			client.changeDirectory("/home/u552/IPERFuploads/");
//			client.upload(uploadFile, new MyTransferListener());
//			Log.w("IPERF","EXPORT TRY");
//		} catch (Exception e) {
//			toastMessage(context.getString(R.string.ftp_failed)+" "+ e.toString());
//			Log.w("IPERF",e.toString());
//			try {
//				client.disconnect(true);	
//			} catch (Exception e2) {
//				Log.w("IPERF","e2"+e2.toString());
//			}
//		}
//		
//   }
	
	public class MyTransferListener implements FTPDataTransferListener {

    	public void started() {
    		Log.w("IPERF","STARTED");
    		toastMessage(context.getString(R.string.ftp_start));
    		
    	}
    	
    	public void transferred(int length) {
    		Log.w("IPERF","Transfered");
    	}

    	public void completed() {
    		Log.w("IPERF","COMPLETED");
    		toastMessage(context.getString(R.string.ftp_success));
    	}

    	public void aborted() {
    		Log.w("IPERF","ABORTED");
    		toastMessage(context.getString(R.string.ftp_aborted));
    	}

    	public void failed() {
    		Log.w("IPERF","FAILED");
    		toastMessage(context.getString(R.string.ftp_failed));
    	}

    }
	
	public void toastMessage(String message) {
		final String toastMsg = message;
		activity.runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(activity, toastMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
