package com.example.mary.hospital.Action;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mary.hospital.Adapters.ItemPatientsAdapter;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grishalive on 05.04.2016.
 */
public class ListOfPatientsActivity extends AppCompatActivity{

    private static UserService userService;
    private static int doctorID;
    private User doctor;
    private static List<User> patients;
    private List<String> patientsNames;
    private static ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates);
        userService = new UserServiceImpl(this);
        getUserInfoAndFillFields();
        createAndRepaintListView();
        createButton();
    }

    private void getUserInfoAndFillFields(){
        doctorID = getIntent().getIntExtra(ExtraResource.DOCTOR_ID, 0);
        doctor = userService.getUserById(doctorID);
        patients = userService.getAllPatients();
        TextView textView = (TextView)findViewById(R.id.certificatesTextView);
        textView.setText(R.string.patients);
        getNotDoctorsPatientsAndTheirNames();
    }

    private void getNotDoctorsPatientsAndTheirNames(){
        ArrayList<User> notDoctorsPatients = new ArrayList<>();
        patientsNames = new ArrayList<>();
        for(User patient: patients){
            if(!patient.getDoctorID().equals(doctorID)){
                notDoctorsPatients.add(patient);
                patientsNames.add(patient.getName());
            }
        }
        patients = notDoctorsPatients;
    }

    private void createButton(){
        Button ok = (Button) findViewById(R.id.certificatesButton);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
    }

    public static void addUserToDoctor(int position) {
        userService.setDoctorToUser(doctorID, patients.get(position));
        listView.invalidate();
    }

    public static void deleteUserFromDoctor(int position) {
        userService.deleteDoctorToUser(patients.get(position));
        listView.invalidate();
    }

    private void createAndRepaintListView(){
        listView = (ListView) findViewById(R.id.certificatesListView);
        listView.setFocusable(true);
        ArrayAdapter<String> adapter = new ItemPatientsAdapter(this, R.layout.item_list_of_users, patients, patientsNames, doctorID, "ListOfPatientsActivity");
        listView.setAdapter(adapter);
    }

    public void onBackPressed(){
        Intent intent = new Intent(ListOfPatientsActivity.this, DoctorInfoActivity.class);
        intent.putExtra(ExtraResource.DOCTOR_ID, doctorID);
        startActivity(intent);
    }

}
