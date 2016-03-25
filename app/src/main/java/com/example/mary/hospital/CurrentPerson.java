package com.example.mary.hospital;

/**
 * Created by Grishalive on 25.03.2016.
 */
public class CurrentPerson {
    static String patientID;
    static String userLogin;
    static String patientLogin;
    static String userRole;

    public static String getCurrentDoctorID() {
        return currentDoctorID;
    }

    public static void setCurrentDoctorID(String currentDoctorID) {
        CurrentPerson.currentDoctorID = currentDoctorID;
    }

    static String currentDoctorID;

    public static String getPatientID() {
        return patientID;
    }

    public static void setPatientID(String patientID) {
        CurrentPerson.patientID = patientID;
    }

    public static String getUserLogin() {
        return userLogin;
    }

    public static void setUserLogin(String userLogin) {
        CurrentPerson.userLogin = userLogin;
    }

    public static String getPatientLogin() {
        return patientLogin;
    }

    public static void setPatientLogin(String patientLogin) {
        CurrentPerson.patientLogin = patientLogin;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static void setUserRole(String userRole) {
        CurrentPerson.userRole = userRole;
    }
}
