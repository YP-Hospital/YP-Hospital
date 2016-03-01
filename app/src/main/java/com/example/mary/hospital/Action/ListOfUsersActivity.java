package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Role;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

import java.util.ArrayList;
import java.util.List;

public class ListOfUsersActivity extends AppCompatActivity {

    private UserService userService;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserServiceImpl(this);
        setContentView(R.layout.activity_list_of_users);
        ListView listView = (ListView)findViewById(R.id.listView);
        List<String> names = new ArrayList<String>();
        Intent intent = getIntent();
        String role = intent.getStringExtra(Login.USER_ROLE);
        List<User> patients;
        if (role.equals(Role.Doctor.toString())) {
            patients = userService.getAllPatient();
        } else {
            patients = userService.getAllUsers();
        }
        for(User i: patients)
                names.add(i.getName());
        String[] str = new String[names.size()];
        str = names.toArray(str);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str);
        listView.setAdapter(adapter);
    }
}
