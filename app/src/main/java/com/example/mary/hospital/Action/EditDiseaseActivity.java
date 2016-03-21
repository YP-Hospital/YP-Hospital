package com.example.mary.hospital.Action;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.DiseaseHistoryService;
import com.example.mary.hospital.Service.Impl.DiseaseHistoryServiceImpl;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Grishalive on 21.03.2016.
 */
public class EditDiseaseActivity extends AppCompatActivity {
    private UserService userService;
    private DiseaseHistoryService diseaseService;
    private EditText diseaseName;
    private EditText openDate;
    private EditText closeDate;
    private EditText text;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_disease_history);
        userService = new UserServiceImpl(this);
        diseaseService = new DiseaseHistoryServiceImpl(this);
        diseaseName = ((EditText) findViewById(R.id.editDiseaseNameEditText));
        openDate = ((EditText) findViewById(R.id.editDiseaseOpenDateEditText));
        closeDate = ((EditText) findViewById(R.id.editDiseaseCloseDateEditText));
        text = ((EditText) findViewById(R.id.editDiseaseTextEditText));
    }
    public void saveDisease(View view){
        String diseaseNameS =  diseaseName.getText().toString();
        String openDateS = openDate.getText().toString();
        String closeDateS = closeDate.getText().toString();
        String textS = text.getText().toString();
        String id = getIntent().getStringExtra(ExtraResource.PATIENT_ID);
        Integer idi = Integer.parseInt(id);
        if(diseaseNameS.isEmpty() || openDateS.isEmpty() || closeDateS.isEmpty() || textS.isEmpty()){
            ExtraResource.showErrorDialog(R.string.error_name_exist, EditDiseaseActivity.this);
        } else if(isStringParsibleToDate(openDateS) && isStringParsibleToDate(closeDateS)){
            DiseaseHistory temp = new DiseaseHistory(diseaseNameS, parseStringToDate(openDateS),
                    parseStringToDate(closeDateS), textS, idi);
            diseaseService.addHistoryInDB(temp);
        } else {
            ExtraResource.showErrorDialog(R.string.error_field_required, this);
        }
    }
    private Boolean isStringParsibleToDate(String str){
        SimpleDateFormat format = new SimpleDateFormat(DiseaseHistory.DATE_FORMAT);
        try {
            Date date = format.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    private Date parseStringToDate(String str){
        SimpleDateFormat format = new SimpleDateFormat(DiseaseHistory.DATE_FORMAT);
        Date date;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }
}