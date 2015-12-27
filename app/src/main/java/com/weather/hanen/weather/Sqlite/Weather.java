package com.weather.hanen.weather.Sqlite;

/**
 * Created by hanen on 27/12/2015.
 */
public class Weather {
    int _ID;
    String day;
    String description;
    String highLow;

    public Weather(int _ID, String day, String description, String highLow) {
        this._ID = _ID;
        this.day = day;
        this.description = description;
        this.highLow = highLow;
    }

    public Weather(String day, String description, String highLow) {
        this.day = day;
        this.description = description;
        this.highLow = highLow;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighLow() {
        return highLow;
    }

    public void setHighLow(String highLow) {
        this.highLow = highLow;
    }

}
