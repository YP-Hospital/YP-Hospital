package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.R;
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
        EditText name = (EditText) findViewById(R.id.name);
        EditText password = (EditText) findViewById(R.id.password);
        if (!userService.isUserExist(name.getText().toString())) {
            showDialog(R.string.error, R.string.error_invalid_email, R.mipmap.error);
        }

    }

    private void showDialog(int title, int message, int error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(error)
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
