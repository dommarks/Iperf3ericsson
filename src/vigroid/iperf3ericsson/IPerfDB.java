package vigroid.iperf3ericsson;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class IPerfDB {
	private IPerfDBHelper dbHelper;  

	private SQLiteDatabase database;  

	public final static String TEST_TABLE="IPerfTestTable"; // name of table 

	public final static String TEST_ID="0"; // id value for individual test
	public final static String TEST_SPEED="0";  // value of the speed of the test
	/** 
	 * 
	 * @param context 
	 */  
	public IPerfDB(Context context){  
	    dbHelper = new IPerfDBHelper(context);  
	    database = dbHelper.getWritableDatabase();  
	}


	public long createRecords(long id, double speed){  
	   ContentValues values = new ContentValues();  
	   values.put(TEST_ID, id);  
	   values.put(TEST_SPEED, speed);  
	   return database.insert(TEST_TABLE, null, values);  
	}    

	public Cursor selectRecords() {
	   String[] cols = new String[] {TEST_ID, TEST_SPEED};  
	   Cursor mCursor = database.query(true, TEST_TABLE,cols,null  
	            , null, null, null, null, null);  
	   if (mCursor != null) {  
	     mCursor.moveToFirst();  
	   }  
	   return mCursor; // iterate to get each value.
}
	
}
