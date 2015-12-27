package com.weather.hanen.weather.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bouaf on 27/12/2015.
 */
public class WeatherDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "weather.db";
    public static final int DB_VERSION = 1;

    public WeatherDBHelper(Context context){
        super(context, DB_NAME,null, DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
