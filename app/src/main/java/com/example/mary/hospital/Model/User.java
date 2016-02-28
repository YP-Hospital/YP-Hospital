package com.example.mary.hospital.Model;

import android.provider.BaseColumns;

public class User implements BaseColumns {

    public static final String DATABASE_TABLE = "users";
    public static final String USER_NAME_COLUMN = "name";
    public static final String PHONE_COLUMN = "phone";
    public static final String AGE_COLUMN = "age";
    public static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + USER_NAME_COLUMN
            + " text not null, " + PHONE_COLUMN + " integer, " + AGE_COLUMN
            + " integer);";
}
