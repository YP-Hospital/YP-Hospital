package com.example.mary.hospital;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "hospital.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.DATABASE_CREATE_SCRIPT);
        ContentValues cv = new ContentValues();
        cv.put("id", 1);
        cv.put(User.USER_NAME_COLUMN, "admin");
        cv.put(User.PASSWORD_COLUMN, UserServiceImpl.passwordToHash("admin"));
        db.insert(User.DATABASE_TABLE, null, cv);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Update from version" + oldVersion + " on version" + newVersion);
        db.execSQL("DROP TABLE IF IT EXIST " + User.DATABASE_TABLE);
        onCreate(db);
    }
}
