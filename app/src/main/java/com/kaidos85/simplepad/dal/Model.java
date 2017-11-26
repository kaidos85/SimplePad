package com.kaidos85.simplepad.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class Model extends SQLiteOpenHelper {

    static final String db_name = "note.db";
    static final int db_version = 1;
    public String createSql;
    public String dropSql;

    public Model(Context context)
    {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropSql);
        onCreate(db);
    }
}
