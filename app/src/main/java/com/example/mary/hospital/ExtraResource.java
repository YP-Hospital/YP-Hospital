package com.example.mary.hospital;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ExtraResource {
    public static final String USER_LOGIN = "com.example.mary.hospital.USER_LOGIN";
    public static final String USER_ID = "com.example.mary.hospital.USER_ID";
    public static final String CURRENT_DOCTOR_ID = "com.example.mary.hospital.CURRENT_DOCTOR_ID";
    public static final String DOCTOR_ID = "com.example.mary.hospital.DOCTOR_ID";
    public static final String PATIENT_ID = "com.example.mary.hospital.PATIENT_ID";
    public static final String DISEASE_ID = "com.example.mary.hospital.DISEASE_ID";
    public static final String USER_ROLE = "com.example.mary.hospital.USER_ROLE";
    public static final String USER_PASSWORD = "com.example.mary.hospital.USER_PASSWORD";
    public static final String USER_PRIVATE_KEY = "com.example.mary.hospital.USER_PRIVATE_KEY";
    public static final String STOP_WORDS = "This is a stop message";
    public static final String separatorForSending = "][";


    public static void showErrorDialog(int message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.error)
                .setMessage(message)
                .setIcon(R.mipmap.error)
                .setCancelable(false)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
