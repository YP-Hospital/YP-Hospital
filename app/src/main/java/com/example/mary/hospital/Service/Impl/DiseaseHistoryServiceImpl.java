package com.example.mary.hospital.Service.Impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mary.hospital.Connection.Connector;
import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.Model.User;
import com.example.mary.hospital.Service.CertificateService;
import com.example.mary.hospital.Service.DiseaseHistoryService;
import com.example.mary.hospital.Service.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class DiseaseHistoryServiceImpl implements DiseaseHistoryService {
    private int booleanAnswer = 0;
    private int dataAnswer = 1;
    private String separator = "]\\[";
    private String separatorForSending = "][";
    private Context context;
    private DateFormat dateFormat;
    private CertificateService certificateService;
    private UserService userService;

    public DiseaseHistoryServiceImpl(Context context) {
        this.context = context;
        certificateService = new CertificateServiceImpl(context);
        userService = new UserServiceImpl(context);
        dateFormat = new SimpleDateFormat(DiseaseHistory.DATE_FORMAT_FROM_DB, Locale.getDefault());
    }

    public Boolean insertHistoryInDB(DiseaseHistory history, String privateKey) {
        String query = history.getStringToInsert() + separatorForSending + privateKey;
        return useQuery(query);
    }

    /**
     * DOESN'T WORK!
     */
    public Boolean updateHistoryInDB(DiseaseHistory history, User user, String privateKey) {
        String query = "update" + separatorForSending + DiseaseHistory.DATABASE_TABLE + separatorForSending + DiseaseHistory.TITLE_COLUMN
                        + separatorForSending + DiseaseHistory.OPEN_DATE_COLUMN + separatorForSending + DiseaseHistory.CLOSE_DATE_COLUMN
                        + separatorForSending + DiseaseHistory.TEXT_COLUMN + separatorForSending + DiseaseHistory.PATIENT_ID_COLUMN
                        + separatorForSending + DiseaseHistory.LAST_MODIFIED_BY_COLUMN + separatorForSending
                        + DiseaseHistory.SIGNATURE_OF_LAST_MODIFIED_COLUMN + separatorForSending + history.getTitle()
                        + separatorForSending + history.getOpenDate() + separatorForSending + history.getCloseDate()
                        + separatorForSending + history.getText() + separatorForSending + history.getPatientID()
                        + separatorForSending + user.getName() + separatorForSending + history.getId()
                        + separatorForSending + privateKey;
        return useQuery(query);
    }

    public DiseaseHistory getHistoryById(Integer id) {
        String result = "";
        DiseaseHistory history = null;
        try {
            result = getAnswerFromServerForQuery("select" + separatorForSending + DiseaseHistory.DATABASE_TABLE
                    + separatorForSending + "*" + separatorForSending
                    + "where" + separatorForSending
                    + DiseaseHistory.ID_COLUMN + separatorForSending + id).get(dataAnswer);
            history = stringToHistories(result).get(booleanAnswer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    public List<DiseaseHistory> getAllHistories() {
        String query = "select" + separatorForSending + DiseaseHistory.DATABASE_TABLE + separatorForSending + "*";
        return getDiseaseHistories(query);
    }

    public List<DiseaseHistory> getAllUsersHistories(User user) {
        String query = "select" + separatorForSending + DiseaseHistory.DATABASE_TABLE + separatorForSending
                        + "*" + separatorForSending + "where" + separatorForSending + DiseaseHistory.PATIENT_ID_COLUMN
                        + separatorForSending + user.getId();
        return getDiseaseHistories(query);
    }

    public List<String> getTitlesOfAllUsersHistories(User user) {
        String query = "select" + separatorForSending + DiseaseHistory.DATABASE_TABLE + separatorForSending
                + DiseaseHistory.TITLE_COLUMN + separatorForSending + "where" + separatorForSending + DiseaseHistory.PATIENT_ID_COLUMN
                + separatorForSending + user.getId();
        List<String> allTitles = new ArrayList<>();
        try {
            String answer = getAnswerFromServerForQuery(query).get(dataAnswer);
            List<String> words = new ArrayList<>(Arrays.asList(answer.split(separator)));
            for (String word : words) {
                if (word.matches("[0-9]+.") || word.equals(DiseaseHistory.TITLE_COLUMN) || word.isEmpty()) {
                    continue;
                }
                allTitles.add(word);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return allTitles;
    }

    private List<DiseaseHistory> getDiseaseHistories(String query) {
        List<DiseaseHistory> histories = null;
        try {
            String result = getAnswerFromServerForQuery(query).get(dataAnswer);
            histories = stringToHistories(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return histories;
    }

    private List<DiseaseHistory> stringToHistories(String answerFromServer) throws Exception{
        List<DiseaseHistory> histories = new ArrayList<>();
        List<String> words = new ArrayList<>(Arrays.asList(answerFromServer.split(separator)));
        Boolean isAllFields = words.get(0).equals("*");
        if (isAllFields) {
            for (int i = 2; i < words.size(); i++) {
                histories.add(new DiseaseHistory(Integer.parseInt(words.get(i++)), words.get(i++),
                        dateFormat.parse(words.get(i++)), dateFormat.parse(words.get(i++)),
                        words.get(i++), Integer.valueOf(words.get(i++)), words.get(i++),
                        words.get(i++)));
            }
        } else {
            formListOfHistories(histories, words);
        }
        return histories;
    }

    private void formListOfHistories(List<DiseaseHistory> histories, List<String> words) throws Exception{
        Boolean isID = false, isTitle = false, isOpenDate = false, isCloseDate = false, isText = false,
                isPatient = false, isLastModifiedBy = false, isSignature = false;
        int i;
        for (i = 0; !words.get(i).equals("0."); i++) {
            switch (words.get(i)) {
                case DiseaseHistory.ID_COLUMN:
                    isID = true;
                    break;
                case DiseaseHistory.TITLE_COLUMN:
                    isTitle = true;
                    break;
                case DiseaseHistory.OPEN_DATE_COLUMN:
                    isOpenDate = true;
                    break;
                case DiseaseHistory.CLOSE_DATE_COLUMN:
                    isCloseDate = true;
                    break;
                case DiseaseHistory.TEXT_COLUMN:
                    isText = true;
                    break;
                case DiseaseHistory.PATIENT_ID_COLUMN:
                    isPatient = true;
                    break;
                case DiseaseHistory.LAST_MODIFIED_BY_COLUMN:
                    isLastModifiedBy = true;
                    break;
                case DiseaseHistory.SIGNATURE_OF_LAST_MODIFIED_COLUMN:
                    isSignature = true;
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
            if (isTitle) {
                diseaseHistory.setTitle(words.get(i++));
            }
            if (isOpenDate) {
                diseaseHistory.setOpenDate(dateFormat.parse(words.get(i++)));
            }
            if (isCloseDate) {
                diseaseHistory.setCloseDate(dateFormat.parse(words.get(i++)));
            }
            if (isText) {
                diseaseHistory.setText(words.get(i++));
            }
            if (isPatient) {
                diseaseHistory.setPatientID(Integer.valueOf(words.get(i++)));
            }
            if (isLastModifiedBy) {
                diseaseHistory.setLastModifiedBy(words.get(i++));
            }
            if (isSignature) {
                diseaseHistory.setSignatureOfLastModified(words.get(i++));
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
