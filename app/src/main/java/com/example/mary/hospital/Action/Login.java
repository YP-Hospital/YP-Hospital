package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.Dialogs.DialogShowPrivateKey;
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
        showPrivateKey();
    }

    private void fillLoginAndPassword() {
        Intent intent = getIntent();
        String login = intent.getStringExtra(ExtraResource.USER_LOGIN);
        String password = intent.getStringExtra(ExtraResource.USER_PASSWORD);
        EditText loginText = (EditText) findViewById(R.id.loginLoginEditText);
        EditText passwordText = (EditText) findViewById(R.id.registrationPasswordEditText);
        loginText.setText(login);
        passwordText.setText(password);
    }

    private void showPrivateKey(){
        String key = getIntent().getStringExtra(ExtraResource.USER_PRIVATE_KEY);
        String role = getIntent().getStringExtra(ExtraResource.USER_ROLE);
        if(key != null && role.equals(Role.Doctor.toString())){
            AlertDialog dialog = DialogShowPrivateKey.getDialog(this, key);
            dialog.show();
        }
    }

    public void registration(View view) {
        Intent intent = new Intent(this, Registration.class);
        setCurrentUserPatient();
        startActivity(intent);
    }

    private void setCurrentUserPatient(){
        User user = new User();//need for registration
        user.setRole(Role.Patient);//
        ExtraResource.setCurrentUser(user);//
    }

    public void signIn(View view) {
        String login = ((EditText) findViewById(R.id.loginLoginEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.registrationPasswordEditText)).getText().toString();
        if (login.isEmpty() || password.isEmpty()) {
            ExtraResource.showErrorDialog(R.string.error_login_password_required, this);
            return;
        }
        User user = userService.signIn(login, password);
        if (user != null) {
            Role role = user.getRole();
            if (role == Role.Patient) {
                redirectToHomePage(user, UserActivity.class);
            } else {
                redirectToHomePage(user, ListOfUsersActivity.class);
            }
        }
    }

    private void redirectToHomePage(User user, Class activityToRedirect) {
        Intent IntentTemp = new Intent(this, activityToRedirect);
        IntentTemp.putExtra(ExtraResource.PATIENT_ID, user.getId());//for UserActivity
        startActivity(IntentTemp);
    }
}
