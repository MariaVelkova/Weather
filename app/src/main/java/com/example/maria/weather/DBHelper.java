package com.example.maria.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Maria on 1/19/2015.
 */
public class DBHelper extends SQLiteOpenHelper {




    public DBHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TABLE_CITIES);
        db.execSQL(Constants.CREATE_TABLE_DAYS);
        db.execSQL("INSERT INTO `" + Constants.DATABASE_TABLE_CITIES + "`(`id`,`name`,`lat`,`lon`) VALUES (NULL,'Sofia','42.69751','23.32415')");
        db.execSQL("INSERT INTO `" + Constants.DATABASE_TABLE_CITIES + "`(`id`,`name`,`lat`,`lon`) VALUES (NULL,'Plovdiv','42.150002','24.75')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
                + ". Old data will be destroyed");
        db.execSQL("DROP TABLE IF EXISTS " +  Constants.DATABASE_TABLE_CITIES);
        db.execSQL("DROP TABLE IF EXISTS " +  Constants.DATABASE_TABLE_DAYS);
        onCreate(db);
    }


}
