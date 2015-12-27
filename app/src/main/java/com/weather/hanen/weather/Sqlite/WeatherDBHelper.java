package com.weather.hanen.weather.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bouaf on 27/12/2015.
 */
public class WeatherDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "weather.db";
    public static final int DB_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME + " (" +
                    WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY," +
                    WeatherContract.WeatherEntry.COLUMN_NAME_DAY + TEXT_TYPE + COMMA_SEP +
                    WeatherContract.WeatherEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    WeatherContract.WeatherEntry.COLUMN_NAME_HIGH_LOW + TEXT_TYPE +
            " )" ;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WeatherContract.WeatherEntry.TABLE_NAME;

    public WeatherDBHelper(Context context){
        super(context, DB_NAME,null, DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public  long  insertWeather(Weather weather  ) {
        SQLiteDatabase db= this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherContract.WeatherEntry.COLUMN_NAME_DAY, weather.getDay());
        values.put(WeatherContract.WeatherEntry.COLUMN_NAME_DESCRIPTION, weather.getDescription());
        values.put(WeatherContract.WeatherEntry.COLUMN_NAME_HIGH_LOW, weather.getHighLow());

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                WeatherContract.WeatherEntry.TABLE_NAME,
                null,
                values);
        return newRowId;
    }
}
