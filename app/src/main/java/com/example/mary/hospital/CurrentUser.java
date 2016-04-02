package com.example.mary.hospital;

import com.example.mary.hospital.Model.Role;
//
/**
 * Created by Grishalive on 25.03.2016.
 */
public class CurrentUser {
    private static String userName;
    private static int userID;
    private static String userLogin;
    private static Role userRole;
    private static int usersDoctorID;// doctorID of the current user

    public CurrentUser(String userName, int userID, String userLogin, Role userRole, int usersDoctorID) {
        this.userName = userName;
        this.userID = userID;
        this.userLogin = userLogin;
        this.userRole = userRole;
        this.usersDoctorID = usersDoctorID;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        CurrentUser.userName = userName;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        CurrentUser.userID = userID;
    }

    public static String getUserLogin() {
        return userLogin;
    }

    public static void setUserLogin(String userLogin) {
        CurrentUser.userLogin = userLogin;
    }

    public static Role getUserRole() {
        return userRole;
    }

    public static void setUserRole(Role userRole) {
        CurrentUser.userRole = userRole;
    }

    public static int getUsersDoctorID() {
        return usersDoctorID;
    }

    public static void setUsersDoctorID(int usersDoctorID) {
        CurrentUser.usersDoctorID = usersDoctorID;
    }
}
