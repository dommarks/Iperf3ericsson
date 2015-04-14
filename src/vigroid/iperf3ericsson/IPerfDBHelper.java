package vigroid.iperf3ericsson;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * Manages database creation and updates
 *
 */
public class IPerfDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "iPerfDB";

    private static final int DATABASE_VERSION = 1;

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
    

    
    // Database creation sql statement
    private static final String CREATE_STATEMENT = "CREATE TABLE "+testTable+" ("+
    		colTimestamp+ " TEXT PRIMARY KEY , "+
    		colConnType+ " TEXT"+
    		colCarrierName+ " TEXT"+
    		colIMEI+ " TEXT"+
    		colLongitude+ " TEXT"+
    		colLatitude+ " TEXT"+
    		colServerName+ " TEXT"+
    		colPortNumber+ " INTEGER"+
    		colAveSpeed+ " REAL"+
    		colPayloadSize+ " INTEGER"+
    		colPingTime+ " TEXT"+
    		colCPUUtil+ " INTEGER"+
    		colIPAddress+ " TEXT"+")";
    
    
    
    
    
    public IPerfDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    
    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(IPerfDBHelper.class.getName(),
                         "Upgrading database from version " + oldVersion + " to "
                         + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS testTable");
        onCreate(database);
    }
    
    public void insertRecords(DetailResult dr){
		SQLiteDatabase db = this.getWritableDatabase();
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

}
