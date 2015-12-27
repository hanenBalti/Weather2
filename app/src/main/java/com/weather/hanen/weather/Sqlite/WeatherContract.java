package com.weather.hanen.weather.Sqlite;

import android.provider.BaseColumns;

/**
 * Created by bouaf on 27/12/2015.
 */
public final class WeatherContract {

    public static  abstract  class  WeatherEntry implements BaseColumns{
        public  static  final String TABLE_NAME = "weather";
        public  static  final String COLUMN_NAME_DAY = "day";
        public  static  final String COLUMN_NAME_DESCRIPTION="description";
        public  static  final String COLUMN_NAME_HIGH_LOW="hight_low";
    }
}
