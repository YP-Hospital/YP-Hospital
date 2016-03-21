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

import com.example.mary.hospital.Adapters.ItemDiseaseAdapter;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.DiseaseHistoryService;
import com.example.mary.hospital.Service.Impl.DiseaseHistoryServiceImpl;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

import java.util.ArrayList;
import java.util.List;


public class UserActivity extends AppCompatActivity {

    private UserService userService;
    private DiseaseHistoryService diseaseService;
    private List<DiseaseHistory> diseases;
    private List<String> diseaseNames;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserServiceImpl(this);
        diseaseService = new DiseaseHistoryServiceImpl(this);
        setContentView(R.layout.activity_user);
        User user = userService.getUserByLogin(getIntent().getStringExtra(ExtraResource.USER_LOGIN));
        TextView textView = (TextView)findViewById(R.id.userInfoTextView);
        textView.setText(user.toString());
        listView = (ListView) findViewById(R.id.userDiseaseListView);
        String str = getIntent().getStringExtra(ExtraResource.PATIENT_ID);
       // diseases = diseaseService.getAllUsersHistories(user);
        diseaseNames = diseaseService.getTitlesOfAllUsersHistories(user);
       // if(diseases == null || diseases.isEmpty()) {
        //    diseaseNames.clear();
        //}
        createAndRepaintListView();
    }

    public void createAndRepaintListView(){
        listView.setFocusable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UserActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
                //Intent IntentTemp = new Intent(view.getContext(), UserActivity.class);

                //startActivity(IntentTemp);
            }
        });
        listView.setAdapter(new ItemDiseaseAdapter(this, R.layout.item_list_of_disease, diseaseNames));
    }

    public void redirectToEditDisease(View view) {
        Intent IntentTemp = new Intent(this, EditDiseaseActivity.class);
        IntentTemp.putExtra(ExtraResource.PATIENT_ID, getIntent().getStringExtra(ExtraResource.PATIENT_ID));
        IntentTemp.putExtra(ExtraResource.USER_LOGIN, getIntent().getStringExtra(ExtraResource.USER_LOGIN));
        startActivity(IntentTemp);
    }

}

