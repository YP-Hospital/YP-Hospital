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
        Intent intent = getIntent();
        String name = intent.getStringExtra(Registration.USER_LOGIN);
        String password = intent.getStringExtra(Registration.USER_PASSWORD);
        EditText nameText = (EditText) findViewById(R.id.name);
        EditText passwordText = (EditText) findViewById(R.id.password);
        nameText.setText(name);
        passwordText.setText(password);
    }
    public void registration(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    public void signIn(View view) {
        Intent intent = new Intent(this, Login.class); //TODO Change to home activity, when it will be created
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if (!userService.isUserExist(name)) {
            showErrorDialog(R.string.error_invalid_email, Login.this);
        } else if (!userService.isCorrectPassword(name, password)) {
            showErrorDialog(R.string.error_incorrect_password, Login.this);
        } else {
            Role role = userService.getUserByName(name).getRole();
            if(role == Role.Patient) {
                Intent IntentTemp = new Intent(this, UserActivity.class);
                IntentTemp.putExtra(USER_LOGIN, name);
                startActivity(IntentTemp);
            } else {
                Intent IntentTemp = new Intent(this, ListOfUsersActivity.class);
                IntentTemp.putExtra(USER_LOGIN, name);
                IntentTemp.putExtra(USER_ROLE, role.toString());
                startActivity(IntentTemp);
            }
            //intent.putExtra(USER_LOGIN, name);
            //startActivity(intent);
        }
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
