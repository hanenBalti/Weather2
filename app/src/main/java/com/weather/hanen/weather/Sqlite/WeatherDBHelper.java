package com.weather.hanen.weather.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bouaf on 27/12/2015.
 */
public class WeatherDBHelper extends SQLiteOpenHelper {

    public WeatherDBHelper(Context context){
        super(context, "weather.db",null,1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
