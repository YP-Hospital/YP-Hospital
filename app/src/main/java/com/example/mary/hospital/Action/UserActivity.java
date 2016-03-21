package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;


public class UserActivity extends AppCompatActivity {

    private UserService userService;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserServiceImpl(this);
        setContentView(R.layout.activity_user);
        User user = userService.getUserByLogin(getIntent().getStringExtra(ExtraResource.USER_LOGIN));
        TextView textView = (TextView)findViewById(R.id.userInfoTextView);
        textView.setText(user.toString());
    }

    public void redirectToEditDisease(View view) {
        Intent IntentTemp = new Intent(this, EditDiseaseActivity.class);
        IntentTemp.putExtra(ExtraResource.PATIENT_ID, getIntent().getStringExtra(ExtraResource.PATIENT_ID));
        startActivity(IntentTemp);
    }

}

