package tuannt.appweather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tuan on 07/12/2015.
 */
public class DBAdapter {

    static final String KEY_ROWID = "_id";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDatabase";
    static final int DATABASE_VERSION = 1;

    private static String queryCreateTable;
    private String tableName;
    private static String name;
    private String[] column;

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx, String tableName, String[] column){
        this.context = ctx;
        this.tableName = tableName;
        this.column = column;
        name = tableName;
        DBHelper = new DatabaseHelper(context, tableName);
        queryCreateTable();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context, String tableName){
            super(context, tableName, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            try{
                db.execSQL(queryCreateTable);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + "to " + newVersion + ",which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + name);
            onCreate(db);
        }
    }

    //open the data
    public DBAdapter open() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //close the data
    public void close(){
        DBHelper.close();
    }

    public boolean deleteTable(){
        return db.delete(tableName, null, null) > 0;
    }

    public void queryCreateTable(){
        queryCreateTable = "create table " + tableName + " (" + "_id integer primary key " +
            "autoincrement";
        for(int i = 0; i<column.length; i++) {
            if (i < column.length - 1) {
                queryCreateTable = queryCreateTable + ", " + column[i] + " " + "text not null";
            } else {
                queryCreateTable = queryCreateTable + ", " + column[i] + " " + "text not null);";
            }
        }
    }

    public long insertRow(String[] values){
        ContentValues initialValues = new ContentValues();
        for (int i = 0; i < column.length; i++){
            initialValues.put(column[i], values[i]);
            Log.i("I", column[i]);
        }
        return db.insert(tableName, null, initialValues);
    }

    //retrieve all the row
    public Cursor getAllRow(){
        return db.query(tableName, column, null, null, null, null, null);
    }

    //retieve a row
    public Cursor getRow(long rowId) throws SQLException{
        Cursor mCursor =
                db.query(true, tableName, column, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean deletePost(String post_id)
    {
        return db.delete(tableName, column[0]+ "="+ post_id, null) > 0;
    }

    //---updates a contact---
    public boolean updateContact(String rowId, String[] values)
    {
        ContentValues initialValues = new ContentValues();
        for (int i = 0; i < column.length; i++){
            initialValues.put(column[i], values[i]);
        }
        return db.update(tableName, initialValues, column[0] + "=" + rowId, null) > 0;
    }
}
