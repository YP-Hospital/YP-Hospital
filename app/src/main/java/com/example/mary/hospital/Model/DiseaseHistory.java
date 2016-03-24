package com.example.mary.hospital.Model;

import com.example.mary.hospital.ExtraResource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiseaseHistory {

    public static final String DATABASE_TABLE = "disease_histories";
    public static final String ID_COLUMN = "id";
    public static final String TITLE_COLUMN = "title";
    public static final String OPEN_DATE_COLUMN = "open_date";
    public static final String CLOSE_DATE_COLUMN = "close_date";
    public static final String TEXT_COLUMN = "text";
    public static final String PATIENT_ID_COLUMN = "patient_id";
    public static final String LAST_MODIFIED_BY_COLUMN = "last_modified_by";
    public static final String SIGNATURE_OF_LAST_MODIFIED_COLUMN = "signature_of_last_modified";
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_FORMAT_FROM_DB = "yyyy-MM-dd";

    private Integer id;
    private String title;
    private Date openDate;
    private Date closeDate;
    private String text;
    private Integer patientID;
    private String lastModifiedBy;
    private String signatureOfLastModified;

    public DiseaseHistory(Integer id, String title, Date openDate, Date closeDate, String text,
                          Integer patientID, String lastModifiedBy, String signatureOfLastModified) {
        this.id = id;
        this.title = title;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.text = text;
        this.patientID = patientID;
        this.lastModifiedBy = lastModifiedBy;
        this.signatureOfLastModified = signatureOfLastModified;
    }

    public DiseaseHistory(String title, Date openDate, Date closeDate, String text, Integer patientID, String lastModifiedBy) {
        this.title = title;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.text = text;
        this.patientID = patientID;
        this.lastModifiedBy = lastModifiedBy;
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
        return "insert" + ExtraResource.separatorForSending + DATABASE_TABLE + ExtraResource.separatorForSending + title + ExtraResource.separatorForSending
                + (new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(openDate)) + ExtraResource.separatorForSending
                + (new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(closeDate))
                + ExtraResource.separatorForSending + text + ExtraResource.separatorForSending + patientID
                + ExtraResource.separatorForSending + lastModifiedBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getSignatureOfLastModified() {
        return signatureOfLastModified;
    }

    public void setSignatureOfLastModified(String signatureOfLastModified) {
        this.signatureOfLastModified = signatureOfLastModified;
    }
}
