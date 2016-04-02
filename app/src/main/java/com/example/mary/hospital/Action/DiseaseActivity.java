package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mary.hospital.Dialogs.DialogEnterPrivateKey;
import com.example.mary.hospital.Dialogs.DialogShowSignature;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.Model.Role;
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
    private static EditText diseaseName;
    private static EditText openDate;
    private static EditText closeDate;
    private static EditText text;
    private Integer currentHistoryID;
    private static DiseaseHistory currentHistory;
    private SimpleDateFormat format;
    private static String userName;
    private Boolean isEditableActivity;
    private int userID;
    private static int patientID;
    private static Role userRole;
    private static Intent intentTemp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_disease_history);
        getUserInfoAndInitVariables();
        if (currentHistoryID != 0) { fillFields();}
        setActivityEditableOrNot();
    }

    private void setActivityEditableOrNot(){
        TextView textView = (TextView) findViewById(R.id.editDiseaseLastModifiedTextView);
        if(!isEditableActivity){
            diseaseName.setEnabled(false);
            openDate.setEnabled(false);
            closeDate.setEnabled(false);
            text.setEnabled(false);
            Button b = (Button) findViewById(R.id.editDiseaseSaveButton);
            b.setText(R.string.signature);
            textView.setVisibility(View.VISIBLE);
            String lastModified = getString(R.string.last_modified_by) + ' ' + currentHistory.getLastModifiedBy();
            textView.setText(lastModified);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void getUserInfoAndInitVariables(){
        userService = new UserServiceImpl(this);
        diseaseService = new DiseaseHistoryServiceImpl(this);
        diseaseName = ((EditText) findViewById(R.id.editDiseaseNameEditText));
        openDate = ((EditText) findViewById(R.id.editDiseaseOpenDateEditText));
        closeDate = ((EditText) findViewById(R.id.editDiseaseCloseDateEditText));
        text = ((EditText) findViewById(R.id.editDiseaseTextEditText));
        format = new SimpleDateFormat(DiseaseHistory.DATE_FORMAT);
        currentHistoryID = getIntent().getIntExtra(ExtraResource.DISEASE_ID, 0);
        userID = ExtraResource.getCurrentUserId();
        userName = ExtraResource.getCurrentUserName();
        userRole = ExtraResource.getCurrentUserRole();
        isEditableActivity = getIntent().getBooleanExtra(ExtraResource.IS_EDITABLE, true);
        patientID = getIntent().getIntExtra(ExtraResource.PATIENT_ID, 0);
        currentHistory = diseaseService.getHistoryById(currentHistoryID);
        if(currentHistory == null)
            currentHistory = new DiseaseHistory();
    }

    private void fillFields() {
        diseaseName.setText(currentHistory.getTitle());
        openDate.setText(format.format(currentHistory.getOpenDate()));
        closeDate.setText(format.format(currentHistory.getCloseDate()));
        text.setText(currentHistory.getText());
    }

    public void saveDisease(View view) throws InterruptedException {
        if(isEditableActivity) {
            String diseaseNameS = diseaseName.getText().toString();
            String openDateS = openDate.getText().toString();
            String closeDateS = closeDate.getText().toString();
            String textS = text.getText().toString();
            intentTemp = new Intent(this, UserActivity.class);
            intentTemp.putExtra(ExtraResource.PATIENT_ID, patientID);
            if (diseaseNameS.isEmpty() || openDateS.isEmpty() || closeDateS.isEmpty() || textS.isEmpty()) {
                ExtraResource.showErrorDialog(R.string.error_field_required, DiseaseActivity.this);
            } else if (isStringConvertibleToDate(openDateS) && isStringConvertibleToDate(closeDateS)) {
                currentHistory.setTitle(diseaseNameS);
                currentHistory.setOpenDate(parseStringToDate(openDateS));
                currentHistory.setCloseDate(parseStringToDate(closeDateS));
                currentHistory.setText(textS);
                currentHistory.setPatientID(patientID);
                currentHistory.setLastModifiedBy(userName);
                showDialogEnterPrivateKey();
            } else {
                ExtraResource.showErrorDialog(R.string.error_field_required, this);
            }
        } else {
           showDialogShowSignature();
        }
    }

    private void showDialogEnterPrivateKey(){
        final AlertDialog dialog = DialogEnterPrivateKey.getDialog(this);
        dialog.show();
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogEnterKeyCancelButton);
        Button okButton = (Button) dialog.findViewById(R.id.dialogEnterKeyOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) dialog.findViewById(R.id.dialogEnterKeyEditText);
                String key = editText.getText().toString();
                Boolean isAdded = false;
                if (currentHistory.getId() != null) {
                    currentHistory.setId(currentHistoryID);
                    isAdded = diseaseService.updateHistoryInDB(currentHistory, userName, key);//user
                } else {
                    isAdded = diseaseService.insertHistoryInDB(currentHistory, key);
                }
                if (isAdded) {
                    startActivity(intentTemp);
                } else {
                    ExtraResource.showErrorDialog(R.string.wrong_key, v.getContext());
                }
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showDialogShowSignature(){
        final AlertDialog dialog = DialogShowSignature.getDialog(this, currentHistory.getSignatureOfLastModified());
        dialog.show();
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogEnterKeyCancelButton);
        Button okButton = (Button) dialog.findViewById(R.id.dialogEnterKeyOkButton);
        cancelButton.setVisibility(View.GONE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private Boolean isStringConvertibleToDate(String str){
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
