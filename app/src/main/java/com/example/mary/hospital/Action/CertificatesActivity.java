package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mary.hospital.Dialogs.DialogAreYouSure;
import com.example.mary.hospital.Dialogs.DialogShowCertificate;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.CertificateService;
import com.example.mary.hospital.Service.Impl.CertificateServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CertificatesActivity extends AppCompatActivity {

    private CertificateService certificateService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates);
        certificateService = new CertificateServiceImpl(CertificatesActivity.this);
        createListView();
    }

    private void createListView(){
        final Map<String, Certificate> outputText = certificateService.getAllCertificatesWithUsersNames();
        final List<String> usersNames = new ArrayList<>(outputText.keySet());
        ListView listView = (ListView) findViewById(R.id.certificatesListView);
        listView.setFocusable(true);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, usersNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Certificate cert = outputText.get(usersNames.get(position));
                showDialogCertificate(cert);
            }
        });
        if(ExtraResource.getCurrentUserRole().equals(Role.Admin)) {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                    String itemToDelete = arg0.getItemAtPosition(pos).toString();
                    showDialogAndDeleteCertificate(adapter, itemToDelete);
                    return true;
                }
            });
        }
    }

    private void showDialogCertificate(Certificate cert){
        final AlertDialog dialog = DialogShowCertificate.getDialog(CertificatesActivity.this, cert);
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

    private void showDialogAndDeleteCertificate(final ArrayAdapter<String> adapter, final String name){
        String dialogText = getString(R.string.are_you_sure_that_you_want_to_delete_certificate_of) + ' ' + name + " ?";
        final AlertDialog dialog = DialogAreYouSure.getDialog(CertificatesActivity.this, dialogText);
        dialog.show();
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogEnterKeyCancelButton);
        Button okButton = (Button) dialog.findViewById(R.id.dialogEnterKeyOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteCertificate
                dialog.dismiss();
                adapter.remove(name);
                adapter.notifyDataSetChanged();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}


