package com.example.mary.hospital.Service.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mary.hospital.DatabaseHelper;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserServiceImpl implements UserService {
    private static String HASH_ALGORITHM = "SHA-256";
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sdb;

    public UserServiceImpl (Context context) {
        databaseHelper = new DatabaseHelper(context);
        sdb = databaseHelper.getReadableDatabase();
    }

    public void addUserInDB (User user) {
        ContentValues values = new ContentValues();
        values.put(User.USER_NAME_COLUMN, user.getName());
        values.put(User.PASSWORD_COLUMN, passwordToHash(user.getPassword()));
        user.setPassword("");
        values.put(User.PHONE_COLUMN, user.getPhone());
        values.put(User.AGE_COLUMN, user.getAge());
        values.put(User.ROLE_COLUMN, user.getAge());
        sdb.insert(User.DATABASE_TABLE, null, values);
    }

    public Boolean isUserExist(String name) {
        Cursor cursor = sdb.rawQuery("Select " + User.USER_NAME_COLUMN + "  from " + User.DATABASE_TABLE, null);
        Boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    public Boolean isCorrectPassword(String name, String password) {
        Cursor cursor = sdb.rawQuery("Select " + User.PASSWORD_COLUMN + " from " + User.DATABASE_TABLE
                                    + " where " + User.USER_NAME_COLUMN + "= ?", new String[]{name});
        cursor.moveToFirst();
        Boolean result = password.equals(cursor.getString(cursor.getColumnIndex(User.PASSWORD_COLUMN)));
        cursor.close();
        return  result;
    }

    public static String passwordToHash(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.reset();
            messageDigest.update(password.getBytes());

            byte result[] = messageDigest.digest();
            password = getHexadecimalValue(result);
        } catch (NoSuchAlgorithmException e1) {
            e1.getMessage();
        }
        return password;
    }

    private static String getHexadecimalValue(byte[] result) {
        StringBuffer buf = new StringBuffer();
        for (byte bytes : result) {
            buf.append(String.format("%02x", bytes & 0xff));
        }
        return buf.toString();
    }
}
