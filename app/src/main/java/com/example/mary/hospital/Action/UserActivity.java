package com.example.mary.hospital.Action;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

public class UserActivity extends AppCompatActivity {

    private UserService userService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserServiceImpl(this);
        setContentView(R.layout.activity_user);
        User user = userService.getUserByName(getIntent().getStringExtra(Login.USER_LOGIN));
        TextView textView = (TextView)findViewById(R.id.textView3);
        textView.setText(user.toString());
    }
}
