package com.example.pushpindersingh.testapp1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pushpindersingh.testapp1.DatabaseHelper;

/**
 * Created by Pushpinder Singh on 7/10/2017.
 */

public class DataSource
{
    public static final String TAG = DataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    DataSource(Context context)
    {
        this.dbHelper = new DatabaseHelper(context);
    }

    public void open()
    {
        this.database = dbHelper.getWritableDatabase();

        Log.d(TAG,"database is opened");
    }

    public void close()
    {
        dbHelper.close();

        Log.d(TAG, "database is closed");
    }
}
