package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

public class Registration extends AppCompatActivity {

    private UserService userService;
    private String login;
    private String password;
    private String privateKey;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        userService = new UserServiceImpl(this);
        if(ExtraResource.getCurrentUserRole().equals(Role.Patient)){
            Spinner spinner = (Spinner) findViewById(R.id.registrationRoleSpinner);
            spinner.setEnabled(false);
            spinner.setEnabled(true);
        }

    }

    public void saveNewUser(View view){
        try {
            EditText passwordText = (EditText) findViewById(R.id.registrationPasswordEditText);
            EditText confirmPasswordText = (EditText) findViewById(R.id.registrationConfirmPasswordEditText);
            String password = passwordText.getText().toString();
            String confirmPassword = confirmPasswordText.getText().toString();
            if (password.equals(confirmPassword)) {
                String login = ((EditText) findViewById(R.id.registrationLoginEditText)).getText().toString();
                if (login.isEmpty()) {
                    ExtraResource.showErrorDialog(R.string.error_field_required, Registration.this);
                } else if (!userService.isUserExist(login)) {
                    String name = ((EditText) findViewById(R.id.registrationNameEditText)).getText().toString();
                    String phone = ((EditText) findViewById(R.id.registrationPhoneEditText)).getText().toString();
                    Integer age = Integer.valueOf(((EditText) findViewById(R.id.registrationAgeEditText)).getText().toString());
                    Role role = Role.valueOf(((Spinner) findViewById(R.id.registrationRoleSpinner)).getSelectedItem().toString());
                    this.role = role.toString();
                    if (login.isEmpty() || name.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                        ExtraResource.showErrorDialog(R.string.error_field_required, Registration.this);
                    } else {
                        this.login = login;
                        this.password = password;
                        User user = new User(login, password, name, role, age, phone);
                        privateKey = userService.insertUserInDB(user);
                        Boolean isSuccess = !privateKey.isEmpty();
                        if (isSuccess) {
                            returnBack(view);
                        }
                    }
                } else {
                    ExtraResource.showErrorDialog(R.string.error_name_exist, Registration.this);
                }
            } else {
                passwordText.setText("");
                confirmPasswordText.setText("");
                ExtraResource.showErrorDialog(R.string.error_invalid_password, Registration.this);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void returnBack(View view) {
        Intent intent;
        if(!ExtraResource.getCurrentUserRole().equals(Role.Admin)) {
            intent = new Intent(this, Login.class);
        } else {
            intent = new Intent(this, ListOfUsersActivity.class);
        }
            intent.putExtra(ExtraResource.USER_LOGIN, this.login);
            intent.putExtra(ExtraResource.USER_PASSWORD, this.password);
            intent.putExtra(ExtraResource.USER_PRIVATE_KEY, this.privateKey);
            intent.putExtra(ExtraResource.USER_ROLE, this.role);
            startActivity(intent);
    }
}
