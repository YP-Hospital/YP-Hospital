package com.example.mary.hospital.Model;

public class Certificate {
    public static final String DATABASE_TABLE = "certificates";
    public static final String ID_COLUMN = "id";
    public static final String OPEN_KEY_COLUMN = "open_key";
    public static final String SIGNATURE_COLUMN = "signature";
    public static final String DOCTOR_ID_COLUMN = "doctor_id";

    private Integer id;
    private String openKey;
    private String signature;
    private Integer doctorID;

    public Certificate() {
    }

    public Certificate(Integer id, String openKey, String signature, Integer doctorID) {
        this.id = id;
        this.openKey = openKey;
        this.signature = signature;
        this.doctorID = doctorID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenKey() {
        return openKey;
    }

    public void setOpenKey(String openKey) {
        this.openKey = openKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }
}
