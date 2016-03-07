package com.example.mary.hospital.Service.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.mary.hospital.DatabaseHelper;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Role;
import com.example.mary.hospital.Service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final static String HASH_ALGORITHM = "SHA-256";
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sdb;

    public UserServiceImpl (Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void deleteTable() {
        sdb = databaseHelper.getWritableDatabase();
        sdb.execSQL("delete from " + User.DATABASE_TABLE);
        addAdmin(sdb);
        addDoctor(sdb);
        sdb.close();
    }

    public static void addAdmin(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(BaseColumns._ID, 1);
        cv.put(User.USER_NAME_COLUMN, "admin");
        cv.put(User.ROLE_COLUMN, Role.Admin.toString());
        cv.put(User.PASSWORD_COLUMN, UserServiceImpl.passwordToHash("admin"));
        db.insert(User.DATABASE_TABLE, null, cv);
    }

    public static void addDoctor(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(BaseColumns._ID, 2);
        cv.put(User.USER_NAME_COLUMN, "doctor");
        cv.put(User.ROLE_COLUMN, Role.Doctor.toString());
        cv.put(User.PASSWORD_COLUMN, UserServiceImpl.passwordToHash("doctor"));
        db.insert(User.DATABASE_TABLE, null, cv);
    }

    public void addUserInDB (User user) {
        sdb = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.USER_NAME_COLUMN, user.getName());
        values.put(User.PASSWORD_COLUMN, passwordToHash(user.getPassword()));
        user.setPassword("");
        values.put(User.PHONE_COLUMN, user.getPhone());
        values.put(User.AGE_COLUMN, user.getAge());
        values.put(User.ROLE_COLUMN, user.getRole().toString());
        sdb.insert(User.DATABASE_TABLE, null, values);
        sdb.close();
    }

    public Boolean isUserExist(String name) {
        sdb = databaseHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("Select " + User.USER_NAME_COLUMN + "  from " + User.DATABASE_TABLE
                                    + " where " + User.USER_NAME_COLUMN + "= ?", new String[]{name});
        Boolean result = cursor.moveToFirst();
        cursor.close();
        sdb.close();
        return result;
    }

    public Boolean isCorrectPassword(String name, String password) {
        password = passwordToHash(password);
        sdb = databaseHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("Select " + User.PASSWORD_COLUMN + " from " + User.DATABASE_TABLE
                                    + " where " + User.USER_NAME_COLUMN + "= ?", new String[]{name});
        cursor.moveToFirst();
        Boolean result = password.equals(cursor.getString(cursor.getColumnIndex(User.PASSWORD_COLUMN)));
        cursor.close();
        sdb.close();
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
        StringBuilder buf = new StringBuilder();
        for (byte bytes : result) {
            buf.append(String.format("%02x", bytes & 0xff));
        }
        return buf.toString();
    }

    public User getUserByName(String name) {
        sdb = databaseHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("Select " + User.USER_NAME_COLUMN + ", " + User.AGE_COLUMN + ", " + User.PHONE_COLUMN + ", " + User.ROLE_COLUMN
                                    + " from " + User.DATABASE_TABLE + " where " + User.USER_NAME_COLUMN + "= ?", new String[]{name});
        User user = new User();
        if (cursor.moveToFirst()) {
            user.setName(cursor.getString(cursor.getColumnIndex(User.USER_NAME_COLUMN)));
            user.setAge(cursor.getInt(cursor.getColumnIndex(User.AGE_COLUMN)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(User.PHONE_COLUMN)));
            user.setRole(Role.valueOf(cursor.getString(cursor.getColumnIndex(User.ROLE_COLUMN))));
        }
        cursor.close();
        sdb.close();
        return user;
    }

    public Role getUsersRole(String name) {
        Role role = null;
        sdb = databaseHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("Select " + User.ROLE_COLUMN + " from " + User.DATABASE_TABLE
                + " where " + User.USER_NAME_COLUMN + "= ?", new String[]{name});
        if (cursor.moveToFirst()) {
           role = Role.valueOf(cursor.getString(cursor.getColumnIndex(User.ROLE_COLUMN)));
        }
        cursor.close();
        sdb.close();
        return role;
    }

    public List<User> getAllUsers() {
        sdb = databaseHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("Select " + User.USER_NAME_COLUMN + ", " + User.AGE_COLUMN + ", " + User.PHONE_COLUMN
                                    + ", " + User.ROLE_COLUMN + " from " + User.DATABASE_TABLE, null);
        List<User> users = formListOfUsers(cursor);
        cursor.close();
        sdb.close();
        return users;
    }

    public List<User> getAllPatient() {
        sdb = databaseHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("Select " + User.USER_NAME_COLUMN + ", " + User.AGE_COLUMN + ", " + User.PHONE_COLUMN + ", " + User.ROLE_COLUMN
                                    + " from " + User.DATABASE_TABLE + " where " + User.ROLE_COLUMN + "= ?", new String[]{Role.Patient.toString()});
        List<User> users = formListOfUsers(cursor);
        cursor.close();
        sdb.close();
        return users;
    }

    private List<User> formListOfUsers(Cursor cursor) {
        List<User> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            User user = new User();
            user.setName(cursor.getString(cursor.getColumnIndex(User.USER_NAME_COLUMN)));
            user.setAge(cursor.getInt(cursor.getColumnIndex(User.AGE_COLUMN)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(User.PHONE_COLUMN)));
            try {
                user.setRole(Role.valueOf(cursor.getString(cursor.getColumnIndex(User.ROLE_COLUMN))));
            } catch (Exception e) {
                user.setRole(Role.Patient);
            }
            users.add(user);
        }
        return users;
    }
}
