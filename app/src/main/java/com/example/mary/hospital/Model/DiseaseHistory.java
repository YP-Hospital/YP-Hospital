package com.example.mary.hospital.Model;

public class DiseaseHistory {

    public static final String DATABASE_TABLE = "disease_histories";
    public static final String ID_COLUMN = "id";
    public static final String TEXT_COLUMN = "text";

    private Integer id;
    private String text;

    public DiseaseHistory(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public DiseaseHistory(String text) {
        this.text = text;
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
}
