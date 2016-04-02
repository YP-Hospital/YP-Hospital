package com.example.mary.hospital.Action;
//check
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mary.hospital.Adapters.ItemAdapter;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

import java.util.ArrayList;
import java.util.List;

public class ListOfUsersActivity extends AppCompatActivity {

    private static UserService userService;
    private final String[] typeOfPatients = {"All","My"};
    private static Role userRole;
    private static int doctorID;
    private static ArrayList<User> users;
    private static ArrayList<String> names;
    private static ListView listView;
    private static Spinner spinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);
        userService = new UserServiceImpl(this);
        getUserInfo();
        createAndRepaintListView(0);
        createSpinner();
    }

    public static void addUserToDoctor(int position){
        userService.setDoctorToUser(doctorID, users.get(position));
        listView.invalidate();
    }

    public static void deleteUserFromDoctor(int position){
        userService.deleteDoctorToUser(users.get(position));//TODO must delete users by id
        listView.invalidate();
    }

    public void createAndRepaintListView(int position){
        getUsersToDisplayAndFillTextField(position);
            listView = (ListView) findViewById(R.id.listOfUsersListView);
            listView.setFocusable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent IntentTemp = new Intent(view.getContext(), UserActivity.class);
                    IntentTemp.putExtra(ExtraResource.PATIENT_ID, users.get(position).getId());
                    startActivity(IntentTemp);
                }
            });
            listView.setAdapter(new ItemAdapter(this, R.layout.item_list_of_users, users, position, names, doctorID));
    }

    public void createSpinner(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOfPatients);
        spinnerAdapter.notifyDataSetChanged();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) findViewById(R.id.listOfUsersSpinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setPrompt(typeOfPatients[1]);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                createAndRepaintListView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @NonNull
    private void getUsersToDisplayAndFillTextField(int position) {
        List<User> patients;
        names = new ArrayList<>();
        users = new ArrayList<>();
        if(!names.isEmpty())
            names.clear();
        if(!users.isEmpty())
            users.clear();
       // if (userRole.equals(Role.Doctor)) {
            patients = userService.getAllPatients();
       // } else {
       //     patients = userService.getAllUsers();
       // }
        for(User patient: patients)
            if(position == 0) {
                names.add(patient.getName());
                users.add(patient);
            } else {
                if (patient.getDoctorID().equals(doctorID)) {//TODO create server method returns names of all patients and all doctors patients
                    names.add(patient.getName());
                    users.add(patient);
                }
            }
        setOutputText(names.size());
    }

    private void setOutputText(Integer size) {
        TextView textview = (TextView) findViewById(R.id.listOfUsersTextView);
        String outputText = "";
        if (size == 0) {
            outputText = getResources().getString(R.string.noUsers) + " ";
        }
        outputText += getResources().getString((userRole.equals(Role.Doctor)? R.string.Patients : R.string.Users));
        textview.setText(outputText);
    }

    private void getUserInfo() {
        userRole = ExtraResource.getCurrentUserRole();
        doctorID = ExtraResource.getCurrentUserId();//Because user - doctor
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.certificatesPage:
                redirectToHomePage(CertificatesActivity.class);
                return true;
            case R.id.mainPage:
                redirectToHomePage(ListOfUsersActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void redirectToHomePage(Class activityToRedirect) {
        Intent IntentTemp = new Intent(this, activityToRedirect);
        IntentTemp.putExtra(ExtraResource.USER_LOGIN, getIntent().getStringExtra(ExtraResource.USER_LOGIN));
        IntentTemp.putExtra(ExtraResource.CURRENT_DOCTOR_ID, getIntent().getStringExtra(ExtraResource.CURRENT_DOCTOR_ID));
        IntentTemp.putExtra(ExtraResource.USER_ID, getIntent().getStringExtra(ExtraResource.USER_ID));
        IntentTemp.putExtra(ExtraResource.USER_ROLE, getIntent().getStringExtra(ExtraResource.USER_ROLE));
        startActivity(IntentTemp);
    }
}
