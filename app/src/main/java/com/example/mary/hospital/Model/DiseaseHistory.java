package com.example.mary.hospital.Model;

public class DiseaseHistory {

    public static final String DATABASE_TABLE = "disease_histories";
    public static final String ID_COLUMN = "id";
    public static final String TEXT_COLUMN = "text";
    public static final String PATIENT_ID_COLUMN = "patient_id";

    private Integer id;
    private String text;
    private Integer patientID;

    public DiseaseHistory(Integer id, String text, Integer patientID) {
        this.id = id;
        this.text = text;
        this.patientID = patientID;
    }

    public DiseaseHistory(String text, Integer patientID) {
        this.text = text;
        this.patientID = patientID;
    }

    public DiseaseHistory() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public String getStringToInsert() {
        return "insert " + DATABASE_TABLE + " " + text
                + " " + patientID;
    }
}
