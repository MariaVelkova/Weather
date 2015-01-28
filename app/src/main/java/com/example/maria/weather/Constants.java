package com.example.maria.weather;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by Maria on 1/19/2015.
 */
public class Constants {
    // Constants for the content provider
    public static final String AUTHORITY            = "com.example.maria.weather.ForecastProvider";
    public static final String DATABASE_NAME        = "Forecast.db";
    public static final int DATABASE_VERSION = 2;



    // ------- define some Uris
    private static final String PATH_CITIES = "cities";
    private static final String PATH_DAYS = "days";

    public static final Uri CONTENT_URI_CITIES = Uri.parse("content://" + AUTHORITY
            + "/" + PATH_CITIES);
    public static final Uri CONTENT_URI_DAYS = Uri.parse("content://" + AUTHORITY
            + "/" + PATH_DAYS);

// ------- maybe also define CONTENT_TYPE for each

    //public static final String URL                  = "content://" + AUTHORITY + "/" + DATABASE_NAME;
    //public static final Uri CONTENT_URI             = Uri.parse(URL);

    // ------- setup UriMatcher
    public static final int TAG_CITIES = 10;
    public static final int TAG_CITY_ID = 20;
    public static final int TAG_DAYS = 30;
    public static final int TAG_DAY_ID = 40;
    public static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_CITIES, TAG_CITIES);
        uriMatcher.addURI(AUTHORITY, PATH_CITIES + "/#", TAG_CITY_ID);
        uriMatcher.addURI(AUTHORITY, PATH_DAYS, TAG_DAYS);
        uriMatcher.addURI(AUTHORITY, PATH_DAYS + "/#", TAG_DAY_ID);
    }

    public static final String DATABASE_TABLE_CITIES = "cities";
    public static final String DATABASE_TABLE_DAYS = "days";

    // Constants for the database
    public static final String DATABASE_TABLE_CITIES_ID              = "id";
    public static final String DATABASE_TABLE_CITIES_NAME            = "name";
    public static final String DATABASE_TABLE_CITIES_LAT             = "lat";
    public static final String DATABASE_TABLE_CITIES_LON             = "lon";

    public static final String DATABASE_TABLE_DAYS_ID                = "id";
    public static final String DATABASE_TABLE_DAYS_DT                = "dt";
    public static final String DATABASE_TABLE_DAYS_CITY_ID           = "city_id";
    public static final String DATABASE_TABLE_DAYS_MIN               = "min";
    public static final String DATABASE_TABLE_DAYS_MAX               = "max";



    public static final String CREATE_TABLE_DAYS = " CREATE TABLE " + DATABASE_TABLE_DAYS +
            " (" + DATABASE_TABLE_DAYS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             DATABASE_TABLE_DAYS_DT + " INTEGER NOT NULL, " +
             DATABASE_TABLE_DAYS_CITY_ID + " INTEGER NOT NULL, " +
             DATABASE_TABLE_DAYS_MIN + " INTEGER NOT NULL, " +
             DATABASE_TABLE_DAYS_MAX + " INTEGER NOT NULL);";

    public static final String CREATE_TABLE_CITIES = " CREATE TABLE " + DATABASE_TABLE_CITIES +
            " (" + DATABASE_TABLE_CITIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DATABASE_TABLE_CITIES_NAME + " TEXT NOT NULL, " +
            DATABASE_TABLE_CITIES_LAT + " DOUBLE NOT NULL, " +
            DATABASE_TABLE_CITIES_LON + " DOUBLE NOT NULL);";



}
