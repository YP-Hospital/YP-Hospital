package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mary.hospital.CurrentPerson;
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
    private  String isEditable;
    private String currentDoctorID;
    String historyOwnerPatientID;
    private static String patientID;
    private static String userLogin;
    private static String patientLogin;
    private static String userRole;
    private static Intent intentTemp;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_disease_history);
        userService = new UserServiceImpl(this);
        diseaseService = new DiseaseHistoryServiceImpl(this);
        currentDoctorID = getIntent().getStringExtra(ExtraResource.CURRENT_DOCTOR_ID);
        if (currentDoctorID != null)
            currentDoctorName = userService.getUserById(Integer.parseInt(currentDoctorID)).getName();
        isEditable = getIntent().getStringExtra(ExtraResource.IS_EDITABLE);
        format = new SimpleDateFormat(DiseaseHistory.DATE_FORMAT);
        currentHistoryID = getIntent().getIntExtra(ExtraResource.DISEASE_ID, 0);
        diseaseName = ((EditText) findViewById(R.id.editDiseaseNameEditText));
        openDate = ((EditText) findViewById(R.id.editDiseaseOpenDateEditText));
        closeDate = ((EditText) findViewById(R.id.editDiseaseCloseDateEditText));
        text = ((EditText) findViewById(R.id.editDiseaseTextEditText));
        patientID = getIntent().getStringExtra(ExtraResource.PATIENT_ID);
        userLogin = getIntent().getStringExtra(ExtraResource.USER_LOGIN);
        currentDoctorID = getIntent().getStringExtra(ExtraResource.CURRENT_DOCTOR_ID);
        userRole = getIntent().getStringExtra(ExtraResource.USER_ROLE);
        patientLogin = getIntent().getStringExtra(ExtraResource.PATIENT_LOGIN);
        historyOwnerPatientID = getIntent().getStringExtra(ExtraResource.PATIENT_ID);
        String doctorID = getIntent().getStringExtra(ExtraResource.CURRENT_DOCTOR_ID);
        if (currentHistoryID != 0) {
            fillFields();
        }
        TextView e = (TextView) findViewById(R.id.textView3);
        e.setVisibility(View.INVISIBLE);
        currentHistory = diseaseService.getHistoryById(currentHistoryID);
        if(isEditable.equals("false")){
            diseaseName.setEnabled(false);
            openDate.setEnabled(false);
            closeDate.setEnabled(false);
            text.setEnabled(false);
            Button b = (Button) findViewById(R.id.editDiseaseSaveButton);
            b.setText("Signature");
            e.setVisibility(View.VISIBLE);
            e.setText("Last modified by " + currentHistory.getLastModifiedBy());
        } else {
            e.setVisibility(View.GONE);
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
        if(isEditable.equals("true")) {
            String diseaseNameS = diseaseName.getText().toString();
            String openDateS = openDate.getText().toString();
            String closeDateS = closeDate.getText().toString();
            String textS = text.getText().toString();
            Integer idi = Integer.parseInt(historyOwnerPatientID);
            intentTemp = new Intent(this, UserActivity.class);
            intentTemp.putExtra(ExtraResource.PATIENT_ID, CurrentPerson.getPatientID());
            intentTemp.putExtra(ExtraResource.USER_LOGIN, CurrentPerson.getUserLogin());
            intentTemp.putExtra(ExtraResource.CURRENT_DOCTOR_ID, CurrentPerson.getCurrentDoctorID());
            String user = CurrentPerson.getUserRole();
            intentTemp.putExtra(ExtraResource.USER_ROLE, CurrentPerson.getUserRole());
            intentTemp.putExtra(ExtraResource.PATIENT_LOGIN, CurrentPerson.getPatientLogin());
            if (diseaseNameS.isEmpty() || openDateS.isEmpty() || closeDateS.isEmpty() || textS.isEmpty()) {
                ExtraResource.showErrorDialog(R.string.error_name_exist, DiseaseActivity.this);
            } else if (isStringParsibleToDate(openDateS) && isStringParsibleToDate(closeDateS)) {
                currentHistory = new DiseaseHistory(diseaseNameS, parseStringToDate(openDateS),
                        parseStringToDate(closeDateS), textS, idi, currentDoctorName);
                final AlertDialog dialog = DialogEnterPrivateKey.getDialog(this);
                dialog.show();
                Button c = (Button) dialog.findViewById(R.id.button5);
                Button b = (Button) dialog.findViewById(R.id.button7);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) dialog.findViewById(R.id.editText);
                        String key = editText.getText().toString();
                        if (diseaseService.updateHistoryInDB(currentHistory, Integer.valueOf(currentDoctorID), key)) {
                            startActivity(intentTemp);
                        } else {
                            ExtraResource.showErrorDialog(R.string.wrong_key, v.getContext());
                        }
                        dialog.dismiss();
                    }
                });
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else {
                ExtraResource.showErrorDialog(R.string.error_field_required, this);
            }
        } else {
            final AlertDialog dialog = DialogShowSignature.getDialog(this, currentHistory.getSignatureOfLastModified());
            dialog.show();
            Button b = (Button) dialog.findViewById(R.id.button5);
            Button c = (Button) dialog.findViewById(R.id.button7);
            b.setVisibility(View.GONE);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
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
