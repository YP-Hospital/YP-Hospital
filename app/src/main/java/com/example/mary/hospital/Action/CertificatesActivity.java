package com.example.mary.hospital.Action;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.hospital.Dialogs.DialogAreYouSure;
import com.example.mary.hospital.Dialogs.DialogEnterPrivateKey;
import com.example.mary.hospital.Dialogs.DialogShowCertificate;
import com.example.mary.hospital.Dialogs.DialogUniversal;
import com.example.mary.hospital.ExtraResource;
import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.R;
import com.example.mary.hospital.Service.CertificateService;
import com.example.mary.hospital.Service.Impl.CertificateServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CertificatesActivity extends AppCompatActivity {

    private CertificateService certificateService;
    private Map<User, Certificate> certificatesAndNames;
    private List<User> users;
    private List<String> usersNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates);
        certificateService = new CertificateServiceImpl(CertificatesActivity.this);
        initVariables();
        createListView();
        createButton();
    }

    private void initVariables(){
        certificatesAndNames = certificateService.getAllCertificatesWithUsersNames();
        users = new ArrayList<>(certificatesAndNames.keySet());
        usersNames = getTitles(users);
        TextView textView = (TextView)findViewById(R.id.certificatesTextView);
        textView.setText(R.string.certificates);
    }

    private void createButton(){
        Button signature = (Button) findViewById(R.id.certificatesButton);
        signature.setText(R.string.signature);
        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = DialogUniversal.getDialog(CertificatesActivity.this, R.string.signature, certificateService.getAllCertificatesSignature());
                dialog.show();
                Button ok = (Button) dialog.findViewById(R.id.dialogEnterKeyOkButton);
                Button cancel = (Button) dialog.findViewById(R.id.dialogEnterKeyCancelButton);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
               cancel.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                   }
               });
            }
        });
    }

    private void createListView(){
        final Map<User, Certificate> outputText = certificatesAndNames;
        final List<User> users = this.users;
        final List<String> usersNames = this.usersNames;
        ListView listView = (ListView) findViewById(R.id.certificatesListView);
        listView.setFocusable(true);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, usersNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Certificate cert = outputText.get(users.get(position));
                showDialogCertificate(cert);
            }
        });
        if(ExtraResource.getCurrentUserRole().equals(Role.Admin)) {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                    String itemToDelete = arg0.getItemAtPosition(pos).toString();
                    Certificate cert = (outputText.get(users.get(pos)));
                    showDialogAndDeleteCertificate(cert, adapter, itemToDelete, pos);
                    return true;
                }
            });
        }
    }

    private List<String> getTitles(List<User> users){
        List<String> userNames = new ArrayList<String>();
        for(User user: users){
            userNames.add(user.getName());
        }
        return userNames;
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

    private void showDialogAndDeleteCertificate(final Certificate certificate, final ArrayAdapter<String> adapter, final String name, final int pos){
        String dialogText = getString(R.string.are_you_sure_that_you_want_to_delete_certificate_of) + ' ' + name + " ?";
        final AlertDialog dialog = DialogEnterPrivateKey.getDialog(CertificatesActivity.this);
        dialog.show();
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogEnterKeyCancelButton);
        Button okButton = (Button) dialog.findViewById(R.id.dialogEnterKeyOkButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextKey = (EditText) dialog.findViewById(R.id.dialogEnterKeyEditText);
                EditText editTextPassword = (EditText) dialog.findViewById(R.id.dialogEnterKeyEditText);
                String key = editTextKey.getText().toString();
                String password = editTextPassword.getText().toString();
                if(certificateService.deleteCertificate(certificate.getId(), key, password)){//
                    users.remove(pos);
                    usersNames.remove(pos);
                    dialog.dismiss();
                    createListView();
                } else {
                    dialog.dismiss();
                    Toast.makeText(v.getContext(), R.string.wrong_key, Toast.LENGTH_LONG).show();
                }
                //adapter.remove(name);
                //adapter.notifyDataSetChanged();
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


