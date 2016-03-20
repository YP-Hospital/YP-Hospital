package com.example.mary.hospital.Service.Impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mary.hospital.Connection.Connector;
import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Service.DiseaseHistoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiseaseHistoryServiceImpl implements DiseaseHistoryService {
    private int booleanAnswer = 0;
    private int dataAnswer = 1;
    private String separator = "|";
    private Context context;

    public DiseaseHistoryServiceImpl(Context context) {
        this.context = context;
    }

    public Boolean addHistoryInDB(DiseaseHistory history) {
        String query = history.getStringToInsert();
        return useQuery(query);
    }

    public Boolean updateHistoryInDB(DiseaseHistory history) {
        String query = "update " + DiseaseHistory.DATABASE_TABLE + " " + DiseaseHistory.TEXT_COLUMN
                        + " " + history.getText() + history.getId();
        return useQuery(query);
    }

    public List<DiseaseHistory> getAllHistories() {
        String query = " select " + DiseaseHistory.DATABASE_TABLE + " *";
        return getDiseaseHistories(query);
    }

    public List<DiseaseHistory> getAllUsersHistory(User user) {
        String query = " select " + DiseaseHistory.DATABASE_TABLE + " * where " + DiseaseHistory.PATIENT_ID_COLUMN
                        + " " + user.getId();
        return getDiseaseHistories(query);
    }

    private List<DiseaseHistory> getDiseaseHistories(String query) {
        List<DiseaseHistory> histories = null;
        try {
            String result = getAnswerFromServerForQuery(query).get(dataAnswer);
            histories = stringToHistories(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return histories;
    }

    private List<DiseaseHistory> stringToHistories(String answerFromServer) {
        List<DiseaseHistory> histories = new ArrayList<>();
        List<String> words = new ArrayList<>(Arrays.asList(answerFromServer.split(separator)));
        Boolean isAllFields = words.get(0).equals("*");
        if (isAllFields) {
            for (int i = 2; i < words.size(); i++) {
                histories.add(new DiseaseHistory(Integer.getInteger(words.get(i++)), words.get(i++), Integer.valueOf(words.get(i++))));
            }
        } else {
            formListOfHistories(histories, words);
        }
        return histories;
    }

    private void formListOfHistories(List<DiseaseHistory> histories, List<String> words) {
        Boolean isID = false, isText = false, isPatient = false;
        int i;
        for (i = 0; !words.get(i).equals("0."); i++) {
            switch (words.get(i)) {
                case DiseaseHistory.ID_COLUMN:
                    isID = true;
                    break;
                case DiseaseHistory.TEXT_COLUMN:
                    isText = true;
                    break;
                case DiseaseHistory.PATIENT_ID_COLUMN:
                    isPatient = true;
                    break;
            }
        }
        DiseaseHistory diseaseHistory = null;
        for (; i < words.size(); i++) {
            if (words.get(i).matches("[0-9]+.")) {
                diseaseHistory = new DiseaseHistory();
                continue;
            }
            if (isID) {
                diseaseHistory.setId(Integer.valueOf(words.get(i++)));
            }
            if (isText) {
                diseaseHistory.setText(words.get(i++));
            }
            if (isPatient) {
                diseaseHistory.setPatientID(Integer.valueOf(words.get(i++)));
            }
            histories.add(diseaseHistory);
        }
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

    private List<String> getAnswerFromServerForQuery(String query) throws InterruptedException, ExecutionException {
        return new Connector(context).execute(query).get();
    }
}
