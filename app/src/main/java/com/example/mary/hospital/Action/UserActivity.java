package com.example.mary.hospital.Action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.List;
//TODO Text area write in top

public class UserActivity extends AppCompatActivity {

    private UserService userService;
    private DiseaseHistoryService diseaseService;
    private List<DiseaseHistory> diseases;
    private List<String> diseaseNames;
    private ListView listView;
    private String userRole;
    private String userDoctorID;//
    private String currentDoctorID;//doctors id, that see
    private Button addButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserServiceImpl(this);
        diseaseService = new DiseaseHistoryServiceImpl(this);
        userRole = getIntent().getStringExtra(ExtraResource.USER_ROLE);
        setContentView(R.layout.activity_user);
        addButton = (Button) findViewById(R.id.userDiseaseAddButton);
        User user = userService.getUserByLogin(getIntent().getStringExtra(ExtraResource.USER_LOGIN));//TODO исправить, с логина посылаю одно, с листа другое
        userDoctorID = user.getDoctorID().toString();
        currentDoctorID =  getIntent().getStringExtra(ExtraResource.CURRENT_DOCTOR_ID);
        TextView textView = (TextView)findViewById(R.id.userInfoTextView);
        textView.setText(user.toString());
        listView = (ListView) findViewById(R.id.userDiseaseListView);
        diseases = diseaseService.getAllUsersHistories(user);
        diseaseNames = diseaseService.getTitlesOfAllUsersHistories(user);
        createAndRepaintListView();
    }

    public void createAndRepaintListView(){
        listView.setFocusable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UserActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
                Intent intentTemp = new Intent(view.getContext(), DiseaseActivity.class);
                intentTemp.putExtra(ExtraResource.PATIENT_LOGIN, getIntent().getStringExtra(ExtraResource.PATIENT_LOGIN));
                intentTemp.putExtra(ExtraResource.PATIENT_ID, getIntent().getStringExtra(ExtraResource.PATIENT_ID));
                int i = diseases.get(position).getId();
                intentTemp.putExtra(ExtraResource.DISEASE_ID, i);
                if (userDoctorID.equals(currentDoctorID)) {
                    intentTemp.putExtra(ExtraResource.IS_EDITABLE, "true");
                } else {
                    intentTemp.putExtra(ExtraResource.IS_EDITABLE, "false");
                }
                intentTemp.putExtra(ExtraResource.CURRENT_DOCTOR_ID, currentDoctorID);
                startActivity(intentTemp);
            }
        });
        if(userRole.equals(Role.Patient.toString()) || !currentDoctorID.equals(userDoctorID)){
            String[] str = new String[diseaseNames.size()];
            str = diseaseNames.toArray(str);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str));
            addButton.setVisibility(View.GONE);
        } else {
            listView.setAdapter(new ItemDiseaseAdapter(this, R.layout.item_list_of_disease, diseaseNames));
        }
    }

    public void redirectToEditDisease(View view) {
        Intent IntentTemp = new Intent(this, DiseaseActivity.class);
        IntentTemp.putExtra(ExtraResource.PATIENT_ID, getIntent().getStringExtra(ExtraResource.PATIENT_ID));
        IntentTemp.putExtra(ExtraResource.PATIENT_LOGIN, getIntent().getStringExtra(ExtraResource.PATIENT_LOGIN));
        IntentTemp.putExtra(ExtraResource.CURRENT_DOCTOR_ID, getIntent().getStringExtra(ExtraResource.CURRENT_DOCTOR_ID));
        IntentTemp.putExtra(ExtraResource.USER_ROLE, getIntent().getStringExtra(ExtraResource.USER_ROLE));
        IntentTemp.putExtra(ExtraResource.IS_EDITABLE, "true");
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
}

