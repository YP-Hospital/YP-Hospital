package com.example.mary.hospital.Action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.hospital.Adapters.ItemPatientsAdapter;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.DiseaseHistoryService;
import com.example.mary.hospital.Service.Impl.DiseaseHistoryServiceImpl;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grishalive on 04.04.2016.
 */
public class DoctorInfoActivity extends AppCompatActivity {
    private static UserService userService;
    private static int doctorID;
    private User doctor;
    private static List<User> patients;
    private List<String> patientsNames;
    private static ListView listView;
    private static Toast toast;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userService = new UserServiceImpl(this);
        getUserInfoAndFillFields();
        createAndRepaintListView();
        createAddButton();
    }

    private void getUserInfoAndFillFields() {
        doctorID = getIntent().getIntExtra(ExtraResource.DOCTOR_ID, 0);
        doctor = userService.getUserById(doctorID);
        patients  = userService.getPatientsByDoctor(doctorID);
        getTitles();
        TextView textViewInfo = (TextView) findViewById(R.id.userInfoTextView);
        TextView textViewIDisease = (TextView) findViewById(R.id.userTextTextView);
        textViewInfo.setText(doctor.toString());
        textViewIDisease.setText(R.string.patients);
    }

    private void getTitles(){
        patientsNames = new ArrayList<>();
        for(User patient: patients){
            patientsNames.add(patient.getName());
        }
    }

    private void createAndRepaintListView(){
        listView = (ListView) findViewById(R.id.userDiseaseListView);
        listView.setFocusable(true);
        ArrayAdapter<String> adapter = new ItemPatientsAdapter(this, R.layout.item_list_of_users, patients, patientsNames, doctorID, "DoctorInfoActivity");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent IntentTemp = new Intent(view.getContext(), UserActivity.class);
                IntentTemp.putExtra(ExtraResource.PATIENT_ID, patients.get(position).getId());
                IntentTemp.putExtra(ExtraResource.DOCTOR_ID, doctorID);
                ExtraResource.lastActivity = "DoctorInfoActivity";
                startActivity(IntentTemp);
            }
        });
    }

    public static void addUserToDoctor(int position, Context context) {
        if(userService.setDoctorToUser(doctorID, patients.get(position))){
        } else {
            toast.makeText(context,"Please, refresh page, this user have a doctor", Toast.LENGTH_SHORT);
        }
        listView.invalidate();
    }

    public static void deleteUserFromDoctor(int position, Context context) {
        if(userService.deleteDoctorToUser(patients.get(position))){

        } else {
            toast.makeText(context,"Please, refresh page, this user have a doctor", Toast.LENGTH_SHORT);
        }
        listView.invalidate();
    }

    private void createAddButton(){
        Button add = (Button) findViewById(R.id.userDiseaseAddButton);
        add.setText(R.string.add_patient_to_doctor);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntentTemp = new Intent(DoctorInfoActivity.this, ListOfPatientsActivity.class);
                IntentTemp.putExtra(ExtraResource.DOCTOR_ID, doctorID);
                startActivity(IntentTemp);
            }
        });
    }

    public void onBackPressed(){
        startActivity(new Intent(DoctorInfoActivity.this, ListOfUsersActivity.class));
    }
}
