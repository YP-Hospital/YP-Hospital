package com.example.mary.hospital.Action;
//check
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.hospital.Adapters.ItemAdapter;
import com.example.mary.hospital.Dialogs.DialogAreYouSure;
import com.example.mary.hospital.Dialogs.DialogShowCertificate;
import com.example.mary.hospital.Dialogs.DialogShowPrivateKey;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class ListOfUsersActivity extends AppCompatActivity {

    private static UserService userService;
    private final String[] typeOfPatients = {"All", "My"};
    private final String[] typeOfUsers = {"Doctors", "Patients"};
    private static Role userRole;
    private static int userID;
    private static ArrayList<User> users;
    private static ArrayList<String> names;
    private static ListView listView;
    private static Spinner spinner;
    public Boolean isDeleted = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);
        userService = new UserServiceImpl(this);
        getUserInfo();
        getUsersToDisplayAndFillTextField(0);
        createAndRepaintListView(0);
        createSpinner();
        createButton();
        showPrivateKey();
    }

    private void createButton(){
        Button add = (Button) findViewById(R.id.listOfUsersButton);
        if(userRole.equals(Role.Admin)){
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ListOfUsersActivity.this, Registration.class));
                }
            });
        } else {
            add.setVisibility(View.GONE);
        }
    }

    public static void addUserToDoctor(int position, Context context) {
        if(userService.setDoctorToUser(userID, users.get(position))){
        } else {
            Toast.makeText(context, R.string.please_refresh_page, Toast.LENGTH_SHORT).show();
        }
        listView.invalidate();
    }

    public static void deleteUserFromDoctor(int position, Context context) {
        if(userService.deleteDoctorToUser(users.get(position))){
        } else {
            Toast.makeText(context, R.string.please_refresh_page, Toast.LENGTH_SHORT).show();
        }
        listView.invalidate();
    }

    public void createAndRepaintListView(final int positionSpinner) {
        listView = (ListView) findViewById(R.id.listOfUsersListView);
        listView.setFocusable(true);
        final ArrayAdapter<String> adapter;
        if (userRole.equals(Role.Doctor)) {
            adapter = new ItemAdapter(this, R.layout.item_list_of_users, users, positionSpinner, names, userID);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent IntentTemp = new Intent(view.getContext(), UserActivity.class);
                    IntentTemp.putExtra(ExtraResource.PATIENT_ID, users.get(position).getId());
                    startActivity(IntentTemp);
                }
            });
        } else {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent IntentTemp;
                    if (positionSpinner == 0) {
                        IntentTemp = new Intent(view.getContext(), DoctorInfoActivity.class);
                        IntentTemp.putExtra(ExtraResource.DOCTOR_ID, users.get(position).getId());
                    } else {
                        IntentTemp = new Intent(view.getContext(), UserActivity.class);
                        IntentTemp.putExtra(ExtraResource.PATIENT_ID, users.get(position).getId());
                    }
                    ExtraResource.lastActivity = "ListOfUsersActivity";
                    startActivity(IntentTemp);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                    String itemToDelete = arg0.getItemAtPosition(pos).toString();
                    showDialogAndDeleteUser(users.get(pos), adapter, itemToDelete, pos);
                    return true;
                }
            });
        }
    }

    private void showDialogAndDeleteUser(final User user, final ArrayAdapter<String> adapter, final String itemDelete, final int pos){
        String dialogText = getString(R.string.are_you_sure_that_you_want_to_delete_user) + ' ' + user.getName() + " ?";
        final AlertDialog dialog = DialogAreYouSure.getDialog(ListOfUsersActivity.this, dialogText);
        dialog.show();
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogEnterKeyCancelButton);
        Button okButton = (Button) dialog.findViewById(R.id.dialogEnterKeyOkButton);
        final int spinnerInt;
        if(user.getRole().equals(Role.Doctor))
            spinnerInt = 0;
        else
            spinnerInt = 1;
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userService.deleteUserFromDB(user.getId());
                users.remove(pos);
                names.remove(pos);
                dialog.dismiss();
                createAndRepaintListView(spinnerInt);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void createSpinner() {
        ArrayAdapter<String> spinnerAdapter;
        if (userRole.equals(Role.Doctor)) {
            spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOfPatients);
        } else {
            spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOfUsers);
        }
        spinnerAdapter.notifyDataSetChanged();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) findViewById(R.id.listOfUsersSpinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setPrompt(typeOfPatients[1]);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                getUsersToDisplayAndFillTextField(position);
                createAndRepaintListView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @NonNull
    private void getUsersToDisplayAndFillTextField(int position) {
        names = new ArrayList<>();
        users = new ArrayList<>();
        if (!names.isEmpty())
            names.clear();
        if (!users.isEmpty())
            users.clear();
        if (userRole.equals(Role.Doctor)) {
            getPatientsToDisplay(userService.getAllPatients(), position);
        } else {
            getUsersToDisplay(userService.getAllUsers(), position);
        }
        setOutputText(names.size());
    }

    private void getPatientsToDisplay(List<User> allUsers, int position) {
        for (User patient : allUsers) {
            if (position == 0) {
                names.add(patient.getName());
                users.add(patient);
            } else {
                if (patient.getDoctorID().equals(userID)) {
                    names.add(patient.getName());
                    users.add(patient);
                }
            }
        }
    }//for Doctor

    private void getUsersToDisplay(List<User> allUsers, int position) {
        for (User user : allUsers) {
            if (position == 0) {
                if (user.getRole().equals(Role.Doctor)) {
                    names.add(user.getName());
                    users.add(user);
                }
            } else {
                if (user.getRole().equals(Role.Patient)) {
                    names.add(user.getName());
                    users.add(user);
                }
            }
        }
    }//for Admin

    private void setOutputText(Integer size) {
        TextView textview = (TextView) findViewById(R.id.listOfUsersTextView);
        String outputText = "";
        if (size == 0) {
            outputText = getResources().getString(R.string.noUsers) + " ";
        }
        outputText += getResources().getString((userRole.equals(Role.Doctor) ? R.string.Patients : R.string.Users));
        textview.setText(outputText);
    }

    private void getUserInfo() {
        userRole = ExtraResource.getCurrentUserRole();
        userID = ExtraResource.getCurrentUserId();
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

    private void showPrivateKey(){
        String key = getIntent().getStringExtra(ExtraResource.USER_PRIVATE_KEY);
        String role  = getIntent().getStringExtra(ExtraResource.USER_ROLE);
        if(key != null && ExtraResource.getCurrentUserRole().equals(Role.Admin) && !role.equals(Role.Patient.toString())){
            AlertDialog dialog = DialogShowPrivateKey.getDialog(this, key);
            dialog.show();
        }
    }

    public void onBackPressed(){
        startActivity(new Intent(ListOfUsersActivity.this, Login.class));
    }
}
