package com.example.mary.hospital.Service.Impl;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.mary.hospital.Connection.Connector;
import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Service.CertificateService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CertificateServiceImpl implements CertificateService {
    private int booleanAnswer = 0;
    private int dataAnswer = 1;
    private String separator = "]\\[";
    private Context context;

    public CertificateServiceImpl(Context context) {
        this.context = context;
    }

    public List<Certificate> getAllCertificates() {
        String query = "select " + Certificate.DATABASE_TABLE + " *";
        return getCertificates(query);
    }

    public Map<String, Certificate> getAllCertificatesWithUsersNames() {
        String query = "select " + Certificate.DATABASE_TABLE + " *";
        List<Certificate> certificates = getCertificates(query);
        return null;
    }

    private void formListOfCertificates(List<Certificate> users, List<String> words) {
        Boolean isID = false, isOpenKey = false, isSignature = false, isDoctorID = false ;
        int i;
        for (i = 0; !words.get(i).equals("0."); i++) {
            switch (words.get(i)) {
                case Certificate.ID_COLUMN:
                    isID = true;
                    break;
                case Certificate.OPEN_KEY_COLUMN:
                    isOpenKey = true;
                    break;
                case Certificate.SIGNATURE_COLUMN:
                    isSignature = true;
                    break;
                case Certificate.DOCTOR_ID_COLUMN:
                    isDoctorID = true;
                    break;
            }
        }
        Certificate certificate = null;
        for (;  i<words.size() ; i++) {
            if (words.get(i).matches("[0-9]+.")) {
                certificate = new Certificate();
                continue;
            }
            if (isID) {
                certificate.setId(Integer.valueOf(words.get(i++)));
            }
            if (isOpenKey) {
                certificate.setOpenKey(words.get(i++));
            }
            if (isSignature) {
                certificate.setSignature(words.get(i++));
            }
            if (isDoctorID) {
                certificate.setDoctorID(Integer.valueOf(words.get(i++)));
            }
            users.add(certificate);
        }
    }

    @Nullable
    private List<Certificate> getCertificates(String query) {
        List<Certificate> certificates = null;
        try {
            String result = getAnswerFromServerForQuery(query).get(dataAnswer);
            certificates = stringToCertificates(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return certificates;
    }

    private List<Certificate> stringToCertificates(String answerFromServer) {
        List<Certificate> certificates = new ArrayList<>();
        try {
            List<String> words = new ArrayList<>(Arrays.asList(answerFromServer.split(separator)));
            Boolean isAllFields = words.get(booleanAnswer).equals("*");
            if (isAllFields) {
                for (int i = 2; i < words.size(); i++) {
                    certificates.add(new Certificate(Integer.valueOf(words.get(i++)), words.get(i++),
                                        words.get(i++), Integer.valueOf(words.get(i++))));
                }
            } else {
                formListOfCertificates(certificates, words);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificates;
    }

    private List<String> getAnswerFromServerForQuery(String query) throws InterruptedException, ExecutionException {
        return new Connector(context).execute(query).get();
    }
}