package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Role;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

import java.util.List;

public class Login extends AppCompatActivity {
    public static final String USER_LOGIN = "com.example.mary.hospital.USER_LOGIN";
    public static final String USER_ROLE = "com.example.mary.hospital.USER_ROLE";
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userService = new UserServiceImpl(this);
    }

    public void registration(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    public void signIn(View view) {
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if (!userService.isUserExist(name)) {
            showErrorDialog(R.string.error_invalid_email, Login.this);
        } else if (!userService.isCorrectPassword(name, password)) {
            showErrorDialog(R.string.error_incorrect_password, Login.this);
        } else {
            Role role = userService.getUsersRole(name);
            if(role == Role.Patient) {
                redirectToHomePage(name, role, UserActivity.class);
            } else {
                redirectToHomePage(name, role, ListOfUsersActivity.class);
            }
        }
    }

    private void redirectToHomePage(String name, Role role, Class activityToRedirect) {
        Intent IntentTemp = new Intent(this, activityToRedirect);
        IntentTemp.putExtra(USER_LOGIN, name);
        IntentTemp.putExtra(USER_ROLE, role.toString());
        startActivity(IntentTemp);
    }

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
