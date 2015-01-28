package com.example.maria.weather;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Maria on 1/19/2015.
 */
public class ForecastProvider extends ContentProvider {
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();

        if(database == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = Constants.uriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        long id = 0;
        switch (uriType){
            case Constants.TAG_CITIES:
                queryBuilder.setTables(Constants.DATABASE_TABLE_CITIES);
                if (sortOrder == null || sortOrder == ""){
                    // No sorting-> sort on names by default
                    sortOrder = Constants.DATABASE_TABLE_CITIES_ID;
                }
                break;
            case Constants.TAG_CITY_ID:
                queryBuilder.setTables(Constants.DATABASE_TABLE_CITIES);
                if (sortOrder == null || sortOrder == ""){
                    // No sorting-> sort on names by default
                    sortOrder = Constants.DATABASE_TABLE_CITIES_ID;
                }
                break;
            case Constants.TAG_DAYS:
                queryBuilder.setTables(Constants.DATABASE_TABLE_DAYS);
                if (sortOrder == null || sortOrder == ""){
                    // No sorting-> sort on names by default
                    sortOrder = Constants.DATABASE_TABLE_DAYS_ID;
                }
                break;
            case Constants.TAG_DAY_ID:
                queryBuilder.setTables(Constants.DATABASE_TABLE_DAYS);
                if (sortOrder == null || sortOrder == ""){
                    // No sorting-> sort on names by default
                    sortOrder = Constants.DATABASE_TABLE_DAYS_ID;
                }
                break;
            default:
                throw new SQLException("Failed to insert row into " + uri);
        }





        /*
        SQLiteDatabase db = dbName, the table name to compile the query against.
        String[] projection = columnNames, a list of which table columns to return. Passing "null" will return all.
        String selection = whereClause, filter for the selection of data, null will select all data.
        String[] selectionArgs = you may include ?s in the "whereClause"". These placeholders will get replaced by the values from the selectionArgs array.
        String[] groupBy = a filter declaring how to group rows, null will cause the rows to not be grouped.
        String[] having	= filter for the groups, null means no filter.
        String sortOrder = orderBy, table columns which will be used to order the data, null means no ordering.
        */
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        // Register to watch a content URI for changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        switch (Constants.uriMatcher.match(uri)){
            case Constants.TAG_CITIES:
                long _ID1 = database.insert(Constants.DATABASE_TABLE_CITIES, "", values);
                //---if added successfully---
                if (_ID1 > 0) {
                    _uri = ContentUris.withAppendedId(Constants.CONTENT_URI_CITIES, _ID1);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case Constants.TAG_DAYS:
                long _ID2 = database.insert(Constants.DATABASE_TABLE_DAYS, "", values);
                //---if added successfully---
                if (_ID2 > 0) {
                    _uri = ContentUris.withAppendedId(Constants.CONTENT_URI_DAYS, _ID2);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            default: throw new SQLException("Failed to insert row into " + uri);
        }
        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        String tableName;
        switch (Constants.uriMatcher.match(uri)){
            case Constants.TAG_CITIES:
                tableName = Constants.DATABASE_TABLE_CITIES;
                break;
            case Constants.TAG_DAYS:
                tableName = Constants.DATABASE_TABLE_DAYS;
                break;
            default: throw new SQLException("Failed to delete row from " + uri);
        }
        count = database.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        String tableName;
        switch (Constants.uriMatcher.match(uri)){
            case Constants.TAG_CITIES:
                tableName = Constants.DATABASE_TABLE_CITIES;
                break;
            case Constants.TAG_DAYS:
                tableName = Constants.DATABASE_TABLE_DAYS;
                break;
            default: throw new SQLException("Failed to delete row from " + uri);
        }
        count = database.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
