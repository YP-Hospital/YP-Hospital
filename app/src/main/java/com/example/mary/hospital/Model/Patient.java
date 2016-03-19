package com.example.mary.hospital.Model;

public class Patient extends User {
    public static final String DOCTOR_ID_COLUMN = "doctor_id";

    Integer doctorID;

    public Patient(String login, String password, String name, Role role, Integer age, String phone, Integer doctorID) {
        super(login, password, name, role, age, phone);
        this.doctorID = doctorID;
    }

    public Patient(User user, Integer doctorID) {
        super(user.getLogin(), user.getPassword(), user.getName(), user.getRole(), user.getAge(), user.getPhone());
        this.doctorID = doctorID;
    }

    public Patient() {
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }
}
