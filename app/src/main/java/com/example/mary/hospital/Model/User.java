package com.example.mary.hospital.Model;

import android.provider.BaseColumns;

public class User implements BaseColumns {

    public static final String DATABASE_TABLE = "users";
    public static final String USER_NAME_COLUMN = "name";
    public static final String PHONE_COLUMN = "phone";
    public static final String AGE_COLUMN = "age";
    public static final String ROLE_COLUMN = "role";
    public static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + USER_NAME_COLUMN
            + " text not null, " + PHONE_COLUMN + " integer, " + AGE_COLUMN
            + " integer);";

    private String name;
    private String phone;
    private Integer age;
    private String role;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
