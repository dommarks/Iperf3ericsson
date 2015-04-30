package vigroid.iperf3ericsson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

//// See http://opencsv.sourceforge.net/ for OpenCSV licenses (under Apache 2.0 - http://www.apache.org/licenses/LICENSE-2.0) and information /////
/**
 * 
 * Manages database creation and updates
 *
 */
public class IPerfDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "iPerfDB";

	//DB Version
    private static final int DATABASE_VERSION = 1;

    //DB Name
    static final String dbName="SpeedTestDB";
    
    //Table of the iperf tests
    static final String testTable="SpeedTestTable";
    
    //Iperf test table column names
    static final String colTimestamp = "Timestamp";
    static final String colConnType="ConnectionType";
    static final String colCarrierName="CarrierName";
    static final String colIMEI="IMEI";
    static final String colModelNum = "ModelNumber";
    static final String colLongitude = "Longitude";
    static final String colLatitude = "Latitude";
    static final String colServerName = "ServerName";
    static final String colPortNumber = "PortNumber";
    static final String colAveSpeed="AverageSpeed";
    static final String colPayloadSize = "PayloadSize";
    static final String colPingTime = "PingTime";
    static final String colCPUUtil = "CPUUtilization";
    static final String colIPAddress = "IPAddress";
     
    public IPerfDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
    	   // Database creation sql statement
        String CREATE_STATEMENT = "CREATE TABLE "+testTable+"("+
        		colTimestamp+ " TEXT PRIMARY KEY, "+
        		colConnType+ " TEXT,"+
        		colCarrierName+ " TEXT,"+
        		colIMEI+ " TEXT,"+
        		colModelNum +" TEXT,"+
        		colLongitude+ " TEXT,"+
        		colLatitude+ " TEXT,"+
        		colServerName+ " TEXT,"+
        		colPortNumber+ " TEXT,"+
        		colAveSpeed+ " TEXT,"+
        		colPayloadSize+ " TEXT,"+
        		colPingTime+ " TEXT,"+
        		colCPUUtil+ " TEXT,"+
        		colIPAddress+ " TEXT"+")";
        db.execSQL(CREATE_STATEMENT);
    }

    
    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(IPerfDBHelper.class.getName(),
                         "Upgrading database from version " + oldVersion + " to "
                         + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS "+ testTable);
        onCreate(database);
    }
    
    /// CRUD OPERATIONS ///
    
    public void insertRecords(TestResultDetails dr){
    	Log.w("IPERF","insertRecords "+dr.getTimestamp());
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues(); 
		
		cv.put(colTimestamp, dr.getTimestamp());
		cv.put(colConnType, dr.getConnectionType());
		cv.put(colCarrierName, dr.getCarrierName());
		cv.put(colIMEI, dr.getIMEINumber());
		cv.put(colModelNum, dr.getModelNumber());
		cv.put(colLongitude, dr.getLongtitude());
		cv.put(colLatitude, dr.getLatitude());
		cv.put(colServerName, dr.getServerName());
		cv.put(colPortNumber, dr.getPortNumber());
		cv.put(colAveSpeed, dr.getAverageSpeed());
		cv.put(colPayloadSize, dr.getDataPayloadSize());
		cv.put(colPingTime, dr.getPingTime());
		cv.put(colCPUUtil, dr.getCpuUtilization());
		cv.put(colIPAddress, dr.getIpAddress());
		
		db.insert(testTable,null,cv);
		db.close();	
	}
    
    /*
     * Queries DB for a specific test
     * Returns TestResultDetails object
     */
    public TestResultDetails getTestResultByID(long testID){
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
        Cursor cursor = db.query(testTable, null, colTimestamp + "=?",
                new String[] { String.valueOf(testID) }, null, null, null, null);
        TestResultDetails trd = new TestResultDetails();
        
        if (cursor != null){
        cursor.moveToFirst();
        trd.setTimestamp(cursor.getString(0));
    	trd.setConnectionType(cursor.getString(1));
    	trd.setCarrierName(cursor.getString(2));
    	trd.setIMEINumber(cursor.getString(3));
    	trd.setModelNumber(cursor.getString(4));
    	trd.setLongtitude(cursor.getString(5));
    	trd.setLatitude(cursor.getString(6));
    	trd.setServerName(cursor.getString(7));
    	trd.setPortNumber(cursor.getString(8));
    	trd.setAverageSpeed(cursor.getString(9));
    	trd.setDataPayloadSize(cursor.getString(10));
    	trd.setPingTime(cursor.getString(11));
    	trd.setCpuUtilization(cursor.getString(12));
    	trd.setIpAddress(cursor.getString(13));}
    	
    	db.close();
        return trd;
    	
    }
    
    
 /*
  * Returns number of tests in DB
  */
     public int getNumberOfTestsRun() {
    	 SQLiteDatabase db = this.getReadableDatabase();
         String countQuery = "SELECT  * FROM " + testTable;
         Cursor cursor = db.rawQuery(countQuery, null);
         int count = cursor.getCount();
         
         cursor.close();
         db.close();
         return count;
     }
    
     /*
      * Method for PreviousTests.java. Returns a list of all tests run, and updates a list of timestamps and 
      * average speeds for display in PreviousTests.java 
      */
    public List<TestResultDetails> getAllTests() {
        List<TestResultDetails> testResultList = new ArrayList<TestResultDetails>();
        PreviousTests.ArrayofTests.clear();
        
        // Select All Query
        String selectQuery = "SELECT  * FROM " + testTable;
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.w("IPERF","Count = " +Integer.toString(cursor.getCount()));

        if (cursor.moveToFirst()) {
        	//Log.w("IPERF","DB MOVED TO FIRST");
            do {
            	TestResultDetails trd = new TestResultDetails();
                trd.setTimestamp(cursor.getString(0));
            	trd.setConnectionType(cursor.getString(1));
            	trd.setCarrierName(cursor.getString(2));
            	trd.setIMEINumber(cursor.getString(3));
            	trd.setModelNumber(cursor.getString(4));
            	trd.setLongtitude(cursor.getString(5));
            	trd.setLatitude(cursor.getString(6));
            	trd.setServerName(cursor.getString(7));
            	trd.setPortNumber(cursor.getString(8));
            	trd.setAverageSpeed(cursor.getString(9));
            	trd.setDataPayloadSize(cursor.getString(10));
            	trd.setPingTime(cursor.getString(11));
            	trd.setCpuUtilization(cursor.getString(12));
            	trd.setIpAddress(cursor.getString(13));
            	
                String name = cursor.getString(0)+" Speed:"+cursor.getString(9);
                PreviousTests.ArrayofTests.add(name);
                testResultList.add(trd);
               // Log.w("IPERF","ADDED RECORD");
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return testResultList;
    }
    
    /**
     * Exports the database into a CSV file, returns the path the file is saved to.
     * @return String - path the CSV file is saved to
     */
    public String exportDatabase() {
    	String fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FILEEXPORT.csv";
        CSVWriter writer;
        Log.w("IPERF","EXPORT RUN to:"+fileLocation);
        try {
        	//android.os.Environment.getExternalStorageDirectory(), "FILEEXPORT.csv")
		writer = new CSVWriter(new FileWriter(fileLocation), '\t');
		
		
        // feed in your array (or convert your data to an array)
//        String[] entries = "first#second#third".split("#");
//        writer.writeNext(entries);
//   	 	writer.close();
    	
    	
    	// Select All Query
    	String selectQuery = "SELECT  * FROM " + testTable;
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
//        Log.w("IPERF","Count = " +Integer.toString(cursor.getCount()));

       // String[] temp = {};
        
        int counter = 0;
        if (cursor.moveToFirst()) {
            do {
            	if (counter ==0){
            	String tempheader = 
                        "Timestamp#"+
                    	"Connection Type#"+
                    	"Carrier Name#"+
                    	"IMEI Number#"+
                    	"Model#"+
                    	"Latitude#"+
                    	"Longitude#"+
                    	"Server Name#"+
                    	"Port#"+
                    	"Average Speed#"+
                    	"Payload Size#"+
                    	"Ping Time#"+
                    	"CPU Utilization#"+
                    	"Device IP";

                    	String[] header = tempheader.split("#");
                    	writer.writeNext(header);
                    	counter ++;
                    	Log.w("IPERF","Writing header");}
            	else{
            	String tempdata = 
                cursor.getString(0)+"#"+
            	cursor.getString(1)+"#"+
            	cursor.getString(2)+"#"+
            	cursor.getString(3)+"#"+
            	cursor.getString(4)+"#"+
            	cursor.getString(6)+"#"+
            	cursor.getString(5)+"#"+
            	cursor.getString(7)+"#"+
            	cursor.getString(8)+"#"+
            	cursor.getString(9)+"#"+
            	cursor.getString(10)+"#"+
            	cursor.getString(11)+"#"+
            	cursor.getString(12)+"#"+
            	cursor.getString(13);
            	
            	
            	String[] entry = tempdata.split("#");
            	writer.writeNext(entry);
            	Log.w("IPERF",tempdata);
            	}
            } while (cursor.moveToNext());
            
        }
        db.close();
        writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w("IPERF","EXPORT FAILED");
			Log.w("IPERF",e.toString());
		}
        
        return fileLocation;
    	}

//    /*
//     * Converts unix timestamp to human-readable date.
//     */
//    public String timestampToDate(String timestamp){
//    long dv = Long.valueOf(timestamp);
//    Date df = new java.util.Date(dv);
//    String date = new SimpleDateFormat("MM dd, yyyy hh:mm:ss").format(df);
//    return date;
//    }
}
