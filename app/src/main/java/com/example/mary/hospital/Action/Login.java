package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.User;
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
        fillLoginAndPassword();
    }

    private void fillLoginAndPassword() {
        Intent intent = getIntent();
        String login = intent.getStringExtra(ExtraResource.USER_LOGIN);
        String password = intent.getStringExtra(ExtraResource.USER_PASSWORD);
        EditText loginText = (EditText) findViewById(R.id.login);
        EditText passwordText = (EditText) findViewById(R.id.password);
        loginText.setText(login);
        passwordText.setText(password);
    }

    public void registration(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    public void signIn(View view) {
        String login = ((EditText) findViewById(R.id.login)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if (login.isEmpty() || password.isEmpty()) {
            ExtraResource.showErrorDialog(R.string.error_login_password_required, this);
            return;
        }
        User user = userService.signIn(login, password);
        if (user != null) {
            Role role = user.getRole();
            if (role == Role.Patient) {
                redirectToHomePage(login, role, UserActivity.class);
            } else {
                redirectToHomePage(login, role, ListOfUsersActivity.class);
            }
        }
    }

    private void redirectToHomePage(String login, Role role, Class activityToRedirect) {
        Intent IntentTemp = new Intent(this, activityToRedirect);
        IntentTemp.putExtra(ExtraResource.USER_LOGIN, login);
        IntentTemp.putExtra(ExtraResource.CURRENT_DOCTOR_LOGIN, userService.getUserByLogin(login).getDoctorID().toString());
        IntentTemp.putExtra(ExtraResource.DOCTOR_ID, userService.getUserByLogin(login).getId().toString());
        IntentTemp.putExtra(ExtraResource.USER_ROLE, role.toString());
        startActivity(IntentTemp);
    }
}
