package com.example.mary.hospital.Service.Impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.mary.hospital.Connection.Connector;
import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.Model.Role;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Service.CertificateService;
import com.example.mary.hospital.Service.UserService;

import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CertificateServiceImpl implements CertificateService {
    private int booleanAnswer = 0;
    private int dataAnswer = 1;
    private String separator = "]\\[";
    private String separatorForSending = "][";
    private Context context;
    private UserService userService;

    public Boolean deleteCertificate(Integer id, String key) {
        String query = "delete" + separatorForSending + Certificate.DATABASE_TABLE + separatorForSending
                + id + separatorForSending + key;
        return useQuery(query);
    }

    public String getAllCertificatesSignature() {
        String query = "select" + separatorForSending + "signatures" + separatorForSending + "signature";
        String answer = null;
        try {
            answer = getAnswerFromServerForQuery(query).get(dataAnswer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @NonNull
    private Boolean useQuery(String query) {
        Boolean isSuccess = false;
        try {
            isSuccess = Boolean.parseBoolean(getAnswerFromServerForQuery(query).get(booleanAnswer));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public CertificateServiceImpl(Context context) {
        this.context = context;
        userService = new UserServiceImpl(context);
    }

    public List<Certificate> getAllCertificates() {
        String query = "select" + separatorForSending + Certificate.DATABASE_TABLE + separatorForSending + "*";
        return getCertificates(query);
    }

    public String getSignatureByPrivateKey(String privateKey) {
        String messageForServer = "check" + separatorForSending + privateKey;
        try {
            return getAnswerFromServerForQuery(messageForServer).get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<User, Certificate> getAllCertificatesWithUsersNames() {
        Map<User, Certificate> usersNameToCertificates = new HashMap<>();
        String query = "select" + separatorForSending + Certificate.DATABASE_TABLE + separatorForSending + "*";
        List<Certificate> certificates = getCertificates(query);
        List<User> users = userService.getAllDoctors();
        for (Certificate certificate : certificates) {
            for (User user : users) {
                if (user.getId().equals(certificate.getDoctorID())) {
                    usersNameToCertificates.put(user, certificate);
                    break;
                }
            }
        }
        return usersNameToCertificates;
    }

    public Certificate getCertificateByUser(User user) {
        String query = "select" + separatorForSending + Certificate.DATABASE_TABLE + separatorForSending + "*"
                        + separatorForSending + "where" + separatorForSending
                        + Certificate.DOCTOR_ID_COLUMN + separatorForSending + user.getId();
        return getCertificates(query).get(0);
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
                    certificates.add(new Certificate(Integer.valueOf(words.get(i++)), words.get(i++), Integer.valueOf(words.get(i++))));
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
