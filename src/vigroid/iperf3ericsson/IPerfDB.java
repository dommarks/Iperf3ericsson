package vigroid.iperf3ericsson;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class IPerfDB {
	private IPerfDBHelper dbHelper;  

	private SQLiteDatabase database;  

    static final String dbName="SpeedTestDB";
    static final String testTable="speedTests";
    static final String colTimestamp = "Timestamp";
    static final String colConnType="ConnectionType";
    static final String colCarrierName="CarrierName";
    static final String colIMEI="IMEI";
    static final String colLongitude = "Longitude";
    static final String colLatitude = "Latitude";
    static final String colServerName = "ServerName";
    static final String colPortNumber = "PortNumber";
    static final String colAveSpeed="AverageSpeed";
    static final String colPayloadSize = "PayloadSize";
    static final String colPingTime = "PingTime";
    static final String colCPUUtil = "CPUUtilization";
    static final String colIPAddress = "IPAddress";
	/** 
	 * 
	 * @param context 
	 */  
	public IPerfDB(Context context){  
	    dbHelper = new IPerfDBHelper(context);  
	    database = dbHelper.getWritableDatabase();  
	}

	public void insertTestRecord(DetailResult dr){
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues(); 
		cv.put(colTimestamp, Long.toString(dr.getTimestamp()));
		cv.put(colConnType, dr.getConnectionType());
		cv.put(colCarrierName, dr.getCarrierName());
		cv.put(colIMEI, dr.getIMEINumber());
		cv.put(colLongitude, Double.toString(dr.getLongtitude()));
		cv.put(colLatitude, Double.toString(dr.getLatitude()));
		cv.put(colServerName, dr.getServerName());
		cv.put(colPortNumber, dr.getPortNumber());
		cv.put(colAveSpeed, Double.toString(dr.getAverageSpeed()));
		cv.put(colPayloadSize, Double.toString(dr.getDataPayloadSize()));
		cv.put(colPingTime, Long.toString(dr.getPingTime()));
		cv.put(colCPUUtil, Double.toString(dr.getCpuUtilization()));
		cv.put(colIPAddress, dr.getIpAddress());
		db.insert(testTable,colTimestamp,cv);
		db.close();
		
	}
	public long createRecords(long id, double speed){  
	   ContentValues values = new ContentValues();  
//	   values.put(TEST_ID, id);  
//	   values.put(TEST_SPEED, speed);  
	   
	   
	   
	   
	   
	   return database.insert(testTable, null, values);  
	}    

	public Cursor selectRecords() {
	   String[] cols = new String[] {colTimestamp, colConnType,colCarrierName,colIMEI,colLongitude,colLatitude,
			   colServerName,colPortNumber,colAveSpeed,colPayloadSize,colPingTime,colCPUUtil,colIPAddress};  
	   Cursor mCursor = database.query(true, testTable,cols,null  
	            , null, null, null, null, null);  
	   if (mCursor != null) {  
	     mCursor.moveToFirst();  
	   }  
	   return mCursor; // iterate to get each value.
}
	
}
