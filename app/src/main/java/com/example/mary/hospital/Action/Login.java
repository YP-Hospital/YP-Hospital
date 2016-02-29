package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.R;
import com.example.mary.hospital.Role;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

public class Login extends AppCompatActivity {
    public static final String USER_LOGIN = "com.example.mary.hospital.USER_LOGIN";
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userService = new UserServiceImpl(this);
    }

    public void signIn(View view) {
        Intent intent = new Intent(this, Login.class); //TODO Change to home activity, when it will be created
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if (!userService.isUserExist(name)) {
            showErrorDialog(R.string.error_invalid_email);
        } else if (!userService.isCorrectPassword(name, password)) {
            showErrorDialog(R.string.error_incorrect_password);
        } else {
            intent.putExtra(USER_LOGIN, name);
            startActivity(intent);
        }
    }

    private void showErrorDialog(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
