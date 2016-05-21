package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mary.hospital.Adapters.ItemDiseaseAdapter;
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
//TODO Text area write in top

public class UserActivity extends AppCompatActivity {

    private UserService userService;
    private DiseaseHistoryService diseaseService;
    private List<DiseaseHistory> diseases;
    private List<String> diseaseNames;
    private int userID;//users id, that see
    private Role userRole;
    private int patientsDoctorID;// doctorID checked in listOfUsers patient
    private static int doctorID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userService = new UserServiceImpl(this);
        diseaseService = new DiseaseHistoryServiceImpl(this);
        getUserInfoAndFillFields();
        createAndRepaintListView();
        doctorID = getIntent().getIntExtra(ExtraResource.DOCTOR_ID, 0);
    }

    public void createAndRepaintListView(){
        Button addButton = (Button) findViewById(R.id.userDiseaseAddButton);
        ListView listView = (ListView) findViewById(R.id.userDiseaseListView);
        listView.setFocusable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentTemp = new Intent(view.getContext(), DiseaseActivity.class);
                intentTemp.putExtra(ExtraResource.PATIENT_ID, getIntent().getIntExtra(ExtraResource.PATIENT_ID, 0));
                int i = diseases.get(position).getId();
                intentTemp.putExtra(ExtraResource.DISEASE_ID, i);
                intentTemp.putExtra(ExtraResource.DOCTOR_ID, doctorID);
                if ((patientsDoctorID == userID) || (userRole == Role.Admin)) {
                    intentTemp.putExtra(ExtraResource.IS_EDITABLE, true);
                } else {
                    intentTemp.putExtra(ExtraResource.IS_EDITABLE, false);
                }
                startActivity(intentTemp);
            }
        });
        if(userRole.equals(Role.Admin) || (userID == patientsDoctorID)){
            listView.setAdapter(new ItemDiseaseAdapter(this, R.layout.item_list_of_disease, diseaseNames));
        } else {
            String[] str = new String[diseaseNames.size()];
            str = diseaseNames.toArray(str);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str));
            addButton.setVisibility(View.GONE);
        }
    }

    public void redirectToEditDisease(View view) {// addButton was clicked
        Intent IntentTemp = new Intent(this, DiseaseActivity.class);
        IntentTemp.putExtra(ExtraResource.PATIENT_ID, getIntent().getIntExtra(ExtraResource.PATIENT_ID, 0));
        IntentTemp.putExtra(ExtraResource.IS_EDITABLE, true);
        IntentTemp.putExtra(ExtraResource.DOCTOR_ID, doctorID);
        startActivity(IntentTemp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.certificatesPage:
                redirectToHomePage(CertificatesActivity.class);
                return true;
            case R.id.mainPage:
                redirectToHomePage(UserActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void redirectToHomePage(Class activityToRedirect) {
        Intent IntentTemp = new Intent(this, activityToRedirect);
        IntentTemp.putExtra(ExtraResource.PATIENT_ID, getIntent().getStringExtra(ExtraResource.PATIENT_ID));
        IntentTemp.putExtra(ExtraResource.PATIENT_LOGIN, getIntent().getStringExtra(ExtraResource.PATIENT_LOGIN));
        IntentTemp.putExtra(ExtraResource.CURRENT_DOCTOR_ID, getIntent().getStringExtra(ExtraResource.CURRENT_DOCTOR_ID));
        IntentTemp.putExtra(ExtraResource.USER_ROLE, getIntent().getStringExtra(ExtraResource.USER_ROLE));
        startActivity(IntentTemp);
    }

    private void getUserInfoAndFillFields(){
        userRole = ExtraResource.getCurrentUserRole();
        userID =  ExtraResource.getCurrentUserId();
        User user = userService.getUserById(getIntent().getIntExtra(ExtraResource.PATIENT_ID, 0));
        patientsDoctorID = user.getDoctorID();
        TextView textView = (TextView)findViewById(R.id.userInfoTextView);
        textView.setText(user.toString());
        diseases = diseaseService.getAllUsersHistories(user);
        diseaseNames = getTitles(diseases);
    }

    public static List<String> getTitles(List<DiseaseHistory> diseases) {
        List<String> diseaseNames = new ArrayList<>();
        if(diseases != null) {
            for (DiseaseHistory disease : diseases) {
                diseaseNames.add(disease.getTitle());
            }
        }
        return diseaseNames;
    }

    public void onBackPressed(){
        Intent intent;
        if(userRole.equals(Role.Patient)){
            intent = new Intent(UserActivity.this, Login.class);
        } else if(userRole.equals(Role.Doctor)) {
            intent = new Intent(UserActivity.this, ListOfUsersActivity.class);
        } else if(ExtraResource.lastActivity.equals("ListOfUsersActivity")){
            intent = new Intent(UserActivity.this, ListOfUsersActivity.class);
        } else {
            intent = new Intent(UserActivity.this, DoctorInfoActivity.class);
            intent.putExtra(ExtraResource.DOCTOR_ID, doctorID);
        }
        startActivity(intent);
    }
}

