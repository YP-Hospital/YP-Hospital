package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.Connection.Connector;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

public class Login extends AppCompatActivity {
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userService = new UserServiceImpl(this);
        fillNameAndPassword();
    }

    private void fillNameAndPassword() {
        Intent intent = getIntent();
        String name = intent.getStringExtra(ExtraResource.USER_LOGIN);
        String password = intent.getStringExtra(ExtraResource.USER_PASSWORD);
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
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if (!userService.isUserExist(name)) {
            ExtraResource.showErrorDialog(R.string.error_invalid_email, Login.this);
        } else if (!userService.isCorrectPassword(name, password)) {
            ExtraResource.showErrorDialog(R.string.error_incorrect_password, Login.this);
        } else {
            Role role = userService.getUsersRole(name);
            if (role == Role.Patient) {
                redirectToHomePage(name, role, UserActivity.class);
            } else {
                redirectToHomePage(name, role, ListOfUsersActivity.class);
            }
        }
    }

    private void redirectToHomePage(String name, Role role, Class activityToRedirect) {
        Intent IntentTemp = new Intent(this, activityToRedirect);
        IntentTemp.putExtra(ExtraResource.USER_LOGIN, name);
        IntentTemp.putExtra(ExtraResource.USER_ROLE, role.toString());
        startActivity(IntentTemp);
    }
}
