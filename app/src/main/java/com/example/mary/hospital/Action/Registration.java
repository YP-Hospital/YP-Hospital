package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Role;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

public class Registration extends AppCompatActivity {

    public static final String USER_LOGIN = "com.example.mary.hospital.USER_LOGIN";
    public static final String USER_PASSWORD = "com.example.mary.hospital.USER_PASSWORD";

    private UserService userService;
    private String currentUserName;
    private String name;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Spinner spinner = (Spinner) findViewById(R.id.role);
        Intent intent = getIntent();
        currentUserName = intent.getStringExtra(Login.USER_LOGIN);
        if (currentUserName == null) {
            spinner.setClickable(false);
            spinner.setEnabled(false);
        }
        userService = new UserServiceImpl(this);
    }

    public void saveNewUser(View view){
        try {
            EditText passwordText = (EditText) findViewById(R.id.password);
            EditText confirmPasswordText = (EditText) findViewById(R.id.confirmPassword);
            String password = passwordText.getText().toString();
            String confirmPassword = confirmPasswordText.getText().toString();
            if (password.equals(confirmPassword)) {
                String name = ((EditText) findViewById(R.id.name)).getText().toString();
                if (!userService.isUserExist(name)) {
                    String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
                    Integer age = Integer.valueOf(((EditText) findViewById(R.id.age)).getText().toString());
                    Role role = Role.valueOf(((Spinner) findViewById(R.id.role)).getSelectedItem().toString());
                    if (name.isEmpty() || password.isEmpty() || phone.isEmpty()
                            || age == null || role == null) {
                        Login.showErrorDialog(R.string.error_field_required, Registration.this);
                    } else {
                        this.name = name;
                        this.password = password;
                        User user = new User(name, password, phone, age, role);
                        userService.addUserInDB(user);
                        returnBack(view);
                    }
                } else {
                    Login.showErrorDialog(R.string.error_name_exist, Registration.this);
                }
            } else {
                passwordText.setText("");
                confirmPasswordText.setText("");
                Login.showErrorDialog(R.string.error_invalid_password, Registration.this);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void returnBack(View view) {
        if (currentUserName == null) {
            Intent intent = new Intent(this, Login.class);
            intent.putExtra(USER_LOGIN, this.name);
            intent.putExtra(USER_PASSWORD, this.password);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, Login.class);//TODO Change on activity, where is button will be
            startActivity(intent);
        }
    }
}
