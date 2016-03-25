package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mary.hospital.Adapters.ItemAdapter;
import com.example.mary.hospital.Adapters.ItemCertificatesAdapter;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.Certificate;
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
        final Map<String, Certificate> outputText = certificateService.getAllCertificatesWithUsersNames();
        final List<String> usersNames = new ArrayList<>(outputText.keySet());
        ListView listView = (ListView) findViewById(R.id.listOfUsersListView);
        listView.setFocusable(true);
        listView.setAdapter(new ItemCertificatesAdapter(this, R.layout.activity_certificates, outputText, usersNames));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Certificate cert = outputText.get(usersNames.get(position));
                AlertDialog dialog = DialogShowSertificate.getDialog(CertificatesActivity.this, cert);
                dialog.show();
            }
        });
    }
}
