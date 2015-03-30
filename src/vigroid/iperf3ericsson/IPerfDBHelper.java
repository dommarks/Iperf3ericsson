package vigroid.iperf3ericsson;

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

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table iPerfDB(_id integer primary key,name text not null);";

    public IPerfDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(IPerfDBHelper.class.getName(),
                         "Upgrading database from version " + oldVersion + " to "
                         + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS iPerfDB");
        onCreate(database);
    }

}
