package com.example.mary.hospital.Service.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.mary.hospital.DatabaseHelper;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserServiceImpl implements UserService {
    public String HASH_ALGORITHM = "SHA-256";
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
        return null;
    }

    public Boolean isCorrectPassword(String name, String password) {
        return null;
    }

    public String passwordToHash(String password) {
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

    private String getHexadecimalValue(byte[] result) {
        StringBuffer buf = new StringBuffer();
        for (byte bytes : result) {
            buf.append(String.format("%02x", bytes & 0xff));
        }
        return buf.toString();
    }
}
