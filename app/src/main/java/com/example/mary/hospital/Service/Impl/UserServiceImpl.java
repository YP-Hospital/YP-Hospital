package com.example.mary.hospital.Service.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.mary.hospital.Connection.Connector;
import com.example.mary.hospital.DatabaseHelper;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserServiceImpl implements UserService {
    private final static String HASH_ALGORITHM = "SHA-256";
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sdb;
    private Context context;

    public UserServiceImpl (Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);//TODO remove it!
    }

    public Boolean addUserInDB (User user) {
        Boolean isSuccess = false;
        try {
            user.setPassword(passwordToHash(user.getPassword()));
           isSuccess = Boolean.parseBoolean(new Connector(context).execute(user.getStringToInsertInServer()).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public Boolean isUserExist(String login) {
        String result = "";
        try {
            result = new Connector(context).execute("select users id where login " + login).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Boolean.parseBoolean(result.split(" ")[0]);
    }

    public User logIn(String login, String password) {
        String answerFromServer = "";
        try {
            answerFromServer = new Connector(context).execute("select users password role where login " + login).get();
            if (answerFromServer.equals("false")) {
                ExtraResource.showErrorDialog(R.string.error_name_exist, context);
                return null;
            } else {
                List<User> users = stringToUsers(answerFromServer);
                if (!users.get(0).getPassword().equals(passwordToHash(password))) {
                    ExtraResource.showErrorDialog(R.string.error_invalid_password, context);
                    return null;
                }
                return users.get(0);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<User> stringToUsers(String answerFromServer) {
        List<User> users = new ArrayList<>();
        List<String> words = new ArrayList<>(Arrays.asList(answerFromServer.split(" ")));
        Boolean isAllFields = words.get(1).equals("*");
        if (isAllFields) {
            for (int i = 3; i < words.size(); i++) {
                users.add(new User(words.get(i++), words.get(i++), words.get(i++), Role.valueOf(words.get(i++)), Integer.valueOf(words.get(i++)), words.get(i++)));
            }
        } else {
            getUsers(users, words);
        }
        return users;
    }
    /**Warning! GOVNOKOD*/
    private void getUsers(List<User> users, List<String> words) {
        Boolean isLogin = false, isPassword = false, isName = false, isRole = false, isAge = false, isPhone = false;
        int i;
        for (i = 1; !words.get(i).equals("0."); i++) {
            switch (words.get(i)) {
                case "login":
                    isLogin = true;
                    break;
                case "password":
                    isPassword = true;
                    break;
                case "name":
                    isName = true;
                    break;
                case "role":
                    isRole = true;
                    break;
                case "age":
                    isAge = true;
                    break;
                case "phone":
                    isPhone = true;
                    break;
            }
        }
        User user = null;
        for (;  i<words.size() ; i++) {
            if (words.get(i).matches("[0-9]+.")) {
                user = new User();
                continue;
            }
            if (isLogin) {
                user.setLogin(words.get(i++));
            }
            if (isPassword) {
                user.setPassword(words.get(i++));
            }
            if (isName) {
                user.setName(words.get(i++));
            }
            if (isRole) {
                user.setRole(Role.valueOf(words.get(i++)));
            }
            if (isAge) {
                user.setAge(Integer.valueOf(words.get(i++)));
            }
            if (isPhone) {
                user.setPhone(words.get(i++));
            }
            users.add(user);
        }
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

    public User getUserByLogin(String login) {
        String result = "";
        User user = null;
//        sdb = databaseHelper.getReadableDatabase();
//        Cursor cursor = sdb.rawQuery("Select " + User.USER_NAME_COLUMN + ", " + User.AGE_COLUMN + ", " + User.PHONE_COLUMN + ", " + User.ROLE_COLUMN
//                                    + " from " + User.DATABASE_TABLE + " where " + User.USER_NAME_COLUMN + "= ?", new String[]{name});
//        User user = new User();
//        if (cursor.moveToFirst()) {
//            user.setName(cursor.getString(cursor.getColumnIndex(User.USER_NAME_COLUMN)));
//            user.setAge(cursor.getInt(cursor.getColumnIndex(User.AGE_COLUMN)));
//            user.setPhone(cursor.getString(cursor.getColumnIndex(User.PHONE_COLUMN)));
//            user.setRole(Role.valueOf(cursor.getString(cursor.getColumnIndex(User.ROLE_COLUMN))));
//        }
//        cursor.close();
//        sdb.close();
        try {
            result = new Connector(context).execute("select users * where login " + login).get();
            user = stringToUsers(result).get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
