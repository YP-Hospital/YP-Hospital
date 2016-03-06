package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.annotation.NonNull;
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
    private String userRole;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);

        userService = new UserServiceImpl(this);

        ListView listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = getUsersToDisplay();
        listView.setAdapter(adapter);
    }

    @NonNull
    private ArrayAdapter<String> getUsersToDisplay() {
        List<String> names = new ArrayList<String>();
        getUserRole();
        List<User> patients;
        if (userRole.equals(Role.Doctor.toString())) {
            patients = userService.getAllPatient();
        } else {
            patients = userService.getAllUsers();
        }
        for(User patient: patients) {
            names.add(patient.getName());
        }
        String[] str = new String[names.size()];
        str = names.toArray(str);
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str);
    }

    private void getUserRole() {
        Intent intent = getIntent();
        userRole = intent.getStringExtra(Login.USER_ROLE);
    }
}
