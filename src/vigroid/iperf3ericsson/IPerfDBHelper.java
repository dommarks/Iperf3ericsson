package vigroid.iperf3ericsson;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    static final String colTestID = "TestID";
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
        		colTestID+ " TEXT PRIMARY KEY,"+
        		colTimestamp+ " TEXT, "+
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
        database.execSQL("DROP TABLE IF EXISTS "+ testTable);
        onCreate(database);
    }
    
    /// CRUD OPERATIONS ///
    
    /*
     * Inserts a test result into the database
     */
    public void insertRecords(TestResultDetails dr){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues(); 
		
		cv.put(colTestID, dr.getTestID());
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
     * Flushes the database
     */
    public void deleteAllRecords(){
    	SQLiteDatabase db = this.getReadableDatabase();
        db.delete(testTable, null, null);
        db.close();
    }
    
    /*
     * Queries DB for a specific test
     * Returns TestResultDetails object
     */
    public TestResultDetails getTestResultByID(String testID){
    	TestResultDetails trd = new TestResultDetails();
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("select * from " + testTable + " where " + colTestID + "='" + testID + "'" , null);
        
        if (cursor != null){
	        cursor.moveToFirst();
	        trd.setTestID(cursor.getString(0));
	        trd.setTimestamp(cursor.getString(1));
	    	trd.setConnectionType(cursor.getString(2));
	    	trd.setCarrierName(cursor.getString(3));
	    	trd.setIMEINumber(cursor.getString(4));
	    	trd.setModelNumber(cursor.getString(5));
	    	trd.setLongtitude(cursor.getString(7));
	    	trd.setLatitude(cursor.getString(6));
	    	trd.setServerName(cursor.getString(8));
	    	trd.setPortNumber(cursor.getString(9));
	    	trd.setAverageSpeed(cursor.getString(10));
	    	trd.setDataPayloadSize(cursor.getString(11));
	    	trd.setPingTime(cursor.getString(12));
	    	trd.setCpuUtilization(cursor.getString(13));
	    	trd.setIpAddress(cursor.getString(14));
        }
    	
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
      * Gets most recent test added to DB
      */
     public TestResultDetails getMostRecentTest() {
         	SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT  * FROM " + testTable;
         	Cursor cursor = db.rawQuery(selectQuery, null);
         	TestResultDetails trd = new TestResultDetails();
         	if (cursor.moveToLast()){
         			cursor.moveToLast();
                    trd.setTestID(cursor.getString(0));
                    trd.setTimestamp(cursor.getString(1));
                	trd.setConnectionType(cursor.getString(2));
                	trd.setCarrierName(cursor.getString(3));
                	trd.setIMEINumber(cursor.getString(4));
                	trd.setModelNumber(cursor.getString(5));
                	trd.setLongtitude(cursor.getString(7));
                	trd.setLatitude(cursor.getString(6));
                	trd.setServerName(cursor.getString(8));
                	trd.setPortNumber(cursor.getString(9));
                	trd.setAverageSpeed(cursor.getString(10));
                	trd.setDataPayloadSize(cursor.getString(11));
                	trd.setPingTime(cursor.getString(12));
                	trd.setCpuUtilization(cursor.getString(13));
                	trd.setIpAddress(cursor.getString(14));
         		}
             return trd;
         }
         
     /*
      * Method for HistoryFragmentTab.java. Returns a list of testID's, and a list of timestamps and 
      * average speeds for display in PreviousTests.java 
      */
    public PreviousTestLists getAllTests() {
    	ArrayList<String> arrayOfTests = new ArrayList<String>();
        ArrayList<String> testIDList = new ArrayList<String>();
        
        // Select All Query
        String selectQuery = "SELECT  * FROM " + testTable;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1)+" Speed: "+String.format("%.2f", Double.valueOf(cursor.getString(10)));
                arrayOfTests.add(name);
                testIDList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.close();
        return new PreviousTestLists(testIDList,arrayOfTests);
    }
    
    /*
     * Deletes a record with testID
     */
    public void deleteRecord(String testID){
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.delete(testTable, colTestID + "=?", new String[] {testID});
    	db.close();
    }
    
    /**
     * Exports the database into a CSV file, returns the path the file is saved to.
     * @return String - path the CSV file is saved to
     */
    public String exportDatabase(Context context) {
    	//File name and location
    	String IMEINumber = "";
    	String fileLocation = "";
    	if (LocationHelper.IMEINumber==null){
    		LocationHelper lh = new LocationHelper(context);
    		IMEINumber = lh.IMEINumber;	
        	fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IPERF-"+IMEINumber+"-"+getFormattedDate()+".csv";
    	}else{
	    	IMEINumber = LocationHelper.IMEINumber;
	    	fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IPERF-"+IMEINumber+"-"+getFormattedDate()+".csv";
    	}
    	CSVWriter writer;

    	try {
        	writer = new CSVWriter(new FileWriter(fileLocation), ':');
        	
        	// Select All Query
        	String selectQuery = "SELECT  * FROM " + testTable;

        	SQLiteDatabase db = this.getReadableDatabase();
        	Cursor cursor = db.rawQuery(selectQuery, null);

        	boolean headerWritten = false;
        	if (cursor.moveToFirst()) {
        		do {
        			///Writing CSV file header
        			if (headerWritten==false){
        				String tempheader = 
        								"Test ID#"+
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
        				headerWritten = true;
        			}
        			////End of CSV file header
        			
        			//Getting record
        			String tempdata = 
        					cursor.getString(0)+"#"+
        					cursor.getString(1)+"#"+
        					cursor.getString(2)+"#"+
        					cursor.getString(3)+"#"+
        					cursor.getString(4)+"#"+
        					cursor.getString(5)+"#"+
        					cursor.getString(7)+"#"+
        					cursor.getString(6)+"#"+
        					cursor.getString(8)+"#"+
        					cursor.getString(9)+"#"+
        					cursor.getString(10)+"#"+
        					cursor.getString(11)+"#"+
        					cursor.getString(12)+"#"+
        					cursor.getString(13)+"#"+
        					cursor.getString(14);
        			
        			String[] testEntry = tempdata.split("#");
        			writer.writeNext(testEntry);
        		}while (cursor.moveToNext());
        	}
        	db.close();
        	writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return fileLocation;
    	}
    
    /*
     * Returns human-readable current timestamp
     */
    private String getFormattedDate(){
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.hh.mm.ss");
    	return sdf.format(System.currentTimeMillis());

    }
}