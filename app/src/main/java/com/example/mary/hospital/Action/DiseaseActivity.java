package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.DiseaseHistoryService;
import com.example.mary.hospital.Service.Impl.DiseaseHistoryServiceImpl;
import com.example.mary.hospital.Service.Impl.UserServiceImpl;
import com.example.mary.hospital.Service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Grishalive on 21.03.2016.
 */
public class DiseaseActivity extends AppCompatActivity {
    private UserService userService;
    private static DiseaseHistoryService diseaseService;
    private Integer currentHistoryID;
    private static DiseaseHistory currentHistory;
    private SimpleDateFormat format;
    private List<DiseaseHistory> diseases;
    private List<String> diseaseNames;
    private static EditText diseaseName;
    private static EditText openDate;
    private static EditText closeDate;
    private static EditText text;
    private static String doctorName;
    private static Boolean isInserted = true;
    private static String currentDoctorName;
    String historyOwnerPatientID;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_disease_history);
        userService = new UserServiceImpl(this);
        diseaseService = new DiseaseHistoryServiceImpl(this);
        format = new SimpleDateFormat(DiseaseHistory.DATE_FORMAT);
        currentHistoryID = getIntent().getIntExtra(ExtraResource.DISEASE_ID, 0);
        diseaseName = ((EditText) findViewById(R.id.editDiseaseNameEditText));
        openDate = ((EditText) findViewById(R.id.editDiseaseOpenDateEditText));
        closeDate = ((EditText) findViewById(R.id.editDiseaseCloseDateEditText));
        text = ((EditText) findViewById(R.id.editDiseaseTextEditText));
        historyOwnerPatientID = getIntent().getStringExtra(ExtraResource.PATIENT_ID);
        String doctorID = getIntent().getStringExtra(ExtraResource.CURRENT_DOCTOR_ID);
        if (currentHistoryID != 0) {
            fillFields();
        }
        User user = userService.getUserByLogin(getIntent().getStringExtra(ExtraResource.PATIENT_LOGIN));
        diseases = diseaseService.getAllUsersHistories(user);
        diseaseNames = diseaseService.getTitlesOfAllUsersHistories(user);
    }

    private void fillFields() {
        currentHistory = diseaseService.getHistoryById(currentHistoryID);
        diseaseName.setText(currentHistory.getTitle());
        openDate.setText(format.format(currentHistory.getOpenDate()));
        closeDate.setText(format.format(currentHistory.getCloseDate()));
        text.setText(currentHistory.getText());
    }

    public void saveDisease(View view) throws InterruptedException {
        String diseaseNameS =  diseaseName.getText().toString();
        String openDateS = openDate.getText().toString();
        String closeDateS = closeDate.getText().toString();
        String textS = text.getText().toString();
        Integer idi = Integer.parseInt(historyOwnerPatientID);
        if(diseaseNameS.isEmpty() || openDateS.isEmpty() || closeDateS.isEmpty() || textS.isEmpty()){
            ExtraResource.showErrorDialog(R.string.error_name_exist, DiseaseActivity.this);
        } else if(isStringParsibleToDate(openDateS) && isStringParsibleToDate(closeDateS)){
            currentHistory = new DiseaseHistory(diseaseNameS, parseStringToDate(openDateS),
                    parseStringToDate(closeDateS), textS, idi, currentDoctorName);
                AlertDialog dialog = DialogEnterPrivateKey.getDialog(DiseaseActivity.this);
                    dialog.show();
            if(isInserted){
                //Intent IntentTemp = new Intent(this, DiseaseActivity.class);
                //IntentTemp.putExtra(ExtraResource.PATIENT_ID, getIntent().getStringExtra(ExtraResource.PATIENT_ID));
                //IntentTemp.putExtra(ExtraResource.USER_LOGIN, getIntent().getStringExtra(ExtraResource.USER_LOGIN));
                //IntentTemp.putExtra(ExtraResource.CURRENT_DOCTOR_ID, getIntent().getStringExtra(ExtraResource.CURRENT_DOCTOR_ID));
                //startActivity(IntentTemp);
            } else {
                ExtraResource.showErrorDialog(R.string.incorrect_date_format, this);
            }
        } else {
            ExtraResource.showErrorDialog(R.string.error_field_required, this);
        }
    }

    public static void checkPrivateKey(String key){
        if(diseaseService.insertHistoryInDB(currentHistory, key)){
            isInserted = true;
        } else {
            isInserted = false;
        }
    }

    private Boolean isStringParsibleToDate(String str){
        try {
            Date date = format.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    private Date parseStringToDate(String str){
        Date date;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            return null;
        }
        return date;//.
    }
}
