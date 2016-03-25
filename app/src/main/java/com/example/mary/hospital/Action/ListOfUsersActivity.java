package com.example.mary.hospital.Action;
//check
import android.app.AlertDialog;
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
import android.widget.Toast;

import com.example.mary.hospital.Adapters.ItemAdapter;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

import java.util.ArrayList;
import java.util.List;
//TODO When click on some person open UserActivity with him
public class ListOfUsersActivity extends AppCompatActivity {

    private static UserService userService;
    private String userRole;
    private static String doctorID;
    private final String[] typeOfPatients = {"All","My"};
    private static ArrayList<User> users;
    private static ListView listView;
    private static Spinner spinner;
    private static ArrayList<String> names;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);
        userService = new UserServiceImpl(this);
        getUserRole();
        createAndRepaintListView(0);
        createSpinner();
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
                //TODO
                return true;
            case R.id.mainPage:
                //TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static void addUserToDoctor(int position){
        int temp = Integer.parseInt(doctorID);
        userService.setDoctorToUser(temp, users.get(position));
        listView.invalidate();
    }

    public static void deleteUserFromDoctor(int position){
        userService.deleteDoctorToUser(users.get(position));
        listView.invalidate();
    }

    public void createAndRepaintListView(int position){
        getUsersToDisplayAndFillTextsField(position);
        //Toast.makeText(getBaseContext(), "DoctID = " + " " + (getIntent().getStringExtra(ExtraResource.DOCTOR_ID)), Toast.LENGTH_SHORT).show();
            listView = (ListView) findViewById(R.id.listOfUsersListView);
            listView.setFocusable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(ListOfUsersActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
                    Intent IntentTemp = new Intent(view.getContext(), UserActivity.class);
                    IntentTemp.putExtra(ExtraResource.PATIENT_LOGIN, users.get(position).getLogin());
                    IntentTemp.putExtra(ExtraResource.USER_ROLE, userRole);
                    IntentTemp.putExtra(ExtraResource.CURRENT_DOCTOR_ID, doctorID);
                    IntentTemp.putExtra(ExtraResource.PATIENT_ID, users.get(position).getId().toString());
                    startActivity(IntentTemp);
                    //Toast.makeText(ListOfUsersActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
                }
            });
            listView.setAdapter(new ItemAdapter(this, R.layout.item_list_of_users, users, position, names, Integer.parseInt(doctorID)));
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
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @NonNull
    private void getUsersToDisplayAndFillTextsField(int position) {
        List<User> patients;
        names = new ArrayList<>();
        users = new ArrayList<>();
        if(!names.isEmpty())
            names.clear();
        if(!users.isEmpty())
            users.clear();
        if (userRole.equals(Role.Doctor.toString())) {
            patients = userService.getAllPatients();
        } else {
            patients = userService.getAllUsers();
        }
        for(User patient: patients)
            if(position == 0) {
                names.add(patient.getName());
                users.add(patient);
            } else {
                if (patient.getDoctorID().toString().equals(doctorID)) {
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
        outputText += getResources().getString((userRole.equals(Role.Doctor.toString())? R.string.Patients : R.string.Users));
        textview.setText(outputText);
    }

    private void getUserRole() {
        Intent intent = getIntent();
        userRole = intent.getStringExtra(ExtraResource.USER_ROLE);
        doctorID = intent.getStringExtra(ExtraResource.CURRENT_DOCTOR_ID);
    }
}
